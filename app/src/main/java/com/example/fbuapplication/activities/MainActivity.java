package com.example.fbuapplication.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.fbuapplication.CustomViewPager;
import com.example.fbuapplication.R;
import com.example.fbuapplication.fragments.ComposeFragment;
import com.example.fbuapplication.fragments.InboxFragment;
import com.example.fbuapplication.fragments.MainWallFragment;
import com.example.fbuapplication.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import eu.long1.spacetablayout.SpaceTabLayout;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    private static final String DEFAULT_FB_APP_ID = "826542378066880";
    SpaceTabLayout tabLayout;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final FragmentManager fragmentManager = getSupportFragmentManager();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new MainWallFragment());
        fragmentList.add(new InboxFragment());
        fragmentList.add(new ComposeFragment());
        fragmentList.add(new ProfileFragment());

        CustomViewPager viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.spaceTabLayout);

        tabLayout.initialize(viewPager, getSupportFragmentManager(),
                fragmentList, savedInstanceState);

    }

}