package com.what3words.search.wrapper.bng

import com.what3words.core.types.language.W3WLanguage
import com.what3words.core.types.language.W3WRFC5646Language
import com.what3words.search.wrapper.core.SearchConfig

/**
 * Configuration for [BritishNationalGridSearchProvider].
 *
 * @property language The language used for what3words address lookup. Defaults to [W3WRFC5646Language.EN_GB].
 */
class BritishNationalGridSearchConfig(
    var language: W3WLanguage = W3WRFC5646Language.EN_GB
) : SearchConfig()