package com.ahmedesam.egyptyouth.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedesam.egyptyouth.Models.ImageModel;
import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Ui.Activities.ShowFullVideo;
import com.devbrackets.android.exomedia.listener.OnCompletionListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.analytics.AnalyticsListener;


import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class VideoUserAdapter extends RecyclerView.Adapter<VideoUserAdapter.Video> implements AnalyticsListener {

    ArrayList<ImageModel> mItems;
    Context mContext;
    boolean Status = true;

    public VideoUserAdapter(boolean status) {
        Status = status;
    }

    public VideoUserAdapter() {
    }

    public VideoUserAdapter(ArrayList<ImageModel> mItems, Context mContext) {
        this.mItems = mItems;
        this.mContext = mContext;


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
        holder.videoWeb.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion() {
                holder.videoWeb.restart();
            }
        });

        holder.FullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowFullVideo mShowFullVideo = new ShowFullVideo(mItems.get(position).getUrl());
                Intent intent = new Intent(mContext, ShowFullVideo.class);
                mContext.startActivity(intent);

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


        @SuppressLint("SetJavaScriptEnabled")
        public Video(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            videoWeb = itemView.findViewById(R.id.videoWebView);
        }


    }


}

