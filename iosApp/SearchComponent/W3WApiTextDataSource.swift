//
//  W3WApiTextDataSource.swift
//  SearchComponent
//
//  Created by Dave Duprey on 13/04/2026.
//

import Foundation
import W3WKotlinSearchWrapper


// HTTP implementation of W3WTextDataSource that calls the what3words REST API v4
class W3WApiTextDataSource: W3WTextDataSource {

  private let apiKey: String
  private let baseURL: String
  private let session: URLSession

  init(apiKey: String, baseURL: String = "https://api.what3words.com/v4") {
    self.apiKey = apiKey
    self.baseURL = baseURL
    self.session = URLSession(configuration: .default)
  }


  func autosuggest(input: String, options: W3WAutosuggestOptions?) -> W3WResult<NSArray> {
    var params: [String: String] = ["input": input]

    if let opts = options {
      params["n-results"] = String(opts.nResults)
      if opts.preferLand { params["prefer-land"] = "true" }
      if opts.includeCoordinates { params["include-coordinates"] = "true" }

      if let focus = opts.focus {
        params["focus"] = "\(focus.lat),\(focus.lng)"
      }
      if let nFocus = opts.nFocusResults {
        params["n-focus-results"] = String(nFocus.intValue)
      }
      if let lang = opts.language {
        params["language"] = lang.w3wCode
      }
      if !opts.clipToCountry.isEmpty {
        params["clip-to-country"] = opts.clipToCountry.map { $0.twoLetterCode }.joined(separator: ",")
      }
      if let box = opts.clipToBoundingBox {
        params["clip-to-bounding-box"] = "\(box.southwest.lat),\(box.southwest.lng),\(box.northeast.lat),\(box.northeast.lng)"
      }
      if let circle = opts.clipToCircle {
        params["clip-to-circle"] = "\(circle.center.lat),\(circle.center.lng),\(circle.radius.distance)"
      }
      if let polygon = opts.clipToPolygon {
        let coords = polygon.points.map { "\($0.lat),\($0.lng)" }.joined(separator: ",")
        params["clip-to-polygon"] = coords
      }
    }

    guard let data = synchronousGet(path: "/autosuggest", params: params) else {
      return W3WResultFailure(error: W3WError(message: "Network error"), message: "Network error")
    }

    do {
      let json = try JSONSerialization.jsonObject(with: data) as? [String: Any]
      let suggestions = (json?["suggestions"] as? [[String: Any]]) ?? []
      let kmpSuggestions: [W3WSuggestion] = suggestions.enumerated().map { index, s in
        let words = s["words"] as? String ?? ""
        let nearestPlace = s["nearestPlace"] as? String ?? ""
        let country = s["country"] as? String ?? ""
        let language = s["language"] as? String ?? "en"
        let distanceKm = s["distanceToFocusKm"] as? Double

        let address = W3WAddress(
          words: words,
          center: nil,
          square: nil,
          language: W3WProprietaryLanguage(code: language, locale: nil, name: nil, nativeName: nil),
          country: W3WCountry(twoLetterCode: country),
          nearestPlace: nearestPlace
        )
        let distance: W3WDistance? = distanceKm.map { W3WDistance(distance: $0 * 1000) }
        return W3WSuggestion(w3wAddress: address, rank: Int32(index), distanceToFocus: distance)
      }
      return W3WResultSuccess(value: kmpSuggestions as NSArray)
    } catch {
      return W3WResultFailure(error: W3WError(message: error.localizedDescription), message: error.localizedDescription)
    }
  }


  func availableLanguages() -> W3WResult<NSSet> {
    guard let data = synchronousGet(path: "/available-languages", params: [:]) else {
      return W3WResultFailure(error: W3WError(message: "Network error"), message: "Network error")
    }

    do {
      let json = try JSONSerialization.jsonObject(with: data) as? [String: Any]
      let langs = (json?["languages"] as? [[String: Any]]) ?? []
      let kmpLanguages: [W3WProprietaryLanguage] = langs.map { l in
        W3WProprietaryLanguage(
          code: l["code"] as? String ?? "",
          locale: l["locale"] as? String,
          name: l["name"] as? String,
          nativeName: l["nativeName"] as? String
        )
      }
      return W3WResultSuccess(value: NSSet(array: kmpLanguages))
    } catch {
      return W3WResultFailure(error: W3WError(message: error.localizedDescription), message: error.localizedDescription)
    }
  }


  func convertTo3wa(coordinates: W3WCoordinates, language: any W3WLanguage) -> W3WResult<W3WAddress> {
    let params: [String: String] = [
      "coordinates": "\(coordinates.lat),\(coordinates.lng)",
      "language": language.w3wCode
    ]

    guard let data = synchronousGet(path: "/convert-to-3wa", params: params) else {
      return W3WResultFailure(error: W3WError(message: "Network error"), message: "Network error")
    }

    return parseAddressResponse(data: data)
  }


  func convertToCoordinates(words: String) -> W3WResult<W3WAddress> {
    guard let data = synchronousGet(path: "/convert-to-coordinates", params: ["words": words]) else {
      return W3WResultFailure(error: W3WError(message: "Network error"), message: "Network error")
    }

    return parseAddressResponse(data: data)
  }


  func gridSection(boundingBox: W3WRectangle) -> W3WResult<W3WGridSection> {
    let bbox = "\(boundingBox.southwest.lat),\(boundingBox.southwest.lng),\(boundingBox.northeast.lat),\(boundingBox.northeast.lng)"

    guard let data = synchronousGet(path: "/grid-section", params: ["bounding-box": bbox]) else {
      return W3WResultFailure(error: W3WError(message: "Network error"), message: "Network error")
    }

    do {
      let json = try JSONSerialization.jsonObject(with: data) as? [String: Any]
      let rawLines = (json?["lines"] as? [[String: Any]]) ?? []
      let kmpLines: [W3WLine] = rawLines.compactMap { line in
        guard
          let start = line["start"] as? [String: Double],
          let end = line["end"] as? [String: Double],
          let sLat = start["lat"], let sLng = start["lng"],
          let eLat = end["lat"], let eLng = end["lng"]
        else { return nil }
        return W3WLine(
          start: W3WCoordinates(lat: sLat, lng: sLng),
          end: W3WCoordinates(lat: eLat, lng: eLng)
        )
      }
      return W3WResultSuccess(value: W3WGridSection(lines: kmpLines))
    } catch {
      return W3WResultFailure(error: W3WError(message: error.localizedDescription), message: error.localizedDescription)
    }
  }


  func isValid3wa(words: String) -> W3WResult<KotlinBoolean> {
    let result = convertToCoordinates(words: words)
    switch onEnum(of: result) {
    case .success:
      return W3WResultSuccess(value: KotlinBoolean(bool: true))
    case .failure:
      return W3WResultSuccess(value: KotlinBoolean(bool: false))
    }
  }


  func version(version: W3WTextDataSourceVersion) -> String? {
    switch version {
    case .library:
      return "W3WApiTextDataSource/1.0"
    default:
      return nil
    }
  }


  // MARK: - Private Helpers

  private func parseAddressResponse(data: Data) -> W3WResult<W3WAddress> {
    do {
      guard let json = try JSONSerialization.jsonObject(with: data) as? [String: Any] else {
        return W3WResultFailure(error: W3WError(message: "Invalid response"), message: "Invalid response")
      }

      if let errorMsg = (json["error"] as? [String: Any])?["message"] as? String {
        return W3WResultFailure(error: W3WError(message: errorMsg), message: errorMsg)
      }

      let words = json["words"] as? String ?? ""
      let nearestPlace = json["nearestPlace"] as? String ?? ""
      let country = json["country"] as? String ?? ""
      let language = json["language"] as? String ?? "en"

      var center: W3WCoordinates? = nil
      if let coords = json["coordinates"] as? [String: Double],
         let lat = coords["lat"], let lng = coords["lng"] {
        center = W3WCoordinates(lat: lat, lng: lng)
      }

      var square: W3WRectangle? = nil
      if let sq = json["square"] as? [String: Any],
         let sw = sq["southwest"] as? [String: Double],
         let ne = sq["northeast"] as? [String: Double],
         let swLat = sw["lat"], let swLng = sw["lng"],
         let neLat = ne["lat"], let neLng = ne["lng"] {
        square = W3WRectangle(
          southwest: W3WCoordinates(lat: swLat, lng: swLng),
          northeast: W3WCoordinates(lat: neLat, lng: neLng)
        )
      }

      let address = W3WAddress(
        words: words,
        center: center,
        square: square,
        language: W3WProprietaryLanguage(code: language, locale: nil, name: nil, nativeName: nil),
        country: W3WCountry(twoLetterCode: country),
        nearestPlace: nearestPlace
      )
      return W3WResultSuccess(value: address)
    } catch {
      return W3WResultFailure(error: W3WError(message: error.localizedDescription), message: error.localizedDescription)
    }
  }


  private func synchronousGet(path: String, params: [String: String]) -> Data? {
    var components = URLComponents(string: baseURL + path)!
    var queryItems = params.map { URLQueryItem(name: $0.key, value: $0.value) }
    queryItems.append(URLQueryItem(name: "key", value: apiKey))
    components.queryItems = queryItems

    guard let url = components.url else { return nil }

    var request = URLRequest(url: url, timeoutInterval: 10)
    request.setValue(apiKey, forHTTPHeaderField: "X-Api-Key")

    var result: Data? = nil
    let semaphore = DispatchSemaphore(value: 0)

    session.dataTask(with: request) { data, _, _ in
      result = data
      semaphore.signal()
    }.resume()

    semaphore.wait()
    return result
  }

}
