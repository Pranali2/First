package com.example.first;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.first.Adapter_Hope.MessageAdapter_Hope;
import com.example.first.Model_Hope.Chat_Hope;
import com.example.first.Model_Hope.User_Hope;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends AppCompatActivity {
    private CircleImageView profile_image;
    private TextView username;
    private FirebaseUser fuser;
    private DatabaseReference reference;

    private MessageAdapter_Hope messageAdapter_hope;
    private List<Chat_Hope> mChat;
    private ImageButton btn_send;
    private EditText txt_send;
    private RecyclerView recyclerView;

    private Intent intent;
    private ValueEventListener seenListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);


        profile_image=findViewById(R.id.profile_image);
        username=findViewById(R.id.username);
        btn_send=findViewById(R.id.btn_send);
        txt_send=findViewById(R.id.text_send);
recyclerView=findViewById(R.id.recycler_view);
recyclerView.setHasFixedSize(true);
LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplicationContext());
linearLayoutManager.setStackFromEnd(true);
recyclerView.setLayoutManager(linearLayoutManager);

        intent=getIntent();
        String userid=intent.getStringExtra("userid");

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String msg=txt_send.getText().toString();
                if (!msg.equals("")){
                sentMessage(fuser.getUid(),userid,msg);

            }else {
                Toast.makeText(MessageActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
            }
                txt_send.setText("");
        }
    });
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User_Hope user_hope=dataSnapshot.getValue(User_Hope.class);
                username.setText(user_hope.getUsername());
                if (user_hope.getImageurl().equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                }else {
                    Glide.with(getApplicationContext()).load(user_hope.getImageurl()).into(profile_image);
                }
                readMessage(fuser.getUid(),userid,user_hope.getImageurl());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        seenMessage(userid);
    }

    private void seenMessage(String userid){
        reference=FirebaseDatabase.getInstance().getReference("Chats");
        seenListner=reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Chat_Hope chat_hope=snapshot.getValue(Chat_Hope.class);
                    if (chat_hope.getReceiver().equals(fuser.getUid())&& chat_hope.getSender().equals(userid)){
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("isseen",true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void sentMessage(String sender,String receiver,String message){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashmap=new  HashMap<>();
        hashmap.put("sender",sender);
        hashmap.put("receiver",receiver);
        hashmap.put("message",message);
        reference.child("Chats").push().setValue(hashmap);
        hashmap.put("isseen",false);
    }
    private  void readMessage(final String myid, final String userid, final String imageurl){
        mChat=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChat.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Chat_Hope chat_hope=snapshot.getValue(Chat_Hope.class);
                    if (chat_hope.getReceiver().equals(myid) && chat_hope.getSender().equals(userid) ||
                            chat_hope.getReceiver().equals(userid) && chat_hope.getSender().equals(myid)){
                        mChat.add(chat_hope);
                    }
                    messageAdapter_hope= new  MessageAdapter_Hope(MessageActivity.this,mChat,imageurl);
                    recyclerView.setAdapter(messageAdapter_hope);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void status(String status){
        reference=FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("status",status);
        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        reference.removeEventListener(seenListner);
        status("offline");
    }
}

