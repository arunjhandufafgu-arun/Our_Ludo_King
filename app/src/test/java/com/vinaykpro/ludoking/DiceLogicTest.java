package com.vinaykpro.ludoking;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DiceLogicTest {

    @Test
    public void fourRapidTaps_locksFirstWinnerColorAndNeverChangesAfterward() {
        int tapCount = 0;
        long lastTapTime = 0L;
        String lastColor = null;
        String lockedWinner = null;

        assertEquals(4, MainActivity.RAPID_TAP_THRESHOLD);

        // Player Red taps 3 times rapidly (1000ms, 1180ms, 1360ms) -> not locked yet (needs 4 taps)
        tapCount = MainActivity.updateColorRapidTapCount(tapCount, lastColor, "red", lastTapTime, 1000L);
        lastColor = "red";
        lastTapTime = 1000L;
        lockedWinner = MainActivity.tryLockWinnerColor(lockedWinner, lastColor, tapCount, false);
        assertNull(lockedWinner);

        tapCount = MainActivity.updateColorRapidTapCount(tapCount, lastColor, "red", lastTapTime, 1180L);
        lastTapTime = 1180L;
        lockedWinner = MainActivity.tryLockWinnerColor(lockedWinner, lastColor, tapCount, false);
        assertNull(lockedWinner);

        tapCount = MainActivity.updateColorRapidTapCount(tapCount, lastColor, "red", lastTapTime, 1360L);
        lastTapTime = 1360L;
        lockedWinner = MainActivity.tryLockWinnerColor(lockedWinner, lastColor, tapCount, false);
        assertNull(lockedWinner);

        // 4th rapid tap (1520ms) locks "red" as the winner color
        tapCount = MainActivity.updateColorRapidTapCount(tapCount, lastColor, "red", lastTapTime, 1520L);
        lastTapTime = 1520L;
        assertEquals(4, tapCount);
        lockedWinner = MainActivity.tryLockWinnerColor(lockedWinner, lastColor, tapCount, false);
        assertEquals("red", lockedWinner);

        // Another player (Blue) later taps 4 times rapidly — lockedWinner must remain "red"
        tapCount = MainActivity.updateColorRapidTapCount(tapCount, lastColor, "blue", lastTapTime, 2000L);
        lastColor = "blue";
        lastTapTime = 2000L;
        tapCount = MainActivity.updateColorRapidTapCount(tapCount, lastColor, "blue", lastTapTime, 2150L);
        lastTapTime = 2150L;
        tapCount = MainActivity.updateColorRapidTapCount(tapCount, lastColor, "blue", lastTapTime, 2300L);
        lastTapTime = 2300L;
        tapCount = MainActivity.updateColorRapidTapCount(tapCount, lastColor, "blue", lastTapTime, 2450L);
        lastTapTime = 2450L;
        assertEquals(4, tapCount);
        lockedWinner = MainActivity.tryLockWinnerColor(lockedWinner, "blue", tapCount, false);
        assertEquals("red", lockedWinner);
    }

    @Test
    public void fourTaps_doesNotForceSixDirectly() {
        assertFalse(MainActivity.shouldForceSix(4));
        for (int roll = 1; roll <= 5; roll++) {
            assertEquals(roll, MainActivity.resolveDiceValue(roll, false, false, 4));
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
    public void smartFairRoll_playsNaturallyMidGameAndProtectsClutchEndgame() {
        // Mid-game: opponent with 2 completed tokens is allowed to complete their 3rd token naturally (reaching 56)
        int[] opponentMidGameSteps = new int[]{54, 56, 56, 20};
        int opponentMidRoll = MainActivity.selectSmartFairRoll(2, false, opponentMidGameSteps, 2, 1, 1, 0);
        assertEquals(2, opponentMidRoll);

        // Endgame: opponent on final 4th piece at step 54 (needs 2 to reach 56 and win match) is blocked from rolling 2
        int[] opponentFinalSteps = new int[]{54, 56, 56, 56};
        int opponentFinalRoll = MainActivity.selectSmartFairRoll(2, false, opponentFinalSteps, 3, 2, 1, 0);
        assertNotEquals(2, opponentFinalRoll);

        // Late-game clutch: favored player with a piece at step 53 (3 steps from home 56) gets 3 to finish in the clutch
        int[] favoredSteps = new int[]{53, 20, 56, 56};
        int favoredRoll = MainActivity.selectSmartFairRoll(2, true, favoredSteps, 2, 2, 1, 0);
        assertEquals(3, favoredRoll);
    }
}
