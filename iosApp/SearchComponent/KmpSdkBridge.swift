//
//  KmpSdkBridge.swift
//  SearchComponent
//
//  Created by Dave Duprey on 13/04/2026.
//

import CoreLocation
import Foundation
import W3WSwiftCoreSdk
import W3WKotlinSearchWrapper
import w3w


// Facade — picks the right implementation based on how it's initialised
class KmpSdkBridge: W3WTextDataSource {

  private let impl: any W3WTextDataSource

  ///  uses bundled w3w data, no network required
  init(sdk: any What3WordsProtocol) {
    impl = SdkDataSource(sdk: sdk)
  }

  /// Online mode — uses the what3words HTTP API
  init(apiKey: String, baseURL: String = "https://api.what3words.com/v4") {
    impl = HttpDataSource(apiKey: apiKey, baseURL: baseURL)
  }

  func autosuggest(input: String, options: W3WAutosuggestOptions?) -> W3WResult<NSArray> {
    impl.autosuggest(input: input, options: options)
  }

  func availableLanguages() -> W3WResult<NSSet> {
    impl.availableLanguages()
  }

  func convertTo3wa(coordinates: W3WCoordinates, language: any W3WKotlinSearchWrapper.W3WLanguage) -> W3WResult<W3WAddress> {
    impl.convertTo3wa(coordinates: coordinates, language: language)
  }

  func convertToCoordinates(words: String) -> W3WResult<W3WAddress> {
    impl.convertToCoordinates(words: words)
  }

  func gridSection(boundingBox: W3WRectangle) -> W3WResult<W3WGridSection> {
    impl.gridSection(boundingBox: boundingBox)
  }

  func isValid3wa(words: String) -> W3WResult<KotlinBoolean> {
    impl.isValid3wa(words: words)
  }

  func version(version: W3WTextDataSourceVersion) -> String? {
    impl.version(version: version)
  }
}


// MARK: - SDK implementation

private class SdkDataSource: W3WTextDataSource {

  private let sdk: any What3WordsProtocol

  init(sdk: any What3WordsProtocol) {
    self.sdk = sdk
  }

  func autosuggest(input: String, options: W3WAutosuggestOptions?) -> W3WResult<NSArray> {
    do {
      let suggestions = try sdk.autosuggest(text: input, options: convertOptions(options))
      let result: [W3WKotlinSearchWrapper.W3WSuggestion] = suggestions.enumerated().map { index, s in
        let address = W3WAddress(
          words: s.words ?? "", center: nil, square: nil,
          language: W3WProprietaryLanguage(code: s.language?.code ?? "en", locale: nil, name: nil, nativeName: nil),
          country: W3WCountry(twoLetterCode: s.country?.code ?? ""),
          nearestPlace: s.nearestPlace ?? ""
        )
        return W3WSuggestion(w3wAddress: address, rank: Int32(index),
                             distanceToFocus: s.distanceToFocus.map { W3WDistance(distance: $0.meters) })
      }
      return W3WResultSuccess(value: result as NSArray)
    } catch {
      return W3WResultFailure(error: W3WError(message: error.localizedDescription), message: error.localizedDescription)
    }
  }

  func availableLanguages() -> W3WResult<NSSet> {
    let langs = sdk.availableLanguages().map {
      W3WProprietaryLanguage(code: $0.code, locale: $0.locale, name: nil, nativeName: nil)
    }
    return W3WResultSuccess(value: NSSet(array: langs))
  }

  func convertTo3wa(coordinates: W3WCoordinates, language: any W3WKotlinSearchWrapper.W3WLanguage) -> W3WResult<W3WAddress> {
    do {
      let cl = CLLocationCoordinate2D(latitude: coordinates.lat, longitude: coordinates.lng)
      if let square = try sdk.convertToSquare(coordinates: cl, language: W3WSdkLanguage(language.w3wCode)) {
        return W3WResultSuccess(value: convertSquareToAddress(square))
      }
      return W3WResultFailure(error: W3WError(message: "No result"), message: "No result")
    } catch {
      return W3WResultFailure(error: W3WError(message: error.localizedDescription), message: error.localizedDescription)
    }
  }

  func convertToCoordinates(words: String) -> W3WResult<W3WAddress> {
    do {
      if let square = try sdk.convertToSquare(words: words) {
        return W3WResultSuccess(value: convertSquareToAddress(square))
      }
      return W3WResultFailure(error: W3WError(message: "No result"), message: "No result")
    } catch {
      return W3WResultFailure(error: W3WError(message: error.localizedDescription), message: error.localizedDescription)
    }
  }

  func gridSection(boundingBox: W3WRectangle) -> W3WResult<W3WGridSection> {
    do {
      let sw = CLLocationCoordinate2D(latitude: boundingBox.southwest.lat, longitude: boundingBox.southwest.lng)
      let ne = CLLocationCoordinate2D(latitude: boundingBox.northeast.lat, longitude: boundingBox.northeast.lng)
      let lines = (try sdk.gridSection(southWest: sw, northEast: ne) ?? []).map {
        W3WLine(start: W3WCoordinates(lat: $0.start.latitude, lng: $0.start.longitude),
                end: W3WCoordinates(lat: $0.end.latitude, lng: $0.end.longitude))
      }
      return W3WResultSuccess(value: W3WGridSection(lines: lines))
    } catch {
      return W3WResultFailure(error: W3WError(message: error.localizedDescription), message: error.localizedDescription)
    }
  }

  func isValid3wa(words: String) -> W3WResult<KotlinBoolean> {
    let valid = (try? sdk.convertToCoordinates(words: words)) != nil
    return W3WResultSuccess(value: KotlinBoolean(bool: valid))
  }

  func version(version: W3WTextDataSourceVersion) -> String? {
    switch version {
    case .library:    return sdk.version(module: W3WSdkModule.swiftInterface)
    case .dataSource: return sdk.version(module: W3WSdkModule.engine)
    case .data:       return sdk.version(module: W3WSdkModule.data)
    }
  }

  private func convertSquareToAddress(_ square: W3WSdkSquare) -> W3WAddress {
    let center = square.coordinates.map { W3WCoordinates(lat: $0.latitude, lng: $0.longitude) }
    let rect = square.bounds.map {
      W3WRectangle(southwest: W3WCoordinates(lat: $0.southWest.latitude, lng: $0.southWest.longitude),
                   northeast: W3WCoordinates(lat: $0.northEast.latitude, lng: $0.northEast.longitude))
    }
    return W3WAddress(
      words: square.words ?? "", center: center, square: rect,
      language: W3WProprietaryLanguage(code: square.language?.code ?? "en", locale: square.language?.locale, name: nil, nativeName: nil),
      country: W3WCountry(twoLetterCode: square.country?.code ?? ""),
      nearestPlace: square.nearestPlace ?? ""
    )
  }

  private func convertOptions(_ options: W3WAutosuggestOptions?) -> W3WSdkOptions? {
    guard let options = options else { return nil }
    var o = W3WSdkOptions()
    o = o.numberOfResults(Int(options.nResults)).preferLand(options.preferLand)
    if let focus = options.focus { o = o.focus(CLLocationCoordinate2D(latitude: focus.lat, longitude: focus.lng)) }
    if let n = options.nFocusResults { o = o.numberFocusResults(n.intValue) }
    if let lang = options.language, let sdkLang = try? W3WSdkLanguage(lang.w3wCode) { o = o.language(sdkLang) }
    if let box = options.clipToBoundingBox,
       let b = try? W3WSdkBox(southWest: CLLocationCoordinate2D(latitude: box.southwest.lat, longitude: box.southwest.lng),
                               northEast: CLLocationCoordinate2D(latitude: box.northeast.lat, longitude: box.northeast.lng)) { o = o.clip(to: b) }
    if let circle = options.clipToCircle,
       let c = try? W3WSdkCircle(center: CLLocationCoordinate2D(latitude: circle.center.lat, longitude: circle.center.lng),
                                  radius: W3WSdkDistance(meters: circle.radius.distance)) { o = o.clip(to: c) }
    if let polygon = options.clipToPolygon,
       let p = try? W3WSdkPolygon(points: polygon.points.map { CLLocationCoordinate2D(latitude: $0.lat, longitude: $0.lng) }) { o = o.clip(to: p) }
    for country in options.clipToCountry { if let c = try? W3WSdkCountry(code: country.twoLetterCode) { o = o.clip(to: c) } }
    return o
  }
}


// MARK: - HTTP API implementation

private class HttpDataSource: W3WTextDataSource {

  private let apiKey: String
  private let baseURL: String
  private let session = URLSession(configuration: .default)

  init(apiKey: String, baseURL: String) {
    self.apiKey = apiKey
    self.baseURL = baseURL
  }

  func autosuggest(input: String, options: W3WAutosuggestOptions?) -> W3WResult<NSArray> {
    var params: [String: String] = ["input": input]
    if let opts = options {
      params["n-results"] = String(opts.nResults)
      if opts.preferLand { params["prefer-land"] = "true" }
      if opts.includeCoordinates { params["include-coordinates"] = "true" }
      if let focus = opts.focus { params["focus"] = "\(focus.lat),\(focus.lng)" }
      if let n = opts.nFocusResults { params["n-focus-results"] = String(n.intValue) }
      if let lang = opts.language { params["language"] = lang.w3wCode }
      if !opts.clipToCountry.isEmpty { params["clip-to-country"] = opts.clipToCountry.map { $0.twoLetterCode }.joined(separator: ",") }
      if let box = opts.clipToBoundingBox { params["clip-to-bounding-box"] = "\(box.southwest.lat),\(box.southwest.lng),\(box.northeast.lat),\(box.northeast.lng)" }
      if let circle = opts.clipToCircle { params["clip-to-circle"] = "\(circle.center.lat),\(circle.center.lng),\(circle.radius.distance)" }
      if let polygon = opts.clipToPolygon { params["clip-to-polygon"] = polygon.points.map { "\($0.lat),\($0.lng)" }.joined(separator: ",") }
    }
    guard let data = get(path: "/autosuggest", params: params) else {
      return W3WResultFailure(error: W3WError(message: "Network error"), message: "Network error")
    }
    do {
      let json = try JSONSerialization.jsonObject(with: data) as? [String: Any]
      let suggestions: [W3WKotlinSearchWrapper.W3WSuggestion] = ((json?["suggestions"] as? [[String: Any]]) ?? []).enumerated().map { index, s in
        let address = W3WAddress(
          words: s["words"] as? String ?? "", center: nil, square: nil,
          language: W3WProprietaryLanguage(code: s["language"] as? String ?? "en", locale: nil, name: nil, nativeName: nil),
          country: W3WCountry(twoLetterCode: s["country"] as? String ?? ""),
          nearestPlace: s["nearestPlace"] as? String ?? ""
        )
        return W3WSuggestion(w3wAddress: address, rank: Int32(index),
                             distanceToFocus: (s["distanceToFocusKm"] as? Double).map { W3WDistance(distance: $0 * 1000) })
      }
      return W3WResultSuccess(value: suggestions as NSArray)
    } catch {
      return W3WResultFailure(error: W3WError(message: error.localizedDescription), message: error.localizedDescription)
    }
  }

  func availableLanguages() -> W3WResult<NSSet> {
    guard let data = get(path: "/available-languages", params: [:]) else {
      return W3WResultFailure(error: W3WError(message: "Network error"), message: "Network error")
    }
    do {
      let json = try JSONSerialization.jsonObject(with: data) as? [String: Any]
      let langs = ((json?["languages"] as? [[String: Any]]) ?? []).map {
        W3WProprietaryLanguage(code: $0["code"] as? String ?? "", locale: $0["locale"] as? String,
                                name: $0["name"] as? String, nativeName: $0["nativeName"] as? String)
      }
      return W3WResultSuccess(value: NSSet(array: langs))
    } catch {
      return W3WResultFailure(error: W3WError(message: error.localizedDescription), message: error.localizedDescription)
    }
  }

  func convertTo3wa(coordinates: W3WCoordinates, language: any W3WKotlinSearchWrapper.W3WLanguage) -> W3WResult<W3WAddress> {
    guard let data = get(path: "/convert-to-3wa",
                         params: ["coordinates": "\(coordinates.lat),\(coordinates.lng)", "language": language.w3wCode]) else {
      return W3WResultFailure(error: W3WError(message: "Network error"), message: "Network error")
    }
    return parseAddress(data)
  }

  func convertToCoordinates(words: String) -> W3WResult<W3WAddress> {
    guard let data = get(path: "/convert-to-coordinates", params: ["words": words]) else {
      return W3WResultFailure(error: W3WError(message: "Network error"), message: "Network error")
    }
    return parseAddress(data)
  }

  func gridSection(boundingBox: W3WRectangle) -> W3WResult<W3WGridSection> {
    let bbox = "\(boundingBox.southwest.lat),\(boundingBox.southwest.lng),\(boundingBox.northeast.lat),\(boundingBox.northeast.lng)"
    guard let data = get(path: "/grid-section", params: ["bounding-box": bbox]) else {
      return W3WResultFailure(error: W3WError(message: "Network error"), message: "Network error")
    }
    do {
      let json = try JSONSerialization.jsonObject(with: data) as? [String: Any]
      let lines = ((json?["lines"] as? [[String: Any]]) ?? []).compactMap { line -> W3WKotlinSearchWrapper.W3WLine? in
        guard let s = line["start"] as? [String: Double], let e = line["end"] as? [String: Double],
              let sLat = s["lat"], let sLng = s["lng"], let eLat = e["lat"], let eLng = e["lng"] else { return nil }
        return W3WLine(start: W3WCoordinates(lat: sLat, lng: sLng), end: W3WCoordinates(lat: eLat, lng: eLng))
      }
      return W3WResultSuccess(value: W3WGridSection(lines: lines))
    } catch {
      return W3WResultFailure(error: W3WError(message: error.localizedDescription), message: error.localizedDescription)
    }
  }

  func isValid3wa(words: String) -> W3WResult<KotlinBoolean> {
    guard let data = get(path: "/convert-to-coordinates", params: ["words": words]) else {
      return W3WResultSuccess(value: KotlinBoolean(bool: false))
    }
    switch onEnum(of: parseAddress(data)) {
    case .success: return W3WResultSuccess(value: KotlinBoolean(bool: true))
    case .failure: return W3WResultSuccess(value: KotlinBoolean(bool: false))
    }
  }

  func version(version: W3WTextDataSourceVersion) -> String? {
    version == .library ? "KmpSdkBridge-HTTP/1.0" : nil
  }

  private func parseAddress(_ data: Data) -> W3WResult<W3WAddress> {
    do {
      guard let json = try JSONSerialization.jsonObject(with: data) as? [String: Any] else {
        return W3WResultFailure(error: W3WError(message: "Invalid response"), message: "Invalid response")
      }
      if let msg = (json["error"] as? [String: Any])?["message"] as? String {
        return W3WResultFailure(error: W3WError(message: msg), message: msg)
      }
      var center: W3WCoordinates? = nil
      if let c = json["coordinates"] as? [String: Double], let lat = c["lat"], let lng = c["lng"] {
        center = W3WCoordinates(lat: lat, lng: lng)
      }
      var square: W3WRectangle? = nil
      if let sq = json["square"] as? [String: Any],
         let sw = sq["southwest"] as? [String: Double], let ne = sq["northeast"] as? [String: Double],
         let swLat = sw["lat"], let swLng = sw["lng"], let neLat = ne["lat"], let neLng = ne["lng"] {
        square = W3WRectangle(southwest: W3WCoordinates(lat: swLat, lng: swLng),
                               northeast: W3WCoordinates(lat: neLat, lng: neLng))
      }
      return W3WResultSuccess(value: W3WAddress(
        words: json["words"] as? String ?? "", center: center, square: square,
        language: W3WProprietaryLanguage(code: json["language"] as? String ?? "en", locale: nil, name: nil, nativeName: nil),
        country: W3WCountry(twoLetterCode: json["country"] as? String ?? ""),
        nearestPlace: json["nearestPlace"] as? String ?? ""
      ))
    } catch {
      return W3WResultFailure(error: W3WError(message: error.localizedDescription), message: error.localizedDescription)
    }
  }

  private func get(path: String, params: [String: String]) -> Data? {
    var components = URLComponents(string: baseURL + path)!
    var items = params.map { URLQueryItem(name: $0.key, value: $0.value) }
    items.append(URLQueryItem(name: "key", value: apiKey))
    components.queryItems = items
    guard let url = components.url else { return nil }
    var request = URLRequest(url: url, timeoutInterval: 10)
    request.setValue(apiKey, forHTTPHeaderField: "X-Api-Key")
    var result: Data? = nil
    let semaphore = DispatchSemaphore(value: 0)
    session.dataTask(with: request) { data, _, _ in result = data; semaphore.signal() }.resume()
    semaphore.wait()
    return result
  }
}
