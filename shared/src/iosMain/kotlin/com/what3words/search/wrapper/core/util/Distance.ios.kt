package com.what3words.search.wrapper.core.util

import com.what3words.core.types.geometry.W3WCoordinates
import platform.CoreLocation.CLLocation

internal actual fun W3WCoordinates.distanceInMetersTo(other: W3WCoordinates): Double {
    val from = CLLocation(latitude = lat, longitude = lng)
    val to = CLLocation(latitude = other.lat, longitude = other.lng)
    return from.distanceFromLocation(to)
}
