package com.example.lastlastrecipe;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.example.lastlastrecipe.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RateUsDialog extends Dialog {

    private float userRate = 0;
    private String recipeId;

    public RateUsDialog(@NonNull Context context, String recipeId) {
        super(context);
        this.recipeId = recipeId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate_us_dialog_layout);

        final AppCompatButton rateNowBtn = findViewById(R.id.rateNowBtn);
        final AppCompatButton laterBtn = findViewById(R.id.laterBtn);
        final RatingBar ratingBar = findViewById(R.id.ratingBar);
        final ImageView ratingImage = findViewById(R.id.ratingImage);

        rateNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRating(userRate);
                dismiss();
            }
        });

        laterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });



        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating <= 1) {
                    ratingImage.setImageResource(R.drawable.emoji_2);
                } else if (rating <= 2) {
                    ratingImage.setImageResource(R.drawable.emoji_3);
                } else if (rating <= 3) {
                    ratingImage.setImageResource(R.drawable.emoji_5);
                } else if (rating <= 4) {
                    ratingImage.setImageResource(R.drawable.emoji_1);
                } else if (rating <= 5) {
                    ratingImage.setImageResource(R.drawable.emoji_4);
                }

                animateImage(ratingImage);

                userRate = rating;
            }
        });
    }

    private void animateImage(ImageView ratingImage) {
        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1f, 0, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setDuration(200);
        ratingImage.startAnimation(scaleAnimation);
    }

    private void submitRating(float rating) {

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RecipeRatings")
                .child(recipeId)
                .child(userId);

        reference.setValue(rating)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                    } else {

                    }
                });
    }
    private void calculateAverageRating(String recipeId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RecipeRatings").child(recipeId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0;
                float total = 0;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    float rating = snapshot.getValue(Float.class);
                    total += rating;
                    count++;
                }

                if (count > 0) {
                    float average = total / count;

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
