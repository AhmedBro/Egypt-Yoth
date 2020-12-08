package com.ahmedesam.egyptyouth.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedesam.egyptyouth.Models.userModel;
import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Ui.Activities.UserInfo;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class PlayersAdapter extends RecyclerView.Adapter<PlayersAdapter.Player> {
    ArrayList<userModel> mItems;
    Context mContext;

    public PlayersAdapter(ArrayList<userModel> mItems, Context mContext) {
        this.mItems = mItems;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Player onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Player(LayoutInflater.from(parent.getContext()).inflate(R.layout.player_shap, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Player holder, int position) {
        Glide.with(mContext).load(mItems.get(position).getmImage()).into(holder.UserImage);
        holder.mUserDescription.setText(mItems.get(position).getmDescription());
        Log.e("Des" ,mItems.get(position).getmDescription() );
        holder.UserName.setText(mItems.get(position).getmName());
        holder.mLikeNumber.setText(mItems.get(position).getmLikeNumber()+" "+" Likes");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, UserInfo.class);
                mIntent.putExtra("ID", mItems.get(position).getmId());
                mContext.startActivity(mIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class Player extends RecyclerView.ViewHolder {
        @BindView(R.id.UserImage)
        CircleImageView UserImage;
        @BindView(R.id.UserName)
        TextView UserName;
        @BindView(R.id.linearLayout)
        RelativeLayout linearLayout;
        @BindView(R.id.mUserDescription)
        TextView mUserDescription;
        @BindView(R.id.cLikeNumber)
        TextView mLikeNumber;
        public Player(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
