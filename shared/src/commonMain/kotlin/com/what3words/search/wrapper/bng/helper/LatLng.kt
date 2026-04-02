package com.what3words.search.wrapper.bng.helper

import com.what3words.core.types.geometry.W3WCoordinates
import kotlin.math.atan
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Converts this [W3WCoordinates] from the OSGB36 datum to WGS84 using a Helmert transformation.
 *
 * @return A new [W3WCoordinates] expressed in the WGS84 datum.
 */
internal fun W3WCoordinates.osgb36ToWgs84(): W3WCoordinates {
    val airy1830 = ReferenceEllipsoid(6377563.396, 6356256.909)
    val a = airy1830.semiMajorAxis
    val eSquared = airy1830.eccentricitySquared
    val phi = AngleMath.degToRad(lat)
    val lambda = AngleMath.degToRad(lng)
    var v = a / sqrt(1.0 - eSquared * AngleMath.sinSquared(phi))
    val h = 0.0 // height
    val x = (v + h) * cos(phi) * cos(lambda)
    val y = (v + h) * cos(phi) * sin(lambda)
    val z = ((1.0 - eSquared) * v + h) * sin(phi)

    val tx = 446.448
    val ty = -125.157
    val tz = 542.06
    val s = -0.0000204894
    val rx = AngleMath.degToRad(0.00004172222)
    val ry = AngleMath.degToRad(0.00006861111)
    val rz = AngleMath.degToRad(0.00023391666)

    val xB = tx + (1.0 + s) * (x - rz * y + ry * z)
    val yB = ty + (1.0 + s) * (rz * x + y - rx * z)
    val zB = tz + (1.0 + s) * (-ry * x + rx * y + z)

    val wgs84 = ReferenceEllipsoid(6378137.0, 6356752.3141)
    val aB = wgs84.semiMajorAxis
    val eSquaredB = wgs84.eccentricitySquared

    val lambdaB = atan2(yB, xB)
    val p = sqrt(xB * xB + yB * yB)
    var phiN = atan(zB / (p * (1.0 - eSquaredB)))
    repeat(10) {
        v = aB / sqrt(1.0 - eSquaredB * AngleMath.sinSquared(phiN))
        phiN = atan((zB + eSquaredB * v * sin(phiN)) / p)
    }

    return W3WCoordinates(AngleMath.radToDeg(phiN), AngleMath.radToDeg(lambdaB))
}
