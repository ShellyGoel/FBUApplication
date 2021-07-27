package com.example.fbuapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import com.example.fbuapplication.FriendRequest;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class AddFriend extends AppCompatActivity {


    private AutoCompleteTextView autocompleteFriend;
    private Button btnSubmitFriend;
    private List<String> getAllUsernames;

    public static final String TAG = "AddFriend";

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
                    // The query was successful.
                    for (ParseUser p : objects) {
                        getAllUsernames.add(p.getUsername());
                    }


                    ArrayAdapter<String> adapter = new ArrayAdapter<String>
                            (AddFriend.this, android.R.layout.select_dialog_item, getAllUsernames);

                    autocompleteFriend.setThreshold(2);
                    autocompleteFriend.setAdapter(adapter);

                } else {
                    // Something went wrong.
                    Log.e(TAG, "Error: " + e.getMessage());
                }
            }
        });


        btnSubmitFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "recipient1 ");

                String recipient = autocompleteFriend.getText().toString();
                final ParseUser[] recipientUser = new ParseUser[1];
                if (recipient.isEmpty()) {
                    //Toast.makeText(getContext(),"Recipient cannot be empty", Toast.LENGTH_SHORT).show();
                    Snackbar.make(btnSubmitFriend, "Recipient cannot be empty", Snackbar.LENGTH_LONG).show();
                    Log.i(TAG, "recipient1 ");

                    return;
                } else {
                    Log.i(TAG, "recipient1 ");

                    ParseQuery<ParseUser> query = ParseUser.getQuery();
                    query.whereEqualTo("username", recipient);
                    query.findInBackground(new FindCallback<ParseUser>() {
                        public void done(List<ParseUser> userList, ParseException e) {
                            if (e == null) {
                                Log.i(TAG, "Retrieved " + userList.size() + " scores");

                                //Log.i(TAG, "recipient1 "+recipientUser[0].toString());
                                if (userList.isEmpty()) {
                                    Log.i(TAG, "recipient1 ");

                                    //Toast.makeText(getContext(),"Please select a valid user!", Toast.LENGTH_LONG).show();
                                    Snackbar.make(btnSubmitFriend, "Please select a valid user!", Snackbar.LENGTH_LONG).show();

                                    //etRecipient.setText("");
                                    autocompleteFriend.setText("");
                                } else {
                                    Log.i(TAG, "recipient1 ");

                                    recipientUser[0] = userList.get(0);
                                    Log.i(TAG, "recipient2 "+recipientUser[0].toString());
                                    ParseQuery<FriendRequest> queryFriend = ParseQuery.getQuery(FriendRequest.class);
                                    queryFriend.whereEqualTo(FriendRequest.KEY_FROMUSER, ParseUser.getCurrentUser());
                                    queryFriend.whereEqualTo(FriendRequest.KEY_TOUSER, recipientUser[0]);

                                    queryFriend.findInBackground(new FindCallback<FriendRequest>() {
                                        @Override
                                        public void done(List<FriendRequest> allFriendRequests, ParseException e) {
                                            // check for errors
                                            if (e != null) {
                                                Log.e(TAG, "Issue with checking friend requests", e);
                                                //Snackbar.make(rvInboxMessages, "Issue with getting messages. Please try again.", Snackbar.LENGTH_LONG).show();

                                                Log.i(TAG, "recipient3 "+recipientUser[0].toString());
                                                FriendRequest allFriendRequest = new FriendRequest();
                                                allFriendRequest.setStatus("pending");
                                                allFriendRequest.setFromUser(ParseUser.getCurrentUser());
                                                allFriendRequest.setToUser(recipientUser[0]);
Log.i(TAG, "recipient "+recipientUser[0].toString());
                                                allFriendRequest.saveInBackground(new SaveCallback() {
                                                    @Override
                                                    public void done(ParseException e) {
                                                        if (e != null) {
                                                            Log.e(TAG, "Error while saving", e);
                                                            //Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_SHORT).show();
                                                            Snackbar.make(btnSubmitFriend, "Error while sending friend request!", Snackbar.LENGTH_LONG).show();

                                                        } else {
                                                            Snackbar.make(autocompleteFriend, "Friend request sent!", Snackbar.LENGTH_LONG).show();
                                                            Log.i(TAG, "recipient4 "+recipientUser[0].toString());
//                                                            Snackbar.make(btnSubmitFriend, "Friend Request Sent!", Snackbar.LENGTH_LONG).show();
//                                                            //ParseUser.getCurrentUser().put("Friends", recipientUser[0]);
//                                                            ParseUser.getCurrentUser().saveInBackground();
                                                        }


                                                    }


                                                });


                                            } else {


                                                if (allFriendRequests.isEmpty()) {
                                                    FriendRequest allFriendRequest = new FriendRequest();
                                                    allFriendRequest.setStatus("pending");
                                                    allFriendRequest.setFromUser(ParseUser.getCurrentUser());
                                                    allFriendRequest.setToUser(recipientUser[0]);
                                                    Log.i(TAG, "recipient " + recipientUser[0].toString());
                                                    allFriendRequest.saveInBackground(new SaveCallback() {
                                                        @Override
                                                        public void done(ParseException e) {
                                                            if (e != null) {
                                                                Log.e(TAG, "Error while saving", e);
                                                                //Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_SHORT).show();
                                                                Snackbar.make(btnSubmitFriend, "Error while sending friend request!", Snackbar.LENGTH_LONG).show();

                                                            } else {
                                                                Snackbar.make(autocompleteFriend, "Friend request sent!", Snackbar.LENGTH_LONG).show();
                                                                Log.i(TAG, "recipient4 " + recipientUser[0].toString());
//                                                            Snackbar.make(btnSubmitFriend, "Friend Request Sent!", Snackbar.LENGTH_LONG).show();
//                                                            //ParseUser.getCurrentUser().put("Friends", recipientUser[0]);
//                                                            ParseUser.getCurrentUser().saveInBackground();
                                                            }


                                                        }


                                                    });
                                                } else {
                                                    FriendRequest allFriendRequest = allFriendRequests.get(0);

                                                    switch (allFriendRequest.getStatus()) {


                                                        case "accepted":
                                                            Snackbar.make(btnSubmitFriend, "Already sent friend request to " + recipient, Snackbar.LENGTH_LONG).show();

                                                            break;


                                                        case "pending":
                                                            Snackbar.make(btnSubmitFriend, "Already sent friend request to " + recipient, Snackbar.LENGTH_LONG).show();

                                                            break;

                                                        case "denied":
                                                            Snackbar.make(btnSubmitFriend, "Friend request not accapted by " + recipient, Snackbar.LENGTH_LONG).show();

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