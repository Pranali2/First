package com.example.first;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Update_book extends AppCompatActivity {




    ImageView bookImage;
    Uri uri;
    EditText txt_name,txt_description,txt_price;
    String imageUrl;
    String key,oldImageUrl;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    String bookName,bookDescription,bookPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book);

        bookImage = (ImageView)findViewById(R.id.ivBookImage);
        txt_name = (EditText)findViewById(R.id.txt_book_name);
        txt_description = (EditText)findViewById(R.id.text_description);
        txt_price = (EditText)findViewById(R.id.text_price);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            Glide.with(Update_book.this)
                    .load(bundle.getString("oldimageUrl"))
                    .into(bookImage);
            txt_name.setText(bundle.getString("bookNamekey"));
            txt_description.setText(bundle.getString("descriptionKey"));
            txt_price.setText(bundle.getString("priceKey"));
            key = bundle.getString("key");
            oldImageUrl = bundle.getString("oldimageUrl");
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Book").child(key);

    }

    public void btnSelectImage(View view) {
        Intent photoPicker = new Intent (Intent.ACTION_PICK);
        photoPicker.setType("image/*");
        startActivityForResult(photoPicker,1);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){

            uri = data.getData();
            bookImage.setImageURI(uri);
        }
        else Toast.makeText(this, "You haven't Pick Image", Toast.LENGTH_SHORT).show();
    }

    public void btnUpdateBook(View view) {
        bookName = txt_name.getText().toString().trim();
        bookDescription = txt_description.getText().toString().trim();
        bookPrice = txt_price.getText().toString();



        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Book Uploding ....");
        progressDialog.show();

        storageReference = FirebaseStorage.getInstance().getReference().child("BookImage").child(uri.getLastPathSegment());

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isComplete());
                Uri urlImage = uriTask.getResult();
                imageUrl = urlImage.toString();
                uploadBook();
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
            }
        });





    }

    public void uploadBook(){



        BookData bookData = new BookData(
                bookName,
                bookDescription,
                bookPrice,
                imageUrl
        );

        databaseReference.setValue(bookData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                StorageReference storageReferenceNew = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageUrl);
                storageReferenceNew.delete();
                Toast.makeText(Update_book.this, "Data updated", Toast.LENGTH_SHORT).show();
            }
        });


    }

}
