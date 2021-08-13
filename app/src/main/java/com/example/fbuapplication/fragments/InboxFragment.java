package com.example.fbuapplication.fragments;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alexvasilkov.foldablelayout.UnfoldableView;
import com.baoyz.widget.PullRefreshLayout;
import com.example.fbuapplication.ParseModels.Message;
import com.example.fbuapplication.R;
import com.example.fbuapplication.adapters.MessagesInboxAdapter;
import com.example.fbuapplication.fragments.dialogFragments.DecidePinnedWallDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

//Fragment where a user can view all the messages they have received
public class InboxFragment extends Fragment implements DecidePinnedWallDialogFragment.DecidePinnedWallDialogListener {

    public static final String TAG = "InboxFragment";
    protected MessagesInboxAdapter adapter;
    protected List<Message> allMessages;
    private RecyclerView rvInboxMessages;
    private TextView tvInboxTitle;
    private boolean shouldDelete;
    private String shouldDeleteString;
    private UnfoldableView mUnfoldableView;
    private TextView tvInboxNewMessages;
    private SwipeRefreshLayout swipeContainer;
    private ConstraintLayout cl;
    private RecyclerView.ViewHolder inboxViewHolder;
    private PullRefreshLayout pullRefreshLayout;
    private Stack<Pair<Integer, Message>> deleteMessageStack;
    private Button undoButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_inbox, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        rvInboxMessages = view.findViewById(R.id.rvInboxMessages);
        cl = view.findViewById(R.id.placeholderInbox);

        deleteMessageStack = new Stack<Pair<Integer, Message>>();

        tvInboxTitle = view.findViewById(R.id.tvInboxTitle);
        pullRefreshLayout = view.findViewById(R.id.pullRefreshLayout);

        tvInboxNewMessages = view.findViewById(R.id.tvNewMessagesInbox);

        undoButton = view.findViewById(R.id.undoButton);
        shouldDelete = true;
        allMessages = new ArrayList<>();
        adapter = new MessagesInboxAdapter(getContext(), allMessages, InboxFragment.this);

        if (ParseUser.getCurrentUser().get("full_name") == null) {
            ParseUser.getCurrentUser().put("full_name", "User");
        }

        tvInboxTitle.setText(ParseUser.getCurrentUser().get("full_name").toString() + "'s Inbox");

        TextView tvNum = view.findViewById(R.id.tvNumUnread);

        rvInboxMessages.setAdapter(adapter);

        rvInboxMessages.setLayoutManager(new GridLayoutManager(getContext(), 1));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvInboxMessages.getContext(), 1);

        rvInboxMessages.addItemDecoration(dividerItemDecoration);
        rvInboxMessages.smoothScrollToPosition(0);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                shouldDelete = true;
                undoButton.setVisibility(View.VISIBLE);
                Message deletedMessage = allMessages.get(viewHolder.getAdapterPosition());

                undoButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!deleteMessageStack.isEmpty()) {
                            Pair<Integer, Message> dpair = deleteMessageStack.peek();
                            Message dMessage = dpair.second;
                            int dposition = dpair.first;

                            allMessages.add(dposition, dMessage);

                            adapter.notifyItemInserted(dposition);
                            rvInboxMessages.smoothScrollToPosition(dposition);
                            shouldDelete = false;
                            deleteMessageStack.pop();
                        } else {
                            Snackbar.make(rvInboxMessages, "No more messages to Undo Delete! ", Snackbar.LENGTH_SHORT).show();

                        }
                    }
                });

                int position = viewHolder.getAdapterPosition();

                allMessages.remove(viewHolder.getAdapterPosition());

                deleteMessageStack.push(new Pair(position, deletedMessage));

                adapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                Snackbar snack = Snackbar.make(rvInboxMessages, deletedMessage.getMessageBody(), Snackbar.LENGTH_SHORT).setAction("Undo Delete", new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        allMessages.add(position, deletedMessage);

                        adapter.notifyItemInserted(position);
                        rvInboxMessages.smoothScrollToPosition(position);
                        shouldDelete = false;

                        if (!deleteMessageStack.isEmpty()) {
                            deleteMessageStack.pop();
                        }

                    }

                });
                snack.show();
                snack.addCallback(new Snackbar.Callback() {

                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT || event == Snackbar.Callback.DISMISS_EVENT_ACTION || event == Snackbar.Callback.DISMISS_EVENT_ACTION || event == Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE || event == Snackbar.Callback.DISMISS_EVENT_SWIPE) {

                            if (false) {

                                ParseQuery<Message> query = ParseQuery.getQuery(Message.class);

                                query.whereEqualTo("objectId", deletedMessage.getObjectId());
                                query.findInBackground(new FindCallback<Message>() {
                                    public void done(List<Message> messages, ParseException e) {
                                        if (e == null) {

                                            for (Message message : messages) {
                                                message.deleteInBackground();
                                            }
                                            allMessages.clear();
                                            allMessages.addAll(messages);
                                            adapter.notifyDataSetChanged();

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

        }).attachToRecyclerView(rvInboxMessages);

        queryMessages();

        int[] color = {R.color.teal_200, R.color.teal_400, R.color.teal_700, R.color.purple_500};
        pullRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_WATER_DROP);

        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                int numDeleted = deleteMessageStack.size();
                if (numDeleted > 0) {
                    Snackbar.make(rvInboxMessages, "Deleting " + numDeleted + " messages... Please refresh again.", Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(rvInboxMessages, "Refreshing completed. All messages are loaded!", Snackbar.LENGTH_SHORT).show();
                }
                for (Pair<Integer, Message> a : deleteMessageStack) {

                    Message m = a.second;

                    ParseQuery<Message> query = ParseQuery.getQuery(Message.class);

                    query.whereEqualTo("objectId", m.getObjectId());
                    query.findInBackground(new FindCallback<Message>() {
                        public void done(List<Message> messages, ParseException e) {
                            if (e == null) {

                                for (Message message : messages) {

                                    message.deleteInBackground();
                                }

                                allMessages.clear();
                                allMessages.addAll(messages);
                                adapter.notifyDataSetChanged();
                            } else {
                                Log.d(TAG, e.getMessage());
                            }
                        }
                    });
                }
                deleteMessageStack.clear();
                undoButton.setVisibility(View.INVISIBLE);
                fetchTimelineAsync();
            }
        });

        pullRefreshLayout.setRefreshing(false);

    }

    public void fetchTimelineAsync() {

        adapter.clear();
        queryMessages();
        adapter.notifyDataSetChanged();
        pullRefreshLayout.setRefreshing(false);

    }

    protected void queryMessages() {

        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        ArrayList<String> messagesForUser = new ArrayList<>();

        query.whereEqualTo(Message.KEY_RECIEVER, ParseUser.getCurrentUser());

        query.setLimit(20);

        query.addDescendingOrder("createdAt");

        query.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> messages, ParseException e) {

                if (e != null) {
                    Log.e(TAG, "Issue with getting messages", e);
                    Snackbar.make(rvInboxMessages, "Issue with getting messages. Please try again.", Snackbar.LENGTH_LONG).show();
                    return;
                } else {
                    query.whereEqualTo(Message.KEY_ISUNREAD, true);

                    try {
                        int count = query.count();
                        if (count == 1) {
                            tvInboxNewMessages.setText("You have " + query.count() + " new message!");
                        } else {
                            tvInboxNewMessages.setText("You have " + query.count() + " new messages!");
                        }

                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }

                }

                cl.postDelayed(new Runnable() {
                    public void run() {
                        cl.setVisibility(View.GONE);
                    }
                }, 3000);

                allMessages.addAll(messages);
                adapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    public void onFinishDecidePinnedWallDialog(boolean[] toSend) {

        if (toSend[0] == true) {
            getCurrentMessage().setIsKudos(true);
            getCurrentMessage().saveInBackground();
        }
        if (toSend[1] == true) {
            getCurrentMessage().setIsmemories(true);
            getCurrentMessage().saveInBackground();
        }
        if (toSend[2] == true) {
            getCurrentMessage().setIsGoals(true);
            getCurrentMessage().saveInBackground();
        }

    }

    private Message getCurrentMessage() {
        return allMessages.get(adapter.getAdapterPosition());
    }

}