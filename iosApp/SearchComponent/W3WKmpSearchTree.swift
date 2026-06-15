import MapKit
import W3WSwiftCore
import W3WSwiftComponentsSearch
import W3WKotlinSearchWrapper
import W3WSwiftCoreSdk
import Foundation

private enum APIKeys {
    static let w3w = "KEY_HERE"
    static let googlePlaces = "KEY_HERE"
}

// MARK: - Search Tree
class W3WKmpSearchTree: W3WSearchTreeProtocol {

  let searchClient: W3WSearchClient

    init() {
        
        searchClient = W3WSearchClient(textDataSource: KmpSdkBridge(sdk: W3WSdk.sdk))
        
        searchClient.addPlugin(plugin: BritishNationalGridSearch.shared.asHandle(), priority: 9)
        searchClient.addPlugin(plugin: CoordinatesSearch.shared.asHandle(), priority: 8)
        searchClient.addPlugin(plugin: ThreeWordAddressSearch.shared.asHandle(config: ThreeWordAddressSearchConfig(clippedCountries: [], fallbackLanguage: nil, preferLand: false, focus: nil, clipToCircle: nil, clipToBoundingBox: nil, clipToPolygon: nil, includeCoordinates: true, maxResults: nil, nFocusResults: nil, allowSpaceSeparator: true)), priority: 7)
        searchClient.addPlugin(plugin: MayBeAThreeWordAddressSearch.shared.asHandle(), priority: 1)
        
        if (false) {
            searchClient.addPlugin(
                plugin: GooglePlacesSearch.shared.asHandle(config:
                                                            GooglePlacesConfig(
                                                                apiKey: APIKeys.googlePlaces,
                                                                language: .enGb,
                                                                useSessionTokens: true,
                                                                minQueryLength: 3,
                                                                maxResults: 5,
                                                                locationBias: nil,
                                                                origin: nil,
                                                                includedRegionCodes: [],
                                                                headers: [:]
                                                            )),
                priority: 1
            )
        } else {
            searchClient.addPlugin(plugin: AppleMapSearch.shared.asHandle(), priority: 1)
        }
        
    }

  func search(text: String, start: Int?, length: Int?, completion: @escaping W3WSearchResultsClosure) {
    Task {
      do {
        let result: W3WResult<NSArray> = try await searchClient.search(query: text)
        var list = [W3WSearchItem]()

        switch onEnum(of: result) {
        case .failure(let failure):
          let error = W3WError.message(failure.message ?? "Search failed")
          list.append(W3WSearchItem(value: .notice("Error: \(failure.message ?? "?")")))
          completion(list, error)

        case .success(let success):
          var items: [SearchResult] = []
          if let array = success.value {
            for element in array {
              if let searchResult = element as? SearchResult {
                items.append(searchResult)
              }
            }
          }

          for item in items {
            if let suggestion = item as? SearchResult.SearchSuggestion {
              let words = suggestion.suggestedAddressOrNull() ?? suggestion.title ?? suggestion.query
              let nearestPlace = suggestion.subtitle ?? ""
              list.append(W3WSearchItem(
                value: .suggestion(W3WBaseSuggestion(words: words, nearestPlace: nearestPlace, distanceToFocus: nil)),
                container: false,
                custom: suggestion
              ))
            } else if let resolved = item as? SearchResult.ResolvedAddress {
              let address = resolved.address
              list.append(W3WSearchItem(
                value: .suggestion(W3WBaseSuggestion(words: address.words, nearestPlace: address.nearestPlace, distanceToFocus: nil)),
                custom: resolved
              ))
            }
          }
          completion(list, nil)
        }

      } catch {
        let w3wError = W3WError.message(error.localizedDescription)
        completion([W3WSearchItem(value: .notice(error.localizedDescription))], w3wError)
      }
    }
  }


  func search(item: W3WSearchItem, start: Int?, length: Int?, completion: @escaping W3WSearchResultsClosure) {
    Task {
      // Already resolved — return the address directly
      if let resolved = item.custom as? SearchResult.ResolvedAddress {
        let address = resolved.address
        completion([W3WSearchItem(value: .suggestion(W3WBaseSuggestion(words: address.words, nearestPlace: address.nearestPlace, distanceToFocus: nil)))], nil)
        return
      }

      guard let suggestion = item.custom as? SearchResult.SearchSuggestion else {
        completion([], nil)
        return
      }

      do {
        let result = try await searchClient.resolve(data: suggestion)
        switch onEnum(of: result) {
        case .failure(let failure):
          let error = W3WError.message(failure.message ?? "Resolve failed")
          completion([W3WSearchItem(value: .notice("Error: \(failure.message ?? "?")"))], error)
        case .success(let success):
          let address = success.value?.address
          completion([W3WSearchItem(value: .suggestion(W3WBaseSuggestion(words: address?.words ?? "", nearestPlace: address?.nearestPlace ?? "", distanceToFocus: nil)))], nil)
        }
      } catch {
        let w3wError = W3WError.message(error.localizedDescription)
        completion([W3WSearchItem(value: .notice(error.localizedDescription))], w3wError)
      }
    }
  }


  func detail(item: W3WSearchItem, completion: @escaping W3WSearchResultClosure) {
    Task {
      // Already resolved — return directly
      if let resolved = item.custom as? SearchResult.ResolvedAddress {
        let address = resolved.address
        completion(W3WSearchItem(value: .suggestion(W3WBaseSuggestion(words: address.words, nearestPlace: address.nearestPlace, distanceToFocus: nil))), nil)
        return
      }

      // SearchSuggestion — resolve to get full address before emitting the event
      guard let suggestion = item.custom as? SearchResult.SearchSuggestion else {
        completion(item, nil)
        return
      }

      do {
        let result = try await searchClient.resolve(data: suggestion)
        switch onEnum(of: result) {
        case .failure(let failure):
          let error = W3WError.message(failure.message ?? "Resolve failed")
          completion(W3WSearchItem(value: .notice("Error: \(failure.message ?? "?")")), error)
        case .success(let success):
          let address = success.value?.address
          completion(W3WSearchItem(value: .suggestion(W3WBaseSuggestion(words: address?.words ?? "", nearestPlace: address?.nearestPlace ?? "", distanceToFocus: nil))), nil)
        }
      } catch {
        let w3wError = W3WError.message(error.localizedDescription)
        completion(W3WSearchItem(value: .notice(error.localizedDescription)), w3wError)
      }
    }
  }

}
