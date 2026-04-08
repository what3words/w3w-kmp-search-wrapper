package com.what3words.search.wrapper.fake

import com.what3words.core.datasource.text.W3WTextDataSource
import com.what3words.core.types.common.W3WResult
import com.what3words.core.types.domain.W3WAddress
import com.what3words.core.types.domain.W3WSuggestion
import com.what3words.core.types.geometry.W3WCoordinates
import com.what3words.core.types.geometry.W3WGridSection
import com.what3words.core.types.geometry.W3WRectangle
import com.what3words.core.types.language.W3WLanguage
import com.what3words.core.types.options.W3WAutosuggestOptions
import com.what3words.search.wrapper.fixtures.fakeAddress

/** A stub implementation of [W3WTextDataSource] for testing purposes. */
class FakeW3WTextDataSource : W3WTextDataSource {
    var convertTo3waResult: W3WResult<W3WAddress> = W3WResult.Success(fakeAddress())
    var convertToCoordinatesResult: W3WResult<W3WAddress>? = null
    var autosuggestResult: W3WResult<List<W3WSuggestion>>? = null

    var lastConvertToCoordinatesWords: String? = null
    var lastAutosuggestInput: String? = null
    var lastAutosuggestOptions: W3WAutosuggestOptions? = null

    override fun version(version: W3WTextDataSource.Version): String? = null

    override fun convertTo3wa(coordinates: W3WCoordinates, language: W3WLanguage): W3WResult<W3WAddress> {
        return convertTo3waResult
    }

    override fun convertToCoordinates(words: String): W3WResult<W3WAddress> {
        lastConvertToCoordinatesWords = words
        return convertToCoordinatesResult ?: throw NotImplementedError()
    }

    override fun autosuggest(input: String, options: W3WAutosuggestOptions?): W3WResult<List<W3WSuggestion>> {
        lastAutosuggestInput = input
        lastAutosuggestOptions = options
        return autosuggestResult ?: throw NotImplementedError()
    }

    override fun gridSection(boundingBox: W3WRectangle): W3WResult<W3WGridSection> =
        throw NotImplementedError()

    override fun availableLanguages() = throw NotImplementedError()
    override fun isValid3wa(words: String) = throw NotImplementedError()
}
