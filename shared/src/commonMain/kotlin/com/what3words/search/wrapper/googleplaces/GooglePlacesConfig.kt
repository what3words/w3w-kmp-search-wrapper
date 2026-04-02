package com.what3words.search.wrapper.googleplaces

import com.what3words.core.types.language.W3WRFC5646Language

/**
 * Configuration for [GooglePlacesProvider].
 *
 * @property apiKey Google Places API key for authenticating requests.
 * @property language RFC 5646 language for autocomplete and what3words conversion. Defaults to [W3WRFC5646Language.EN_GB].
 * @property useSessionTokens When `true`, a UUID session token groups each autocomplete call
 *   with its paired place-details fetch into one billing session, rotating after every resolve. Defaults to `true`.
 * @property minQueryLength Minimum characters before a search is dispatched. Defaults to `3`.
 * @property maxResults Maximum autocomplete suggestions returned per search. Defaults to `5`.
 * @property headers Additional HTTP headers sent with every request.
 */
expect class GooglePlacesConfig(
    apiKey: String,
    language: W3WRFC5646Language = W3WRFC5646Language.EN_GB,
    useSessionTokens: Boolean = true,
    minQueryLength: Int = 3,
    maxResults: Int = 5,
    headers: Map<String, String?> = emptyMap(),
) {
    val apiKey: String
    val language: W3WRFC5646Language
    val useSessionTokens: Boolean
    val minQueryLength: Int
    val maxResults: Int
    val headers: Map<String, String?>

    fun copy(
        apiKey: String = this.apiKey,
        language: W3WRFC5646Language = this.language,
        useSessionTokens: Boolean = this.useSessionTokens,
        minQueryLength: Int = this.minQueryLength,
        maxResults: Int = this.maxResults,
        headers: Map<String, String?> = this.headers,
    ): GooglePlacesConfig
}
