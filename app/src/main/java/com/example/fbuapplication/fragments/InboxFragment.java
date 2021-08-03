package com.example.fbuapplication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alexvasilkov.foldablelayout.UnfoldableView;
import com.baoyz.widget.PullRefreshLayout;
import com.example.fbuapplication.ParseModels.Message;
import com.example.fbuapplication.adapters.MessagesInboxAdapter;
import com.example.fbuapplication.R;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.widget.TextView;


import com.example.fbuapplication.fragments.dialogFragments.DecidePinnedWallDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import android.widget.Button;

public class InboxFragment extends Fragment  implements DecidePinnedWallDialogFragment.DecidePinnedWallDialogListener {

    private RecyclerView rvInboxMessages;
    protected MessagesInboxAdapter adapter;
    protected List<Message> allMessages;
    private TextView tvInboxTitle;
    private boolean shouldDelete;
    private String shouldDeleteString;
    private UnfoldableView mUnfoldableView;
    private TextView tvInboxNewMessages;
    private SwipeRefreshLayout swipeContainer;

    private ConstraintLayout cl;
    private RecyclerView.ViewHolder inboxViewHolder;
    private PullRefreshLayout pullRefreshLayout;
    private Stack<Pair<Integer,Message>> deleteMessageStack;

    private Button undoButton;

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
        cl = view.findViewById(R.id.placeholderInbox);

         deleteMessageStack = new Stack<Pair<Integer,Message>>();

       // rvInboxMessages
//                .getViewTreeObserver()
//                .addOnGlobalLayoutListener(
//                        new ViewTreeObserver.OnGlobalLayoutListener() {
//                            @Override
//                            public void onGlobalLayout() {
//                                // At this point the layout is complete and the
//                                // dimensions of recyclerView and any child views
//                                // are known.
//                                rvInboxMessages
//                                        .getViewTreeObserver()
//                                        .removeOnGlobalLayoutListener(this);
//                            }
//                        });
        tvInboxTitle = view.findViewById(R.id.tvInboxTitle);
        pullRefreshLayout = (PullRefreshLayout) view.findViewById(R.id.pullRefreshLayout);

        tvInboxNewMessages = view.findViewById(R.id.tvNewMessagesInbox);

        undoButton = view.findViewById(R.id.undoButton);
        shouldDelete = true;
        allMessages = new ArrayList<>();
        adapter = new MessagesInboxAdapter(getContext(), allMessages, InboxFragment.this);
        //tvInboxTitle.setText(ParseUser.getCurrentUser().getUsername().toString() + "\'s Inbox");

        if(ParseUser.getCurrentUser().get("full_name")==null){
            ParseUser.getCurrentUser().put("full_name","User");
        }


        tvInboxTitle.setText(ParseUser.getCurrentUser().get("full_name").toString() + "\'s Inbox");


        TextView tvNum = view.findViewById(R.id.tvNumUnread);


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
                undoButton.setVisibility(View.VISIBLE);
                Message deletedMessage = allMessages.get(viewHolder.getAdapterPosition());


                undoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //pop top of stack message from stack since we no longer want to delete
                        if(!deleteMessageStack.isEmpty()) {
                            Snackbar.make(rvInboxMessages, "Undo Delete of " + deleteMessageStack.peek().second.getMessageBody(), Snackbar.LENGTH_SHORT).show();

                            Pair<Integer,Message> dpair = deleteMessageStack.peek();
                            Message dMessage = dpair.second;
                            int dposition = dpair.first;




                            allMessages.add(dposition,dMessage);
                            adapter.notifyItemInserted(dposition);
                            shouldDelete = false;
                            deleteMessageStack.pop();
                              }
                        else {
                            Snackbar.make(rvInboxMessages, "No more messages to Undo Delete! ", Snackbar.LENGTH_SHORT).show();

                        }
                    }
                });

                // below line is to get the position
                // of the item at that position.
                int position = viewHolder.getAdapterPosition();

                // this method is called when item is swiped.
                // below line is to remove item from our array list.
                allMessages.remove(viewHolder.getAdapterPosition());


                //add message to stack since we want to delete it
                deleteMessageStack.push(new Pair(position,deletedMessage));


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

//for the second undo button option dont want to use this. Instead delete all messages when user refreshes the screen
                            if(false) {
                            //if(shouldDelete) {

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

        // listen refresh event
        int[] color =  {R.color.teal_200,R.color.teal_400,R.color.teal_700,R.color.purple_500};
        pullRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_WATER_DROP);
        //pullRefreshLayout.setColorSchemeColors(color);
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // start refresh
                Snackbar.make(rvInboxMessages, "Deleted " + deleteMessageStack.size() + " messages.", Snackbar.LENGTH_SHORT).show();


                for(Pair<Integer,Message> a:deleteMessageStack) {

                    Message m = a.second;
                    //delete all messages in stack!
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
                    //query.whereEqualTo("receiver", ParseUser.getCurrentUser());
                    query.whereEqualTo("objectId", m.getObjectId());
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
                deleteMessageStack.clear();
                undoButton.setVisibility(View.INVISIBLE);
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
        queryMessages();
        // Now we call setRefreshing(false) to signal refresh has finished
        pullRefreshLayout.setRefreshing(false);
        //swipeContainer.setRefreshing(false);


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

                else{
                    query.whereEqualTo(Message.KEY_ISUNREAD, true);

                    try {
                        tvInboxNewMessages.setText("You have "+ query.count()+ " new messages!");
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }


                }

                cl.postDelayed(new Runnable() {
                    public void run() {
                        cl.setVisibility(View.GONE);
                    }
                }, 3000);



                // save received messages to list and notify adapter of new data


                allMessages.addAll(messages);
                adapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    public void onFinishDecidePinnedWallDialog(boolean[] toSend) {

        Log.i("MESSAGE INBOX ", getCurrentMessage().getMessageBody()+" val: " +toSend);

        if(toSend[0] == true){
            getCurrentMessage().setIsKudos(true);
            getCurrentMessage().saveInBackground();
        }
        if(toSend[1] == true){
            getCurrentMessage().setIsmemories(true);
            getCurrentMessage().saveInBackground();
        }
        if(toSend[2] == true){
            getCurrentMessage().setIsGoals(true);
            getCurrentMessage().saveInBackground();
        }


    }

    private Message getCurrentMessage() {
        return allMessages.get(adapter.getAdapterPosition());
    }


}