package com.what3words.search.wrapper.core.util

import com.what3words.core.types.geometry.W3WCoordinates

/**
 * Distance in metres between two coordinates, delegated to each platform's native geodesic
 * implementation so results match what the rest of the host app measures.
 */
internal expect fun W3WCoordinates.distanceInMetersTo(other: W3WCoordinates): Double
