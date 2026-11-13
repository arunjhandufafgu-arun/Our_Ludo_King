package com.vinaykpro.ludoking;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DiceLogicTest {

    @Test
    public void threeRapidTapsInQuickSuccession_forcesSixForHumanPlayer() {
        int tapCount = 0;
        long lastTapTime = 0L;

        // Tap 1 at t = 1000ms
        tapCount = MainActivity.updateRapidTapCount(tapCount, lastTapTime, 1000L);
        lastTapTime = 1000L;
        assertEquals(1, tapCount);
        assertFalse(MainActivity.shouldForceSix(tapCount));

        // Tap 2 at t = 1200ms (200ms later <= 500ms)
        tapCount = MainActivity.updateRapidTapCount(tapCount, lastTapTime, 1200L);
        lastTapTime = 1200L;
        assertEquals(2, tapCount);
        assertFalse(MainActivity.shouldForceSix(tapCount));

        // Tap 3 at t = 1450ms (250ms later <= 500ms)
        tapCount = MainActivity.updateRapidTapCount(tapCount, lastTapTime, 1450L);
        lastTapTime = 1450L;
        assertEquals(3, tapCount);
        assertTrue(MainActivity.shouldForceSix(tapCount));

        // Verify any random roll (1..5) is overridden to 6 for human player
        for (int randomRoll = 1; randomRoll <= 6; randomRoll++) {
            int result = MainActivity.resolveDiceValue(
                    randomRoll,
                    false,
                    MainActivity.shouldForceSix(tapCount),
                    tapCount
            );
            assertEquals(6, result);
        }
    }

    @Test
    public void singleOrDoubleTap_keepsRandomDiceValue() {
        int tapCount = MainActivity.updateRapidTapCount(0, 0L, 1000L);
        tapCount = MainActivity.updateRapidTapCount(tapCount, 1000L, 1250L);
        assertEquals(2, tapCount);
        assertFalse(MainActivity.shouldForceSix(tapCount));

        for (int randomRoll = 1; randomRoll <= 6; randomRoll++) {
            int result = MainActivity.resolveDiceValue(randomRoll, false, false, tapCount);
            assertEquals(randomRoll, result);
        }
    }

    @Test
    public void slowTapsWithPauseOver500ms_resetsCounterAndDoesNotForceSix() {
        int tapCount = MainActivity.updateRapidTapCount(0, 0L, 1000L);
        tapCount = MainActivity.updateRapidTapCount(tapCount, 1000L, 1300L);
        assertEquals(2, tapCount);

        // Pause of 600ms (> 500ms) resets rapid tap streak back to 1
        tapCount = MainActivity.updateRapidTapCount(tapCount, 1300L, 1900L);
        assertEquals(1, tapCount);
        assertFalse(MainActivity.shouldForceSix(tapCount));
        assertEquals(3, MainActivity.resolveDiceValue(3, false, false, tapCount));
    }

    @Test
    public void botTurn_neverForcesSixEvenIfRapidTapsOccurred() {
        int result = MainActivity.resolveDiceValue(2, true, true, 3);
        assertEquals(2, result);
    }
}
