package com.what3words.search.wrapper.coordinates

import com.what3words.core.types.language.W3WRFC5646Language
import com.what3words.search.wrapper.fake.FakeW3WTextDataSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class CoordinatesSearchTest {

    @Test
    fun testDefaultConfig() {
        val config = CoordinatesSearch.defaultConfig()
        
        // Assert defaults
        assertEquals(true, config.enableDecimal)
        assertEquals(true, config.enableDDM)
        assertEquals(true, config.enableDMS)
        assertEquals(W3WRFC5646Language.EN_GB, config.language)
    }

    @Test
    fun testConfigModification() {
        val config = CoordinatesSearch.defaultConfig().apply {
            enableDecimal = false
            enableDDM = false
            enableDMS = false
            language = W3WRFC5646Language.FR_FR
        }
        
        assertEquals(false, config.enableDecimal)
        assertEquals(false, config.enableDDM)
        assertEquals(false, config.enableDMS)
        assertEquals(W3WRFC5646Language.FR_FR, config.language)
    }

    @Test
    fun testBuildProvider() {
        val config = CoordinatesSearch.defaultConfig()
        val provider = CoordinatesSearch.build(config, FakeW3WTextDataSource())
        
        assertNotNull(provider)
        assertIs<CoordinatesSearchProvider>(provider)
        assertEquals("CoordinatesSearchProvider", provider.providerId)
    }
}
