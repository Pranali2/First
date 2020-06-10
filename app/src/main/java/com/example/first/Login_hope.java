package com.example.first;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login_hope extends AppCompatActivity {
    private Button btn;
    private TextView newaccount;
    private TextView forgotpass;
  private   FirebaseAuth firebaseAuth;
    private EditText txtEmail;
    private EditText txtPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_hope);
        btn = findViewById(R.id.btn_Login);
        txtEmail = findViewById(R.id.et_email);
        progressBar= findViewById(R.id.progress_circular);
        firebaseAuth=FirebaseAuth.getInstance();
        txtPassword = findViewById(R.id.et_password);
        newaccount = findViewById(R.id.textView);
        forgotpass = findViewById(R.id.textView4);
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
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Login_hope.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Login_hope.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;

                }


                if (password.length() < 6) {
                    Toast.makeText(Login_hope.this, "Password is too short", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.VISIBLE);

                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login_hope.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                if (task.isSuccessful()) {

                                    progressBar.setVisibility(View.INVISIBLE);
                                    Intent i5 = new  Intent(Login_hope.this,toolbar.class);
                                    startActivity(i5);

                                } else {
                                    Toast.makeText(Login_hope.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                                }
                            }

                        });


            }
        });

    }
    private void sendUserToRegisterActivity() {
        Intent registerIntent = new Intent(Login_hope.this,Register1.class);
        startActivity(new Intent(getApplicationContext(),Register1.class));
        finish();
    }
    private void sendUserToForgotActivity() {
        Intent forgotIntent = new Intent(Login_hope.this,forgotpass.class);
        startActivity(new Intent(getApplicationContext(),forgotpass.class));
        finish();
    }
}
