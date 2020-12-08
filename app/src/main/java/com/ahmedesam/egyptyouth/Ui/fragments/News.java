package com.ahmedesam.egyptyouth.Ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedesam.egyptyouth.Adapters.AllNews;
import com.ahmedesam.egyptyouth.Adapters.InternationalAndNationalAdapter;
import com.ahmedesam.egyptyouth.Models.NewModel;
import com.ahmedesam.egyptyouth.Models.userModel;
import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Shard.ShardPrefrances;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;


public class News extends Fragment {
    View view;
    Unbinder mUnbinder;
    @BindView(R.id.InternationalNews)
    RecyclerView InternationalNews;
    @BindView(R.id.NationalNews)
    RecyclerView NationalNews;
    @BindView(R.id.AllNews)
    RecyclerView AllNews;
    InternationalAndNationalAdapter mInternationalAndNationalAdapter;
    @BindView(R.id.UserImage)
    CircleImageView UserImage;
    //    @BindView(R.id.search)
//    EditText search;
    @BindView(R.id.UserName)
    TextView UserName;
    private DatabaseReference mDatabase;
    NewModel mNewModel;
    ArrayList<NewModel> mNational;
    com.ahmedesam.egyptyouth.Adapters.AllNews mAllNews;
    ShardPrefrances mShardPrefrances;
    userModel model;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_news, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mShardPrefrances = new ShardPrefrances(getActivity());
//        UserName.setText(mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_FNAME));


//        search.requestFocus();
//        search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                search.setFocusableInTouchMode(true);
//            }
//        });
        SetData();
        InstRec();
        GetNationalNews();
        GetLocalNews();
        GetAllNews();

        return view;
    }

    private void SetData() {
        model = new userModel();
        mDatabase.child("Users").child(mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                model = snapshot.getValue(userModel.class);
                UserName.setText(model.getmName());

                try {
                    Glide.with(getActivity()).load(model.getmImage()).into(UserImage);
                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void GetAllNews() {
        mDatabase.child("News").child("AllNews").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mNational = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    mNewModel = postSnapshot.getValue(NewModel.class);


                    mNational.add(mNewModel);
                }
                mAllNews = new AllNews(mNational, getActivity());
                AllNews.setAdapter(mAllNews);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.e("error", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    private void GetLocalNews() {
        mDatabase.child("News").child("Local").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mNational = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    mNewModel = postSnapshot.getValue(NewModel.class);
                    Log.e("Photo", mNewModel.getmHeader());
                    Log.e("Photo", mNewModel.getmImage());
                    mNational.add(mNewModel);
                }
                mInternationalAndNationalAdapter = new InternationalAndNationalAdapter(mNational, getActivity());
                NationalNews.setAdapter(mInternationalAndNationalAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.e("error", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    private void InstRec() {
        RecyclerView.LayoutManager horizontal = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        RecyclerView.LayoutManager horizontal2 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);

        RecyclerView.LayoutManager Vertical = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        InternationalNews.setLayoutManager(horizontal);
        NationalNews.setLayoutManager(horizontal2);
        AllNews.setLayoutManager(Vertical);
    }

    private void GetNationalNews() {
        mDatabase.child("News").child("National").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mNational = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    mNewModel = postSnapshot.getValue(NewModel.class);
                    Log.e("Photo", mNewModel.getmHeader());
                    Log.e("Photo", mNewModel.getmImage());
                    mNational.add(mNewModel);
                }
                mInternationalAndNationalAdapter = new InternationalAndNationalAdapter(mNational, getActivity());
                InternationalNews.setAdapter(mInternationalAndNationalAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.e("error", "loadPost:onCancelled", databaseError.toException());
            }
        });

    }


}