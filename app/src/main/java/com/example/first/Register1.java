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
import android.widget.ImageView;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register1 extends AppCompatActivity {
    private static final int GOOGLE_SIGN = 123;
    private EditText txtEmail;
    private EditText txtPassword;
    private EditText txtConfirmPassword;
    private EditText txtName;
    private Button btn_register;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private TextView text;
    ImageView image;
    private Button btn_google;
    private GoogleSignInClient mGoogleSignInClient;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);
        text = findViewById(R.id.textView3);
        txtEmail = findViewById(R.id.et_sendemail);
        txtPassword = findViewById(R.id.et_password);
        txtName = findViewById(R.id.et_name);
        txtConfirmPassword = findViewById(R.id.et_cmpassword);
        progressBar = findViewById(R.id.progressBar);
        btn_register = findViewById(R.id.btn_register);
        btn_google= findViewById(R.id.btn_goggle);
        firebaseAuth=FirebaseAuth.getInstance();
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendUserTologinrActivity();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString();
                String password = txtPassword.getText().toString();
                String uname = txtName.getText().toString();
                String confirmpassword = txtConfirmPassword.getText().toString();

                password.trim();
                confirmpassword.trim();
                email.trim();
                uname.trim();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Register1.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Register1.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;

                }

                if (TextUtils.isEmpty(confirmpassword)) {
                    Toast.makeText(Register1.this, "Enter Confirm Password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;

                }
                if (password.length() < 6) {
                    Toast.makeText(Register1.this, "Password is too short", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                }
                progressBar.setVisibility(View.VISIBLE);
                if (password.equals(confirmpassword)) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(Register1.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                    progressBar.setVisibility(View.INVISIBLE);
                                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

                                        String user = firebaseUser.getUid();
                                        reference= FirebaseDatabase.getInstance().getReference("Users").child(user);
                                        HashMap<String,String> hashMap=new HashMap<>();
                                        hashMap.put("id",user);
                                        hashMap.put("username", uname);
                                        hashMap.put("imageURL","default");
                                        hashMap.put("search",user.toLowerCase());
                                        hashMap.put("status","offline");
                                        hashMap.put("Uploadbook","default");
                                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Intent intent=new Intent(Register1.this,Login1.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                            }
                                        });
                                       sendEmailVerification();
                                    } else {
                                        Toast.makeText(Register1.this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });
                }
            }
        });
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,googleSignInOptions);
        btn_google.setOnClickListener(v -> SignInGoogle());


    }

    private void sendUserTologinrActivity() {
        Intent loginIntent = new Intent(Register1.this,Login_hope.class);
        startActivity(new Intent(getApplicationContext(),Login1.class));
        finish();
    }

    private void SignInGoogle() {
        progressBar.setVisibility(View.VISIBLE);
        Intent signIntent =mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent,GOOGLE_SIGN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GOOGLE_SIGN)
        {
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account=task.getResult(ApiException.class);
                if (account!=null) firebaseAuthWithGoogle(account);
            }catch (ApiException e){
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
                startActivity(new Intent(getApplicationContext(), Login1.class));
            } else {
                progressBar.setVisibility(View.INVISIBLE);
                Log.d("TAG", "signin failure");
                Toast.makeText(this, "SignIn Failed", Toast.LENGTH_SHORT).show();
            }

        });

    }
    private void  sendEmailVerification(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser!=null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(Register1.this, "Successfully Registerd..Please verify your Email Address", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(Register1.this,Login1.class));
                    }else
                    {
                        Toast.makeText(Register1.this, "Verification mail has'nt been  sent", Toast.LENGTH_SHORT).show();

                    }

                }
            });
        }
    }
    public void btn_al (View view){
        startActivity(new Intent(getApplicationContext(),Login1.class));
    }
    public void btn_register(View view){
        startActivity(new Intent(getApplicationContext(),toolbar.class));
    }
}
