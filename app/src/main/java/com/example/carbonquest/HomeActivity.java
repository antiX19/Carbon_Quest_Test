package com.example.carbonquest;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private MediaPlayer backgroundMusic;
    private ToggleButton musicToggleButton;
    private boolean isMusicPlaying = true;
    private boolean areSettingsVisible = false;
    private Button playButton;
    private ImageButton settingsButton;
    private final Handler mainHandler = new Handler(Looper.getMainLooper()); // Handler for UI thread

    private static final int FADE_OUT_DURATION_MS = 1000; // Duration of fade-out in milliseconds
    private static final int FADE_STEP_DELAY_MS = 100; // Delay between each volume step in milliseconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize and start the background music
        initializeBackgroundMusic();

        // Initialize UI elements
        initializeUI();
    }

    private void initializeBackgroundMusic() {
        backgroundMusic = MediaPlayer.create(this, R.raw.soundtrack1);
        backgroundMusic.setLooping(true);
        startMusic();
    }

    private void initializeUI() {
        // Play Button
        playButton = findViewById(R.id.playButton);
        Animation blink = AnimationUtils.loadAnimation(this, R.anim.blink);
        playButton.startAnimation(blink);
        playButton.setOnClickListener(v -> fadeOutMusic(() -> {
            Intent intent = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }));
        Button exitButton = findViewById(R.id.btn_exit);
        exitButton.setOnClickListener(v -> exitApp());

        // Music Toggle Button
        musicToggleButton = findViewById(R.id.toggleMusicButton);
        musicToggleButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                startMusic();
            } else {
                stopMusic();
            }
        });

        // Settings Button
        settingsButton = findViewById(R.id.display_settings);
        settingsButton.setOnClickListener(v -> toggleSettingsVisibility());
    }

    public void exitApp() {
        finishAffinity();
    }
    private void startMusic() {
        if (backgroundMusic != null && !backgroundMusic.isPlaying()) {
            backgroundMusic.start();
            isMusicPlaying = true;
            if(musicToggleButton != null){
                musicToggleButton.setChecked(true);
            }
        }
    }

    private void stopMusic() {
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
            backgroundMusic.seekTo(0); // Reset to the beginning
            isMusicPlaying = false;
            if(musicToggleButton != null){
                musicToggleButton.setChecked(false);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMediaPlayer();
    }

    private void releaseMediaPlayer() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.release();
            backgroundMusic = null;
        }
    }

    private void toggleSettingsVisibility() {
        areSettingsVisible = !areSettingsVisible;
        float targetAlpha = areSettingsVisible ? 1f : 0f;
        musicToggleButton.animate().alpha(targetAlpha).setDuration(300);
        //playButton.animate().alpha(targetAlpha).setDuration(300);
        mainHandler.postDelayed(() -> {
            int newVisibility = areSettingsVisible ? View.VISIBLE : View.GONE;
            musicToggleButton.setVisibility(newVisibility);
        //    playButton.setVisibility(newVisibility);
        }, 300);
    }

    private void fadeOutMusic(Runnable onComplete) {
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            final float volumeDecrement = 1.0f / (FADE_OUT_DURATION_MS / FADE_STEP_DELAY_MS);
            new Thread(() -> {
                for (int i = 0; i <= FADE_OUT_DURATION_MS / FADE_STEP_DELAY_MS; i++) {
                    float volume = Math.max(0, 1.0f - (volumeDecrement * i));
                    backgroundMusic.setVolume(volume, volume);
                    try {
                        Thread.sleep(FADE_STEP_DELAY_MS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt(); // Restore the interrupted status
                        e.printStackTrace();
                        return; // Exit the thread if interrupted
                    }
                }
                mainHandler.post(() -> {
                    stopMusic();
                    if (onComplete != null) {
                        onComplete.run();
                    }
                });
            }).start();
        } else if (onComplete != null) {
            onComplete.run();
        }
    }
}