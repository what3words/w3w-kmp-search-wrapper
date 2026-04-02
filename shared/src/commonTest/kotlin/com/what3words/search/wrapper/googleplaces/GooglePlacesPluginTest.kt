package com.what3words.search.wrapper.googleplaces

import com.what3words.core.types.language.W3WRFC5646Language
import com.what3words.search.wrapper.fake.FakeW3WTextDataSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class GooglePlacesPluginTest {

    private val dataSource = FakeW3WTextDataSource()

    @Test
    fun build_createsGooglePlacesSearchProvider() {
        val config = GooglePlacesConfig(apiKey = "key")
        val provider = GooglePlacesSearch.build(config, dataSource)

        assertNotNull(provider)
        assertIs<GooglePlacesSearchProvider>(provider)
    }

    @Test
    fun build_setsCorrectProviderId() {
        val provider = GooglePlacesSearch.build(GooglePlacesConfig(apiKey = "key"), dataSource)

        assertEquals(GOOGLE_PLACES_PROVIDER_ID, provider.providerId)
    }

    @Test
    fun config_defaultLanguageIsEnGb() {
        val config = GooglePlacesConfig(apiKey = "key")

        assertEquals(W3WRFC5646Language.EN_GB, config.language)
    }

    @Test
    fun config_defaultUseSessionTokensIsTrue() {
        val config = GooglePlacesConfig(apiKey = "key")

        assertEquals(true, config.useSessionTokens)
    }

    @Test
    fun config_defaultMinQueryLengthIsThree() {
        val config = GooglePlacesConfig(apiKey = "key")

        assertEquals(3, config.minQueryLength)
    }

    @Test
    fun config_defaultMaxResultsIsFive() {
        val config = GooglePlacesConfig(apiKey = "key")

        assertEquals(5, config.maxResults)
    }

}
