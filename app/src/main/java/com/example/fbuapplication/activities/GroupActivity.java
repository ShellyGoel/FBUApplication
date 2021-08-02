package com.example.fbuapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.example.fbuapplication.ParseModels.Group;
import com.example.fbuapplication.R;
import com.example.fbuapplication.adapters.GroupsAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GroupActivity extends AppCompatActivity {


    private RecyclerView rvGroup;
    protected GroupsAdapter adapter;
    protected List<Group> allGroup;


    private TextView tvGroupListTitle;
    private PullRefreshLayout pullRefreshLayout;

    private boolean shouldDelete = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        rvGroup = findViewById(R.id.rvGroupRequest);
        tvGroupListTitle = findViewById(R.id.tvGroupListTitle);

        pullRefreshLayout = (PullRefreshLayout) findViewById(R.id.pullRefreshLayout);


        shouldDelete = true;
        allGroup = new ArrayList<>();


        adapter = new GroupsAdapter(this, allGroup);

        if(ParseUser.getCurrentUser().get("full_name")==null){
            ParseUser.getCurrentUser().put("full_name","User");
        }
        tvGroupListTitle.setText(ParseUser.getCurrentUser().get("full_name").toString() + "\'s Groups");

        // set the adapter on the recycler view
        rvGroup.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvGroup.setLayoutManager(new GridLayoutManager(this, 1));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvGroup.getContext(), 1);

        rvGroup.addItemDecoration(dividerItemDecoration);
        rvGroup.smoothScrollToPosition(0);

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
                Group deletedGroup = allGroup.get(viewHolder.getAdapterPosition());


                // below line is to get the position
                // of the item at that position.
                int position = viewHolder.getAdapterPosition();

                // this method is called when item is swiped.
                // below line is to remove item from our array list.
                allGroup.remove(viewHolder.getAdapterPosition());



                // below line is to notify our item is removed from adapter.
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                // below line is to display our snackbar with action.
                Snackbar snack = Snackbar.make(rvGroup, deletedGroup.getGroupName(), Snackbar.LENGTH_SHORT).setAction("Undo Delete", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        allGroup.add(position, deletedGroup);

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

//                                deletedGroup.setStatus("denied");
//                                deletedGroup.saveInBackground();
//                                ParseQuery<ParseObject> query = ParseQuery.getQuery("Group");
//                                //query.whereEqualTo("receiver", ParseUser.getCurrentUser());
//                                query.whereEqualTo("objectId", deletedGroup.getObjectId());
//                                query.findInBackground(new FindCallback<ParseObject>() {
//                                    public void done(List<ParseObject> groups, ParseException e) {
//                                        if (e == null) {
//
//                                            // iterate over all groups and delete them
//                                            for (ParseObject message : groups) {
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
        }).attachToRecyclerView(rvGroup);

        // query groups from Parstagram
        queryGroups();

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
        queryGroups();
        // Now we call setRefreshing(false) to signal refresh has finished
        //


        pullRefreshLayout.setRefreshing(false);
        //swipeContainer.setRefreshing(false);




    }



    protected void queryGroups() {
        // specify what type of data we want to query - Group.class

        Log.e("SHELL","WH");
        ParseQuery<Group> query = ParseQuery.getQuery("Group");
        ArrayList<String> groupsForUser = new ArrayList<>();

        JSONArray allGroupNames = ParseUser.getCurrentUser().getJSONArray("groupsIn");
        Log.e("SHELL","WH");
        if(allGroupNames == null) {
            Log.e("SHELL","NULL");
            return;
        }

        String[] groupnames = new String[allGroupNames.length()];
        Log.e("SHELL","WH");
//        for(int i = 0; i < allGroupNames.length;i ++){
//
//            try {
//                groupnames[i] = (String) allGroupNames;
//                Log.i("GROUPACTIVITY", groupnames[i]);
//            }
//            catch (JSONException e){
//
//            }
//        }


       // query.whereContainedIn(Group.KEY_GROUPNAME, Arrays.asList(allGroupNames));
        Log.e("SHELL","WH");
        Log.e("SHELL","WH");
        query.whereEqualTo(Group.KEY_GROUPNAME,"shell");
        // limit query to latest 20 items
        query.setLimit(20);
        // order groups by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for groups
        query.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(List<Group> groups, ParseException e) {
                // check for errors
                if (e != null) {
                    Snackbar.make(rvGroup, "Issue with getting groups. Please try again.", Snackbar.LENGTH_LONG).show();
                    return;
                }

                else{
                    Snackbar.make(rvGroup, "Got a group: "+groups.get(0).getGroupName(), Snackbar.LENGTH_LONG).show();

                }


                allGroup.addAll(groups);
                adapter.clear();
                //added this

                allGroup.addAll(groups);
                adapter.notifyDataSetChanged();

            }
        });
    }

}