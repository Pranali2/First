package com.example.first.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.first.Adapter_Hope.UserAdapter_Hope;
import com.example.first.Model_Hope.User_Hope;
import com.example.first.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class UsersFragment extends Fragment {
private RecyclerView recyclerView;
private UserAdapter_Hope userAdapter_hope;
private List<User_Hope> mUsers;
    private EditText search_usees;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_users, container, false);
        recyclerView=view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mUsers=new ArrayList<>();

        reasUsers();
        search_usees=view.findViewById(R.id.search_users);
        search_usees.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
               searchUsers(s.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return view;
    }

    private void searchUsers(String s) {
        FirebaseUser fuser=FirebaseAuth.getInstance().getCurrentUser();
        Query query=FirebaseDatabase.getInstance().getReference("Users").orderByChild("username").startAt(s).endAt(s+"\uf0ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User_Hope user_hope = snapshot.getValue(User_Hope.class);
                    assert user_hope != null;
                    assert fuser != null;
                    try {
                        if (!user_hope.getId().equals(fuser.getUid())) {
                            mUsers.add(user_hope);
                        }
                    } catch (Exception e) {
                    }
                    userAdapter_hope = new UserAdapter_Hope(getContext(), mUsers);
                    recyclerView.setAdapter(userAdapter_hope);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void reasUsers() {
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (search_usees.getText().toString().equals("")) {
                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User_Hope user_hope = snapshot.getValue(User_Hope.class);
                        if (user_hope == null) {
                            Log.i("Test USER", "NULL OBJECT");
                        } else {
                            Log.i("Test USER", user_hope.getUsername());
                            try {

                                if (!user_hope.getId().equals(firebaseUser.getUid())) {
                                    mUsers.add(user_hope);
                                }
                            } catch (Exception e) {
                            }
                        }
                        userAdapter_hope = new UserAdapter_Hope(getContext(), mUsers);
                        recyclerView.setAdapter(userAdapter_hope);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
