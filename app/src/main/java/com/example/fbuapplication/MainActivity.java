package com.example.fbuapplication;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.fbuapplication.fragments.ComposeFragment;
import com.example.fbuapplication.fragments.InboxFragment;
import com.example.fbuapplication.fragments.MainWallFragment;
import com.example.fbuapplication.fragments.ProfileFragment;
import com.facebook.AccessToken;
import com.facebook.LoginStatusCallback;
import com.facebook.login.LoginManager;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
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

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;



import eu.long1.spacetablayout.SpaceTabLayout;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";


    private BottomNavigationView bottomNavigationView;
    SpaceTabLayout tabLayout;
    private static final String DEFAULT_FB_APP_ID = "826542378066880";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        final FragmentManager fragmentManager = getSupportFragmentManager();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (getResources().getString(R.string.facebook_app_id).equals(DEFAULT_FB_APP_ID)) {
//            showAlertNoFacebookAppId();
//            return;
//        }


        //add the fragments you want to display in a List
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new MainWallFragment());
        fragmentList.add(new InboxFragment());
        fragmentList.add(new ComposeFragment());
        fragmentList.add(new ProfileFragment());


        //bottomNavigationView = findViewById(R.id.bottom_navigation);
        CustomViewPager viewPager = (CustomViewPager) findViewById(R.id.viewPager);
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


//        // User was previously logged in, can log them in directly here.
//        // If this callback is called, a popup notification appears
//        LoginManager.getInstance()
//                .retrieveLoginStatus(
//                        this,
//                        new LoginStatusCallback() {
//                            @Override
//                            public void onCompleted(AccessToken accessToken) {
//                                Snackbar snackbar = Snackbar.make(tabLayout, "User Logged in", 2);
//                                snackbar.show();
//                            }
//
//                            @Override
//                            public void onFailure() {
//                                // If MainActivity is reached without the user being logged in,
//                                // redirect to the Login Activity
//                                if (AccessToken.getCurrentAccessToken() == null) {
//                                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
//                                    startActivity(loginIntent);
//                                }
//                            }
//
//                            @Override
//                            public void onError(Exception exception) {
//                                // Handle exception
//                            }
//                        });
//
//    }
//
//    private void showAlertNoFacebookAppId() {
//        AlertDialog alert = new AlertDialog.Builder(MainActivity.this).create();
//        alert.setTitle("Use your facebook app id in strings.xml");
//        alert.setMessage(
//                "This sample app can not properly function without your app id. "
//                        + "Use your facebook app id in strings.xml. Check out https://developers.facebook.com/docs/android/getting-started/ for more info. "
//                        + "Restart the app after that");
//        alert.show();
//    }

    }

}