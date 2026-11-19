package com.vinaykpro.ludoking;

import static android.view.View.GONE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    DisplayMetrics displayMetrics;
    double dpHeight;
    double dpWidth;
    double pxWidth;
    double pxHeight;
    ImageView ludoBoard,redHomeBlink,red1activecircle,red2activecircle,red3activecircle,red4activecircle;
    View step1,step2,step3,step4,step5,step6;
    ConstraintLayout completeBackground,redpiece1,redpiece2,redpiece3,redpiece4,greenpiece1,greenpiece2,greenpiece3,greenpiece4,bluepiece1,bluepiece2,bluepiece3,bluepiece4,yellowpiece1,yellowpiece2,yellowpiece3,yellowpiece4;

    float onePercentWidth,onePercentHeight;

    TextView playername1,playername2,playername3,playername4;

    EditText editText;

    int gametype = 1;

    int nop;

    double[] dx = {53.32,53.32,53.32,53.32,53.32,60.04,66.7,73.36,80.02,86.68,93.34,93.34,93.34,86.68,80.02,73.36,66.7 ,60.04,53.32,53.32,53.32,53.32,53.32,53.32,46.66,40   ,40   ,40   ,40   ,40   ,40
                  ,33.34,26.68,20.02,13.36,6.7  ,0.04 ,0.04 ,0.04,6.7  ,13.36,20.02,26.68,33.34, 40  , 40  , 40  , 40  , 40  , 40  ,46.66,53.32,46.66,46.66,46.66,46.66,46.66,46.66,86.68,80.02,73.36,66.7 ,60.04,53.38,46.66,46.66,46.66,46.66,46.66,46.66,6.66 ,13.32,19.98,26.64,33.3 ,39.96};
    double[] dy = { 6.66,13.32,19.98,26.64,33.3 , 40  , 40 , 40  ,  40 ,  40 ,  40 ,46.66,53.32,53.32,53.32,53.32,53.32,53.32,60.04,66.7 ,73.36,80.02,86.68,93.34,93.34,93.34,86.68,80.02,73.36,66.7 ,60.04
                  ,53.32,53.32,53.32,53.32,53.32,53.32,46.66, 40 , 40  , 40  , 40  , 40  , 40  ,33.34,26.68,20.02,13.36,6.7  ,0.04 ,0.04 ,0.04 ,6.66 ,13.32,19.98,26.64,33.3 ,39.96,46.66,46.66,46.66,46.66,46.66,46.66,86.68,80.02,73.36,66.7 ,60.04,53.38,46.66,46.66,46.66,46.66,46.66,46.66};

    float[] x = new float[dy.length];
    float[] y = new float[dy.length];

    float pushXForPieces;
    float pushYForPieces;

    ImageView mainDiceImageView,hintArrow;

    int reddicevalue,greendicevalue,bluedicevalue,yellowdicevalue;

    float pos1[][],pos2[][],pos3[][],pos4[][];

    float pieceWidth,pieceHeight;

    Drawable redCircle,greenCircle,blueCircle,yellowCircle;

    List<Player> players;

    static String currentPlayerColor = "";
    static  int currentPlayerPosition = 3;

    static int currentPlayerDice = 6;

    int currentPlayerIndex = 0;

    String currentPlayerName;

    int currentPlayerSelectedIndex = 0;

    List<Piece> rp,gp,bp,yp;

    int[] wBlocks52,wBlocks58,wBlocks64,wBlocks70;

    static boolean isDiceMovableExtraChance = false;
    Dice d;

    MediaPlayer gameStartSound,diceRollSound,stepSound,safeSound,deathSound,pantaSound,congratulationSound;
    int sizeOfBox;

    float blockSize;

    Piece autoMove;

    HashSet<Integer> safeSpots;

    boolean normalPiece = true;

    private AnimatorSet[] animatorSets;

    ImageView crownIndex1,crownIndex2,crownIndex3,crownIndex4;

    int currentWinnerPosition = 1;

    // ingame menu button and items
    ConstraintLayout ingamemenuitemslayout;
    ImageView ingamemenubtn,menuremoveplayersbtn,menuexitbtn;

    // ingameexitgamelayout
    ConstraintLayout quitgamelayout;
    ImageView ingameyesbtn,ingamenobtn,ingamesoundbtn,ingamemusicbtn;

    // ingame rmp layout
    ConstraintLayout ingamermplayout,rmp1bg,rmp2bg,rmp3bg,rmp4bg;
    ImageView rmpp1piece,rmpp2piece,rmpp3piece,rmpp4piece,rmpp1removeicon,rmpp2removeicon,rmpp3removeicon,rmpp4removeicon,rmpbackbtn,rmpmenubtn;
    TextView rmpp1name, rmpp2name,rmpp3name,rmpp4name;

    // ingmae rmp sublayout confirmrmplayout
    ConstraintLayout confirmrmplayout;
    ImageView selectedrmppiece,confirmrmpyesbtn,confirmrmpnobtn;

    int selectedconfirmrmpplayerindex = -1;

    int rmpindexforp1=1,rmpindexforp2=2,rmpindexforp3=3,rmpindexforp4=4;

    // players exit boxes
    ImageView p1exitbox,p2exitbox,p3exitbox,p4exitbox;
    String player1color,player2color,player3color,player4color;
    String player1name,player2name,player3name,player4name;

    int currentWinnerPlayerIndex = -1;

    // Congratulations screen
    ConstraintLayout congratulationslayout;
    ImageView congratsmenubtn,congratssoundbtn,congratssharebtn,congratsreplaybtn;

    ConstraintLayout winnerlistp1layout,winnerlistp2layout,winnerlistp3layout,winnerlistp4layout;
    ImageView wlistcrown1,wlistcrown2,wlistcrown3,wlistcrown4,wlistpiece1,wlistpiece2,wlistpiece3,wlistpiece4,wlistwinorlose1,wlistwinorlose2,wlistwinorlose3,wlistwinorlose4;
    TextView wlistname1,wlistname2,wlistname3,wlistname4;

    boolean isbluegreenreadytowin=false,isredyellowreadytowin=false;

    Handler globalHandler;

    SharedPreferences sharedPreferences;

    boolean isSoundOn=true,isMusicOn=false;

    int botwins=0,botloses=0;

    ImageView gameStartImageView;

    class Piece
    {
        // managed by constructor
        String colour;
        ConstraintLayout piece;
        ImageView readyToPick;
        int startPosition;

        int numberOfSteps = 0;

        int endPosition;
        float defX;
        float defY;

        int diceValue;

        // managed at runtime
        boolean isAlive = false;
        boolean isClickable = true;
        boolean isBotPiece;
        boolean isReadyToEnterWinnerZone = false;
        int currBlock = -1;

        boolean hasCompletedItsPurpose = false;

        int currWinnerBlock = 0;

        int[] winnerBlocks;

        ImageView pieceIcon,pieceStandIcon;
        boolean isThisPlayerWon = false;


        private final ObjectAnimator rotateAnimator;

        Piece(String color,ConstraintLayout piece,ImageView readyToPick,ImageView pieceIcon,ImageView pieceStandIcon,int startPosition,float defX,float defY,boolean isBotPiece)
        {
            this.colour = color;
            this.piece = piece;
            this.readyToPick = readyToPick;
            this.pieceIcon = pieceIcon;
            this.pieceStandIcon = pieceStandIcon;
            this.startPosition = startPosition;
            this.defX = defX;
            this.defY = defY;
            this.isBotPiece = isBotPiece;
            this.piece.setTranslationX(defX);
            this.piece.setTranslationY(defY);
            this.readyToPick.setVisibility(View.INVISIBLE);
            this.piece.setVisibility(View.VISIBLE);
            this.endPosition = startPosition!=0?startPosition-2:50;
            if(!isBotPiece) {
                this.piece.setOnClickListener(view -> {
                    String controlColor = getActivePieceControlColor(currentPlayerColor);
                    if (isAlive && isClickable && controlColor.equals(colour)) {
                        diceValue = currentPlayerDice;
                        currentPlayerDice = -1;
                        for (Piece p : getPiecesByColor(colour)) {
                            p.inactiveState();
                        }
                        checkAdjustments(currBlock);
                        move(diceValue);
                        //Toast.makeText(MainActivity.this, x+"", Toast.LENGTH_SHORT).show();
                    } else if (!isAlive && controlColor.equals(colour) && currentPlayerDice == 6) {
                        currentPlayerDice = -1;
                        for (Piece p : getPiecesByColor(colour)) {
                            p.inactiveState();
                        }
                        makeAlive();
                    }
                });
            }
            if(!normalPiece) {
                pieceIcon.setScaleX(0.8f);pieceIcon.setScaleY(0.8f);
                pieceIcon.setImageDrawable(getStylishIconDrawableByColor(this.colour));
                pieceStandIcon.setVisibility(GONE);
                ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) readyToPick.getLayoutParams();
                params.topMargin = 0;
                params.topToTop = pieceIcon.getId();
                params.leftToLeft = pieceIcon.getId();
                params.bottomToBottom = pieceIcon.getId();
                params.rightToRight = pieceIcon.getId();
                this.readyToPick.setLayoutParams(params);
                readyToPick.setScaleX(1.2f);readyToPick.setScaleY(1.2f);
            }

            /*piece.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    die();
                    return false;
                }
            });*/
            rotateAnimator = ObjectAnimator.ofFloat(readyToPick,"rotation",360,0);
            rotateAnimator.setDuration(900);
            rotateAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            rotateAnimator.setRepeatMode(ObjectAnimator.RESTART);
            rotateAnimator.setInterpolator(new LinearInterpolator());
            rotateAnimator.start();
        }

        void onClickForBot() {
            String controlColor = getActivePieceControlColor(currentPlayerColor);
            if (isAlive && isClickable && controlColor.equals(colour)) {
                diceValue = currentPlayerDice;
                currentPlayerDice = -1;
                for (Piece p : getPiecesByColor(colour)) {
                    p.inactiveState();
                }
                checkAdjustments(currBlock);
                move(diceValue);
                //Toast.makeText(MainActivity.this, x+"", Toast.LENGTH_SHORT).show();
            } else if (!isAlive && controlColor.equals(colour) && currentPlayerDice == 6) {
                currentPlayerDice = -1;
                for (Piece p : getPiecesByColor(colour)) {
                    p.inactiveState();
                }
                makeAlive();
            }
        }

        void makeAlive()
        {
            isAlive = true;
            currBlock = startPosition;
            if (d != null && d.lockedWinnerColor != null && isFavoredColor(this.colour, d.lockedWinnerColor, gametype)) {
                d.favoredNeedsReopenAfterCut = false;
            }
            piece.animate().translationX(x[startPosition]+pushXForPieces).translationY(y[startPosition]-pushYForPieces).setDuration(400).start(); // 16 75
            globalHandler.postDelayed(() -> {
                isDiceMovableExtraChance = true;
                if(!isBotPiece) { hintArrow.setVisibility(View.VISIBLE); }
                checkAdjustments(currBlock);
                if(currBlock>=24 && currBlock<=51) {
                    piece.setElevation(51-currBlock);
                } else if(currBlock>=64 && currBlock<=69) {
                    piece.setElevation(70-currBlock);
                } else {
                    piece.setElevation(currBlock);
                }
            },400);
            globalHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(isBotPiece) {
                        d.roll();
                    }
                }
            },550);
        }

        void die()
        {
            die(true);
        }

        void die(boolean playSound)
        {
            isAlive = false;
            isClickable = false;
            isReadyToEnterWinnerZone = false;
            checkAdjustments(currBlock);
            currWinnerBlock=0;
            numberOfSteps = 0;
            if (d != null && d.lockedWinnerColor != null && !d.lockedWinnerColor.isEmpty()) {
                if (isFavoredColor(this.colour, d.lockedWinnerColor, gametype)) {
                    d.favoredTimesCut++;
                    d.favoredNeedsReopenAfterCut = true;
                    d.turnsSinceFavoredCut = 0;
                    d.opponentTurnsSinceCutFavored = 0;
                } else {
                    d.opponentTimesCut++;
                }
            }
            if(isSoundOn && playSound) {
                final MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.death);
                if (mp != null) {
                    deathSound = mp;
                    mp.start();
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            mediaPlayer.release();
                        }
                    });
                }
            }


            Runnable r = new Runnable() {
                @Override
                public void run() {
                    if(currBlock!=startPosition)
                    {
                        if(currBlock==0) currBlock=51; else --currBlock;
                        piece.setTranslationX(x[currBlock]+pushXForPieces);
                        piece.setTranslationY(y[currBlock]-pushYForPieces);
                        globalHandler.postDelayed(this,20);
                    } else {
                        if (!isAlive) {
                            currBlock = -1;
                        }
                        if(piece.getScaleX()<1.0f) {
                            piece.setScaleX(1.0f);
                            piece.setScaleY(1.0f);
                        }
                        piece.animate().translationX(defX).translationY(defY).setDuration(400).start();
                        globalHandler.removeCallbacks(this);
                    }
                }
            };
            globalHandler.post(r);
            //piece.animate().translationX(defX).translationY(defY).setDuration(400).start();
        }

        void activeState()
        {
            isClickable = true;
            rotateAnimator.start();
            readyToPick.setVisibility(View.VISIBLE);
            if(piece.getScaleX()<1.0f) {
                piece.setScaleX(0.95f);
                piece.setScaleY(0.95f);
            } else {
                piece.setScaleX(1.0f);
                piece.setScaleY(1.0f);
            }
        }

        void inactiveState()
        {
            isClickable = false;
            rotateAnimator.cancel();
            readyToPick.setVisibility(View.INVISIBLE);
            if(currBlock!=-1) {
                checkAdjustments(currBlock);
            }
        }

        void move(int n) {
            isClickable = false;
            if(stepSound!=null) {
                if(isSoundOn) {
                    if (!stepSound.isPlaying()) {
                        stepSound.seekTo(120);
                        stepSound.start();
                    } else if (stepSound.isPlaying()) {
                        stepSound.pause();
                        stepSound.seekTo(120);
                        stepSound.start();
                    }
                }
            }



            if (currBlock == -1) {
                currBlock = startPosition;
            }

            if(currBlock>=51)
                currBlock = 0;
            else
                currBlock++;

            if(currBlock==0) { checkAdjustments(51); } else { checkAdjustments(currBlock-1); }

            if(isReadyToEnterWinnerZone)
            {
                currBlock = winnerBlocks[currWinnerBlock];
                currWinnerBlock++;
            }

            if(currBlock==endPosition) { winnerBlocks = getWinnerBlocks(endPosition); isReadyToEnterWinnerZone = true; }

            piece.animate().translationX(x[currBlock] + pushXForPieces).translationY(y[currBlock] - pushYForPieces).setDuration(220).start();
            /*if(!isReadyToEnterWinnerZone)
            {
                piece.animate().translationX(x[currBlock] + pushXForPieces).translationY(y[currBlock] - pushYForPieces).setDuration(300).start();
            } else {
                piece.animate().translationX(x[winnerBlocks[currWinnerBlock]] + pushXForPieces).translationY(y[winnerBlocks[currWinnerBlock]] - pushYForPieces).setDuration(300).start();
            }*/

            PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.0f, 1.4f);
            PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.0f, 1.4f);
            ObjectAnimator popUpAnimator = ObjectAnimator.ofPropertyValuesHolder(piece, scaleX, scaleY);
            popUpAnimator.setDuration(100);
            scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.4f, 1.0f);
            scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.4f, 1.0f);
            ObjectAnimator popDownAnimator = ObjectAnimator.ofPropertyValuesHolder(piece, scaleX, scaleY);
            popDownAnimator.setDuration(90);

            /*ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, View.ALPHA, 1.0f, 0.0f);
            alphaAnimator.setDuration(duration);*/

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playSequentially(popUpAnimator,popDownAnimator);
            animatorSet.start();




            globalHandler.postDelayed(() -> {
                if(n>1) {
                    int step = currBlock;
                    globalHandler.postDelayed( ()-> {
                        showStep(n,this.colour,x[step],y[step]);
                    },100);
                    numberOfSteps++;
                    move(n - 1);
                } else {
                    showStep(n,this.colour,x[currBlock],y[currBlock]);
                    isThisPlayerWon = false;
                    if(currBlock>=24 && currBlock<=51) {
                        piece.setElevation(51-currBlock);
                    } else if(currBlock>=64 && currBlock<=69) {
                        piece.setElevation(70-currBlock);
                    } else {
                        piece.setElevation(currBlock);
                    }
                    stepSound.pause();
                    numberOfSteps++;

                    boolean isDeadChanceAvailable = false;

                    if(safeSpots.contains(currBlock) && isSoundOn)
                    {
                        safeSound = MediaPlayer.create(MainActivity.this,R.raw.safe);
                        safeSound.start();
                        safeSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                            @Override
                            public void onCompletion(MediaPlayer mediaPlayer) {
                                safeSound.release();
                            }
                        });
                    } else if(currWinnerBlock>0) {
                        int temp = 0;
                        if(currWinnerBlock>5)
                        {
                            hasCompletedItsPurpose = true;
                            if(isSoundOn) {
                                pantaSound = MediaPlayer.create(MainActivity.this, R.raw.panta);
                                pantaSound.start();
                                pantaSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                    @Override
                                    public void onCompletion(MediaPlayer mediaPlayer) {
                                        pantaSound.release();
                                    }
                                });
                            }

                            List<Piece> pieces = getPiecesByColor(this.colour);
                            for(Piece p: pieces) { if(p.hasCompletedItsPurpose) { temp++; } }

                            int pantaValue = 4;
                            if(gametype==3) { pantaValue = 1; }

                            if(temp>=pantaValue) { //4
                                if (gametype == 2) {
                                    String teammateColor = getTeammateColor(this.colour);
                                    boolean teamComplete = isColorAllPiecesCompleted(teammateColor);
                                    if (this.colour.equals("red") || this.colour.equals("yellow")) {
                                        if (isredyellowreadytowin || teamComplete) {
                                            isThisPlayerWon = true;
                                            isDeadChanceAvailable = isDiceMovableExtraChance = false;
                                            redHomeBlink.clearAnimation();
                                            redHomeBlink.setVisibility(GONE);
                                            mainDiceImageView.setVisibility(GONE);
                                            hintArrow.setVisibility(GONE);
                                            globalHandler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    checkPantaAdjustments(colour, currBlock);
                                                }
                                            }, 50);
                                            setRedYellowTeamAsWinners();
                                            return;
                                        } else {
                                            isredyellowreadytowin = true;
                                        }
                                    } else {
                                        if (isbluegreenreadytowin || teamComplete) {
                                            isThisPlayerWon = true;
                                            isDeadChanceAvailable = isDiceMovableExtraChance = false;
                                            redHomeBlink.clearAnimation();
                                            redHomeBlink.setVisibility(GONE);
                                            mainDiceImageView.setVisibility(GONE);
                                            hintArrow.setVisibility(GONE);
                                            globalHandler.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    checkPantaAdjustments(colour, currBlock);
                                                }
                                            }, 50);
                                            stopEverything();
                                            setBlueGreenTeamAsWinners();
                                            showGameOverScreen();
                                            mainDiceImageView.setVisibility(GONE);
                                            hintArrow.setVisibility(GONE);
                                            return;
                                        } else {
                                            isbluegreenreadytowin = true;
                                        }
                                    }
                                    // Real Ludo King Team Up rule: when one teammate completes all 4 tokens,
                                    // keep them in the turn order with their dice visible so they continue rolling
                                    // to move their partner's remaining tokens (including the bonus roll for reaching home).
                                    isThisPlayerWon = false;
                                    globalHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            checkPantaAdjustments(colour, currBlock);
                                        }
                                    }, 50);
                                    isDiceMovableExtraChance = true;
                                    d.isDiceClickable = true;
                                    if (!isBotPiece) {
                                        hintArrow.setVisibility(View.VISIBLE);
                                    } else {
                                        globalHandler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                d.roll();
                                            }
                                        }, 150);
                                    }
                                    return;
                                }

                                isThisPlayerWon = true;
                                isDeadChanceAvailable = isDiceMovableExtraChance = false;
                                if(players.size()==2) {
                                    int x;
                                    if(currentPlayerIndex==0) { x=players.size()-1; } else { x=currentPlayerIndex-1; }
                                    if(gametype!=3) {
                                        globalHandler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                checkPantaAdjustments(colour, currBlock);
                                            }
                                        }, 50);
                                    }
                                    redHomeBlink.clearAnimation();
                                    redHomeBlink.setVisibility(GONE);
                                    mainDiceImageView.setVisibility(GONE);
                                    players.remove(x);
                                    setCurrentPlayerAsWinnerAtCurrentPosition(currentPlayerSelectedIndex, colour, currentPlayerName);
                                    String name = players.get(0).name,colour = players.get(0).color;
                                    int loserplayerselectedindex = players.get(0).index;

                                    if(gametype==4) {
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        if(loserplayerselectedindex!=0) {
                                            botwins += 1;
                                            editor.putInt("botwins",botwins);
                                        } else {
                                            botloses += 1;
                                            editor.putInt("botloses",botloses);
                                        }
                                        ((TextView)findViewById(R.id.userwins)).setText((botwins+""));
                                        ((TextView)findViewById(R.id.userloses)).setText((botloses+""));

                                        editor.apply();
                                    }

                                    hideThisPlayerDiceBg(loserplayerselectedindex);
                                    if(nop==2) {
                                        wlistcrown2.setVisibility(View.INVISIBLE);
                                        wlistname2.setText(name);
                                        wlistpiece2.setImageDrawable(getPieceDrawableByColor(colour));
                                        winnerlistp3layout.setVisibility(GONE);
                                        winnerlistp4layout.setVisibility(GONE);
                                    } else if(nop==3) {
                                        //makeThisPlayerLoserBasedOnSelectedIndexNOP3(colour,loserplayerselectedindex);
                                        wlistcrown3.setVisibility(View.INVISIBLE);
                                        wlistname3.setText(getLoserNameBasedOnSelectedIndex(loserplayerselectedindex));
                                        wlistpiece3.setImageDrawable(getPieceDrawableByColor(colour));
                                        winnerlistp4layout.setVisibility(GONE);
                                    } else {
                                        wlistcrown4.setVisibility(View.INVISIBLE);
                                        wlistname4.setText(getLoserNameBasedOnSelectedIndex(loserplayerselectedindex));
                                        wlistpiece4.setImageDrawable(getPieceDrawableByColor(colour));
                                    }
                                    showGameOverScreen();
                                    return;
                                } else {
                                    int x;
                                    if (currentPlayerIndex == 0) {
                                        x = players.size() - 1;
                                    } else {
                                        x = currentPlayerIndex - 1;
                                    }
                                    currentWinnerPlayerIndex = currentPlayerIndex;
                                    //checkAdjustments(currBlock);
                                    setCurrentPlayerAsWinnerAtCurrentPosition(currentPlayerSelectedIndex, colour, currentPlayerName);

                                    currentPlayerIndex = x;
                                    menuremoveplayersbtn.setVisibility(GONE);
                                    players.remove(currentPlayerIndex);
                                    if (currentPlayerIndex == players.size()) {
                                        currentPlayerIndex=0;
                                    }
                                    //Toast.makeText(MainActivity.this, currentPlayerIndex + "", Toast.LENGTH_SHORT).show();
                                    switchPlayers();
                                    if(gametype!=3) {
                                        globalHandler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                checkPantaAdjustments(colour,currBlock);
                                            }
                                        }, 50);
                                    }
                                    d.isDiceClickable = true;
                                    return;
                                }
                            }
                        }
                    } else {
                        isDeadChanceAvailable = checkDeath(this,currBlock);
                    }

                    globalHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(!isThisPlayerWon) { checkAdjustments(currBlock); }
                        }
                    },10);
                    //checkAdjustments(currBlock);

                    if(diceValue == 6 || currWinnerBlock>5 || isDeadChanceAvailable && !isThisPlayerWon) {
                        isDiceMovableExtraChance = true;
                        if(!isBotPiece) { hintArrow.setVisibility(View.VISIBLE); } else {
                            globalHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    d.roll();
                                }
                            },150);
                        }
                    } else {
                        switchPlayers();
                        d.isDiceClickable = true;
                    }
                }
            }, 200); //325
        }



        public boolean check(int diceValue) {
            if (hasCompletedItsPurpose || isThisPlayerWon) {
                inactiveState();
                return false;
            }
            if(diceValue == 6)
            {
                if(!isAlive || (numberOfSteps+diceValue)<57)
                {
                    activeState();
                    return true;
                }
                inactiveState();
                return false;
            } else if(isAlive && (numberOfSteps+diceValue)<57) {
                activeState();
                return true;
            } else {
                inactiveState();
                return false;
            }
        }
    }

    private void setBlueGreenTeamAsWinners() {
        findViewById(R.id.constraintLayout8).setVisibility(GONE);
        findViewById(R.id.imageView114).setVisibility(GONE);
        findViewById(R.id.teamslayout).setVisibility(View.VISIBLE);

        ((ImageView)findViewById(R.id.imageView132)).setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.team1header,null));

        ((ImageView)findViewById(R.id.imageView137)).setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.team2header,null));

        ((ImageView)findViewById(R.id.imageView133)).setImageDrawable(getPieceDrawableByColor("blue"));
        ((ImageView)findViewById(R.id.imageView135)).setImageDrawable(getPieceDrawableByColor("green"));
        ((ImageView)findViewById(R.id.imageView138)).setImageDrawable(getPieceDrawableByColor("red"));
        ((ImageView)findViewById(R.id.imageView140)).setImageDrawable(getPieceDrawableByColor("yellow"));

        ((TextView)findViewById(R.id.team1name1)).setText(player3name);
        ((TextView)findViewById(R.id.team1name2)).setText(player2name);
        ((TextView)findViewById(R.id.team2name1)).setText(player1name);
        ((TextView)findViewById(R.id.team2name2)).setText(player4name);
    }

    private void setRedYellowTeamAsWinners() {
        stopEverything();
        findViewById(R.id.constraintLayout8).setVisibility(GONE);
        findViewById(R.id.imageView114).setVisibility(GONE);
        findViewById(R.id.teamslayout).setVisibility(View.VISIBLE);
        showGameOverScreen();
        mainDiceImageView.setVisibility(GONE);
        hintArrow.setVisibility(GONE);

        ((ImageView)findViewById(R.id.imageView132)).setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.team2header,null));

        ((ImageView)findViewById(R.id.imageView137)).setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.team1header,null));

        ((ImageView)findViewById(R.id.imageView133)).setImageDrawable(getPieceDrawableByColor("red"));
        ((ImageView)findViewById(R.id.imageView135)).setImageDrawable(getPieceDrawableByColor("yellow"));
        ((ImageView)findViewById(R.id.imageView138)).setImageDrawable(getPieceDrawableByColor("blue"));
        ((ImageView)findViewById(R.id.imageView140)).setImageDrawable(getPieceDrawableByColor("green"));

        ((TextView)findViewById(R.id.team1name1)).setText(player1name);
        ((TextView)findViewById(R.id.team1name2)).setText(player4name);
        ((TextView)findViewById(R.id.team2name1)).setText(player3name);
        ((TextView)findViewById(R.id.team2name2)).setText(player2name);
    }

    private String getLoserNameBasedOnSelectedIndex(int loserplayerselectedindex) {
        switch (loserplayerselectedindex+1) {
            case 1: return player3name;
            case 2: return player1name;
            case 3: return player2name;
            case 4: return player4name;
        }
        return player3name;
    }

    private void showGameOverScreen() {
        congratulationslayout.setVisibility(View.VISIBLE);

        if(isSoundOn) {
            congratulationSound = MediaPlayer.create(this, R.raw.congratulations);
            if (congratulationSound != null) {
                congratulationSound.setLooping(true);
                congratulationSound.start();
            }
        }

        ImageView imageView = findViewById(R.id.fireworksview);
        Glide.with(this)
                .asGif()
                .load(R.drawable.fireworksimg)
                .into(new DrawableImageViewTarget(imageView) {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        if (resource instanceof GifDrawable) {
                            GifDrawable gifDrawable = (GifDrawable) resource;
                            gifDrawable.setLoopCount(GifDrawable.LOOP_FOREVER);
                            gifDrawable.start();
                        }
                    }
                }.getView());
    }

    private void makeThisPlayerLoserBasedOnSelectedIndexNOP3(String colour, int loserplayerselectedindex) {
        switch ((loserplayerselectedindex+1)) {
            case 1:
                if(nop==3) {
                    wlistcrown3.setVisibility(View.INVISIBLE);
                    wlistpiece3.setImageDrawable(getPieceDrawableByColor(colour));
                    wlistname3.setText(player3name);
                    wlistwinorlose1.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.loser1, null));
                }
                hideThisPlayerDiceBg(1);
                break;
            case 2:
                if(nop==3) {
                    wlistcrown3.setVisibility(View.INVISIBLE);
                    wlistpiece3.setImageDrawable(getPieceDrawableByColor(colour));
                    wlistname3.setText(player1name);
                    wlistwinorlose1.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.loser1, null));
                }
                hideThisPlayerDiceBg(2);
                break;
            case 3:
                if(nop==3) {
                    wlistcrown3.setVisibility(View.INVISIBLE);
                    wlistpiece3.setImageDrawable(getPieceDrawableByColor(colour));
                    wlistname3.setText(player2name);
                    wlistwinorlose1.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.loser1, null));
                }
                hideThisPlayerDiceBg(3);
                break;
            case 4:
                if(nop==3) {
                    wlistcrown3.setVisibility(View.INVISIBLE);
                    wlistpiece3.setImageDrawable(getPieceDrawableByColor(colour));
                    wlistname3.setText(player4name);
                    wlistwinorlose1.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.loser1, null));
                }
                hideThisPlayerDiceBg(4);
                break;
        }
    }

    private void setCurrentPlayerAsWinnerAtCurrentPosition(int currentPlayerSelectedIndex,String colour,String playername) {
        switch ((currentPlayerSelectedIndex+1)) {
            case 1:
                crownIndex1.setVisibility(View.VISIBLE);
                findViewById(R.id.winnerindexnumber1).setVisibility(View.VISIBLE);
                ((ImageView)findViewById(R.id.winnerindexnumber1)).setImageDrawable(getWinnerNumberDrawableByNumber(currentWinnerPosition));
                if(nop!=2) {
                    addCurrentPlayerToCongratsList(colour, player3name, currentWinnerPosition);
                }
                else {
                    addCurrentPlayerToCongratsList(colour, playername, currentWinnerPosition);
                }

                hideThisPlayerDiceBg(1);
                break;
            case 2:
                crownIndex2.setVisibility(View.VISIBLE);
                findViewById(R.id.winnerindexnumber2).setVisibility(View.VISIBLE);
                ((ImageView)findViewById(R.id.winnerindexnumber2)).setImageDrawable(getWinnerNumberDrawableByNumber(currentWinnerPosition));
                if(nop!=2) {
                    addCurrentPlayerToCongratsList(colour, player1name, currentWinnerPosition);
                }
                else {
                    addCurrentPlayerToCongratsList(colour, playername, currentWinnerPosition);
                }
                hideThisPlayerDiceBg(2);
                break;
            case 3:
                crownIndex3.setVisibility(View.VISIBLE);
                findViewById(R.id.winnerindexnumber3).setVisibility(View.VISIBLE);
                ((ImageView)findViewById(R.id.winnerindexnumber3)).setImageDrawable(getWinnerNumberDrawableByNumber(currentWinnerPosition));
                if(nop!=2) {
                    addCurrentPlayerToCongratsList(colour, player2name, currentWinnerPosition);
                }
                else {
                    addCurrentPlayerToCongratsList(colour, playername, currentWinnerPosition);
                }
                hideThisPlayerDiceBg(3);
                break;
            case 4:
                crownIndex4.setVisibility(View.VISIBLE);
                findViewById(R.id.winnerindexnumber4).setVisibility(View.VISIBLE);
                ((ImageView)findViewById(R.id.winnerindexnumber4)).setImageDrawable(getWinnerNumberDrawableByNumber(currentWinnerPosition));
                addCurrentPlayerToCongratsList(colour,playername,currentWinnerPosition);
                hideThisPlayerDiceBg(4);
                break;
        }
        currentWinnerPosition++;
    }

    private void addCurrentPlayerToCongratsList(String color,String name,int currentWinnerPosition) {
        switch (currentWinnerPosition) {
            case 1:
                wlistcrown1.setVisibility(View.VISIBLE);
                wlistcrown2.setVisibility(View.INVISIBLE);
                wlistpiece1.setImageDrawable(getPieceDrawableByColor(color));
                wlistname1.setText(name);
                wlistwinorlose1.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.winner1,null));
                break;
            case 2:
                wlistcrown2.setVisibility(View.VISIBLE);
                wlistcrown3.setVisibility(View.INVISIBLE);
                wlistpiece2.setImageDrawable(getPieceDrawableByColor(color));
                wlistname2.setText(name);
                wlistwinorlose2.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.winner1,null));
                break;
            case 3:
                wlistcrown3.setVisibility(View.VISIBLE);
                wlistcrown4.setVisibility(View.INVISIBLE);
                wlistpiece3.setImageDrawable(getPieceDrawableByColor(color));
                wlistname3.setText(name);
                wlistwinorlose3.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.winner1,null));
                break;
            case 4:
                wlistcrown4.setVisibility(View.VISIBLE);
                wlistcrown4.setVisibility(View.INVISIBLE);
                wlistpiece1.setImageDrawable(getPieceDrawableByColor(color));
                wlistname1.setText(name);
                wlistwinorlose4.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.loser1,null));
                break;
        }
    }

    Drawable getWinnerNumberDrawableByNumber(int x) {
        switch (x) {
            case 1:
                return ResourcesCompat.getDrawable(getResources(),R.drawable.winner1number,null);
            case 2:
                return ResourcesCompat.getDrawable(getResources(),R.drawable.winner2number,null);
            case 3:
                return ResourcesCompat.getDrawable(getResources(),R.drawable.winner3number,null);
            case 4:
                return ResourcesCompat.getDrawable(getResources(),R.drawable.winner4number,null);
        }
        return null;
    }

    Drawable getWinnerCrownDrawableByNumber(int x) {
        switch (x) {
            case 1:
                return ResourcesCompat.getDrawable(getResources(),R.drawable.listwinner1crown,null);
            case 2:
                return ResourcesCompat.getDrawable(getResources(),R.drawable.listwinner2crown,null);
            case 3:
                return ResourcesCompat.getDrawable(getResources(),R.drawable.listwinner3crown,null);
            case 4:
                return null;
        }
        return null;
    }

    private Drawable getStylishIconDrawableByColor(String colour) {
        switch(colour){
            case "red":
                return ResourcesCompat.getDrawable(getResources(),R.drawable.redpiecestylish,null);
            case "blue":
                return ResourcesCompat.getDrawable(getResources(),R.drawable.bluepiecestylish,null);
            case "green":
                return ResourcesCompat.getDrawable(getResources(),R.drawable.greenpiecestylish,null);
            case "yellow":
                return ResourcesCompat.getDrawable(getResources(),R.drawable.yellowpiecestylish,null);
        }
        return null;
    }

    void checkPantaAdjustments(String color,int targetBox) {
        int pantaBox=targetBox;
        boolean horizontal = (pantaBox >= 0 && pantaBox <= 4) || (pantaBox >= 18 && pantaBox <= 30) || (pantaBox >= 44 && pantaBox <= 51) || (pantaBox >= 52 && pantaBox <= 57) || (pantaBox >= 64 && pantaBox <= 69);
        boolean isWon = false;
        List<Piece> pairs = getPiecesByColor(color);
        for(Piece p : pairs) { if(p.hasCompletedItsPurpose) { isWon = true; } else {isWon = false; break;} }
        if(isWon) {
            if(horizontal) {
                float elevation = pairs.get(0).piece.getElevation();
                float extra = 4 * displayMetrics.density, yextra = 17 * displayMetrics.density;
                float xt = x[targetBox] + pushXForPieces, yt = y[targetBox];
                float scale = 100f / ((4) * 30 + 75);
                float gap = 100f - scale;
                //Toast.makeText(this, scale + "", Toast.LENGTH_SHORT).show();
                float space = blockSize - (pieceWidth * scale * 0.35f * 4 + pieceWidth * scale * 0.65f) / 2;
                if (space <= 0) {
                    space = 0;
                }
                space = 0;
                //scale = 0.70f;
                for (int i = 0; i < 4; i++) {
                    Piece p = pairs.get(i);
                    ConstraintLayout piece = p.piece;
                    piece.setElevation(elevation++);
                    piece.setScaleX(scale);
                    piece.setScaleY(scale);
                    piece.setTranslationX(xt - ((blockSize - (pieceWidth * scale)) / 2) + ((pieceWidth * scale * 0.30f) * i) - (extra * scale * (4 / 2)) + space);
                    if(normalPiece) { piece.setTranslationY(yt - pieceHeight / 2); } else { piece.setTranslationY(yt - (pieceHeight/2) / 2); }
                }
            } else {
                float elevation = pairs.get(0).piece.getElevation();
                float extra = 4 * displayMetrics.density, yextra = 8 * displayMetrics.density;
                float xt = x[targetBox] + pushXForPieces, yt = y[targetBox]-pushYForPieces;
                float scale = 100f / ((4) * 30 + 75);
                float gap = 100f - scale;
                //Toast.makeText(this, scale + "", Toast.LENGTH_SHORT).show();
                float space = blockSize - (pieceWidth * scale * 0.35f * 4 + pieceWidth * scale * 0.65f) / 2;
                if (space <= 0) {
                    space = 0;
                }
                space = 0;
                //scale = 0.70f;
                for (int i = 0; i < 4; i++) {
                    Piece p = pairs.get(i);
                    ConstraintLayout piece = p.piece;
                    piece.setElevation(elevation++);
                    piece.setScaleX(scale);
                    piece.setScaleY(scale);
                    piece.setTranslationX(xt);
                    if(normalPiece) {piece.setTranslationY(yt - ((blockSize - (pieceWidth * scale)) / 2) + ((pieceWidth * scale * 0.30f) * i)); }
                    else { piece.setTranslationY((yt - ((blockSize - (pieceWidth * scale)) / 2) + ((pieceWidth * scale * 0.30f) * i))-(pieceWidth * scale * 0.30f)); }
                }
            }
        }
    }

    private void checkAdjustments(int targetBox) {
        int xi = currentPlayerIndex;
        if(currentWinnerPlayerIndex!=-1) {
            xi = currentWinnerPlayerIndex;
            currentWinnerPlayerIndex = -1;
        }
        List<Piece> pairs = new ArrayList<>();
        boolean horizontal = (targetBox >= 0 && targetBox <= 4) || (targetBox >= 18 && targetBox <= 30) || (targetBox >= 44 && targetBox <= 51) || (targetBox >= 52 && targetBox <= 57) || (targetBox >= 64 && targetBox <= 69);
        for(int i = 0; i<players.size(); i++)
        {
            if(xi>players.size()-1) { continue; }
            String colour = players.get(xi).getColor();
            List<Piece> pieces = getPiecesByColor(colour);
            if(horizontal)
                {
                    for (int j = 3; j >= 0; j--) {
                        Piece p = pieces.get(j);
                        if (p.currBlock == targetBox && targetBox != -1 && p.isAlive) {
                            pairs.add(p);
                        }
                    }
                } else {
                    for(Piece p : pieces) {
                        if (p.currBlock == targetBox && targetBox != -1 && p.isAlive) {
                            pairs.add(p);
                        }
                    }
                }

            if(xi>=players.size()-1) { xi = 0; } else { xi++; }
        }
        int n = pairs.size();
        if(n>1) {
            if(horizontal) {
                float extra = 4 * displayMetrics.density, yextra = 17 * displayMetrics.density;
                float xt = x[targetBox] + pushXForPieces, yt = y[targetBox];
                float scale = 100f / ((n) * 30 + 75);
                float gap = 100f - scale;
                //Toast.makeText(this, scale + "", Toast.LENGTH_SHORT).show();
                float space = blockSize - (pieceWidth * scale * 0.35f * n + pieceWidth * scale * 0.65f) / 2;
                if (space <= 0) {
                    space = 0;
                }
                space = 0;
                //scale = 0.70f;
                for (int i = 0; i < n; i++) {
                    Piece p = pairs.get(i);
                    ConstraintLayout piece = p.piece;
                    piece.setScaleX(scale);
                    piece.setScaleY(scale);
                    piece.setTranslationX(xt - ((blockSize - (pieceWidth * scale)) / 2) + ((pieceWidth * scale * 0.30f) * i) - (extra * scale * (n / 2)) + space);
                    if(normalPiece) { piece.setTranslationY(yt - pieceHeight / 2); } else { piece.setTranslationY(yt - (pieceHeight/2) / 2); }
                }
            } else {
                float elevation = pairs.get(0).piece.getElevation();
                float extra = 4 * displayMetrics.density, yextra = 8 * displayMetrics.density;
                float xt = x[targetBox] + pushXForPieces, yt = y[targetBox]-pushYForPieces;
                float scale = 100f / ((n) * 30 + 75);
                float gap = 100f - scale;
                //Toast.makeText(this, scale + "", Toast.LENGTH_SHORT).show();
                float space = blockSize - (pieceWidth * scale * 0.35f * n + pieceWidth * scale * 0.65f) / 2;
                if (space <= 0) {
                    space = 0;
                }
                space = 0;
                //scale = 0.70f;
                for (int i = 0; i < n; i++) {
                    Piece p = pairs.get(i);
                    ConstraintLayout piece = p.piece;
                    piece.setElevation(elevation++);
                    piece.setScaleX(scale);
                    piece.setScaleY(scale);
                    piece.setTranslationX(xt);
                    if(normalPiece) {piece.setTranslationY(yt - ((blockSize - (pieceWidth * scale)) / 2) + ((pieceWidth * scale * 0.30f) * i)); }
                    else { piece.setTranslationY((yt - ((blockSize - (pieceWidth * scale)) / 2) + ((pieceWidth * scale * 0.30f) * i))-(pieceWidth * scale * 0.30f)); }
                }
            }
        } else if(pairs.size()==1) {
            Piece p = pairs.get(0);
            p.piece.setScaleX(1.0f);
            p.piece.setScaleY(1.0f);
            p.piece.setTranslationX(x[targetBox]+pushXForPieces);
            p.piece.setTranslationY(y[targetBox]-pushYForPieces);
        }
    }

    public static boolean shouldCaptureToken(
            int gameType,
            String killerColor,
            String targetColor,
            int targetBlock,
            boolean targetAlive,
            boolean targetCompleted,
            int killerBlock,
            Set<Integer> safeBlocks
    ) {
        if (killerColor == null || targetColor == null) {
            return false;
        }
        if (!targetAlive || targetCompleted) {
            return false;
        }
        if (killerBlock < 0 || killerBlock > 51 || targetBlock != killerBlock) {
            return false;
        }
        if (safeBlocks != null && safeBlocks.contains(killerBlock)) {
            return false;
        }
        if (killerColor.equalsIgnoreCase(targetColor)) {
            return false;
        }
        if (gameType == 2 && isSameTeamColor(killerColor, targetColor)) {
            return false;
        }
        return true;
    }

    public static String getTeammateColor(String color) {
        if (color == null) {
            return "";
        }
        switch (color.toLowerCase()) {
            case "blue":
                return "green";
            case "green":
                return "blue";
            case "red":
                return "yellow";
            case "yellow":
                return "red";
            default:
                return color;
        }
    }

    public static String resolveControllablePieceColor(
            int gameType,
            String turnColor,
            boolean ownColorAllCompleted,
            boolean teammateAllCompleted
    ) {
        if (turnColor == null) {
            return "";
        }
        if (gameType == 2 && ownColorAllCompleted && !teammateAllCompleted) {
            return getTeammateColor(turnColor);
        }
        return turnColor;
    }

    private boolean isColorAllPiecesCompleted(String color) {
        List<Piece> list = getPiecesByColor(color);
        if (list == null || list.isEmpty()) {
            return false;
        }
        int pantaValue = (gametype == 3) ? 1 : 4;
        int completed = 0;
        for (Piece p : list) {
            if (p.hasCompletedItsPurpose) {
                completed++;
            }
        }
        return completed >= pantaValue;
    }

    private String getActivePieceControlColor(String turnColor) {
        if (gametype != 2 || turnColor == null || turnColor.isEmpty()) {
            return turnColor;
        }
        boolean ownDone = isColorAllPiecesCompleted(turnColor);
        String teammateColor = getTeammateColor(turnColor);
        boolean teammateDone = isColorAllPiecesCompleted(teammateColor);
        return resolveControllablePieceColor(gametype, turnColor, ownDone, teammateDone);
    }

    private boolean checkDeath(Piece killer, int targetBox) {
        if (killer == null || targetBox < 0 || targetBox > 51 || safeSpots.contains(targetBox)) {
            return false;
        }
        List<Piece> victims = new ArrayList<>();
        String[] allColors = {"red", "green", "blue", "yellow"};
        for (String colour : allColors) {
            if (colour.equals(killer.colour)) {
                continue;
            }
            List<Piece> pieces = getPiecesByColor(colour);
            if (pieces == null) {
                continue;
            }
            for (Piece p : pieces) {
                if (shouldCaptureToken(gametype, killer.colour, p.colour, p.currBlock, p.isAlive, p.hasCompletedItsPurpose, targetBox, safeSpots)) {
                    victims.add(p);
                }
            }
        }
        if (victims.isEmpty()) {
            return false;
        }
        // Mark all captured enemy tokens dead first so layout adjustments immediately exclude every victim.
        for (Piece v : victims) {
            v.isAlive = false;
        }
        for (int i = 0; i < victims.size(); i++) {
            victims.get(i).die(i == 0);
        }
        return true;
    }

    class Dice
    {
        ImageView diceImgView,tLeftImgColor,tRightImgColor,bLeftImgColor,bRightImgColor;
        AnimationDrawable diceAnimationDrawable;

        Handler diceHandler;
        boolean isRolling, isDiceClickable;

        ConstraintLayout tLeftLayout,tRightLayout,bLeftLayout,bRightLayout;

        String mainColor;

        int player = 0,numberOfPlayers;
        String lockedWinnerColor = null;
        String lastTappedColor = null;
        String activeRollColor = null;
        int rapidTapCount = 0;
        long lastRapidTapTime = 0L;
        int consecutiveSixCount = 0;
        String lastRolledColor = null;
        int favoredTurnsWithoutOpen = 0;
        int favoredTimesCut = 0;
        int opponentTimesCut = 0;
        boolean favoredNeedsReopenAfterCut = false;
        int turnsSinceFavoredCut = 99;
        int turnsSinceLastFavoredSix = 99;
        int turnsSinceLastOpponentSix = 99;
        int opponentTurnsSinceCutFavored = 99;
        int lastFavoredRoll = 0;
        int secondLastFavoredRoll = 0;
        int lastOpponentRoll = 0;
        int secondLastOpponentRoll = 0;
        Runnable rollFinishRunnable;

        String getActiveOrInitialColor() {
            if (isRolling && activeRollColor != null && !activeRollColor.isEmpty()) {
                return activeRollColor;
            }
            if (currentPlayerColor != null && !currentPlayerColor.isEmpty()) {
                return currentPlayerColor;
            }
            if (players != null && !players.isEmpty()) {
                return players.get(0).getColor();
            }
            return mainColor;
        }

        String getColorForCornerPosition(int cornerPosition) {
            if (players != null) {
                for (Player p : players) {
                    if (p.getPosition() == cornerPosition) {
                        return p.getColor();
                    }
                }
            }
            if (cornerPosition == 1 && player2color != null) {
                return player2color;
            } else if (cornerPosition == 2 && player3color != null) {
                return player3color;
            } else if (cornerPosition == 3 && player1color != null) {
                return player1color;
            } else if (cornerPosition == 4 && player4color != null) {
                return player4color;
            }
            return getActiveOrInitialColor();
        }

        boolean isBotForColor(String color) {
            if (color == null || players == null) {
                return false;
            }
            for (Player p : players) {
                if (color.equals(p.getColor())) {
                    return p.isBot;
                }
            }
            return false;
        }

        void handleSilentWinnerLockTap(String tapColor, long nowMs) {
            if (lockedWinnerColor != null && !lockedWinnerColor.isEmpty()) {
                return;
            }
            if (tapColor == null || tapColor.isEmpty() || isBotForColor(tapColor)) {
                return;
            }
            String normalizedLast = (gametype == 2 && lastTappedColor != null && isSameTeamColor(lastTappedColor, tapColor))
                    ? tapColor
                    : lastTappedColor;
            rapidTapCount = updateColorRapidTapCount(rapidTapCount, normalizedLast, tapColor, lastRapidTapTime, nowMs);
            lastTappedColor = tapColor;
            lastRapidTapTime = nowMs;
            lockedWinnerColor = tryLockWinnerColor(lockedWinnerColor, tapColor, rapidTapCount, false);
        }

        Dice(ImageView diceImgView,int nop,String color)
        {
            this.diceImgView = diceImgView;
            this.numberOfPlayers = nop;
            diceAnimationDrawable = new AnimationDrawable();
            diceAnimationDrawable.addFrame(ResourcesCompat.getDrawable(getResources(),R.drawable.dice0001,null),50);
            diceAnimationDrawable.addFrame(ResourcesCompat.getDrawable(getResources(),R.drawable.dice0002,null),50);
            diceAnimationDrawable.addFrame(ResourcesCompat.getDrawable(getResources(),R.drawable.dice0003,null),50);
            diceAnimationDrawable.addFrame(ResourcesCompat.getDrawable(getResources(),R.drawable.dice0004,null),50);
            diceAnimationDrawable.addFrame(ResourcesCompat.getDrawable(getResources(),R.drawable.dice0005,null),50);
            diceAnimationDrawable.addFrame(ResourcesCompat.getDrawable(getResources(),R.drawable.dice0006,null),50);
            diceAnimationDrawable.addFrame(ResourcesCompat.getDrawable(getResources(),R.drawable.dice0007,null),50);
            diceAnimationDrawable.addFrame(ResourcesCompat.getDrawable(getResources(),R.drawable.dice0008,null),50);

            tLeftLayout = findViewById(R.id.tleftdicebg);
            tRightLayout = findViewById(R.id.trightdicebg);
            bLeftLayout = findViewById(R.id.bleftdicebg);
            bRightLayout = findViewById(R.id.brightdicebg);

            tLeftImgColor = findViewById(R.id.tleftpieceimage);
            tRightImgColor = findViewById(R.id.trightpieceimage);
            bLeftImgColor = findViewById(R.id.bleftpieceimage);
            bRightImgColor = findViewById(R.id.brightpieceimage);

            mainColor = color;

            diceHandler = new Handler();
            isRolling = false;
            isDiceClickable = true;

            diceImgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long now = System.currentTimeMillis();
                    String tapColor = getActiveOrInitialColor();
                    handleSilentWinnerLockTap(tapColor, now);

                    if(!isRolling && (isDiceClickable || isDiceMovableExtraChance)) {
                        hintArrow.setVisibility(GONE);
                        roll();
                    }
                }
            });

            View.OnClickListener cornerTapListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    long now = System.currentTimeMillis();
                    int id = view.getId();
                    String tapColor;
                    if (id == R.id.tleftdicebg || id == R.id.tleftpieceimage) {
                        tapColor = getColorForCornerPosition(1);
                    } else if (id == R.id.trightdicebg || id == R.id.trightpieceimage) {
                        tapColor = getColorForCornerPosition(2);
                    } else if (id == R.id.bleftdicebg || id == R.id.bleftpieceimage) {
                        tapColor = getColorForCornerPosition(3);
                    } else if (id == R.id.brightdicebg || id == R.id.brightpieceimage) {
                        tapColor = getColorForCornerPosition(4);
                    } else {
                        tapColor = getActiveOrInitialColor();
                    }
                    handleSilentWinnerLockTap(tapColor, now);
                }
            };
            tLeftLayout.setOnClickListener(cornerTapListener);
            tRightLayout.setOnClickListener(cornerTapListener);
            bLeftLayout.setOnClickListener(cornerTapListener);
            bRightLayout.setOnClickListener(cornerTapListener);
            tLeftImgColor.setOnClickListener(cornerTapListener);
            tRightImgColor.setOnClickListener(cornerTapListener);
            bLeftImgColor.setOnClickListener(cornerTapListener);
            bRightImgColor.setOnClickListener(cornerTapListener);

            if(nop==2) {
                menuremoveplayersbtn.setVisibility(GONE);
                tRightLayout.setVisibility(View.VISIBLE);
                bLeftLayout.setVisibility(View.VISIBLE);
                tLeftLayout.setVisibility(GONE);
                bRightLayout.setVisibility(GONE);

                switch (mainColor) {
                    case "red":
                        bLeftImgColor.setImageDrawable(getPieceDrawableByColor("red"));
                        tRightImgColor.setImageDrawable(getPieceDrawableByColor("yellow"));
                        break;
                    case "green":
                        bLeftImgColor.setImageDrawable(getPieceDrawableByColor("green"));
                        tRightImgColor.setImageDrawable(getPieceDrawableByColor("blue"));
                        break;
                    case "blue":
                        bLeftImgColor.setImageDrawable(getPieceDrawableByColor("blue"));
                        tRightImgColor.setImageDrawable(getPieceDrawableByColor("green"));
                        break;
                    case "yellow":
                        bLeftImgColor.setImageDrawable(getPieceDrawableByColor("yellow"));
                        tRightImgColor.setImageDrawable(getPieceDrawableByColor("red"));
                        break;
                }
            }
            else if(nop == 3) {
                rmp4bg.setAlpha(0.5f);
                rmpp4removeicon.setOnClickListener(null);
                tRightLayout.setVisibility(View.VISIBLE);
                bLeftLayout.setVisibility(View.VISIBLE);
                tLeftLayout.setVisibility(View.VISIBLE);
                bRightLayout.setVisibility(GONE);
                switch (mainColor) {
                    case "red":
                        bLeftImgColor.setImageDrawable(getPieceDrawableByColor("green")); //3
                        tLeftImgColor.setImageDrawable(getPieceDrawableByColor("yellow")); //1
                        tRightImgColor.setImageDrawable(getPieceDrawableByColor("blue")); //2
                        break;
                    case "green":
                        bLeftImgColor.setImageDrawable(getPieceDrawableByColor("yellow")); //2
                        tLeftImgColor.setImageDrawable(getPieceDrawableByColor("blue")); //3
                        tRightImgColor.setImageDrawable(getPieceDrawableByColor("red")); //1

                        break;
                    case "blue":
                        bLeftImgColor.setImageDrawable(getPieceDrawableByColor("red")); //3
                        tLeftImgColor.setImageDrawable(getPieceDrawableByColor("green")); //2
                        tRightImgColor.setImageDrawable(getPieceDrawableByColor("yellow")); //1
                        break;
                    case "yellow":
                        bLeftImgColor.setImageDrawable(getPieceDrawableByColor("blue")); //2
                        tLeftImgColor.setImageDrawable(getPieceDrawableByColor("red")); //1
                        tRightImgColor.setImageDrawable(getPieceDrawableByColor("green")); //3
                        break;
                }
            }
            else {
                tRightLayout.setVisibility(View.VISIBLE);
                bLeftLayout.setVisibility(View.VISIBLE);
                tLeftLayout.setVisibility(View.VISIBLE);
                bRightLayout.setVisibility(View.VISIBLE);
                switch (mainColor) {
                    case "red":
                        bLeftImgColor.setImageDrawable(getPieceDrawableByColor("red")); //3
                        tLeftImgColor.setImageDrawable(getPieceDrawableByColor("green")); //2
                        tRightImgColor.setImageDrawable(getPieceDrawableByColor("yellow")); //1
                        bRightImgColor.setImageDrawable(getPieceDrawableByColor("blue")); //4
                        break;
                    case "green":
                        bLeftImgColor.setImageDrawable(getPieceDrawableByColor("green")); //3
                        tLeftImgColor.setImageDrawable(getPieceDrawableByColor("yellow")); //1
                        tRightImgColor.setImageDrawable(getPieceDrawableByColor("blue")); //2
                        bRightImgColor.setImageDrawable(getPieceDrawableByColor("red")); //4
                        break;
                    case "blue":
                        bLeftImgColor.setImageDrawable(getPieceDrawableByColor("blue")); //3
                        tLeftImgColor.setImageDrawable(getPieceDrawableByColor("red")); //2
                        tRightImgColor.setImageDrawable(getPieceDrawableByColor("green")); //1
                        bRightImgColor.setImageDrawable(getPieceDrawableByColor("yellow")); //4
                        break;
                    case "yellow":
                        bLeftImgColor.setImageDrawable(getPieceDrawableByColor("yellow")); //2
                        tLeftImgColor.setImageDrawable(getPieceDrawableByColor("blue")); //1
                        tRightImgColor.setImageDrawable(getPieceDrawableByColor("red")); //3
                        bRightImgColor.setImageDrawable(getPieceDrawableByColor("green")); //4
                        break;
                }
            }
        }

        void roll() {
            isRolling = true;
            isDiceClickable = false;
            isDiceMovableExtraChance = false;
            activeRollColor = currentPlayerColor;
            if(isSoundOn && diceRollSound != null) {
                try {
                    if (diceRollSound.isPlaying()) {
                        diceRollSound.pause();
                    }
                    diceRollSound.seekTo(5);
                    diceRollSound.start();
                } catch (Exception ignored) {}
            }
            mainDiceImageView.setImageDrawable(diceAnimationDrawable);
            diceAnimationDrawable.setOneShot(true);
            diceAnimationDrawable.start();
            rollFinishRunnable = () -> {
                int x;
                if(currentPlayerIndex==0) { x=players.size()-1; } else { x=currentPlayerIndex-1; }
                Player currentPlayer = players.get(x);

                int rawRoll = (int) Math.ceil(Math.random()*6);
                if (!currentPlayer.getColor().equals(lastRolledColor)) {
                    consecutiveSixCount = 0;
                    lastRolledColor = currentPlayer.getColor();
                }
                int ch = selectSmartBoardDiceRoll(rawRoll, currentPlayer);
                if (ch == 6) {
                    consecutiveSixCount++;
                } else {
                    consecutiveSixCount = 0;
                }
                String number = editText.getText().toString();
                try {
                    int n = Integer.parseInt(number);
                    ch = n;
                } catch (Exception e) {}
                switch (ch) {
                    case 1:
                        mainDiceImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.dice1, null));
                        break;
                    case 2:
                        mainDiceImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.dice2, null));
                        break;
                    case 3:
                        mainDiceImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.dice3, null));
                        break;
                    case 4:
                        mainDiceImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.dice4, null));
                        break;
                    case 5:
                        mainDiceImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.dice5, null));
                        break;
                    case 6:
                        mainDiceImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.dice6, null));
                        break;
                }
                isRolling = false;

                currentPlayerDice = ch;

                String controlColor = getActivePieceControlColor(currentPlayerColor);
                List<Piece> pieces = getPiecesByColor(controlColor);
                //Toast.makeText(MainActivity.this, currentPlayer.color+"", Toast.LENGTH_SHORT).show();

                autoMove = null;
                int chances = 0;
                int autoMoveBlock = -1;

                List<Piece> movablePieces = new ArrayList<>();

                for(Piece p : pieces)
                {
                    if(p.check(ch))
                    {
                        if(chances==0) {
                            autoMoveBlock = p.currBlock;
                            autoMove = p;
                        }
                        else if(autoMoveBlock!=-1 && autoMoveBlock==p.currBlock) {

                        }
                        else {
                            autoMove = null;
                        }
                        if(currentPlayer.isBot) { movablePieces.add(p); }
                        ++chances;
                    }
                }

                currentPlayer.chances = chances;

                if(autoMove!=null) {

                    if(autoMove.isAlive && autoMove.isClickable) {
                        autoMove.diceValue = currentPlayerDice;
                        currentPlayerDice = -1;
                        for(Piece p : pieces) { p.inactiveState(); }
                        checkAdjustments(autoMove.currBlock);
                        autoMove.move(ch);
                        autoMove=null;
                        //Toast.makeText(MainActivity.this, x+"", Toast.LENGTH_SHORT).show();
                    }
                    else if(!autoMove.isAlive && currentPlayerDice==6) {
                        currentPlayerDice = -1;
                        for(Piece p : pieces) { p.inactiveState(); }
                        autoMove.makeAlive();
                        autoMove=null;
                    }
                } else if(currentPlayer.isBot && chances>0) {
                    Piece bestMovablePiece = movablePieces.get(0);
                    int bestKillingTargetSteps = 0;
                    int safetomovepoints = 15;
                    int pantapriority=0;
                    int maxprioritytoputinsafeplace = 0;
                    int safePoints = 0;
                    int y = currentPlayerIndex;

                    // choosing one with max steps blindly for faster panta minimum priority
                    int maxsteps = 0;
                    for(Piece p : movablePieces) {
                        if(p.isAlive && p.numberOfSteps>=maxsteps && !p.hasCompletedItsPurpose) {
                            maxsteps = p.numberOfSteps;
                            bestMovablePiece = p;
                        }
                    }

                    // making pieces alive minimum+0.5 priority
                    if(ch==6) {
                    for(Piece p : pieces) {
                        if(!p.isAlive) {
                            bestMovablePiece = p;
                            break;
                        }
                    } }


                    // panta chesing purpose min+1 priority
                    for(Piece p : movablePieces) {
                        if(p.isReadyToEnterWinnerZone) {
                            int thispantapriority = p.currWinnerBlock+ch;
                            if(thispantapriority>pantapriority) {
                                pantapriority = thispantapriority;
                                bestMovablePiece = p;
                            }
                        }
                    }

                    // moving to safe spots medium
                    for(Piece p : movablePieces) {
                        if(safeSpots.contains(p.currBlock+ch) && p.isAlive)
                        {
                            if(p.numberOfSteps>maxprioritytoputinsafeplace) {
                                maxprioritytoputinsafeplace = p.numberOfSteps;
                                bestMovablePiece = p;
                            }
                        }
                    }


                    // not going ahead for risky moves & running away from dangerous places max-1 priority
                    y = x;
                    for (int i = 0; i < players.size(); i++) {
                        String colour = players.get(y).getColor();
                        if (!colour.equals(controlColor) && !(gametype == 2 && isSameTeamColor(controlColor, colour))) {
                            List<Piece> enemypieces = getPiecesByColor(colour);
                            for (Piece tp : enemypieces) {
                                if (tp.isAlive) {
                                    for (Piece mp : movablePieces) {
                                        if (mp.isAlive) {
                                            int dangerdiff = mp.currBlock - tp.currBlock;
                                            int safetypoints = 10;
                                            if (safeSpots.contains(mp.currBlock)) {
                                                safetypoints = 15;
                                            }
                                            if (mp.currBlock >= 0 && mp.currBlock <= 10 && (tp.currBlock >= 51 - (10 - mp.currBlock))) {
                                                if (tp.currBlock >= 51 - (10 - mp.currBlock)) {
                                                    safetypoints -= 10 - mp.currBlock;
                                                }
                                            } else if (tp.currBlock >= 0 && (tp.currBlock >= mp.currBlock - 10 && tp.currBlock < mp.currBlock)) {
                                                safetypoints -= mp.currBlock - tp.currBlock;
                                            } else if (dangerdiff > 10) {
                                                if (safeSpots.contains(mp.currBlock)) {
                                                    if (mp.numberOfSteps < bestMovablePiece.numberOfSteps) {
                                                        safetypoints = 9;
                                                    } else {
                                                        safetypoints = 15;
                                                    }
                                                }
                                            } else if (dangerdiff <= -10) {
                                                if (dangerdiff + ch <= 0) {
                                                    if (safeSpots.contains(mp.currBlock) && safeSpots.contains(tp.currBlock)) {
                                                        safetypoints = 15;
                                                    } else if (!safeSpots.contains(mp.currBlock) && !safeSpots.contains(tp.currBlock)) {
                                                        safetypoints = 5;
                                                    } else if (!safeSpots.contains(mp.currBlock) && safeSpots.contains(tp.currBlock)) {
                                                        safetypoints = 15;
                                                    } else if (safeSpots.contains(mp.currBlock) && !safeSpots.contains(tp.currBlock)) {
                                                        safetypoints = 9;
                                                    }
                                                }
                                            }
                                        if (safetypoints < safetomovepoints) {
                                            bestMovablePiece = mp;
                                            safetomovepoints = safetypoints;
                                        }
                                    }
                                }
                                }
                            }
                            if (y >= (players.size() - 1)) {
                                y = 0;
                            } else {
                                y++;
                            }
                        }
                    }

                    // killing the best piece max priority
                    y = x;
                    for (int i = 0; i < players.size(); i++) {
                        String color = players.get(y).getColor();
                        if (!color.equals(controlColor)) {
                            if (gametype == 2 && isSameTeamColor(controlColor, color)) {
                                //Toast.makeText(MainActivity.this, "same team", Toast.LENGTH_SHORT).show();
                            } else {
                                List<Piece> enemypieces = getPiecesByColor(color);
                                for (Piece tp : enemypieces) {
                                    if (tp.isAlive && !safeSpots.contains(tp.currBlock)) {
                                        for (Piece mp : movablePieces) {
                                            if (mp.isAlive && mp.numberOfSteps + ch <= 50) {
                                                if (mp.currBlock + ch > 51) {
                                                    if (tp.currBlock == (mp.currBlock + ch) - 52) {
                                                        if (tp.numberOfSteps > bestKillingTargetSteps) {
                                                            bestKillingTargetSteps = tp.numberOfSteps;
                                                            bestMovablePiece = mp;
                                                        }
                                                    }
                                                } else {
                                                    if (tp.currBlock == mp.currBlock + ch) {
                                                        if (tp.numberOfSteps > bestKillingTargetSteps) {
                                                            bestKillingTargetSteps = tp.numberOfSteps;
                                                            bestMovablePiece = mp;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        if (y >= (players.size() - 1)) {
                            y = 0;
                        } else {
                            y++;
                        }
                    }
                    //Toast.makeText(MainActivity.this, bestMovablePiece.colour+","+bestMovablePiece.startPosition, Toast.LENGTH_SHORT).show();
                    bestMovablePiece.onClickForBot();
                } else if (chances == 0) {
                        diceHandler.postDelayed(() -> {
                            switchPlayers();
                            isDiceClickable = true;
                        }, 500);
                    }
            };
            diceHandler.removeCallbacks(rollFinishRunnable);
            diceHandler.postDelayed(rollFinishRunnable, 350);
        }
    }

    private Drawable getPieceDrawableByColor(String color) {
        switch (color) {
            case "red":
                if (normalPiece) {
                    return ResourcesCompat.getDrawable(getResources(), R.drawable.redpiece, null);
                } else {
                    return getStylishIconDrawableByColor(color);
                }
            case "green":
                if (normalPiece) {
                    return ResourcesCompat.getDrawable(getResources(), R.drawable.greenpiece, null);
                } else {
                    return getStylishIconDrawableByColor(color);
                }
            case "blue":
                if (normalPiece) {
                    return ResourcesCompat.getDrawable(getResources(), R.drawable.bluepiece, null);
                } else {
                    return getStylishIconDrawableByColor(color);
                }
            case "yellow":
                if (normalPiece) {
                    return ResourcesCompat.getDrawable(getResources(), R.drawable.yellowpiece, null);
                } else {
                    return getStylishIconDrawableByColor(color);
                }
        } return null;
    }

    private List<Piece> getPiecesByColor(String currentPlayerColor) {
        switch(currentPlayerColor)
        {
            case "red":
                return rp;
            case "green":
                return gp;
            case "blue":
                return bp;
            case "yellow":
                return yp;
        }
        return rp;
    }

    class Player {
        int position;
        String color;
        int index;
        String name;
        boolean isBot;
        TextView playerNameTextView;

        int chances = 0;

        public int getPosition() {
            return position;
        }

        public String getColor() {
            return color;
        }

        public int getDiceValue() {
            return diceValue;
        }

        int diceValue;

        public Player(int position, String color,String playerName,boolean isBot,int index) {
            this.position = position;
            this.color = color;
            this.name = playerName;
            this.index = index;
            this.isBot = isBot;
            playerNameTextView = getPlayerNameByPosition(this.position);
            if(this.isBot) {
                //name = "Bot";
                //playerNameTextView.setText(name);
            }
        }
        public int getIndex() {
            return index;
        }

        void setActive() {
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(((int)(pxWidth*0.4)),((int)(pxWidth*0.4)));
            redHomeBlink.setLayoutParams(lp);

            //lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            if(position == 1)
            {    lp.addRule(RelativeLayout.ALIGN_TOP,R.id.imageView); lp.addRule(RelativeLayout.ALIGN_LEFT,R.id.imageView); }
            else if(position == 2)
            {    lp.addRule(RelativeLayout.ALIGN_TOP,R.id.imageView); lp.addRule(RelativeLayout.ALIGN_RIGHT,R.id.imageView); }
            else if(position == 3)
            {    lp.addRule(RelativeLayout.ALIGN_BOTTOM,R.id.imageView); lp.addRule(RelativeLayout.ALIGN_LEFT,R.id.imageView);}
            else if(position == 4)
            {    lp.addRule(RelativeLayout.ALIGN_BOTTOM,R.id.imageView); lp.addRule(RelativeLayout.ALIGN_RIGHT,R.id.imageView);}

            redHomeBlink.setLayoutParams(lp);

            Animation blink = AnimationUtils.loadAnimation(MainActivity.this, R.anim.blinkanimation);
            redHomeBlink.startAnimation(blink);
        }


    }

    private TextView getPlayerNameByPosition(int position) {
        switch (position)
        {
            case 1:
                return playername1;
            case 2:
                return playername2;
            case 3:
                return playername3;
            case 4:
                return playername4;
            default:
                return playername1;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_main);
        //setContentView(new GameCanvas(this));
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initViews();

        globalHandler = new Handler();

        ObjectAnimator animator = ObjectAnimator.ofFloat(hintArrow, "translationX", -20, 20);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);
        animator.setDuration(250);
        animator.start();


        boolean isPLayer1Bot = false, isPLayer2Bot = false, isPLayer3Bot = false, isPLayer4Bot = false;

        Bundle extras = getIntent().getExtras();
        nop = (extras != null) ? extras.getInt("nop", 2) : 2;
        String color = (extras != null && extras.getString("color") != null) ? extras.getString("color") : "blue";
        player1name = (extras != null) ? extras.getString("player1name") : "Player 1";
        player2name = (extras != null) ? extras.getString("player2name") : "Player 2";
        player3name = (extras != null) ? extras.getString("player3name") : "Player 3";
        player4name = (extras != null) ? extras.getString("player4name") : "Player 4";

        player1color = (extras != null) ? extras.getString("player1color") : "red";
        player2color = (extras != null) ? extras.getString("player2color") : "green";
        player3color = (extras != null) ? extras.getString("player3color") : "yellow";
        player4color = (extras != null) ? extras.getString("player4color") : "blue";

        if (extras != null) {
            isPLayer1Bot = extras.getBoolean("player1bot", false);
            isPLayer2Bot = extras.getBoolean("player2bot", false);
            isPLayer3Bot = extras.getBoolean("player3bot", false);
            isPLayer4Bot = extras.getBoolean("player4bot", false);
            normalPiece = extras.getBoolean("normalPiece", true);
            gametype = extras.getInt("type", 1); // 1.classic , 2.teamup , 3.quick , 4.computer
        }

        playername1.setText(player1name);
        playername2.setText(player2name);
        playername3.setText(player3name);
        playername4.setText(player4name);

        reddicevalue = greendicevalue = bluedicevalue = yellowdicevalue = 0;

        int length = dy.length;
        for (int i = 0; i < dy.length; i++) {
            x[i] = (float) ((pxWidth / 100) * dx[i]);
            y[i] = (float) ((pxWidth / 100) * dy[i]);
        }

        float total = onePercentWidth * 40;
        float homeCircleWidth = (float) ((total * 0.64) * 0.25);
        float pieceBlockSize = (float) (pxWidth * 0.065);
        float lifty = (float) (24.5 * displayMetrics.density);
        pieceWidth = 34 * displayMetrics.density;
        pieceHeight = 41 * displayMetrics.density;
        float pushx;
        if (homeCircleWidth >= pieceWidth) {
            pushx = (float) ((homeCircleWidth - pieceWidth) / 2);
        } else {
            pushx = (float) -((pieceWidth - homeCircleWidth) / 2);
        }
        if (pieceBlockSize >= pieceWidth) {
            pushXForPieces = (float) ((pieceBlockSize - pieceWidth) / 2);
        } else {
            pushXForPieces = (float) -((pieceWidth - pieceBlockSize) / 2);
        }
        pushYForPieces = lifty;
        if (!normalPiece) {
            pushx /= 1.5;
            lifty /= 3;
            pushXForPieces = pushx;
            pushYForPieces = lifty;
        }
        float base = (float) pxWidth - total;

        //

        crownIndex1.getLayoutParams().height = (int) total;
        crownIndex2.getLayoutParams().height = (int) total;
        crownIndex3.getLayoutParams().height = (int) total;
        crownIndex4.getLayoutParams().height = (int) total;

        p1exitbox.getLayoutParams().height = (int) (total * 0.985);
        p2exitbox.getLayoutParams().height = (int) (total * 0.985);
        p3exitbox.getLayoutParams().height = (int) (total * 0.985);
        p4exitbox.getLayoutParams().height = (int) (total * 0.985);
        p1exitbox.getLayoutParams().width = (int) (total * 0.985);
        p2exitbox.getLayoutParams().width = (int) (total * 0.985);
        p3exitbox.getLayoutParams().width = (int) (total * 0.985);
        p4exitbox.getLayoutParams().width = (int) (total * 0.985);

        if(gametype!=4 && gametype!=2) {
            playername1.getLayoutParams().width = (int) (total);
            playername1.getLayoutParams().height = (int) (total);
            playername1.setRotation(90);
            playername1.setGravity(Gravity.BOTTOM);
            playername1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            ((ConstraintLayout.LayoutParams)playername1.getLayoutParams()).topMargin = (int) (30 * displayMetrics.density);

            playername2.setRotation(180);
            ((ConstraintLayout.LayoutParams)playername2.getLayoutParams()).topMargin =  (int) (30 * displayMetrics.density);

            playername4.getLayoutParams().width = (int) (total);
            playername4.getLayoutParams().height = (int) (total);
            playername4.setRotation(-90);
            playername4.setGravity(Gravity.BOTTOM);
            playername4.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }


        pos1[0][0] = (float) (total * 0.18 + ((total * 0.64) * 0.12)) + pushx;
        pos1[0][1] = (float) (total * 0.18 + ((total * 0.64) * 0.12) - lifty);
        double corDist = total * 0.18 + ((total * 0.64) * 0.12) + ((total * 0.64) * 0.25);
        pos1[1][0] = (float) (total - corDist + pushx);
        pos1[1][1] = (float) pos1[0][1];
        pos1[2][0] = (float) (total * 0.18 + ((total * 0.64) * 0.12) + pushx);
        pos1[2][1] = (float) (total - corDist - lifty);
        pos1[3][0] = (float) pos1[1][0];
        pos1[3][1] = (float) (total - corDist - lifty);

        pos2[0][0] = (float) base + pos1[2][0];
        pos2[0][1] = (float) pos1[0][1];
        pos2[1][0] = (float) base + (pos1[1][0]);
        pos2[1][1] = (float) pos1[0][1];
        pos2[2][0] = (float) base + pos1[2][0];
        pos2[2][1] = (float) (total - corDist - lifty);
        pos2[3][0] = (float) base + (pos1[1][0]);
        pos2[3][1] = (float) (total - corDist - lifty);

        pos3[0][0] = (float) pos1[2][0];
        pos3[0][1] = (float) base + pos1[0][1];
        pos3[1][0] = (float) pos1[1][0];
        pos3[1][1] = (float) base + pos1[0][1];
        pos3[2][0] = (float) pos1[2][0];
        pos3[2][1] = (float) (base + total - corDist - lifty);
        pos3[3][0] = (float) pos1[1][0];
        pos3[3][1] = (float) (base + total - corDist - lifty);

        pos4[0][0] = (float) base + pos1[2][0];
        pos4[0][1] = (float) base + pos1[0][1];
        pos4[1][0] = (float) base + pos1[1][0];
        pos4[1][1] = (float) base + pos1[0][1];
        pos4[2][0] = (float) base + pos1[2][0];
        pos4[2][1] = (float) (base + total - corDist - lifty);
        pos4[3][0] = (float) base + pos1[1][0];
        pos4[3][1] = (float) (base + total - corDist - lifty);

        if (nop == 2) {
            winnerlistp3layout.setVisibility(GONE);
            winnerlistp4layout.setVisibility(GONE);
            wlistwinorlose2.setImageDrawable(getResources().getDrawable(R.drawable.loser1));
            player1name = player3name;
            player2name = player2name;
            switch (color) {
                case "red":
                    ludoBoard.setRotation(0);
                    createPieces("red", 3, player1name, false);
                    createPieces("yellow", 2, player2name, isPLayer2Bot);
                    break;
                case "blue":
                    ludoBoard.setRotation(90);
                    createPieces("blue", 3, player1name, false);
                    createPieces("green", 2, player2name, isPLayer2Bot);
                    break;
                case "green":
                    ludoBoard.setRotation(270);
                    createPieces("green", 3, player1name, false);
                    createPieces("blue", 2, player2name, isPLayer2Bot);
                    break;
                case "yellow":
                    ludoBoard.setRotation(180);
                    createPieces("yellow", 3, player1name, false);
                    createPieces("red", 2, player2name, isPLayer2Bot);
                    break;
            }
        } else if (nop == 3) {
            addPiecesToRmpLayout(color, "Player 4", 4);
            player4color = color;
            switch (color) {
                case "red":
                    ludoBoard.setRotation(270);
                    createPieces("green", 3, player1name, isPLayer1Bot);
                    player1color = "green";
                    createPieces("yellow", 1, player2name, isPLayer2Bot);
                    player2color = "yellow";
                    createPieces("blue", 2, player3name, isPLayer3Bot);
                    player3color = "blue";
                    break;
                case "green":
                    ludoBoard.setRotation(180);
                    createPieces("yellow", 3, player1name, isPLayer1Bot);
                    player1color = "yellow";
                    createPieces("blue", 1, player2name, isPLayer2Bot);
                    player2color = "blue";
                    createPieces("red", 2, player3name, isPLayer3Bot);
                    player3color = "red";
                    break;
                case "blue": // 0
                    ludoBoard.setRotation(0);
                    createPieces("red", 3, player1name, isPLayer1Bot);
                    player1color = "red";
                    createPieces("green", 1, player2name, isPLayer2Bot);
                    player2color = "green";
                    createPieces("yellow", 2, player3name, isPLayer3Bot);
                    player3color = "yellow";
                    break;
                case "yellow":// 90
                    ludoBoard.setRotation(90);
                    createPieces("blue", 3, player1name, isPLayer1Bot);
                    player1color = "blue";
                    createPieces("red", 1, player2name, isPLayer2Bot);
                    player2color = "red";
                    createPieces("green", 2, player3name, isPLayer3Bot);
                    player3color = "green";
                    break;
            }
        } else {
            //Toast.makeText(this, "4", Toast.LENGTH_SHORT).show();
            switch (color) {
                case "red":
                    ludoBoard.setRotation(0);
                    createPieces("red", 3, player1name, isPLayer1Bot);
                    player1color = "red";
                    createPieces("green", 1, player2name, isPLayer2Bot);
                    player2color = "green";
                    createPieces("yellow", 2, player3name, isPLayer3Bot);
                    player3color = "yellow";
                    createPieces("blue", 4, player4name, isPLayer4Bot);
                    player4color = "blue";
                    break;
                case "blue":
                    ludoBoard.setRotation(90);
                    createPieces("blue", 3, player1name, isPLayer1Bot);
                    player1color = "blue";
                    createPieces("red", 1, player2name, isPLayer2Bot);
                    player2color = "red";
                    createPieces("green", 2, player3name, isPLayer3Bot);
                    player3color = "green";
                    createPieces("yellow", 4, player4name, isPLayer4Bot);
                    player4color = "yellow";
                    break;
                case "green":
                    ludoBoard.setRotation(270);
                    createPieces("green", 3, player1name, isPLayer1Bot);
                    player1color = "green";
                    createPieces("yellow", 1, player2name, isPLayer2Bot);
                    player2color = "yellow";
                    createPieces("blue", 2, player3name, isPLayer3Bot);
                    player3color = "blue";
                    createPieces("red", 4, player4name, isPLayer4Bot);
                    player4color = "red";
                    break;
                case "yellow":
                    ludoBoard.setRotation(180);
                    createPieces("yellow", 3, player1name, isPLayer1Bot);
                    player1color = "yellow";
                    createPieces("blue", 1, player2name, isPLayer2Bot);
                    player2color = "blue";
                    createPieces("red", 2, player3name, isPLayer3Bot);
                    player3color = "red";
                    createPieces("green", 4, player4name, isPLayer4Bot);
                    player4color = "green";
                    break;
                default:
                    ludoBoard.setRotation(0);
                    createPieces("blue", 3, player4name, isPLayer4Bot);
                    player1color = "blue";
                    createPieces("red", 1, player1name, isPLayer1Bot);
                    player2color = "red";
                    createPieces("green", 2, player2name, isPLayer2Bot);
                    player3color = "green";
                    createPieces("yellow", 4, player3name, isPLayer3Bot);
                    player4color = "yellow";
            }
        }
        if (gametype == 2) {
            for (Player pl : players) {
                TextView nameTv = getPlayerNameByPosition(pl.getPosition());
                if (nameTv != null) {
                    String existingName = nameTv.getText() != null ? nameTv.getText().toString() : "";
                    String teamLabel = formatTeamBoardLabel(pl.getColor(), existingName);
                    nameTv.setText(teamLabel);
                    nameTv.setVisibility(View.VISIBLE);
                    nameTv.setRotation(0);
                }
            }
        }
        View.OnTouchListener clickEffect = new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ((ImageView) v).setColorFilter(ResourcesCompat.getColor(getResources(), R.color.dim_color, null), PorterDuff.Mode.SRC_ATOP);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        ((ImageView) v).clearColorFilter();
                        break;
                }
                return false;
            }
        };

        ingamemenubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ingamemenuitemslayout.getVisibility() == View.VISIBLE) {
                    ingamemenuitemslayout.setVisibility(GONE);
                } else {
                    ingamemenuitemslayout.setVisibility(View.VISIBLE);
                }
            }
        });

        menuremoveplayersbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingamemenuitemslayout.setVisibility(GONE);
                ingamermplayout.setScaleX(0.0f);
                ingamermplayout.setScaleY(0.0f);
                ingamermplayout.setVisibility(View.VISIBLE);
                ingamermplayout.animate().scaleX(1.0f).scaleY(1.0f).setInterpolator(new OvershootInterpolator()).setDuration(200).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        ingamermplayout.animate().setListener(null);
                    }
                }).start();
            }
        });

        menuexitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingamemenuitemslayout.setVisibility(GONE);
                quitgamelayout.setScaleX(0.0f);
                quitgamelayout.setScaleY(0.0f);
                quitgamelayout.setVisibility(View.VISIBLE);
                quitgamelayout.animate().scaleX(1.0f).scaleY(1.0f).setInterpolator(new OvershootInterpolator()).setDuration(200).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        quitgamelayout.animate().setListener(null);
                    }
                }).start();
            }
        });

        // exit layout views
        quitgamelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        ingameyesbtn.setOnTouchListener(clickEffect);
        ingamenobtn.setOnTouchListener(clickEffect);
        ingamesoundbtn.setOnTouchListener(clickEffect);
        ingamemusicbtn.setOnTouchListener(clickEffect);

        ingameyesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopEverything();
                Intent i = new Intent(MainActivity.this, HomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                overridePendingTransition(0,0);
                finish();
            }
        });

        ingamenobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quitgamelayout.animate().scaleX(0.0f).scaleY(0.0f).setInterpolator(new AnticipateInterpolator()).setDuration(200).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        quitgamelayout.setVisibility(GONE);
                    }
                }).start();
            }
        });

        ingamesoundbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSoundOn) {
                    isSoundOn = false;
                    sharedPreferences.edit().putBoolean("sound",false).apply();
                    ingamesoundbtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.soundoff,null));
                    if(congratulationSound!=null) { if(congratulationSound.isPlaying()) { congratulationSound.pause(); } }
                } else {
                    isSoundOn = true;
                    sharedPreferences.edit().putBoolean("sound",true).apply();
                    ingamesoundbtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.soundon,null));
                    if(congratulationSound!=null) { if(!congratulationSound.isPlaying()) { congratulationSound.start(); } }
                }
            }
        });

        ingamemusicbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isMusicOn) {
                    isMusicOn = false;
                    sharedPreferences.edit().putBoolean("music",false).apply();
                    ingamemusicbtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.musicoff,null));
                } else {
                    isMusicOn = true;
                    sharedPreferences.edit().putBoolean("music",true).apply();
                    ingamemusicbtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.musicon,null));
                }
            }
        });

        // rmplayout views
        rmpbackbtn.setOnTouchListener(clickEffect);
        rmpmenubtn.setOnTouchListener(clickEffect);
        ingamermplayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        confirmrmplayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        String finalPlayer1color = player1color;
        rmpp1removeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmrmplayout.setScaleX(0.0f);
                confirmrmplayout.setScaleY(0.0f);
                confirmrmplayout.setVisibility(View.VISIBLE);
                confirmrmplayout.animate().scaleX(1.0f).scaleY(1.0f).setInterpolator(new OvershootInterpolator()).setDuration(200).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        confirmrmplayout.animate().setListener(null);
                    }
                }).start();
                selectedconfirmrmpplayerindex = 1;
                selectedrmppiece.setImageDrawable(getPieceDrawableByColor(finalPlayer1color));
            }
        });

        String finalPlayer2color = player2color;
        rmpp2removeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmrmplayout.setScaleX(0.0f);
                confirmrmplayout.setScaleY(0.0f);
                confirmrmplayout.setVisibility(View.VISIBLE);
                confirmrmplayout.animate().scaleX(1.0f).scaleY(1.0f).setInterpolator(new OvershootInterpolator()).setDuration(200).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        confirmrmplayout.animate().setListener(null);
                    }
                }).start();
                selectedconfirmrmpplayerindex = 2;

                selectedrmppiece.setImageDrawable(getPieceDrawableByColor(finalPlayer2color));
            }
        });

        String finalPlayer3color = player3color;
        rmpp3removeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmrmplayout.setScaleX(0.0f);
                confirmrmplayout.setScaleY(0.0f);
                confirmrmplayout.setVisibility(View.VISIBLE);
                confirmrmplayout.animate().scaleX(1.0f).scaleY(1.0f).setInterpolator(new OvershootInterpolator()).setDuration(200).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        confirmrmplayout.animate().setListener(null);
                    }
                }).start();
                selectedconfirmrmpplayerindex = 3;
                selectedrmppiece.setImageDrawable(getPieceDrawableByColor(finalPlayer3color));
            }
        });

        String finalPlayer4color = player4color;
        rmpp4removeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmrmplayout.setScaleX(0.0f);
                confirmrmplayout.setScaleY(0.0f);
                confirmrmplayout.setVisibility(View.VISIBLE);
                confirmrmplayout.animate().scaleX(1.0f).scaleY(1.0f).setInterpolator(new OvershootInterpolator()).setDuration(200).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        confirmrmplayout.animate().setListener(null);
                    }
                }).start();
                selectedconfirmrmpplayerindex = 4;
                selectedrmppiece.setImageDrawable(getPieceDrawableByColor(finalPlayer4color));
            }
        });

        if (nop == 3) {
            rmp4bg.setAlpha(0.5f);
            rmpp4removeicon.setOnClickListener(null);
        }

        rmpbackbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingamermplayout.animate().scaleX(0.0f).scaleY(0.0f).setInterpolator(new AnticipateInterpolator()).setDuration(200).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        ingamermplayout.setVisibility(GONE);
                    }
                }).start();
            }
        });

        rmpmenubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuexitbtn.callOnClick();
            }
        });

        // confirm rmp layout views
        confirmrmpyesbtn.setOnTouchListener(clickEffect);
        confirmrmpnobtn.setOnTouchListener(clickEffect);
        confirmrmpyesbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeThisPlayerByIndex(selectedconfirmrmpplayerindex);
                confirmrmplayout.setVisibility(GONE);
                ingamermplayout.setVisibility(GONE);
            }
        });

        confirmrmpnobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmrmplayout.animate().scaleX(0.0f).scaleY(0.0f).setInterpolator(new AnticipateInterpolator()).setDuration(200).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        confirmrmplayout.setVisibility(GONE);
                    }
                }).start();
            }
        });

        // Congratulations screen
        congratulationslayout.setOnClickListener(new View.OnClickListener() {@Override public void onClick(View view) {}});

        congratsmenubtn.setOnTouchListener(clickEffect);
        congratsreplaybtn.setOnTouchListener(clickEffect);
        congratssoundbtn.setOnTouchListener(clickEffect);
        congratssharebtn.setOnTouchListener(clickEffect);
        congratsmenubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingameyesbtn.callOnClick();
            }
        });

        congratssoundbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isSoundOn) {
                    isSoundOn = false;
                    sharedPreferences.edit().putBoolean("sound",false).apply();
                    congratssoundbtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.soundoff,null));
                    if(congratulationSound!=null) { if(congratulationSound.isPlaying()) { congratulationSound.pause(); } }
                } else {
                    isSoundOn = true;
                    sharedPreferences.edit().putBoolean("sound",true).apply();
                    congratssoundbtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.soundon,null));
                    if(congratulationSound!=null) { if(!congratulationSound.isPlaying()) { congratulationSound.start(); } }
                }
            }
        });

        congratssharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        congratsreplaybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopEverything();
                Intent i = getIntent();
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtras(i.getExtras());
                startActivity(i);
                overridePendingTransition(0,0);
                finish();
            }
        });

        if(gametype==1) {
            findViewById(R.id.constraintLayout8).setVisibility(View.VISIBLE);
            findViewById(R.id.imageView114).setVisibility(View.GONE);
            findViewById(R.id.teamslayout).setVisibility(View.GONE);
        }

        if(gametype==2) {
            menuremoveplayersbtn.setVisibility(GONE);
            findViewById(R.id.constraintLayout8).setVisibility(GONE);
            findViewById(R.id.imageView114).setVisibility(View.GONE);
            findViewById(R.id.teamslayout).setVisibility(View.VISIBLE);
            ((ImageView)findViewById(R.id.imageView132)).setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.team1header,null));

            ((ImageView)findViewById(R.id.imageView137)).setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.team2header,null));

            ((ImageView)findViewById(R.id.imageView133)).setImageDrawable(getPieceDrawableByColor("blue"));
            ((ImageView)findViewById(R.id.imageView135)).setImageDrawable(getPieceDrawableByColor("green"));
            ((ImageView)findViewById(R.id.imageView138)).setImageDrawable(getPieceDrawableByColor("red"));
            ((ImageView)findViewById(R.id.imageView140)).setImageDrawable(getPieceDrawableByColor("yellow"));

            ((TextView)findViewById(R.id.team1name1)).setText(player3name);
            ((TextView)findViewById(R.id.team1name2)).setText(player2name);
            ((TextView)findViewById(R.id.team2name1)).setText(player1name);
            ((TextView)findViewById(R.id.team2name2)).setText(player4name);
        }

        if (gametype == 3) {
            findViewById(R.id.constraintLayout8).setVisibility(View.VISIBLE);
            findViewById(R.id.imageView114).setVisibility(GONE);
            findViewById(R.id.teamslayout).setVisibility(GONE);
            for (Player p : players) {
                List<Piece> pieces = getPiecesByColor(p.getColor());
                for (int i = 0; i < 2; i++) {
                    pieces.get(i).makeAlive();
                }
                globalHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkAdjustments(pieces.get(0).startPosition);
                    }
                },420);
            }
        }

        if(gametype==4) {
            findViewById(R.id.constraintLayout8).setVisibility(View.VISIBLE);
            findViewById(R.id.imageView114).setVisibility(View.VISIBLE);
            findViewById(R.id.teamslayout).setVisibility(GONE);
        }


            Runnable blinkAnim = new Runnable() {
                @Override
                public void run() {
                    if(gameStartImageView.getVisibility()==View.INVISIBLE) {
                        gameStartImageView.setVisibility(View.VISIBLE);
                    } else {
                        gameStartImageView.setVisibility(View.INVISIBLE);
                    }
                    globalHandler.postDelayed(this,220);
                }
            };

            globalHandler.post(blinkAnim);

            gameStartSound = MediaPlayer.create(this,R.raw.gamestartsound);
            if (gameStartSound != null) {
                try {
                    gameStartSound.start();
                } catch (Exception ignored) {}
                gameStartSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        globalHandler.removeCallbacks(blinkAnim);
                        gameStartImageView.setVisibility(GONE);
                        mainDiceImageView.setVisibility(View.VISIBLE);
                        hintArrow.setVisibility(View.VISIBLE);
                        switchPlayers();
                        try { gameStartSound.release(); } catch (Exception ignored) {}
                    }
                });
            } else {
                globalHandler.postDelayed(() -> {
                    globalHandler.removeCallbacks(blinkAnim);
                    gameStartImageView.setVisibility(GONE);
                    mainDiceImageView.setVisibility(View.VISIBLE);
                    hintArrow.setVisibility(View.VISIBLE);
                    switchPlayers();
                }, 900);
            }
            mainDiceImageView.setVisibility(GONE);
            hintArrow.setVisibility(GONE);
            d = new Dice(mainDiceImageView, nop, color);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(((int)(pxWidth*0.4)),((int)(pxWidth*0.4)));
        redHomeBlink.setLayoutParams(lp);
        lp.addRule(RelativeLayout.ALIGN_TOP,R.id.imageView); lp.addRule(RelativeLayout.ALIGN_LEFT,R.id.imageView);

        }


    private void stopEverything() {
        globalHandler.removeCallbacksAndMessages(null);
        d.diceHandler.removeCallbacksAndMessages(null);
        if(congratulationSound!=null) {
            if(congratulationSound.isPlaying()) {
                congratulationSound.stop();
            }
            try { congratulationSound.release(); } catch (Exception e) {}
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void hideThisPlayerDiceBg(int index) {
        switch (index) {
            case 1:
                findViewById(R.id.bleftdicebg).setVisibility(GONE);
                break;
            case 2:
                findViewById(R.id.tleftdicebg).setVisibility(GONE);
                break;
            case 3:
                findViewById(R.id.trightdicebg).setVisibility(GONE);
                break;
            case 4:
                findViewById(R.id.brightdicebg).setVisibility(GONE);
                break;
        }
    }

    private void removeThisPlayerByIndex(int index) {
        if(currentPlayerIndex!=0) { currentPlayerIndex--; }
        switch (index) {
            case 1:
                rmp1bg.setAlpha(0.5f);
                rmpp1removeicon.setOnClickListener(null);
                if(currentPlayerIndex==(rmpindexforp1-1)) {
                    players.remove(rmpindexforp1-1);
                    rmpindexforp2--;
                    rmpindexforp3--;
                    rmpindexforp4--;
                    switchPlayers();
                } else {
                    currentPlayerIndex++;
                    players.remove(rmpindexforp1-1);
                    rmpindexforp3--;
                    rmpindexforp4--;
                }
                hideThisPlayerDiceBg(1);
                for(Piece p : getPiecesByColor(player1color)) {
                    p.piece.setVisibility(GONE);
                }
                p1exitbox.setImageDrawable(getExitBoxByColor(player1color));
                p1exitbox.setVisibility(View.VISIBLE);
                break;
            case 2:
                rmp2bg.setAlpha(0.5f);
                rmpp2removeicon.setOnClickListener(null);
                if(currentPlayerIndex==(rmpindexforp2-1)) {
                    players.remove(rmpindexforp2-1);
                    rmpindexforp3--;
                    rmpindexforp4--;
                    switchPlayers();
                } else {
                    currentPlayerIndex++;
                    players.remove(rmpindexforp2-1);
                    rmpindexforp3--;
                    rmpindexforp4--;
                }
                hideThisPlayerDiceBg(2);
                for(Piece p : getPiecesByColor(player2color)) {
                    p.piece.setVisibility(GONE);
                }
                p2exitbox.setImageDrawable(getExitBoxByColor(player2color));
                p2exitbox.setVisibility(View.VISIBLE);
                break;
            case 3:
                rmp3bg.setAlpha(0.5f);
                rmpp3removeicon.setOnClickListener(null);
                if(currentPlayerIndex==(rmpindexforp3-1)) {
                    players.remove(rmpindexforp3-1);
                    rmpindexforp4--;
                    switchPlayers();
                } else {
                    currentPlayerIndex++;
                    players.remove(rmpindexforp3-1);
                    rmpindexforp4--;
                }
                hideThisPlayerDiceBg(3);
                for(Piece p : getPiecesByColor(player3color)) {
                    p.piece.setVisibility(GONE);
                }
                p3exitbox.setImageDrawable(getExitBoxByColor(player3color));
                p3exitbox.setVisibility(View.VISIBLE);
                break;
            case 4:
                rmp4bg.setAlpha(0.5f);
                rmpp4removeicon.setOnClickListener(null);
                if(currentPlayerIndex==(rmpindexforp4-1-1)) {
                    players.remove(rmpindexforp4-1);
                    switchPlayers();
                } else {
                    currentPlayerIndex++;
                    players.remove(rmpindexforp4-1);
                }
                hideThisPlayerDiceBg(4);
                for(Piece p : getPiecesByColor(player4color)) {
                    p.piece.setVisibility(GONE);
                }
                p4exitbox.setImageDrawable(getExitBoxByColor(player4color));
                p4exitbox.setVisibility(View.VISIBLE);
                break;
        }

        if(players.size()==2) {
            menuremoveplayersbtn.setVisibility(GONE);
        }

    }

    private Drawable getExitBoxByColor(String color) {
        switch (color)
        {
            case "red":
                return ResourcesCompat.getDrawable(getResources(),R.drawable.redexitbox,null);
            case "blue":
                return ResourcesCompat.getDrawable(getResources(),R.drawable.blueexitbox,null);
            case "green":
                return ResourcesCompat.getDrawable(getResources(),R.drawable.greenexitbox,null);
            case "yellow":
                return ResourcesCompat.getDrawable(getResources(),R.drawable.yellowexitbox,null);
        }
        return ResourcesCompat.getDrawable(getResources(),R.drawable.redexitbox,null);
    }

    void addPiecesToRmpLayout(String color,String name,int index) {
        switch (index) {
            case 1:
                rmpp1piece.setImageDrawable(getPieceDrawableByColor(color));
                rmpp1name.setText(name);
                break;
            case 2:
                rmpp2piece.setImageDrawable(getPieceDrawableByColor(color));
                rmpp2name.setText(name);
                break;
            case 3:
                rmpp3piece.setImageDrawable(getPieceDrawableByColor(color));
                rmpp3name.setText(name);
                break;
            case 4:
                rmpp4piece.setImageDrawable(getPieceDrawableByColor(color));
                rmpp4name.setText(name);
                break;
        }
    }


    void switchPlayers()
    {
        Player currentPlayer = players.get(currentPlayerIndex);
        currentPlayerPosition = currentPlayer.getPosition();
        currentPlayerColor = currentPlayer.getColor();
        currentPlayerSelectedIndex = currentPlayer.getIndex();
        currentPlayerName = currentPlayer.name;
        //Toast.makeText(this, currentPlayerColor+"", Toast.LENGTH_SHORT).show();
        currentPlayer.setActive();
        if(currentPlayer.isBot) {
            hintArrow.setVisibility(GONE);
            moveDice(currentPlayerPosition);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    d.roll();
                }
            },150);
        } else {
            moveDice(currentPlayerPosition);
            hintArrow.setVisibility(View.VISIBLE);
        }

        if(currentPlayerIndex>=(players.size()-1)) { currentPlayerIndex = 0; } else { currentPlayerIndex++; }
    }

    void moveDice(int position)
    {
        switch(position)
        {
            case 1:
                moveToTopLeft();
                break;
            case 2:
                moveToTopRight();
                break;
            case 3:
                moveToBottomLeft();
                break;
            case 4:
                moveToBottomRight();
                break;
        }

    }

    private void createPieces(String color, int position,String playername,boolean isBot) {
        if(nop==2) {
            if(players.size()==0)
            { players.add(new Player(position, color, playername, isBot, 0)); }
            else
            { players.add(new Player(position, color, playername, isBot, 2)); }
        }else {
            players.add(new Player(position, color, playername, isBot, players.size()));
            addPiecesToRmpLayout(color,getPlayerNameByPosition(position).getText().toString(),players.size());
        }
        float pos[][];
        int sPos[] = {39,0,26,13};
        switch (position) {case 1: pos = pos1; break; case 2: pos = pos2; break; case 3: pos = pos3; break; case 4: pos = pos4; break; default: pos = new float[4][2]; }
        switch (color) {
            case "red":
                rp.add(new Piece("red", redpiece1, findViewById(R.id.red1activecircle), findViewById(R.id.imageView6), findViewById(R.id.imageView7),  sPos[position - 1], pos[0][0], pos[0][1],isBot));
                rp.add(new Piece("red", redpiece2, findViewById(R.id.red2activecircle), findViewById(R.id.imageView62), findViewById(R.id.imageView72),  sPos[position - 1], pos[1][0], pos[1][1],isBot));
                rp.add(new Piece("red", redpiece3, findViewById(R.id.red3activecircle), findViewById(R.id.imageView63), findViewById(R.id.imageView73),  sPos[position - 1], pos[2][0], pos[2][1],isBot));
                rp.add(new Piece("red", redpiece4, findViewById(R.id.red4activecircle), findViewById(R.id.imageView64), findViewById(R.id.imageView74),  sPos[position - 1], pos[3][0], pos[3][1],isBot));
                break;
            case "green":
                gp.add(new Piece("green", greenpiece1, findViewById(R.id.green1activecircle), findViewById(R.id.imageView51), findViewById(R.id.imageView81),  sPos[position - 1], pos[0][0], pos[0][1],isBot));
                gp.add(new Piece("green", greenpiece2, findViewById(R.id.green2activecircle), findViewById(R.id.imageView52), findViewById(R.id.imageView82),  sPos[position - 1], pos[1][0], pos[1][1],isBot));
                gp.add(new Piece("green", greenpiece3, findViewById(R.id.green3activecircle), findViewById(R.id.imageView53), findViewById(R.id.imageView83),  sPos[position - 1], pos[2][0], pos[2][1],isBot));
                gp.add(new Piece("green", greenpiece4, findViewById(R.id.green4activecircle), findViewById(R.id.imageView54), findViewById(R.id.imageView84),  sPos[position - 1], pos[3][0], pos[3][1],isBot));
                break;
            case "blue":
                bp.add(new Piece("blue", bluepiece1, findViewById(R.id.blue1activecircle), findViewById(R.id.imageView41), findViewById(R.id.imageView91),  sPos[position - 1], pos[0][0], pos[0][1],isBot));
                bp.add(new Piece("blue", bluepiece2, findViewById(R.id.blue2activecircle), findViewById(R.id.imageView42), findViewById(R.id.imageView92),  sPos[position - 1], pos[1][0], pos[1][1],isBot));
                bp.add(new Piece("blue", bluepiece3, findViewById(R.id.blue3activecircle), findViewById(R.id.imageView43), findViewById(R.id.imageView93),  sPos[position - 1], pos[2][0], pos[2][1],isBot));
                bp.add(new Piece("blue", bluepiece4, findViewById(R.id.blue4activecircle), findViewById(R.id.imageView44), findViewById(R.id.imageView94),  sPos[position - 1], pos[3][0], pos[3][1],isBot));
                break;
            case "yellow":
                yp.add(new Piece("yellow", yellowpiece1, findViewById(R.id.yellow1activecircle), findViewById(R.id.imageView31), findViewById(R.id.imageView101),  sPos[position - 1], pos[0][0], pos[0][1],isBot));
                yp.add(new Piece("yellow", yellowpiece2, findViewById(R.id.yellow2activecircle), findViewById(R.id.imageView32), findViewById(R.id.imageView102),  sPos[position - 1], pos[1][0], pos[1][1],isBot));
                yp.add(new Piece("yellow", yellowpiece3, findViewById(R.id.yellow3activecircle), findViewById(R.id.imageView33), findViewById(R.id.imageView103),  sPos[position - 1], pos[2][0], pos[2][1],isBot));
                yp.add(new Piece("yellow", yellowpiece4, findViewById(R.id.yellow4activecircle), findViewById(R.id.imageView34), findViewById(R.id.imageView104),  sPos[position - 1], pos[3][0], pos[3][1],isBot));
                break;
        }
    }

    View getStepBubble(int n)
    {
        switch (n)
        {
            case 1:
                return step1;
            case 2:
                return step2;
            case 3:
                return step3;
            case 4:
                return step4;
            case 5:
                return step5;
            case 6:
                return step6;
        }
        return step1;
    }

    void initViews()
    {
        sharedPreferences = getSharedPreferences("LudoModUser",MODE_PRIVATE);

        botwins= sharedPreferences.getInt("botwins",0);
        botloses=sharedPreferences.getInt("botloses",0);
        ((TextView)findViewById(R.id.userwins)).setText((botwins+""));
        ((TextView)findViewById(R.id.userloses)).setText((botloses+""));

        isSoundOn = sharedPreferences.getBoolean("sound",true);
        isMusicOn = sharedPreferences.getBoolean("music",false);

        completeBackground = findViewById(R.id.activitybg);
        displayMetrics = getResources().getDisplayMetrics();
        dpHeight = displayMetrics.heightPixels/displayMetrics.density;
        dpWidth = displayMetrics.widthPixels/displayMetrics.density;
        pxWidth = displayMetrics.widthPixels;
        pxHeight = displayMetrics.heightPixels-getStatusBarHeight();
        ludoBoard = findViewById(R.id.imageView);
        red1activecircle = findViewById(R.id.red1activecircle);
        red2activecircle = findViewById(R.id.red2activecircle);
        red3activecircle = findViewById(R.id.red3activecircle);
        red4activecircle = findViewById(R.id.red4activecircle);
        redHomeBlink = findViewById(R.id.imageView3);
        redpiece1 = findViewById(R.id.redpiece1);
        redpiece2 = findViewById(R.id.redpiece2);
        redpiece3 = findViewById(R.id.redpiece3);
        redpiece4 = findViewById(R.id.redpiece4);

        greenpiece1 = findViewById(R.id.greenpiece1);
        greenpiece2 = findViewById(R.id.greenpiece2);
        greenpiece3 = findViewById(R.id.greenpiece3);
        greenpiece4 = findViewById(R.id.greenpiece4);

        bluepiece1 = findViewById(R.id.bluepiece1);
        bluepiece2 = findViewById(R.id.bluepiece2);
        bluepiece3 = findViewById(R.id.bluepiece3);
        bluepiece4 = findViewById(R.id.bluepiece4);

        yellowpiece1 = findViewById(R.id.yellowpiece1);
        yellowpiece2 = findViewById(R.id.yellowpiece2);
        yellowpiece3 = findViewById(R.id.yellowpiece3);
        yellowpiece4 = findViewById(R.id.yellowpiece4);

        // player names 1 to 4
        playername1 = findViewById(R.id.pname1);
        playername2 = findViewById(R.id.pname2);
        playername3 = findViewById(R.id.pname3);
        playername4 = findViewById(R.id.pname4);

        mainDiceImageView = findViewById(R.id.maindiceimageview);
        hintArrow = findViewById(R.id.hintarrowleft);

        crownIndex1 = findViewById(R.id.winnercrownindex1);
        crownIndex2 = findViewById(R.id.winnercrownindex2);
        crownIndex3 = findViewById(R.id.winnercrownindex3);
        crownIndex4 = findViewById(R.id.winnercrownindex4);

        // ingame menu items
        ingamemenubtn = findViewById(R.id.ingamemenubtn);
        ingamemenuitemslayout = findViewById(R.id.ingamemenuitemslayout);
        menuremoveplayersbtn = findViewById(R.id.rmpitemmenu);
        menuexitbtn = findViewById(R.id.exititemmenu);

        // ingame exitlayout views
        quitgamelayout = findViewById(R.id.ingamequitlayout);
        ingameyesbtn = findViewById(R.id.ingamequitlayoutyesbtn);
        ingamenobtn = findViewById(R.id.ingamequitlayoutnobtn);
        ingamesoundbtn = findViewById(R.id.ingamequitlayoutsoundbtn);
        ingamemusicbtn = findViewById(R.id.ingamequitlayoutmusicbtn);

        //in game rmplayout
        ingamermplayout = findViewById(R.id.ingamermplayout);
        rmp1bg = findViewById(R.id.rmp1);
        rmp2bg = findViewById(R.id.rmp2);
        rmp3bg = findViewById(R.id.rmp3);
        rmp4bg = findViewById(R.id.rmp4);
        rmpp1piece = findViewById(R.id.rmp1piece);
        rmpp2piece = findViewById(R.id.rmp2piece);
        rmpp3piece = findViewById(R.id.rmp3piece);
        rmpp4piece = findViewById(R.id.rmp4piece);
        rmpp1removeicon = findViewById(R.id.rmp1btn);
        rmpp2removeicon = findViewById(R.id.rmp2btn);
        rmpp3removeicon = findViewById(R.id.rmp3btn);
        rmpp4removeicon = findViewById(R.id.rmp4btn);
        rmpbackbtn = findViewById(R.id.rmpbackbtn);
        rmpmenubtn = findViewById(R.id.rmpmenubtn);
        rmpp1name = findViewById(R.id.rmp1name);
        rmpp2name = findViewById(R.id.rmp2name);
        rmpp3name = findViewById(R.id.rmp3name);
        rmpp4name = findViewById(R.id.rmp4name);


        //in gmae rmp sublayout confirmrmplayout
        confirmrmplayout = findViewById(R.id.confirmrmplayout);
        selectedrmppiece = findViewById(R.id.confirmremovepieceview);
        confirmrmpyesbtn = findViewById(R.id.confirmrmpyesbtn);
        confirmrmpnobtn = findViewById(R.id.confirmrmpnobtn);

        // players exit boxes
        p1exitbox = findViewById(R.id.p1exitbox);
        p2exitbox = findViewById(R.id.p2exitbox);
        p3exitbox = findViewById(R.id.p3exitbox);
        p4exitbox = findViewById(R.id.p4exitbox);

        // congratulaions layout

        congratulationslayout = findViewById(R.id.congratulationsscreen);

        congratsmenubtn = findViewById(R.id.congratsscreenmenubtn);
        congratssoundbtn = findViewById(R.id.congratsscreensoundbtn);
        congratssharebtn = findViewById(R.id.congratssharebtn);
        congratsreplaybtn = findViewById(R.id.congratsscreenreplaybtn);

        winnerlistp1layout = findViewById(R.id.winnerpositionlayout1);
        winnerlistp2layout = findViewById(R.id.winnerpositionlayout2);
        winnerlistp3layout = findViewById(R.id.winnerpositionlayout3);
        winnerlistp4layout = findViewById(R.id.winnerpositionlayout4);

        wlistcrown1 = findViewById(R.id.winnerliscrown1);
        wlistcrown2 = findViewById(R.id.winnerliscrown2);
        wlistcrown3 = findViewById(R.id.winnerliscrown3);
        wlistcrown4 = findViewById(R.id.winnerliscrown4);
        wlistpiece1 = findViewById(R.id.winnerlistp1);
        wlistpiece2 = findViewById(R.id.winnerlistp2);
        wlistpiece3 = findViewById(R.id.winnerlistp3);
        wlistpiece4 = findViewById(R.id.winnerlistp4);
        wlistwinorlose1 = findViewById(R.id.winorlose1);
        wlistwinorlose2 = findViewById(R.id.winorlose2);
        wlistwinorlose3 = findViewById(R.id.winorlose3);
        wlistwinorlose4 = findViewById(R.id.winorlose4);

        wlistname1 = findViewById(R.id.winnerlistpname1);
        wlistname2 = findViewById(R.id.winnerlistpname2);
        wlistname3 = findViewById(R.id.winnerlistpname3);
        wlistname4 = findViewById(R.id.winnerlistpname4);

        gameStartImageView = findViewById(R.id.gamestartimageview);

        pos1 = new float[4][2];
        pos2 = new float[4][2];
        pos3 = new float[4][2];
        pos4 = new float[4][2];

        onePercentWidth = (float) pxWidth/100;
        onePercentHeight = (float) pxHeight/100;

        players = new ArrayList<>();

        rp = new ArrayList<>();
        gp = new ArrayList<>();
        bp = new ArrayList<>();
        yp = new ArrayList<>();

        safeSpots = new HashSet<>();

        safeSpots.add(0);safeSpots.add(8);safeSpots.add(13);safeSpots.add(21);safeSpots.add(26);safeSpots.add(34);safeSpots.add(39);safeSpots.add(47);
        //safeSpots.add(57);safeSpots.add(63);safeSpots.add(69);safeSpots.add(75);

        step1 = findViewById(R.id.step1);
        step2 = findViewById(R.id.step2);
        step3 = findViewById(R.id.step3);
        step4 = findViewById(R.id.step4);
        step5 = findViewById(R.id.step5);
        step6 = findViewById(R.id.step6);

        int boxSize = (int)(Math.ceil(pxWidth*0.0667)*1.3);
        sizeOfBox = (int)(Math.ceil(pxWidth*0.0667)*0.15);

        blockSize = (float) (pxWidth*0.0667);

        RelativeLayout.LayoutParams stepsParams = new RelativeLayout.LayoutParams(boxSize,boxSize);
        step1.setLayoutParams(stepsParams);
        step2.setLayoutParams(stepsParams);
        step3.setLayoutParams(stepsParams);
        step4.setLayoutParams(stepsParams);
        step5.setLayoutParams(stepsParams);
        step6.setLayoutParams(stepsParams);


        redCircle = AppCompatResources.getDrawable(this,R.drawable.red_circle_shaded);
        greenCircle = AppCompatResources.getDrawable(this,R.drawable.green_circle_shaded);
        blueCircle = AppCompatResources.getDrawable(this,R.drawable.blue_circle_shaded);
        yellowCircle = AppCompatResources.getDrawable(this,R.drawable.yellow_circle_shaded);

        wBlocks52 = new int[] {52, 53, 54, 55, 56, 57};
        wBlocks58 = new int[] {58, 59, 60, 61, 62, 63};
        wBlocks64 = new int[] {64, 65, 66, 67, 68, 69};
        wBlocks70 = new int[] {70, 71, 72, 73, 74, 75};

        diceRollSound = MediaPlayer.create(this, R.raw.diceroll);
        stepSound = MediaPlayer.create(this, R.raw.step);
//        safeSound = MediaPlayer.create(this, R.raw.safe);
//        deathSound = MediaPlayer.create(this, R.raw.death);
//        pantaSound = MediaPlayer.create(this, R.raw.panta);
//        congratulationSound = MediaPlayer.create(this, R.raw.congratulations);

        if(isSoundOn) {
            ingamesoundbtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.soundon,null));
            congratssoundbtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.soundon,null));
        } else { ingamesoundbtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.soundoff,null));
            congratssoundbtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.soundoff,null));}

        if(isMusicOn) {
            ingamemusicbtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.musicon,null));
        } else { ingamemusicbtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(),R.drawable.musicoff,null)); }


        editText = findViewById(R.id.editTextNumber);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(((int)(pxWidth)),((int)(pxWidth)));
        ludoBoard.setLayoutParams(layoutParams); // size of board set
    }

    void initializeAnimatorSets() {
        animatorSets = new AnimatorSet[6];

        for (int i = 0; i < animatorSets.length; i++) {
            View v = getStepBubble(i+1);
            animatorSets[i] = createAnimatorSet(v);
            animatorSets[i].addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    v.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    void showStep(int viewIndex,String color, float x, float y) {
        if (animatorSets == null || animatorSets.length != 6) {
            initializeAnimatorSets();
        }

        View view = null;
        view = getStepBubble(viewIndex);

        if (view != null) {
            view.setBackground(getColorDrawable(color));
            view.setTranslationX(x-sizeOfBox);
            view.setTranslationY(y-sizeOfBox);

            AnimatorSet animatorSet = animatorSets[viewIndex - 1];
            view.setVisibility(View.VISIBLE);
            animatorSet.start();
        }
    }

    private AnimatorSet createAnimatorSet(View view) {
        AnimatorSet animatorSet = new AnimatorSet();

        float initialScale = 0.5f;
        float finalScale = 1.0f;
        long duration = 700;
        PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, initialScale, finalScale);
        PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, initialScale, finalScale);
        ObjectAnimator scaleAnimator = ObjectAnimator.ofPropertyValuesHolder(view, scaleX, scaleY);
        scaleAnimator.setDuration(600);

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(view, View.ALPHA, 1.0f, 0.0f);
        alphaAnimator.setDuration(800);

        animatorSet.playTogether(scaleAnimator, alphaAnimator);

        return animatorSet;
    }

    Drawable getColorDrawable(String color)
    {
        switch (color)
        {
            case "red":
                return redCircle;
            case "green":
                return greenCircle;
            case "blue":
                return blueCircle;
            case "yellow":
                return yellowCircle;
        }
        return redCircle;
    }

    void setFullScreen() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            View dview = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            dview.setSystemUiVisibility(uiOptions);
        } else {
            View dview = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            dview.setSystemUiVisibility(uiOptions);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
                WindowInsetsController insetsController = getWindow().getInsetsController();
                if(insetsController !=null) {
                    insetsController.hide(WindowInsets.Type.navigationBars());
                    insetsController.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
                }
            }

        }
    }

    float getNavHeight()
    {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public float getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
        View dview = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        dview.setSystemUiVisibility(uiOptions);
    }

    void moveToTopLeft()
    {
        ConstraintLayout.LayoutParams lps = (ConstraintLayout.LayoutParams) mainDiceImageView.getLayoutParams();
        lps.startToStart = ConstraintLayout.LayoutParams.UNSET;
        lps.setMarginStart(-((int)displayMetrics.density));
        lps.endToEnd = R.id.tleftdicebg;
        lps.bottomToBottom = R.id.tleftdicebg;
        mainDiceImageView.setLayoutParams(lps);
        ConstraintLayout.LayoutParams arrowLps = (ConstraintLayout.LayoutParams) hintArrow.getLayoutParams();
        arrowLps.endToStart = ConstraintLayout.LayoutParams.UNSET;
        arrowLps.startToEnd = R.id.tleftdicebg;
        arrowLps.topToTop = R.id.tleftdicebg;
        arrowLps.bottomToBottom = R.id.tleftdicebg;
        hintArrow.setRotation(0);
        hintArrow.setLayoutParams(arrowLps);
    }

    void moveToTopRight()
    {
        ConstraintLayout.LayoutParams lps = (ConstraintLayout.LayoutParams) mainDiceImageView.getLayoutParams();
        lps.startToStart = R.id.trightdicebg;
        lps.setMarginStart(-((int)(2*displayMetrics.density)));
        lps.endToEnd = ConstraintLayout.LayoutParams.UNSET;
        lps.bottomToBottom = R.id.trightdicebg;
        mainDiceImageView.setLayoutParams(lps);
        ConstraintLayout.LayoutParams arrowLps = (ConstraintLayout.LayoutParams) hintArrow.getLayoutParams();
        arrowLps.startToEnd = ConstraintLayout.LayoutParams.UNSET;
        arrowLps.endToStart = R.id.trightdicebg;
        arrowLps.topToTop = R.id.trightdicebg;
        arrowLps.bottomToBottom = R.id.trightdicebg;
        hintArrow.setRotation(180);
        hintArrow.setLayoutParams(arrowLps);
    }

    void moveToBottomRight()
    {
        ConstraintLayout.LayoutParams lps = (ConstraintLayout.LayoutParams) mainDiceImageView.getLayoutParams();
        lps.startToStart = R.id.brightdicebg;
        lps.setMarginStart(-((int)(2*displayMetrics.density)));
        lps.endToEnd = ConstraintLayout.LayoutParams.UNSET;
        lps.bottomToBottom = R.id.brightdicebg;
        mainDiceImageView.setLayoutParams(lps);
        ConstraintLayout.LayoutParams arrowLps = (ConstraintLayout.LayoutParams) hintArrow.getLayoutParams();
        arrowLps.startToEnd = ConstraintLayout.LayoutParams.UNSET;
        arrowLps.endToStart = R.id.brightdicebg;
        arrowLps.topToTop = R.id.brightdicebg;
        arrowLps.bottomToBottom = R.id.brightdicebg;
        hintArrow.setRotation(180);
        hintArrow.setLayoutParams(arrowLps);
    }

    void moveToBottomLeft()
    {
        ConstraintLayout.LayoutParams lps = (ConstraintLayout.LayoutParams) mainDiceImageView.getLayoutParams();
        lps.startToStart = ConstraintLayout.LayoutParams.UNSET;
        lps.setMarginStart(-((int)displayMetrics.density));
        lps.endToEnd = R.id.bleftdicebg;
        lps.bottomToBottom = R.id.bleftdicebg;
        mainDiceImageView.setLayoutParams(lps);
        ConstraintLayout.LayoutParams arrowLps = (ConstraintLayout.LayoutParams) hintArrow.getLayoutParams();
        arrowLps.endToStart = ConstraintLayout.LayoutParams.UNSET;
        arrowLps.startToEnd = R.id.bleftdicebg;
        arrowLps.topToTop = R.id.bleftdicebg;
        arrowLps.bottomToBottom = R.id.bleftdicebg;
        hintArrow.setRotation(0);
        hintArrow.setLayoutParams(arrowLps);
    }

    int[] getWinnerBlocks(int n)
    {
        switch (n)
        {
            case 0:
                return wBlocks52;
            case 11:
                return wBlocks58;
            case 24:
                return wBlocks64;
            case 37:
                return wBlocks70;
            default:
                return wBlocks52;
        }
    }

    public static final long RAPID_TAP_INTERVAL_MS = 500L;
    public static final int RAPID_TAP_THRESHOLD = 3;

    public static int updateRapidTapCount(int currentTapCount, long lastTapTimeMs, long currentTapTimeMs) {
        if (lastTapTimeMs > 0L && (currentTapTimeMs - lastTapTimeMs) >= 0L && (currentTapTimeMs - lastTapTimeMs) <= RAPID_TAP_INTERVAL_MS) {
            return currentTapCount + 1;
        }
        return 1;
    }

    public static int updateColorRapidTapCount(int currentTapCount, String lastTappedColor, String currentTappedColor, long lastTapTimeMs, long currentTapTimeMs) {
        if (currentTappedColor == null || currentTappedColor.isEmpty()) {
            return 0;
        }
        if (currentTappedColor.equals(lastTappedColor)) {
            return updateRapidTapCount(currentTapCount, lastTapTimeMs, currentTapTimeMs);
        }
        return 1;
    }

    public static boolean shouldForceSix(int rapidTapCount) {
        // 3-tap forced 6 is disabled; 3 rapid taps now silently lock the winner color once per match
        return false;
    }

    public static int resolveDiceValue(int randomRoll, boolean isBot, boolean forceSixOnRoll, int rapidTapCount) {
        return (randomRoll >= 1 && randomRoll <= 6) ? randomRoll : 1;
    }

    public static String tryLockWinnerColor(String currentLockedColor, String tappedColor, int rapidTapCount, boolean isBot) {
        if (currentLockedColor != null && !currentLockedColor.isEmpty()) {
            return currentLockedColor;
        }
        if (!isBot && tappedColor != null && !tappedColor.isEmpty() && rapidTapCount >= RAPID_TAP_THRESHOLD) {
            return tappedColor;
        }
        return null;
    }

    public static boolean isSameTeamColor(String colorA, String colorB) {
        if (colorA == null || colorB == null) {
            return false;
        }
        if (colorA.equals(colorB)) {
            return true;
        }
        boolean aRedYellow = colorA.equals("red") || colorA.equals("yellow");
        boolean bRedYellow = colorB.equals("red") || colorB.equals("yellow");
        if (aRedYellow && bRedYellow) {
            return true;
        }
        boolean aBlueGreen = colorA.equals("blue") || colorA.equals("green");
        boolean bBlueGreen = colorB.equals("blue") || colorB.equals("green");
        return aBlueGreen && bBlueGreen;
    }

    public static boolean isFavoredColor(String candidateColor, String lockedWinnerColor, int gameType) {
        if (candidateColor == null || lockedWinnerColor == null || lockedWinnerColor.isEmpty()) {
            return false;
        }
        if (candidateColor.equals(lockedWinnerColor)) {
            return true;
        }
        if (gameType == 2) {
            return isSameTeamColor(candidateColor, lockedWinnerColor);
        }
        return false;
    }

    public static int selectSmartFairRoll(
            int randomRoll,
            boolean isFavoredPlayer,
            int[] playerPieceSteps,
            int completedPieces,
            int favoredCompletedPieces,
            int gameType,
            int consecutiveSixes
    ) {
        int normalized = (randomRoll >= 1 && randomRoll <= 6) ? randomRoll : 1;
        int pantaValue = (gameType == 3) ? 1 : 4;

        if (isFavoredPlayer) {
            boolean anyAlive = false;
            boolean anyInBase = false;
            int homeFinishRoll = -1;
            for (int steps : playerPieceSteps) {
                if (steps == -1) {
                    anyInBase = true;
                } else if (steps >= 0 && steps < 56) {
                    anyAlive = true;
                    int needed = 56 - steps;
                    if (needed >= 1 && needed <= 6 && (needed != 6 || consecutiveSixes < 2)) {
                        homeFinishRoll = needed;
                    }
                }
            }
            if (homeFinishRoll != -1) {
                return homeFinishRoll;
            }
            if (!anyAlive && anyInBase && consecutiveSixes < 2) {
                return 6;
            }
            if (normalized == 6 && consecutiveSixes >= 2) {
                return 4;
            }
            return normalized;
        } else {
            // Opponent player: allow completing non-final pieces freely; only block the final match-winning piece
            boolean mustBlockCompletion = (gameType == 3) || (completedPieces >= pantaValue - 1);

            if (!mustBlockCompletion) {
                if (normalized == 6 && consecutiveSixes >= 2) {
                    return 4;
                }
                return normalized;
            }

            int bestRoll = normalized;
            int bestScore = -100000;
            for (int cand = 1; cand <= 6; cand++) {
                int score = (cand == normalized) ? 50 : 15;
                if (cand == 6 && consecutiveSixes >= 1) {
                    score -= 5000;
                }
                for (int steps : playerPieceSteps) {
                    if (steps >= 0 && steps < 56) {
                        int next = steps + cand;
                        if (next == 56) {
                            score -= 50000;
                        }
                    }
                }
                if (score > bestScore) {
                    bestScore = score;
                    bestRoll = cand;
                }
            }
            return bestRoll;
        }
    }

    private boolean isThreatenedByEnemyAtBlock(int block, String pieceColor) {
        if (block < 0 || block > 51 || safeSpots.contains(block)) {
            return false;
        }
        for (Player pl : players) {
            String enemyColor = pl.getColor();
            if (enemyColor.equals(pieceColor) || (gametype == 2 && isSameTeamColor(enemyColor, pieceColor))) {
                continue;
            }
            for (Piece ep : getPiecesByColor(enemyColor)) {
                if (ep.isAlive && !ep.hasCompletedItsPurpose && ep.numberOfSteps <= 50) {
                    int dist = (block - ep.currBlock + 52) % 52;
                    if (dist >= 1 && dist <= 6 && ep.numberOfSteps + dist <= 50) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean hasUnprotectedEnemyAtBlock(int block, String moverColor, boolean onlyNonFavoredTargets) {
        if (block < 0 || block > 51 || safeSpots.contains(block)) {
            return false;
        }
        for (Player pl : players) {
            String otherColor = pl.getColor();
            if (otherColor.equals(moverColor) || (gametype == 2 && isSameTeamColor(otherColor, moverColor))) {
                continue;
            }
            if (onlyNonFavoredTargets && d != null && isFavoredColor(otherColor, d.lockedWinnerColor, gametype)) {
                continue;
            }
            for (Piece ep : getPiecesByColor(otherColor)) {
                if (ep.isAlive && !ep.hasCompletedItsPurpose && ep.currBlock == block) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String formatTeamBoardLabel(String color, String rawName) {
        String teamTag = ("blue".equalsIgnoreCase(color) || "green".equalsIgnoreCase(color))
                ? "Team A"
                : "Team B";
        if (rawName == null) {
            return teamTag;
        }
        String trimmed = rawName.trim();
        if (trimmed.isEmpty()
                || trimmed.matches("(?i)^p[1-4]$")
                || trimmed.matches("(?i)^player\\s*[1-4a-d]$")) {
            return teamTag;
        }
        if (trimmed.toLowerCase().startsWith("team a") || trimmed.toLowerCase().startsWith("team b")) {
            return trimmed;
        }
        return teamTag + " - " + trimmed;
    }

    public static boolean canAllowFavoredTokenCut(
            int gameType,
            int favoredTimesCut,
            int favoredTokenSteps,
            int favoredActiveAndCompletedCount,
            int favoredRemainingSteps,
            int minOpponentRemainingSteps,
            boolean opponentNearWin,
            boolean isFinalWinningToken
    ) {
        if (favoredTokenSteps < 0 || favoredTokenSteps > 50) {
            return false;
        }
        // Only protect a locked winner token in a genuine final-piece endgame crisis:
        // when the opponent is on their final winning token and cutting would leave the locked winner far behind.
        if (opponentNearWin && isFinalWinningToken && favoredRemainingSteps > minOpponentRemainingSteps + 24) {
            return false;
        }
        int minSafeActive = (gameType == 2) ? 2 : 1;
        if (opponentNearWin && favoredActiveAndCompletedCount <= minSafeActive && favoredTokenSteps >= 40) {
            return false;
        }
        if (gameType == 3) {
            if (opponentNearWin && favoredTokenSteps >= 38) {
                return false;
            }
            return favoredTimesCut < 3 && favoredTokenSteps <= 50;
        }
        // Allow cuts throughout the entire match (early, mid, and late game on all outer track steps 0..50)
        // so opponents never feel blocked from cutting locked winner tokens even after multiple cuts.
        int maxAllowedCuts = (gameType == 2) ? 14 : 10;
        return favoredTimesCut < maxAllowedCuts;
    }

    private boolean isSafeForOpponentToCutFavoredPiece(Piece fp) {
        if (d == null || d.lockedWinnerColor == null || fp == null || !fp.isAlive || fp.hasCompletedItsPurpose) {
            return false;
        }
        int pantaValue = (gametype == 3) ? 1 : 4;
        int favoredActiveAndCompleted = 0;
        int favoredRem = 0;
        int minOpponentRem = 9999;
        boolean opponentNearWin = false;

        if (gametype == 2) {
            String[] allColors = {"blue", "green", "red", "yellow"};
            int favTotalRem = 0;
            int oppTotalRem = 0;
            int oppTeamCompleted = 0;
            int maxOppSteps = 0;
            for (String col : allColors) {
                boolean fav = isFavoredColor(col, d.lockedWinnerColor, gametype);
                List<Piece> colPieces = getPiecesByColor(col);
                if (colPieces == null || colPieces.isEmpty()) {
                    continue;
                }
                int comp = 0;
                int rem = 0;
                for (Piece p : colPieces) {
                    if (p.hasCompletedItsPurpose || p.numberOfSteps >= 56 || p.isThisPlayerWon) {
                        comp++;
                        if (fav) {
                            favoredActiveAndCompleted++;
                        }
                    } else if (p.isAlive) {
                        if (fav) {
                            favoredActiveAndCompleted++;
                        } else if (p.numberOfSteps > maxOppSteps) {
                            maxOppSteps = p.numberOfSteps;
                        }
                        rem += Math.max(1, 56 - p.numberOfSteps);
                    } else {
                        rem += 62;
                    }
                }
                if (fav) {
                    favTotalRem += rem;
                } else {
                    oppTotalRem += rem;
                    oppTeamCompleted += comp;
                }
            }
            favoredRem = favTotalRem / 2;
            minOpponentRem = oppTotalRem / 2;
            opponentNearWin = (oppTeamCompleted >= 6)
                    || (oppTeamCompleted >= 5 && maxOppSteps >= 38)
                    || (minOpponentRem <= 52);
        } else {
            for (Player pl : players) {
                String col = pl.getColor();
                boolean fav = isFavoredColor(col, d.lockedWinnerColor, gametype);
                int comp = 0;
                int maxSteps = 0;
                int rem = (gametype == 3) ? 999 : 0;
                for (Piece p : getPiecesByColor(col)) {
                    int pRem;
                    if (p.hasCompletedItsPurpose || p.numberOfSteps >= 56) {
                        comp++;
                        if (fav) {
                            favoredActiveAndCompleted++;
                        }
                        pRem = 0;
                    } else if (p.isAlive) {
                        if (fav) {
                            favoredActiveAndCompleted++;
                        }
                        pRem = Math.max(1, 56 - p.numberOfSteps);
                        if (p.numberOfSteps > maxSteps) {
                            maxSteps = p.numberOfSteps;
                        }
                    } else {
                        pRem = 62;
                    }
                    if (gametype == 3) {
                        if (pRem < rem) {
                            rem = pRem;
                        }
                    } else {
                        rem += pRem;
                    }
                }
                if (fav) {
                    if (rem > favoredRem) {
                        favoredRem = rem;
                    }
                } else {
                    if (rem < minOpponentRem) {
                        minOpponentRem = rem;
                    }
                    if ((gametype == 3 && maxSteps >= 38)
                            || (gametype != 3 && comp >= pantaValue - 1)
                            || (gametype != 3 && comp >= pantaValue - 2 && maxSteps >= 42)) {
                        opponentNearWin = true;
                    }
                }
            }
        }
        return canAllowFavoredTokenCut(
                gametype,
                d.favoredTimesCut,
                fp.numberOfSteps,
                favoredActiveAndCompleted,
                favoredRem,
                minOpponentRem,
                opponentNearWin,
                false
        );
    }

    private int getMaxUnprotectedEnemyStepsAtBlock(int block, String moverColor, boolean onlyNonFavoredTargets) {
        if (block < 0 || block > 51 || safeSpots.contains(block)) {
            return -1;
        }
        int maxEnemySteps = -1;
        for (Player pl : players) {
            String otherColor = pl.getColor();
            if (otherColor.equals(moverColor) || (gametype == 2 && isSameTeamColor(otherColor, moverColor))) {
                continue;
            }
            if (onlyNonFavoredTargets && d != null && isFavoredColor(otherColor, d.lockedWinnerColor, gametype)) {
                continue;
            }
            for (Piece ep : getPiecesByColor(otherColor)) {
                if (ep.isAlive && !ep.hasCompletedItsPurpose && ep.currBlock == block) {
                    if (ep.numberOfSteps > maxEnemySteps) {
                        maxEnemySteps = ep.numberOfSteps;
                    }
                }
            }
        }
        return maxEnemySteps;
    }

    private int evaluateFavoredPieceAtBlockForOpponentCut(
            int block,
            boolean opponentNearWin,
            boolean isFinalWinningToken,
            int favoredActiveAndCompletedCount,
            int favoredRemainingSteps,
            int minOpponentRemainingSteps
    ) {
        if (d == null || d.lockedWinnerColor == null || block < 0 || block > 51 || safeSpots.contains(block)) {
            return 0;
        }
        int status = 0;
        String[] allColors = {"red", "green", "blue", "yellow"};
        for (String col : allColors) {
            if (isFavoredColor(col, d.lockedWinnerColor, gametype)) {
                List<Piece> fPieces = getPiecesByColor(col);
                if (fPieces != null) {
                    for (Piece fp : fPieces) {
                        if (fp.isAlive && !fp.hasCompletedItsPurpose && fp.currBlock == block) {
                            boolean safeToCut = canAllowFavoredTokenCut(
                                    gametype,
                                    d.favoredTimesCut,
                                    fp.numberOfSteps,
                                    favoredActiveAndCompletedCount,
                                    favoredRemainingSteps,
                                    minOpponentRemainingSteps,
                                    opponentNearWin,
                                    isFinalWinningToken
                            );
                            if (!safeToCut) {
                                return -1;
                            }
                            status = 1;
                        }
                    }
                }
            }
        }
        return status;
    }

    private boolean isThreatenedByFavoredAtBlock(int block) {
        if (d == null || d.lockedWinnerColor == null || block < 0 || block > 51 || safeSpots.contains(block)) {
            return false;
        }
        String[] allColors = {"red", "green", "blue", "yellow"};
        for (String col : allColors) {
            if (isFavoredColor(col, d.lockedWinnerColor, gametype)) {
                List<Piece> fPieces = getPiecesByColor(col);
                if (fPieces != null) {
                    for (Piece fp : fPieces) {
                        if (fp.isAlive && !fp.hasCompletedItsPurpose && fp.numberOfSteps <= 50) {
                            int dist = (block - fp.currBlock + 52) % 52;
                            if (dist >= 1 && dist <= 6 && fp.numberOfSteps + dist <= 50) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    private int recordAndReturnRoll(int chosenRoll, boolean isFavored) {
        if (d != null) {
            if (isFavored) {
                d.secondLastFavoredRoll = d.lastFavoredRoll;
                d.lastFavoredRoll = chosenRoll;
                d.turnsSinceFavoredCut++;
                if (chosenRoll == 6) {
                    d.turnsSinceLastFavoredSix = 0;
                } else {
                    d.turnsSinceLastFavoredSix++;
                }
            } else {
                d.secondLastOpponentRoll = d.lastOpponentRoll;
                d.lastOpponentRoll = chosenRoll;
                d.opponentTurnsSinceCutFavored++;
                if (chosenRoll == 6) {
                    d.turnsSinceLastOpponentSix = 0;
                } else {
                    d.turnsSinceLastOpponentSix++;
                }
            }
        }
        return chosenRoll;
    }

    private int selectSmartBoardDiceRoll(int randomRoll, Player currentPlayer) {
        int normalizedRoll = (randomRoll >= 1 && randomRoll <= 6) ? randomRoll : 1;
        if (d != null && normalizedRoll == 6) {
            if (d.consecutiveSixCount >= 2) {
                normalizedRoll = 3 + (int) (Math.random() * 3); // 3, 4, or 5 — never allow 3 sixes in a row
            } else if (d.consecutiveSixCount == 1 && Math.random() < 0.75) {
                // Keep natural double-6 rare so players don't get repeated multiple-6 streaks
                normalizedRoll = 2 + (int) (Math.random() * 4); // 2, 3, 4, or 5
            }
        }
        if (d == null || d.lockedWinnerColor == null || d.lockedWinnerColor.isEmpty() || currentWinnerPosition > 1) {
            return normalizedRoll;
        }

        String currentColor = getActivePieceControlColor(currentPlayer.getColor());
        boolean isFavored = isFavoredColor(currentColor, d.lockedWinnerColor, gametype);
        int pantaValue = (gametype == 3) ? 1 : 4;

        int favoredCompleted = 0;
        int favoredTeamCompleted = 0;
        int favoredActiveAndCompleted = 0;
        int favoredProgress = 0;
        int favoredRemainingSteps = 0;
        int maxOpponentCompleted = 0;
        int opponentTeamCompleted = 0;
        int maxOpponentProgress = 0;
        int minOpponentRemainingSteps = 9999;
        boolean anyOpponentAlive = false;
        boolean opponentNearWin = false;

        if (gametype == 2) {
            String[] allColors = {"blue", "green", "red", "yellow"};
            int favTotalRem = 0;
            int favTotalProg = 0;
            int oppTotalRem = 0;
            int oppTotalProg = 0;
            int maxOppSteps = 0;
            for (String col : allColors) {
                boolean colIsFavored = isFavoredColor(col, d.lockedWinnerColor, gametype);
                List<Piece> colPieces = getPiecesByColor(col);
                if (colPieces == null || colPieces.isEmpty()) {
                    continue;
                }
                int comp = 0;
                int prog = 0;
                int remSteps = 0;
                for (Piece p : colPieces) {
                    if (p.hasCompletedItsPurpose || p.numberOfSteps >= 56 || p.isThisPlayerWon) {
                        comp++;
                        if (colIsFavored) {
                            favoredActiveAndCompleted++;
                        }
                        prog += 65;
                    } else if (p.isAlive) {
                        if (colIsFavored) {
                            favoredActiveAndCompleted++;
                        } else {
                            anyOpponentAlive = true;
                            if (p.numberOfSteps > maxOppSteps) {
                                maxOppSteps = p.numberOfSteps;
                            }
                        }
                        prog += p.numberOfSteps + 8;
                        remSteps += Math.max(1, 56 - p.numberOfSteps);
                    } else {
                        remSteps += 62;
                    }
                }
                if (colIsFavored) {
                    favoredTeamCompleted += comp;
                    if (comp > favoredCompleted) {
                        favoredCompleted = comp;
                    }
                    favTotalRem += remSteps;
                    favTotalProg += prog;
                } else {
                    opponentTeamCompleted += comp;
                    if (comp > maxOpponentCompleted) {
                        maxOpponentCompleted = comp;
                    }
                    oppTotalRem += remSteps;
                    oppTotalProg += prog;
                }
            }
            // Normalize 2-teammate totals to a single 4-piece scale (0..248) so all thresholds match seamlessly
            favoredRemainingSteps = favTotalRem / 2;
            favoredProgress = favTotalProg / 2;
            minOpponentRemainingSteps = oppTotalRem / 2;
            maxOpponentProgress = oppTotalProg / 2;
            opponentNearWin = (opponentTeamCompleted >= 6)
                    || (opponentTeamCompleted >= 5 && maxOppSteps >= 38)
                    || (minOpponentRemainingSteps <= 52);
        } else {
            for (Player pl : players) {
                String col = pl.getColor();
                boolean colIsFavored = isFavoredColor(col, d.lockedWinnerColor, gametype);
                List<Piece> colPieces = getPiecesByColor(col);
                int comp = 0;
                int prog = 0;
                int maxSteps = 0;
                int remSteps = (gametype == 3) ? 999 : 0;
                for (Piece p : colPieces) {
                    int pRem;
                    if (p.hasCompletedItsPurpose || p.numberOfSteps >= 56) {
                        comp++;
                        if (colIsFavored) {
                            favoredActiveAndCompleted++;
                        }
                        prog += 65;
                        pRem = 0;
                    } else if (p.isAlive) {
                        if (colIsFavored) {
                            favoredActiveAndCompleted++;
                        }
                        prog += p.numberOfSteps + 8;
                        pRem = Math.max(1, 56 - p.numberOfSteps);
                        if (p.numberOfSteps > maxSteps) {
                            maxSteps = p.numberOfSteps;
                        }
                        if (!colIsFavored) {
                            anyOpponentAlive = true;
                        }
                    } else {
                        pRem = 62;
                    }
                    if (gametype == 3) {
                        if (pRem < remSteps) {
                            remSteps = pRem;
                        }
                    } else {
                        remSteps += pRem;
                    }
                }
                if (colIsFavored) {
                    if (comp > favoredCompleted) {
                        favoredCompleted = comp;
                    }
                    if (prog > favoredProgress) {
                        favoredProgress = prog;
                    }
                    if (remSteps > favoredRemainingSteps) {
                        favoredRemainingSteps = remSteps;
                    }
                } else {
                    if (comp > maxOpponentCompleted) {
                        maxOpponentCompleted = comp;
                    }
                    if (prog > maxOpponentProgress) {
                        maxOpponentProgress = prog;
                    }
                    if (remSteps < minOpponentRemainingSteps) {
                        minOpponentRemainingSteps = remSteps;
                    }
                    if ((gametype == 3 && maxSteps >= 38)
                            || (gametype != 3 && comp >= pantaValue - 1)
                            || (gametype != 3 && comp >= pantaValue - 2 && maxSteps >= 42)) {
                        opponentNearWin = true;
                    }
                }
            }
        }

        List<Piece> myPieces = getPiecesByColor(currentColor);
        int myCompleted = 0;
        int myProgress = 0;
        int aliveCount = 0;
        int baseCount = 0;
        int myRemainingSteps = (gametype == 3) ? 999 : 0;
        for (Piece p : myPieces) {
            int pRem;
            if (p.hasCompletedItsPurpose || p.numberOfSteps >= 56 || p.isThisPlayerWon) {
                myCompleted++;
                myProgress += 65;
                pRem = 0;
            } else if (p.isAlive) {
                aliveCount++;
                myProgress += p.numberOfSteps + 8;
                pRem = Math.max(1, 56 - p.numberOfSteps);
            } else {
                baseCount++;
                pRem = 62;
            }
            if (gametype == 3) {
                if (pRem < myRemainingSteps) {
                    myRemainingSteps = pRem;
                }
            } else {
                myRemainingSteps += pRem;
            }
        }

        int effectiveFavoredRem = (gametype == 2) ? favoredRemainingSteps : myRemainingSteps;
        boolean isEmergencyCatchUp = opponentNearWin
                || (gametype == 3 && minOpponentRemainingSteps <= 28 && myRemainingSteps > minOpponentRemainingSteps + 12)
                || (gametype != 3 && minOpponentRemainingSteps <= 115 && effectiveFavoredRem > minOpponentRemainingSteps + 38)
                || (gametype != 3 && maxOpponentCompleted >= 2 && myRemainingSteps > minOpponentRemainingSteps + 52);

        if (isFavored) {
            if (aliveCount == 0 && baseCount > 0) {
                boolean shouldOpenNow = (normalizedRoll == 6)
                        || opponentNearWin
                        || (d.favoredTurnsWithoutOpen >= 2)
                        || (d.favoredTurnsWithoutOpen >= 1 && (anyOpponentAlive || d.favoredNeedsReopenAfterCut) && Math.random() < 0.55);
                if (d.consecutiveSixCount == 0 && shouldOpenNow) {
                    d.favoredTurnsWithoutOpen = 0;
                    d.favoredNeedsReopenAfterCut = false;
                    return recordAndReturnRoll(6, true);
                }
                d.favoredTurnsWithoutOpen++;
                int fallback = (normalizedRoll == 6 && d.consecutiveSixCount >= 1) ? 4 : normalizedRoll;
                return recordAndReturnRoll(fallback, true);
            }
            d.favoredTurnsWithoutOpen = 0;

            // Normal early & mid-game play: use original repo random roll (with smooth home-entry completion
            // and reopen recovery after a natural cooldown so a cut token doesn't immediately roll 6 on the next turn).
            if (!isEmergencyCatchUp) {
                // 1. If a token was cut by the opponent and is waiting in base, wait at least 2 turns and ensure
                // we haven't rolled a 6 recently before softly assisting a 6 (prevents suspicious instant-6 after cut).
                if (d.favoredNeedsReopenAfterCut
                        && baseCount > 0
                        && d.consecutiveSixCount == 0
                        && d.turnsSinceFavoredCut >= 2
                        && d.turnsSinceLastFavoredSix >= 2
                        && Math.random() < 0.36) {
                    d.favoredNeedsReopenAfterCut = false;
                    return recordAndReturnRoll(6, true);
                }
                if (normalizedRoll == 6 && baseCount > 0) {
                    d.favoredNeedsReopenAfterCut = false;
                }

                // 2. If a token is inside the home stretch (steps 50..55) and the natural roll would overshoot 56
                // (or we are trailing the opponent), help it finish into step 56 smoothly without wasted turns.
                for (Piece p : myPieces) {
                    if (p.isAlive && !p.hasCompletedItsPurpose && p.numberOfSteps >= 50 && p.numberOfSteps < 56) {
                        int exactHomeRoll = 56 - p.numberOfSteps;
                        boolean wouldOvershoot = (p.numberOfSteps + normalizedRoll > 56);
                        boolean trailingOpponent = (effectiveFavoredRem > minOpponentRemainingSteps);
                        if (exactHomeRoll >= 1 && exactHomeRoll <= 6
                                && (exactHomeRoll != 6 || d.consecutiveSixCount == 0)
                                && ((wouldOvershoot && Math.random() < 0.72) || (trailingOpponent && Math.random() < 0.58))) {
                            return recordAndReturnRoll(exactHomeRoll, true);
                        }
                    }
                }

                // 3. Smooth pace keeper: if behind the leading opponent (or recovering from being cut) and rolled a 1 or 2,
                // pick a natural non-cutting forward stride (4, 5, or 3) so we recover steps smoothly without sniping or rolling 6s.
                int naturalRoll = normalizedRoll;
                boolean needsPaceRecovery = (effectiveFavoredRem > minOpponentRemainingSteps + 12)
                        || (d.favoredTimesCut > 0 && effectiveFavoredRem > minOpponentRemainingSteps - 6);
                if (needsPaceRecovery && naturalRoll <= 2 && Math.random() < 0.58) {
                    int[] strideCandidates = {5, 4, 3};
                    for (int sc : strideCandidates) {
                        if (sc == d.lastFavoredRoll) {
                            continue;
                        }
                        boolean validNonCuttingMove = false;
                        boolean cutsAnyEnemy = false;
                        for (Piece p : myPieces) {
                            if (p.isAlive && !p.hasCompletedItsPurpose && !p.isThisPlayerWon && p.numberOfSteps + sc < 57) {
                                validNonCuttingMove = true;
                                if (p.numberOfSteps + sc <= 50) {
                                    int tBlock = (p.startPosition + p.numberOfSteps + sc) % 52;
                                    if (getMaxUnprotectedEnemyStepsAtBlock(tBlock, currentColor, false) >= 0) {
                                        cutsAnyEnemy = true;
                                    }
                                }
                            }
                        }
                        if (validNonCuttingMove && !cutsAnyEnemy) {
                            naturalRoll = sc;
                            break;
                        }
                    }
                }

                if (naturalRoll == 6 && d.consecutiveSixCount >= 1) {
                    naturalRoll = 2 + (int) (Math.random() * 4);
                    if (naturalRoll == d.lastFavoredRoll) {
                        naturalRoll = (naturalRoll % 5) + 1;
                    }
                } else if (naturalRoll == d.lastFavoredRoll && naturalRoll == d.secondLastFavoredRoll) {
                    naturalRoll = (naturalRoll % 5) + 1; // 1..5 so we never force a 6
                }

                boolean naturalCanMove = false;
                for (Piece p : myPieces) {
                    boolean canMove = (naturalRoll == 6)
                            ? (!p.isAlive || (p.numberOfSteps + 6 < 57 && !p.hasCompletedItsPurpose && !p.isThisPlayerWon))
                            : (p.isAlive && (p.numberOfSteps + naturalRoll < 57) && !p.hasCompletedItsPurpose && !p.isThisPlayerWon);
                    if (canMove) {
                        naturalCanMove = true;
                        break;
                    }
                }
                if (naturalCanMove) {
                    return recordAndReturnRoll(naturalRoll, true);
                }
            }

            // Emergency late-game comeback mode: only active when opponent is near win or locked winner is far behind.
            // Focuses purely on fast forward travel (4, 5, 3) and home completion — NEVER artificial token sniping or multiple 6s.
            int[] candScores = new int[7];
            int bestCand = normalizedRoll;
            int bestScore = -100000;

            for (int cand = 1; cand <= 6; cand++) {
                if (cand == 6 && d.consecutiveSixCount >= 1) {
                    candScores[cand] = -50000;
                    continue;
                }

                int maxPieceScore = -10000;
                int movableCount = 0;
                int jitter = (int) (Math.random() * 28);

                for (Piece p : myPieces) {
                    boolean canMove = (cand == 6)
                            ? (!p.isAlive || (p.numberOfSteps + 6 < 57 && !p.hasCompletedItsPurpose && !p.isThisPlayerWon))
                            : (p.isAlive && (p.numberOfSteps + cand < 57) && !p.hasCompletedItsPurpose && !p.isThisPlayerWon);
                    if (!canMove) {
                        continue;
                    }
                    movableCount++;
                    int pScore = 0;

                    if (!p.isAlive && cand == 6) {
                        if (d.consecutiveSixCount == 0 && d.turnsSinceLastFavoredSix >= 2 && (aliveCount <= 1 || baseCount >= 2)) {
                            pScore = 165 + jitter;
                        } else {
                            pScore = 75 + jitter;
                        }
                    } else if (p.isAlive) {
                        int nextSteps = p.numberOfSteps + cand;
                        boolean currentlyInDanger = isThreatenedByEnemyAtBlock(p.currBlock, currentColor);

                        if (nextSteps == 56) {
                            pScore = 295 + jitter;
                        } else if (p.numberOfSteps <= 50 && nextSteps > 50) {
                            pScore = 200 + (currentlyInDanger ? 35 : 0) + jitter;
                        } else if (p.numberOfSteps > 50) {
                            pScore = 140 + cand * 4 + jitter;
                        } else {
                            int targetBlock = (p.startPosition + nextSteps) % 52;
                            boolean landsSafe = safeSpots.contains(targetBlock);
                            boolean landsInDanger = isThreatenedByEnemyAtBlock(targetBlock, currentColor);
                            boolean cutsEnemyOnNonNaturalRoll = (cand != normalizedRoll)
                                    && (getMaxUnprotectedEnemyStepsAtBlock(targetBlock, currentColor, false) >= 0);

                            if (cutsEnemyOnNonNaturalRoll) {
                                // Never pick a non-natural roll to cut an opponent token (eliminates sniper doubt)
                                pScore = -150 + jitter;
                            } else if (landsInDanger && p.numberOfSteps >= 36 && d.favoredTimesCut >= 2) {
                                pScore = -60 + cand;
                            } else {
                                // Prefer fast non-6 strides (4 and 5) over 6 so comeback looks completely organic
                                int strideBonus = (cand == 5) ? 44 : ((cand == 4) ? 38 : (cand * 6));
                                pScore = 105 + strideBonus + (landsSafe ? 18 : 0) + (cand == normalizedRoll ? 25 : 0) + jitter;
                            }
                        }
                    }

                    if (pScore > maxPieceScore) {
                        maxPieceScore = pScore;
                    }
                }

                if (movableCount == 0) {
                    if (baseCount > 0 && cand == 6 && d.consecutiveSixCount == 0) {
                        maxPieceScore = 195;
                    } else {
                        maxPieceScore = -5000;
                    }
                }

                if (cand == 6 && d.turnsSinceLastFavoredSix < 2 && movableCount > 0 && aliveCount > 0) {
                    maxPieceScore -= 85;
                }

                if (maxPieceScore < 260) {
                    if (cand == d.lastFavoredRoll && cand == d.secondLastFavoredRoll) {
                        maxPieceScore -= 160;
                    } else if (cand == d.lastFavoredRoll && cand != normalizedRoll) {
                        maxPieceScore -= 42;
                    }
                }

                candScores[cand] = maxPieceScore;
                if (maxPieceScore > bestScore) {
                    bestScore = maxPieceScore;
                    bestCand = cand;
                }
            }

            if (!opponentNearWin && candScores[normalizedRoll] >= 95 && Math.random() < 0.45) {
                return recordAndReturnRoll(normalizedRoll, true);
            }
            return recordAndReturnRoll(bestCand, true);
        } else {
            // Opponent turn: 100% authentic Ludo feel, actively allows & assists natural cuts on locked winner tokens
            // throughout early, mid, and late game, unrestricted home completion for non-final tokens, and zero loops.
            boolean opponentTeamReady = false;
            if (gametype == 2) {
                if (currentColor.equals("red") || currentColor.equals("yellow")) {
                    opponentTeamReady = isredyellowreadytowin;
                } else {
                    opponentTeamReady = isbluegreenreadytowin;
                }
            }

            int inHomeOrCompleted = 0;
            int maxOpponentPieceSteps = 0;
            for (Piece p : myPieces) {
                if (p.hasCompletedItsPurpose || p.numberOfSteps > 50 || p.isThisPlayerWon) {
                    inHomeOrCompleted++;
                }
                if (p.isAlive && !p.hasCompletedItsPurpose && p.numberOfSteps > maxOpponentPieceSteps) {
                    maxOpponentPieceSteps = p.numberOfSteps;
                }
            }

            boolean isPlayerOnFourthToken = (myCompleted >= pantaValue - 1)
                    || (inHomeOrCompleted >= pantaValue - 1 && baseCount == 0);

            // In Team Up mode (gametype == 2), allow the first opponent teammate to even complete all 4 tokens
            // if the favored team already has >= 5 tokens completed and the other opponent teammate is not ready;
            // otherwise gate each opponent player's 4th token so both opponent teammates stay active on the board
            // and can freely complete 3 tokens each (6 to 7 out of 8 team tokens!) without winning the match.
            boolean isFinalWinningTokenForWinGate = (gametype == 3)
                    || (gametype != 2 && isPlayerOnFourthToken)
                    || (gametype == 2 && isPlayerOnFourthToken && (opponentTeamReady || opponentTeamCompleted >= 5 || favoredTeamCompleted < 5));

            boolean isFinalWinningTokenForCutCheck = (gametype == 3 && maxOpponentPieceSteps >= 42)
                    || (gametype != 3 && gametype != 2 && myCompleted >= pantaValue - 1 && maxOpponentPieceSteps >= 42)
                    || (gametype == 2 && opponentTeamReady && myCompleted >= pantaValue - 1 && maxOpponentPieceSteps >= 42);

            // Keep the opponent's 4th token in base ONLY while they still have at least 1 active token moving on the track.
            // Once their active tokens finish into home (aliveCount == 0), allow opening the 4th token normally so they have 56 steps of normal moves.
            boolean holdFourthTokenInBase = (gametype != 3)
                    && (baseCount == 1)
                    && (aliveCount >= 1)
                    && (inHomeOrCompleted >= pantaValue - 1)
                    && (favoredRemainingSteps > 14);

            int[] candScores = new int[7];
            int bestCand = normalizedRoll;
            int bestScore = -1000000;
            int safeCutFavoredCand = -1;

            for (int cand = 1; cand <= 6; cand++) {
                int jitter = (int) (Math.random() * 28);
                int score = ((cand == normalizedRoll) ? 60 : 20) + jitter;

                if (cand == 6 && d.consecutiveSixCount >= 1) {
                    score -= 35000;
                } else if (cand == 6 && d.turnsSinceLastOpponentSix < 2 && aliveCount > 0) {
                    score -= 90;
                }

                // Prevent 3 identical opponent rolls in a row so dice never loops on the same number
                if (cand == d.lastOpponentRoll && cand == d.secondLastOpponentRoll) {
                    score -= 500;
                }

                int movableForCand = 0;
                boolean candCutsFavoredSafely = false;
                boolean candHardBlocked = false;
                int softHomeEntryPenalty = 0;

                for (Piece p : myPieces) {
                    boolean canMove = (cand == 6)
                            ? (!p.isAlive || (p.numberOfSteps + 6 < 57 && !p.hasCompletedItsPurpose && !p.isThisPlayerWon))
                            : (p.isAlive && (p.numberOfSteps + cand < 57) && !p.hasCompletedItsPurpose && !p.isThisPlayerWon);
                    if (!canMove) {
                        continue;
                    }
                    movableForCand++;

                    if (p.isAlive) {
                        int nextSteps = p.numberOfSteps + cand;
                        if (isFinalWinningTokenForWinGate) {
                            if (nextSteps == 56) {
                                // Strictly prevent opponent from completing the final match-winning token
                                score -= 300000;
                                candHardBlocked = true;
                            } else if (p.numberOfSteps <= 50 && nextSteps > 50 && favoredRemainingSteps > 6) {
                                // Softly prefer keeping the 4th token on the outer track when not making a safe cut;
                                // if already at step 50, prefer stepping to 51 over 55.
                                softHomeEntryPenalty += 45000 + nextSteps * 50;
                            } else if (p.numberOfSteps >= 38 && nextSteps <= 50 && favoredRemainingSteps > 12) {
                                // Smoothly pace the 4th token on the outer track so it doesn't rush into 50 early
                                score += (6 - cand) * 14;
                            }
                        }

                        if (nextSteps <= 50) {
                            int targetBlock = (p.startPosition + nextSteps) % 52;
                            int cutFavoredStatus = evaluateFavoredPieceAtBlockForOpponentCut(
                                    targetBlock,
                                    opponentNearWin,
                                    isFinalWinningTokenForCutCheck,
                                    favoredActiveAndCompleted,
                                    favoredRemainingSteps,
                                    minOpponentRemainingSteps
                            );
                            if (cutFavoredStatus == -1) {
                                // Only protect locked winner token in critical late-game crisis
                                score -= 85000;
                                candHardBlocked = true;
                            } else if (cutFavoredStatus == 1) {
                                candCutsFavoredSafely = true;
                                score += 260;
                            }
                        }
                    }
                }

                if (!candCutsFavoredSafely && softHomeEntryPenalty > 0) {
                    score -= softHomeEntryPenalty;
                }

                if (cand == 6 && d.consecutiveSixCount == 0 && !candCutsFavoredSafely && holdFourthTokenInBase) {
                    score -= 18000;
                }

                if (candCutsFavoredSafely && !candHardBlocked && (cand != 6 || d.consecutiveSixCount == 0)) {
                    if (safeCutFavoredCand == -1
                            || cand == normalizedRoll
                            || (safeCutFavoredCand == 6 && cand != 6)) {
                        safeCutFavoredCand = cand;
                    }
                }

                // Prefer rolls that give the opponent at least 1 valid move on the board (prevents zero-move loops),
                // except when the opponent's only active piece is inside the home stretch waiting on a final win gate.
                boolean waitingOnHomeStretchWinGate = isFinalWinningTokenForWinGate
                        && aliveCount == 1
                        && maxOpponentPieceSteps > 50;
                if (aliveCount > 0 && movableForCand > 0) {
                    score += 40;
                } else if (aliveCount > 0 && movableForCand == 0 && !waitingOnHomeStretchWinGate) {
                    score -= 80;
                }

                candScores[cand] = score;
                if (score > bestScore) {
                    bestScore = score;
                    bestCand = cand;
                }
            }

            // If an opponent token is 1..6 blocks behind a locked winner token that is safe to cut,
            // always allow the natural cut roll and provide a steady, natural cut assist throughout the entire game
            // (early, mid, and late game) so opponents never feel blocked from cutting locked winner tokens!
            if (safeCutFavoredCand != -1 && candScores[safeCutFavoredCand] >= 0) {
                double cutProb;
                if (d.opponentTurnsSinceCutFavored >= 5 || d.favoredTimesCut <= 1) {
                    cutProb = 0.56;
                } else if (d.favoredTimesCut <= ((gametype == 2) ? 5 : 3)) {
                    cutProb = 0.44;
                } else {
                    cutProb = 0.36;
                }
                if (normalizedRoll == safeCutFavoredCand || Math.random() < cutProb) {
                    return recordAndReturnRoll(safeCutFavoredCand, false);
                }
            }

            // Whenever the genuine random roll is valid and does not violate endgame winner protection,
            // return it directly so opponent dice behave 100% like original Ludo King.
            if (candScores[normalizedRoll] >= 0) {
                return recordAndReturnRoll(normalizedRoll, false);
            }
            return recordAndReturnRoll(bestCand, false);
        }
    }

    @Override
    public void onBackPressed() {
        if(quitgamelayout.getVisibility()==View.VISIBLE)
        { ingamenobtn.callOnClick(); } else
        { menuexitbtn.callOnClick(); }
    }
}