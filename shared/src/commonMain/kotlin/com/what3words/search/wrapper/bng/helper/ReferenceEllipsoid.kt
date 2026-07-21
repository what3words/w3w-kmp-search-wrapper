package com.what3words.search.wrapper.bng.helper

/**
 * Represents a reference ellipsoid with its semi-major and semi-minor axes.
 *
 * @property semiMajorAxis The semi-major axis (a) in meters.
 * @property semiMinorAxis The semi-minor axis (b) in meters.
 */
internal data class ReferenceEllipsoid(val semiMajorAxis: Double, val semiMinorAxis: Double) {
    /** The squared eccentricity (e^2) of the ellipsoid. */
    val eccentricitySquared: Double = (semiMajorAxis * semiMajorAxis - semiMinorAxis * semiMinorAxis) / (semiMajorAxis * semiMajorAxis)
}
