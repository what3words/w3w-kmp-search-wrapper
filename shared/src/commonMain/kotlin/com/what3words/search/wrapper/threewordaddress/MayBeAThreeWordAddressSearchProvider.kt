package com.what3words.search.wrapper.threewordaddress

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.core.types.common.W3WResult
import com.what3words.search.wrapper.error.InvalidQueryException
import com.what3words.search.wrapper.core.SearchProvider
import com.what3words.search.wrapper.core.SearchResult
import com.what3words.search.wrapper.core.SearchResult.Companion.EXTRAS_KEY_DISTANCE_TO_FOCUS
import com.what3words.search.wrapper.core.SearchResult.Companion.EXTRAS_KEY_RANK
import com.what3words.search.wrapper.core.SearchResult.Companion.EXTRAS_KEY_SUGGESTED_ADDRESS
import com.what3words.search.wrapper.threewordaddress.helper.lettersOnly
import com.what3words.search.wrapper.threewordaddress.helper.mayBeA3WordAddress
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
    private val config: ThreeWordAddressSearchConfig,
) : SearchProvider {

    override val providerId: String = MAY_BE_THREE_WORD_ADDRESS_PROVIDER_ID

    private val autosuggestOptions by lazy { config.toAutosuggestOptions() }

    override fun canHandle(query: String): Boolean = query.mayBeA3WordAddress() != null

    override suspend fun executeSearch(query: String): W3WResult<List<SearchResult>> =
        withContext(Dispatchers.IO) {
            val maybe3wa = query.mayBeA3WordAddress()
                ?: return@withContext W3WResult.Failure(InvalidQueryException())

            when (val result = textDataSource.autosuggest(maybe3wa, autosuggestOptions)) {
                is W3WResult.Success -> W3WResult.Success(
                    // If the query is like "///index home raft"
                    if (query.startsWith(THREE_WORD_ADDRESS_PREFIX)) {
                        result.value.map { suggestion ->
                            SearchResult.ResolvedAddress(
                                query = query,
                                providerId = providerId,
                                address = suggestion.w3wAddress,
                                extras = buildMap {
                                    put(EXTRAS_KEY_RANK, suggestion.rank.toString())
                                    suggestion.distanceToFocus?.let {
                                        put(
                                            EXTRAS_KEY_DISTANCE_TO_FOCUS,
                                            it.distance.toString()
                                        )
                                    }
                                },
                            )
                        }
                    }
                    // If the query is like "index home raft"
                    else {
                        val firstSuggestion =
                            result.value.firstOrNull() ?: return@withContext W3WResult.Success(emptyList())
                        val suggestedThreeWordAddress = "$THREE_WORD_ADDRESS_PREFIX${firstSuggestion.w3wAddress.words}"
                        if (suggestedThreeWordAddress.lettersOnly() == query.lettersOnly()) {
                            listOf(
                                SearchResult.SearchSuggestion(
                                    query = query,
                                    providerId = providerId,
                                    extras = mapOf(EXTRAS_KEY_SUGGESTED_ADDRESS to suggestedThreeWordAddress),
                                )
                            )
                        } else {
                            emptyList()
                        }
                    }
                )
                is W3WResult.Failure -> W3WResult.Failure(result.error)
            }
        }
}
