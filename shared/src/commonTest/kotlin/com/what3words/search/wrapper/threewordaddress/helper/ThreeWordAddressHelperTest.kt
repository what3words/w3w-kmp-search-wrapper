package com.what3words.search.wrapper.threewordaddress.helper

import kotlin.test.Test
import kotlin.test.assertEquals

class ThreeWordAddressHelperTest {

    // ── normalizeSpaceSeparatedQuery: no dots -> spaces become dots ───────────

    @Test
    fun noDots_threeSpaceSeparatedWords_areJoinedWithDots() {
        assertEquals("filled.count.soap", "filled count soap".normalizeSpaceSeparatedQuery())
    }

    @Test
    fun noDots_nbspIsTreatedAsASpace() {
        assertEquals("filled.count.soap", "filled\u00A0count\u00A0soap".normalizeSpaceSeparatedQuery())
    }

    @Test
    fun noDots_surroundingWhitespaceIsTrimmed() {
        assertEquals("filled.count.soap", "  filled count soap  ".normalizeSpaceSeparatedQuery())
    }

    @Test
    fun noDots_everySpaceBecomesADot() {
        // No segmentation: a spaced-language query without dots is naively dotted on every space.
        assertEquals(
            "xoi.dau.dau.tam.vui.ve",
            "xoi dau dau tam vui ve".normalizeSpaceSeparatedQuery(),
        )
    }

    // ── normalizeSpaceSeparatedQuery: dots present -> sent as-is (trimmed) ─────

    @Test
    fun withDots_plainAddressIsUnchanged() {
        assertEquals("filled.count.soap", "filled.count.soap".normalizeSpaceSeparatedQuery())
    }

    @Test
    fun withDots_vietnameseWordInternalSpacesArePreserved() {
        assertEquals(
            "xôi đậu.đậu tằm.vui vẻ",
            "xôi đậu.đậu tằm.vui vẻ".normalizeSpaceSeparatedQuery(),
        )
    }

    @Test
    fun withDots_prefixAndSpacesAreLeftUntouched() {
        // Dot present -> only outer whitespace is trimmed; /// and inner spaces are kept verbatim.
        assertEquals(
            "///xôi đậu. đậu tằm. vui vẻ",
            "  ///xôi đậu. đậu tằm. vui vẻ  ".normalizeSpaceSeparatedQuery(),
        )
    }

    @Test
    fun withDots_partialInputIsLeftUntouched() {
        assertEquals("filled.cou", "filled.cou".normalizeSpaceSeparatedQuery())
    }
}
