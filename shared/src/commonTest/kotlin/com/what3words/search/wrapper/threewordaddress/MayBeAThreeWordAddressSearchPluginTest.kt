package com.what3words.search.wrapper.threewordaddress

import com.what3words.search.wrapper.fake.FakeW3WTextDataSource
import com.what3words.search.wrapper.threewordaddress.ThreeWordAddressSearchConfig
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class MayBeAThreeWordAddressSearchPluginTest {

    private val dataSource = FakeW3WTextDataSource()

    @Test
    fun build_createsMayBeAThreeWordAddressSearchProvider() {
        val provider = MayBeAThreeWordAddressSearch.build(
            ThreeWordAddressSearchConfig(),
            dataSource,
        )

        assertNotNull(provider)
        assertIs<MayBeAThreeWordAddressSearchProvider>(provider)
    }

    @Test
    fun build_setsCorrectProviderId() {
        val provider = MayBeAThreeWordAddressSearch.build(
            ThreeWordAddressSearchConfig(),
            dataSource,
        )

        assertEquals(MAY_BE_THREE_WORD_ADDRESS_PROVIDER_ID, provider.providerId)
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
