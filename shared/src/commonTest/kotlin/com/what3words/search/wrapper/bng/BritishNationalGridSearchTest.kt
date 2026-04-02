package com.what3words.search.wrapper.bng

import com.what3words.core.types.language.W3WRFC5646Language
import com.what3words.search.wrapper.fake.FakeW3WTextDataSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

class BritishNationalGridSearchTest {

    @Test
    fun defaultConfig_hasEnGbLanguage() {
        val config = BritishNationalGridSearch.defaultConfig()

        assertEquals(W3WRFC5646Language.EN_GB, config.language)
    }

    @Test
    fun configModification_updatesLanguage() {
        val config = BritishNationalGridSearch.defaultConfig().apply {
            language = W3WRFC5646Language.FR_FR
        }

        assertEquals(W3WRFC5646Language.FR_FR, config.language)
    }

    @Test
    fun build_returnsProvider() {
        val config = BritishNationalGridSearch.defaultConfig()
        val provider = BritishNationalGridSearch.build(config, FakeW3WTextDataSource())

        assertNotNull(provider)
        assertIs<BritishNationalGridSearchProvider>(provider)
        assertEquals(BRITISH_NATIONAL_GRID_PROVIDER_ID, provider.providerId)
    }
}
