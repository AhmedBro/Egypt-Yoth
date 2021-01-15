package com.ahmedesam.egyptyouth.Ui.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedesam.egyptyouth.Adapters.ImageAdapter;
import com.ahmedesam.egyptyouth.Adapters.PostsAdapter;
import com.ahmedesam.egyptyouth.Adapters.VideoUserAdapter;
import com.ahmedesam.egyptyouth.Models.ImageModel;
import com.ahmedesam.egyptyouth.Models.PostModel;
import com.ahmedesam.egyptyouth.Models.userModel;
import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Shard.ShardPrefrances;
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
import java.util.Collections;
import java.util.Comparator;
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
    @BindView(R.id.mChat)
    Button mChat;
    @BindView(R.id.mParent)
    ConstraintLayout mParent;
    @BindView(R.id.mYear)
    TextView mYear;
    private FirebaseFirestore mDatabase;
    userModel model;
    ImageModel Image;
    @BindView(R.id.mPosts)
    RecyclerView mUserPosts;
    Intent mIntent;
    static int LikeNumber;
    ShardPrefrances mShardPrefrances;

    static String mName;
    static String mImage;
    PostModel mPostModel;
    FirebaseFirestore mDatabaseReference;
    PostsAdapter mPostsAdapter;
    ArrayList<PostModel> mPosts;

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
        LinearLayoutManager manager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        mUserPosts.setLayoutManager(manager);
        mShardPrefrances = new ShardPrefrances(this);
        SetData();
        mDatabaseReference = FirebaseFirestore.getInstance();
        LoadUserImages();

        LoadUserVideos();

        LoadPosts();

        CheckIfLiked();

        if (mShardPrefrances.IsDark()) {


            mParent.setBackground(getResources().getDrawable(R.color.white));
            mChat.setBackground(getResources().getDrawable(R.drawable.edit_photo_button_light));
            mChat.setTextColor(getResources().getColor(R.color.white));
            mLike.setBackground(getResources().getDrawable(R.drawable.edit_photo_button_light));
            mLike.setTextColor(getResources().getColor(R.color.white));
            mUserAddress.setTextColor(getResources().getColor(R.color.black));
            mUserAge.setTextColor(getResources().getColor(R.color.black));
            mUserName.setTextColor(getResources().getColor(R.color.black));
            mUserSkills.setTextColor(getResources().getColor(R.color.black));
            mYear.setTextColor(getResources().getColor(R.color.black));

        } else {


            mParent.setBackground(getResources().getDrawable(R.color.black));
            mChat.setBackground(getResources().getDrawable(R.drawable.edit_photo_button));
            mChat.setTextColor(getResources().getColor(R.color.white));
            mLike.setBackground(getResources().getDrawable(R.drawable.edit_photo_button));
            mLike.setTextColor(getResources().getColor(R.color.white));
            mUserAddress.setTextColor(getResources().getColor(R.color.white));
            mUserAge.setTextColor(getResources().getColor(R.color.white));
            mUserName.setTextColor(getResources().getColor(R.color.white));
            mUserSkills.setTextColor(getResources().getColor(R.color.white));
            mYear.setTextColor(getResources().getColor(R.color.white));

        }

    }

    private void LoadPosts() {
        mPosts = new ArrayList<>();
        mDatabaseReference.collection("Users").document(ID).collection("Posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        mPostModel = new PostModel((String.valueOf(document.getData().get("mPost"))),
                                String.valueOf(document.getData().get("mImage")),
                                String.valueOf(document.getData().get("mLikeNumber")),
                                String.valueOf(document.getData().get("mPostID")),
                                String.valueOf(document.getData().get("mUserId")),
                                String.valueOf(document.getData().get("mVideo")),
                                String.valueOf(document.getData().get("mUserName")),
                                String.valueOf(document.getData().get("mUserImage")));
                        mPosts.add(mPostModel);
                    }
                    Collections.sort(mPosts, new Comparator<PostModel>() {
                        @Override
                        public int compare(PostModel o1, PostModel o2) {
                            return o2.getmLikeNumber().compareTo(o1.getmLikeNumber());
                        }
                    });
                    mPostsAdapter = new PostsAdapter(mPosts, UserInfo.this);
                    mUserPosts.setAdapter(mPostsAdapter);

                }
            }
        });
    }


    private void CheckIfLiked() {
        mDatabase.collection("Users").document(ID).collection("User Liked").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.getData().get("Id").toString().equals(mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID))) {
                            mLike.setVisibility(View.GONE);
                            mDisLike.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
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
                mName = model.getmName();
                mImage = model.getmImage();
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
                HashMap<String, Object> map3 = new HashMap<>();
                map3.put("Id", mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID));
                mDatabase.collection("Users").document(ID).collection("User Liked").document(mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID)).set(map3);
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
                mDatabase.collection("Users").document(ID).collection("User Liked").document(mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID)).delete();

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

    @OnClick(R.id.mChat)
    public void onViewClicked() {

        Intent mIntent = new Intent(this, ChatActivity.class);
        mIntent.putExtra("Id", ID);
        mIntent.putExtra("Name", mName);
        mIntent.putExtra("Image", mImage);


        startActivity(mIntent);
    }
}