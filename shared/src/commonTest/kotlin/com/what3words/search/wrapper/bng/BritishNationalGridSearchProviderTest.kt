package com.what3words.search.wrapper.bng

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

class BritishNationalGridSearchProviderTest {

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
        val provider = BritishNationalGridSearchProvider(MockDataSource(), BritishNationalGridSearchConfig())
        assertEquals(BRITISH_NATIONAL_GRID_PROVIDER_ID, provider.providerId)
    }

    // --- canHandle() — OS grid reference formats ---

    @Test
    fun canHandle_withValidOSGridReference_6digits_returnsTrue() {
        val provider = BritishNationalGridSearchProvider(MockDataSource(), BritishNationalGridSearchConfig())

        assertTrue(provider.canHandle("TQ388797"))
        assertTrue(provider.canHandle("SU123456"))
        assertTrue(provider.canHandle("NZ123456"))
    }

    @Test
    fun canHandle_withValidOSGridReference_8digits_returnsTrue() {
        val provider = BritishNationalGridSearchProvider(MockDataSource(), BritishNationalGridSearchConfig())

        assertTrue(provider.canHandle("TQ38827970"))
    }

    @Test
    fun canHandle_withValidOSGridReference_10digits_returnsTrue() {
        val provider = BritishNationalGridSearchProvider(MockDataSource(), BritishNationalGridSearchConfig())

        assertTrue(provider.canHandle("TQ3882079700"))
    }

    @Test
    fun canHandle_withInvalidFirstLetter_returnsFalse() {
        val provider = BritishNationalGridSearchProvider(MockDataSource(), BritishNationalGridSearchConfig())

        // First letter must be H, S, N, O, or T
        assertFalse(provider.canHandle("AQ388797"))
        assertFalse(provider.canHandle("BQ388797"))
    }

    @Test
    fun canHandle_withWrongDigitCount_returnsFalse() {
        val provider = BritishNationalGridSearchProvider(MockDataSource(), BritishNationalGridSearchConfig())

        // 4 or 5 digits are not valid
        assertFalse(provider.canHandle("TQ3887"))
        // 7 digits are not valid
        assertFalse(provider.canHandle("TQ3882797"))
    }

    // --- canHandle() — easting/northing formats ---

    @Test
    fun canHandle_withValidEastingNorthing_returnsTrue() {
        val provider = BritishNationalGridSearchProvider(MockDataSource(), BritishNationalGridSearchConfig())

        assertTrue(provider.canHandle("538800, 179700"))
        assertTrue(provider.canHandle("538800,179700"))
    }

    @Test
    fun canHandle_withEastingNorthingAtMinimumBounds_returnsTrue() {
        val provider = BritishNationalGridSearchProvider(MockDataSource(), BritishNationalGridSearchConfig())

        assertTrue(provider.canHandle("1, 1"))
    }

    @Test
    fun canHandle_withEastingNorthingAtMaximumBounds_returnsTrue() {
        val provider = BritishNationalGridSearchProvider(MockDataSource(), BritishNationalGridSearchConfig())

        assertTrue(provider.canHandle("700000, 1300000"))
    }

    @Test
    fun canHandle_withEastingOutOfBounds_returnsFalse() {
        val provider = BritishNationalGridSearchProvider(MockDataSource(), BritishNationalGridSearchConfig())

        assertFalse(provider.canHandle("700001, 179700"))
        assertFalse(provider.canHandle("0, 179700"))
    }

    @Test
    fun canHandle_withNorthingOutOfBounds_returnsFalse() {
        val provider = BritishNationalGridSearchProvider(MockDataSource(), BritishNationalGridSearchConfig())

        assertFalse(provider.canHandle("538800, 1300001"))
        assertFalse(provider.canHandle("538800, 0"))
    }

    @Test
    fun canHandle_withNonNumericEastingNorthing_returnsFalse() {
        val provider = BritishNationalGridSearchProvider(MockDataSource(), BritishNationalGridSearchConfig())

        assertFalse(provider.canHandle("abc, def"))
    }

    // --- canHandle() — invalid inputs ---

    @Test
    fun canHandle_withPlainText_returnsFalse() {
        val provider = BritishNationalGridSearchProvider(MockDataSource(), BritishNationalGridSearchConfig())

        assertFalse(provider.canHandle("just some text"))
        assertFalse(provider.canHandle("///filled.count.soap"))
    }

    @Test
    fun canHandle_withEmptyString_returnsFalse() {
        val provider = BritishNationalGridSearchProvider(MockDataSource(), BritishNationalGridSearchConfig())

        assertFalse(provider.canHandle(""))
    }

    // --- executeSearch() tests ---

    @Test
    fun executeSearch_withValidOSGridReference_returnsResolvedAddress() = runTest {
        val mockDataSource = MockDataSource()
        val provider = BritishNationalGridSearchProvider(mockDataSource, BritishNationalGridSearchConfig())

        val result = provider.executeSearch("TQ388797")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(1, result.value.size)
        val address = result.value.first()
        assertIs<SearchResult.ResolvedAddress>(address)
        assertEquals("TQ388797", address.query)
        assertEquals(BRITISH_NATIONAL_GRID_PROVIDER_ID, address.providerId)
    }

    @Test
    fun executeSearch_withValidEastingNorthing_returnsResolvedAddress() = runTest {
        val mockDataSource = MockDataSource()
        val provider = BritishNationalGridSearchProvider(mockDataSource, BritishNationalGridSearchConfig())

        val result = provider.executeSearch("538800, 179700")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(1, result.value.size)
        assertIs<SearchResult.ResolvedAddress>(result.value.first())
    }

    @Test
    fun executeSearch_passesConfigLanguageToDataSource() = runTest {
        val mockDataSource = MockDataSource()
        val config = BritishNationalGridSearchConfig().apply { language = W3WRFC5646Language.FR_FR }
        val provider = BritishNationalGridSearchProvider(mockDataSource, config)

        provider.executeSearch("TQ388797")

        assertEquals(W3WRFC5646Language.FR_FR, mockDataSource.lastLanguage)
    }

    @Test
    fun executeSearch_passesConvertedCoordinatesToDataSource() = runTest {
        val mockDataSource = MockDataSource()
        val provider = BritishNationalGridSearchProvider(mockDataSource, BritishNationalGridSearchConfig())

        provider.executeSearch("TQ388797")

        // Verify that the data source was called with non-null coordinates
        val coords = mockDataSource.lastCoordinates
        assertTrue(coords != null)
        // TQ388797 resolves to a location in the Greater London area (approx. 51.5°N, -0.06°E)
        assertTrue(coords.lat in 51.0..52.0)
        assertTrue(coords.lng in -1.0..1.0)
    }

    @Test
    fun executeSearch_returnsFailure_whenDataSourceFails() = runTest {
        val mockDataSource = MockDataSource().apply {
            convertTo3waResult = W3WResult.Failure(W3WError("API Error"))
        }
        val provider = BritishNationalGridSearchProvider(mockDataSource, BritishNationalGridSearchConfig())

        val result = provider.executeSearch("TQ388797")

        assertIs<W3WResult.Failure<List<SearchResult>>>(result)
        assertEquals("API Error", result.error.message)
    }

    @Test
    fun executeSearch_returnsInvalidCoordinatesFailure_whenQueryCannotBeParsed() = runTest {
        val mockDataSource = MockDataSource()
        val provider = BritishNationalGridSearchProvider(mockDataSource, BritishNationalGridSearchConfig())

        val result = provider.executeSearch("invalid input")

        assertIs<W3WResult.Failure<List<SearchResult>>>(result)
        assertIs<InvalidCoordinatesException>(result.error)
    }
}
