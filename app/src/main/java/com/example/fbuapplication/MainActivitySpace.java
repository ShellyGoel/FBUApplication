package com.example.fbuapplication;


import android.os.Bundle;

import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import eu.long1.spacetablayout.SpaceTabLayout;


import androidx.fragment.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
//import androidx.viewpager.widget.ViewPager;

import eu.long1.spacetablayout.SpaceTabLayout;

import com.example.fbuapplication.fragments.ComposeFragment;
import com.example.fbuapplication.fragments.InboxFragment;
import com.example.fbuapplication.fragments.MainWallFragment;
import com.example.fbuapplication.fragments.ProfileFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivitySpace extends AppCompatActivity {


    SpaceTabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_space);


        //add the fragments you want to display in a List
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new MainWallFragment());
        fragmentList.add(new InboxFragment());
        fragmentList.add(new ComposeFragment());
        fragmentList.add(new ProfileFragment());

        CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.viewPager);
        tabLayout = (SpaceTabLayout) findViewById(R.id.spaceTabLayout);

        //we need the savedInstanceState to get the position
        tabLayout.initialize(viewPager, getSupportFragmentManager(),
                fragmentList, savedInstanceState);


    }


    //we need the outState to save the position
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        tabLayout.saveState(outState);
        super.onSaveInstanceState(outState);
    }
}