package com.itismob.s02.group9.mco.dormmate;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpEmail extends AppCompatActivity {

    private EditText etEmailEntry, etPasswordEntry;
    private ImageButton imgbtnEmail;
    private CheckBox cbShowPassword;

    private TextView tvEmptyEmail, tvEmptyPassword, tvInvalidCredentials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.enableEdgeToEdge(getWindow());
        setContentView(R.layout.activity_sign_up_email);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        this.etEmailEntry = findViewById(R.id.etEmailEntry);
        this.etPasswordEntry = findViewById(R.id.etPasswordEntry);
        this.imgbtnEmail = findViewById(R.id.imgbtnEmail);
        this.cbShowPassword = findViewById(R.id.cbShowPassword);
        this.tvEmptyEmail = findViewById(R.id.tvEmptyEmail);
        this.tvEmptyPassword = findViewById(R.id.tvEmptyPassword);
        this.tvInvalidCredentials = findViewById(R.id.tvInvalidCredentials);

        cbShowPassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etPasswordEntry.setTransformationMethod(null);
            } else {
                etPasswordEntry.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        imgbtnEmail.setOnClickListener(v -> {
            String email = etEmailEntry.getText().toString().trim();
            String password = etPasswordEntry.getText().toString();


            boolean missingEmail = email.isEmpty();
            boolean missingPassword = password.isEmpty();



            tvEmptyPassword.setVisibility(missingPassword ? View.VISIBLE : View.GONE);

            if (missingEmail) {
                tvEmptyEmail.setText("Enter your email address");
                tvEmptyEmail.setVisibility(View.VISIBLE);
            }
            else {
                tvEmptyEmail.setVisibility(View.GONE);
            }

            if (missingEmail || missingPassword) {
                return;
            }

            imgbtnEmail.setEnabled(false);
            FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        imgbtnEmail.setEnabled(true);
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(SignUpEmail.this, SignUp3.class);
                            startActivity(intent);
                        }
                        else{
                            tvInvalidCredentials.setVisibility(View.VISIBLE);
                        }

                    });




        });

    }
}