package com.vinaykpro.ludoking;

/**
 * Pure deterministic geometry and animation math for the Ludo board,
 * tokens, stacked-cell fan-out, and hop arcs.
 *
 * Kept Android-free so it can be unit tested directly on the JVM.
 */
public final class BoardLayoutMath {

    public static final int GRID_CELLS = 15;
    public static final float BOARD_WIDTH_RATIO = 0.90f;
    public static final float MIN_BOARD_WIDTH_RATIO = 0.72f;
    public static final float RESERVED_VERTICAL_DP = 204f;

    private BoardLayoutMath() {
    }

    /**
     * Computes a square Ludo board size that leaves crisp side margins (~5% each side)
     * and guarantees room for top/bottom player dice trays and bottom controls on every
     * phone aspect ratio (from compact 480x800 up to tall 20:9 displays).
     */
    public static int computeBoardSizePx(int screenWidthPx, int screenHeightPx, float density) {
        if (screenWidthPx <= 0 || screenHeightPx <= 0) {
            return 0;
        }
        float safeDensity = density > 0f ? density : 1f;
        int maxByWidth = Math.round(screenWidthPx * BOARD_WIDTH_RATIO);
        int reservedVerticalPx = Math.round(RESERVED_VERTICAL_DP * safeDensity);
        int minBoardPx = Math.round(screenWidthPx * MIN_BOARD_WIDTH_RATIO);
        int availableHeightPx = screenHeightPx - reservedVerticalPx;
        int maxByHeight = Math.max(minBoardPx, Math.min(availableHeightPx, screenWidthPx));
        return Math.max(1, Math.min(maxByWidth, maxByHeight));
    }

    /**
     * One square cell of the 15x15 Ludo grid.
     */
    public static float computeBlockSizePx(float boardSizePx) {
        return boardSizePx / (float) GRID_CELLS;
    }

    /**
     * Authentic Ludo King token proportions relative to a single board square:
     * slightly taller teardrop pin whose base sits neatly inside one square.
     */
    public static float computeTokenWidthPx(float blockSizePx) {
        return blockSizePx * 1.16f;
    }

    public static float computeTokenHeightPx(float blockSizePx) {
        return blockSizePx * 1.44f;
    }

    public static float computeTokenContainerHeightPx(float blockSizePx) {
        return blockSizePx * 1.85f;
    }

    public static float computeActiveRingSizePx(float blockSizePx) {
        return blockSizePx * 0.96f;
    }

    /**
     * Scale factor when multiple tokens share the same board square.
     */
    public static float computeStackScale(int count) {
        if (count <= 1) {
            return 1.0f;
        }
        if (count == 2) {
            return 0.78f;
        }
        if (count == 3) {
            return 0.67f;
        }
        if (count == 4) {
            return 0.58f;
        }
        return Math.max(0.46f, 0.58f - (count - 4) * 0.04f);
    }

    /**
     * Symmetric centered fan-out offset for the i-th token out of `count` sharing one square.
     * Keeps the entire group centered around 0 so tokens never drift outside their cell.
     */
    public static float computeStackCenteredOffset(int index, int count, float blockSizePx) {
        if (count <= 1) {
            return 0f;
        }
        float maxTotalSpread = blockSizePx * 0.56f;
        float step = Math.min(blockSizePx * 0.24f, maxTotalSpread / (float) (count - 1));
        float centeredIndex = index - ((count - 1) / 2.0f);
        return centeredIndex * step;
    }

    /**
     * Smooth parabolic hop lift (negative Y moves upward on screen).
     * Returns 0 at progress=0 and progress=1, peaking at -peakMultiplier * blockSizePx at progress=0.5.
     */
    public static float computeHopArcOffsetY(float progress, float blockSizePx, float peakMultiplier) {
        float t = Math.max(0f, Math.min(1f, progress));
        float parabola = 4f * t * (1f - t);
        return -parabola * blockSizePx * peakMultiplier;
    }
}
