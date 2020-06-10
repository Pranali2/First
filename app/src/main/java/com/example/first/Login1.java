package com.example.first;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login1 extends AppCompatActivity {
    private static final int GOOGLE_SIGN = 123;
    private EditText txtEmail;
    private EditText txtPassword;
    private Button btn_login;
    private Button btn_google;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private FirebaseUser firebaseUser;
    private TextView newaccount, forgotpass;
    private GoogleSignInClient mGoogleSignInClient;

    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            Intent intent = new Intent(Login1.this, toolbar.class);
            startActivity(intent);
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login1);
        newaccount = findViewById(R.id.textView);
        forgotpass = findViewById(R.id.textView4);
        txtEmail = findViewById(R.id.et_email);
        txtPassword = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_Login);
        btn_google = findViewById(R.id.btn_goggle);
        progressBar = findViewById(R.id.progress_circular);
        firebaseAuth = FirebaseAuth.getInstance();
        newaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToRegisterActivity();
            }
        });
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserToForgotActivity();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();


                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Login1.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Login1.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;

                }


                if (password.length() < 6) {
                    Toast.makeText(Login1.this, "Password is too short", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login1.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {

                                    progressBar.setVisibility(View.INVISIBLE);
                                    Log.i("Test", "Success");
                                    checkEmailVerfication();
                                } else {
                                    Toast.makeText(Login1.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                                }
                            }

                        });

            }
        });
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        btn_google.setOnClickListener(v -> SignInGoogle());
        if (firebaseAuth.getCurrentUser() != null) {
            FirebaseUser user = firebaseAuth.getCurrentUser();

        }
    }

    private void sendUserToForgotActivity() {
        Intent forgotIntent = new Intent(Login1.this, forgotpass.class);
        startActivity(new Intent(getApplicationContext(), forgotpass.class));
        finish();
    }

    private void sendUserToRegisterActivity() {
        Intent registerIntent = new Intent(Login1.this, Register1.class);
        startActivity(new Intent(getApplicationContext(), Register1.class));
        finish();
    }

    private void SignInGoogle() {
        progressBar.setVisibility(View.VISIBLE);
        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent, GOOGLE_SIGN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("TAG", "firebaseAuthWithGoogle: " + account.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                progressBar.setVisibility(View.INVISIBLE);
                Log.d("TAG", "signin success");
                FirebaseUser user = firebaseAuth.getCurrentUser();
                startActivity(new Intent(getApplicationContext(), toolbar.class));
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                Log.d("TAG", "signin failure");
                Toast.makeText(this, "SignIn Failed", Toast.LENGTH_SHORT).show();
            }

        });


    }

    private void checkEmailVerfication() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        Boolean emailflag = firebaseUser.isEmailVerified();
        if (emailflag) {
            Intent intent = new Intent(Login1.this, toolbar.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

        } else {
            Toast.makeText(this, "Verify your Email", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }

    public void btn_nu(View view) {
        startActivity(new Intent(getApplicationContext(), Register1.class));

    }
}




