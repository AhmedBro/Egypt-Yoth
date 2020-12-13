package com.ahmedesam.egyptyouth.Ui.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedesam.egyptyouth.Adapters.ImageAdapter;
import com.ahmedesam.egyptyouth.Adapters.VideoUserAdapter;
import com.ahmedesam.egyptyouth.Models.ImageModel;
import com.ahmedesam.egyptyouth.Models.userModel;
import com.ahmedesam.egyptyouth.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

    @BindView(R.id.Images)
    RecyclerView Images;
    @BindView(R.id.Videos)
    RecyclerView Videos;
    static String ID;
    ArrayList<ImageModel> mImages;
    ArrayList<ImageModel> mVideos;
    VideoUserAdapter mVideoUserAdapter;
    ImageAdapter mImageAdapter;
    @BindView(R.id.mLike)
    Button mLike;
    @BindView(R.id.mDisLike)
    Button mDisLike;
    @BindView(R.id.mUserId)
    TextView mUserId;
    private FirebaseFirestore mDatabase;
    userModel model;
    ImageModel Image;

    Intent mIntent;
    static int LikeNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        mDatabase = FirebaseFirestore.getInstance();
        mIntent = getIntent();
        ID = mIntent.getStringExtra("ID");
        mImages = new ArrayList<>();
        mVideos = new ArrayList<>();
        SetData();

        LoadUserImages();

        LoadUserVideos();
    }


    //----------------------------------------------------------------------------------------------
    private void SetData() {
        model = new userModel();
        mDatabase.collection("Users").document(ID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot document) {
                try {
                    model = new userModel(document.getData().get("mName").toString(), document.getData().get("mId").toString(), document.getData().get("mMail").toString(), document.getData().get("mImage").toString(), document.getData().get("mAge").toString(), document.getData().get("mDescription").toString(), document.getData().get("mAddress").toString(), document.getData().get("mLikeNumber").toString());

                } catch (Exception e) {
                    model = new userModel(document.getData().get("mName").toString(), document.getData().get("mId").toString(), document.getData().get("mMail").toString(), document.getData().get("mImage").toString(), document.getData().get("mLikeNumber").toString());

                }
                mUserName.setText(model.getmName());
                mUserAge.setText(model.getmAge());
                mUserAddress.setText(model.getmAddress());
                mUserSkills.setText(model.getmDescription());
                Glide.with(UserInfo.this).load(model.getmImage()).into(UserImage);
                mUserId.setText(model.getmId());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

    //----------------------------------------------------------------------------------------------
    private void LoadUserImages() {
        mDatabase.collection("Users").document(ID).collection("Images").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Map<String, String> map = new HashMap<>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        map = (Map<String, String>) document.getData().get("Image");

                        Image = new ImageModel(map.get("url"), map.get("id"));


                        mImages.add(Image);
                    }
                    RecyclerView.LayoutManager manager = new LinearLayoutManager(UserInfo.this, RecyclerView.HORIZONTAL, false);
                    Images.setLayoutManager(manager);
                    mImageAdapter = new ImageAdapter(mImages, UserInfo.this);
                    Images.setAdapter(mImageAdapter);
                } else {
                    Log.e("Faild To Load Images", Objects.requireNonNull(task.getException().getMessage()));
                }
            }
        });


    }

    //----------------------------------------------------------------------------------------------
    private void LoadUserVideos() {
        mDatabase.collection("Users").document(ID).collection("Videos").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                Map<String, String> map = new HashMap<>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        map = (Map<String, String>) document.getData().get("Video");

                        Image = new ImageModel(map.get("url"), map.get("id"));


                        mVideos.add(Image);
                    }
                    RecyclerView.LayoutManager manager = new LinearLayoutManager(UserInfo.this, RecyclerView.HORIZONTAL, false);
                    Videos.setLayoutManager(manager);
                    mVideoUserAdapter = new VideoUserAdapter(mVideos, UserInfo.this);
                    Videos.setAdapter(mVideoUserAdapter);
                } else {
                    Log.e("Faild To Load Videos", task.getException().getMessage());
                }
            }
        });


    }

    @OnClick({R.id.mLike, R.id.mDisLike})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.mLike:

                HashMap<String, Object> map = new HashMap<>();
                mDatabase.collection("Users").document(ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            LikeNumber = Integer.parseInt((String) task.getResult().getData().get("mLikeNumber"));
                        }
                    }
                });

                map.put("mLikeNumber", String.valueOf(LikeNumber + 1));
                mDatabase.collection("Users").document(ID).update(map);
                mLike.setVisibility(View.GONE);
                mDisLike.setVisibility(View.VISIBLE);
                break;
            case R.id.mDisLike:
                HashMap<String, Object> map2 = new HashMap<>();
                mDatabase.collection("Users").document(ID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            LikeNumber = Integer.parseInt((String) task.getResult().getData().get("mLikeNumber"));
                        }
                    }
                });

                map2.put("mLikeNumber", String.valueOf(LikeNumber = 1));
                mDatabase.collection("Users").document(ID).update(map2);
                mLike.setVisibility(View.VISIBLE);
                mDisLike.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoUserAdapter = new VideoUserAdapter(false);
    }
}