package com.example.lastlastrecipe;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.lastlastrecipe.databinding.ActivitySingUpBinding;
import com.example.lastlastrecipe.models.User;

import java.util.Objects;


public class SignUpActivity extends AppCompatActivity {
    ActivitySingUpBinding binding;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySingUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btnSignup.setOnClickListener(view -> signup());
        binding.tvLogin.setOnClickListener(view -> finish());
    }

    private void signup() {
        String name = Objects.requireNonNull(binding.etName.getText()).toString().trim();
        String email = Objects.requireNonNull(binding.etEmail.getText()).toString().trim();
        String password = Objects.requireNonNull(binding.etPassword.getText()).toString().trim();
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter your name, email and password", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
        } else {
            // let's create a new user in the Firebase
            createNewUser(name, email, password);
        }
    }

    private void createNewUser(String name, String email, String password) {
        // Currently, we are not using name field, but we will use it in the next video.
        // So, let's create a new user in the Firebase
        // We will use the Firebase Auth class to create a new user
        // We will use the createUserWithEmailAndPassword() method to create a new user
        // This method takes two parameters, email and password
 
        // Our code works as expected
        // Need to show a progress dialog while creating a new user

        dialog = new ProgressDialog(this);
        dialog.setMessage("Creating user...");
        dialog.setCancelable(false);
        dialog.show();

        FirebaseApp.initializeApp(this);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // user account created successfully
                        saveName(name, email);
                    } else {
                        // account creation failed
                        dialog.dismiss();
                        Toast.makeText(this, "Account creation failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveName(String name, String email) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        User user = new User(FirebaseAuth.getInstance().getUid(), name, email, "", "");
        reference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isComplete()) {
                    dialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "User created successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                    finishAffinity();
                } else {
                    dialog.dismiss();
                    Toast.makeText(SignUpActivity.this, "Failed to create user", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}