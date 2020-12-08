package com.ahmedesam.egyptyouth.Ui.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ahmedesam.egyptyouth.Models.ImageModel;
import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Zoom.TouchImageView;
import com.bumptech.glide.Glide;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class ShowFullImages extends AppCompatActivity {
    static String Url;

    public ShowFullImages(String url) {
        Url = url;
    }

    public ShowFullImages() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sh6w_full_images);
        TouchImageView mRoundedImageView = findViewById(R.id.iv_zoom);
        Glide.with(this).load(Url).into(mRoundedImageView);

    }
}