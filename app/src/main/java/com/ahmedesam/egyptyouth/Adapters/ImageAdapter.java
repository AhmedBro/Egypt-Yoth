package com.ahmedesam.egyptyouth.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedesam.egyptyouth.Models.ImageModel;
import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Shard.ShardPrefrances;
import com.ahmedesam.egyptyouth.Ui.Activities.ShowFullImages;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.makeramen.roundedimageview.RoundedImageView;


import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.holder> {
    ArrayList<ImageModel> mItems;
    Context mContext;


    public ImageAdapter() {
    }

    public ImageAdapter(ArrayList<ImageModel> mItems, Context mContext) {
        this.mItems = mItems;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.images_shap, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        Glide.with(mContext).load(mItems.get(position).getUrl()).into(holder.mImage);
        Log.e("UserImage", mItems.get(position).getUrl());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowFullImages mShowFullImages = new ShowFullImages(mItems.get(position).getUrl() , mItems.get(position).getmUserId(),mItems.get(position).getId());
                Intent mIntent = new Intent(mContext , ShowFullImages.class);
                mContext.startActivity(mIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class holder extends RecyclerView.ViewHolder {
        RoundedImageView mImage;

        public holder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.mImage);
        }
    }
}
