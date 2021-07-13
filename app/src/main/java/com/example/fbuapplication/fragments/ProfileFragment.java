package com.example.fbuapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fbuapplication.Message;
import com.example.fbuapplication.MessagesInboxAdapter;
import com.example.fbuapplication.R;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment{

    public static final String TAG = "profileFragment";
    private ImageView ivProfileImage;
    private TextView tvUsername;
    private  TextView tvNumSent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_profile,parent, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        tvUsername = view.findViewById(R.id.tvUsername);
        tvNumSent = view.findViewById(R.id.tvNumSent);


    }


}
