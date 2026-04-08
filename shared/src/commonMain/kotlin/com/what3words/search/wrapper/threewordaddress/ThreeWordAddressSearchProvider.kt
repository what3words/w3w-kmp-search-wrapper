package com.what3words.search.wrapper.threewordaddress

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.core.types.common.W3WResult
import com.what3words.core.types.language.W3WRFC5646Language
import com.what3words.core.types.options.W3WAutosuggestInputType
import com.what3words.core.types.options.W3WAutosuggestOptions
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

    override suspend fun executeSearch(query: String): W3WResult<List<SearchResult>> =
        withContext(Dispatchers.IO) {
            when (val result = textDataSource.autosuggest(normalizeQuery(query), buildAutosuggestOptions())) {
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

    private fun normalizeQuery(query: String): String =
        if (config.language.w3wCode == W3WRFC5646Language.VI.w3wCode) {
            query.replace(" ", "")
        } else {
            query
        }

    private fun buildAutosuggestOptions(): W3WAutosuggestOptions =
        W3WAutosuggestOptions.Builder()
            .language(config.language)
            .nResults(config.maxResults)
            .apply {
                if (config.clippedCountries.isNotEmpty()) {
                    clipToCountry(*config.clippedCountries.toTypedArray())
                }
            }
            .focus(config.focus)
            .preferLand(config.preferLand)
            .inputType(W3WAutosuggestInputType.TEXT)
            .build()
}
