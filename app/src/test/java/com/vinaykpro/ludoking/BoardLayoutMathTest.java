package com.vinaykpro.ludoking;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BoardLayoutMathTest {

    @Test
    public void computeBoardSizePx_leavesSideMarginsAndTrayRoomAcrossPhoneSizes() {
        int[][] phones = new int[][]{
                {480, 800, 150},    // Old compact phone (1.5x density)
                {720, 1280, 200},   // 720p 16:9 phone (2.0x density)
                {1080, 1920, 300},  // 1080p 16:9 phone (3.0x density)
                {1080, 2400, 300},  // Modern tall 20:9 phone (3.0x density)
                {1440, 3200, 400}   // QHD flagship (4.0x density)
        };

        for (int[] p : phones) {
            int w = p[0];
            int h = p[1];
            float density = p[2] / 100f;
            int board = BoardLayoutMath.computeBoardSizePx(w, h, density);

            // Board is always strictly narrower than screen width (at most 90% => >= 5% side margin each side)
            assertTrue("Board must leave side margin for " + w + "x" + h, board <= Math.round(w * 0.90f));
            // Board stays comfortably playable (at least 72% width)
            assertTrue("Board must remain readable for " + w + "x" + h, board >= Math.round(w * 0.72f));
            // Vertical breathing room remains for top/bottom dice trays + controls
            assertTrue("Vertical tray room must exist for " + w + "x" + h, h - board >= Math.round(180 * density));
        }
    }

    @Test
    public void computeBoardSizePx_capsOnShortAspectRatioWithoutCollapsing() {
        int board = BoardLayoutMath.computeBoardSizePx(1080, 1300, 3.0f);
        // 1300 - 204*3 = 688 < 1080*0.72 (778) => clamped to 72% floor so board never collapses
        assertEquals(Math.round(1080 * BoardLayoutMath.MIN_BOARD_WIDTH_RATIO), board);
        assertEquals(0, BoardLayoutMath.computeBoardSizePx(0, 1920, 3.0f));
        assertEquals(0, BoardLayoutMath.computeBoardSizePx(1080, -10, 3.0f));
    }

    @Test
    public void tokenDimensionsScaleProportionallyWithBoard() {
        int smallBoard = BoardLayoutMath.computeBoardSizePx(480, 800, 1.5f);
        int largeBoard = BoardLayoutMath.computeBoardSizePx(1080, 2400, 3.0f);
        float smallBlock = BoardLayoutMath.computeBlockSizePx(smallBoard);
        float largeBlock = BoardLayoutMath.computeBlockSizePx(largeBoard);

        assertEquals(smallBoard / 15f, smallBlock, 0.001f);
        assertTrue(BoardLayoutMath.computeTokenWidthPx(smallBlock) < BoardLayoutMath.computeTokenWidthPx(largeBlock));
        assertTrue(BoardLayoutMath.computeTokenHeightPx(largeBlock) > BoardLayoutMath.computeTokenWidthPx(largeBlock));
        assertTrue(BoardLayoutMath.computeActiveRingSizePx(largeBlock) < largeBlock);
    }

    @Test
    public void stackScaleDecreasesSmoothlyWithFloor() {
        assertEquals(1.0f, BoardLayoutMath.computeStackScale(1), 0.001f);
        assertTrue(BoardLayoutMath.computeStackScale(2) < 1.0f);
        assertTrue(BoardLayoutMath.computeStackScale(3) < BoardLayoutMath.computeStackScale(2));
        assertTrue(BoardLayoutMath.computeStackScale(4) < BoardLayoutMath.computeStackScale(3));
        assertTrue(BoardLayoutMath.computeStackScale(12) >= 0.46f);
    }

    @Test
    public void stackCenteredOffsetsAreSymmetricAndStayInsideCell() {
        float blockSize = 64f;
        assertEquals(0f, BoardLayoutMath.computeStackCenteredOffset(0, 1, blockSize), 0.0001f);

        for (int count = 2; count <= 8; count++) {
            float sum = 0f;
            float maxAbs = 0f;
            for (int i = 0; i < count; i++) {
                float offset = BoardLayoutMath.computeStackCenteredOffset(i, count, blockSize);
                sum += offset;
                maxAbs = Math.max(maxAbs, Math.abs(offset));
            }
            assertEquals("Stack offsets must sum to 0 for count=" + count, 0f, sum, 0.001f);
            assertTrue("Stack spread must stay well inside cell for count=" + count, maxAbs <= blockSize * 0.30f);
        }
    }

    @Test
    public void hopArcOffsetIsZeroAtEndpointsAndPeaksAtMidpoint() {
        float blockSize = 60f;
        assertEquals(0f, BoardLayoutMath.computeHopArcOffsetY(0f, blockSize, 0.56f), 0.0001f);
        assertEquals(0f, BoardLayoutMath.computeHopArcOffsetY(1f, blockSize, 0.56f), 0.0001f);
        assertEquals(-blockSize * 0.56f, BoardLayoutMath.computeHopArcOffsetY(0.5f, blockSize, 0.56f), 0.001f);
        // Clamps out-of-range inputs cleanly
        assertEquals(0f, BoardLayoutMath.computeHopArcOffsetY(-0.5f, blockSize, 0.56f), 0.0001f);
        assertEquals(0f, BoardLayoutMath.computeHopArcOffsetY(1.5f, blockSize, 0.56f), 0.0001f);
    }
}
