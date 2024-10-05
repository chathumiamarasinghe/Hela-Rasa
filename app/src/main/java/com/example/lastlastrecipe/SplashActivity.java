package com.example.lastlastrecipe;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.lastlastrecipe.databinding.ActivitySplashBinding;


@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private final int splashScreenTime = 1000;
    private final int timeInterval = 100;
    private int progress = 0;
    private Runnable runnable;
    private Handler handler;

    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.progressBar.setMax(splashScreenTime);
        binding.progressBar.setProgress(progress);
        handler = new Handler(Looper.getMainLooper());
        runnable = () -> {

            if (progress < splashScreenTime) {
                progress += timeInterval;
                binding.progressBar.setProgress(progress);
                handler.postDelayed(runnable, timeInterval);
            } else {

                FirebaseApp.initializeApp(this);
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                startActivity(user != null ? new Intent(SplashActivity.this, MainActivity.class) : new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        };
        handler.postDelayed(runnable, timeInterval);
    }
}