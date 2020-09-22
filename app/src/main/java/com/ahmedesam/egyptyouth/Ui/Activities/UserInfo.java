package com.ahmedesam.egyptyouth.Ui.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedesam.egyptyouth.Adapters.ImageAdapter;
import com.ahmedesam.egyptyouth.Adapters.VideoUserAdapter;
import com.ahmedesam.egyptyouth.Models.ImageModel;
import com.ahmedesam.egyptyouth.Models.userModel;
import com.ahmedesam.egyptyouth.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfo extends AppCompatActivity {

    @BindView(R.id.UserImage)
    CircleImageView UserImage;
    @BindView(R.id.mUserName)
    TextView mUserName;
    @BindView(R.id.mUserAge)
    TextView mUserAge;
    @BindView(R.id.mUserAddress)
    TextView mUserAddress;
    @BindView(R.id.mUserSkills)
    TextView mUserSkills;
    @BindView(R.id.mEditInformation)
    Button mEditInformation;
    @BindView(R.id.Images)
    RecyclerView Images;
    @BindView(R.id.Videos)
    RecyclerView Videos;
    static String ID;
    ArrayList<ImageModel> mImages;
    ArrayList<ImageModel> mVideos;
    VideoUserAdapter mVideoUserAdapter;
    ImageAdapter mImageAdapter;
    private DatabaseReference mDatabase;
    userModel model;
    ImageModel Image;

    Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mIntent = getIntent();
        ID = mIntent.getStringExtra("ID");
        SetData();

        LoadUserImages();

        LoadUserVideos();
    }

    @OnClick(R.id.mEditInformation)
    public void onViewClicked() {
    }

    //----------------------------------------------------------------------------------------------
    private void SetData() {
        model = new userModel();
        mDatabase.child("Users").child(ID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                model = snapshot.getValue(userModel.class);
                mUserName.setText(model.getmName());
                mUserAge.setText(model.getmDate());
                mUserAddress.setText(model.getmAddress());
                mUserSkills.setText(model.getmDescription());
                try {
                    Glide.with(UserInfo.this).load(model.getmImage()).into(UserImage);
                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //----------------------------------------------------------------------------------------------
    private void LoadUserImages() {
        mDatabase.child("Users").child(ID).child("Images").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mImages = new ArrayList<>();
                Image = new ImageModel();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Image = data.getValue(ImageModel.class);
                    mImages.add(Image);

                }
                Log.e("ImageArray", mImages + "");
                RecyclerView.LayoutManager manager = new GridLayoutManager(UserInfo.this, 2);
                Images.setLayoutManager(manager);
                mImageAdapter = new ImageAdapter(mImages, UserInfo.this);
                Images.setAdapter(mImageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //----------------------------------------------------------------------------------------------
    private void LoadUserVideos() {
        mDatabase.child("Users").child(ID).child("Videos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mVideos = new ArrayList<>();
                Image = new ImageModel();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Image = data.getValue(ImageModel.class);
                    mVideos.add(Image);

                }
                Log.e("Videos", mVideos + "");
                RecyclerView.LayoutManager manager = new LinearLayoutManager(UserInfo.this, RecyclerView.VERTICAL, false);
                Videos.setLayoutManager(manager);
                mVideoUserAdapter = new VideoUserAdapter(mVideos, UserInfo.this);
                Videos.setAdapter(mVideoUserAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}