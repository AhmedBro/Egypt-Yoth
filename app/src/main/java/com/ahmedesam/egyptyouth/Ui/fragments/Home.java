package com.ahmedesam.egyptyouth.Ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedesam.egyptyouth.Adapters.PostsAdapter;
import com.ahmedesam.egyptyouth.Adapters.ShowAllPlayers;
import com.ahmedesam.egyptyouth.Models.PostModel;
import com.ahmedesam.egyptyouth.Models.userModel;
import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Shard.ShardPrefrances;
import com.ahmedesam.egyptyouth.Ui.Activities.AddPost;
import com.bitvale.switcher.SwitcherC;
import com.bitvale.switcher.SwitcherX;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class Home extends Fragment {
    View view;
    Unbinder mUnbinder;
    ArrayList<userModel> mPlayers;
    @BindView(R.id.UserImage)
    CircleImageView UserImage;
    @BindView(R.id.AllPlayers)
    RecyclerView AllPlayers;
    @BindView(R.id.Posts)
    RecyclerView Posts;
    ShowAllPlayers mShowAllPlayers;
    FirebaseFirestore mDatabaseReference;
    userModel model;
    ShardPrefrances mShardPrefrances;
    PostsAdapter mPostsAdapter;
    ArrayList<PostModel> mPosts;
    PostModel mPostModel;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.switcher)
    SwitcherX switcher;
    @BindView(R.id.UserName)
    TextView UserName;
    @BindView(R.id.bar)
    LinearLayout bar;
    @BindView(R.id.mParent)
    LinearLayout mParent;
    @BindView(R.id.mSeprator)
    View mSeprator;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_home, container, false);
        mUnbinder = ButterKnife.bind(this, view);


        mDatabaseReference = FirebaseFirestore.getInstance();
        mShardPrefrances = new ShardPrefrances(getActivity());
        mShowAllPlayers = new ShowAllPlayers();
        mPostsAdapter = new PostsAdapter();
        mAuth = FirebaseAuth.getInstance();


        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        AllPlayers.setLayoutManager(manager);
        RecyclerView.LayoutManager manager1 = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        Posts.setLayoutManager(manager1);
        GetAllUsers();
        GetPosts();

        if (mShardPrefrances.IsDark()) {
            switcher.setChecked(false, true);

            mParent.setBackground(getResources().getDrawable(R.color.white));
            mSeprator.setBackground(getResources().getDrawable(R.color.black));
            bar.setBackground(getResources().getDrawable(R.drawable.bar_home_light));
            progressBar.setBackground(getResources().getDrawable(R.color.colorPrimaryLight));
        } else {
            switcher.setChecked(true, true);

            mParent.setBackground(getResources().getDrawable(R.color.black));
            mSeprator.setBackground(getResources().getDrawable(R.color.white));
            bar.setBackground(getResources().getDrawable(R.drawable.bar_home));

        }

        switcher.setOnCheckedChangeListener(new Function1<Boolean, Unit>() {
            @Override
            public Unit invoke(Boolean aBoolean) {
                if (!aBoolean) {
                    mShardPrefrances.EditTheme(true);
                    getActivity().finish();
                    startActivity(getActivity().getIntent());
                } else {
                    mShardPrefrances.EditTheme(false);
                    getActivity().finish();
                    startActivity(getActivity().getIntent());
                }
                return null;
            }
        });
        return view;
    }

    private void GetPosts() {
        mPosts = new ArrayList<>();
        mDatabaseReference.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                mPosts.clear();
                for (QueryDocumentSnapshot document : value) {
                    mPostModel = new PostModel((String.valueOf(document.getData().get("mPost"))),
                            String.valueOf(document.getData().get("mImage")),
                            String.valueOf(document.getData().get("mLikeNumber")),
                            String.valueOf(document.getData().get("mPostID")),
                            String.valueOf(document.getData().get("mUserId")),
                            String.valueOf(document.getData().get("mVideo")),
                            String.valueOf(document.getData().get("mUserName")),
                            String.valueOf(document.getData().get("mUserImage")),
                            String.valueOf(document.getData().get("mCommentsNumber"))
                            );
                    mPosts.add(mPostModel);
                }
                Collections.sort(mPosts, new Comparator<PostModel>() {
                    @Override
                    public int compare(PostModel o1, PostModel o2) {
                        return o2.getmLikeNumber().compareTo(o1.getmLikeNumber());
                    }
                });
               try {
                   mPostsAdapter = new PostsAdapter(mPosts, getContext());
                   Posts.setAdapter(mPostsAdapter);
               }catch (Exception E){

               }

                progressBar.setVisibility(View.GONE);
            }

        });
    }

    @OnClick(R.id.UserImage)
    public void onViewClicked() {
        Intent mIntent = new Intent(getActivity(), AddPost.class);

        startActivity(mIntent);

    }

    private void GetAllUsers() {
        mPlayers = new ArrayList<>();

        mDatabaseReference.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {


                        model = new userModel(document.getData().get("mName").toString(), document.getData().get("mId").toString(), document.getData().get("mMail").toString(), document.getData().get("mImage").toString(), document.getData().get("mDescription").toString(), document.getData().get("mLikeNumber").toString());
                        if (model.getmId().equals(mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID))) {
                            continue;
                        }
                        mPlayers.add(model);


                    }
                }

                mShowAllPlayers = new ShowAllPlayers(mPlayers, getActivity());
                AllPlayers.setAdapter(mShowAllPlayers);
                mShowAllPlayers.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mPostsAdapter.notifyDataSetChanged();
        mShowAllPlayers.notifyDataSetChanged();


    }

    @Override
    public void onPause() {
        super.onPause();
        mPostsAdapter = new PostsAdapter(false);
    }


}