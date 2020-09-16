package com.ahmedesam.egyptyouth.Ui.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.ahmedesam.egyptyouth.R;
import com.ahmedesam.egyptyouth.Ui.fragments.News;
import com.ahmedesam.egyptyouth.Ui.fragments.UserProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.replace)
    LinearLayout replace;
    @BindView(R.id.nav_view)
    BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        LoadFragment(new News());
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

                        FragmentManager fragmentManager2 = getSupportFragmentManager();
                        fragmentManager2.beginTransaction().replace(R.id.replace, new News()).commit();
                        return true;

                    case R.id.Profile:

                        FragmentManager fragmentManager4 = getSupportFragmentManager();
                        fragmentManager4.beginTransaction().replace(R.id.replace, new UserProfileFragment()).commit();
                        return true;
//                    case R.id.order:
//                        FragmentManager fragmentManager5 = getSupportFragmentManager();
//                        fragmentManager5.beginTransaction().replace(R.id.replace, new MakeOrderFragment()).commit();
//                        return true;
//                    case R.id.Map:
//                        FragmentManager fragmentManager6 = getSupportFragmentManager();
//                        fragmentManager6.beginTransaction().replace(R.id.replace, new MapFragment()).commit();
//                        return true;
               }

                return false;
            };
}
