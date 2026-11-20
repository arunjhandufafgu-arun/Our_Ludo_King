package com.vinaykpro.ludoking;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
    public void naturalTokenCuts_allowsOpponentToCutLockedWinnerThroughoutMatch_butProtectsInCriticalEndgame() {
        // Early/mid game (0 cuts so far, token at step 18, even when locked winner has 3 tokens in base and higher remaining steps):
        // opponent IS allowed to cut locked winner token!
        assertTrue(MainActivity.canAllowFavoredTokenCut(
                1, 0, 18, 1, 224, 150, false, false
        ));

        // Mid and late game cuts in Classic (including step 50 right before home entry and after 6+ previous cuts)
        assertTrue(MainActivity.canAllowFavoredTokenCut(
                1, 1, 34, 2, 160, 145, false, false
        ));
        assertTrue(MainActivity.canAllowFavoredTokenCut(
                1, 5, 50, 2, 140, 145, false, false
        ));
        assertTrue(MainActivity.canAllowFavoredTokenCut(
                1, 8, 44, 3, 95, 45, true, false
        ));

        // Quick mode (gameType == 3): opponent IS allowed to cut locked winner token (0..2 cuts, up to step 50)
        assertTrue(MainActivity.canAllowFavoredTokenCut(
                3, 0, 24, 1, 32, 28, false, false
        ));
        assertTrue(MainActivity.canAllowFavoredTokenCut(
                3, 2, 48, 1, 18, 25, false, false
        ));

        // Team Up mode (gameType == 2): allows up to 14 natural cuts across the 2 teammates (all outer track steps 0..50)
        assertTrue(MainActivity.canAllowFavoredTokenCut(
                2, 0, 22, 1, 210, 180, false, false
        ));
        assertTrue(MainActivity.canAllowFavoredTokenCut(
                2, 7, 50, 3, 140, 145, false, false
        ));
        assertTrue(MainActivity.canAllowFavoredTokenCut(
                2, 12, 46, 4, 110, 48, true, false
        ));
        assertFalse(MainActivity.canAllowFavoredTokenCut(
                2, 14, 20, 3, 130, 140, false, false
        ));

        // Protected after 10 cuts in Classic, or when opponent is near win and locked winner is in a final-token crisis
        assertFalse(MainActivity.canAllowFavoredTokenCut(
                1, 10, 20, 2, 140, 145, false, false
        ));
        assertFalse(MainActivity.canAllowFavoredTokenCut(
                1, 0, 42, 1, 120, 40, true, false
        ));
        assertFalse(MainActivity.canAllowFavoredTokenCut(
                1, 0, 25, 2, 120, 40, true, true
        ));
    }

    @Test
    public void teamUpMode_boardDisplaysTeamAAndTeamBLabels() {
        assertEquals("Team A", MainActivity.formatTeamBoardLabel("blue", "Player 1"));
        assertEquals("Team A", MainActivity.formatTeamBoardLabel("green", "Player 3"));
        assertEquals("Team B", MainActivity.formatTeamBoardLabel("red", "Player 2"));
        assertEquals("Team B", MainActivity.formatTeamBoardLabel("yellow", "Player 4"));
        assertEquals("Team A", MainActivity.formatTeamBoardLabel("blue", "Player A"));
        assertEquals("Team B", MainActivity.formatTeamBoardLabel("red", "p1"));
        assertEquals("Team A - Rahul", MainActivity.formatTeamBoardLabel("blue", "Rahul"));
        assertEquals("Team B - Amit", MainActivity.formatTeamBoardLabel("yellow", "Amit"));
    }

    @Test
    public void teamUpMode_captureRules_cutAllEnemyTokensOnNonSafeSquareAndProtectTeammatesAndSafeSpots() {
        Set<Integer> safeSpots = new HashSet<>(Arrays.asList(0, 8, 13, 21, 26, 34, 39, 47));
        int nonSafeBlock = 20;

        // Team A (Blue + Green) share a non-safe square; Opponent Red (Team B) lands there:
        // BOTH Blue and Green must be captured so neither stays sitting with Red like friendly tokens.
        assertTrue(MainActivity.shouldCaptureToken(2, "red", "blue", nonSafeBlock, true, false, nonSafeBlock, safeSpots));
        assertTrue(MainActivity.shouldCaptureToken(2, "red", "green", nonSafeBlock, true, false, nonSafeBlock, safeSpots));

        // Team B (Red + Yellow) share a non-safe square; Opponent Blue (Team A) lands there:
        // BOTH Red and Yellow must be captured.
        assertTrue(MainActivity.shouldCaptureToken(2, "blue", "red", nonSafeBlock, true, false, nonSafeBlock, safeSpots));
        assertTrue(MainActivity.shouldCaptureToken(2, "blue", "yellow", nonSafeBlock, true, false, nonSafeBlock, safeSpots));

        // Teammates in Team Up mode must NEVER capture each other.
        assertFalse(MainActivity.shouldCaptureToken(2, "blue", "green", nonSafeBlock, true, false, nonSafeBlock, safeSpots));
        assertFalse(MainActivity.shouldCaptureToken(2, "green", "blue", nonSafeBlock, true, false, nonSafeBlock, safeSpots));
        assertFalse(MainActivity.shouldCaptureToken(2, "red", "yellow", nonSafeBlock, true, false, nonSafeBlock, safeSpots));
        assertFalse(MainActivity.shouldCaptureToken(2, "yellow", "red", nonSafeBlock, true, false, nonSafeBlock, safeSpots));

        // Same-color tokens never capture each other.
        assertFalse(MainActivity.shouldCaptureToken(2, "blue", "blue", nonSafeBlock, true, false, nonSafeBlock, safeSpots));

        // Classic mode (gameType == 1): Blue and Green are opponents and capture each other.
        assertTrue(MainActivity.shouldCaptureToken(1, "blue", "green", nonSafeBlock, true, false, nonSafeBlock, safeSpots));

        // Safe spots protect all tokens from capture.
        assertFalse(MainActivity.shouldCaptureToken(2, "red", "blue", 8, true, false, 8, safeSpots));
        assertFalse(MainActivity.shouldCaptureToken(2, "red", "green", 0, true, false, 0, safeSpots));

        // Dead or completed tokens are never captured.
        assertFalse(MainActivity.shouldCaptureToken(2, "red", "blue", nonSafeBlock, false, false, nonSafeBlock, safeSpots));
        assertFalse(MainActivity.shouldCaptureToken(2, "red", "blue", nonSafeBlock, true, true, nonSafeBlock, safeSpots));
    }

    @Test
    public void teamUpMode_finishedTeammateDelegatesDiceControlToPartnerTokens() {
        // Teammate color pairs: Blue <-> Green, Red <-> Yellow
        assertEquals("green", MainActivity.getTeammateColor("blue"));
        assertEquals("blue", MainActivity.getTeammateColor("green"));
        assertEquals("yellow", MainActivity.getTeammateColor("red"));
        assertEquals("red", MainActivity.getTeammateColor("yellow"));

        // While own tokens are still active, player controls own color
        assertEquals("blue", MainActivity.resolveControllablePieceColor(2, "blue", false, false));
        assertEquals("red", MainActivity.resolveControllablePieceColor(2, "red", false, false));

        // Once one teammate completes all 4 tokens in Team Up (gameType == 2), their dice rolls move partner's tokens
        assertEquals("green", MainActivity.resolveControllablePieceColor(2, "blue", true, false));
        assertEquals("blue", MainActivity.resolveControllablePieceColor(2, "green", true, false));
        assertEquals("yellow", MainActivity.resolveControllablePieceColor(2, "red", true, false));
        assertEquals("red", MainActivity.resolveControllablePieceColor(2, "yellow", true, false));

        // In Classic mode (gameType == 1), never delegates to another color
        assertEquals("blue", MainActivity.resolveControllablePieceColor(1, "blue", true, false));
    }

    @Test
    public void safePairCapture_requiresMatchingOrGreaterAttackerCount() {
        // Normal 1v1 cut succeeds
        assertTrue(MainActivity.canSideCaptureEnemySide(1, 1));

        // 1 attacker landing on 2 defending tokens (same color or Team Up pair) does NOT cut either token
        assertFalse(MainActivity.canSideCaptureEnemySide(1, 2));

        // Once a 2nd token from the attacking color/team also lands on the same square (2 vs 2), cuts BOTH
        assertTrue(MainActivity.canSideCaptureEnemySide(2, 2));
        assertTrue(MainActivity.canSideCaptureEnemySide(2, 1));

        // Larger stacks follow the same rule (3 defenders require 3+ attackers)
        assertFalse(MainActivity.canSideCaptureEnemySide(2, 3));
        assertTrue(MainActivity.canSideCaptureEnemySide(3, 3));

        // Zero counts never capture
        assertFalse(MainActivity.canSideCaptureEnemySide(0, 1));
        assertFalse(MainActivity.canSideCaptureEnemySide(1, 0));
    }

    @Test
    public void teamUpMode_mixedHumanBotTurnUsesActiveTurnPlayerControl() {
        // Human finished teammate rolling for Bot partner's pieces -> remains Human-controlled
        assertFalse(MainActivity.resolveEffectiveTurnIsBot(2, false, true));
        // Bot finished teammate rolling for Human partner's pieces -> remains Bot-controlled
        assertTrue(MainActivity.resolveEffectiveTurnIsBot(2, true, false));
        // Classic mode keeps the piece owner's bot flag
        assertTrue(MainActivity.resolveEffectiveTurnIsBot(1, false, true));
        assertFalse(MainActivity.resolveEffectiveTurnIsBot(1, true, false));
    }

    @Test
    public void sameColorTokens_cannotStackOnNonSafeOuterSquare_butAllowedOnSafeSpotsAndHomeStretch() {
        Set<Integer> safeSpots = new HashSet<>(Arrays.asList(0, 8, 13, 21, 26, 34, 39, 47));

        // Non-safe outer square already occupied by another token of the same color -> BLOCKED
        assertTrue(MainActivity.isSameColorLandingBlocked(20, false, true, safeSpots));

        // Non-safe outer square empty (or occupied only by teammate/enemy, not same color) -> ALLOWED
        assertFalse(MainActivity.isSameColorLandingBlocked(20, false, false, safeSpots));

        // Safe spots (stars and starting squares) -> ALLOWED even if same color is already there
        assertFalse(MainActivity.isSameColorLandingBlocked(0, false, true, safeSpots));
        assertFalse(MainActivity.isSameColorLandingBlocked(8, false, true, safeSpots));
        assertFalse(MainActivity.isSameColorLandingBlocked(26, false, true, safeSpots));

        // Home stretch / finish -> ALLOWED even if same color is already there
        assertFalse(MainActivity.isSameColorLandingBlocked(64, true, true, safeSpots));
    }

    @Test
    public void teamUpMode_departureExposureCut_cutsRemainingAllyWhenTeammateLeavesSquareWithEnemy() {
        Set<Integer> safeSpots = new HashSet<>(Arrays.asList(0, 8, 13, 21, 26, 34, 39, 47));
        int nonSafeBlock = 20;

        // Team A (Blue + Green) + Opponent Red share non-safe square 20.
        // Blue moves away -> remaining Team A count = 1, waiting Red count = 1 -> Green MUST be cut immediately!
        assertTrue(MainActivity.shouldCutRemainingAlliesOnDeparture(nonSafeBlock, 1, 1, safeSpots));

        // If Red leaves instead -> remaining Team B count = 0 -> nothing cut
        assertFalse(MainActivity.shouldCutRemainingAlliesOnDeparture(nonSafeBlock, 0, 2, safeSpots));

        // If no enemy was standing on the square when Blue leaves -> Green stays safe
        assertFalse(MainActivity.shouldCutRemainingAlliesOnDeparture(nonSafeBlock, 1, 0, safeSpots));

        // On safe squares (star / start), leaving NEVER triggers a capture
        assertFalse(MainActivity.shouldCutRemainingAlliesOnDeparture(8, 1, 1, safeSpots));
        assertFalse(MainActivity.shouldCutRemainingAlliesOnDeparture(0, 1, 1, safeSpots));
    }
}
