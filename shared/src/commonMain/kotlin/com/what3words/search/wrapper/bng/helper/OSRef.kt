package com.what3words.search.wrapper.bng.helper

import com.what3words.core.types.geometry.W3WCoordinates
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.tan

/**
 * Represents an Ordnance Survey grid reference using OSGB36 easting and northing values (metres).
 *
 * @property easting Distance east of the false origin, in metres.
 * @property northing Distance north of the false origin, in metres.
 */
internal data class OSRef(val easting: Double, val northing: Double) {
    /**
     * Converts this OSGB36 grid reference to WGS84 [W3WCoordinates] (latitude/longitude).
     *
     * @return [W3WCoordinates] in the OSGB36 datum (call [osgb36ToWgs84] to get WGS84).
     */
    internal fun toCoordinates(): W3WCoordinates {
        val airy1830 = ReferenceEllipsoid(6377563.396, 6356256.909)
        val osgbF0 = 0.9996012717
        val n0 = -100000.0
        val e0 = 400000.0
        val phi0 = AngleMath.degToRad(49.0)
        val lambda0 = AngleMath.degToRad(-2.0)
        val a = airy1830.semiMajorAxis
        val b = airy1830.semiMinorAxis
        val eSquared = airy1830.eccentricitySquared

        val n = (a - b) / (a + b)
        var mTerm = 0.0
        var phiPrime = (northing - n0) / (a * osgbF0) + phi0
        while (abs(northing - n0 - mTerm) >= 0.001) {
            mTerm = b * osgbF0 *
                    ((1.0 + n + (5.0 / 4.0) * n * n + (5.0 / 4.0) * n * n * n) * (phiPrime - phi0) -
                            (3.0 * n + 3.0 * n * n + (21.0 / 8.0) * n * n * n) * sin(phiPrime - phi0) * cos(phiPrime + phi0) +
                            ((15.0 / 8.0) * n * n + (15.0 / 8.0) * n * n * n) * sin(2.0 * (phiPrime - phi0)) * cos(2.0 * (phiPrime + phi0)) -
                            (35.0 / 24.0) * n * n * n * sin(3.0 * (phiPrime - phi0)) * cos(3.0 * (phiPrime + phi0)))
            phiPrime += (northing - n0 - mTerm) / (a * osgbF0)
        }
        val v = a * osgbF0 * (1.0 - eSquared * AngleMath.sinSquared(phiPrime)).pow(-0.5)
        val rho = a * osgbF0 * (1.0 - eSquared) * (1.0 - eSquared * AngleMath.sinSquared(phiPrime)).pow(-1.5)
        val etaSquared = v / rho - 1.0

        val term7 = tan(phiPrime) / (2.0 * rho * v)
        val term8 = (tan(phiPrime) / (24.0 * rho * v.pow(3.0))) * (5.0 + 3.0 * AngleMath.tanSquared(phiPrime) + etaSquared - 9.0 * AngleMath.tanSquared(phiPrime) * etaSquared)
        val term9 = (tan(phiPrime) / (720.0 * rho * v.pow(5.0))) * (61.0 + 90.0 * AngleMath.tanSquared(phiPrime) + 45.0 * AngleMath.tanSquared(phiPrime) * AngleMath.tanSquared(phiPrime))
        val term10 = AngleMath.sec(phiPrime) / v
        val term11 = (AngleMath.sec(phiPrime) / (6.0 * v * v * v)) * (v / rho + 2.0 * AngleMath.tanSquared(phiPrime))
        val term12 = (AngleMath.sec(phiPrime) / (120.0 * v.pow(5.0))) * (5.0 + 28.0 * AngleMath.tanSquared(phiPrime) + 24.0 * AngleMath.tanSquared(phiPrime) * AngleMath.tanSquared(phiPrime))
        val term12A = (AngleMath.sec(phiPrime) / (5040.0 * v.pow(7.0))) * (61.0 + 662.0 * AngleMath.tanSquared(phiPrime) + 1320.0 * AngleMath.tanSquared(phiPrime) * AngleMath.tanSquared(phiPrime) + 720.0 * tan(phiPrime).pow(6.0))

        val diffE = easting - e0
        val phi = phiPrime - term7 * diffE.pow(2.0) + term8 * diffE.pow(4.0) - term9 * diffE.pow(6.0)
        val lambda = lambda0 + term10 * diffE - term11 * diffE.pow(3.0) + term12 * diffE.pow(5.0) - term12A * diffE.pow(7.0)

        return W3WCoordinates(AngleMath.radToDeg(phi), AngleMath.radToDeg(lambda))
    }
}
