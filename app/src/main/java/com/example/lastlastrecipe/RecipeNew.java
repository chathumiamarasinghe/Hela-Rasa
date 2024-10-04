package com.example.lastlastrecipe;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class RecipeNew extends AppCompatActivity {

    private LinearLayout ingredientsLayout, procedureLayout;
    private Button btnIngredients, btnProcedure;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        ingredientsLayout = findViewById(R.id.linearLayout);
        procedureLayout = findViewById(R.id.deltaRelative);
        btnIngredients = findViewById(R.id.btnIngredients);
        btnProcedure = findViewById(R.id.btnProcedure);

        // Set OnClickListener for Ingredients button
        btnIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientsLayout.setVisibility(View.VISIBLE);
                procedureLayout.setVisibility(View.GONE);
                btnIngredients.setBackgroundTintList(getResources().getColorStateList(R.color.black));
                btnProcedure.setBackgroundTintList(getResources().getColorStateList(R.color.white));
            }
        });

        // Set OnClickListener for Procedure button
        btnProcedure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientsLayout.setVisibility(View.GONE);
                procedureLayout.setVisibility(View.VISIBLE);
                btnIngredients.setBackgroundTintList(getResources().getColorStateList(R.color.white));
                btnProcedure.setBackgroundTintList(getResources().getColorStateList(R.color.black));
            }
        });
    }
}