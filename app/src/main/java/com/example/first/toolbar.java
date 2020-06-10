package com.example.first;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.first.Model_Hope.User_Hope;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageClickListener;
import com.synnapps.carouselview.ImageListener;

import java.util.HashMap;

public class toolbar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private ImageView image_profile_nav;
    private TextView username;
    DatabaseReference reference;
    private FirebaseUser fuser;
    private StorageReference storageReference;
    private StorageTask uploadTask;
    private  static  final  int DEVICE_REQUEST=1;
    //private MyDrawerLayout mDrawerLayout;

    private Uri imageUri;

    private DrawerLayout drawerLayout;


    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private Button btn_notes;
    private Button btn_book;
    private Button btn_cal;
    private Button btn_reference;
    private int[] mImages = new int[]{
         R.drawable.comfort,R.drawable.education,R.drawable.finance
    };
    private String[] mImagesTitle = new String[]{
            "book", "education", " finance", "comfort"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        bottomNavigationView=findViewById(R.id.bottom_nav);
        btn_reference = findViewById(R.id.btn_reference);
        btn_book = findViewById(R.id.btn_book);
        btn_cal = findViewById(R.id.btn_cal);
        btn_notes = findViewById(R.id.btn_notes);
        bottomNavigationView.setSelectedItemId(R.id.home);
  //  mDrawerLayout = (MyDrawerLayout)findViewById(R.id.drawer);
        btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i5 = new  Intent(toolbar.this,Branches.class);
                startActivity(i5);
            }
        });
        btn_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i5 = new  Intent(toolbar.this,Branches.class);
                startActivity(i5);
            }
        });
        btn_reference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i5 = new  Intent(toolbar.this,Branches.class);
                startActivity(i5);
            }
        });
        btn_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i5 = new  Intent(toolbar.this,Branches.class);
                startActivity(i5);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.chat:
                        startActivity(new Intent(getApplicationContext(),Chatting_Hope.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        return true;

                    case R.id.sell:
                        startActivity(new Intent(getApplicationContext(),MainActivity_rr.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        CarouselView carouselView = findViewById(R.id.cars);

        carouselView.setImageListener(new ImageListener() {

            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(mImages[position]);

            }
        });
        carouselView.setImageClickListener(new ImageClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(toolbar.this, mImagesTitle[position], Toast.LENGTH_SHORT).show();
            }
        });

        carouselView.setPageCount(mImages.length);


    navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View headView =navigationView.getHeaderView(0);
        image_profile_nav= headView.findViewById(R.id.profile_image);
        username = headView.findViewById(R.id.username);
        storageReference= FirebaseStorage.getInstance().getReference("uploads");

         fuser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
      reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User_Hope user_hope=dataSnapshot.getValue(User_Hope.class);
                assert user_hope != null;
           username.setText(user_hope.getUsername());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       image_profile_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImage();
            }
        });


        carouselView.setImageListener(new ImageListener() {

            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setImageResource(mImages[position]);

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        switch (id){
            case R.id.nav_home:
                startActivity(new Intent(getApplicationContext(),toolbar.class));
                break;
            case R.id.nav_fav:
                startActivity(new Intent(getApplicationContext(),fav.class));
                break;
            case R.id.nav_wallet:
                startActivity(new Intent(getApplicationContext(),wallet.class));
                break;
            case R.id.nav_about:
                startActivity(new Intent(getApplicationContext(),about.class));
                break;
            case R.id.nav_contact:
                startActivity(new Intent(getApplicationContext(),contact.class));
                break;
            case R.id.nav_privacy:
                startActivity(new Intent(getApplicationContext(),privacy1.class));
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent= new Intent(toolbar.this,Login1.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }





    private void openImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,DEVICE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver=toolbar.this.getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImae(){
        final ProgressDialog pd =new ProgressDialog(toolbar.this);
        pd.setMessage("Uploading");
        pd.show();
        if (imageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()+"."+getFileExtension(imageUri));
            uploadTask=fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task <UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri=task.getResult();
                        assert downloadUri != null;
                        String mUri=downloadUri.toString();

                        reference=FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
                        HashMap<String, Object> map=new  HashMap<>();
                        map.put("imageURL",mUri);
                        reference.updateChildren(map);

                        pd.dismiss();
                    }else {
                        Toast.makeText(toolbar.this, "Failed", Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(toolbar.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        }else {
            Toast.makeText(toolbar.this, "No image selected", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DEVICE_REQUEST && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            imageUri=data.getData();

            if (uploadTask!=null&& uploadTask.isInProgress()){
                Toast.makeText(toolbar.this, "Upload in Progress", Toast.LENGTH_SHORT).show();
            }else {
                uploadImae();
            }
        }
    }
}