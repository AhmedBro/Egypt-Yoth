package com.ahmedesam.egyptyouth.Ui.fragments;

import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_players, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mDatabaseReference = FirebaseFirestore.getInstance();
        mShardPrefrances = new ShardPrefrances(getActivity());

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        Players.setLayoutManager(manager);
        return view;
    }

    private void GetAllUsers() {
        mPlayers = new ArrayList<>();

        mDatabaseReference.collection("Users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {


                        model = new userModel(document.getData().get("mName").toString(), document.getData().get("mId").toString(), document.getData().get("mMail").toString(), document.getData().get("mImage").toString(),  document.getData().get("mDescription").toString(),document.getData().get("mLikeNumber").toString());
                        if (model.getmId().equals(mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID))){
                            continue;
                        }
                        mPlayers.add(model);
                        Log.e("Users", model .getmLikeNumber());
                    }
                }
                mPlayersAdapter = new PlayersAdapter(mPlayers, getActivity());
                Players.setAdapter(mPlayersAdapter);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        GetAllUsers();
    }
}