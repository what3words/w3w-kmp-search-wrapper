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

    // ── space separator normalisation (allowSpaceSeparator) ───────────────────

    private fun spaceSeparatorConfig() =
        ThreeWordAddressSearchConfig().apply { allowSpaceSeparator = true }

    @Test
    fun executeSearch_withSpaceSeparatorOff_sendsRawQueryUnchanged() = runTest {
        // allowSpaceSeparator defaults to false: the query is forwarded verbatim, /// included.
        val dataSource = fakeSuccessDataSource()

        provider(dataSource = dataSource).executeSearch("///filled count soap")

        assertEquals("///filled count soap", dataSource.lastAutosuggestInput)
    }

    @Test
    fun executeSearch_vietnameseWithSpaceSeparatorOffIsSentAsIs() = runTest {
        // Vietnamese (spaced language): clients keep allowSpaceSeparator false, so the fully dotted
        // query is forwarded verbatim and its word-internal spaces are preserved.
        val dataSource = fakeSuccessDataSource()

        provider(dataSource = dataSource)
            .executeSearch("///xôi đậu.đậu tằm.vui vẻ")

        assertEquals("///xôi đậu.đậu tằm.vui vẻ", dataSource.lastAutosuggestInput)
    }

    @Test
    fun executeSearch_convertsSpaceSeparatedQueryToDots() = runTest {
        // Case 6: no dots present -> every space becomes a dot.
        val dataSource = fakeSuccessDataSource()

        provider(config = spaceSeparatorConfig(), dataSource = dataSource)
            .executeSearch("filled count soap")

        assertEquals("filled.count.soap", dataSource.lastAutosuggestInput)
    }

    @Test
    fun executeSearch_trimsSurroundingWhitespaceWhenConverting() = runTest {
        val dataSource = fakeSuccessDataSource()

        provider(config = spaceSeparatorConfig(), dataSource = dataSource)
            .executeSearch("  filled count soap  ")

        assertEquals("filled.count.soap", dataSource.lastAutosuggestInput)
    }

    @Test
    fun executeSearch_partialDottedInputIsPassedThroughUnchanged() = runTest {
        // Partial input while typing with no spaces -> nothing to convert -> passthrough.
        val dataSource = fakeSuccessDataSource()

        provider(config = spaceSeparatorConfig(), dataSource = dataSource)
            .executeSearch("///filled.cou")

        assertEquals("///filled.cou", dataSource.lastAutosuggestInput)
    }

    @Test
    fun executeSearch_makesExactlyOneAutosuggestCall() = runTest {
        val dataSource = fakeSuccessDataSource()

        provider(config = spaceSeparatorConfig(), dataSource = dataSource)
            .executeSearch("index home raft")

        assertEquals(1, dataSource.autosuggestInputs.size)
    }

    // ── unsupported multi-token spaced-language queries (simplified behaviour) ──
    // Without segmentation these queries are no longer disambiguated. They are documented here to
    // lock in the simplified single-call behaviour. Clients must keep allowSpaceSeparator false for
    // spaced languages such as Vietnamese.

    @Test
    fun executeSearch_noDotSpacedQueryIsNaivelyDottedAndNotSplit() = runTest {
        // Cases 2 & 10: no dots -> all spaces become dots (no balanced split), single call.
        val dataSource = fakeSuccessDataSource()

        provider(config = spaceSeparatorConfig(), dataSource = dataSource)
            .executeSearch("///xoi dau dau tam vui ve")

        assertEquals("///xoi.dau.dau.tam.vui.ve", dataSource.lastAutosuggestInput)
        assertEquals(1, dataSource.autosuggestInputs.size)
    }

    @Test
    fun executeSearch_dotPlusSpaceMixIsCompletedToDots() = runTest {
        // Fewer than two dots -> remaining space boundaries are filled in.
        val dataSource = fakeSuccessDataSource()

        provider(config = spaceSeparatorConfig(), dataSource = dataSource)
            .executeSearch("index.home raft")

        assertEquals("index.home.raft", dataSource.lastAutosuggestInput)
        assertEquals(1, dataSource.autosuggestInputs.size)
    }

    @Test
    fun executeSearch_spacedLanguageQueryWithSeparatorOnIsDottedNotPreserved() = runTest {
        // With allowSpaceSeparator on, every space becomes a dot. Enabling it for a spaced language
        // (Vietnamese) therefore mangles word-internal spaces — which is exactly why clients must
        // keep it off for such languages.
        val dataSource = fakeSuccessDataSource()

        provider(config = spaceSeparatorConfig(), dataSource = dataSource)
            .executeSearch("///xôi đậu.đậu tằm vui vẻ")

        assertEquals("///xôi.đậu.đậu.tằm.vui.vẻ", dataSource.lastAutosuggestInput)
        assertEquals(1, dataSource.autosuggestInputs.size)
    }

    @Test
    fun executeSearch_tooManyDotsMakesSingleCallWithNoResults() = runTest {
        // No spaces to convert -> sent as-is, one call, empty result.
        val dataSource = dataSourceReturning(autosuggestResult = W3WResult.Success(emptyList()))

        val result = provider(config = spaceSeparatorConfig(), dataSource = dataSource)
            .executeSearch("///one.two.three.four")

        assertIs<W3WResult.Success<List<SearchResult>>>(result)
        assertTrue(result.value.isEmpty())
        assertEquals("///one.two.three.four", dataSource.lastAutosuggestInput)
        assertEquals(1, dataSource.autosuggestInputs.size)
    }

    @Test
    fun executeSearch_apiFailureReturnsErrorAfterSingleCall() = runTest {
        // Case 9: failure returned after exactly one call.
        val dataSource = dataSourceReturning(
            autosuggestResult = W3WResult.Failure(W3WError("autosuggest failed"))
        )

        val result = provider(config = spaceSeparatorConfig(), dataSource = dataSource)
            .executeSearch("///xoi dau dau tam vui ve")

        assertIs<W3WResult.Failure<List<SearchResult>>>(result)
        assertEquals("autosuggest failed", result.error.message)
        assertEquals(1, dataSource.autosuggestInputs.size)
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
