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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgotpass extends AppCompatActivity {

    private EditText textView;
    private FirebaseAuth firebaseAuth;
    private Button reset;
    private ProgressBar progressBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);
        reset = findViewById(R.id.btn_reset);
        textView = findViewById(R.id.sendemail);
        progressBar = findViewById(R.id.progressBar);
        firebaseAuth=FirebaseAuth.getInstance();
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail=textView.getText().toString();
                if (TextUtils.isEmpty(userEmail)){
                    Toast.makeText(forgotpass.this, "Please Enter your valid Email Address", Toast.LENGTH_SHORT).show();
            }else
            {
                progressBar.setVisibility(View.VISIBLE);
                firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(forgotpass.this, "Check your Email Account,if you want to reset your password..", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(forgotpass.this,Login1.class));
                        }else{
                            String message=task.getException().getMessage();
                            Toast.makeText(forgotpass.this, "Error occured" + message , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            }
        });
    }
}
