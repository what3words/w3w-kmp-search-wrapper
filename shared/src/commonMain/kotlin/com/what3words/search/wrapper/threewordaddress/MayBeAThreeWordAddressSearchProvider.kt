package com.what3words.search.wrapper.threewordaddress

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.core.types.common.W3WResult
import com.what3words.core.types.geometry.W3WCoordinates
import com.what3words.core.types.language.W3WProprietaryLanguage
import com.what3words.core.types.language.W3WRFC5646Language
import com.what3words.search.wrapper.core.ResolvableSearchProvider
import com.what3words.search.wrapper.core.SearchResult
import com.what3words.search.wrapper.core.SearchResult.Companion.EXTRAS_KEY_DISTANCE_TO_FOCUS
import com.what3words.search.wrapper.core.SearchResult.Companion.EXTRAS_KEY_SUBTITLE
import com.what3words.search.wrapper.core.SearchResult.Companion.EXTRAS_KEY_SUGGESTED_ADDRESS
import com.what3words.search.wrapper.core.SearchResult.Companion.EXTRAS_KEY_TITLE
import com.what3words.search.wrapper.core.safeW3WCall
import com.what3words.search.wrapper.core.util.distanceInMetersTo
import com.what3words.search.wrapper.error.InvalidQueryException
import com.what3words.search.wrapper.error.MissingCoordinatesException
import com.what3words.search.wrapper.threewordaddress.helper.lettersOnly
import com.what3words.search.wrapper.threewordaddress.helper.mayBeA3WordAddress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlin.concurrent.Volatile
import kotlin.math.roundToInt

/** Unique identifier for the may-be three-word address search provider. */
const val MAY_BE_THREE_WORD_ADDRESS_PROVIDER_ID = "MayBeAThreeWordAddressSearchProvider"

private const val THREE_WORD_ADDRESS_PREFIX = "///"
private const val EXTRAS_LATITUDE = "latitude"
private const val EXTRAS_LONGITUDE = "longitude"
private const val EXTRAS_LANGUAGE = "language"

/**
 * Search provider for loose three-word-address-like queries.
 * The query is normalized to dot-separated words before autosuggest.
 */
internal class MayBeAThreeWordAddressSearchProvider(
    private val textDataSource: W3WTextDataSource,
    @Volatile var config: MayBeAThreeWordAddressSearchConfig,
) : ResolvableSearchProvider {

    override val providerId: String = MAY_BE_THREE_WORD_ADDRESS_PROVIDER_ID

    override fun canHandle(query: String): Boolean = query.mayBeA3WordAddress() != null

    override suspend fun executeSearch(query: String): W3WResult<List<SearchResult>> =
        withContext(Dispatchers.IO) {
            safeW3WCall {
                val snapshot = config
                val autosuggestOptions = snapshot.toAutosuggestOptions()
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
                                firstSuggestion.w3wAddress.center?.let { center ->
                                    put(EXTRAS_LATITUDE, center.lat.toString())
                                    put(EXTRAS_LONGITUDE, center.lng.toString())
                                    snapshot.focus?.let { focus ->
                                        put(
                                            EXTRAS_KEY_DISTANCE_TO_FOCUS,
                                            focus.distanceInMetersTo(center).roundToInt().toString()
                                        )
                                    }
                                }
                                put(EXTRAS_LANGUAGE, firstSuggestion.w3wAddress.language.w3wCode)
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

    override suspend fun resolve(data: SearchResult.SearchSuggestion): W3WResult<SearchResult.ResolvedAddress> =
        withContext(Dispatchers.IO) {
            safeW3WCall {
                val latitude = data.extras[EXTRAS_LATITUDE]?.toDouble()
                val longitude = data.extras[EXTRAS_LONGITUDE]?.toDouble()
                val language = data.extras[EXTRAS_LANGUAGE]?.let {
                    W3WProprietaryLanguage(
                        it,
                        null,
                        null,
                        null
                    )
                } ?: W3WRFC5646Language.EN_GB

                if (latitude == null || longitude == null)
                    return@safeW3WCall W3WResult.Failure(MissingCoordinatesException())

                when (val result =
                    textDataSource.convertTo3wa(
                        coordinates = W3WCoordinates(latitude, longitude),
                        language = language
                    )
                ) {
                    is W3WResult.Success -> W3WResult.Success(
                        SearchResult.ResolvedAddress(
                            data.query,
                            providerId,
                            result.value
                        )
                    )

                    is W3WResult.Failure -> W3WResult.Failure(result.error)
                }
            }
        }
}