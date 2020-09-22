package com.ahmedesam.egyptyouth.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedesam.egyptyouth.Models.NewModel;
import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Shard.ShardPrefrances;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllNews extends RecyclerView.Adapter<AllNews.holder> {
    ArrayList<NewModel> mItems;
    Context mContext;
    DatabaseReference mDatabaseReference;
    static boolean check = false;
    ShardPrefrances mShardPrefrances;

    public AllNews(ArrayList<NewModel> mItems, Context mContext) {
        this.mItems = mItems;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_news_shap, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {
        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, Object> map2 = new HashMap<>();

        holder.mNewHeader.setText(mItems.get(position).getmHeader());
        Glide.with(mContext).load(mItems.get(position).getmImage()).into(holder.mNewImage);
        holder.mNew.setText(mItems.get(position).getmNew());
        holder.mLikeNumber.setText(mItems.get(position).getmLikeNumber() + "  Likes");
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mShardPrefrances = new ShardPrefrances(mContext);

        holder.mSparkButton.setChecked(check);

        Log.e("Checkkkk" , check +"");
        holder.mSparkButton.setEventListener(new SparkEventListener() {
            @Override
            public void onEvent(ImageView button, boolean buttonState) {
                if (buttonState) {
                    Log.e("Like", "Ok");
                    map.put("mLikeNumber", String.valueOf(Integer.parseInt(mItems.get(position).getmLikeNumber()) + 1));
                    mDatabaseReference.child("News").child("AllNews").child(mItems.get(position).getmPostID()).updateChildren(map);
                    map2.put(mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID), mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID));
                    mDatabaseReference.child("News").child("AllNews").child(mItems.get(position).getmPostID()).child("Users").updateChildren(map2);
                    check = true;
                }

                if (!buttonState) {
                    Log.e("Like", "Cancel");
                    map.put("mLikeNumber", String.valueOf(Integer.parseInt(mItems.get(position).getmLikeNumber()) - 1));
                    mDatabaseReference.child("News").child("AllNews").child(mItems.get(position).getmPostID()).updateChildren(map);
                    check = false;
                }

            }

            @Override
            public void onEventAnimationEnd(ImageView button, boolean buttonState) {
                if (!check) {
                    mDatabaseReference.child("News").child("AllNews").child(mItems.get(position).getmPostID()).child("Users").child(mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID)).removeValue();
                    check = false;
                }
            }

            @Override
            public void onEventAnimationStart(ImageView button, boolean buttonState) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class holder extends RecyclerView.ViewHolder {

        ImageView mNewImage;

        TextView mNewHeader;

        TextView mNew, mLikeNumber;
        SparkButton mSparkButton;

        public holder(@NonNull View itemView) {
            super(itemView);
            mNewHeader = itemView.findViewById(R.id.mNewHeader);
            mNewImage = itemView.findViewById(R.id.mNewImage);
            mNew = itemView.findViewById(R.id.mNew);
            mSparkButton = itemView.findViewById(R.id.like_button);
            mLikeNumber = itemView.findViewById(R.id.LikeNumber);
        }

    }
}
