package com.example.fbuapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

import com.example.fbuapplication.ParseModels.Group;
import com.example.fbuapplication.R;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AddGroupActivity extends AppCompatActivity {


    private MultiAutoCompleteTextView autocompleteGroup;
    private AutoCompleteTextView autoCompleteCategories;
    private EditText etIntroMessage;
    private EditText etGroupName;
    private Button btnSubmitGroup;
    private List<String> getAllUsernames;
    private List<String> getAllCategories;

    public static final String TAG = "AddGroup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        autocompleteGroup = findViewById(R.id.autoCompleteReceiverGroup);
        autoCompleteCategories = findViewById(R.id.autoCompleteCategories);
        etIntroMessage = findViewById(R.id.etIntroMessage);
        etGroupName = findViewById(R.id.etGroupName);
        btnSubmitGroup = findViewById(R.id.btnSubmitGroup);

        getAllUsernames = new ArrayList<>();
        getAllCategories = new ArrayList<>();

        getAllCategories.add("Kudos");
        getAllCategories.add("Favorite Memory");
        getAllCategories.add("Best Quality");
        getAllCategories.add("Motivating Message");

        ParseQuery<ParseUser> query = ParseUser.getQuery();

        query.findInBackground(new FindCallback<ParseUser>() {

            private String[] users;

            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    // The query was successful.
                    users = new String[objects.size()];
                    int i = 0;
                    for (ParseUser p : objects) {
                        users[i] = p.getUsername();
                        getAllUsernames.add(p.getUsername());
                        i++;
                    }


                    ArrayAdapter<String> adapter = new ArrayAdapter<String>
                            (AddGroupActivity.this, android.R.layout.select_dialog_item, users);

                    autocompleteGroup.setThreshold(2);
                    autocompleteGroup.setAdapter(adapter);
                    autocompleteGroup.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

                } else {
                    // Something went wrong.
                    Log.e(TAG, "Error: " + e.getMessage());
                }
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (AddGroupActivity.this, android.R.layout.select_dialog_item, getAllCategories);

        autoCompleteCategories.setThreshold(0);
        autoCompleteCategories.setAdapter(adapter);

        btnSubmitGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "recipient1 ");

                String introMessage = etIntroMessage.getText().toString();

                String groupName = etGroupName.getText().toString();
                if (introMessage.isEmpty()) {
                    //Toast.makeText(getContext(),"Description cannot be empty", Toast.LENGTH_SHORT).show();
                    Snackbar.make(btnSubmitGroup, "Intro Message cannot be empty", Snackbar.LENGTH_LONG).show();

                    return;
                }

                if (introMessage.length() > 300) {
                    Snackbar.make(btnSubmitGroup, "Intro Message exceeds 300 character length limit", Snackbar.LENGTH_LONG).show();

                    return;

                }

                String category = autoCompleteCategories.getText().toString();
                if (category.isEmpty()) {
                    //Toast.makeText(getContext(),"Description cannot be empty", Toast.LENGTH_SHORT).show();
                    Snackbar.make(btnSubmitGroup, "Category cannot be empty", Snackbar.LENGTH_LONG).show();

                    return;
                }

                if (groupName.isEmpty()) {
                    //Toast.makeText(getContext(),"Description cannot be empty", Toast.LENGTH_SHORT).show();
                    Snackbar.make(btnSubmitGroup, "Group name cannot be empty", Snackbar.LENGTH_LONG).show();

                    return;
                }


                String[] groupMembers = autocompleteGroup.getText().toString().split(",");
                final ParseUser[] recipientUser = new ParseUser[1];
                if (groupMembers.length == 0) {
                    //Toast.makeText(getContext(),"Recipient cannot be empty", Toast.LENGTH_SHORT).show();
                    Snackbar.make(btnSubmitGroup, "Recipient cannot be empty", Snackbar.LENGTH_LONG).show();
                    Log.i(TAG, "recipient1 ");

                    return;
                } else {
                    Log.i(TAG, "recipient1 ");

                   // ParseQuery<ParseUser> query = ParseUser.getQuery();
//                    for (String recipient : groupMembers) {
//                        Log.i(TAG, "user " + recipient);
//
//                    }


                    ParseQuery<ParseUser> query1 = ParseUser.getQuery();


                    if(groupMembers.length==0){
                        return;
                    }
                    String[] groupMembersNew = new String[groupMembers.length];
                    if(groupMembers.length!=0) {
                       groupMembersNew = new String[groupMembers.length-1];
                          for(int i=0;i<groupMembersNew.length;i++){
                            groupMembersNew[i] = groupMembers[i].trim();
                              Log.i(TAG, "USER " + groupMembersNew[i] + " GET");

                          }

                    }
                    for(String u:groupMembersNew) {
                        query1.whereEqualTo("username",u);
                        query1.findInBackground(new FindCallback<ParseUser>() {
                            public void done(List<ParseUser> userList, ParseException e) {
                                if(e == null){
                                    ParseUser user = userList.get(0);
                                    user.add("groupsIn",groupName);
                                    user.saveInBackground();
                                }
                                else{

                                }
                        }
                        });
                    }
                    //query1.whereEqualTo("username","shelly");
                    Log.i(TAG, "recipient111 ");

//                    query1.findInBackground(new FindCallback<ParseUser>() {
//                        public void done(List<ParseUser> userList, ParseException e) {
//                            if (e == null) {
//                                Log.i(TAG, "Retrieved " + userList.size() + " scores");
//                                Log.i(TAG, "recipient1 ");

                                //Log.i(TAG, "recipient1 "+recipientUser[0].toString());

                                    Log.i(TAG, "recipient1 ");

                                    Log.i(TAG, "recipient2 ");
                                    ParseQuery<Group> queryGroup = ParseQuery.getQuery(Group.class);
                                    queryGroup.whereEqualTo(Group.KEY_FROMUSER, ParseUser.getCurrentUser());

                                    Log.i(TAG, "recipient111111 ");

                                    Group allGroup = new Group();
                                    allGroup.setGroupName(groupName);
                                    allGroup.setIntroMessage(introMessage);
                                    allGroup.setCategory(category);

                                    allGroup.setFromUser(ParseUser.getCurrentUser());
                                    Log.i(TAG, "recipient122221 ");

//                                   ParseUser[] usersGroup = new ParseUser[userList.size()];//new JSONArray();//[userList.size()];
//
//                                    for(int i = 0;i<userList.size();i++){
//                                        usersGroup[i] = userList.get(i);
//                                    }

                                 //   allGroup.setToUsers(usersGroup);
                                    Log.i(TAG, "recipient1222212323232 ");

                                    allGroup.saveInBackground(new SaveCallback() {
                                        @Override
                                        public void done(ParseException e) {
                                            if (e != null) {
                                                Log.e(TAG, "Error while saving", e);
                                                //Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_SHORT).show();
                                                Snackbar.make(btnSubmitGroup, "Error while creating group!", Snackbar.LENGTH_LONG).show();
                                                Log.i(TAG, "recipient3431 ");

                                            } else {
                                                Snackbar.make(autocompleteGroup, "Group created!", Snackbar.LENGTH_LONG).show();
                                                Log.i(TAG, "recipient4 " );
                                                Log.i(TAG, "recipientf3431 ");


                                            }
                                        }

                                    });

                                    Log.i(TAG, "recipient343341222212323232 ");

                                }
//                            }
//
//                        }
//                    });
                }

        });
    }

}