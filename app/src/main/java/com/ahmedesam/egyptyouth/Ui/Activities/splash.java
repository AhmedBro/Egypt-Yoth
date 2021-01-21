package com.ahmedesam.egyptyouth.Ui.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.ahmedesam.egyptyouth.Models.userModel;
import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Shard.ShardPrefrances;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class splash extends AppCompatActivity {
    ShardPrefrances mShardPrefrances;


    FirebaseUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mShardPrefrances = new ShardPrefrances(this);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mShardPrefrances.isLoggedIn() && currentUser != null) {
                    Intent mainIntent = new Intent(splash.this, HomeActivity.class);
                    splash.this.startActivity(mainIntent);
                    splash.this.finish();
                } else {
                    Intent mainIntent = new Intent(splash.this, LogIn.class);
                    splash.this.startActivity(mainIntent);
                    splash.this.finish();
                }
                /* Create an Intent that will start the Menu-Activity. */

            }
        }, 1000);
    }


}



