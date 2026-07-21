package com.what3words.search.wrapper.threewordaddress

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.core.types.common.W3WError
import com.what3words.core.types.common.W3WResult
import com.what3words.core.types.domain.W3WCountry
import com.what3words.core.types.domain.W3WSuggestion
import com.what3words.core.types.language.W3WRFC5646Language
import com.what3words.search.wrapper.core.SearchResult
import com.what3words.search.wrapper.core.SearchResult.Companion.EXTRAS_KEY_SUGGESTED_ADDRESS
import com.what3words.search.wrapper.fake.FakeW3WTextDataSource
import com.what3words.search.wrapper.fixtures.fakeAddress
import kotlinx.coroutines.test.runTest
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class MayBeAThreeWordAddressSearchProviderTest {

    private val defaultConfig = MayBeAThreeWordAddressSearchConfig()

    private fun dataSourceReturning(
        autosuggestResult: W3WResult<List<W3WSuggestion>>,
    ) = FakeW3WTextDataSource().apply {
        this.autosuggestResult = autosuggestResult
    }

    private fun successAutosuggestResult() = W3WResult.Success(
        listOf(
            W3WSuggestion(
                w3wAddress = fakeAddress(),
                rank = 1,
                distanceToFocus = null,
            )
        )
    )

    private fun fakeTrackingDataSource() = FakeW3WTextDataSource().apply {
        autosuggestResult = successAutosuggestResult()
    }

    private fun provider(
        config: MayBeAThreeWordAddressSearchConfig = defaultConfig,
        dataSource: W3WTextDataSource = dataSourceReturning(successAutosuggestResult()),
    ) = MayBeAThreeWordAddressSearchProvider(dataSource, config)

    fun canHandle_returnsTrueForValidThreeWordAddressWithSpecialCharacters() {
        val p = provider()

        assertTrue(p.canHandle("filled/count,soap"))
    }

    fun canHandle_returnsTrueForValidThreeWordAddressWithSpaces() {
        val p = provider()

        assertTrue(p.canHandle("filled count soap"))
    }

    fun canHandle_returnsTrueForValidThreeWordAddressWithSlashPrefix() {
        val p = provider()

        assertTrue(p.canHandle("///filled count soap"))
    }

    fun canHandle_returnsFalseForNormalAddressQuery() {
        val p = provider()

        assertFalse(p.canHandle("123 Main Street"))
    }

    fun canHandle_returnsFalseForShortQuery() {
        val p = provider()

        assertFalse(p.canHandle("ab"))
    }

    fun canHandle_returnsFalseForBlankQuery() {
        val p = provider()

        assertFalse(p.canHandle(""))
        assertFalse(p.canHandle("  "))
    }

    fun executeSearch_withSlashPrefix_returnsResolvedAddressesFromAutosuggest() = runTest {
        val result = provider().executeSearch("///filled count soap")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(1, result.value.size)
        val address = assertIs<SearchResult.ResolvedAddress>(result.value.first())
        assertEquals(MAY_BE_THREE_WORD_ADDRESS_PROVIDER_ID, address.providerId)
        assertEquals("filled.count.soap", address.address.words)
        assertEquals("///filled count soap", address.query)
    }

    fun executeSearch_withSlashPrefixAndSpaces_returnsResolvedAddressesFromAutosuggest() = runTest {
        val result = provider().executeSearch("/// filled count soap ")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(1, result.value.size)
        val address = assertIs<SearchResult.ResolvedAddress>(result.value.first())
        assertEquals(MAY_BE_THREE_WORD_ADDRESS_PROVIDER_ID, address.providerId)
        assertEquals("filled.count.soap", address.address.words)
        assertEquals("/// filled count soap ", address.query)
    }

    fun executeSearch_withoutSlashPrefix_returnsSuggestedAddressWhenCharactersMatch() = runTest {
        val result = provider().executeSearch("filled count soap")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(1, result.value.size)
        val suggestion = assertIs<SearchResult.SearchSuggestion>(result.value.first())
        assertEquals(MAY_BE_THREE_WORD_ADDRESS_PROVIDER_ID, suggestion.providerId)
        assertEquals("filled count soap", suggestion.query)
        assertEquals("///filled.count.soap", suggestion.extras[EXTRAS_KEY_SUGGESTED_ADDRESS])
    }

    fun executeSearch_withoutSlashPrefix_returnsEmptyWhenCharactersDoNotMatch() = runTest {
        val result = provider().executeSearch("apple banana orange")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(0, result.value.size)
    }

    fun executeSearch_returnsEmptyListWhenAutosuggestReturnsEmpty() = runTest {
        val emptyDataSource = dataSourceReturning(
            autosuggestResult = W3WResult.Success(emptyList())
        )

        val result = provider(dataSource = emptyDataSource).executeSearch("filled count soap")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertEquals(0, result.value.size)
    }

    fun executeSearch_propagatesAutosuggestFailure() = runTest {
        val failingDataSource = dataSourceReturning(
            autosuggestResult = W3WResult.Failure(W3WError("autosuggest failed"))
        )

        val result = provider(dataSource = failingDataSource).executeSearch("filled count soap")

        assertIs<W3WResult.Failure<List<SearchResult>>>(result)
        assertEquals("autosuggest failed", result.error.message)
    }

    fun executeSearch_normalisesInputBeforeAutosuggest() = runTest {
        val trackingDataSource = fakeTrackingDataSource()
        val p = provider(dataSource = trackingDataSource)

        p.executeSearch("filled-count soap")

        assertEquals("filled.count.soap", trackingDataSource.lastAutosuggestInput)
    }


    fun executeSearch_respectsLanguageConfig() = runTest {
        val trackingDataSource = fakeTrackingDataSource()
        val config = MayBeAThreeWordAddressSearchConfig().apply {
            fallbackLanguage = W3WRFC5646Language.FR_FR
        }
        val p = MayBeAThreeWordAddressSearchProvider(trackingDataSource, config)

        p.executeSearch("filled count soap")

        assertNotNull(trackingDataSource.lastAutosuggestOptions)
        assertEquals(W3WRFC5646Language.FR_FR, trackingDataSource.lastAutosuggestOptions!!.language)
    }

    fun executeSearch_respectsClipToCountryConfig() = runTest {
        val trackingDataSource = fakeTrackingDataSource()
        val config = MayBeAThreeWordAddressSearchConfig().apply {
            clippedCountries = listOf(W3WCountry("GB"), W3WCountry("US"))
        }
        val p = MayBeAThreeWordAddressSearchProvider(trackingDataSource, config)

        p.executeSearch("filled count soap")

        assertNotNull(trackingDataSource.lastAutosuggestOptions)
        assertNotNull(trackingDataSource.lastAutosuggestOptions!!.clipToCountry)
        assertEquals(2, trackingDataSource.lastAutosuggestOptions!!.clipToCountry.size)
    }

    fun executeSearch_respectsPreferLandConfig() = runTest {
        val trackingDataSource = fakeTrackingDataSource()
        val config = MayBeAThreeWordAddressSearchConfig().apply {
            preferLand = true
        }
        val p = MayBeAThreeWordAddressSearchProvider(trackingDataSource, config)

        p.executeSearch("filled count soap")

        assertNotNull(trackingDataSource.lastAutosuggestOptions)
        assertTrue(trackingDataSource.lastAutosuggestOptions!!.preferLand)
    }
}
