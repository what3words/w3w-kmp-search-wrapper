package com.what3words.search.wrapper.googleplaces

import com.what3words.core.types.geometry.W3WCoordinates

/**
 * Specifies a geographic bias for Google Places autocomplete results.
 *
 * Use [Circle] to bias towards a circular region, or [Rectangle] to bias towards a
 * rectangular bounding box.
 */
sealed class LocationBias {

    /**
     * Biases results toward a circular area.
     *
     * @property center The geographic center of the bias circle.
     * @property radiusMeters The radius of the circle in metres.
     */
    data class Circle(val center: W3WCoordinates, val radiusMeters: Double) : LocationBias()

    /**
     * Biases results toward a rectangular bounding box.
     *
     * @property low The southwest (lower-left) corner of the rectangle.
     * @property high The northeast (upper-right) corner of the rectangle.
     */
    data class Rectangle(val low: W3WCoordinates, val high: W3WCoordinates) : LocationBias()
}
