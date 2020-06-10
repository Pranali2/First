package com.example.first.Adapter_Hope;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.first.Model_Hope.Chat_Hope;
import com.example.first.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;



public class MessageAdapter_Hope extends RecyclerView.Adapter<MessageAdapter_Hope.ViewHolder> {
    private Context mContext;
    public  static  final int MSG_TYPE_LEFT=0;
    private static final int MSG_TYPE_RIGHT = 1;

    private List<Chat_Hope> mChat;
    private String imageurl;
     private FirebaseUser fuser;

    public MessageAdapter_Hope(Context mContext, List<Chat_Hope> mChat, String imageurl){
        this.mChat=mChat;
        this.imageurl=imageurl;
        this.mContext=mContext;
    }

    @NonNull
    @Override
    public MessageAdapter_Hope.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);

            return new MessageAdapter_Hope.ViewHolder(view);
        }else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);

            return new MessageAdapter_Hope.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter_Hope.ViewHolder holder, int position) {

        Chat_Hope chat_hope= mChat.get(position);
        holder.show_message.setText(chat_hope.getMessage());
        if (imageurl.equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }else {
            Glide.with(mContext).load(imageurl).into(holder.profile_image);
        }

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        TextView show_message;
        ImageView profile_image;
        TextView txt_seen;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

           show_message=itemView.findViewById(R.id.show_message);
            profile_image=itemView.findViewById(R.id.profile_image);
            txt_seen=itemView.findViewById(R.id.text_seen);
        }
    }
    @Override
    public int getItemViewType(int position) {
        fuser= FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }else {
            return MSG_TYPE_LEFT;
        }
    }
}
