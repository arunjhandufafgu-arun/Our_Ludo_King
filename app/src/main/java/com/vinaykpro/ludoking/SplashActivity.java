package com.vinaykpro.ludoking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class SplashActivity extends AppCompatActivity {
    private final Handler splashHandler = new Handler(Looper.getMainLooper());
    private ImageView splashImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setFullScreen();
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        splashImage = findViewById(R.id.splashImage);
        loadSplashDrawable(R.drawable.intro1);

        long firstDelay = (long) Math.floor(Math.random() * 1200) + 1200;
        long secondDelay = (long) Math.floor(Math.random() * 1500) + 1800;

        splashHandler.postDelayed(() -> {
            if (isFinishing() || isDestroyed()) {
                return;
            }
            loadSplashDrawable(R.drawable.intro2);
            splashHandler.postDelayed(() -> {
                if (isFinishing() || isDestroyed()) {
                    return;
                }
                clearSplashImage();
                Intent i = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(i);
                finish();
            }, secondDelay);
        }, firstDelay);
    }

    private void loadSplashDrawable(int drawableResId) {
        if (splashImage == null || isFinishing() || isDestroyed()) {
            return;
        }
        try {
            Glide.with(this)
                    .load(drawableResId)
                    .format(DecodeFormat.PREFER_RGB_565)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .centerCrop()
                    .into(splashImage);
        } catch (Throwable t) {
            try {
                splashImage.setImageResource(drawableResId);
            } catch (Throwable ignored) {
            }
        }
    }

    private void clearSplashImage() {
        if (splashImage != null) {
            try {
                Glide.with(this).clear(splashImage);
            } catch (Throwable ignored) {
            }
            splashImage.setImageDrawable(null);
        }
    }

    @Override
    protected void onDestroy() {
        splashHandler.removeCallbacksAndMessages(null);
        clearSplashImage();
        super.onDestroy();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            setFullScreen();
        }
    }

    void setFullScreen() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                WindowInsetsController insetsController = getWindow().getInsetsController();
                if (insetsController != null) {
                    insetsController.hide(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
                    insetsController.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
                }
            } else {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                View dview = getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                dview.setSystemUiVisibility(uiOptions);
            }
        } catch (Throwable ignored) {
        }
    }
}