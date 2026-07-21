//
//  SearchTree.swift
//  SearchComponent
//
//  Created by Dave Duprey on 11/03/2025.
//

import W3WSwiftCore
import W3WSwiftComponentsSearch


/// Dummy data to test the search component
class SearchTree: W3WSearchTreeProtocol {
  
  
  /// dummy search based on text - only responds to "test"
  func search(text: String, start: Int? = nil, length: Int? = nil, completion: @escaping W3WSearchResultsClosure) {
    if text == "test" {
      completion(generateRows(text: text), nil)
    }
  }
  
  
  /// search based on a search result
  func search(item: W3WSearchItem, start: Int? = nil, length: Int? = nil, completion: @escaping W3WSearchResultsClosure) {
    completion(generateRows(text: item.description), nil)
  }
  
  
  /// dummy detail data
  func detail(item: W3WSearchItem, completion: @escaping W3WSearchResultClosure) {
    completion(W3WSearchItem(value: .streetAddress(W3WStreetAddress(primary: "hi", secondary: "lo"))), nil)
  }
  
  
  /// Generate test data
  func generateRows(text: String) -> [W3WSearchItem] {
    let rows = [
      W3WSearchItem(value: .header("Example Header")),
      W3WSearchItem(value: .streetAddress(W3WStreetAddress(address: [.address: text, .city: "CityVille", .postCode: "HA 1HA1"]))),
      W3WSearchItem(value: .suggestion(W3WBaseSuggestion(words: "filled.count.soap", nearestPlace: text, distanceToFocus: W3WBaseDistance(meters: 200.0)))),
      W3WSearchItem(value: .streetAddress(W3WStreetAddress(address: [.address: text, .city: "CityVille", .postCode: "HA 1HA1"]))),
      W3WSearchItem(value: .streetAddress(W3WStreetAddress(address: [.address: "address", .city: text, .postCode: "HA 1HA1"]))),
      W3WSearchItem(value: .streetAddress(W3WStreetAddress(address: [.address: "address", .city: "CityVille", .postCode: text.prefix(6).description]))),
      W3WSearchItem(value: .notice("This is a notice row, used to give little messages of encouragement to the user, a place to communicate stuff like \"\(text)\"")),
    ]

    return rows
  }
  
  
}
