package com.what3words.search.wrapper.coordinates

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.core.types.common.W3WError
import com.what3words.core.types.common.W3WResult
import com.what3words.core.types.domain.W3WAddress
import com.what3words.core.types.geometry.W3WCoordinates
import com.what3words.core.types.language.W3WLanguage
import com.what3words.core.types.language.W3WRFC5646Language
import com.what3words.search.wrapper.core.SearchResult
import com.what3words.search.wrapper.error.InvalidCoordinatesException
import com.what3words.search.wrapper.fake.FakeW3WTextDataSource
import com.what3words.search.wrapper.fixtures.fakeAddress
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class CoordinatesSearchProviderTest {

    private class MockDataSource : W3WTextDataSource by FakeW3WTextDataSource() {
        var convertTo3waResult: W3WResult<W3WAddress> = W3WResult.Success(fakeAddress())
        var lastCoordinates: W3WCoordinates? = null
        var lastLanguage: W3WLanguage? = null

        override fun convertTo3wa(
            coordinates: W3WCoordinates,
            language: W3WLanguage
        ): W3WResult<W3WAddress> {
            lastCoordinates = coordinates
            lastLanguage = language
            return convertTo3waResult
        }
    }

    // --- Provider basic info ---

    @Test
    fun providerId_isCorrect() {
        val provider = CoordinatesSearchProvider(MockDataSource(), CoordinatesSearchConfig())
        assertEquals("CoordinatesSearchProvider", provider.providerId)
    }

    // --- canHandle() tests ---

    @Test
    fun canHandle_matchesDD_whenEnabled() {
        val config = CoordinatesSearchConfig().apply { enableDecimal = true }
        val provider = CoordinatesSearchProvider(MockDataSource(), config)
        
        assertTrue(provider.canHandle("51.520847, -0.195521"))
        assertTrue(provider.canHandle("N 51.520847, W 0.195521"))
        assertTrue(provider.canHandle("51.520847 N, 0.195521 W"))
    }

    @Test
    fun canHandle_rejectsDD_whenDisabled() {
        val config = CoordinatesSearchConfig().apply { enableDecimal = false }
        val provider = CoordinatesSearchProvider(MockDataSource(), config)
        
        assertFalse(provider.canHandle("51.520847, -0.195521"))
        assertFalse(provider.canHandle("N 51.520847, W 0.195521"))
        assertFalse(provider.canHandle("51.520847 N, 0.195521 W"))
    }

    @Test
    fun canHandle_matchesDDM_whenEnabled() {
        val config = CoordinatesSearchConfig().apply { enableDDM = true }
        val provider = CoordinatesSearchProvider(MockDataSource(), config)
        
        assertTrue(provider.canHandle("51° 31.2508' N, 0° 11.7312' W"))
    }

    @Test
    fun canHandle_rejectsDDM_whenDisabled() {
        val config = CoordinatesSearchConfig().apply { enableDDM = false }
        val provider = CoordinatesSearchProvider(MockDataSource(), config)
        
        assertFalse(provider.canHandle("51° 31.2508' N, 0° 11.7312' W"))
    }

    @Test
    fun canHandle_matchesDMS_whenEnabled() {
        val config = CoordinatesSearchConfig().apply { enableDMS = true }
        val provider = CoordinatesSearchProvider(MockDataSource(), config)
        
        assertTrue(provider.canHandle("51° 31' 15.04\" N, 0° 11' 43.87\" W"))
    }

    @Test
    fun canHandle_rejectsDMS_whenDisabled() {
        val config = CoordinatesSearchConfig().apply { enableDMS = false }
        val provider = CoordinatesSearchProvider(MockDataSource(), config)
        
        assertFalse(provider.canHandle("51° 31' 15.04\" N, 0° 11' 43.87\" W"))
    }

    @Test
    fun canHandle_rejectsInvalidFormats() {
        val provider = CoordinatesSearchProvider(MockDataSource(), CoordinatesSearchConfig())
        
        assertFalse(provider.canHandle("just some text"))
        assertFalse(provider.canHandle("51.5, hello"))
        assertFalse(provider.canHandle("///filled.count.soap"))
    }

    // --- executeSearch() tests ---

    @Test
    fun executeSearch_withValidDD_returnsParsedCoordinatesAndLanguage() = runTest {
        val mockDataSource = MockDataSource()
        val config = CoordinatesSearchConfig().apply { language = W3WRFC5646Language.FR_FR }
        val provider = CoordinatesSearchProvider(mockDataSource, config)

        val result = provider.executeSearch("51.52, -0.19")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(1, result.value.size)
        val addressResult = result.value.first()
        assertIs<SearchResult.ResolvedAddress>(addressResult)
        
        // Check that data source was called with correct values
        assertEquals(51.52, mockDataSource.lastCoordinates?.lat)
        assertEquals(-0.19, mockDataSource.lastCoordinates?.lng)
        assertEquals(W3WRFC5646Language.FR_FR, mockDataSource.lastLanguage)
    }

    @Test
    fun executeSearch_withValidDDPrefix_returnsParsedCoordinates() = runTest {
        val mockDataSource = MockDataSource()
        val provider = CoordinatesSearchProvider(mockDataSource, CoordinatesSearchConfig())

        val result = provider.executeSearch("S 51.52, W 0.19")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(-51.52, mockDataSource.lastCoordinates?.lat)
        assertEquals(-0.19, mockDataSource.lastCoordinates?.lng)
    }

    @Test
    fun executeSearch_withValidDDSuffix_returnsParsedCoordinates() = runTest {
        val mockDataSource = MockDataSource()
        val provider = CoordinatesSearchProvider(mockDataSource, CoordinatesSearchConfig())

        val result = provider.executeSearch("51.52 S, 0.19 W")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(-51.52, mockDataSource.lastCoordinates?.lat)
        assertEquals(-0.19, mockDataSource.lastCoordinates?.lng)
    }

    @Test
    fun executeSearch_withValidDDM_returnsParsedCoordinates() = runTest {
        val mockDataSource = MockDataSource()
        val provider = CoordinatesSearchProvider(mockDataSource, CoordinatesSearchConfig())

        val result = provider.executeSearch("51° 30.0' S, 0° 15.0' E")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        // 51 + 30/60 = 51.5, south makes it negative
        assertEquals(-51.5, mockDataSource.lastCoordinates?.lat)
        // 0 + 15/60 = 0.25, east makes it positive
        assertEquals(0.25, mockDataSource.lastCoordinates?.lng)
    }

    @Test
    fun executeSearch_withValidDMS_returnsParsedCoordinates() = runTest {
        val mockDataSource = MockDataSource()
        val provider = CoordinatesSearchProvider(mockDataSource, CoordinatesSearchConfig())

        val result = provider.executeSearch("51° 30' 0.0\" N, 0° 15' 0.0\" W")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        // 51 + 30/60 = 51.5
        assertEquals(51.5, mockDataSource.lastCoordinates?.lat)
        // 0 + 15/60 = 0.25, west makes it negative
        assertEquals(-0.25, mockDataSource.lastCoordinates?.lng)
    }

    @Test
    fun executeSearch_returnsFailure_whenDataSourceFails() = runTest {
        val mockDataSource = MockDataSource().apply {
            convertTo3waResult = W3WResult.Failure(W3WError("API Error"))
        }
        val provider = CoordinatesSearchProvider(mockDataSource, CoordinatesSearchConfig())

        val result = provider.executeSearch("51.52, -0.19")

        assertIs<W3WResult.Failure<List<SearchResult>>>(result)
        assertEquals("API Error", result.error.message)
    }

    @Test
    fun executeSearch_returnsFailure_whenCoordinatesCannotBeParsed() = runTest {
        val mockDataSource = MockDataSource()
        val provider = CoordinatesSearchProvider(mockDataSource, CoordinatesSearchConfig())

        // Pass invalid coordinates to executeSearch directly.
        // It should drop through the 'when' statement and return Failure with InvalidCoordinatesException.
        val result = provider.executeSearch("invalid input")

        assertIs<W3WResult.Failure<List<SearchResult>>>(result)
        assertIs<InvalidCoordinatesException>(result.error)
    }

    // --- InvalidCoordinatesException test ---

    @Test
    fun invalidCoordinatesException_hasCorrectMessage() {
        val exception = InvalidCoordinatesException()
        assertEquals("Invalid coordinates", exception.message)
    }
}
