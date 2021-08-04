package com.example.fbuapplication.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.baoyz.widget.PullRefreshLayout;
import com.example.fbuapplication.ParseModels.FriendRequest;
import com.example.fbuapplication.R;
import com.example.fbuapplication.adapters.FriendRequestsAdapter;
import com.example.fbuapplication.adapters.FriendsAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class FriendsRequestListActivity extends AppCompatActivity {

    protected FriendRequestsAdapter adapter;
    protected List<FriendRequest> allFriendRequests;
    protected FriendsAdapter adapterfriends;
    protected List<FriendRequest> allFriends;
    private RecyclerView rvFriendRequest;
    private RecyclerView rvFriends;
    private TextView tvFriendListTitle;
    private PullRefreshLayout pullRefreshLayout;

    private boolean shouldDelete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_request_list);

        rvFriendRequest = findViewById(R.id.rvFriendRequest);
        rvFriends = findViewById(R.id.rvFriends);
        tvFriendListTitle = findViewById(R.id.tvInboxTitle);

        pullRefreshLayout = findViewById(R.id.pullRefreshLayout);

        shouldDelete = true;
        allFriendRequests = new ArrayList<>();
        allFriends = new ArrayList<>();

        adapter = new FriendRequestsAdapter(this, allFriendRequests);

        adapterfriends = new FriendsAdapter(this, allFriends);

        if (ParseUser.getCurrentUser().get("full_name") == null) {
            ParseUser.getCurrentUser().put("full_name", "User");
        }

        rvFriendRequest.setAdapter(adapter);

        rvFriendRequest.setLayoutManager(new GridLayoutManager(this, 1));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvFriendRequest.getContext(), 1);

        rvFriendRequest.addItemDecoration(dividerItemDecoration);
        rvFriendRequest.smoothScrollToPosition(0);

        rvFriends.setAdapter(adapterfriends);

        rvFriends.setLayoutManager(new GridLayoutManager(this, 1));
        DividerItemDecoration dividerItemDecoration2 = new DividerItemDecoration(rvFriends.getContext(), 1);

        rvFriends.addItemDecoration(dividerItemDecoration2);
        rvFriends.smoothScrollToPosition(0);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                shouldDelete = true;
                FriendRequest deletedFriendRequest = allFriendRequests.get(viewHolder.getAdapterPosition());

                int position = viewHolder.getAdapterPosition();

                allFriendRequests.remove(viewHolder.getAdapterPosition());

                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                Snackbar snack = Snackbar.make(rvFriendRequest, deletedFriendRequest.getStatus(), Snackbar.LENGTH_SHORT).setAction("Undo Delete", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        allFriendRequests.add(position, deletedFriendRequest);

                        adapter.notifyItemInserted(position);
                        shouldDelete = false;
                    }

                });
                snack.show();
                snack.addCallback(new Snackbar.Callback() {

                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT || event == Snackbar.Callback.DISMISS_EVENT_ACTION || event == Snackbar.Callback.DISMISS_EVENT_ACTION || event == Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE || event == Snackbar.Callback.DISMISS_EVENT_SWIPE) {

                            if (shouldDelete) {

                                deletedFriendRequest.setStatus("denied");
                                deletedFriendRequest.saveInBackground();

                            }
                        }
                    }

                    @Override
                    public void onShown(Snackbar snackbar) {
                        shouldDelete = true;
                    }
                });

            }

        }).attachToRecyclerView(rvFriendRequest);

        queryFriendRequests();

        int[] color = {R.color.teal_200, R.color.teal_400, R.color.teal_700, R.color.purple_500};
        pullRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_WATER_DROP);

        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                fetchTimelineAsync(0);
            }
        });

        pullRefreshLayout.setRefreshing(false);

    }

    public void fetchTimelineAsync(int page) {

        adapter.clear();
        queryFriendRequests();

        adapterfriends.clear();
        queryFriends();

        pullRefreshLayout.setRefreshing(false);

    }

    protected void queryFriendRequests() {

        ParseQuery<FriendRequest> query = ParseQuery.getQuery(FriendRequest.class);

        query.whereEqualTo(FriendRequest.KEY_TOUSER, ParseUser.getCurrentUser());
        query.whereEqualTo(FriendRequest.KEY_STATUS, "pending");

        query.setLimit(20);

        query.addDescendingOrder("createdAt");

        query.findInBackground(new FindCallback<FriendRequest>() {
            @Override
            public void done(List<FriendRequest> messages, ParseException e) {

                if (e != null) {
                    Snackbar.make(rvFriendRequest, "Issue with getting messages. Please try again.", Snackbar.LENGTH_LONG).show();
                    return;
                } else {

                }
                allFriendRequests.clear();
                allFriendRequests.addAll(messages);
                adapter.notifyDataSetChanged();

            }
        });
    }

    protected void queryFriends() {

        ParseQuery<FriendRequest> query = ParseQuery.getQuery(FriendRequest.class);
        ArrayList<String> messagesForUser = new ArrayList<>();

        query.whereEqualTo(FriendRequest.KEY_TOUSER, ParseUser.getCurrentUser());
        query.whereEqualTo(FriendRequest.KEY_STATUS, "accepted");

        query.setLimit(20);

        query.addDescendingOrder("createdAt");

        query.findInBackground(new FindCallback<FriendRequest>() {
            @Override
            public void done(List<FriendRequest> messages, ParseException e) {

                if (e != null) {
                    Snackbar.make(rvFriends, "Issue with getting messages. Please try again.", Snackbar.LENGTH_LONG).show();
                    return;
                } else {

                }
                allFriends.clear();
                allFriends.addAll(messages);
                adapterfriends.notifyDataSetChanged();

            }
        });
    }
}