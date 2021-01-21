package com.ahmedesam.egyptyouth.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedesam.egyptyouth.Models.LikeModel;
import com.ahmedesam.egyptyouth.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class LikeAdapter extends RecyclerView.Adapter<LikeAdapter.Holder> {
    ArrayList<LikeModel> mItems;
    Context mContext;

    public LikeAdapter(ArrayList<LikeModel> mItems, Context mContext) {
        this.mItems = mItems;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.like_shap, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.mUserName.setText(mItems.get(position).getmUserName());
        Glide.with(mContext).load(mItems.get(position).getmUserImage()).into(holder.mUserImage);
        Log.e("Lik adapter", mItems.get(position).getmUserName());
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.mUserImage)
        CircleImageView mUserImage;
        @BindView(R.id.mUserName)
        TextView mUserName;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
