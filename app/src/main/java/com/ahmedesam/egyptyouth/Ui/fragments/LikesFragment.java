package com.ahmedesam.egyptyouth.Ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedesam.egyptyouth.Adapters.LikeAdapter;
import com.ahmedesam.egyptyouth.Models.LikeModel;
import com.ahmedesam.egyptyouth.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class LikesFragment extends BottomSheetDialogFragment {

    View view;
    Unbinder unbinder;
    @BindView(R.id.mLikes)
    RecyclerView mLikes;
    FirebaseFirestore mFirebaseFirestore;
    static String Id;
    LikeModel model;
    ArrayList<LikeModel> mLikesArray;
    LikeAdapter mLikeAdapter;

    public LikesFragment() {
        // Required empty public constructor
    }

    public LikesFragment(String getmPostID) {
        Id = getmPostID;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_likes, container, false);
        unbinder = ButterKnife.bind(this, view);
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        mLikesArray = new ArrayList<>();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mLikes.setLayoutManager(manager);
        mFirebaseFirestore.collection("Posts").document(Id).collection("User Liked").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot document : value) {
                    model = new LikeModel(String.valueOf(document.getData().get("mId")), String.valueOf(document.getData().get("mUserName")), String.valueOf(document.getData().get("mUserImage")));
                    mLikesArray.add(model);
                    Log.e("Likes", model.getmUserImage());
                }
                mLikeAdapter = new LikeAdapter(mLikesArray, getContext());
                mLikes.setAdapter(mLikeAdapter);
            }
        });

        return view;
    }
}