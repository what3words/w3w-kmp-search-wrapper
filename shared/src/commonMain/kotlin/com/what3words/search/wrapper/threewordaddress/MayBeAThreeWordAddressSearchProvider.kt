package com.what3words.search.wrapper.threewordaddress

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.core.types.common.W3WResult
import com.what3words.search.wrapper.core.SearchProvider
import com.what3words.search.wrapper.core.SearchResult
import com.what3words.search.wrapper.core.SearchResult.Companion.EXTRAS_KEY_DISTANCE_TO_FOCUS
import com.what3words.search.wrapper.core.SearchResult.Companion.EXTRAS_KEY_SUBTITLE
import com.what3words.search.wrapper.core.SearchResult.Companion.EXTRAS_KEY_SUGGESTED_ADDRESS
import com.what3words.search.wrapper.core.SearchResult.Companion.EXTRAS_KEY_TITLE
import com.what3words.search.wrapper.core.safeW3WCall
import com.what3words.search.wrapper.error.InvalidQueryException
import com.what3words.search.wrapper.threewordaddress.helper.lettersOnly
import com.what3words.search.wrapper.threewordaddress.helper.mayBeA3WordAddress
import kotlin.concurrent.Volatile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

/** Unique identifier for the may-be three-word address search provider. */
const val MAY_BE_THREE_WORD_ADDRESS_PROVIDER_ID = "MayBeAThreeWordAddressSearchProvider"

private const val THREE_WORD_ADDRESS_PREFIX = "///"

/**
 * Search provider for loose three-word-address-like queries.
 * The query is normalized to dot-separated words before autosuggest.
 */
internal class MayBeAThreeWordAddressSearchProvider(
    private val textDataSource: W3WTextDataSource,
    @Volatile var config: MayBeAThreeWordAddressSearchConfig,
) : SearchProvider {

    override val providerId: String = MAY_BE_THREE_WORD_ADDRESS_PROVIDER_ID

    override fun canHandle(query: String): Boolean = query.mayBeA3WordAddress() != null

    override suspend fun executeSearch(query: String): W3WResult<List<SearchResult>> =
        withContext(Dispatchers.IO) {
            safeW3WCall {
                val autosuggestOptions = config.toAutosuggestOptions()
                val maybe3wa = query.mayBeA3WordAddress()
                    ?: return@safeW3WCall W3WResult.Failure(InvalidQueryException())

                when (val result = textDataSource.autosuggest(maybe3wa, autosuggestOptions)) {
                    is W3WResult.Failure -> W3WResult.Failure(result.error)

                    is W3WResult.Success -> {
                        val firstSuggestion = result.value.firstOrNull()
                            ?: return@safeW3WCall W3WResult.Success(emptyList())

                        val suggestedAddress =
                            "$THREE_WORD_ADDRESS_PREFIX${firstSuggestion.w3wAddress.words}"
                        val isMatch = suggestedAddress.lettersOnly() == query.lettersOnly()

                        val suggestions = if (isMatch) {
                            val extras = buildMap {
                                put(EXTRAS_KEY_SUGGESTED_ADDRESS, suggestedAddress)
                                put(EXTRAS_KEY_TITLE, firstSuggestion.w3wAddress.words)
                                put(EXTRAS_KEY_SUBTITLE, firstSuggestion.w3wAddress.nearestPlace)
                                firstSuggestion.distanceToFocus?.let {
                                    put(EXTRAS_KEY_DISTANCE_TO_FOCUS, it.distance.toString())
                                }
                            }
                            listOf(SearchResult.SearchSuggestion(query, providerId, extras))
                        } else {
                            emptyList()
                        }

                        W3WResult.Success(suggestions)
                    }
                }
            }
        }
}
