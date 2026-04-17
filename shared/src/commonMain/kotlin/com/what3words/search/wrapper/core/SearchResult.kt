package com.what3words.search.wrapper.core

import com.what3words.core.types.domain.W3WAddress

/**
 * Represents a generic search result from a unified search provider.
 *
 * @property query The original search query string.
 * @property providerId The identifier of the provider that returned this result.
 */
sealed class SearchResult(
    open val query: String,
    open val providerId: String,
    open val extras: Map<String, String> = emptyMap()
) {

    /**
     * A search result that has been fully resolved to a [W3WAddress].
     *
     * @property address The resolved what3words address.
     */
    data class ResolvedAddress(
        override val query: String,
        override val providerId: String,
        val address: W3WAddress,
        override val extras: Map<String, String> = emptyMap()
    ) : SearchResult(query, providerId, extras)

    /**
     * A search result that requires further resolution to obtain a [W3WAddress].
     * Typically displayed as a suggestion to the user before selection.
     *
     * Use [ResolvableSearchProvider.resolve] to convert this into a [ResolvedAddress].
     *
     * @property extras Provider-specific metadata associated with this suggestion.
     *   Providers may include display keys such as `"title"` and `"subtitle"` in this map.
     */
    data class SearchSuggestion(
        override val query: String,
        override val providerId: String,
        override val extras: Map<String, String>,
    ) : SearchResult(query, providerId) {

        /** Returns a normalized "did you mean" three-word address when present. */
        fun suggestedAddressOrNull(): String? = extras[EXTRAS_KEY_SUGGESTED_ADDRESS]
    }

    companion object {
        /** Extras key for the primary display text of a suggestion (e.g. place or street name). */
        const val EXTRAS_KEY_TITLE = "title"

        /** Extras key for the secondary display text of a suggestion (e.g. city or region). */
        const val EXTRAS_KEY_SUBTITLE = "subtitle"

        /** Extras key for a did-you-mean three-word address suggestion (e.g. "///filled.count.soap"). */
        const val EXTRAS_KEY_SUGGESTED_ADDRESS = "suggestedAddress"

        /** Extras key for the rank of a suggestion, indicating its relevance to the search query. */
        const val EXTRAS_KEY_RANK = "rank"

        /** Extras key for the distance (kilometers) from the suggestion to the focus coordinates. */
        const val EXTRAS_KEY_DISTANCE_TO_FOCUS = "distanceToFocus"
    }

    /**
     * The primary display text of the search result.
     */
    val title: String?
        get() {
            return when (this) {
                is ResolvedAddress -> extras[EXTRAS_KEY_TITLE] ?: address.words
                is SearchSuggestion -> extras[EXTRAS_KEY_TITLE]
            }
        }

    /**
     * The secondary display text of the search result.
     */
    val subtitle: String?
        get() {
            return when (this) {
                is ResolvedAddress -> extras[EXTRAS_KEY_SUBTITLE] ?: address.nearestPlace
                is SearchSuggestion -> extras[EXTRAS_KEY_SUBTITLE]
            }
        }
}