package com.nuguru.gitreposearch;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.nuguru.gitreposearch.Frames.HomeFragment;
import com.nuguru.gitreposearch.Frames.SettingFragment;

public class MainActivity extends AppCompatActivity {

    private ActionBar toolbar;
    SettingFragment mSettingFrame = new SettingFragment();
    HomeFragment mMainFrame = new HomeFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = getSupportActionBar();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar.setTitle("Trending Repos");
        loadFragment(mMainFrame);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.nav_trending:
                    toolbar.setTitle("Trending Repos");
                    loadFragment(mMainFrame);
                    return true;
                case R.id.nav_settings:
                    toolbar.setTitle("Settings");
                    loadFragment(mSettingFrame);
                    return true;
            }
            return false;
        }
    };


    private void loadFragment(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_main, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


}
