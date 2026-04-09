package com.what3words.search.wrapper.maybethreewordaddress

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.core.types.common.W3WError
import com.what3words.core.types.common.W3WResult
import com.what3words.search.wrapper.core.SearchProvider
import com.what3words.search.wrapper.core.SearchResult
import com.what3words.search.wrapper.core.SearchResult.SearchSuggestion.Companion.EXTRAS_KEY_SUGGESTED_ADDRESS
import com.what3words.search.wrapper.maybethreewordaddress.helper.mayBeA3WordAddress
import com.what3words.search.wrapper.threewordaddress.ThreeWordAddressSearchConfig
import com.what3words.search.wrapper.threewordaddress.toAutosuggestOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

/** Unique identifier for the may-be three-word address search provider. */
const val MAY_BE_THREE_WORD_ADDRESS_PROVIDER_ID = "MayBeAThreeWordAddressSearchProvider"

private const val THREE_WORD_ADDRESS_PREFIX = "///"
private val NON_LETTER_REGEX = "\\P{L}+".toRegex()

/**
 * Search provider for loose three-word-address-like queries.
 * The query is normalized to dot-separated words before autosuggest.
 */
internal class MayBeAThreeWordAddressSearchProvider(
    private val textDataSource: W3WTextDataSource,
    private val config: ThreeWordAddressSearchConfig,
) : SearchProvider {

    override val providerId: String = MAY_BE_THREE_WORD_ADDRESS_PROVIDER_ID

    override fun canHandle(query: String): Boolean = query.mayBeA3WordAddress() != null

    private val option by lazy { config.toAutosuggestOptions() }

    override suspend fun executeSearch(query: String): W3WResult<List<SearchResult>> =
        withContext(Dispatchers.IO) {
            val maybe3wa =
                query.mayBeA3WordAddress()
                    ?: return@withContext W3WResult.Failure(W3WError("Query is not a valid three-word-address-like input"))

            when (val result = textDataSource.autosuggest(maybe3wa, option)) {
                is W3WResult.Success -> W3WResult.Success(
                    if (query.startsWith(THREE_WORD_ADDRESS_PREFIX)) {
                        result.value.map { suggestion ->
                            SearchResult.ResolvedAddress(
                                query = query,
                                providerId = providerId,
                                address = suggestion.w3wAddress,
                            )
                        }
                    } else {
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

private fun String.lettersOnly(): String = replace(NON_LETTER_REGEX, "").lowercase()
