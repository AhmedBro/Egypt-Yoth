package com.ahmedesam.egyptyouth.Ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedesam.egyptyouth.Adapters.PlayersAdapter;
import com.ahmedesam.egyptyouth.Adapters.ShowAllPlayers;
import com.ahmedesam.egyptyouth.Models.userModel;
import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Shard.ShardPrefrances;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class PlayersFragment extends Fragment {
    View view;
    FirebaseFirestore mDatabaseReference;
    userModel model;
    ArrayList<userModel> mPlayers;
    @BindView(R.id.Players)
    RecyclerView Players;
    PlayersAdapter mPlayersAdapter;
    Unbinder mUnbinder;
    ShardPrefrances mShardPrefrances;
    @BindView(R.id.AllPlayers)
    RecyclerView AllPlayers;
    ShowAllPlayers mShowAllPlayers;
    @BindView(R.id.bar)
    LinearLayout bar;
    @BindView(R.id.mParent)
    ConstraintLayout mParent;
    @BindView(R.id.mSeprator)
    View mSeprator;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_players, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mDatabaseReference = FirebaseFirestore.getInstance();
        mShardPrefrances = new ShardPrefrances(getActivity());
        mPlayersAdapter = new PlayersAdapter();
        if (mShardPrefrances.IsDark()) {


            mParent.setBackground(getResources().getDrawable(R.color.white));
            mSeprator.setBackground(getResources().getDrawable(R.color.black));
            bar.setBackground(getResources().getDrawable(R.drawable.bar_home_light));
        } else {


            mParent.setBackground(getResources().getDrawable(R.color.black));
            mSeprator.setBackground(getResources().getDrawable(R.color.white));
            bar.setBackground(getResources().getDrawable(R.drawable.bar_home));

        }

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        Players.setLayoutManager(manager);

        RecyclerView.LayoutManager manager1 = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
        AllPlayers.setLayoutManager(manager1);
        GetAllUsers();
        return view;
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
                mPlayersAdapter = new PlayersAdapter(mPlayers, getActivity());
                Players.setAdapter(mPlayersAdapter);

                mShowAllPlayers = new ShowAllPlayers(mPlayers, getActivity());
                AllPlayers.setAdapter(mShowAllPlayers);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mPlayersAdapter.notifyDataSetChanged();

    }
}