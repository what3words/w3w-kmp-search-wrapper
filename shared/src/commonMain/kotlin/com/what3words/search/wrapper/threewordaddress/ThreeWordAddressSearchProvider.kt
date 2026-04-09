package com.what3words.search.wrapper.threewordaddress

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.core.types.common.W3WResult

import com.what3words.search.wrapper.core.SearchProvider
import com.what3words.search.wrapper.core.SearchResult
import com.what3words.search.wrapper.threewordaddress.helper.isA3WordAddress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

/** Unique identifier for the three-word address search provider. */
const val THREE_WORD_ADDRESS_PROVIDER_ID = "ThreeWordAddressSearchProvider"

/**
 * Search provider for three-word address queries.
 */
internal class ThreeWordAddressSearchProvider(
    private val textDataSource: W3WTextDataSource,
    private val config: ThreeWordAddressSearchConfig,
) : SearchProvider {

    override val providerId: String = THREE_WORD_ADDRESS_PROVIDER_ID

    override fun canHandle(query: String): Boolean = query.isA3WordAddress()

    private val autosuggestOptions by lazy { config.toAutosuggestOptions() }

    override suspend fun executeSearch(query: String): W3WResult<List<SearchResult>> =
        withContext(Dispatchers.IO) {
            when (val result = textDataSource.autosuggest(query, autosuggestOptions)) {
                is W3WResult.Success -> W3WResult.Success(
                    result.value.map { suggestion ->
                        SearchResult.ResolvedAddress(
                            query = query,
                            providerId = providerId,
                            address = suggestion.w3wAddress,
                        )
                    },
                )

                is W3WResult.Failure -> W3WResult.Failure(result.error)
            }
        }

}
