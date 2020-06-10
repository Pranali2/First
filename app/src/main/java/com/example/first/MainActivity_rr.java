package com.example.first;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity_rr extends AppCompatActivity {

    RecyclerView mRecyclerView;
    List<BookData> myBookList;
    BookData mBookData;
    private DatabaseReference databaseReference;
    public ValueEventListener eventListener;
    ProgressDialog progressDialog;
    MyAdapter myAdapter;
    EditText txt_Search;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_rr);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity_rr.this, 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        txt_Search = (EditText) findViewById(R.id.txt_searchtext);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Items.......");
        myBookList = new ArrayList<>();
        floatingActionButton = findViewById(R.id.btn_upload);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i2 = new Intent(MainActivity_rr.this, Upload_book.class);
                startActivity(i2);
            }
        });
        myAdapter = new MyAdapter(MainActivity_rr.this, myBookList);
        mRecyclerView.setAdapter(myAdapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("BookImage");

        progressDialog.show();

        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                myBookList.clear();

                for (DataSnapshot itemSnapshot : dataSnapshot.getChildren()) {

                    BookData bookData = itemSnapshot.getValue(BookData.class);
                    bookData.setKey(itemSnapshot.getKey());
                    myBookList.add(bookData);


                }

                myAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });


        txt_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());

            }
        });


    }

    private void filter(String text) {

        ArrayList<BookData> filterList = new ArrayList<>();

        for (BookData item : myBookList) {

            if (item.getItemName().toLowerCase().contains(text.toLowerCase())) {
                filterList.add(item);
            }

        }

        myAdapter.filteredList(filterList);

    }

    public void btn_uplodeactivity(View view) {

        startActivity(new Intent(this, Upload_book.class));

    }


}


