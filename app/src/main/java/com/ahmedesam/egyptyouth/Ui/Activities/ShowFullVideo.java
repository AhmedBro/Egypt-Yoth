package com.ahmedesam.egyptyouth.Ui.Activities;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.ahmedesam.egyptyouth.R;
import com.devbrackets.android.exomedia.ui.widget.VideoView;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.analytics.AnalyticsListener;


import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowFullVideo extends AppCompatActivity implements AnalyticsListener {
    static String mSource;
    @BindView(R.id.videoWebView)
    VideoView videoWebView;

    public ShowFullVideo() {
    }

    public ShowFullVideo(String Source) {
        this.mSource = Source;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_full_video);
        ButterKnife.bind(this);


        videoWebView.setVideoURI(Uri.parse(mSource));
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoWebView.pause();
    }
}