package com.what3words.search.wrapper.googleplaces.model

import kotlinx.serialization.Serializable

@Serializable
internal data class AutocompleteRequest(
    val input: String,
    /** Billing session token (UUID v4). Null when session tokens are disabled. */
    val sessionToken: String? = null
)
