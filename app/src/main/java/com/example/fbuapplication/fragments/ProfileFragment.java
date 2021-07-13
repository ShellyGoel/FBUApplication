package com.example.fbuapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fbuapplication.Message;
import com.example.fbuapplication.R;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment extends Fragment{

    @Override
    protected void queryMessages() {
        // specify what type of data we want to query - Message.class
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        // include data referred by user key
        query.include(Message.KEY_USER);
        query.whereEqualTo(Message.KEY_USER, ParseUser.getCurrentUser());
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
                    Log.i(TAG, "Message: " + message.getDescription() + ", username: " + message.getUser().getUsername());
                }

                // save received messages to list and notify adapter of new data
                allMessages.addAll(messages);
                adapter.notifyDataSetChanged();
            }
        });
    }
}
