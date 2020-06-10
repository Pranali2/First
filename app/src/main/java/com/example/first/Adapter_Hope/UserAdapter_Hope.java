package com.example.first.Adapter_Hope;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.first.MessageActivity;
import com.example.first.Model_Hope.User_Hope;
import com.example.first.R;

import java.util.List;

public class UserAdapter_Hope extends RecyclerView.Adapter<UserAdapter_Hope.ViewHolder> {
    private Context mContext;
    private List<User_Hope> mUsers;

    public UserAdapter_Hope(Context mContext,List<User_Hope> mUsers){
        this.mUsers=mUsers;
        this.mContext=mContext;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.uers_item_hope,parent,false);

        return new UserAdapter_Hope.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
User_Hope user_hope=mUsers.get(position);
holder.username.setText(user_hope.getUsername());
if (user_hope.getImageurl().equals("default")){
    holder.profile_image.setImageResource(R.mipmap.ic_launcher);
       }else {
    Glide.with(mContext).load(user_hope.getImageurl()).into(holder.profile_image);
       }



holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent= new Intent(mContext, MessageActivity.class);
        intent.putExtra("userid",user_hope.getId());
        mContext.startActivity(intent);
    }
});
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        TextView username;
        ImageView profile_image;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            username=itemView.findViewById(R.id.username);

            profile_image=itemView.findViewById(R.id.profile_image);
        }
    }
}
