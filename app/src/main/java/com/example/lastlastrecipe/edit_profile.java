package com.example.lastlastrecipe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

public class edit_profile extends AppCompatActivity {

    private EditText editName, editEmail, editDescription, editPassword, editConfirmPassword;
    private Button btnUpdate;
    private EditText editUserName;
    // Firebase references
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize the EditText
        editUserName = findViewById(R.id.editTextText11);

        // Get the user name passed from the ProfileFragment
        String userName = getIntent().getStringExtra("USER_NAME");

        // Set the user name in the EditText
        if (userName != null) {
            editUserName.setText(userName);
        }

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference("Users");

        // Initialize UI elements
        editName = findViewById(R.id.editTextText11);
        editEmail = findViewById(R.id.editTextText2);
        editDescription = findViewById(R.id.Description);
        editPassword = findViewById(R.id.editTextNumberPassword);
        editConfirmPassword = findViewById(R.id.editTextNumberPassword2);
        btnUpdate = findViewById(R.id.button2);

        // Set button click listener to update profile
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
    }

    private void updateProfile() {
        String name = editName.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String confirmPassword = editConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(description)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(password)) {
            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Create a map to hold user profile data
        HashMap<String, Object> profileMap = new HashMap<>();
        profileMap.put("name", name);
        profileMap.put("email", email);
        profileMap.put("description", description);

        // Update user profile in Firebase
        userRef.child(currentUser.getUid()).updateChildren(profileMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(edit_profile.this, "Profile updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(edit_profile.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
            }
        });

        // Optionally update the user's password if it's changed
        if (!TextUtils.isEmpty(password)) {
            currentUser.updatePassword(password).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(edit_profile.this, "Password updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(edit_profile.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}