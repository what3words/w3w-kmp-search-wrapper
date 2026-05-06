package com.what3words.search.wrapper.mapbox

import com.what3words.core.types.language.W3WRFC5646Language
import com.what3words.search.wrapper.fake.FakeW3WTextDataSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class MapboxPluginTest {

    private val dataSource = FakeW3WTextDataSource()

    @Test
    fun build_createsMapboxSearchProvider() {
        val config = MapboxConfig(apiKey = "token")
        val provider = MapboxSearch.build(config, dataSource)

        assertNotNull(provider)
        assertIs<MapboxSearchProvider>(provider)
    }

    @Test
    fun build_setsCorrectProviderId() {
        val provider = MapboxSearch.build(MapboxConfig(apiKey = "token"), dataSource)

        assertEquals(MAPBOX_PROVIDER_ID, provider.providerId)
    }

    @Test
    fun config_defaultLanguageIsEnGb() {
        val config = MapboxConfig(apiKey = "token")

        assertEquals(W3WRFC5646Language.EN_GB, config.language)
    }

    @Test
    fun config_defaultMinQueryLengthIsThree() {
        val config = MapboxConfig(apiKey = "token")

        assertEquals(3, config.minQueryLength)
    }

    @Test
    fun config_defaultMaxResultsIsFive() {
        val config = MapboxConfig(apiKey = "token")

        assertEquals(5, config.maxResults)
    }
}
