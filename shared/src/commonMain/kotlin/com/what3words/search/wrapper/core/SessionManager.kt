package com.what3words.search.wrapper.core

import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

/**
 * Manages session tokens for API providers that use session-based billing.
 *
 * @param maxSuggestCalls Maximum number of suggest calls before the session should be refreshed.
 *                        Defaults to [Int.MAX_VALUE] (no limit / manual refresh only).
 * @param sessionTimeoutSeconds Maximum session duration in seconds before the session should be refreshed.
 *                              Defaults to [Long.MAX_VALUE] (no limit / manual refresh only).
 */
@OptIn(ExperimentalTime::class, ExperimentalUuidApi::class)
internal class SessionManager(
    private val maxSuggestCalls: Int = Int.MAX_VALUE,
    private val sessionTimeoutSeconds: Long = Long.MAX_VALUE,
) {
    var sessionToken: String = Uuid.random().toString()
        private set

    private var suggestCallCount = 0
    private var sessionStartTime = Clock.System.now().epochSeconds

    /**
     * Refreshes the session token and resets all counters.
     * Call this after a successful retrieve/resolve call, or when [shouldRefresh] returns true.
     */
    fun refresh() {
        sessionToken = Uuid.random().toString()
        suggestCallCount = 0
        sessionStartTime = Clock.System.now().epochSeconds
    }

    /**
     * Checks if the session should be refreshed based on call count or time elapsed.
     * Returns false if both [maxSuggestCalls] and [sessionTimeoutSeconds] are set to their defaults.
     */
    fun shouldRefresh(): Boolean {
        if (maxSuggestCalls != Int.MAX_VALUE && suggestCallCount >= maxSuggestCalls) return true
        if (sessionTimeoutSeconds != Long.MAX_VALUE) {
            val now = Clock.System.now().epochSeconds
            if (now - sessionStartTime >= sessionTimeoutSeconds) return true
        }
        return false
    }

    /**
     * Records a suggest call. Increments the call count if auto-refresh is enabled
     * ([maxSuggestCalls] is not the default).
     */
    fun onSuggestCall() {
        if (maxSuggestCalls != Int.MAX_VALUE) {
            suggestCallCount++
        }
    }
}
