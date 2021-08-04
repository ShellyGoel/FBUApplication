package com.example.fbuapplication.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.fbuapplication.ParseModels.Message;
import com.example.fbuapplication.R;
import com.example.fbuapplication.activities.MessageDetailsActivity;
import com.example.fbuapplication.adapters.MessagesMainWallAdapter;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import dyanamitechetan.vusikview.VusikView;

public class MainWallFragment extends Fragment {

    public static final String TAG = "FeedActivity";
    protected MessagesMainWallAdapter adapter;
    protected List<Message> allMessages;
    protected List<Message> wallSpecificAllMessages;
    private RecyclerView rvMainWallMessages;
    private SwipeRefreshLayout swipeContainer;
    private TextView tvWallTitle;
    private Toolbar toolbar;
    private VusikView vusikView;
    private ImageView ivPlant;
    private int clickedID;
    private SearchView searchView;

    public int getClickedID() {
        return clickedID;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_main_wall, parent, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_main_wall, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        final MenuItem itemSelectWall = menu.findItem(R.id.action_choosewall);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                String toUse;

                final List<Message> filteredModelList = filter(wallSpecificAllMessages, newText);
                adapter.setFilter(filteredModelList);
                return true;
            }
        });

        item.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {

                adapter.setFilter(wallSpecificAllMessages);
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {

                return true;
            }
        });

        itemSelectWall.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem itemSelectWall) {

                int id = item.getItemId();
                clickedID = id;
                adapter.setFilter(allMessages);
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem itemSelectWall) {

                int id = item.getItemId();
                clickedID = id;
                return true;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        clickedID = id;
        System.out.println("clicked " + clickedID);
        final List<Message> filteredModelList;
        rvMainWallMessages.setVisibility(View.VISIBLE);

        ivPlant.setVisibility(View.INVISIBLE);
        switch (id) {

            case R.id.action_search:
                adapter.setFilter(wallSpecificAllMessages);
                return true;

            case R.id.action_kudos:
                vusikView.setVisibility(View.GONE);

                if (ParseUser.getCurrentUser().get("full_name") == null) {
                    ParseUser.getCurrentUser().put("full_name", "User");
                }
                tvWallTitle.setText(ParseUser.getCurrentUser().get("full_name").toString() + "'s Kudos Wall");
                rvMainWallMessages.setBackground(getResources().getDrawable(R.drawable.b8));

                filteredModelList = filterIsKudos(allMessages);
                wallSpecificAllMessages.clear();
                wallSpecificAllMessages.addAll(filteredModelList);

                adapter.setFilter(filteredModelList);
                return true;

            case R.id.action_memories:
                vusikView.setVisibility(View.GONE);

                if (ParseUser.getCurrentUser().get("full_name") == null) {
                    ParseUser.getCurrentUser().put("full_name", "User");
                }
                tvWallTitle.setText(ParseUser.getCurrentUser().get("full_name").toString() + "'s Memories Wall");
                rvMainWallMessages.setBackground(getResources().getDrawable(R.drawable.b2));

                filteredModelList = filterIsMemories(allMessages);
                wallSpecificAllMessages.clear();
                wallSpecificAllMessages.addAll(filteredModelList);

                adapter.setFilter(filteredModelList);
                return true;

            case R.id.action_goals:
                vusikView.setVisibility(View.GONE);

                if (ParseUser.getCurrentUser().get("full_name") == null) {
                    ParseUser.getCurrentUser().put("full_name", "User");
                }
                tvWallTitle.setText(ParseUser.getCurrentUser().get("full_name").toString() + "'s Goals Wall");
                rvMainWallMessages.setBackground(getResources().getDrawable(R.drawable.b6));

                filteredModelList = filterIsGoals(allMessages);
                wallSpecificAllMessages.clear();

                wallSpecificAllMessages.addAll(filteredModelList);

                adapter.setFilter(filteredModelList);

                return true;

            case R.id.action_wallmain:
                vusikView.setVisibility(View.GONE);

                if (ParseUser.getCurrentUser().get("full_name") == null) {
                    ParseUser.getCurrentUser().put("full_name", "User");
                }

                tvWallTitle.setText(ParseUser.getCurrentUser().get("full_name").toString() + "'s Main Wall");
                rvMainWallMessages.setBackground(getResources().getDrawable(R.drawable.b2));
                wallSpecificAllMessages.clear();
                wallSpecificAllMessages.addAll(allMessages);
                adapter.setFilter(allMessages);
                return true;

            case R.id.action_fun:

                tvWallTitle.setText("Pick a random note!");

                rvMainWallMessages.setVisibility(View.GONE);
                wallSpecificAllMessages.clear();
                wallSpecificAllMessages.addAll(allMessages);
                vusikView.setVisibility(View.VISIBLE);

                List<Message> emptyList = new ArrayList<>();
                adapter.setFilter(emptyList);
                ivPlant.setVisibility(View.VISIBLE);
                ivPlant.setBackground(getResources().getDrawable(R.drawable.stickynote_pile));
                vusikView.start();
                int[] myImageList = new int[]{R.drawable._removal_ai__tmp_60ebbf1103f00, R.drawable._removal_ai__tmp_60ebbf43c2076, R.drawable._removal_ai__tmp_60ebbf5282318, R.drawable._removal_ai__tmp_60ebbf5fd350d};
                vusikView
                        .setImages(myImageList)
                        .start();

                ivPlant.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Random rand = new Random();
                        if (allMessages.size() > 0) {
                            int randomNum = rand.nextInt(allMessages.size());
                            Message randomMessage = allMessages.get(randomNum);
                            Date createdAt = randomMessage.getCreatedAt();
                            String timeAgo = Message.calculateTimeAgo(createdAt);

                            Intent intent = new Intent(getContext(), MessageDetailsActivity.class);

                            intent.putExtra("createdAt", timeAgo);
                            intent.putExtra("caption", randomMessage.getMessageBody());

                            getContext().startActivity(intent);
                        }
                    }
                });

                return true;

            default:
                vusikView.setVisibility(View.GONE);

                return true;
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

    private List<Message> filterIsKudos(List<Message> models) {

        final List<Message> filteredModelList = new ArrayList<>();
        for (Message model : models) {
            if (model.getIsKudos()) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private List<Message> filterIsMemories(List<Message> models) {

        final List<Message> filteredModelList = new ArrayList<>();
        for (Message model : models) {
            if (model.getIsMemories()) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    private List<Message> filterIsGoals(List<Message> models) {

        final List<Message> filteredModelList = new ArrayList<>();
        for (Message model : models) {
            if (model.getIsGoals()) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        rvMainWallMessages = view.findViewById(R.id.rvMainWallMessages);
        tvWallTitle = view.findViewById(R.id.tvWallTitle);
        toolbar = view.findViewById(R.id.toolbar);
        vusikView = view.findViewById(R.id.vusik);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        wallSpecificAllMessages = new ArrayList<>();
        allMessages = new ArrayList<>();

        adapter = new MessagesMainWallAdapter(getContext(), allMessages, new MessagesMainWallAdapter.MainWallInterface() {

            public int onWork() {
                return getClickedID();
            }
        });
        ivPlant = view.findViewById(R.id.ivPlant);

        if (ParseUser.getCurrentUser().get("full_name") == null) {
            ParseUser.getCurrentUser().put("full_name", "User");
        }
        tvWallTitle.setText(ParseUser.getCurrentUser().get("full_name").toString() + "'s Main Wall");

        rvMainWallMessages.setAdapter(adapter);

        rvMainWallMessages.setLayoutManager(new GridLayoutManager(getContext(), 2));

        if (ParseUser.getCurrentUser().get("num_messages_sent") == null) {
            ParseUser.getCurrentUser().put("num_messages_sent", 0);
        }

        queryMessages();

        swipeContainer = view.findViewById(R.id.swipeContainer);

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

        if (searchView.getQuery().length() != 0) {
            swipeContainer.setRefreshing(false);
            return;
        }
        adapter.clear();
        queryMessages();

        swipeContainer.setRefreshing(false);

        if (ParseUser.getCurrentUser().get("full_name") == null) {
            ParseUser.getCurrentUser().put("full_name", "User");
        }

        adapter.setFilter(allMessages);

    }

    protected void queryMessages() {

        if (ParseUser.getCurrentUser().get("full_name") == null) {
            ParseUser.getCurrentUser().put("full_name", "User");
        }

        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        ArrayList<String> messagesPinnedByUser = new ArrayList<>();

        query.whereEqualTo(Message.KEY_RECIEVER, ParseUser.getCurrentUser());
        query.whereEqualTo(Message.KEY_ISPINNED, true);
        int wall_chosen = getClickedID();

        if (wall_chosen == R.id.action_kudos) {
            query.whereEqualTo(Message.KEY_ISKUDOS, true);
        }
        if (wall_chosen == R.id.action_memories) {
            query.whereEqualTo(Message.KEY_ISMEMORIES, true);
        }
        if (wall_chosen == R.id.action_goals) {
            query.whereEqualTo(Message.KEY_ISGOALS, true);
        }

        query.setLimit(20);

        query.addDescendingOrder("createdAt");

        query.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> messages, ParseException e) {

                if (e != null) {
                    Log.e(TAG, "Issue with getting messages", e);
                    Snackbar.make(rvMainWallMessages, "Issue with getting messages. Please try again.", Snackbar.LENGTH_LONG).show();
                    return;
                }

                for (Message message : messages) {

                    messagesPinnedByUser.add(message.getMessageBody());

                    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.e(TAG, "Error while saving new messages", e);

                                Activity activity = getActivity();
                                if (activity != null) {

                                    Snackbar.make(rvMainWallMessages, "Error while retrieving new messages!", Snackbar.LENGTH_LONG).show();

                                }
                            }

                        }
                    });

                }

                allMessages.clear();

                allMessages.addAll(messages);
                adapter.clear();

                adapter.addAll(messages);
                adapter.notifyDataSetChanged();
            }
        });
    }

}