package com.example.lastlastrecipe.room;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.example.lastlastrecipe.R;

public class RateUsDialog extends Dialog {

    private float userRate = 0;

    public RateUsDialog(@NonNull Context context){

     super(context);

}

@Override

protected void onCreate(Bundle savedInstanceState){

super.onCreate(savedInstanceState);
setContentView(R.layout.rate_us_dialog_layout);

final AppCompatButton rateNowBtn = findViewById(R.id.rateNowBtn);
final AppCompatButton laterBtn = findViewById(R.id.laterBtn);
final RatingBar ratingBar = findViewById(R.id.ratingBar);
final ImageView ratingImage =findViewById(R.id.ratingImage);

rateNowBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        // hide rating dialog
        dismiss();
    }
});

laterBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

    }
});
ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

        if(rating <= 1){
          ratingImage.setImageResource(R.drawable.emoji_2);
        } else if(rating <= 2) {
            ratingImage.setImageResource(R.drawable.emoji_3);
        } else if (rating <= 3) {
            ratingImage.setImageResource(R.drawable.emoji_5);

        } else if (rating <= 4) {
           ratingImage.setImageResource(R.drawable.emoji_1);
        }else if (rating <= 5){
            ratingImage.setImageResource(R.drawable.emoji_4);
        }

        // animate emoji
        animateImage(ratingImage);

        // selected rating by user
        userRate = rating;
    }
});



}
  private void animateImage(ImageView ratingImage){
      ScaleAnimation scaleAnimation = new ScaleAnimation(0,1f,0,1f , Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);

      scaleAnimation.setFillAfter(true);
      scaleAnimation.setDuration(200);
      ratingImage.startAnimation(scaleAnimation);
  }
}