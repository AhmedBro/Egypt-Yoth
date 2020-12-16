package com.ahmedesam.egyptyouth.Ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.ahmedesam.egyptyouth.Models.PostModel;
import com.ahmedesam.egyptyouth.R;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class ShowNewFragment extends DialogFragment {
    View view;
    DatabaseReference mDatabaseReference;
    @BindView(R.id.mNewHeader)
    TextView mNewHeader;
    @BindView(R.id.mNew)
    TextView mNew;
    @BindView(R.id.mNewImage)
    ImageView mNewImage;

    @BindView(R.id.LikeNumber)
    TextView LikeNumber;
    static String Id;
    PostModel model;
    Unbinder mUnbinder;
    static boolean check = false;

    public ShowNewFragment(String Id) {
        // Required empty public constructor
        this.Id = Id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_show_new, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        HashMap<String, Object> map = new HashMap<>();
        HashMap<String, Object> map2 = new HashMap<>();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        GetAndSetData();

        return view;
    }

    private void GetAndSetData() {

        mDatabaseReference.child("News").child("AllNews").child(Id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                model = snapshot.getValue(PostModel.class);

                Glide.with(Objects.requireNonNull(getActivity())).load(model.getmImage()).into(mNewImage);
//                mNew.setText(model.getmNew());
//                mNewHeader.setText(model.getmHeader());
//                LikeNumber.setText(model.getmLikeNumber());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        int width = getResources().getDimensionPixelSize(R.dimen._300sdp);
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setLayout(width, height);


    }
}