package com.what3words.search.wrapper.threewordaddress

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.core.types.common.W3WError
import com.what3words.core.types.common.W3WResult
import com.what3words.core.types.domain.W3WAddress
import com.what3words.core.types.domain.W3WCountry
import com.what3words.core.types.domain.W3WSuggestion
import com.what3words.core.types.language.W3WRFC5646Language
import com.what3words.search.wrapper.core.SearchResult
import com.what3words.search.wrapper.fake.FakeW3WTextDataSource
import com.what3words.search.wrapper.fixtures.fakeAddress
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ThreeWordAddressSearchProviderTest {

    private val defaultConfig = ThreeWordAddressSearchConfig()

    private fun fakeSuccessDataSource() = FakeW3WTextDataSource().apply {
        autosuggestResult = W3WResult.Success(
            listOf(
                W3WSuggestion(
                    w3wAddress = fakeAddress(),
                    rank = 1,
                    distanceToFocus = null,
                )
            )
        )
        convertToCoordinatesResult = W3WResult.Success(fakeAddress())
    }

    private fun dataSourceReturning(
        autosuggestResult: W3WResult<List<W3WSuggestion>>? = null,
        convertToCoordinatesResult: W3WResult<W3WAddress>? = null,
    ) = FakeW3WTextDataSource().apply {
        this.autosuggestResult = autosuggestResult
        this.convertToCoordinatesResult = convertToCoordinatesResult
    }

    private fun provider(
        config: ThreeWordAddressSearchConfig = defaultConfig,
        dataSource: W3WTextDataSource = fakeSuccessDataSource(),
    ) = ThreeWordAddressSearchProvider(dataSource, config)

    private fun suggestionWith(words: String = "filled.count.soap") =
        SearchResult.SearchSuggestion(
            query = words,
            providerId = THREE_WORD_ADDRESS_PROVIDER_ID,
            extras = mapOf("words" to words),
        )

    // ── canHandle ────────────────────────────────────────────────────────────

    @Test
    fun canHandle_returnsTrueForValidThreeWordAddress() {
        val p = provider()
        assertTrue(p.canHandle("filled.count.soap"))
    }

    @Test
    fun canHandle_returnsTrueForThreeWordAddressWithDots() {
        val p = provider()
        assertTrue(p.canHandle("filled.count.soap"))
    }

    @Test
    fun canHandle_returnsFalseForThreeWordAddressWithSpaces() {
        val p = provider()
        assertFalse(p.canHandle("filled count soap"))
    }


    @Test
    fun canHandle_returnsFalseForNormalAddressQuery() {
        val p = provider()
        assertFalse(p.canHandle("123 Main Street"))
    }

    @Test
    fun canHandle_returnsFalseForShortQuery() {
        val p = provider()
        assertFalse(p.canHandle("ab"))
    }

    @Test
    fun canHandle_returnsFalseForBlankQuery() {
        val p = provider()
        assertFalse(p.canHandle(""))
        assertFalse(p.canHandle("  "))
    }

    // ── executeSearch ─────────────────────────────────────────────────────────

    @Test
    fun executeSearch_returnsResolvedAddressesFromAutosuggest() = runTest {
        val result = provider().executeSearch("filled.count.soap")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(1, result.value.size)
        val address = assertIs<SearchResult.ResolvedAddress>(result.value.first())
        assertEquals(THREE_WORD_ADDRESS_PROVIDER_ID, address.providerId)
        assertEquals("filled.count.soap", address.address.words)
    }

    @Test
    fun executeSearch_returnsEmptyListWhenAutosuggestReturnsEmpty() = runTest {
        val emptyDataSource = dataSourceReturning(
            autosuggestResult = W3WResult.Success(emptyList())
        )
        val result = provider(dataSource = emptyDataSource).executeSearch("filled.count.soap")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(0, result.value.size)
    }

    @Test
    fun executeSearch_propagatesAutosuggestFailure() = runTest {
        val failingDataSource = dataSourceReturning(
            autosuggestResult = W3WResult.Failure(W3WError("autosuggest failed"))
        )

        val result = provider(dataSource = failingDataSource).executeSearch("filled.count.soap")

        assertIs<W3WResult.Failure<List<SearchResult>>>(result)
        assertEquals("autosuggest failed", result.error.message)
    }

    @Test
    fun executeSearch_returnsFailureOnException() = runTest {
        val exceptionDataSource = FakeW3WTextDataSource().apply {
            autosuggestException = RuntimeException("unexpected error")
        }

        val result = provider(dataSource = exceptionDataSource).executeSearch("filled.count.soap")

        assertIs<W3WResult.Failure<List<SearchResult>>>(result)
    }

    @Test
    fun executeSearch_setsCorrectProviderId() = runTest {
        val result = provider().executeSearch("filled.count.soap")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        val address = assertIs<SearchResult.ResolvedAddress>(result.value.first())
        assertEquals(THREE_WORD_ADDRESS_PROVIDER_ID, address.providerId)
    }

    @Test
    fun executeSearch_preservesQueryInResult() = runTest {
        val result = provider().executeSearch("filled.count.soap")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        val address = assertIs<SearchResult.ResolvedAddress>(result.value.first())
        assertEquals("filled.count.soap", address.query)
    }

    // ── resolve ───────────────────────────────────────────────────────────────

    @Test
    fun resolve_returnsFailureWhenWordsMissingFromExtras() = runTest {
        val suggestion = SearchResult.SearchSuggestion(
            query = "filled.count.soap",
            providerId = THREE_WORD_ADDRESS_PROVIDER_ID,
            extras = emptyMap(),
        )

        val result = provider().resolve(suggestion)

        assertIs<W3WResult.Failure<SearchResult.ResolvedAddress>>(result)
        assertTrue(result.error.message!!.contains("words"))
    }

    @Test
    fun resolve_returnsFailureWhenConvertToCoordinatesFails() = runTest {
        val failingDataSource = dataSourceReturning(
            convertToCoordinatesResult = W3WResult.Failure(W3WError("conversion failed"))
        )
        val suggestion = suggestionWith()

        val result = provider(dataSource = failingDataSource).resolve(suggestion)

        assertIs<W3WResult.Failure<SearchResult.ResolvedAddress>>(result)
        assertEquals("conversion failed", result.error.message)
    }

    @Test
    fun resolve_returnsResolvedAddressOnSuccess() = runTest {
        val suggestion = suggestionWith()

        val result = provider().resolve(suggestion)

        assertIs<W3WResult.Success<SearchResult.ResolvedAddress>>(result)
        assertEquals(THREE_WORD_ADDRESS_PROVIDER_ID, result.value.providerId)
        assertEquals("filled.count.soap", result.value.address.words)
    }

    @Test
    fun resolve_propagatesConvertToCoordinatesFailure() = runTest {
        val failingDataSource = dataSourceReturning(
            convertToCoordinatesResult = W3WResult.Failure(W3WError("conversion failed"))
        )
        val suggestion = suggestionWith()

        val result = provider(dataSource = failingDataSource).resolve(suggestion)

        assertIs<W3WResult.Failure<SearchResult.ResolvedAddress>>(result)
        assertEquals("conversion failed", result.error.message)
    }

    @Test
    fun resolve_usesWordsFromExtrasEvenWhenQueryDiffers() = runTest {
        val trackingDataSource = FakeW3WTextDataSource().apply {
            convertToCoordinatesResult = W3WResult.Success(fakeAddress())
        }
        val suggestion = SearchResult.SearchSuggestion(
            query = "different.query.value",
            providerId = THREE_WORD_ADDRESS_PROVIDER_ID,
            extras = mapOf("words" to "filled.count.soap"),
        )

        provider(dataSource = trackingDataSource).resolve(suggestion)

        assertEquals("filled.count.soap", trackingDataSource.lastConvertToCoordinatesWords)
    }

    @Test
    fun resolve_returnsFailureOnException() = runTest {
        val exceptionDataSource = FakeW3WTextDataSource().apply {
            convertToCoordinatesException = RuntimeException("unexpected error")
        }
        val suggestion = suggestionWith()

        val result = provider(dataSource = exceptionDataSource).resolve(suggestion)

        assertIs<W3WResult.Failure<SearchResult.ResolvedAddress>>(result)
    }

    @Test
    fun resolve_doesNotCallAutosuggest() = runTest {
        val trackingDataSource = FakeW3WTextDataSource().apply {
            convertToCoordinatesResult = W3WResult.Success(fakeAddress())
        }
        val suggestion = suggestionWith()

        provider(dataSource = trackingDataSource).resolve(suggestion)

        assertEquals(null, trackingDataSource.lastAutosuggestInput)
    }

    // ── config options ───────────────────────────────────────────────────────

    @Test
    fun executeSearch_respectsMaxResultsConfig() = runTest {
        val trackingDataSource = FakeW3WTextDataSource().apply {
            autosuggestResult = W3WResult.Success(listOf(W3WSuggestion(fakeAddress(), 1, null)))
            convertToCoordinatesResult = W3WResult.Success(fakeAddress())
        }
        val config = defaultConfig.copy(maxResults = 10)
        val p = ThreeWordAddressSearchProvider(trackingDataSource, config)

        p.executeSearch("filled.count.soap")

        assertNotNull(trackingDataSource.lastAutosuggestOptions)
        assertEquals(10, trackingDataSource.lastAutosuggestOptions!!.nResults)
        assertEquals(10, trackingDataSource.lastAutosuggestOptions!!.nFocusResults)
    }

    @Test
    fun executeSearch_forVietnameseLanguage_removesInternalSpacesBeforeAutosuggest() = runTest {
        val trackingDataSource = FakeW3WTextDataSource().apply {
            autosuggestResult = W3WResult.Success(emptyList())
        }
        val p = ThreeWordAddressSearchProvider(
            trackingDataSource,
            defaultConfig.copy(language = W3WRFC5646Language.VI),
        )

        p.executeSearch("tinh xảo.hòa hợp.đường tàu")

        assertEquals("tinhxảo.hòahợp.đườngtàu", trackingDataSource.lastAutosuggestInput)
    }

    @Test
    fun executeSearch_respectsLanguageConfig() = runTest {
        val trackingDataSource = FakeW3WTextDataSource().apply {
            autosuggestResult = W3WResult.Success(listOf(W3WSuggestion(fakeAddress(), 1, null)))
            convertToCoordinatesResult = W3WResult.Success(fakeAddress())
        }
        val config = defaultConfig.copy(language = W3WRFC5646Language.FR_FR)
        val p = ThreeWordAddressSearchProvider(trackingDataSource, config)

        p.executeSearch("filled.count.soap")

        assertNotNull(trackingDataSource.lastAutosuggestOptions)
        assertEquals(W3WRFC5646Language.FR_FR, trackingDataSource.lastAutosuggestOptions!!.language)
    }

    @Test
    fun executeSearch_respectsClipToCountryConfig() = runTest {
        val trackingDataSource = FakeW3WTextDataSource().apply {
            autosuggestResult = W3WResult.Success(listOf(W3WSuggestion(fakeAddress(), 1, null)))
            convertToCoordinatesResult = W3WResult.Success(fakeAddress())
        }
        val config = defaultConfig.copy(clipToCountry = listOf(W3WCountry("GB"), W3WCountry("US")))
        val p = ThreeWordAddressSearchProvider(trackingDataSource, config)

        p.executeSearch("filled.count.soap")

        assertNotNull(trackingDataSource.lastAutosuggestOptions)
        assertNotNull(trackingDataSource.lastAutosuggestOptions!!.clipToCountry)
        assertEquals(2, trackingDataSource.lastAutosuggestOptions!!.clipToCountry.size)
    }

    @Test
    fun executeSearch_respectsPreferLandConfig() = runTest {
        val trackingDataSource = FakeW3WTextDataSource().apply {
            autosuggestResult = W3WResult.Success(listOf(W3WSuggestion(fakeAddress(), 1, null)))
            convertToCoordinatesResult = W3WResult.Success(fakeAddress())
        }
        val config = defaultConfig.copy(preferLand = true)
        val p = ThreeWordAddressSearchProvider(trackingDataSource, config)

        p.executeSearch("filled.count.soap")

        assertNotNull(trackingDataSource.lastAutosuggestOptions)
        assertTrue(trackingDataSource.lastAutosuggestOptions!!.preferLand)
    }
}
