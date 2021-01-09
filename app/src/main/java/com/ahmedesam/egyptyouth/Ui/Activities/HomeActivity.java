package com.ahmedesam.egyptyouth.Ui.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Ui.fragments.ChatsFragment;
import com.ahmedesam.egyptyouth.Ui.fragments.Home;
import com.ahmedesam.egyptyouth.Ui.fragments.PlayersFragment;
import com.ahmedesam.egyptyouth.Ui.fragments.SearchFragment;
import com.ahmedesam.egyptyouth.Ui.fragments.UserProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.onesignal.OneSignal;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.replace)
    LinearLayout replace;
    @BindView(R.id.nav_view)
    BottomNavigationView navView;
    private static final String ONESIGNAL_APP_ID = "66b4a76c-6fff-433d-b831-e29c9fc8b0ea";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);



        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        Log.e("Tokenn", token);


                    }
                });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        LoadFragment(new Home());
        //---------------notification------------------------


        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        //---------------notification------------------------

//        FirebaseMessaging.getInstance().subscribeToTopic("weather")
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        String msg = getString(R.string.msg_subscribed);
//                        if (!task.isSuccessful()) {
//                            msg = getString(R.string.msg_subscribe_failed);
//                        }
//                        Log.d(TAG, msg);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//                    }
//                });
    }

    public void LoadFragment(Fragment mFragment) {
        FragmentManager fragmentManager2 = getSupportFragmentManager();
        fragmentManager2.beginTransaction().replace(R.id.replace, mFragment).commit();
    }

    //-----------------------------------------------------bottomNavigation----------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_nav_menu, menu);
        return true;
    }


    private BottomNavigationView.OnNavigationItemSelectedListener
            mOnNavigationItemSelectedListener
            = item -> {

        switch (item.getItemId()) {
            case R.id.homeFragment:
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.replace, new Home()).commit();
                return true;


            case R.id.players:
                FragmentManager fragmentManager5 = getSupportFragmentManager();
                fragmentManager5.beginTransaction().replace(R.id.replace, new PlayersFragment()).commit();
                return true;
//
            case R.id.Profile:

                FragmentManager fragmentManager4 = getSupportFragmentManager();
                fragmentManager4.beginTransaction().replace(R.id.replace, new UserProfileFragment()).commit();
                return true;
            case R.id.Chats:
                FragmentManager fragmentManager6 = getSupportFragmentManager();
                fragmentManager6.beginTransaction().replace(R.id.replace, new ChatsFragment()).commit();
                return true;
            case R.id.mSearch:
                FragmentManager fragmentManager7 = getSupportFragmentManager();
                fragmentManager7.beginTransaction().replace(R.id.replace, new SearchFragment()).commit();
                return true;
        }

        return false;
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


}
