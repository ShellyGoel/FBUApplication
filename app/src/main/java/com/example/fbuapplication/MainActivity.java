package com.example.fbuapplication;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.fbuapplication.fragments.ComposeFragment;
import com.example.fbuapplication.fragments.InboxFragment;
import com.example.fbuapplication.fragments.MainWallFragment;
import com.example.fbuapplication.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import eu.long1.spacetablayout.SpaceTabLayout;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";



    private BottomNavigationView bottomNavigationView;
    SpaceTabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        final FragmentManager fragmentManager = getSupportFragmentManager();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //add the fragments you want to display in a List
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new MainWallFragment());
        fragmentList.add(new InboxFragment());
        fragmentList.add(new ComposeFragment());
        fragmentList.add(new ProfileFragment());


        //bottomNavigationView = findViewById(R.id.bottom_navigation);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (SpaceTabLayout) findViewById(R.id.spaceTabLayout);


        tabLayout.initialize(viewPager, getSupportFragmentManager(),
                fragmentList, savedInstanceState);
//
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
//                Fragment fragment;
//                switch (item.getItemId()) {
//                    case R.id.action_profile:
//                        // do something here
//                        fragment = new ProfileFragment();
//                        break;
//                    case R.id.action_compose:
//                        // do something here
//                        fragment = new ComposeFragment();
//                        break;
//                    case R.id.action_inbox:
//                        // do something here
//                        fragment = new InboxFragment();
//                        break;
//
//                    case R.id.action_main_wall:
//                        fragment = new MainWallFragment();
//
//                    default:
//                        fragment = new MainWallFragment();
//                        break;
//                }
//
//                fragmentManager.beginTransaction().replace(R.id.flContainer,fragment).commit();
//                return true;
//
//
//            }
//        });
//
//        //set default selection
//        bottomNavigationView.setSelectedItemId(R.id.action_main_wall);
//
//
//    }
    }

}