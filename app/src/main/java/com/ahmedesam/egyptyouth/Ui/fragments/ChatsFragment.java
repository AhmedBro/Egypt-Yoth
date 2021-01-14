package com.ahmedesam.egyptyouth.Ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedesam.egyptyouth.Adapters.Chats;
import com.ahmedesam.egyptyouth.Models.Contact;
import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Shard.ShardPrefrances;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ChatsFragment extends Fragment {
    View view;
    @BindView(R.id.RecUser)
    RecyclerView mRecyclerView;
    Unbinder mUnbinder;
    DatabaseReference mDatabaseReference;
    Chats mUserAdapter;
    ArrayList<Contact> mUsers;
    FirebaseAuth mFirebaseAuth;
    ShardPrefrances mShardPrefrances;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chats, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mShardPrefrances = new ShardPrefrances(getActivity());
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Users").child(mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID)).child("Chats");
        mUsers = new ArrayList<>();
        mUserAdapter = new Chats();
        mFirebaseAuth = FirebaseAuth.getInstance();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        LoadUsers();
        return view;
    }

    void LoadUsers() {
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mUsers.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    Contact imageUploadInfo = postSnapshot.getValue(Contact.class);

                    mUsers.add(imageUploadInfo);


                }
                Collections.sort(mUsers, new Comparator<Contact>() {
                    @Override
                    public int compare(Contact o1, Contact o2) {
                        return o2.getmTime().compareTo(o1.getmTime());

                    }
                });
                mUserAdapter = new Chats(getActivity(), mUsers);

                mRecyclerView.setAdapter(mUserAdapter);

                progressBar.setVisibility(View.GONE);
                // Hiding the progress dialog.
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                // Hiding the progress dialog.
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mUserAdapter.notifyDataSetChanged();
    }
}