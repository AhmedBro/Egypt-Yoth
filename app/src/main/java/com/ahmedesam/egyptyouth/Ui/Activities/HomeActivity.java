package com.ahmedesam.egyptyouth.Ui.Activities;


import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import android.widget.LinearLayout;



import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Shard.ShardPrefrances;
import com.ahmedesam.egyptyouth.Ui.fragments.ChatsFragment;
import com.ahmedesam.egyptyouth.Ui.fragments.Home;
import com.ahmedesam.egyptyouth.Ui.fragments.PlayersFragment;
import com.ahmedesam.egyptyouth.Ui.fragments.SearchFragment;
import com.ahmedesam.egyptyouth.Ui.fragments.UserProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.replace)
    LinearLayout replace;
    @BindView(R.id.nav_view)
    BottomNavigationView navView;
    ShardPrefrances mShardPrefrances;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        mShardPrefrances = new ShardPrefrances(this);

        try {
            if (mShardPrefrances.IsDark()) {
                navView.setBackground(getResources().getDrawable(R.drawable.bar_home_light));

            } else {
                navView.setBackground(getResources().getDrawable(R.drawable.menu_dark));
            }
        } catch (Exception e) {

        }

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        LoadFragment(new Home());

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


    }

