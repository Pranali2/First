package com.example.first;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;

public class firstpage extends AppCompatActivity {
    private static int SPLASH_TIME_OUT=100;
    private boolean InternetCheck =true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firstpage);
        PostDelayedMethod();

    }

    private void PostDelayedMethod()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean InternetResult = checkConnection();
                if(InternetResult) {
                    Intent intent = new Intent(firstpage.this, Login1.class);
                    startActivity(intent);
                    finish();
                }

                else{
                    DialogAppear();
                }
            }
        }, SPLASH_TIME_OUT);
    }

    private void DialogAppear()
    {
        AlertDialog.Builder builder = new   AlertDialog.Builder(firstpage.this);
        builder.setTitle("Network Error");
        builder.setMessage("No Internet Connectivity");
        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PostDelayedMethod();
            }
        });
        builder.show();
    }

    private boolean isOnline()
    {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();



    }

    private boolean checkConnection()
    {
        if(isOnline())
        {
            return InternetCheck;
        }
        else {
            InternetCheck = false;
            return  InternetCheck;
        }
    }

}
