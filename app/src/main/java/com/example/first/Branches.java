package com.example.first;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

public class Branches extends AppCompatActivity {
    private Button but5;
    private Button but;
    private Button but2;
    private Button but4;
    private Button but6;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_branches);
        but = findViewById(R.id.button);
        but2 = findViewById(R.id.button2);
        but4 = findViewById(R.id.button4);
        but5 = findViewById(R.id.button5);
        but6 = findViewById(R.id.button6);
        toolbar=findViewById(R.id.toolbar);
        //setActionBar(toolbar);
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new  Intent(Branches.this,civil.class);
                startActivity(i);
            }
        });

        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new  Intent(Branches.this,mechanical.class);
                startActivity(i2);
            }
        });
        but4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i4 = new  Intent(Branches.this,computer.class);
                startActivity(i4);
            }
        });
        but5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i5 = new  Intent(Branches.this,electrical.class);
                startActivity(i5);
            }
        });

        but6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i6 = new  Intent(Branches.this,electronics.class);
                startActivity(i6);
            }
        });

    }
}

