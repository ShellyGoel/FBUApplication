package com.example.fbuapplication.fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fbuapplication.Message;
import com.example.fbuapplication.MessagesInboxAdapter;
import com.example.fbuapplication.R;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class InboxFragment extends Fragment {

    private RecyclerView rvInboxMessages;
    protected MessagesInboxAdapter adapter;
    protected List<Message> allMessages;
    private TextView tvInboxTitle;

    private SwipeRefreshLayout swipeContainer;


    public static final String TAG = "InboxFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_inbox,parent, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);


        rvInboxMessages = view.findViewById(R.id.rvInboxMessages);
        tvInboxTitle = view.findViewById(R.id.tvInboxTitle);


        allMessages = new ArrayList<>();
        adapter = new MessagesInboxAdapter(getContext(), allMessages);
        //tvInboxTitle.setText(ParseUser.getCurrentUser().getUsername().toString() + "\'s Inbox");

        if(ParseUser.getCurrentUser().get("full_name")==null){
            ParseUser.getCurrentUser().put("full_name","User");
        }
        tvInboxTitle.setText(ParseUser.getCurrentUser().get("full_name").toString() + "\'s Inbox");



        // set the adapter on the recycler view
        rvInboxMessages.setAdapter(adapter);
        // set the layout manager on the recycler view
        rvInboxMessages.setLayoutManager(new GridLayoutManager(getContext(), 1));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvInboxMessages.getContext(), 1);
        rvInboxMessages.addItemDecoration(dividerItemDecoration);
        rvInboxMessages.smoothScrollToPosition(0);

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
                Message deletedCourse = allMessages.get(viewHolder.getAdapterPosition());

                // below line is to get the position
                // of the item at that position.
                int position = viewHolder.getAdapterPosition();

                // this method is called when item is swiped.
                // below line is to remove item from our array list.
                allMessages.remove(viewHolder.getAdapterPosition());

                // below line is to notify our item is removed from adapter.
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                // below line is to display our snackbar with action.
                Snackbar.make(rvInboxMessages, deletedCourse.getMessageBody(), Snackbar.LENGTH_LONG).setAction("Undo Delete", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // adding on click listener to our action of snack bar.
                        // below line is to add our item to array list with a position.
                        allMessages.add(position, deletedCourse);

                        // below line is to notify item is
                        // added to our adapter class.
                        adapter.notifyItemInserted(position);
                    }
                }).show();
                //TODO: Update User inbox_message arrays by removing deleted inbox message!
            }
            // at last we are adding this
            // to our recycler view.
        }).attachToRecyclerView(rvInboxMessages);


        // query messages from Parstagram
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
        ArrayList<String> messagesForUser = new ArrayList<>();
//        // include data referred by user key
        //query.include(Message.KEY_RECIEVER);
        query.whereEqualTo(Message.KEY_RECIEVER, ParseUser.getCurrentUser());
        // limit query to latest 20 items
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
                    return;
                }


                // for debugging purposes let's print every message description to logcat
                for (Message message : messages) {
                    Log.i(TAG, "InboxMessage: " + message.getMessageBody() + "sent to: " + ParseUser.getCurrentUser().getUsername());
                    ParseUser.getCurrentUser().add("user_inbox", message.getMessageBody());
                    ParseUser.getCurrentUser().add("inbox_messages",message);
                    messagesForUser.add(message.getMessageBody());

                    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e != null){
                                Log.e(TAG, "Error while saving new messages",e);

                                if(getActivity() != null){
                                    Toast.makeText(requireActivity(), "Error while retrieving new messages!", Toast.LENGTH_SHORT).show();
                                }
                                 }
                            Log.i(TAG, "Profile picture upload was successful!");
                        }
                    });

//                    try {
//                        //TODO: maybe change to background save?
//                        ParseUser.getCurrentUser().save();
//                    } catch (ParseException parseException) {
//                        parseException.printStackTrace();
//                    }

                }

                // save received messages to list and notify adapter of new data
                allMessages.addAll(messages);
                adapter.notifyDataSetChanged();
            }
        });
    }
}