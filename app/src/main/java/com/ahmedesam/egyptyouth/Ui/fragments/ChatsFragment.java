package com.ahmedesam.egyptyouth.Ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmedesam.egyptyouth.Adapters.Chats;
import com.ahmedesam.egyptyouth.Models.Contact;
import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Shard.ShardPrefrances;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
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
import butterknife.Unbinder;


public class ChatsFragment extends Fragment {
    View view;
    @BindView(R.id.RecUser)
    RecyclerView mRecyclerView;
    Unbinder mUnbinder;
    CollectionReference mDatabaseReference;
    Chats mUserAdapter;
    ArrayList<Contact> mUsers;
    FirebaseAuth mFirebaseAuth;
    ShardPrefrances mShardPrefrances;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.mToolBar)
    Toolbar mToolBar;
    @BindView(R.id.mParent)
    ConstraintLayout mParent;

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
        mDatabaseReference = FirebaseFirestore.getInstance().collection("Users").document(mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID)).collection("Chats");
        mUsers = new ArrayList<>();
        mUserAdapter = new Chats();
        mFirebaseAuth = FirebaseAuth.getInstance();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        LoadUsers();


        if (mShardPrefrances.IsDark()) {


            mParent.setBackground(getResources().getDrawable(R.color.white));


            mToolBar.setBackground(getResources().getDrawable(R.drawable.bar_home_light));





        } else {


            mParent.setBackground(getResources().getDrawable(R.color.black));


            mToolBar.setBackground(getResources().getDrawable(R.drawable.bar_home));





        }

        return view;
    }

    void LoadUsers() {

        mDatabaseReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                mUsers.clear();
                for (QueryDocumentSnapshot postSnapshot : value) {
                    Contact imageUploadInfo = new Contact(String.valueOf(postSnapshot.getData().get("mName")), String.valueOf(postSnapshot.getData().get("mImage")), String.valueOf(postSnapshot.getData().get("mId")), String.valueOf(postSnapshot.getData().get("mLastMessage")), String.valueOf(postSnapshot.getData().get("mTime")));

                    mUsers.add(imageUploadInfo);
                }
                try {
                    Collections.sort(mUsers, new Comparator<Contact>() {
                        @Override
                        public int compare(Contact o1, Contact o2) {
                            return o2.getmTime().compareTo(o1.getmTime());

                        }
                    });
                } catch (Exception ignored) {
                }
             try {
                 mUserAdapter = new Chats(getActivity(), mUsers);

                 mRecyclerView.setAdapter(mUserAdapter);
             }
             catch (Exception e){

             }

                progressBar.setVisibility(View.GONE);
            }
        });

//        mDatabaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                mUsers.clear();
//                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//
//                    Contact imageUploadInfo = postSnapshot.getValue(Contact.class);
//
//                    mUsers.add(imageUploadInfo);
//
//
//                }
//           try {
//               Collections.sort(mUsers, new Comparator<Contact>() {
//                   @Override
//                   public int compare(Contact o1, Contact o2) {
//                       return o2.getmTime().compareTo(o1.getmTime());
//
//                   }
//               });
//           }
//           catch (Exception ignored){
//           }
//                mUserAdapter = new Chats(getActivity(), mUsers);
//
//                mRecyclerView.setAdapter(mUserAdapter);
//
//                progressBar.setVisibility(View.GONE);
//                // Hiding the progress dialog.
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//                // Hiding the progress dialog.
//            }
//        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mUserAdapter.notifyDataSetChanged();
    }
}