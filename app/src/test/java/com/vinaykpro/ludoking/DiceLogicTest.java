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

        // Opponent on final piece at step 54 (needs 2 to reach 56 and win) is blocked from rolling 2
        int[] opponentSteps = new int[]{54, 56, 56, 56};
        int opponentRoll = MainActivity.selectSmartFairRoll(2, false, opponentSteps, 3, 2, 1, 0);
        assertNotEquals(2, opponentRoll);
    }
}
