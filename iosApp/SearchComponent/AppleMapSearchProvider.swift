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
    var focus: CLLocation?
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
                var extras = [
                    keys.EXTRAS_KEY_TITLE: item.name ?? query,
                    keys.EXTRAS_KEY_SUGGESTED_ADDRESS: item.placemark.name ?? "",
                    keys.EXTRAS_KEY_SUBTITLE: item.placemark.locality ?? ""
                ]
                // `CLLocation.distance(from:)` is already in metres, which is what the extras
                // contract expects; round to a whole number of metres.
                if let focus = config.focus, let location = item.placemark.location {
                    extras[keys.EXTRAS_KEY_DISTANCE_TO_FOCUS] =
                        String(Int(location.distance(from: focus).rounded()))
                }

                return SearchResult.SearchSuggestion(
                    query: query,
                    providerId: "apple-map-search",
                    extras: extras
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
