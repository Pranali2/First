package com.example.first;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class contact extends AppCompatActivity {
    private TextView etTo;
    private EditText etMessage;
    private Button btSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        etTo = findViewById(R.id.edit1);
        etMessage = findViewById(R.id.edit2);
        btSend = findViewById(R.id.btn_Login);
etTo.setText("bookhubgcoen@gmail.com");
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW
                        , Uri.parse("mailto:" + etTo.getText().toString()));
                intent.putExtra(Intent.EXTRA_TEXT,etMessage.getText().toString());
                startActivity(intent);

            }
        });
    }
}
