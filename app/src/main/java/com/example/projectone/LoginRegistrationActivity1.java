package com.example.projectone;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginRegistrationActivity1 extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private CheckBox rememberMeCheckBox;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "MyPrefs";
    private static final String PREF_EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_registration1);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        rememberMeCheckBox = findViewById(R.id.rememberMeCheckBox);

        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        // Check if the email is already saved in shared preferences
        String savedEmail = sharedPreferences.getString(PREF_EMAIL, "");
        if (!savedEmail.isEmpty()) {
            emailEditText.setText(savedEmail);
            rememberMeCheckBox.setChecked(true);
        }

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered email and password
                String enteredEmail = emailEditText.getText().toString();
                String enteredPassword = passwordEditText.getText().toString();

                // Replace this with your actual login logic (check against database, etc.)
                boolean loginSuccess = performLogin(enteredEmail, enteredPassword);

                if (loginSuccess) {
                    // If login is successful, save the email in shared preferences if "Remember Me" is checked
                    if (rememberMeCheckBox.isChecked()) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(PREF_EMAIL, enteredEmail);
                        editor.apply();
                    }

                    // Navigate to the special menu or another activity
                    // Example: startActivity(new Intent(LoginRegistrationActivity1.this, SpecialMenuActivity.class));
                    Toast.makeText(LoginRegistrationActivity1.this, "Login successful!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginRegistrationActivity1.this, "Login failed. Check your credentials.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button signUpButton = findViewById(R.id.signUpButton);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the EmptyActivity when the "Sign Up" button is clicked
                startActivity(new Intent(LoginRegistrationActivity1.this, Farha.class));
            }
        });
    }

    // Replace this with your actual login logic
    private boolean performLogin(String email, String password) {
        return !email.isEmpty() && !password.isEmpty();
    }
}
