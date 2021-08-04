package com.example.fbuapplication.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fbuapplication.ParseModels.FriendRequest;
import com.example.fbuapplication.R;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class AddFriendActivity extends AppCompatActivity {

    public static final String TAG = "AddFriend";
    private AutoCompleteTextView autocompleteFriend;
    private Button btnSubmitFriend;
    private List<String> getAllUsernames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        autocompleteFriend = findViewById(R.id.autoCompleteReceiverFriend);
        btnSubmitFriend = findViewById(R.id.btnSubmitFriend);

        getAllUsernames = new ArrayList<>();
        ParseQuery<ParseUser> query = ParseUser.getQuery();

        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {

                    for (ParseUser p : objects) {
                        getAllUsernames.add(p.getUsername());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>
                            (AddFriendActivity.this, android.R.layout.select_dialog_item, getAllUsernames);

                    autocompleteFriend.setThreshold(2);
                    autocompleteFriend.setAdapter(adapter);

                } else {

                    Log.e(TAG, "Error: " + e.getMessage());
                }
            }
        });

        btnSubmitFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String recipient = autocompleteFriend.getText().toString();
                final ParseUser[] recipientUser = new ParseUser[1];
                if (recipient.isEmpty()) {

                    Snackbar.make(btnSubmitFriend, "Recipient cannot be empty", Snackbar.LENGTH_LONG).show();

                } else {

                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereEqualTo("username", recipient);
                    query.findInBackground(new FindCallback<ParseUser>() {
                        public void done(List<ParseUser> userList, ParseException e) {
                            if (e == null) {

                                if (userList.isEmpty()) {

                                    Snackbar.make(btnSubmitFriend, "Please select a valid user!", Snackbar.LENGTH_LONG).show();

                                    autocompleteFriend.setText("");
                                } else {

                                    recipientUser[0] = userList.get(0);

                                    ParseQuery<FriendRequest> queryFriend = ParseQuery.getQuery(FriendRequest.class);
                                    queryFriend.whereEqualTo(FriendRequest.KEY_FROMUSER, ParseUser.getCurrentUser());
                                    queryFriend.whereEqualTo(FriendRequest.KEY_TOUSER, recipientUser[0]);

                                    queryFriend.findInBackground(new FindCallback<FriendRequest>() {
                                        @Override
                                        public void done(List<FriendRequest> allFriendRequests, ParseException e) {

                                            if (e != null) {
                                                Log.e(TAG, "Issue with checking friend requests", e);

                                                FriendRequest allFriendRequest = new FriendRequest();
                                                allFriendRequest.setStatus("pending");
                                                allFriendRequest.setFromUser(ParseUser.getCurrentUser());
                                                allFriendRequest.setToUser(recipientUser[0]);

                                                allFriendRequest.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        if (e != null) {
                                                            Log.e(TAG, "Error while saving", e);

                                                            Snackbar.make(btnSubmitFriend, "Error while sending friend request!", Snackbar.LENGTH_LONG).show();

                                                        } else {
                                                            Snackbar.make(autocompleteFriend, "Friend request sent!", Snackbar.LENGTH_LONG).show();

                                                        }

                                                    }

                                                });

                                            } else {

                                                if (allFriendRequests.isEmpty()) {
                                                    FriendRequest allFriendRequest = new FriendRequest();
                                                    allFriendRequest.setStatus("pending");
                                                    allFriendRequest.setFromUser(ParseUser.getCurrentUser());
                                                    allFriendRequest.setToUser(recipientUser[0]);

                                                    allFriendRequest.saveInBackground(new SaveCallback() {
                                                        @Override
                                                        public void done(ParseException e) {
                                                            if (e != null) {
                                                                Log.e(TAG, "Error while saving", e);

                                                                Snackbar.make(btnSubmitFriend, "Error while sending friend request!", Snackbar.LENGTH_LONG).show();

                                                            } else {
                                                                Snackbar.make(autocompleteFriend, "Friend request sent!", Snackbar.LENGTH_LONG).show();

                                                            }

                                                        }

                                                    });
                                                } else {
                                                    FriendRequest allFriendRequest = allFriendRequests.get(0);

                                                    switch (allFriendRequest.getStatus()) {

                                                        case "accepted":
                                                            Snackbar.make(btnSubmitFriend, "You are already friends with " + recipient, Snackbar.LENGTH_LONG).show();

                                                            break;

                                                        case "pending":
                                                            Snackbar.make(btnSubmitFriend, "Already sent friend request to " + recipient, Snackbar.LENGTH_LONG).show();

                                                            break;

                                                        case "denied":
                                                            Snackbar.make(btnSubmitFriend, "Friend request was not accepted by " + recipient, Snackbar.LENGTH_LONG).show();

                                                            break;

                                                        case "unfriended":
                                                            Snackbar.make(btnSubmitFriend, "Sending friend request again to " + recipient, Snackbar.LENGTH_LONG).show();
                                                            allFriendRequest.setStatus("pending");
                                                            break;
                                                        default:
                                                            break;
                                                    }

                                                    allFriendRequest.saveInBackground();

                                                }

                                            }
                                        }

                                    });
                                }
                            }
                        }
                    });

                }

            }
        });

    }
}