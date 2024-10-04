package com.example.lastlastrecipe;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.MediaController;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class RecipeNew extends AppCompatActivity {

    private VideoView recipeVideo;
    private ImageView playButton;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    private LinearLayout ingredientsLayout, procedureLayout;
    private Button btnIngredients, btnProcedure;

    private void onFailure(Exception e) {
        // Handle any errors
        Toast.makeText(RecipeNew.this, "Failed to load video", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_new);

        recipeVideo = findViewById(R.id.recipeVideo);
        playButton = findViewById(R.id.playButton);

        // Initialize Firebase Storage
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        // Fetch video URL from Firebase Storage
       /* StorageReference videoRef = storageReference.child("video/WhatsApp Video 2024-10-04 at 17.52.39_9ad78437.mp4");
        videoRef.getDownloadUrl().addOnSuccessListener(uri -> {
            // Set the video URI to the VideoView
            recipeVideo.setVideoURI(uri);

            // Play button functionality
            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!recipeVideo.isPlaying()) {
                        recipeVideo.start();
                        playButton.setVisibility(View.GONE); // Hide play button when video starts
                    }
                }
            });

        }).addOnFailureListener(this::onFailure);*/

        StorageReference videoRef = storageReference.child("video/WhatsApp Video 2024-10-04 at 17.52.39_9ad78437.mp4");
        videoRef.getDownloadUrl().addOnSuccessListener(uri -> {
            // Set the video URI to the VideoView
            recipeVideo.setVideoURI(uri);

            // Set media controller for VideoView
            MediaController mediaController = new MediaController(this);
            recipeVideo.setMediaController(mediaController);

            // Play button functionality
            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!recipeVideo.isPlaying()) {
                        recipeVideo.start();
                        playButton.setVisibility(View.GONE); // Hide play button when video starts
                    }
                }
            });
        }).addOnFailureListener(this::onFailure);
        recipeVideo.start();
        // Initialize views
        ingredientsLayout = findViewById(R.id.ingredientsLayout);
        procedureLayout = findViewById(R.id.procedureLayout);
        btnIngredients = findViewById(R.id.btnIngredients);
        btnProcedure = findViewById(R.id.btnProcedure);

        // Set OnClickListener for Ingredients button
        btnIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientsLayout.setVisibility(View.VISIBLE);
                procedureLayout.setVisibility(View.GONE);
                btnIngredients.setBackgroundTintList(getResources().getColorStateList(R.color.brown));
                btnProcedure.setBackgroundTintList(getResources().getColorStateList(R.color.light_brown));
            }
        });

        // Set OnClickListener for Procedure button
        btnProcedure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientsLayout.setVisibility(View.GONE);
                procedureLayout.setVisibility(View.VISIBLE);
                btnIngredients.setBackgroundTintList(getResources().getColorStateList(R.color.light_brown));
                btnProcedure.setBackgroundTintList(getResources().getColorStateList(R.color.brown));
            }
        });
    }
}