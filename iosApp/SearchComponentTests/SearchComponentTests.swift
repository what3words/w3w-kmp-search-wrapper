//
//  SearchComponentTests.swift
//  SearchComponentTests
//
//  Created by Dave Duprey on 11/03/2025.
//

import Testing
import CoreLocation
import W3WKotlinSearchWrapper
import w3w
@testable import SearchComponent


// MARK: - Mock

/// Mock implementation of the What3Words SDK for testing KmpSdkBridge
class MockWhat3Words: What3WordsProtocol {
    
    // Stubs for configuring return values and thrown errors
    var autosuggestResult: [W3WSdkSuggestion] = []
    var autosuggestError: Error?
    var capturedAutosuggestText: String?
    var capturedAutosuggestOptions: W3WSdkOptions?
    
    var languages: [W3WSdkLanguage] = []
    
    var convertToSquareByWordsResult: W3WSdkSquare?
    var convertToSquareByWordsError: Error?
    var capturedConvertToSquareWords: String?
    
    var convertToSquareByCoordsResult: W3WSdkSquare?
    var convertToSquareByCoordsError: Error?
    var capturedConvertToSquareCoordinates: CLLocationCoordinate2D?
    var capturedConvertToSquareLanguage: W3WSdkLanguage?
    
    var convertToCoordinatesResult: CLLocationCoordinate2D?
    var convertToCoordinatesError: Error?
    var capturedConvertToCoordinatesWords: String?
    
    var gridSectionResult: [W3WSdkLine]?
    var gridSectionError: Error?
    var capturedGridSectionSW: CLLocationCoordinate2D?
    var capturedGridSectionNE: CLLocationCoordinate2D?
    
    var versionResults: [W3WSdkModule: String] = [:]
    
    func autosuggest(text: String, options: W3WSdkOptions?) throws -> [W3WSdkSuggestion] {
        capturedAutosuggestText = text
        capturedAutosuggestOptions = options
        if let error = autosuggestError { throw error }
        return autosuggestResult
    }
    
    func availableLanguages() -> [W3WSdkLanguage] {
        return languages
    }
    
    func convertToSquare(coordinates: CLLocationCoordinate2D, language: W3WSdkLanguage) throws -> W3WSdkSquare? {
        capturedConvertToSquareCoordinates = coordinates
        capturedConvertToSquareLanguage = language
        if let error = convertToSquareByCoordsError { throw error }
        return convertToSquareByCoordsResult
    }
    
    func convertToSquare(words: String) throws -> W3WSdkSquare? {
        capturedConvertToSquareWords = words
        if let error = convertToSquareByWordsError { throw error }
        return convertToSquareByWordsResult
    }
    
    func convertToCoordinates(words: String) throws -> CLLocationCoordinate2D? {
        capturedConvertToCoordinatesWords = words
        if let error = convertToCoordinatesError { throw error }
        return convertToCoordinatesResult
    }
    
    func gridSection(southWest: CLLocationCoordinate2D, northEast: CLLocationCoordinate2D) throws -> [W3WSdkLine]? {
        capturedGridSectionSW = southWest
        capturedGridSectionNE = northEast
        if let error = gridSectionError { throw error }
        return gridSectionResult
    }
    
    func version(module: W3WSdkModule) -> String {
        return versionResults[module] ?? ""
    }
}


// MARK: - Tests

struct KmpSdkBridgeTests {

    // MARK: - autosuggest
    
    @Test func autosuggestReturnsSuccessWithEmptyResults() {
        let mock = MockWhat3Words()
        mock.autosuggestResult = []
        
        let bridge = KmpSdkBridge(sdk: mock)
        let result = bridge.autosuggest(input: "test.input", options: nil)
        
        #expect(mock.capturedAutosuggestText == "test.input")
        
        if let success = result as? W3WResultSuccess<NSArray> {
            let suggestions = success.value as? [W3WSuggestion]
            #expect(suggestions?.count == 0)
        } else {
            Issue.record("Expected W3WResultSuccess but got \(type(of: result))")
        }
    }
    
    @Test func autosuggestReturnsFailureOnError() {
        let mock = MockWhat3Words()
        mock.autosuggestError = W3WSdkError.coreError(message: "test error")
        
        let bridge = KmpSdkBridge(sdk: mock)
        let result = bridge.autosuggest(input: "bad", options: nil)
        
        if let failure = result as? W3WResultFailure<NSArray> {
            #expect(failure.message != nil)
        } else {
            Issue.record("Expected W3WResultFailure")
        }
    }
    
    @Test func autosuggestPassesInputTextToSdk() {
        let mock = MockWhat3Words()
        
        let bridge = KmpSdkBridge(sdk: mock)
        _ = bridge.autosuggest(input: "filled.count", options: nil)
        
        #expect(mock.capturedAutosuggestText == "filled.count")
    }
    
    
    // MARK: - availableLanguages
    
    @Test func availableLanguagesReturnsSuccess() {
        let mock = MockWhat3Words()
        mock.languages = [W3WSdkLanguage.english]
        
        let bridge = KmpSdkBridge(sdk: mock)
        let result = bridge.availableLanguages()
        
        if let success = result as? W3WResultSuccess<NSSet> {
            let langs = success.value as? Set<AnyHashable>
            #expect(langs?.isEmpty == false)
        } else {
            Issue.record("Expected W3WResultSuccess")
        }
    }
    
    @Test func availableLanguagesReturnsEmptySet() {
        let mock = MockWhat3Words()
        mock.languages = []
        
        let bridge = KmpSdkBridge(sdk: mock)
        let result = bridge.availableLanguages()
        
        if let success = result as? W3WResultSuccess<NSSet> {
            let langs = success.value as? Set<AnyHashable>
            #expect(langs?.count == 0)
        } else {
            Issue.record("Expected W3WResultSuccess")
        }
    }
    
    
    // MARK: - convertToCoordinates
    
    @Test func convertToCoordinatesReturnsSuccessWithFullAddress() {
        let mock = MockWhat3Words()
        let square = W3WSdkSquare(
            words: "filled.count.soap",
            country: try? W3WSdkCountry(code: "GB"),
            nearestPlace: "London",
            language: W3WSdkLanguage.english,
            coordinates: CLLocationCoordinate2D(latitude: 51.5, longitude: -0.1),
            bounds: try? W3WSdkBox(
                southWest: CLLocationCoordinate2D(latitude: 51.49, longitude: -0.11),
                northEast: CLLocationCoordinate2D(latitude: 51.51, longitude: -0.09)
            )
        )
        mock.convertToSquareByWordsResult = square
        
        let bridge = KmpSdkBridge(sdk: mock)
        let result = bridge.convertToCoordinates(words: "filled.count.soap")
        
        #expect(mock.capturedConvertToSquareWords == "filled.count.soap")
        
        if let success = result as? W3WResultSuccess<W3WAddress> {
            let address = success.value
            #expect(address?.words == "filled.count.soap")
            #expect(address?.nearestPlace == "London")
            #expect(address?.center?.lat == 51.5)
            #expect(address?.center?.lng == -0.1)
            #expect(address?.square?.southwest.lat == 51.49)
            #expect(address?.square?.northeast.lng == -0.09)
            #expect(address?.country.twoLetterCode == "GB")
        } else {
            Issue.record("Expected W3WResultSuccess")
        }
    }
    
    @Test func convertToCoordinatesReturnsFailureWhenNoResult() {
        let mock = MockWhat3Words()
        mock.convertToSquareByWordsResult = nil
        
        let bridge = KmpSdkBridge(sdk: mock)
        let result = bridge.convertToCoordinates(words: "not.a.word")
        
        if let failure = result as? W3WResultFailure<W3WAddress> {
            #expect(failure.message?.contains("not.a.word") == true)
        } else {
            Issue.record("Expected W3WResultFailure")
        }
    }
    
    @Test func convertToCoordinatesReturnsFailureOnError() {
        let mock = MockWhat3Words()
        mock.convertToSquareByWordsError = W3WSdkError.coreError(message: "lookup failed")
        
        let bridge = KmpSdkBridge(sdk: mock)
        let result = bridge.convertToCoordinates(words: "error.error.error")
        
        #expect(result is W3WResultFailure<W3WAddress>)
    }
    
    @Test func convertToCoordinatesHandlesNilCoordinatesAndBounds() {
        let mock = MockWhat3Words()
        let square = W3WSdkSquare(
            words: "test.words.here",
            country: try? W3WSdkCountry(code: "US"),
            nearestPlace: "New York",
            language: W3WSdkLanguage.english,
            coordinates: nil,
            bounds: nil
        )
        mock.convertToSquareByWordsResult = square
        
        let bridge = KmpSdkBridge(sdk: mock)
        let result = bridge.convertToCoordinates(words: "test.words.here")
        
        if let success = result as? W3WResultSuccess<W3WAddress> {
            #expect(success.value?.center == nil)
            #expect(success.value?.square == nil)
            #expect(success.value?.words == "test.words.here")
            #expect(success.value?.nearestPlace == "New York")
        } else {
            Issue.record("Expected W3WResultSuccess")
        }
    }
    
    @Test func convertToCoordinatesHandlesNilLanguageAndCountry() {
        let mock = MockWhat3Words()
        let square = W3WSdkSquare(
            words: "a.b.c",
            country: nil,
            nearestPlace: nil,
            language: nil,
            coordinates: nil,
            bounds: nil
        )
        mock.convertToSquareByWordsResult = square
        
        let bridge = KmpSdkBridge(sdk: mock)
        let result = bridge.convertToCoordinates(words: "a.b.c")
        
        if let success = result as? W3WResultSuccess<W3WAddress> {
            #expect(success.value?.words == "a.b.c")
            #expect(success.value?.nearestPlace == "")
            #expect(success.value?.country.twoLetterCode == "")
        } else {
            Issue.record("Expected W3WResultSuccess")
        }
    }
    
    
    // MARK: - convertTo3wa
    
    @Test func convertTo3waReturnsSuccessWithAddress() {
        let mock = MockWhat3Words()
        let square = W3WSdkSquare(
            words: "filled.count.soap",
            country: try? W3WSdkCountry(code: "GB"),
            nearestPlace: "London",
            language: W3WSdkLanguage.english,
            coordinates: CLLocationCoordinate2D(latitude: 51.5, longitude: -0.1)
        )
        mock.convertToSquareByCoordsResult = square
        
        let bridge = KmpSdkBridge(sdk: mock)
        let coords = W3WCoordinates(lat: 51.5, lng: -0.1)
        let language = W3WProprietaryLanguage(code: "en", locale: nil, name: nil, nativeName: nil)
        let result = bridge.convertTo3wa(coordinates: coords, language: language)
        
        if let success = result as? W3WResultSuccess<W3WAddress> {
            #expect(success.value?.words == "filled.count.soap")
        } else {
            Issue.record("Expected W3WResultSuccess")
        }
    }
    
    @Test func convertTo3waReturnsFailureWhenNoResult() {
        let mock = MockWhat3Words()
        mock.convertToSquareByCoordsResult = nil
        
        let bridge = KmpSdkBridge(sdk: mock)
        let coords = W3WCoordinates(lat: 0.0, lng: 0.0)
        let language = W3WProprietaryLanguage(code: "en", locale: nil, name: nil, nativeName: nil)
        let result = bridge.convertTo3wa(coordinates: coords, language: language)
        
        #expect(result is W3WResultFailure<W3WAddress>)
    }
    
    @Test func convertTo3waReturnsFailureOnError() {
        let mock = MockWhat3Words()
        mock.convertToSquareByCoordsError = W3WSdkError.coreError(message: "conversion failed")
        
        let bridge = KmpSdkBridge(sdk: mock)
        let coords = W3WCoordinates(lat: 51.5, lng: -0.1)
        let language = W3WProprietaryLanguage(code: "en", locale: nil, name: nil, nativeName: nil)
        let result = bridge.convertTo3wa(coordinates: coords, language: language)
        
        #expect(result is W3WResultFailure<W3WAddress>)
    }
    
    @Test func convertTo3waPassesCorrectCoordinatesToSdk() {
        let mock = MockWhat3Words()
        mock.convertToSquareByCoordsResult = W3WSdkSquare(words: "a.b.c", language: W3WSdkLanguage.english)
        
        let bridge = KmpSdkBridge(sdk: mock)
        let coords = W3WCoordinates(lat: 12.34, lng: 56.78)
        let language = W3WProprietaryLanguage(code: "fr", locale: nil, name: nil, nativeName: nil)
        _ = bridge.convertTo3wa(coordinates: coords, language: language)
        
        #expect(mock.capturedConvertToSquareCoordinates?.latitude == 12.34)
        #expect(mock.capturedConvertToSquareCoordinates?.longitude == 56.78)
    }
    
    
    // MARK: - gridSection
    
    @Test func gridSectionReturnsSuccessWithLines() throws {
        let mock = MockWhat3Words()
        // Grid lines must be horizontal or vertical; use a horizontal line (same latitude)
        let line = try W3WSdkLine(
            start: CLLocationCoordinate2D(latitude: 51.0, longitude: -0.2),
            end: CLLocationCoordinate2D(latitude: 51.0, longitude: -0.1)
        )
        mock.gridSectionResult = [line]
        
        let bridge = KmpSdkBridge(sdk: mock)
        let box = W3WRectangle(
            southwest: W3WCoordinates(lat: 51.0, lng: -0.2),
            northeast: W3WCoordinates(lat: 51.1, lng: -0.1)
        )
        let result = bridge.gridSection(boundingBox: box)
        
        if let success = result as? W3WResultSuccess<W3WGridSection> {
            #expect(success.value?.lines.count == 1)
            let firstLine = success.value?.lines.first
            #expect(firstLine?.start.lat == 51.0)
            #expect(firstLine?.start.lng == -0.2)
            #expect(firstLine?.end.lat == 51.0)
            #expect(firstLine?.end.lng == -0.1)
        } else {
            Issue.record("Expected W3WResultSuccess")
        }
    }
    
    @Test func gridSectionReturnsEmptyLinesWhenNil() {
        let mock = MockWhat3Words()
        mock.gridSectionResult = nil
        
        let bridge = KmpSdkBridge(sdk: mock)
        let box = W3WRectangle(
            southwest: W3WCoordinates(lat: 51.0, lng: -0.2),
            northeast: W3WCoordinates(lat: 51.1, lng: -0.1)
        )
        let result = bridge.gridSection(boundingBox: box)
        
        if let success = result as? W3WResultSuccess<W3WGridSection> {
            #expect(success.value?.lines.count == 0)
        } else {
            Issue.record("Expected W3WResultSuccess with empty lines")
        }
    }
    
    @Test func gridSectionReturnsFailureOnError() {
        let mock = MockWhat3Words()
        mock.gridSectionError = W3WSdkError.coreError(message: "grid error")
        
        let bridge = KmpSdkBridge(sdk: mock)
        let box = W3WRectangle(
            southwest: W3WCoordinates(lat: 51.0, lng: -0.2),
            northeast: W3WCoordinates(lat: 51.1, lng: -0.1)
        )
        let result = bridge.gridSection(boundingBox: box)
        
        #expect(result is W3WResultFailure<W3WGridSection>)
    }
    
    @Test func gridSectionPassesCorrectBoundingBox() {
        let mock = MockWhat3Words()
        mock.gridSectionResult = []
        
        let bridge = KmpSdkBridge(sdk: mock)
        let box = W3WRectangle(
            southwest: W3WCoordinates(lat: 10.0, lng: 20.0),
            northeast: W3WCoordinates(lat: 30.0, lng: 40.0)
        )
        _ = bridge.gridSection(boundingBox: box)
        
        #expect(mock.capturedGridSectionSW?.latitude == 10.0)
        #expect(mock.capturedGridSectionSW?.longitude == 20.0)
        #expect(mock.capturedGridSectionNE?.latitude == 30.0)
        #expect(mock.capturedGridSectionNE?.longitude == 40.0)
    }
    
    
    // MARK: - isValid3wa
    
    @Test func isValid3waReturnsTrueForValidWords() {
        let mock = MockWhat3Words()
        mock.convertToCoordinatesResult = CLLocationCoordinate2D(latitude: 51.5, longitude: -0.1)
        
        let bridge = KmpSdkBridge(sdk: mock)
        let result = bridge.isValid3wa(words: "filled.count.soap")
        
        if let success = result as? W3WResultSuccess<KotlinBoolean> {
            #expect(success.value?.boolValue == true)
        } else {
            Issue.record("Expected W3WResultSuccess")
        }
    }
    
    @Test func isValid3waReturnsFalseForNilResult() {
        let mock = MockWhat3Words()
        mock.convertToCoordinatesResult = nil
        
        let bridge = KmpSdkBridge(sdk: mock)
        let result = bridge.isValid3wa(words: "not.valid.words")
        
        if let success = result as? W3WResultSuccess<KotlinBoolean> {
            #expect(success.value?.boolValue == false)
        } else {
            Issue.record("Expected W3WResultSuccess")
        }
    }
    
    @Test func isValid3waReturnsFalseOnError() {
        let mock = MockWhat3Words()
        mock.convertToCoordinatesError = W3WSdkError.coreError(message: "invalid")
        
        let bridge = KmpSdkBridge(sdk: mock)
        let result = bridge.isValid3wa(words: "bad.bad.bad")
        
        if let success = result as? W3WResultSuccess<KotlinBoolean> {
            #expect(success.value?.boolValue == false)
        } else {
            Issue.record("Expected W3WResultSuccess")
        }
    }
    
    
    // MARK: - version
    
    @Test func versionReturnsLibraryVersion() {
        let mock = MockWhat3Words()
        mock.versionResults[.swiftInterface] = "1.2.3"
        
        let bridge = KmpSdkBridge(sdk: mock)
        let result = bridge.version(version: .library)
        
        #expect(result == "1.2.3")
    }
    
    @Test func versionReturnsDataSourceVersion() {
        let mock = MockWhat3Words()
        mock.versionResults[.engine] = "4.5.6"
        
        let bridge = KmpSdkBridge(sdk: mock)
        let result = bridge.version(version: .dataSource)
        
        #expect(result == "4.5.6")
    }
    
    @Test func versionReturnsDataVersion() {
        let mock = MockWhat3Words()
        mock.versionResults[.data] = "7.8.9"
        
        let bridge = KmpSdkBridge(sdk: mock)
        let result = bridge.version(version: .data)
        
        #expect(result == "7.8.9")
    }
    
    @Test func versionReturnsEmptyStringForUnsetModule() {
        let mock = MockWhat3Words()
        // No version set for any module
        
        let bridge = KmpSdkBridge(sdk: mock)
        let result = bridge.version(version: .library)
        
        #expect(result == "")
    }
    
    
    // MARK: - convertSquareToAddress mapping
    
    @Test func addressMappingPreservesLanguageCode() {
        let mock = MockWhat3Words()
        let square = W3WSdkSquare(
            words: "test.words.here",
            language: try? W3WSdkLanguage("fr")
        )
        mock.convertToSquareByWordsResult = square
        
        let bridge = KmpSdkBridge(sdk: mock)
        let result = bridge.convertToCoordinates(words: "test.words.here")
        
        if let success = result as? W3WResultSuccess<W3WAddress> {
            #expect(success.value?.language.w3wCode == "fr")
        } else {
            Issue.record("Expected W3WResultSuccess")
        }
    }
    
    @Test func addressMappingHandlesNilWords() {
        let mock = MockWhat3Words()
        let square = W3WSdkSquare(words: nil)
        mock.convertToSquareByWordsResult = square
        
        let bridge = KmpSdkBridge(sdk: mock)
        let result = bridge.convertToCoordinates(words: "anything")
        
        if let success = result as? W3WResultSuccess<W3WAddress> {
            #expect(success.value?.words == "")
        } else {
            Issue.record("Expected W3WResultSuccess")
        }
    }
}
