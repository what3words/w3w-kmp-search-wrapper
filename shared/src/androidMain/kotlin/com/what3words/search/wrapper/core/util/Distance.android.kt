package com.what3words.search.wrapper.core.util

import android.location.Location
import com.what3words.core.types.geometry.W3WCoordinates

internal actual fun W3WCoordinates.distanceInMetersTo(other: W3WCoordinates): Double {
    val results = FloatArray(1)
    Location.distanceBetween(lat, lng, other.lat, other.lng, results)
    return results[0].toDouble()
}
