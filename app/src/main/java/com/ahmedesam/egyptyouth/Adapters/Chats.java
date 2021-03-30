package com.ahmedesam.egyptyouth.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.ahmedesam.egyptyouth.Models.mChats;
import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Shard.ShardPrefrances;
import com.ahmedesam.egyptyouth.Ui.Activities.ChatActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chats extends RecyclerView.Adapter<Chats.adapter> {
    Context mContext;
    ArrayList<mChats> mUsersModels;
    ShardPrefrances mShardPrefrances;

    public Chats() {
    }

    public Chats(Context mContext, ArrayList<mChats> mUsersModels) {
        this.mContext = mContext;
        this.mUsersModels = mUsersModels;
        mShardPrefrances = new ShardPrefrances(mContext);
    }

    @NonNull
    @Override
    public adapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_shap, parent, false);
        adapter mAdapter = new adapter(view);
        return mAdapter;
    }

    @Override
    public void onBindViewHolder(@NonNull adapter holder, final int position) {
        holder.mName.setText(mUsersModels.get(position).getmName());
        holder.mMail.setText(mUsersModels.get(position).getmLastMessage());
        Glide.with(mContext).load(mUsersModels.get(position).getmImage()).into(holder.mUserImage);
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        try {
            cal.setTimeInMillis(Long.parseLong(mUsersModels.get(position).getmTime()));
            String Date = DateFormat.format(" hh:mm aa", cal).toString();
            holder.mTime.setText(Date);

        } catch (Exception e) {

        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, ChatActivity.class);
                mIntent.putExtra("Id", mUsersModels.get(position).getmId());
                mIntent.putExtra("Name", mUsersModels.get(position).getmName());
                mIntent.putExtra("Image", mUsersModels.get(position).getmImage());

                mContext.startActivity(mIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsersModels.size();
    }

    class adapter extends RecyclerView.ViewHolder {

        TextView mName, mMail, mTime;
        CircleImageView mUserImage;

        public adapter(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.mName);
            mMail = itemView.findViewById(R.id.mLasMessage);
            mTime = itemView.findViewById(R.id.mTime);
            mUserImage = itemView.findViewById(R.id.mUserImage);


            if (mShardPrefrances.IsDark()) {


                mName.setTextColor(mContext.getResources().getColor(R.color.black));
                mMail.setTextColor(mContext.getResources().getColor(R.color.black));
                mTime.setTextColor(mContext.getResources().getColor(R.color.black));
                mName.setTextColor(mContext.getResources().getColor(R.color.black));


            } else {

                mName.setTextColor(mContext.getResources().getColor(R.color.white));
                mMail.setTextColor(mContext.getResources().getColor(R.color.white));
                mTime.setTextColor(mContext.getResources().getColor(R.color.white));
                mName.setTextColor(mContext.getResources().getColor(R.color.white));


            }

        }
    }

}
