package com.ahmedesam.egyptyouth.Ui.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedesam.egyptyouth.Adapters.PlayersAdapter;
import com.ahmedesam.egyptyouth.Models.userModel;
import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Shard.ShardPrefrances;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class SearchFragment extends Fragment {

    View view;
    Unbinder mUnbinder;
    @BindView(R.id.et_search)
    TextInputEditText etSearch;
    @BindView(R.id.mSearchEResult)
    RecyclerView mSearchEResult;
    FirebaseFirestore mDatabaseReference;
    userModel model;
    ArrayList<userModel> mPlayers;
    PlayersAdapter mPlayersAdapter;
    ShardPrefrances mShardPrefrances;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.mParent)
    ConstraintLayout mParent;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mDatabaseReference = FirebaseFirestore.getInstance();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mSearchEResult.setLayoutManager(manager);
        mPlayersAdapter = new PlayersAdapter();
        mShardPrefrances = new ShardPrefrances(getActivity());
        mPlayers = new ArrayList<>();
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SearchWithEnglishPostion(s);
                SearchWithArabicPostion(s);
                SearchWithId(s);
                SearchWithName(s);
                searchWithEmail(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        if (mShardPrefrances.IsDark()) {


            mParent.setBackground(getResources().getDrawable(R.color.background));



        } else {


            mParent.setBackground(getResources().getDrawable(R.color.black));



        }

        return view;

    }

    private void searchWithEmail(CharSequence s) {
        mDatabaseReference.collection("Users").whereEqualTo("mMail", String.valueOf(s).toUpperCase().trim()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("LongLogTag")
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
                    mPlayersAdapter = new PlayersAdapter(mPlayers, getActivity());
                    mSearchEResult.setAdapter(mPlayersAdapter);

                } else {
                    Log.e("Error getting documents: ", task.getException()
                            + "");
                }

            }

        });
    }

    private void SearchWithName(CharSequence s) {
        mDatabaseReference.collection("Users").whereEqualTo("mName", String.valueOf(s).toUpperCase().trim()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("LongLogTag")
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
                    mPlayersAdapter = new PlayersAdapter(mPlayers, getActivity());
                    mSearchEResult.setAdapter(mPlayersAdapter);

                } else {
                    Log.e("Error getting documents: ", task.getException()
                            + "");
                }

            }

        });

    }

    private void SearchWithId(CharSequence s) {
        mDatabaseReference.collection("Users").whereEqualTo("mId", String.valueOf(s).toUpperCase().trim()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("LongLogTag")
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
                    mPlayersAdapter = new PlayersAdapter(mPlayers, getActivity());
                    mSearchEResult.setAdapter(mPlayersAdapter);

                } else {
                    Log.e("Error getting documents: ", task.getException()
                            + "");
                }

            }

        });

    }

    private void SearchWithArabicPostion(CharSequence s) {
        mDatabaseReference.collection("Users").whereEqualTo("mDescription", String.valueOf(s).toUpperCase().trim()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("LongLogTag")
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
                    mPlayersAdapter = new PlayersAdapter(mPlayers, getActivity());
                    mSearchEResult.setAdapter(mPlayersAdapter);

                } else {
                    Log.e("Error getting documents: ", task.getException()
                            + "");
                }

            }

        });
    }

    private void SearchWithEnglishPostion(CharSequence s) {
        mDatabaseReference.collection("Users").whereEqualTo("mDescription", String.valueOf(s).toUpperCase().trim()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @SuppressLint("LongLogTag")
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
                    mPlayersAdapter = new PlayersAdapter(mPlayers, getActivity());
                    mSearchEResult.setAdapter(mPlayersAdapter);

                } else {
                    Log.e("Error getting documents: ", task.getException()
                            + "");
                }

            }

        });

    }
}