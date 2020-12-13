package com.ahmedesam.egyptyouth.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedesam.egyptyouth.Models.NewModel;
import com.ahmedesam.egyptyouth.Models.userModel;
import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Ui.Activities.UserInfo;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ShowAllPlayers extends RecyclerView.Adapter<ShowAllPlayers.holder> {
    ArrayList<userModel> mItems;
    Context mContext;


    public ShowAllPlayers(ArrayList<userModel> mItems, Context mContext) {
        this.mItems = mItems;
        this.mContext = mContext;
    }

    public ShowAllPlayers() {

    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_player_bar, parent, false);
        holder mHolder = new holder(view);
        return mHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {


        Glide.with(mContext).load(mItems.get(position).getmImage()).into(holder.PlayerImage);
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


    class holder extends RecyclerView.ViewHolder {

        @BindView(R.id.PlayerImage)
        CircleImageView PlayerImage;

        public holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
