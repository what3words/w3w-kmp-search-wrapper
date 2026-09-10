//
//  AppleMapSearchProvider.swift
//  SearchComponent
//
//  Created by Henry Ng on 21/4/26.
//


import MapKit
import W3WSwiftCore
import W3WSwiftComponentsSearch
import W3WKotlinSearchWrapper
import W3WSwiftCoreSdk
import Foundation

// MARK: - Apple Maps Plugin

final class AppleMapSearchConfig: SearchConfig {
    var maxResults: Int = 5
}

private final class AppleMapSearchProvider: SearchProvider {

    private let config: AppleMapSearchConfig

    init(config: AppleMapSearchConfig) {
        self.config = config
    }

    var providerId: String { "apple-map-search" }

    func canHandle(query: String) -> Bool {
        return query.count >= 2
    }

    // SKIE renames executeSearchQuery:completionHandler: to __executeSearch via .apinotes
    // (SwiftPrivate: true). The public async executeSearch(query:) wrapper is generated
    // automatically by SKIE's extension on SearchProvider.
    func __search(query: String) async throws -> W3WResult<NSArray> {
        let request = MKLocalSearch.Request()
        request.naturalLanguageQuery = query

        let response = try await MKLocalSearch(request: request).start()

        let keys = SearchResult.companion
        let suggestions = (response.mapItems)
            .prefix(config.maxResults)
            .map { item in
                SearchResult.SearchSuggestion(
                    query: query,
                    providerId: "apple-map-search",
                    extras: [
                        keys.EXTRAS_KEY_TITLE: item.name ?? query,
                        // CLLocation.distance(from:) is already in metres, which is what the
                        // extras contract expects; round to a whole number of metres.
                        // FIXME: measured from lat/lng 0,0 rather than the search focus.
                        keys.EXTRAS_KEY_DISTANCE_TO_FOCUS: item.placemark.location.map {
                            String(Int($0.distance(from: .init(latitude: 0, longitude: 0)).rounded()))
                        } ?? "0",
                        keys.EXTRAS_KEY_SUGGESTED_ADDRESS: item.placemark.name ?? "",
                        keys.EXTRAS_KEY_SUBTITLE: item.placemark.locality ?? ""
                    ]
                )
            }

        return W3WResultSuccess<NSArray>(value: NSArray(array: suggestions))
    }
}

final class AppleMapSearch: SimpleSearchPlugin<AppleMapSearchConfig, any SearchProvider> {

    static let shared = AppleMapSearch()
    override func defaultConfig() -> AppleMapSearchConfig {
        return AppleMapSearchConfig()
    }

    override func build(config: AppleMapSearchConfig, textDataSource: any W3WTextDataSource) -> any SearchProvider {
        return AppleMapSearchProvider(config: config)
    }
}
