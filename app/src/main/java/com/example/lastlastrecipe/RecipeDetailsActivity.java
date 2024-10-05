package com.example.lastlastrecipe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.lastlastrecipe.databinding.ActivityRecipeDetailsBinding;
<<<<<<< HEAD
import com.google.firebase.database.collection.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
=======
import com.example.lastlastrecipe.models.FavouriteRecipe;
import com.example.lastlastrecipe.models.Recipe;
import com.example.lastlastrecipe.room.RecipeRepository;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
>>>>>>> origin/muthuka


public class RecipeDetailsActivity extends AppCompatActivity {

    private VideoView recipeVideo;
    private ImageView playButton;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    
    ActivityRecipeDetailsBinding binding;

<<<<<<< HEAD
=======


    private void onFailure(Exception e) {
        // Handle any errors
        Toast.makeText(RecipeDetailsActivity.this, "Failed to load video", Toast.LENGTH_SHORT).show();
    }

>>>>>>> origin/muthuka
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();

        recipeVideo = findViewById(R.id.recipeVideo);
        playButton = findViewById(R.id.playButton);

        // Initialize Firebase Storage
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

      /*  StorageReference videoRef = storageReference.child("video/WhatsApp Video 2024-10-04 at 17.52.39_9ad78437.mp4");
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
        recipeVideo.start(); */
    }

    @SuppressLint("CheckResult")
    private void init() {
        Recipe recipe = (Recipe) getIntent().getSerializableExtra("recipe");
        binding.tvName.setText(recipe.getName());
        binding.tcCategory.setText(recipe.getCategory());
        binding.tvDescription.setText(recipe.getDescription());
        binding.tvCalories.setText(String.format("%s Calories", recipe.getCalories()));
<<<<<<< HEAD
        binding.imgShare.setOnClickListener(view -> shareRecipe(recipe));
=======
        recipeVideo = binding.recipeVideo;
        playButton = binding.playButton;
       /* recipeVideo = findViewById(R.id.recipeVideo);
        playButton = findViewById(R.id.playButton);*/

        // Initialize Firebase Storage
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

>>>>>>> origin/muthuka

        Glide
                .with(RecipeDetailsActivity.this)
                .load(recipe.getImage())
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher);
               // .into(binding.imgRecipe);

        String videoUrl = recipe.getVideoUrl();

        if (videoUrl != null && !videoUrl.isEmpty()) {
            StorageReference videoRef = storageReference.child("video/" + videoUrl);
            videoRef.getDownloadUrl().addOnSuccessListener(uri -> {
                // Set the video URI to the VideoView
                recipeVideo.setVideoURI(uri);

                // Set media controller for VideoView
                MediaController mediaController = new MediaController(this);
                recipeVideo.setMediaController(mediaController);
                mediaController.setAnchorView(recipeVideo);

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
        } else {
            Toast.makeText(this, "No video available for this recipe", Toast.LENGTH_SHORT).show();
        }

        /*if (videoUrl != null && !videoUrl.isEmpty()) {
            playVideo(videoUrl);
        } else {
            Toast.makeText(this, "No video available for this recipe", Toast.LENGTH_SHORT).show();
        }*/

        binding.imgEdit.setOnClickListener(view -> {
            Intent intent = new Intent(binding.getRoot().getContext(), AddRecipeActivity.class);
            intent.putExtra("recipe", recipe);
            intent.putExtra("isEdit", true);
            binding.getRoot().getContext().startActivity(intent);
        });

        checkFavorite(recipe);
        binding.imgFvrt.setOnClickListener(view -> favouriteRecipe(recipe));

        binding.btnDelete.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Recipe")
                    .setMessage("Are you sure you want to delete this recipe?")
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        ProgressDialog dialog = new ProgressDialog(this);
                        dialog.setMessage("Deleting...");
                        dialog.setCancelable(false);
                        dialog.show();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipes");
                        reference.child(recipe.getId()).removeValue().addOnCompleteListener(task -> {
                            dialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Recipe Deleted Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(this, "Failed to delete recipe", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss())
                    .show();
        });

        updateDataWithFireBase(recipe.getId());
      /*  if (videoUrl != null && !videoUrl.isEmpty()) {
            playVideo(videoUrl);
        } else {
            Toast.makeText(this, "No video available for this recipe", Toast.LENGTH_SHORT).show();
        }
        if (recipe.getAuthorId().equalsIgnoreCase(FirebaseAuth.getInstance().getUid())) {
            binding.imgEdit.setVisibility(View.VISIBLE);
            binding.btnDelete.setVisibility(View.VISIBLE);
        } else {
            binding.imgEdit.setVisibility(View.GONE);
            binding.btnDelete.setVisibility(View.GONE);
        }

        binding.imgEdit.setOnClickListener(view -> {
            Intent intent = new Intent(binding.getRoot().getContext(), AddRecipeActivity.class);
            intent.putExtra("recipe", recipe);
            intent.putExtra("isEdit", true);
            binding.getRoot().getContext().startActivity(intent);
        });
        checkFavorite(recipe);
        binding.imgFvrt.setOnClickListener(view -> favouriteRecipe(recipe));

        binding.btnDelete.setOnClickListener(view -> {
            new AlertDialog.Builder(this)
                    .setTitle("Delete Recipe")
                    .setMessage("Are you sure you want to delete this recipe?")
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        ProgressDialog dialog = new ProgressDialog(this);
                        dialog.setMessage("Deleting...");
                        dialog.setCancelable(false);
                        dialog.show();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipes");
                        reference.child(recipe.getId()).removeValue().addOnCompleteListener(task -> {
                            dialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Recipe Deleted Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(this, "Failed to delete recipe", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss())
                    .show();
        });

        updateDataWithFireBase(recipe.getId());*/
    }

    private void playVideo(String videoUrl) {
        StorageReference videoRef = storageReference.child("video/" + videoUrl);
        videoRef.getDownloadUrl().addOnSuccessListener(uri -> {
            // Set the video URI to the VideoView
            recipeVideo.setVideoURI(uri);

            // Set media controller for VideoView
            MediaController mediaController = new MediaController(this);
            recipeVideo.setMediaController(mediaController);
            mediaController.setAnchorView(recipeVideo);

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
    }
    private void checkFavorite(Recipe recipe) {
        RecipeRepository repository = new RecipeRepository(getApplication());
        boolean isFavourite = repository.isFavourite(recipe.getId());
        if (isFavourite) {
            binding.imgFvrt.setColorFilter(getResources().getColor(R.color.accent));
        } else {
            binding.imgFvrt.setColorFilter(getResources().getColor(R.color.black));
        }
    }


    private void favouriteRecipe(Recipe recipe) {
        RecipeRepository repository = new RecipeRepository(getApplication());
        boolean isFavourite = repository.isFavourite(recipe.getId());
        if (isFavourite) {
            repository.delete(new FavouriteRecipe(recipe.getId()));
            binding.imgFvrt.setColorFilter(getResources().getColor(R.color.black));
        } else {
            repository.insert(new FavouriteRecipe(recipe.getId()));
            binding.imgFvrt.setColorFilter(getResources().getColor(R.color.accent));
        }
    }

    private void updateDataWithFireBase(String id) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Recipes");
        reference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Recipe recipe = snapshot.getValue(Recipe.class);
                binding.tvName.setText(recipe.getName());
                binding.tcCategory.setText(recipe.getCategory());
                binding.tvDescription.setText(recipe.getDescription());
                binding.tvCalories.setText(String.format("%s Calories", recipe.getCalories()));
                Glide
                        .with(RecipeDetailsActivity.this)
                        .load(recipe.getImage())
                        .centerCrop()
                        .placeholder(R.mipmap.ic_launcher);
                       // .into(binding.imgRecipe);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "onCancelled: ", error.toException());
            }
        });

    }

    private void shareRecipe(Recipe recipe) {

        Glide.with(this)
                .asBitmap()
                .load(recipe.getImage()) // Load the image URL
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        try {

                            File cachePath = new File(getCacheDir(), "images");
                            cachePath.mkdirs();
                            File file = new File(cachePath, "image.png");
                            FileOutputStream stream = new FileOutputStream(file); 
                            resource.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            stream.close();


                            Uri imageUri = FileProvider.getUriForFile(
                                    RecipeDetailsActivity.this,
                                    getPackageName() + ".provider",
                                    file);


                            Intent shareIntent = new Intent(Intent.ACTION_SEND);
                            shareIntent.setType("image/*");
                            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                            shareIntent.putExtra(Intent.EXTRA_TEXT, recipe.getName() + "\n\n" + recipe.getDescription()); // Share the text
                            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            startActivity(Intent.createChooser(shareIntent, "Share Recipe Using"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }
}

