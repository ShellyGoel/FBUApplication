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
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class InboxFragment extends Fragment {

    private RecyclerView rvInboxMessages;
    protected MessagesInboxAdapter adapter;
    protected List<Message> allMessages;
    private TextView tvInboxTitle;
    private boolean shouldDelete;
    private String shouldDeleteString;

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

        shouldDelete = true;
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
                shouldDelete = true;
                Message deletedMessage = allMessages.get(viewHolder.getAdapterPosition());

                // below line is to get the position
                // of the item at that position.
                int position = viewHolder.getAdapterPosition();

                // this method is called when item is swiped.
                // below line is to remove item from our array list.
                allMessages.remove(viewHolder.getAdapterPosition());

                //remove deletedMessage from user's inbox in Parse! (from inbox_messages)

                //Steps:
                //1) Get User's inbox messages
                //2) Find Message with ObjectId() of deleted message
                //3) Remove that message from array



//
//                //TODO: Probably don't need this fields. May make more efficient though.
//                List<Message> user_inbox_messages = (List<Message>) ParseUser.getCurrentUser().get("inbox_messages");
//                // for debugging purposes let's print every message description to logcat
//                user_inbox_messages.remove(deletedMessage);
//                ParseUser.getCurrentUser().put("inbox_messages",user_inbox_messages);
//
//                    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
//                        @Override
//                        public void done(ParseException e) {
//                            if(e != null){
//                                Log.e(TAG, "Error while saving new messages",e);
//
//                                if(getActivity() != null){
//                                    //Toast.makeText(requireActivity(), "Error while retrieving new messages!", Toast.LENGTH_SHORT).show();
//                                    Snackbar.make(rvInboxMessages, "Error while retrieving new messages!", Snackbar.LENGTH_LONG).show();
//
//                                }
//                            }
//                            Log.i(TAG, "Deleted message successfully from Parse (for inbox)");
//                        }
//                    });
//
//


                // below line is to notify our item is removed from adapter.
                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                // below line is to display our snackbar with action.
                Snackbar snack = Snackbar.make(rvInboxMessages, deletedMessage.getMessageBody(), Snackbar.LENGTH_SHORT).setAction("Undo Delete", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        allMessages.add(position, deletedMessage);

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
                                ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
                                //query.whereEqualTo("receiver", ParseUser.getCurrentUser());
                                query.whereEqualTo("objectId", deletedMessage.getObjectId());
                                query.findInBackground(new FindCallback<ParseObject>() {
                                    public void done(List<ParseObject> messages, ParseException e) {
                                        if (e == null) {

                                            // iterate over all messages and delete them
                                            for (ParseObject message : messages) {
                                                //message.deleteEventually();
                                                message.deleteInBackground();
                                            }
                                        } else {
                                            Log.d(TAG, e.getMessage());
                                        }
                                    }
                                });

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
                    Snackbar.make(rvInboxMessages, "Issue with getting messages. Please try again.", Snackbar.LENGTH_LONG).show();
                    return;
                }

                ParseUser.getCurrentUser().put("num_messages_sent", messages.size());


//                // for debugging purposes let's print every message description to logcat
//                for (Message message : messages) {
//                    Log.i(TAG, "InboxMessage: " + message.getMessageBody() + "sent to: " + ParseUser.getCurrentUser().getUsername());
//                    //ParseUser.getCurrentUser().add("user_inbox", message.getMessageBody());
//                    //ParseUser.getCurrentUser().add("inbox_messages",message);
//                    messagesForUser.add(message.getMessageBody());
//
//                    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
//                        @Override
//                        public void done(ParseException e) {
//                            if(e != null){
//                                Log.e(TAG, "Error while saving new messages",e);
//
//                                if(getActivity() != null){
//                                    //Toast.makeText(requireActivity(), "Error while retrieving new messages!", Toast.LENGTH_SHORT).show();
//                                    Snackbar.make(rvInboxMessages, "Error while retrieving new messages!", Snackbar.LENGTH_LONG).show();
//
//                                }
//                                 }
//                            Log.i(TAG, "Profile picture upload was successful!");
//                        }
//                    });
//
//                }

                // save received messages to list and notify adapter of new data
                allMessages.addAll(messages);
                adapter.notifyDataSetChanged();
            }
        });
    }
}