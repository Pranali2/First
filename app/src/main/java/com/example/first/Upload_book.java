package com.example.first;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Upload_book extends AppCompatActivity {

    private DatabaseReference reference;
    private FirebaseUser fuser;
    private StorageReference storageReference;
    private StorageTask uploadTask;
    private  static  final  int DEVICE_REQUEST=1;
    ImageView bookImage;
   private Uri uri;
    EditText txt_name,txt_description,txt_price;
    String imageUrl;


    @Override
     protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_upload_book);


    bookImage = (ImageView)findViewById(R.id.ivBookImage);
        txt_name = (EditText)findViewById(R.id.txt_book_name);
        txt_description = (EditText)findViewById(R.id.text_description);
        txt_price = (EditText)findViewById(R.id.text_price);

        storageReference= FirebaseStorage.getInstance().getReference("BookImage");
  reference = FirebaseDatabase.getInstance().getReference("BookImage");

        fuser= FirebaseAuth.getInstance().getCurrentUser();
       // reference= FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

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

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void uploadImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Book Uploding ....");
        progressDialog.show();

        if (uri != null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
            uploadTask = fileReference.putFile(uri);
            fileReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!uriTask.isComplete()) ;
                    Uri urlImage = uriTask.getResult();
                    imageUrl = urlImage.toString();
                    uploadBook();
                    progressDialog.dismiss();
                    Toast.makeText(Upload_book.this, "scuessful", Toast.LENGTH_SHORT).show();
                    BookData bookData=new BookData(txt_name.getText().toString().trim(),txt_description.getText().toString().trim(),txt_price.getText().toString().trim(),
                            taskSnapshot.getStorage().getDownloadUrl().toString());
                    String uploadId=reference.push().getKey();
                    reference.child(uploadId).setValue(bookData);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(Upload_book.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public void btnUploadBook(View view) {
        uploadImage();

    }
    public void uploadBook(){
        BookData bookData = new BookData(
                txt_name.getText().toString(),
                txt_description.getText().toString(),
                txt_price.getText().toString(),
                imageUrl

        );

        String myCurrentDateTime = DateFormat.getDateTimeInstance()
                .format(Calendar.getInstance().getTime());

        FirebaseDatabase.getInstance().getReference("BookImage")
                .child(myCurrentDateTime).setValue(bookData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(Upload_book.this, "Book Uploded", Toast.LENGTH_SHORT).show();

                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Upload_book.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

}
