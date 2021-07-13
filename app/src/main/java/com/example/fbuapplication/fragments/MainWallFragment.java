package com.example.fbuapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fbuapplication.MessagesMainWallAdapter;
import com.example.fbuapplication.R;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fbuapplication.Message;
import com.example.fbuapplication.MessagesInboxAdapter;
import com.example.fbuapplication.R;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.widget.TextView;


import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class MainWallFragment extends Fragment {

    private RecyclerView rvMainWallMessages;
    protected MessagesMainWallAdapter adapter;
    protected List<Message> allMessages;

    private SwipeRefreshLayout swipeContainer;
    private TextView tvWallTitle;


    public static final String TAG = "FeedActivity";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_main_wall,parent, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);


        rvMainWallMessages = view.findViewById(R.id.rvMainWallMessages);
        tvWallTitle = view.findViewById(R.id.tvWallTitle);

        allMessages = new ArrayList<>();
        adapter = new MessagesMainWallAdapter(getContext(), allMessages);

        tvWallTitle.setText(ParseUser.getCurrentUser().getUsername().toString()+ "\'s Main Wall");

        // set the adapter on the recycler view
        rvMainWallMessages.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvMainWallMessages.setLayoutManager(new GridLayoutManager(getContext(), 3));
        // query posts from Parstagram
        queryMessages();

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.

        adapter.clear();
        queryMessages();
        // Now we call setRefreshing(false) to signal refresh has finished
        swipeContainer.setRefreshing(false);


    }



    protected void queryMessages() {
        // specify what type of data we want to query - Message.class
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        // include data referred by user key
        query.include(Message.KEY_SENDER);
        // limit query to latest 20 items
        query.setLimit(20);
        // order posts by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for posts
        query.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> posts, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting posts", e);
                    return;
                }

                // for debugging purposes let's print every post description to logcat
                for (Message post : posts) {
                    Log.i(TAG, "MainWallMessage: " + post.getMessageBody() + ", username: " + post.getSender().getUsername());
                }

                // save received posts to list and notify adapter of new data
                allMessages.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }
}