package com.example.fbuapplication.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.fbuapplication.R;
import com.example.fbuapplication.adapters.MessagesAllNotesAdapter;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AllNotesActivity extends AppCompatActivity {

    public static final String TAG = "FeedActivity";
    protected MessagesAllNotesAdapter adapter;
    protected List<Integer> allMessages;
    private SwipeRefreshLayout swipeContainer;
    private TextView tvWallTitle;
    private RecyclerView rvAllNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notes);

        rvAllNotes = findViewById(R.id.rvAllNotes);
        tvWallTitle = findViewById(R.id.tvWallTitle);
        allMessages = new ArrayList<>();

        adapter = new MessagesAllNotesAdapter(this, allMessages);
        if (ParseUser.getCurrentUser().get("full_name") == null) {
            ParseUser.getCurrentUser().put("full_name", "User");
        }
        tvWallTitle.setText(ParseUser.getCurrentUser().get("full_name").toString() + "'s Sent Notes!");

        rvAllNotes.setAdapter(adapter);

        rvAllNotes.setLayoutManager(new GridLayoutManager(this, 4));

        if (ParseUser.getCurrentUser().get("num_messages_sent") == null) {
            ParseUser.getCurrentUser().put("num_messages_sent", 0);
        }

        queryMessages();

        swipeContainer = findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimelineAsync(0);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void fetchTimelineAsync(int page) {

        adapter.clear();
        queryMessages();

        swipeContainer.setRefreshing(false);

    }

    protected void queryMessages() {

        int numNotes = (int) ParseUser.getCurrentUser().get("num_messages_sent");
        Integer[] myIntArray = new Integer[numNotes];
        for (int i = 0; i < numNotes; i++) {
            myIntArray[i] = i + 1;
        }

        allMessages.addAll(Arrays.asList(myIntArray));
        adapter.notifyDataSetChanged();
    }

}