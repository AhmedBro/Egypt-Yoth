package com.ahmedesam.egyptyouth.Ui.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;

import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Shard.ShardPrefrances;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InsertYourInfo extends AppCompatActivity {

    @BindView(R.id.mUserName)
    TextInputEditText mUserName;
    @BindView(R.id.mSpinner)
    AppCompatSpinner mSpinner;
    @BindView(R.id.mUserAge)
    TextInputEditText mUserAge;
    @BindView(R.id.mUserAddress)
    TextInputEditText mUserAddress;
    @BindView(R.id.linearLayout2)
    LinearLayout linearLayout2;
    @BindView(R.id.mSave)
    Button mSave;
    static ArrayList<String> mPositions;
    static String mDescription;
    ShardPrefrances mShardPrefrances;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private FirebaseFirestore mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_your_info);
        ButterKnife.bind(this);
        mShardPrefrances = new ShardPrefrances(this);

        if (mShardPrefrances.IsDark()){
            setTheme(R.style.AppTheme);
        }else {
            setTheme(R.style.AppThemeLight);
        }
        mDatabase = FirebaseFirestore.getInstance();
        mPositions = new ArrayList<>();
        mPositions.add(getString(R.string.Gk));
        mPositions.add(getString(R.string.CB));
        mPositions.add(getString(R.string.RB));
        mPositions.add(getString(R.string.LB));
        mPositions.add(getString(R.string.DMF));
        mPositions.add(getString(R.string.CMF));
        mPositions.add(getString(R.string.SS));
        mPositions.add(getString(R.string.AMF));
        mPositions.add(getString(R.string.RMF));
        mPositions.add(getString(R.string.LWF));
        mPositions.add(getString(R.string.CF));
        mPositions.add(getString(R.string.enter_your_skills_description));
        ArrayAdapter arrayAdapter;

        arrayAdapter = new ArrayAdapter(InsertYourInfo.this, R.layout.spinner_shap, mPositions);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(arrayAdapter);
        mSpinner.setSelection(mPositions.size()-1); //display hint
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mDescription = mPositions.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @OnClick(R.id.mSave)
    public void onViewClicked() {
        if (Validate()) {
            InsertData(mUserName.getText().toString(), mUserAge.getText().toString(), mDescription, mUserAddress.getText().toString());
            progressBar.setVisibility(View.VISIBLE);
        }

    }

    private boolean Validate() {
        if (mUserAge.getText().toString().isEmpty()) {
            Snackbar.make(this.findViewById(android.R.id.content), R.string.enter_your_age, Snackbar.LENGTH_LONG).show();
            return false;
        } else if (mUserName.getText().toString().isEmpty()) {
            Snackbar.make(this.findViewById(android.R.id.content), R.string.enter_your_name, Snackbar.LENGTH_LONG).show();

            return false;
        } else if (mUserAddress.getText().toString().isEmpty()) {
            Snackbar.make(this.findViewById(android.R.id.content), R.string.enter_your_address, Snackbar.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }

    }

    void InsertData(String aName, String aAge, String aDescription, String aAddress) {
        HashMap<String, Object> Map = new HashMap<>();
        Map.put("mName", aName.toUpperCase());
        Map.put("mAge", aAge.toUpperCase());
        Map.put("mDescription", aDescription.toUpperCase());
        Map.put("mAddress", aAddress.toUpperCase());
        mShardPrefrances.EditAge(aAge);
        mShardPrefrances.EditAddress(aAddress);
        mShardPrefrances.EditDescription(aDescription);
        mShardPrefrances.EditName(aName);
        mShardPrefrances.SetIsLogin();
        mDatabase.collection("Users").document(mShardPrefrances.getUserDetails().get(mShardPrefrances.KEY_ID)).update(Map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(InsertYourInfo.this, "Your Profile Is Updated", Toast.LENGTH_SHORT).show();
                Intent mIntent = new Intent(InsertYourInfo.this, HomeActivity.class);
                startActivity(mIntent);
               InsertYourInfo.this.finish();
                progressBar.setVisibility(View.GONE);

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(InsertYourInfo.this, "Error Please Try Again Later", Toast.LENGTH_SHORT).show();
                        Log.e("Update Profile Error", e.getMessage());
                    }
                });
    }



}