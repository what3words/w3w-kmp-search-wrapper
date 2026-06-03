package com.what3words.search.wrapper.bng

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.core.types.common.W3WResult
import com.what3words.search.wrapper.bng.helper.UKNationalGridTransformer
import com.what3words.search.wrapper.bng.helper.UKNationalGridTransformer.isOSGrid
import com.what3words.search.wrapper.core.SearchProvider
import com.what3words.search.wrapper.core.SearchResult
import com.what3words.search.wrapper.core.safeW3WCall
import com.what3words.search.wrapper.error.InvalidCoordinatesException
import kotlin.concurrent.Volatile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

/** Unique identifier for the British National Grid search provider. */
const val BRITISH_NATIONAL_GRID_PROVIDER_ID: String = "BngSearchProvider"

/**
 * A [SearchProvider] that resolves British National Grid references (OS grid references and
 * easting/northing pairs) to what3words addresses.
 *
 * @param textDataSource The data source used to convert coordinates to a what3words address.
 * @param config Configuration specifying the target language for the address lookup.
 */
internal class BritishNationalGridSearchProvider(
    private val textDataSource: W3WTextDataSource,
    @Volatile var config: BritishNationalGridSearchConfig
) : SearchProvider {
    override val providerId: String = BRITISH_NATIONAL_GRID_PROVIDER_ID

    /** Returns `true` if [query] is a valid OS grid reference or easting/northing pair. */
    override fun canHandle(query: String): Boolean {
        return isOSGrid(query)
    }

    /**
     * Converts the given BNG [query] to a what3words address.
     *
     * @return A [W3WResult.Success] containing a single [SearchResult.ResolvedAddress], or
     * [W3WResult.Failure] if the coordinates are invalid or the lookup fails.
     */
    override suspend fun executeSearch(query: String): W3WResult<List<SearchResult>> = withContext(
        Dispatchers.IO
    ) {
        safeW3WCall {
            val coordinates = UKNationalGridTransformer.getCoordinatesFromOSGrid(query)
                ?: return@safeW3WCall W3WResult.Failure(InvalidCoordinatesException())

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