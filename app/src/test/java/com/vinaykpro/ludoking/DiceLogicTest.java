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
    public void sameColorTokens_canAlwaysStackOnOuterTrackSafeSpotsAndHomeStretch() {
        Set<Integer> safeSpots = new HashSet<>(Arrays.asList(0, 8, 13, 21, 26, 34, 39, 47));

        // Non-safe outer square already occupied by another token of the same color -> ALLOWED (never blocks valid move)
        assertFalse(MainActivity.isSameColorLandingBlocked(20, false, true, safeSpots));

        // Non-safe outer square empty -> ALLOWED
        assertFalse(MainActivity.isSameColorLandingBlocked(20, false, false, safeSpots));

        // Safe spots (stars and starting squares) -> ALLOWED
        assertFalse(MainActivity.isSameColorLandingBlocked(0, false, true, safeSpots));
        assertFalse(MainActivity.isSameColorLandingBlocked(8, false, true, safeSpots));
        assertFalse(MainActivity.isSameColorLandingBlocked(26, false, true, safeSpots));

        // Home stretch / finish -> ALLOWED
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

    @Test
    public void threeConsecutiveSixes_forfeitsTurnOnlyOnThirdSix() {
        assertFalse(MainActivity.shouldForfeitTurnOnConsecutiveSixes(0));
        assertFalse(MainActivity.shouldForfeitTurnOnConsecutiveSixes(1));
        assertFalse(MainActivity.shouldForfeitTurnOnConsecutiveSixes(2));
        assertTrue(MainActivity.shouldForfeitTurnOnConsecutiveSixes(3));
        assertTrue(MainActivity.shouldForfeitTurnOnConsecutiveSixes(4));
    }

    @Test
    public void teamUpWinGate_allowsFirstOpponentTeammateToFinishAllFourTokens_andOnlyGatesEighthToken() {
        // Team Up (gameType == 2): first teammate (teammateAlreadyCompleted == false) can finish all 4 tokens freely
        assertFalse(MainActivity.isFinalMatchWinningToken(2, false, 0, 4));
        assertFalse(MainActivity.isFinalMatchWinningToken(2, false, 3, 4));

        // Second teammate (teammateAlreadyCompleted == true): tokens 1..3 finish freely; only 8th token (ownCompleted == 3) is match-winning
        assertFalse(MainActivity.isFinalMatchWinningToken(2, true, 2, 4));
        assertTrue(MainActivity.isFinalMatchWinningToken(2, true, 3, 4));

        // Classic (gameType == 1): only 4th token is match-winning
        assertFalse(MainActivity.isFinalMatchWinningToken(1, false, 2, 4));
        assertTrue(MainActivity.isFinalMatchWinningToken(1, false, 3, 4));

        // Quick (gameType == 3): 1st token is match-winning
        assertTrue(MainActivity.isFinalMatchWinningToken(3, false, 0, 1));
    }

    @Test
    public void consecutiveSixSuppression_preventsMultipleSixStreaksForEveryPlayerSymmetrically() {
        // After rolling one 6, another 6 is suppressed universally for every player (opponent, locked winner, unlocked)
        assertTrue(MainActivity.shouldSuppressConsecutiveSix(1, 6));
        assertTrue(MainActivity.shouldSuppressConsecutiveSix(2, 6));
        assertTrue(MainActivity.shouldPreventOpponentConsecutiveSix(true, false, 1, 6));
        assertTrue(MainActivity.shouldPreventOpponentConsecutiveSix(true, true, 1, 6));
        assertTrue(MainActivity.shouldPreventOpponentConsecutiveSix(false, false, 1, 6));

        // First 6 is always allowed freely; 1..5 after a 6 are allowed freely
        assertFalse(MainActivity.shouldSuppressConsecutiveSix(0, 6));
        assertFalse(MainActivity.shouldSuppressConsecutiveSix(1, 4));
        assertFalse(MainActivity.shouldPreventOpponentConsecutiveSix(true, false, 0, 6));
        assertFalse(MainActivity.shouldPreventOpponentConsecutiveSix(true, false, 1, 3));
    }

    @Test
    public void zeroDoubtPacing_trimsOpponentFinalTokenSurgeAndNudgesTrailingLockedWinner() {
        // Opponent final winning token while locked winner trails -> trims 5/6 rush into home column, keeps 1..4 natural
        assertTrue(MainActivity.shouldTrimOpponentFinalTokenSurge(true, true, 46, 5));
        assertTrue(MainActivity.shouldTrimOpponentFinalTokenSurge(true, true, 45, 6));
        assertFalse(MainActivity.shouldTrimOpponentFinalTokenSurge(true, true, 46, 3));
        assertFalse(MainActivity.shouldTrimOpponentFinalTokenSurge(false, true, 46, 6));
        assertFalse(MainActivity.shouldTrimOpponentFinalTokenSurge(true, false, 46, 6));

        // Mid-game pace nudge activates smoothly when locked winner trails by >14 steps on a natural 1 or 2
        assertTrue(MainActivity.shouldApplyMidGamePaceNudge(130, 110, 0, 1));
        assertTrue(MainActivity.shouldApplyMidGamePaceNudge(130, 110, 0, 2));
        assertFalse(MainActivity.shouldApplyMidGamePaceNudge(130, 110, 0, 4));
        assertFalse(MainActivity.shouldApplyMidGamePaceNudge(115, 110, 0, 1));
        assertTrue(MainActivity.shouldApplyMidGamePaceNudge(120, 110, 2, 2));
    }

    @Test
    public void boardWidthReduction_leavesBalancedSideMarginsAndRespectsShortScreens() {
        // Modern 1080x2400 phone -> 96% width = 1037px (slim 2% Ludo King side margin on left & right)
        assertEquals(1037, MainActivity.computeBoardWidthPx(1080, 2400));
        // Classic 720x1280 phone -> 96% width = 691px
        assertEquals(691, MainActivity.computeBoardWidthPx(720, 1280));
        // Old small 480x800 phone -> 96% width = 461px
        assertEquals(461, MainActivity.computeBoardWidthPx(480, 800));
        // Tiny 320x480 phone -> 96% width = 307px (fits comfortably within 72% of 480 = 346px)
        assertEquals(307, MainActivity.computeBoardWidthPx(320, 480));
        // Very short / square-ish 600x700 display -> capped at 72% of height = 504px so dice pads never clip
        assertEquals(504, MainActivity.computeBoardWidthPx(600, 700));
    }

    @Test
    public void antiLoopEngine_eliminatesTripleRepeatsAlternatingLoopsClusteringAndConsecutiveSixes() {
        // Fresh distinct roll has zero penalty and is preserved untouched
        assertEquals(0, MainActivity.computeAntiLoopPenalty(3, 3, 5, 2, 4));
        assertEquals(3, MainActivity.resolveNaturalAntiLoopRoll(3, 0, 5, 2, 4, 0, 1, 3, 2, 1));

        // Single natural repeat (e.g. 4 then 4) is completely natural (zero penalty)
        assertEquals(0, MainActivity.computeAntiLoopPenalty(4, 4, 4, 2, 5));

        // Triple repeat (4, 4, 4) gets strongly penalized and broken cleanly
        assertTrue(MainActivity.computeAntiLoopPenalty(4, 4, 4, 4, 2) >= 400);
        int brokenTriple = MainActivity.resolveNaturalAntiLoopRoll(4, 0, 4, 4, 2, 2, 2, 2, 2, 3);
        assertNotEquals(4, brokenTriple);
        assertTrue(brokenTriple >= 1 && brokenTriple <= 6);

        // Alternating A-B-A-B loop (5, 4, 5 -> candidate 4) gets broken cleanly
        assertTrue(MainActivity.computeAntiLoopPenalty(4, 4, 5, 4, 5) >= 250);
        int brokenAlt = MainActivity.resolveNaturalAntiLoopRoll(4, 0, 5, 4, 5, 2, 2, 2, 2, 0);
        assertNotEquals(4, brokenAlt);

        // High-frequency clustering (same number already appeared 3+ times in recent 5 rolls) gets broken
        assertTrue(MainActivity.computeRecentWindowLoopPenalty(2, 2, 5, 2, 3, 3) >= 240);
        int brokenCluster = MainActivity.resolveNaturalAntiLoopRoll(2, 0, 5, 2, 3, 3, 2, 2, 2, 1);
        assertNotEquals(2, brokenCluster);

        // Consecutive 6 after a 6 is always resolved to a varied non-6 roll (1..5)
        for (int seed = 0; seed < 5; seed++) {
            int afterSix = MainActivity.resolveNaturalAntiLoopRoll(6, 1, 6, 3, 1, 1, 1, 3, 0, seed);
            assertNotEquals(6, afterSix);
            assertTrue(afterSix >= 1 && afterSix <= 5);
        }

        // Prolonged 6-drought when 0 tokens are alive relieves smoothly with a 6
        assertEquals(6, MainActivity.resolveNaturalAntiLoopRoll(2, 0, 4, 1, 5, 0, 0, 4, 6, 2));
    }

    @Test
    public void sixDroughtRelief_preventsBaseLockoutForBothSidesWithoutDoubleSix() {
        // Locked winner with 0 live tokens relieved at 3+ turns without 6
        assertTrue(MainActivity.shouldGrantSixDroughtRelief(true, 0, 4, 3, 0));
        assertFalse(MainActivity.shouldGrantSixDroughtRelief(true, 0, 4, 2, 0));
        // Locked winner with 1 live token relieved at 7+ turns without 6
        assertTrue(MainActivity.shouldGrantSixDroughtRelief(true, 1, 3, 7, 0));
        assertFalse(MainActivity.shouldGrantSixDroughtRelief(true, 2, 2, 10, 0));
        // Opponent with 0 live tokens relieved at 6+ turns without 6 so opponents never feel starved
        assertTrue(MainActivity.shouldGrantSixDroughtRelief(false, 0, 4, 6, 0));
        assertFalse(MainActivity.shouldGrantSixDroughtRelief(false, 0, 4, 5, 0));
        assertFalse(MainActivity.shouldGrantSixDroughtRelief(false, 1, 3, 10, 0));
        // Never triggers on consecutive six or when no base tokens remain
        assertFalse(MainActivity.shouldGrantSixDroughtRelief(true, 0, 4, 8, 1));
        assertFalse(MainActivity.shouldGrantSixDroughtRelief(true, 1, 0, 10, 0));
    }

    @Test
    public void opponentFinalTokenSurge_trimsEarlierFromStep28AndQuickModeBlocksAllWinningSteps() {
        // Trims 5 and 6 smoothly starting at step 28 while keeping 1..4 completely untouched
        assertTrue(MainActivity.shouldTrimOpponentFinalTokenSurge(true, true, 28, 5));
        assertTrue(MainActivity.shouldTrimOpponentFinalTokenSurge(true, true, 36, 6));
        assertFalse(MainActivity.shouldTrimOpponentFinalTokenSurge(true, true, 28, 4));
        assertFalse(MainActivity.shouldTrimOpponentFinalTokenSurge(true, true, 27, 6));

        // Quick mode (gameType == 3): opponent with 4 tokens at steps 55, 54, 53, 52 (needs 1, 2, 3, 4 to win)
        // must never roll 1, 2, 3, or 4
        int[] fourHomeTokens = new int[]{55, 54, 53, 52};
        for (int raw = 1; raw <= 6; raw++) {
            int chosen = MainActivity.selectSmartFairRoll(raw, false, fourHomeTokens, 0, 0, 3, 0);
            assertTrue("Expected non-winning roll (5 or 6), got " + chosen, chosen == 5 || chosen == 6);
        }
    }

    @Test
    public void tokenDrawablesAndLowerRingResources_areValidAndIntact() throws Exception {
        java.nio.file.Path resDir = java.nio.file.Paths.get("src/main/res");
        java.nio.file.Path innerRingPng = resDir.resolve("drawable/token_inner_ring.png");
        assertTrue("token_inner_ring.png must exist", java.nio.file.Files.exists(innerRingPng));
        assertFalse("corrupt innercircle.png must remain removed", java.nio.file.Files.exists(resDir.resolve("drawable/innercircle.png")));
        assertFalse("stale token_inner_ring.xml must not conflict with PNG", java.nio.file.Files.exists(resDir.resolve("drawable/token_inner_ring.xml")));

        String layoutXml = new String(java.nio.file.Files.readAllBytes(resDir.resolve("layout/activity_main.xml")), java.nio.charset.StandardCharsets.UTF_8);
        int innerRingSrcRefs = layoutXml.split("android:src=\"@drawable/token_inner_ring\"", -1).length - 1;
        int innerRingCompatRefs = layoutXml.split("app:srcCompat=\"@drawable/token_inner_ring\"", -1).length - 1;
        int outerRingSrcRefs = layoutXml.split("android:src=\"@drawable/outercircle\"", -1).length - 1;
        int outerRingCompatRefs = layoutXml.split("app:srcCompat=\"@drawable/outercircle\"", -1).length - 1;
        assertEquals("All 16 tokens must set android:src to @drawable/token_inner_ring", 16, innerRingSrcRefs);
        assertEquals("All 16 tokens must set app:srcCompat to @drawable/token_inner_ring", 16, innerRingCompatRefs);
        assertEquals("All 16 tokens must set android:src to @drawable/outercircle", 16, outerRingSrcRefs);
        assertEquals("All 16 tokens must set app:srcCompat to @drawable/outercircle", 16, outerRingCompatRefs);

        // Verify all token & lower-ring PNGs have valid PNG signature, non-truncated chunks, valid CRCs, and IEND
        String[] criticalPngs = new String[]{
                "drawable/token_inner_ring.png",
                "drawable/outercircle.png",
                "drawable/redpiece.png",
                "drawable/greenpiece.png",
                "drawable/bluepiece.png",
                "drawable-nodpi/yellowpiece.png"
        };
        for (String rel : criticalPngs) {
            byte[] data = java.nio.file.Files.readAllBytes(resDir.resolve(rel));
            assertTrue(rel + " too small", data.length > 33);
            assertEquals((byte) 0x89, data[0]);
            assertEquals((byte) 'P', data[1]);
            assertEquals((byte) 'N', data[2]);
            assertEquals((byte) 'G', data[3]);
            int pos = 8;
            boolean hasIhdr = false;
            boolean hasIend = false;
            while (pos + 8 <= data.length) {
                int len = ((data[pos] & 0xFF) << 24) | ((data[pos + 1] & 0xFF) << 16)
                        | ((data[pos + 2] & 0xFF) << 8) | (data[pos + 3] & 0xFF);
                assertTrue(rel + " truncated chunk at " + pos, pos + 12 + len <= data.length);
                String type = new String(data, pos + 4, 4, java.nio.charset.StandardCharsets.US_ASCII);
                java.util.zip.CRC32 crc = new java.util.zip.CRC32();
                crc.update(data, pos + 4, 4 + len);
                long expectedCrc = ((long) (data[pos + 8 + len] & 0xFF) << 24)
                        | ((long) (data[pos + 9 + len] & 0xFF) << 16)
                        | ((long) (data[pos + 10 + len] & 0xFF) << 8)
                        | ((long) (data[pos + 11 + len] & 0xFF));
                assertEquals(rel + " CRC mismatch in " + type, expectedCrc, crc.getValue());
                if ("IHDR".equals(type)) hasIhdr = true;
                if ("IEND".equals(type)) hasIend = true;
                pos += 12 + len;
            }
            assertTrue(rel + " missing IHDR", hasIhdr);
            assertTrue(rel + " missing IEND", hasIend);
        }
    }
}
