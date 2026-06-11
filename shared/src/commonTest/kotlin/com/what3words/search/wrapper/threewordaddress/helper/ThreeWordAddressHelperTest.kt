package com.what3words.search.wrapper.threewordaddress.helper

import kotlin.test.Test
import kotlin.test.assertEquals

class ThreeWordAddressHelperTest {

    // ── normalizeSpaceSeparatedQuery: every space becomes a dot ───────────────

    @Test
    fun threeSpaceSeparatedWords_areJoinedWithDots() {
        assertEquals("filled.count.soap", "filled count soap".normalizeSpaceSeparatedQuery())
    }

    @Test
    fun nbspIsTreatedAsASpace() {
        assertEquals("filled.count.soap", "filled\u00A0count\u00A0soap".normalizeSpaceSeparatedQuery())
    }

    @Test
    fun surroundingWhitespaceIsTrimmed() {
        assertEquals("filled.count.soap", "  filled count soap  ".normalizeSpaceSeparatedQuery())
    }

    @Test
    fun dotPlusSpaceMix_remainingBoundariesAreFilledIn() {
        // A partially dotted query still has its leftover space boundaries converted.
        assertEquals("index.home.raft", "index.home raft".normalizeSpaceSeparatedQuery())
    }

    @Test
    fun everySpaceBecomesADot() {
        // No segmentation: a spaced query is naively dotted on every space, never split.
        assertEquals(
            "xoi.dau.dau.tam.vui.ve",
            "xoi dau dau tam vui ve".normalizeSpaceSeparatedQuery(),
        )
    }

    // ── nothing to convert -> sent as-is (trimmed) ────────────────────────────

    @Test
    fun fullyDottedAddressIsUnchanged() {
        assertEquals("filled.count.soap", "filled.count.soap".normalizeSpaceSeparatedQuery())
    }

    @Test
    fun partialDottedInputWithoutSpacesIsUnchanged() {
        assertEquals("filled.cou", "filled.cou".normalizeSpaceSeparatedQuery())
    }
}
