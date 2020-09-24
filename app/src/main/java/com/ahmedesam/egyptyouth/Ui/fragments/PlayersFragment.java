package com.ahmedesam.egyptyouth.Ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedesam.egyptyouth.Adapters.PlayersAdapter;
import com.ahmedesam.egyptyouth.Models.userModel;
import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Shard.ShardPrefrances;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class PlayersFragment extends Fragment {
    View view;
    DatabaseReference mDatabaseReference;
    userModel model;
    ArrayList<userModel> mPlayers;
    @BindView(R.id.Players)
    RecyclerView Players;
    PlayersAdapter mPlayersAdapter;
    Unbinder mUnbinder;
    ShardPrefrances mShardPrefrances;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_players, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mShardPrefrances = new ShardPrefrances(getActivity());

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        Players.setLayoutManager(manager);
        return view;
    }

    private void GetAllUsers() {
        mPlayers = new ArrayList<>();
        mDatabaseReference.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot mDataSnapshot : snapshot.getChildren()) {

                    model = mDataSnapshot.getValue(userModel.class);
                    if (model.getmId().equalsIgnoreCase(mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID))){

                        continue;
                    }
                    else {
                        mPlayers.add(model);
                    }
                }
                mPlayersAdapter = new PlayersAdapter(mPlayers, getActivity());
                Players.setAdapter(mPlayersAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        GetAllUsers();
    }
}