package com.example.fbuapplication.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baoyz.widget.PullRefreshLayout;
import com.example.fbuapplication.ParseModels.GroupToMembers;
import com.example.fbuapplication.R;
import com.example.fbuapplication.adapters.GroupsAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

//class to view all groups
public class GroupActivity extends AppCompatActivity {

    protected GroupsAdapter adapter;
    protected List<GroupToMembers> allGroup;
    private RecyclerView rvGroup;
    private TextView tvGroupListTitle;
    private PullRefreshLayout pullRefreshLayout;

    private boolean shouldDelete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        rvGroup = findViewById(R.id.rvGroupRequest);
        tvGroupListTitle = findViewById(R.id.tvGroupListTitle);

        pullRefreshLayout = findViewById(R.id.pullRefreshLayout);

        shouldDelete = true;
        allGroup = new ArrayList<GroupToMembers>();

        adapter = new GroupsAdapter(this, allGroup);

        if (ParseUser.getCurrentUser().get("full_name") == null) {
            ParseUser.getCurrentUser().put("full_name", "User");
        }
        tvGroupListTitle.setText(ParseUser.getCurrentUser().get("full_name").toString() + "'s Groups");

        rvGroup.setAdapter(adapter);

        rvGroup.setLayoutManager(new GridLayoutManager(this, 1));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvGroup.getContext(), 1);

        rvGroup.addItemDecoration(dividerItemDecoration);
        rvGroup.smoothScrollToPosition(0);

        queryGroups();

        int[] color = {R.color.teal_200, R.color.teal_400, R.color.teal_700, R.color.purple_500};
        pullRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_WATER_DROP);

        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                fetchTimelineAsync();
            }
        });

        pullRefreshLayout.setRefreshing(false);

    }

    public void fetchTimelineAsync() {

        adapter.clear();
        queryGroups();

        pullRefreshLayout.setRefreshing(false);

    }

    protected void queryGroups() {

        ParseQuery<GroupToMembers> query = ParseQuery.getQuery("GroupToMembers");
        query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());

        ArrayList<GroupToMembers> toAddGroups = new ArrayList<>();

        query.setLimit(20);

        query.addDescendingOrder("createdAt");

        query.findInBackground(new FindCallback<GroupToMembers>() {
            @Override
            public void done(List<GroupToMembers> groups, ParseException e) {

                if (e != null) {
                    Snackbar.make(rvGroup, "Issue with getting groups. Please try again.", Snackbar.LENGTH_LONG).show();
                    return;
                } else {
                    Snackbar.make(rvGroup, "All groups shown!", Snackbar.LENGTH_LONG).show();
                    for (GroupToMembers g : groups) {

                        toAddGroups.add(g);
                    }
                }

                allGroup.addAll(toAddGroups);
                adapter.notifyDataSetChanged();

            }
        });
    }

}