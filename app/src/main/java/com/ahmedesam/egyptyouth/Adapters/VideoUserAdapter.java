package com.ahmedesam.egyptyouth.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ProgressBar;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.ahmedesam.egyptyouth.Models.ImageModel;
import com.ahmedesam.egyptyouth.Zoom.OnActivityStateChanged;
import com.ahmedesam.egyptyouth.R;


import java.util.ArrayList;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;


public class VideoUserAdapter extends RecyclerView.Adapter<VideoUserAdapter.Video> {
    public OnActivityStateChanged onActivityStateChanged;
    ArrayList<ImageModel> mItems;
    Context mContext;

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
        Log.e("Video", mItems.get(position).getUrl());
        holder.videoWeb.setUp(mItems.get(position).getUrl(), JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL);

// Sets a callback to receive normal player events.

// Sets a callback that can be used to retrieve updates of the current playback position.


//        MediaController mediaController = new MediaController(mContext);
//        mediaController.setAnchorView(holder.videoWeb);
//        holder.videoWeb.setMediaController(mediaController);
//        holder.videoWeb.setVideoURI(Uri.parse(mItems.get(position).getUrl()));
//
//
//        holder.videoWeb.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mp) {
//                mp.start();
//            }
//        });
//
//        holder.videoWeb.setOnInfoListener(new MediaPlayer.OnInfoListener() {
//            @Override
//            public boolean onInfo(MediaPlayer mp, int what, int extra) {
//                switch (what) {
//                    case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
//                    case MediaPlayer.MEDIA_INFO_BUFFERING_END: {
//                        holder.mImageView.setVisibility(View.GONE);
//                        return true;
//                    }
//                    case MediaPlayer.MEDIA_INFO_BUFFERING_START: {
//                        holder.mImageView.setVisibility(View.VISIBLE);
//                        return true;
//                    }
//
//                }
//                return false;
//            }
//        });
//        holder.videoWeb.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                mp.start();
//            }
//        });

//        holder.videoWeb.setVideoURI(Uri.parse(mItems.get(position).getUrl()));
//        holder.videoWeb.setOnPreparedListener(new OnPreparedListener() {
//            @Override
//            public void onPrepared() {
//
//
//            }
//        });
        onActivityStateChanged = new OnActivityStateChanged() {
            @Override
            public void onResumed() {
               holder.videoWeb.releaseAllVideos();
            }

            @Override
            public void onPaused() {
                holder.videoWeb.releaseAllVideos();
            }
        };
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    class Video extends RecyclerView.ViewHolder {
        JCVideoPlayerStandard videoWeb;
        ProgressBar mImageView;

        @SuppressLint("SetJavaScriptEnabled")
        public Video(@NonNull View itemView) {
            super(itemView);
            videoWeb = itemView.findViewById(R.id.videoWebView);
//            mImageView = itemView.findViewById(R.id.imagePlay);

        }


    }


}

