package com.what3words.search.wrapper.fake

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.core.types.common.W3WResult
import com.what3words.core.types.geometry.W3WCoordinates
import com.what3words.core.types.geometry.W3WGridSection
import com.what3words.core.types.geometry.W3WRectangle
import com.what3words.core.types.language.W3WLanguage
import com.what3words.core.types.options.W3WAutosuggestOptions

/** A no-op [W3WTextDataSource] stub that throws [NotImplementedError] for all operations. */
internal val fakeDataSource = object : W3WTextDataSource {
    override fun version(version: W3WTextDataSource.Version): String? = null
    override fun convertTo3wa(coordinates: W3WCoordinates, language: W3WLanguage) =
        throw NotImplementedError()

    override fun convertToCoordinates(words: String) = throw NotImplementedError()
    override fun autosuggest(input: String, options: W3WAutosuggestOptions?) =
        throw NotImplementedError()

    override fun gridSection(boundingBox: W3WRectangle): W3WResult<W3WGridSection> =
        throw NotImplementedError()

    override fun availableLanguages() = throw NotImplementedError()
    override fun isValid3wa(words: String) = throw NotImplementedError()
}
