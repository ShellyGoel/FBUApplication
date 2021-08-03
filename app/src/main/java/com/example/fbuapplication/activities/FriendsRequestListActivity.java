package com.example.fbuapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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


    private RecyclerView rvFriendRequest;
    protected FriendRequestsAdapter adapter;
    protected List<FriendRequest> allFriendRequests;

    private RecyclerView rvFriends;
    protected FriendsAdapter adapterfriends;
    protected List<FriendRequest> allFriends;


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

        pullRefreshLayout = (PullRefreshLayout) findViewById(R.id.pullRefreshLayout);


        shouldDelete = true;
        allFriendRequests = new ArrayList<>();
        allFriends = new ArrayList<>();

        adapter = new FriendRequestsAdapter(this, allFriendRequests);

        adapterfriends = new FriendsAdapter(this, allFriends);



        if(ParseUser.getCurrentUser().get("full_name")==null){
            ParseUser.getCurrentUser().put("full_name","User");
        }
        //tvFriendListTitle.setText(ParseUser.getCurrentUser().get("full_name").toString() + "\'s Friend Requests");

        // set the adapter on the recycler view
        rvFriendRequest.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvFriendRequest.setLayoutManager(new GridLayoutManager(this, 1));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvFriendRequest.getContext(), 1);

        rvFriendRequest.addItemDecoration(dividerItemDecoration);
        rvFriendRequest.smoothScrollToPosition(0);

        // set the adapter on the recycler view
        rvFriends.setAdapter(adapterfriends);
        // set the layout manager on the recycler view
        rvFriends.setLayoutManager(new GridLayoutManager(this, 1));
        DividerItemDecoration dividerItemDecoration2 = new DividerItemDecoration(rvFriends.getContext(), 1);

        rvFriends.addItemDecoration(dividerItemDecoration2);
        rvFriends.smoothScrollToPosition(0);

        // on below line we are creating a method to create item touch helper
        // method for adding swipe to delete functionality.
        // in this we are specifying drag direction and position to right
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                // this method is called
                // when the item is moved.
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // this method is called when we swipe our item to right direction.
                // on below line we are getting the item at a particular position.
                shouldDelete = true;
                FriendRequest deletedFriendRequest = allFriendRequests.get(viewHolder.getAdapterPosition());


                // below line is to get the position
                // of the item at that position.
                int position = viewHolder.getAdapterPosition();

                // this method is called when item is swiped.
                // below line is to remove item from our array list.
                allFriendRequests.remove(viewHolder.getAdapterPosition());



                // below line is to notify our item is removed from adapter.
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                // below line is to display our snackbar with action.
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
                        if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT || event == Snackbar.Callback.DISMISS_EVENT_ACTION || event == Snackbar.Callback.DISMISS_EVENT_ACTION || event == Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE || event == Snackbar.Callback.DISMISS_EVENT_SWIPE )//|| event == Snackbar.Callback.DISMISS_EVENT_MANUAL)
                        {

                            if(shouldDelete) {

                                deletedFriendRequest.setStatus("denied");
                                deletedFriendRequest.saveInBackground();
//                                ParseQuery<ParseObject> query = ParseQuery.getQuery("FriendRequest");
//                                //query.whereEqualTo("receiver", ParseUser.getCurrentUser());
//                                query.whereEqualTo("objectId", deletedFriendRequest.getObjectId());
//                                query.findInBackground(new FindCallback<ParseObject>() {
//                                    public void done(List<ParseObject> messages, ParseException e) {
//                                        if (e == null) {
//
//                                            // iterate over all messages and delete them
//                                            for (ParseObject message : messages) {
//                                                //message.deleteEventually();
//                                                message.deleteInBackground();
//                                            }
//                                        } else {
//                                            Log.d("friends_request", "e: "+ e);
//                                        }
//                                    }
//                                });

                            }
                        }
                    }

                    @Override
                    public void onShown(Snackbar snackbar) {
                        shouldDelete = true;
                    }
                });




            }
            // at last we are adding this
            // to our recycler view.
        }).attachToRecyclerView(rvFriendRequest);

        // query messages from Parstagram
        queryFriendRequests();

        // listen refresh event
        int[] color =  {R.color.teal_200,R.color.teal_400,R.color.teal_700,R.color.purple_500};
        pullRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_WATER_DROP);
        //pullRefreshLayout.setColorSchemeColors(color);
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // start refresh
                fetchTimelineAsync(0);
            }
        });

        // refresh complete
        pullRefreshLayout.setRefreshing(false);

    }


    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        // getHomeTimeline is an example endpoint.

        adapter.clear();
        queryFriendRequests();
        // Now we call setRefreshing(false) to signal refresh has finished
        //
        adapterfriends.clear();
        queryFriends();


        pullRefreshLayout.setRefreshing(false);
        //swipeContainer.setRefreshing(false);




    }



    protected void queryFriendRequests() {
        // specify what type of data we want to query - FriendRequest.class
        ParseQuery<FriendRequest> query = ParseQuery.getQuery(FriendRequest.class);
        ArrayList<String> messagesForUser = new ArrayList<>();
//        // include data referred by user key
        //query.include(FriendRequest.KEY_RECIEVER);
       query.whereEqualTo(FriendRequest.KEY_TOUSER, ParseUser.getCurrentUser());
       query.whereEqualTo(FriendRequest.KEY_STATUS, "pending");

        // limit query to latest 20 items
        query.setLimit(20);
        // order messages by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for messages
        query.findInBackground(new FindCallback<FriendRequest>() {
            @Override
            public void done(List<FriendRequest> messages, ParseException e) {
                // check for errors
                if (e != null) {
                      Snackbar.make(rvFriendRequest, "Issue with getting messages. Please try again.", Snackbar.LENGTH_LONG).show();
                    return;
                }

                else{

                }
                allFriendRequests.clear();
                allFriendRequests.addAll(messages);
                adapter.notifyDataSetChanged();

            }
        });
    }



    protected void queryFriends() {
        // specify what type of data we want to query - FriendRequest.class
        ParseQuery<FriendRequest> query = ParseQuery.getQuery(FriendRequest.class);
        ArrayList<String> messagesForUser = new ArrayList<>();
//        // include data referred by user key
        //query.include(FriendRequest.KEY_RECIEVER);
        query.whereEqualTo(FriendRequest.KEY_TOUSER, ParseUser.getCurrentUser());
        query.whereEqualTo(FriendRequest.KEY_STATUS, "accepted");

        // limit query to latest 20 items
        query.setLimit(20);
        // order messages by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for messages
        query.findInBackground(new FindCallback<FriendRequest>() {
            @Override
            public void done(List<FriendRequest> messages, ParseException e) {
                // check for errors
                if (e != null) {
                    Snackbar.make(rvFriends, "Issue with getting messages. Please try again.", Snackbar.LENGTH_LONG).show();
                    return;
                }

                else{

                }
                allFriends.clear();
                allFriends.addAll(messages);
                adapterfriends.notifyDataSetChanged();

            }
        });
    }
}