package com.itismob.s02.group9.mco.dormmate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.GetCredentialCancellationException;
import androidx.credentials.exceptions.GetCredentialException;
import androidx.credentials.exceptions.NoCredentialException;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AdditionalUserInfo;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

public class LogIn extends AppCompatActivity {

    private static final String TAG = "LogIn";

    private FirebaseAuth mAuth;
    private CallbackManager callbackManager;

    private EditText loginEmailEtv, loginPasswordEtv;
    private TextView forgotPasswordTv, createAccTv;
    private Button loginBtn, loginGoogleBtn, loginFacebookBtn;
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

        mAuth = FirebaseAuth.getInstance();

        loginEmailEtv = findViewById(R.id.loginEmailEtv);
        loginEmailTil = findViewById(R.id.loginEmailTil);
        loginPasswordEtv = findViewById(R.id.loginPasswordEtv);
        loginPasswordTil = findViewById(R.id.loginPasswordTil);
        forgotPasswordTv = findViewById(R.id.forgotPasswordTv);
        createAccTv = findViewById(R.id.createAccTv);
        loginBtn = findViewById(R.id.loginBtn);
        loginGoogleBtn = findViewById(R.id.loginGoogleBtn);
        loginFacebookBtn = findViewById(R.id.loginFacebookBtn);

        loginBtn.setOnClickListener(v -> attemptEmailLogin());
        forgotPasswordTv.setOnClickListener(v -> showForgotPasswordDialog());
        loginGoogleBtn.setOnClickListener(v -> signInWithGoogle());

        setUpFacebookLogin();

        loginEmailEtv.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) loginEmailTil.setError(null);
        });
        loginPasswordEtv.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) loginPasswordTil.setError(null);
        });

        // TODO: once accounts have a role (student / dorm owner) from Firebase, route by role
        // after login instead of using this shortcut.
        findViewById(R.id.dormOwnerTv).setOnClickListener(v ->
                startActivity(new Intent(LogIn.this, LandlordDashboardActivity.class)));

        createAccTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogIn.this, SignUp.class));
            }
        });
    }

    // Email + password
    private void attemptEmailLogin() {
        String inputEmail = loginEmailEtv.getText().toString().trim();
        String inputPassword = loginPasswordEtv.getText().toString();

        loginEmailTil.setError(null);
        loginPasswordTil.setError(null);

        boolean isValid = true;

        if (inputEmail.isEmpty()) {
            loginEmailTil.setError("Email is required.");
            isValid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()) {
            loginEmailTil.setError("Please enter a valid email address.");
            isValid = false;
        }

        if (inputPassword.isEmpty()) {
            loginPasswordTil.setError("Password is required.");
            isValid = false;
        } else if (inputPassword.length() < 12) {
            loginPasswordTil.setError("Password must be at least 12 characters.");
            isValid = false;
        }

        if (!isValid) return;

        setLoading(true);
        mAuth.signInWithEmailAndPassword(inputEmail, inputPassword)
                .addOnCompleteListener(this, task -> {
                    setLoading(false);
                    if (task.isSuccessful()) {
                        goToPages();
                        return;
                    }
                    Exception e = task.getException();
                    if (e instanceof FirebaseAuthInvalidUserException
                            || e instanceof FirebaseAuthInvalidCredentialsException) {
                        loginPasswordTil.setError("Incorrect email or password.");
                    } else {
                        showAuthError(e);
                    }
                });
    }

    // Forgot password
    private void showForgotPasswordDialog() {
        View content = getLayoutInflater().inflate(R.layout.activity_log_in_forgot_password, null);
        TextInputLayout resetEmailTil = content.findViewById(R.id.resetEmailTil);
        TextInputEditText resetEmailEtv = content.findViewById(R.id.resetEmailEtv);

        resetEmailEtv.setText(loginEmailEtv.getText());

        AlertDialog dialog = new MaterialAlertDialogBuilder(this)
                .setTitle("Reset password")
                .setView(content)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Send link", null)
                .create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String email = resetEmailEtv.getText() == null
                    ? "" : resetEmailEtv.getText().toString().trim();

            if (email.isEmpty()) {
                resetEmailTil.setError("Email is required.");
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                resetEmailTil.setError("Please enter a valid email address.");
                return;
            }
            resetEmailTil.setError(null);

            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    dialog.dismiss();

                    Toast.makeText(this,
                            "If an account exists for that email, a reset link is on its way. Check your spam folder too.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Exception e = task.getException();
                    if (e instanceof FirebaseNetworkException) {
                        resetEmailTil.setError("No internet connection.");
                    } else {
                        Log.w(TAG, "sendPasswordResetEmail failed", e);
                        resetEmailTil.setError("Couldn't send the email. Please try again.");
                    }
                }
            });
        });
    }

    // Google
    private void signInWithGoogle() {
        setLoading(true);

        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setAutoSelectEnabled(false)
                .setServerClientId(getString(R.string.default_web_client_id))
                .build();

        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        CredentialManager credentialManager = CredentialManager.create(this);

        credentialManager.getCredentialAsync(
                this,
                request,
                null,
                ContextCompat.getMainExecutor(this),
                new CredentialManagerCallback<GetCredentialResponse, GetCredentialException>() {

                    @Override
                    public void onResult(GetCredentialResponse result) {
                        handleGoogleCredential(result.getCredential());
                    }

                    @Override
                    public void onError(GetCredentialException e) {
                        setLoading(false);
                        Log.w(TAG, "Google sign-in failed", e);

                        if (e instanceof GetCredentialCancellationException) {
                            return;
                        }

                        if (e instanceof NoCredentialException) {
                            toast("No Google account found. Please add a Google account to your device.");
                        } else {
                            toast("Google sign-in failed. Please try again.");
                        }
                    }
                }
        );
    }


    private void handleGoogleCredential(Credential credential) {
        if (credential instanceof CustomCredential
                && GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL.equals(credential.getType())) {
            GoogleIdTokenCredential googleCredential =
                    GoogleIdTokenCredential.createFrom(((CustomCredential) credential).getData());
            firebaseSignIn(GoogleAuthProvider.getCredential(googleCredential.getIdToken(), null));
        } else {
            setLoading(false);
            toast("Unexpected credential type.");
        }
    }

    // Facebook

    private void setUpFacebookLogin() {
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        firebaseSignIn(FacebookAuthProvider.getCredential(
                                loginResult.getAccessToken().getToken()));
                    }

                    @Override
                    public void onCancel() {
                        setLoading(false);
                    }

                    @Override
                    public void onError(FacebookException error) {
                        setLoading(false);
                        Log.w(TAG, "Facebook login failed", error);
                        toast("Facebook sign-in failed. Please try again.");
                    }
                });

        loginFacebookBtn.setOnClickListener(v -> {
            setLoading(true);
            LoginManager.getInstance().logInWithReadPermissions(
                    this, callbackManager, Arrays.asList("email", "public_profile"));
        });
    }

    // Exchanges a Google or Facebook credential for a Firebase user
    private void firebaseSignIn(AuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            setLoading(false);
            if (task.isSuccessful()) {
                AdditionalUserInfo info = task.getResult().getAdditionalUserInfo();
                boolean isNewUser = info != null && info.isNewUser();
                // TODO: if isNewUser, create their profile in the database
                // (name, photo, etc. from mAuth.getCurrentUser()) before or after going to Pages.
                goToPages();
            } else {
                showAuthError(task.getException());
            }
        });
    }

    private void showAuthError(Exception e) {
        Log.w(TAG, "Authentication failed", e);
        if (e instanceof FirebaseNetworkException) {
            toast("No internet connection.");
        } else if (e instanceof FirebaseAuthUserCollisionException) {
            toast("An account with this email already exists using a different sign-in method. Log in with that method instead.");
        } else {
            toast("Sign-in failed. Please try again.");
        }
    }

    private void goToPages() {
        Intent intent = new Intent(LogIn.this, Pages.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void setLoading(boolean loading) {
        loginBtn.setEnabled(!loading);
        loginGoogleBtn.setEnabled(!loading);
        loginFacebookBtn.setEnabled(!loading);
        forgotPasswordTv.setEnabled(!loading);
    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}