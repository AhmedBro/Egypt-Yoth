package com.ahmedesam.egyptyouth.Ui.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Shard.ShardPrefrances;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splash extends AppCompatActivity {
    ShardPrefrances mShardPrefrances;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mShardPrefrances = new ShardPrefrances(this);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mShardPrefrances.isLoggedIn()) {
                    Intent mainIntent = new Intent(splash.this, HomeActivity.class);
                    splash.this.startActivity(mainIntent);
                    splash.this.finish();
                }

                else {
                    Intent mainIntent = new Intent(splash.this, LogIn.class);
                    splash.this.startActivity(mainIntent);
                    splash.this.finish();
                }
                /* Create an Intent that will start the Menu-Activity. */

            }
        }, 1000);
    }
}

