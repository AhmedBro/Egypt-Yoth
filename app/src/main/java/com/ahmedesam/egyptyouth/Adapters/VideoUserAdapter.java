package com.ahmedesam.egyptyouth.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedesam.egyptyouth.Models.ImageModel;
import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Shard.ShardPrefrances;
import com.ahmedesam.egyptyouth.Ui.Activities.ShowFullVideo;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import needle.Needle;

public class VideoUserAdapter extends RecyclerView.Adapter<VideoUserAdapter.Video> implements AnalyticsListener {

    ArrayList<ImageModel> mItems;
    Context mContext;
    boolean Status = true;
    static String mFrom = null;
    ShardPrefrances mShardPrefrances;

    public VideoUserAdapter(boolean status) {
        Status = status;

    }

    public VideoUserAdapter() {

    }

    public VideoUserAdapter(ArrayList<ImageModel> mItems, Context mContext, String from) {
        this.mItems = mItems;
        this.mContext = mContext;
        mFrom = from;
        mShardPrefrances = new ShardPrefrances(mContext);
    }

    @NonNull
    @Override
    public Video onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Video(LayoutInflater.from(parent.getContext()).inflate(R.layout.video_shap, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Video holder, int position) {

        if (!Status) {
            holder.videoWeb.pause();
        }
        holder.videoWeb.setVideoURI(Uri.parse(mItems.get(position).getUrl()));


        holder.FullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowFullVideo mShowFullVideo = new ShowFullVideo(mItems.get(position).getUrl());
                Intent intent = new Intent(mContext, ShowFullVideo.class);
                mContext.startActivity(intent);

            }
        });

        holder.mChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//creating a popup menu
                PopupMenu popup = new PopupMenu(mContext, holder.mChoose);
                //inflating menu from xml resource
                if (mFrom.equals("MyInfo")) {
                    popup.inflate(R.menu.choose_share_delete);
                } else {
                    popup.inflate(R.menu.share);
                }
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.mDelete:
                                FirebaseFirestore.getInstance().collection("Users").document(Objects.requireNonNull(mShardPrefrances.getUserDetails().get(ShardPrefrances.KEY_ID))).collection("Videos").document(mItems.get(position).getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                return true;
                            case R.id.mShare:
                            Needle.onBackgroundThread().execute(new Runnable() {
                                    @Override
                                    public void run() {

                                        Intent intent = new Intent(Intent.ACTION_SEND);
                                        intent.putExtra(Intent.EXTRA_TEXT, mItems.get(position).getUrl());

                                        intent.setType("text/plain");
                                        mContext.startActivity(Intent.createChooser(intent, "مشاركة بواسطة .."));


                                    }
                                });
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public static class Video extends RecyclerView.ViewHolder {

        public VideoView videoWeb;


        @BindView(R.id.FullScreen)
        ImageView FullScreen;
        @BindView(R.id.mChoose)
        ImageView mChoose;

        @SuppressLint("SetJavaScriptEnabled")
        public Video(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            videoWeb = itemView.findViewById(R.id.videoWebView);
        }


    }

}

