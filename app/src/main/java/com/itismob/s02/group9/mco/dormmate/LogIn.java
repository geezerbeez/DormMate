package com.itismob.s02.group9.mco.dormmate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

public class LogIn extends AppCompatActivity {
    private EditText loginEmailEtv, loginPasswordEtv;
    private TextView forgotPasswordTv, createAccTv;
    private Button loginBtn, loginGoogleBtn, loginFacebookBtn;;
    private TextInputLayout loginEmailTil, loginPasswordTil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginEmailEtv = findViewById(R.id.loginEmailEtv);
        loginEmailTil = findViewById(R.id.loginEmailTil);
        loginPasswordEtv = findViewById(R.id.loginPasswordEtv);
        loginPasswordTil = findViewById(R.id.loginPasswordTil);
        forgotPasswordTv = findViewById(R.id.forgotPasswordTv);
        createAccTv = findViewById(R.id.createAccTv);
        loginBtn = findViewById(R.id.loginBtn);
        loginGoogleBtn = findViewById(R.id.loginGoogleBtn);
        loginFacebookBtn = findViewById(R.id.loginFacebookBtn);

        loginBtn.setOnClickListener(v -> {

            // Email and password validation
            String inputEmail = loginEmailEtv.getText().toString();
            String inputPassword = loginPasswordEtv.getText().toString();

            loginEmailTil.setError(null);
            loginPasswordTil.setError(null);

            boolean isValid = true;

            // Validate email input
            if(inputEmail.isEmpty()) {
                loginEmailTil.setError("Email is required.");
                isValid = false;
            } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()) {
                loginEmailTil.setError("Please enter a valid email address.");
                isValid = false;
            }

            // Validate password input
            if(inputPassword.isEmpty()) {
                loginPasswordTil.setError("Password is required.");
                isValid = false;
            } else if(inputPassword.length() < 12) {
                loginPasswordTil.setError("Password must be at least 12 characters.");
                isValid = false;
            }

            if(!isValid) {
                return;
            }

            // TODO: Authenticate registered user from the database

            Intent intent = new Intent(LogIn.this, Pages.class);
            startActivity(intent);
            finish(); // so Back doesn't return to the login screen
        });

        loginEmailEtv.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                loginEmailTil.setError(null);
            }
        });

        loginPasswordEtv.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                loginPasswordTil.setError(null);
            }
        });

        // TODO: once accounts have a role (student / dorm owner) from Firebase, route by role
        // after login instead of using this shortcut.
        findViewById(R.id.dormOwnerTv).setOnClickListener(v ->
                startActivity(new Intent(LogIn.this, LandlordDashboardActivity.class)));

        createAccTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogIn.this, SignUp.class);
                startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(v -> {
            // TODO: validate email/password (call authentication) first
            Intent intent = new Intent(LogIn.this, Pages.class);
            startActivity(intent);
            finish(); // so Back doesn't return to the login screen
        });
    }
}