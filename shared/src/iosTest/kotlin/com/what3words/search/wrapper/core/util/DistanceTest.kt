package com.what3words.search.wrapper.core.util

import com.what3words.core.types.geometry.W3WCoordinates
import kotlin.math.roundToInt
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Expectations are CoreLocation's WGS84 ellipsoid figures, which differ from a sphere
 * approximation by ~0.3%; the Android actual is covered separately as it needs the framework.
 */
class DistanceTest {

    @Test
    fun testSamePointIsZero() {
        val point = W3WCoordinates(10.7804238, 106.7051357)

        assertEquals(0.0, point.distanceInMetersTo(point))
    }

    @Test
    fun testOneDegreeOfLatitude() {
        val meters = W3WCoordinates(0.0, 0.0).distanceInMetersTo(W3WCoordinates(1.0, 0.0))

        assertEquals(110574.360, meters, absoluteTolerance = 0.01)
    }

    @Test
    fun testKnownCityPair() {
        val london = W3WCoordinates(51.5074, -0.1278)
        val paris = W3WCoordinates(48.8566, 2.3522)

        assertEquals(343923.120, london.distanceInMetersTo(paris), absoluteTolerance = 0.01)
    }

    @Test
    fun testSubKilometreDistanceKeepsMetreResolution() {
        val focus = W3WCoordinates(10.7804238, 106.7051357)
        val nearby = W3WCoordinates(10.7834838, 106.7051357)

        assertEquals(338, focus.distanceInMetersTo(nearby).roundToInt())
    }

    @Test
    fun testIsSymmetric() {
        val a = W3WCoordinates(51.5074, -0.1278)
        val b = W3WCoordinates(48.8566, 2.3522)

        // CoreLocation is not bitwise symmetric, so compare within a micrometre.
        assertEquals(a.distanceInMetersTo(b), b.distanceInMetersTo(a), absoluteTolerance = 1e-6)
    }

    @Test
    fun testAntipodalPointsDoNotProduceNaN() {
        val meters = W3WCoordinates(0.0, 0.0).distanceInMetersTo(W3WCoordinates(0.0, 180.0))

        assertEquals(20037508.342, meters, absoluteTolerance = 0.01)
    }
}
