package com.what3words.search.wrapper.core

import com.what3words.core.types.common.W3WError
import com.what3words.core.types.common.W3WResult
import kotlinx.coroutines.CancellationException

/**
 * Executes [block] and converts any non-cancellation [Throwable] into a [W3WResult.Failure].
 *
 * Cancellation must always propagate so coroutine cancellation continues to work correctly.
 *
 * @param errorMapper Maps a caught [Throwable] to a domain [W3WError]. Defaults to wrapping
 *   the throwable in a generic [W3WError] preserving its message and cause.
 */
internal suspend inline fun <T> safeW3WCall(
    crossinline errorMapper: (Throwable) -> W3WError = { W3WError(it) },
    crossinline block: suspend () -> W3WResult<T>
): W3WResult<T> = try {
    block()
} catch (c: CancellationException) {
    throw c
} catch (t: Throwable) {
    W3WResult.Failure(errorMapper(t), t.message)
}
