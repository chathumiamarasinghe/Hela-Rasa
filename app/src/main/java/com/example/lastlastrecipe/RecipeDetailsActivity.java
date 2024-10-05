package com.example.lastlastrecipe;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.database.collection.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.BreakIterator;


public class RecipeDetailsActivity extends AppCompatActivity {
    ActivityRecipeDetailsBinding binding;

    ImageView Rateresipe;
    TextView tv_average_rating;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        Rateresipe = findViewById(R.id.RateResipe);
        tv_average_rating = findViewById(R.id.tv_average_rating);



        Rateresipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Pass the recipe ID to the dialog
                Recipe recipe = (Recipe) getIntent().getSerializableExtra("recipe");
                RateUsDialog rateUsDialog = new RateUsDialog(RecipeDetailsActivity.this, recipe.getId());
                rateUsDialog.show();
            }
        });

        Recipe recipe = (Recipe) getIntent().getSerializableExtra("recipe");
        calculateAverageRating(recipe.getId());




    }

    private void calculateAverageRating(String recipeId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RecipeRatings").child(recipeId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0;
                float total = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Float rating = snapshot.getValue(Float.class);
                    if (rating != null) { // Check if rating is not null
                        total += rating;
                        count++;
                    }
                }


                if (count > 0) {
                    float average = total / count;
                    tv_average_rating.setText(String.format("Average Rating: %.1f", average)); // Update the TextView with the average rating
                } else {
                    tv_average_rating.setText("Average Rating: 0.0"); // No ratings yet
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors
            }
        });
    }


    private void init() {
        Recipe recipe = (Recipe) getIntent().getSerializableExtra("recipe");
        binding.tvName.setText(recipe.getName());
        binding.tcCategory.setText(recipe.getCategory());
        binding.tvDescription.setText(recipe.getDescription());
        binding.tvCalories.setText(String.format("%s Calories", recipe.getCalories()));
        binding.imgShare.setOnClickListener(view -> shareRecipe(recipe));

        Glide
                .with(RecipeDetailsActivity.this)
                .load(recipe.getImage())
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher)
                .into(binding.imgRecipe);

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

        updateDataWithFireBase(recipe.getId());
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
                        .placeholder(R.mipmap.ic_launcher)
                        .into(binding.imgRecipe);
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

