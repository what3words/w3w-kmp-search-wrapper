//
//  What3WordsProtocol.swift
//  SearchComponent
//
//  Created by Dave Duprey on 16/04/2026.
//

import CoreLocation
import w3w


/// Protocol abstracting the What3Words SfilledDK methods used by KmpSdkBridge,
/// enabling dependency injection and mocking for tests.
protocol What3WordsProtocol {
    func autosuggest(text: String, options: W3WSdkOptions?) throws -> [W3WSdkSuggestion]
    func availableLanguages() -> [W3WSdkLanguage]
    func convertToSquare(coordinates: CLLocationCoordinate2D, language: W3WSdkLanguage) throws -> W3WSdkSquare?
    func convertToSquare(words: String) throws -> W3WSdkSquare?
    func convertToCoordinates(words: String) throws -> CLLocationCoordinate2D?
    func gridSection(southWest: CLLocationCoordinate2D, northEast: CLLocationCoordinate2D) throws -> [W3WSdkLine]?
    func version(module: W3WSdkModule) -> String
}


/// Conform the real SDK to the protocol
extension What3Words: What3WordsProtocol {}
