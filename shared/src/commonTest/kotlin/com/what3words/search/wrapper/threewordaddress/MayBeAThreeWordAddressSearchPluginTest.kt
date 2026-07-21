package com.what3words.search.wrapper.threewordaddress

import com.what3words.search.wrapper.fake.FakeW3WTextDataSource
import com.what3words.search.wrapper.threewordaddress.MayBeAThreeWordAddressSearchConfig
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class MayBeAThreeWordAddressSearchPluginTest {

    private val dataSource = FakeW3WTextDataSource()

    fun build_createsMayBeAThreeWordAddressSearchProvider() {
        val provider = MayBeAThreeWordAddressSearch.build(
            MayBeAThreeWordAddressSearchConfig(),
            dataSource,
        )

        assertNotNull(provider)
        assertIs<MayBeAThreeWordAddressSearchProvider>(provider)
    }

    fun build_setsCorrectProviderId() {
        val provider = MayBeAThreeWordAddressSearch.build(
            MayBeAThreeWordAddressSearchConfig(),
            dataSource,
        )

        assertEquals(MAY_BE_THREE_WORD_ADDRESS_PROVIDER_ID, provider.providerId)
    }


    fun config_defaultClipToCountryIsEmpty() {
        val config = MayBeAThreeWordAddressSearchConfig()

        assertEquals(0, config.clippedCountries.size)
    }

    fun config_defaultPreferLandIsTrue() {
        val config = MayBeAThreeWordAddressSearchConfig()

        assertEquals(true, config.preferLand)
    }
}
