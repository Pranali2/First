package com.example.first;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class DetailActivity extends AppCompatActivity {

    TextView bookDescription ;
    ImageView bookImage;
    TextView bookTitle;
    TextView bookPrice;
    String key = "";
    String imageUrl ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        bookDescription = (TextView)findViewById(R.id.txtDescription);
        bookTitle =(TextView)findViewById(R.id.txtTitle);
        bookPrice = (TextView)findViewById(R.id.txtPrice);
        bookImage = (ImageView)findViewById((R.id.ivImage2));


        Bundle mBundle = getIntent().getExtras();

        if(mBundle != null){

            bookDescription.setText(mBundle.getString("Description"));
            bookTitle.setText(mBundle.getString("Name"));
            bookPrice.setText(mBundle.getString("Price"));
            key = mBundle.getString("keyValue");
            imageUrl =mBundle.getString("Image");

            //bookImage.setImageResource(mBundle.getInt("Image"));

            Glide.with(this)
                    .load(mBundle.getString("Image"))
                    .into(bookImage);
        }

    }

    public void btnDeleteBook(View view) {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Hope_Book");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);

        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                reference.child(key).removeValue();
                Toast.makeText(DetailActivity.this, "Book Deleted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        });

    }

    public void btnUpdateBook(View view) {
        startActivity(new Intent(getApplicationContext(),Update_book.class)
                .putExtra("bookNamekey",bookTitle.getText().toString())
                .putExtra("descriptionKey",bookDescription.getText().toString())
                .putExtra("priceKey",bookPrice.getText().toString())
                .putExtra("oldimageUrl",imageUrl)
                .putExtra("key",key)
        );
    }
}

