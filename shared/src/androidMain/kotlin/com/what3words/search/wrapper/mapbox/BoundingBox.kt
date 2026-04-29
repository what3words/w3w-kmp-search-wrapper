package com.what3words.search.wrapper.mapbox

import com.what3words.core.types.geometry.W3WCoordinates

/**
 * A rectangular bounding box defined by its southwest (low) and northeast (high) corners.
 *
 * @property low Southwest corner of the bounding box.
 * @property high Northeast corner of the bounding box.
 */
data class BoundingBox(
    val low: W3WCoordinates,
    val high: W3WCoordinates,
)
