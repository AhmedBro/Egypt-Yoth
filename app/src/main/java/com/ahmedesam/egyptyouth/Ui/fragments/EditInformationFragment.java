package com.ahmedesam.egyptyouth.Ui.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Shard.ShardPrefrances;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class EditInformationFragment extends DialogFragment {
    View view;
    @BindView(R.id.mUserName)
    EditText mUserName;
    @BindView(R.id.mUserDescription)
    EditText mUserDescription;
    @BindView(R.id.mUserAge)
    EditText mUserAge;
    @BindView(R.id.mSave)
    Button mSave;
    @BindView(R.id.mUserAddress)
    EditText mUserAddress;
    private DatabaseReference mDatabase;
    ShardPrefrances mShardPrefrances;
    Unbinder mUnbinder;

    public EditInformationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.edit_profile, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mShardPrefrances = new ShardPrefrances(getContext());
        Validate();
        return view;
    }

    private boolean Validate() {
        if (mUserAge.getText().toString().isEmpty()) {
            Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.enter_Your_Name, Snackbar.LENGTH_LONG).show();
            return false;
        } else if (mUserDescription.getText().toString().isEmpty()) {
            Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.Enter_Your_Description, Snackbar.LENGTH_LONG).show();

            return false;
        } else if (mUserName.getText().toString().isEmpty()) {
            Snackbar.make(getActivity().findViewById(android.R.id.content), R.string.enter_Your_Name, Snackbar.LENGTH_LONG).show();

            return false;
        } else {
            return true;
        }
    }

    @OnClick(R.id.mSave)
    public void onViewClicked() {
        if (Validate()) {
            HashMap<String, Object> Map = new HashMap<>();
            Map.put("mName", mUserName.getText().toString());
            Map.put("mDate", mUserAge.getText().toString());
            Map.put("mDescription", mUserDescription.getText().toString());
            Map.put("mAddress", mUserAddress.getText().toString());
            mShardPrefrances.EditAge(mUserAge.getText().toString());
            mShardPrefrances.EditAddress(mUserAddress.getText().toString());
            mShardPrefrances.EditDescription(mUserDescription.getText().toString());
            mShardPrefrances.EditName(mUserName.getText().toString());
            mDatabase.child("Users").child(mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID)).updateChildren(Map);
            Log.e("ID", mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID));

            dismiss();
        }
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        FragmentManager fragmentManager4 = getActivity().getSupportFragmentManager();
        fragmentManager4.beginTransaction().replace(R.id.replace, new UserProfileFragment()).commit();
    }
}