package com.ahmedesam.egyptyouth.Adapters;

import android.content.Context;
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
import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.holder> {
    ArrayList<ImageModel> mItems;
    Context mContext;
    ShardPrefrances mShardPrefrances;

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
                mShardPrefrances = new ShardPrefrances(mContext);
                Log.e("ID", mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID));
                Log.e("ID#", mItems.get(position).getId());
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID)).child("Images").child(mItems.get(position).getId());
                databaseReference.removeValue();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class holder extends RecyclerView.ViewHolder {
        ImageView mImage;

        public holder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.mImage);
        }
    }
}
