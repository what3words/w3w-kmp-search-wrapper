package com.what3words.search.wrapper.threewordaddress

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.core.types.common.W3WResult
import com.what3words.search.wrapper.core.ResolvableSearchProvider
import com.what3words.search.wrapper.core.SearchResult
import com.what3words.search.wrapper.core.SearchResult.Companion.EXTRAS_KEY_DISTANCE_TO_FOCUS
import com.what3words.search.wrapper.core.SearchResult.Companion.EXTRAS_KEY_RANK
import com.what3words.search.wrapper.core.SearchResult.Companion.EXTRAS_KEY_SUBTITLE
import com.what3words.search.wrapper.core.SearchResult.Companion.EXTRAS_KEY_TITLE
import com.what3words.search.wrapper.core.safeW3WCall
import com.what3words.search.wrapper.error.MissingSuggestionTitleException
import com.what3words.search.wrapper.threewordaddress.helper.isA3WordAddress
import com.what3words.search.wrapper.threewordaddress.helper.normalizeToCanonicalForm
import kotlin.concurrent.Volatile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

/** Unique identifier for the three-word address search provider. */
const val THREE_WORD_ADDRESS_PROVIDER_ID = "ThreeWordAddressSearchProvider"

private const val THREE_WORD_ADDRESS_PREFIX = "///"

/**
 * Search provider for three-word address queries.
 */
internal class ThreeWordAddressSearchProvider(
    private val textDataSource: W3WTextDataSource,
    @Volatile var config: ThreeWordAddressSearchConfig,
) : ResolvableSearchProvider {

    override val providerId: String = THREE_WORD_ADDRESS_PROVIDER_ID

    override fun canHandle(query: String): Boolean {
        val snapshot = config
        return query.startsWith(THREE_WORD_ADDRESS_PREFIX) ||
                query.isA3WordAddress(snapshot.allowSpaceSeparator)
    }

    override suspend fun executeSearch(query: String): W3WResult<List<SearchResult>> =
        withContext(Dispatchers.IO) {
            safeW3WCall {
                val autosuggestOptions = config.toAutosuggestOptions()
                val strippedQuery = query.removePrefix(THREE_WORD_ADDRESS_PREFIX)
                val canonicalQuery = strippedQuery.normalizeToCanonicalForm()
                when (val result = textDataSource.autosuggest(canonicalQuery, autosuggestOptions)) {
                    is W3WResult.Success -> W3WResult.Success(
                        result.value.map { suggestion ->
                            if (suggestion.w3wAddress.center != null) {
                                SearchResult.ResolvedAddress(
                                    query = canonicalQuery,
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
                            } else {
                                SearchResult.SearchSuggestion(
                                    query = canonicalQuery,
                                    providerId = providerId,
                                    extras = buildMap {
                                        put(EXTRAS_KEY_RANK, suggestion.rank.toString())
                                        suggestion.distanceToFocus?.let {
                                            put(
                                                EXTRAS_KEY_DISTANCE_TO_FOCUS, it.distance.toString()
                                            )
                                        }
                                        put(EXTRAS_KEY_TITLE, suggestion.w3wAddress.words)
                                        suggestion.w3wAddress.nearestPlace
                                            .takeIf { it.isNotEmpty() }
                                            ?.let { put(EXTRAS_KEY_SUBTITLE, it) }
                                    },
                                )
                            }
                        },
                    )

                    is W3WResult.Failure -> W3WResult.Failure(result.error)
                }
            }
        }

    override suspend fun resolve(data: SearchResult.SearchSuggestion): W3WResult<SearchResult.ResolvedAddress> =
        withContext(Dispatchers.IO) {
            safeW3WCall {
                val w3WAddress = data.extras[EXTRAS_KEY_TITLE]
                    ?: return@safeW3WCall W3WResult.Failure(MissingSuggestionTitleException())

                when (val result = textDataSource.convertToCoordinates(w3WAddress)) {
                    is W3WResult.Success -> W3WResult.Success(
                        SearchResult.ResolvedAddress(
                            query = w3WAddress,
                            providerId = providerId,
                            address = result.value
                        )
                    )

                    is W3WResult.Failure -> W3WResult.Failure(result.error)
                }
            }
        }
}
