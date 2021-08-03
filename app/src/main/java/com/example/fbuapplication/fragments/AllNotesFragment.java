package com.example.fbuapplication.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.fbuapplication.adapters.MessagesMainWallAdapter;
import com.example.fbuapplication.R;

import com.example.fbuapplication.ParseModels.Message;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.widget.TextView;


import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;
import androidx.appcompat.widget.SearchView;

public class AllNotesFragment extends Fragment {

    private RecyclerView rvMainWallMessages;
    protected MessagesMainWallAdapter adapter;
    protected List<Message> allMessages;

    private SwipeRefreshLayout swipeContainer;
    private TextView tvWallTitle;
    private Toolbar toolbar;


    public static final String TAG = "FeedActivity";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_main_wall,parent, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_main_wall, menu);

        final MenuItem itemSearch = menu.findItem(R.id.action_search);
        final MenuItem itemSelectWall = menu.findItem(R.id.action_choosewall);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(itemSearch);


        onOptionsItemSelected(itemSelectWall);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final List<Message> filteredModelList = filter(allMessages, newText);
                adapter.setFilter(filteredModelList);
                return true;
            }
        });


        itemSearch.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem itemSearch) {
                // Do something when collapsed
                adapter.setFilter(allMessages);
                return true; // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem itemSearch) {
                // Do something when expanded
                return true; // Return true to expand action view
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_kudos:

                return true;

            case R.id.action_memories:

                return true;

            case R.id.action_goals:

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private List<Message> filter(List<Message> models, String query) {
        query = query.toLowerCase();

        final List<Message> filteredModelList = new ArrayList<>();
        for (Message model : models) {
            final String text = model.getMessageBody().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

//    private void filter(String text) {
//        // creating a new array list to filter our data.
//        ArrayList<Message> filteredlist = new ArrayList<>();
//
//        // running a for loop to compare elements.
//        for (Message itemSearch : allMessages) {
//            // checking if the entered string matched with any itemSearch of our recycler view.
//            if (itemSearch.getMessageBody().toLowerCase().contains(text.toLowerCase())) {
//                // if the itemSearch is matched we are
//                // adding it to our filtered list.
//                filteredlist.add(itemSearch);
//            }
//        }
//        if (filteredlist.isEmpty()) {
//            // if no itemSearch is added in filtered list we are
//            // displaying a toast message as no data found.
//            Toast.makeText(requireActivity(), "No Data Found..", Toast.LENGTH_SHORT).show();
//        } else {
//            // at last we are passing that filtered
//            // list to our adapter class.
//            adapter.filterList(filteredlist);
//        }
//    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        setHasOptionsMenu(true);


        rvMainWallMessages = view.findViewById(R.id.rvMainWallMessages);
        tvWallTitle = view.findViewById(R.id.tvWallTitle);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        allMessages = new ArrayList<>();
        //adapter = new MessagesAllNotesAdapter(getContext(), allMessages);
        adapter  = new MessagesMainWallAdapter(getContext(), allMessages, new MessagesMainWallAdapter.MainWallInterface(){
            // Listen to the callback method of adapter
            public int onWork() {
                return 0;
            }
        });
//        tvWallTitle.setText(ParseUser.getCurrentUser().getUsername().toString()+ "\'s Main Wall");

        if(ParseUser.getCurrentUser().get("full_name")==null){
            ParseUser.getCurrentUser().put("full_name","User");
        }
        tvWallTitle.setText(ParseUser.getCurrentUser().get("full_name").toString()+ "\'s Main Wall");



        // set the adapter on the recycler view
        rvMainWallMessages.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvMainWallMessages.setLayoutManager(new GridLayoutManager(getContext(), 2));

        if(ParseUser.getCurrentUser().get("num_messages_sent")==null){
            ParseUser.getCurrentUser().put("num_messages_sent", 0);
        }
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
        ArrayList<String> messagesPinnedByUser = new ArrayList<>();
//        // include data referred by user key
        //query.include(Message.KEY_RECIEVER);
        query.whereEqualTo(Message.KEY_RECIEVER, ParseUser.getCurrentUser());
        query.whereEqualTo(Message.KEY_ISPINNED, true);
        // limit query to latest 20 itemSearchs
        query.setLimit(20);
        // order messages by creation date (newest first)
        query.addDescendingOrder("createdAt");
        // start an asynchronous call for messages
        query.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> messages, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting messages", e);
                    Snackbar.make(rvMainWallMessages, "Issue with getting messages. Please try again.", Snackbar.LENGTH_LONG).show();
                    return;
                }


                // for debugging purposes let's print every message description to logcat
                for (Message message : messages) {
                    Log.i(TAG, "InboxMessage: " + message.getMessageBody() + "sent to: " + ParseUser.getCurrentUser().getUsername());
                    //ParseUser.getCurrentUser().add("main_wall_notes", message.getMessageBody());
                    //ParseUser.getCurrentUser().add("main_wall_messages",message);
                    messagesPinnedByUser.add(message.getMessageBody());

                    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e != null){
                                Log.e(TAG, "Error while saving new messages",e);

                                // If the Activity is gone (the user navigated elsewhere), getActivity() returns null.
                                Activity activity = getActivity();
                                if(activity != null){

                                    //Toast.makeText(requireActivity(), "Error while retrieving new messages!", Toast.LENGTH_SHORT).show();
                                    Snackbar.make(rvMainWallMessages, "Error while retrieving new messages!", Snackbar.LENGTH_LONG).show();


                                }
                            }
                            Log.i(TAG, "Profile picture upload was successful!");
                        }
                    });


                }



                // save received messages to list and notify adapter of new data
                allMessages.addAll(messages);
                adapter.notifyDataSetChanged();
            }
        });
    }





}