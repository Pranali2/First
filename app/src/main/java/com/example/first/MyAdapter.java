package com.example.first;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<BookViewHolder>{

    private Context mContext;
    private List<BookData> myBookList;
    private  int lastPosition = -1;

    public MyAdapter(Context mContext, List<BookData> myBookList) {
        this.mContext = mContext;
        this.myBookList = myBookList;
    }

    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_row_item,viewGroup , false);

        return new BookViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final BookViewHolder bookViewHolder, final int i) {

        //Glide.with(mContext)
          //      .load(myBookList.get(i).getItemImage())
            //    .into(bookViewHolder.imageView);


        //Glide.with(mContext).load(myBookList.get(i).getItemImage()).into(bookViewHolder.imageView);
        Picasso.get().load(myBookList.get(i).getItemImage()).into(bookViewHolder.imageView);


         //bookViewHolder.imageView.setImageResource(myBookList.get(i).getItemImage());
        //bookViewHolder.imageView.setImageBitmap(myBookList.get(i).getItemImage());
        bookViewHolder.mTitle.setText(myBookList.get(i).getItemName());
        bookViewHolder.mDescription.setText(myBookList.get(i).getItemDescription());
        bookViewHolder.mPrice.setText(myBookList.get(i).getItemPrice());

        bookViewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext,DetailActivity.class);
                intent.putExtra("Image",myBookList.get(bookViewHolder.getAdapterPosition()).getItemImage());
                intent.putExtra("Description",myBookList.get(bookViewHolder.getAdapterPosition()).getItemDescription());
                intent.putExtra("Price",myBookList.get(bookViewHolder.getAdapterPosition()).getItemPrice());
                intent.putExtra("Name",myBookList.get(bookViewHolder.getAdapterPosition()).getItemName());
                intent.putExtra("keyValue",myBookList.get(bookViewHolder.getAdapterPosition()).getKey());
                mContext.startActivity(intent);
            }
        });

        setAnimation(bookViewHolder.itemView,i);
    }

    public void setAnimation(View viewToAnimate, int position){

            if(position > lastPosition){

                ScaleAnimation animation = new ScaleAnimation(0.0f,1.0f,0.0f,1.0f,
                        Animation.RELATIVE_TO_SELF,0.5f,
                        Animation.RELATIVE_TO_SELF,0.5f);
                animation.setDuration(1500);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;

            }

    }
    @Override
    public int getItemCount() {
        return myBookList.size();
    }

    public void filteredList(ArrayList<BookData> filterList) {

        myBookList = filterList;
        notifyDataSetChanged();
    }
}

class BookViewHolder extends RecyclerView.ViewHolder{

    ImageView imageView;
    TextView  mTitle,mDescription,mPrice;
    CardView mCardView;

    public BookViewHolder(View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.ivImage);
        mTitle = itemView.findViewById(R.id.tvTitle);
        mDescription = itemView.findViewById(R.id.tvDescription);
        mPrice = itemView.findViewById(R.id.tvPrice);

        mCardView = itemView.findViewById(R.id.myCardView);

    }
}