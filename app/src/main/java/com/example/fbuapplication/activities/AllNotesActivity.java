package com.example.fbuapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.TextView;

import com.example.fbuapplication.R;
import com.example.fbuapplication.adapters.MessagesAllNotesAdapter;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AllNotesActivity extends AppCompatActivity {




    protected MessagesAllNotesAdapter adapter;
    protected List<Integer> allMessages;

    private SwipeRefreshLayout swipeContainer;
    private TextView tvWallTitle;
    private Toolbar toolbar;
    //RecyclerView rvAllNotes;
    private RecyclerView rvAllNotes;

    public static final String TAG = "FeedActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notes);



        rvAllNotes = findViewById(R.id.rvAllNotes);
        tvWallTitle = findViewById(R.id.tvWallTitle);
        allMessages = new ArrayList<>();
        //adapter = new MessagesAllNotesAdapter(getContext(), allMessages);
        adapter  = new MessagesAllNotesAdapter(this, allMessages);
        if(ParseUser.getCurrentUser().get("full_name")==null){
            ParseUser.getCurrentUser().put("full_name","User");
        }
        tvWallTitle.setText(ParseUser.getCurrentUser().get("full_name").toString()+ "\'s Sent Notes!");



        // set the adapter on the recycler view
        rvAllNotes.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvAllNotes.setLayoutManager(new GridLayoutManager(this, 5));

        if(ParseUser.getCurrentUser().get("num_messages_sent")==null){
            ParseUser.getCurrentUser().put("num_messages_sent", 0);
        }
        // query posts from Parstagram
        queryMessages();



        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
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


        int numNotes = (int) ParseUser.getCurrentUser().get("num_messages_sent");
        Integer[] myIntArray = new Integer[numNotes];
        for(int i = 0; i<numNotes; i++){
            myIntArray[i] = i+1;
        }
        // save received messages to list and notify adapter of new data
                allMessages.addAll(Arrays.asList(myIntArray));
                adapter.notifyDataSetChanged();
            }



}