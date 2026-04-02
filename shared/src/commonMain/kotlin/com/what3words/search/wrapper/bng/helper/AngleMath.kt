package com.what3words.search.wrapper.bng.helper

import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

/**
 * Utility functions for trigonometric calculations and angle conversions.
 */
internal object AngleMath {

    /** Returns sin²([x]). */
    internal fun sinSquared(x: Double): Double {
        val s = sin(x)
        return s * s
    }

    /** Returns tan²([x]). */
    internal fun tanSquared(x: Double): Double {
        val t = tan(x)
        return t * t
    }

    /** Returns the secant of [x] (1 / cos([x])). */
    internal fun sec(x: Double): Double {
        return 1.0 / cos(x)
    }

    /** Converts [x] from degrees to radians. */
    internal fun degToRad(x: Double): Double {
        return x * (PI / 180.0)
    }

    /** Converts [x] from radians to degrees. */
    internal fun radToDeg(x: Double): Double {
        return x * (180.0 / PI)
    }
}
