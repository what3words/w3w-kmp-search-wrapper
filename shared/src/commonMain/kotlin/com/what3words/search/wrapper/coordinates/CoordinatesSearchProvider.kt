package com.what3words.search.wrapper.coordinates

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.core.types.common.W3WResult
import com.what3words.search.wrapper.coordinates.helper.isDdPattern
import com.what3words.search.wrapper.coordinates.helper.isDdPrefixPattern
import com.what3words.search.wrapper.coordinates.helper.isDdSuffixPattern
import com.what3words.search.wrapper.coordinates.helper.isDdmPattern
import com.what3words.search.wrapper.coordinates.helper.isDmsPattern
import com.what3words.search.wrapper.coordinates.helper.parseDdCoordinates
import com.what3words.search.wrapper.coordinates.helper.parseDdPrefixCoordinates
import com.what3words.search.wrapper.coordinates.helper.parseDdSuffixCoordinates
import com.what3words.search.wrapper.coordinates.helper.parseDdmCoordinates
import com.what3words.search.wrapper.coordinates.helper.parseDmsCoordinates
import com.what3words.search.wrapper.core.SearchProvider
import com.what3words.search.wrapper.core.SearchResult
import com.what3words.search.wrapper.core.safeW3WCall
import com.what3words.search.wrapper.error.InvalidCoordinatesException
import kotlin.concurrent.Volatile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

/** Unique identifier for the coordinates search provider. */
const val COORDINATES_PROVIDER_ID: String = "CoordinatesSearchProvider"

/**
 * Provider for searching coordinates in various formats (DD, DDM, DMS).
 * Parses valid coordinate strings and converts them into what3words addresses.
 *
 * @param textDataSource Data source used to perform the coordinate-to-what3words conversion.
 * @param config Configuration that controls which coordinate formats are accepted and the target language.
 */
internal class CoordinatesSearchProvider(
    private val textDataSource: W3WTextDataSource,
    @Volatile var config: CoordinatesSearchConfig
) : SearchProvider {
    override val providerId: String = COORDINATES_PROVIDER_ID

    /**
     * Checks if the given query matches any enabled coordinate format.
     */
    override fun canHandle(query: String): Boolean = when {
        config.enableDMS && query.isDmsPattern() -> true
        config.enableDDM && query.isDdmPattern() -> true
        config.enableDecimal && (query.isDdPattern() || query.isDdPrefixPattern() || query.isDdSuffixPattern()) -> true
        else -> false
    }

    /**
     * Executes a search by parsing coordinates and converting them to a what3words address.
     */
    override suspend fun executeSearch(query: String): W3WResult<List<SearchResult>> = withContext(
        Dispatchers.IO
    ) {
        safeW3WCall {
            val coordinates = when {
                config.enableDMS && query.isDmsPattern() -> parseDmsCoordinates(query)
                config.enableDDM && query.isDdmPattern() -> parseDdmCoordinates(query)
                config.enableDecimal && query.isDdPattern() -> parseDdCoordinates(query)
                config.enableDecimal && query.isDdSuffixPattern() -> parseDdSuffixCoordinates(query)
                config.enableDecimal && query.isDdPrefixPattern() -> parseDdPrefixCoordinates(query)
                else -> null
            } ?: return@safeW3WCall W3WResult.Failure(InvalidCoordinatesException())

            when (val result = textDataSource.convertTo3wa(coordinates, config.language)) {
                is W3WResult.Success -> W3WResult.Success(
                    listOf(
                        SearchResult.ResolvedAddress(
                            query,
                            providerId,
                            result.value
                        )
                    )
                )

                is W3WResult.Failure -> W3WResult.Failure(result.error)
            }
        }
    }

}