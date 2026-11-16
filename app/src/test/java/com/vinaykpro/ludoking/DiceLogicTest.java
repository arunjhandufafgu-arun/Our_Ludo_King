package com.vinaykpro.ludoking;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DiceLogicTest {

    @Test
    public void threeRapidTaps_locksFirstWinnerColorAndNeverChangesAfterward() {
        int tapCount = 0;
        long lastTapTime = 0L;
        String lastColor = null;
        String lockedWinner = null;

        // Player Red taps 3 times rapidly at start (1000ms, 1200ms, 1400ms)
        tapCount = MainActivity.updateColorRapidTapCount(tapCount, lastColor, "red", lastTapTime, 1000L);
        lastColor = "red";
        lastTapTime = 1000L;
        lockedWinner = MainActivity.tryLockWinnerColor(lockedWinner, lastColor, tapCount, false);
        assertNull(lockedWinner);

        tapCount = MainActivity.updateColorRapidTapCount(tapCount, lastColor, "red", lastTapTime, 1200L);
        lastTapTime = 1200L;
        lockedWinner = MainActivity.tryLockWinnerColor(lockedWinner, lastColor, tapCount, false);
        assertNull(lockedWinner);

        tapCount = MainActivity.updateColorRapidTapCount(tapCount, lastColor, "red", lastTapTime, 1400L);
        lastTapTime = 1400L;
        lockedWinner = MainActivity.tryLockWinnerColor(lockedWinner, lastColor, tapCount, false);
        assertEquals("red", lockedWinner);

        // Another player (Blue) later taps 3 times rapidly — lockedWinner must remain "red"
        tapCount = MainActivity.updateColorRapidTapCount(tapCount, lastColor, "blue", lastTapTime, 2000L);
        lastColor = "blue";
        lastTapTime = 2000L;
        tapCount = MainActivity.updateColorRapidTapCount(tapCount, lastColor, "blue", lastTapTime, 2150L);
        lastTapTime = 2150L;
        tapCount = MainActivity.updateColorRapidTapCount(tapCount, lastColor, "blue", lastTapTime, 2300L);
        lastTapTime = 2300L;
        assertEquals(3, tapCount);
        lockedWinner = MainActivity.tryLockWinnerColor(lockedWinner, "blue", tapCount, false);
        assertEquals("red", lockedWinner);
    }

    @Test
    public void threeTaps_doesNotForceSixDirectly() {
        assertFalse(MainActivity.shouldForceSix(3));
        for (int roll = 1; roll <= 5; roll++) {
            assertEquals(roll, MainActivity.resolveDiceValue(roll, false, false, 3));
        }
    }

    @Test
    public void teamUpMode_favorsBothTeammateColors() {
        // In Team Up mode (gameType == 2), Red & Yellow are teammates, Blue & Green are teammates
        assertTrue(MainActivity.isFavoredColor("red", "red", 2));
        assertTrue(MainActivity.isFavoredColor("yellow", "red", 2));
        assertFalse(MainActivity.isFavoredColor("blue", "red", 2));
        assertFalse(MainActivity.isFavoredColor("green", "red", 2));

        assertTrue(MainActivity.isFavoredColor("blue", "green", 2));
        assertTrue(MainActivity.isFavoredColor("green", "green", 2));
        assertFalse(MainActivity.isFavoredColor("red", "green", 2));

        // In Classic mode (gameType == 1), only the exact locked color is favored
        assertTrue(MainActivity.isFavoredColor("red", "red", 1));
        assertFalse(MainActivity.isFavoredColor("yellow", "red", 1));
    }

    @Test
    public void smartFairRoll_guidesFavoredToWinAndBlocksOpponentWinningStep() {
        // Favored player with a piece at step 53 (3 steps from home 56) gets 3 to finish naturally
        int[] favoredSteps = new int[]{53, 20, -1, -1};
        int favoredRoll = MainActivity.selectSmartFairRoll(2, true, favoredSteps, 2, 2, 1, 0);
        assertEquals(3, favoredRoll);

        // Opponent on non-final piece (e.g., 1st or 2nd piece, completedPieces < 3) CAN complete freely for natural pacing
        int[] opponentEarlySteps = new int[]{54, 56, 20, -1};
        int opponentEarlyRoll = MainActivity.selectSmartFairRoll(2, false, opponentEarlySteps, 1, 1, 1, 0);
        assertEquals(2, opponentEarlyRoll);

        // Opponent on final piece at step 54 (completedPieces == 3, needs 2 to reach 56 and win) is blocked from rolling 2
        int[] opponentFinalSteps = new int[]{54, 56, 56, 56};
        int opponentFinalRoll = MainActivity.selectSmartFairRoll(2, false, opponentFinalSteps, 3, 2, 1, 0);
        assertNotEquals(2, opponentFinalRoll);
    }

    @Test
    public void naturalTokenCuts_allowsOpponentToCutLockedWinnerEarlyAndMidGame_butProtectsInCriticalEndgame() {
        // Early/mid game (0 cuts so far, token at step 18, 2 tokens out): opponent IS allowed to cut locked winner token
        assertTrue(MainActivity.canAllowFavoredTokenCut(
                1, 0, 18, 2, 180, 190, false, false
        ));

        // Second cut in mid-game (1 cut so far, token at step 24, 2 tokens out/completed, balanced race) is also allowed
        assertTrue(MainActivity.canAllowFavoredTokenCut(
                1, 1, 24, 2, 140, 145, false, false
        ));

        // Protected after 2 cuts or when token is deep near home entrance (step 45) or when opponent is near win
        assertFalse(MainActivity.canAllowFavoredTokenCut(
                1, 2, 20, 2, 140, 145, false, false
        ));
        assertFalse(MainActivity.canAllowFavoredTokenCut(
                1, 0, 45, 2, 100, 120, false, false
        ));
        assertFalse(MainActivity.canAllowFavoredTokenCut(
                1, 0, 18, 2, 120, 40, true, false
        ));
    }
}
