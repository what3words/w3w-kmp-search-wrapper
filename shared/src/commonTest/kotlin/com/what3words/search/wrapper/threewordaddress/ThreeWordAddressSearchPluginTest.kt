package com.what3words.search.wrapper.threewordaddress

import com.what3words.search.wrapper.fake.FakeW3WTextDataSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class ThreeWordAddressSearchPluginTest {

    private val dataSource = FakeW3WTextDataSource()

    @Test
    fun build_createsThreeWordAddressSearchProvider() {
        val config = ThreeWordAddressSearchConfig()
        val provider = ThreeWordAddressSearch.build(config, dataSource)

        assertNotNull(provider)
        assertIs<ThreeWordAddressSearchProvider>(provider)
    }

    @Test
    fun build_setsCorrectProviderId() {
        val provider = ThreeWordAddressSearch.build(ThreeWordAddressSearchConfig(), dataSource)

        assertEquals(THREE_WORD_ADDRESS_PROVIDER_ID, provider.providerId)
    }

    @Test
    fun config_defaultMaxResultsIsThree() {
        val config = ThreeWordAddressSearchConfig()

        assertEquals(3, config.maxResults)
    }

    @Test
    fun config_defaultClipToCountryIsEmpty() {
        val config = ThreeWordAddressSearchConfig()

        assertEquals(0, config.clippedCountries.size)
    }

    @Test
    fun config_defaultPreferLandIsTrue() {
        val config = ThreeWordAddressSearchConfig()

        assertEquals(true, config.preferLand)
    }
}
