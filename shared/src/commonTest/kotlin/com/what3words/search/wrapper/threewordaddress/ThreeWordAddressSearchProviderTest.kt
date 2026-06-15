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
import kotlin.test.assertNull
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

    // ── config options ───────────────────────────────────────────────────────

    @Test
    fun executeSearch_respectsMaxResultsConfig() = runTest {
        val trackingDataSource = FakeW3WTextDataSource().apply {
            autosuggestResult = W3WResult.Success(listOf(W3WSuggestion(fakeAddress(), 1, null)))
            convertToCoordinatesResult = W3WResult.Success(fakeAddress())
        }
        val config = ThreeWordAddressSearchConfig().apply {
            maxResults = 10
        }
        val p = ThreeWordAddressSearchProvider(trackingDataSource, config)

        p.executeSearch("filled.count.soap")

        assertNotNull(trackingDataSource.lastAutosuggestOptions)
        assertEquals(10, trackingDataSource.lastAutosuggestOptions!!.nResults)
    }

    @Test
    fun executeSearch_usesSdkDefaultNResultsWhenMaxResultsNotConfigured() = runTest {
        val trackingDataSource = FakeW3WTextDataSource().apply {
            autosuggestResult = W3WResult.Success(listOf(W3WSuggestion(fakeAddress(), 1, null)))
            convertToCoordinatesResult = W3WResult.Success(fakeAddress())
        }
        val p = ThreeWordAddressSearchProvider(trackingDataSource, ThreeWordAddressSearchConfig())

        p.executeSearch("filled.count.soap")

        assertNotNull(trackingDataSource.lastAutosuggestOptions)
        // maxResults left null -> wrapper doesn't call nResults, SDK builder default (3) applies.
        assertEquals(3, trackingDataSource.lastAutosuggestOptions!!.nResults)
    }

    @Test
    fun executeSearch_respectsNFocusResultsConfig() = runTest {
        val trackingDataSource = FakeW3WTextDataSource().apply {
            autosuggestResult = W3WResult.Success(listOf(W3WSuggestion(fakeAddress(), 1, null)))
            convertToCoordinatesResult = W3WResult.Success(fakeAddress())
        }
        val config = ThreeWordAddressSearchConfig().apply {
            nFocusResults = 3
        }
        val p = ThreeWordAddressSearchProvider(trackingDataSource, config)

        p.executeSearch("filled.count.soap")

        assertNotNull(trackingDataSource.lastAutosuggestOptions)
        assertEquals(3, trackingDataSource.lastAutosuggestOptions!!.nFocusResults)
    }

    @Test
    fun executeSearch_omitsNFocusResultsWhenNotConfigured() = runTest {
        val trackingDataSource = FakeW3WTextDataSource().apply {
            autosuggestResult = W3WResult.Success(listOf(W3WSuggestion(fakeAddress(), 1, null)))
            convertToCoordinatesResult = W3WResult.Success(fakeAddress())
        }
        val p = ThreeWordAddressSearchProvider(trackingDataSource, ThreeWordAddressSearchConfig())

        p.executeSearch("filled.count.soap")

        assertNotNull(trackingDataSource.lastAutosuggestOptions)
        assertNull(trackingDataSource.lastAutosuggestOptions!!.nFocusResults)
    }

    @Test
    fun executeSearch_respectsLanguageConfig() = runTest {
        val trackingDataSource = FakeW3WTextDataSource().apply {
            autosuggestResult = W3WResult.Success(listOf(W3WSuggestion(fakeAddress(), 1, null)))
            convertToCoordinatesResult = W3WResult.Success(fakeAddress())
        }
        val config = ThreeWordAddressSearchConfig().apply {
            fallbackLanguage = W3WRFC5646Language.FR_FR
        }
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
        val config = ThreeWordAddressSearchConfig().apply {
            clippedCountries = listOf(W3WCountry("GB"), W3WCountry("US"))
        }
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
        val config = ThreeWordAddressSearchConfig().apply {
            preferLand = true
        }
        val p = ThreeWordAddressSearchProvider(trackingDataSource, config)

        p.executeSearch("filled.count.soap")

        assertNotNull(trackingDataSource.lastAutosuggestOptions)
        assertTrue(trackingDataSource.lastAutosuggestOptions!!.preferLand)
    }
}
