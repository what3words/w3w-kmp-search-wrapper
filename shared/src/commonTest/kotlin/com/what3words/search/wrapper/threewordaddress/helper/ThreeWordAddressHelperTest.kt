package com.what3words.search.wrapper.threewordaddress.helper

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ThreeWordAddressHelperTest {

    // ── segmentationCandidates: two dots (fully delimited) ──────────────────

    @Test
    fun twoDots_preservesWordInternalSpaces() {
        val candidates = "xôi đậu.đậu tằm.vui vẻ".segmentationCandidates()
        assertEquals(listOf("xôi đậu.đậu tằm.vui vẻ"), candidates)
    }

    @Test
    fun twoDots_plainAddressIsSingleCandidate() {
        val candidates = "filled.count.soap".segmentationCandidates()
        assertEquals(listOf("filled.count.soap"), candidates)
    }

    @Test
    fun twoDots_trimsSpacesAroundDelimiters() {
        val candidates = "filled. count. soap".segmentationCandidates()
        assertEquals(listOf("filled.count.soap"), candidates)
    }

    @Test
    fun trailingDot_isIgnored() {
        val candidates = "xôi đậu.đậu tằm.vui vẻ.".segmentationCandidates()
        assertEquals(listOf("xôi đậu.đậu tằm.vui vẻ"), candidates)
    }

    @Test
    fun consecutiveDots_areCollapsed() {
        val candidates = "filled..count.soap".segmentationCandidates()
        assertEquals(listOf("filled.count.soap"), candidates)
    }

    // ── segmentationCandidates: zero dots ────────────────────────────────────

    @Test
    fun zeroDots_threeTokens_yieldsSingleDotJoinedCandidate() {
        val candidates = "filled count soap".segmentationCandidates()
        assertEquals(listOf("filled.count.soap"), candidates)
    }

    @Test
    fun zeroDots_sixTokens_ranksBalancedSplitFirst() {
        val candidates = "xoi dau dau tam vui ve".segmentationCandidates()
        assertEquals("xoi dau.dau tam.vui ve", candidates.first())
        // 10 ways to choose 2 of 5 gaps, minus the three splits with a 4-token word.
        assertEquals(7, candidates.size)
    }

    @Test
    fun zeroDots_fourTokens_coversAllLopsidedShapes() {
        val candidates = "xôi đậu mưa gió".segmentationCandidates()
        assertEquals(3, candidates.size)
        assertTrue("xôi đậu.mưa.gió" in candidates)
        assertTrue("xôi.đậu mưa.gió" in candidates)
        assertTrue("xôi.đậu.mưa gió" in candidates)
    }

    @Test
    fun zeroDots_nbspIsTreatedAsSpace() {
        val candidates = "filled count soap".segmentationCandidates()
        assertEquals(listOf("filled.count.soap"), candidates)
    }

    // ── segmentationCandidates: one dot ──────────────────────────────────────

    @Test
    fun oneDot_ranksBalancedSplitFirst() {
        val candidates = "xôi đậu.đậu tằm vui vẻ".segmentationCandidates()
        assertEquals("xôi đậu.đậu tằm.vui vẻ", candidates.first())
        assertEquals(3, candidates.size)
    }

    @Test
    fun oneDot_promotesBoundaryOnEitherSideOfTheDot() {
        val candidates = "xôi đậu tằm.vui vẻ".segmentationCandidates()
        // (1,2,2) and (2,1,2) tie on balance; the earlier boundary wins the tie.
        assertEquals("xôi.đậu tằm.vui vẻ", candidates.first())
        assertTrue("xôi đậu.tằm.vui vẻ" in candidates)
        assertTrue("xôi đậu tằm.vui.vẻ" in candidates)
    }

    // ── segmentationCandidates: not a three-word address ─────────────────────

    @Test
    fun moreThanTwoDots_returnsNoCandidates() {
        assertEquals(emptyList(), "one.two.three.four".segmentationCandidates())
    }

    @Test
    fun tooFewTokens_returnsNoCandidates() {
        assertEquals(emptyList(), "ab".segmentationCandidates())
        assertEquals(emptyList(), "filled count".segmentationCandidates())
        assertEquals(emptyList(), "filled.cou".segmentationCandidates())
    }

    @Test
    fun blankQuery_returnsNoCandidates() {
        assertEquals(emptyList(), "".segmentationCandidates())
        assertEquals(emptyList(), "   ".segmentationCandidates())
    }

    @Test
    fun wordsLongerThanTokenCap_returnsNoCandidates() {
        // Three words of at most 3 tokens each cover at most 9 tokens.
        val tenTokens = (1..10).joinToString(" ") { "t$it" }
        assertEquals(emptyList(), tenTokens.segmentationCandidates())
    }

    // ── normalizeToCanonicalForm (fallback path) ─────────────────────────────

    @Test
    fun normalizeToCanonicalForm_replacesSpacesWithDots() {
        assertEquals("filled.count.soap", "filled count soap".normalizeToCanonicalForm())
    }

    @Test
    fun normalizeToCanonicalForm_keepsPartialInputUntouched() {
        assertEquals("filled.cou", "filled.cou".normalizeToCanonicalForm())
    }
}
