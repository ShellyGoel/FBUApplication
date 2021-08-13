package com.example.fbuapplication.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fbuapplication.ParseModels.Group;
import com.example.fbuapplication.ParseModels.GroupToMembers;
import com.example.fbuapplication.R;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//class for creating a group
public class AddGroupActivity extends AppCompatActivity {

    public static final String TAG = "AddGroup";
    private MultiAutoCompleteTextView autocompleteGroup;
    private AutoCompleteTextView autoCompleteCategories;
    private EditText etIntroMessage;
    private EditText etGroupName;
    private Button btnSubmitGroup;
    private List<String> getAllUsernames;
    private List<String> getAllCategories;

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

                String introMessage = etIntroMessage.getText().toString();

                String groupName = etGroupName.getText().toString();
                if (introMessage.isEmpty()) {

                    Snackbar.make(btnSubmitGroup, "Intro Message cannot be empty", Snackbar.LENGTH_LONG).show();

                    return;
                }

                if (introMessage.length() > 300) {
                    Snackbar.make(btnSubmitGroup, "Intro Message exceeds 300 character length limit", Snackbar.LENGTH_LONG).show();

                    return;

                }

                String category = autoCompleteCategories.getText().toString();
                if (category.isEmpty()) {

                    Snackbar.make(btnSubmitGroup, "Category cannot be empty", Snackbar.LENGTH_LONG).show();

                    return;
                }

                if (groupName.isEmpty()) {

                    Snackbar.make(btnSubmitGroup, "Group name cannot be empty", Snackbar.LENGTH_LONG).show();

                    return;
                }

                String[] groupMembers = autocompleteGroup.getText().toString().split(",");

                if (groupMembers.length == 0) {

                    Snackbar.make(btnSubmitGroup, "Recipient cannot be empty", Snackbar.LENGTH_LONG).show();

                    return;
                } else {

                    if (groupMembers.length == 0) {
                        return;
                    }
                    String[] groupMembersNew = new String[groupMembers.length];
                    if (groupMembers.length != 0) {
                        groupMembersNew = new String[groupMembers.length - 1];
                        for (int i = 0; i < groupMembersNew.length; i++) {
                            groupMembersNew[i] = groupMembers[i].trim();

                        }

                    }

                    ParseQuery<Group> queryGroup = ParseQuery.getQuery(Group.class);
                    queryGroup.whereEqualTo(Group.KEY_FROMUSER, ParseUser.getCurrentUser());

                    Group allGroup = new Group();
                    allGroup.setGroupName(groupName);
                    allGroup.setIntroMessage(introMessage);
                    allGroup.setCategory(category);
                    allGroup.setToUsers(autocompleteGroup.getText().toString());
                    allGroup.setFromUser(ParseUser.getCurrentUser());

                    String[] finalGroupMembersNew = groupMembersNew;
                    allGroup.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.e(TAG, "Error while saving", e);

                                Snackbar.make(btnSubmitGroup, "Error while creating group!", Snackbar.LENGTH_LONG).show();

                            } else {
                                Snackbar.make(autocompleteGroup, "Group created!", Snackbar.LENGTH_LONG).show();
                                etIntroMessage.setText("");
                                etGroupName.setText("");
                                autoCompleteCategories.setText("");
                                autocompleteGroup.setText("");

                            }
                        }

                    });

                    ParseQuery<ParseUser> query1 = ParseUser.getQuery();

                    List<String> groupsList = Arrays.asList(finalGroupMembersNew);

                    Collections.shuffle(groupsList);
                    String[] shuffledArray = new String[groupsList.size()];
                    groupsList.toArray(shuffledArray);
                    String[] finalAssignedUsersArray = new String[groupsList.size()];

                    //shuffled array, now set each assignedUser to the one after, for last element set as first

                    for (int i = 0; i < shuffledArray.length; i++) {
                        if (i != shuffledArray.length - 1) {
                            finalAssignedUsersArray[i] = shuffledArray[i + 1];
                        } else {
                            finalAssignedUsersArray[i] = shuffledArray[0];
                        }
                    }
                    for (int i = 0; i < shuffledArray.length; i++) {

                        String u = finalGroupMembersNew[i];
                        query1.whereEqualTo("username", u);
                        int finalI = i;
                        query1.findInBackground(new FindCallback<ParseUser>() {
                            public void done(List<ParseUser> userList, ParseException e) {
                                if (e == null) {

                                    GroupToMembers groupUser = new GroupToMembers();
                                    groupUser.setUsername(u);
                                    groupUser.setGroupID(allGroup);
                                    String randomMember = finalAssignedUsersArray[finalI];
                                    groupUser.put("assignedUser", randomMember);
                                    groupUser.saveInBackground();
                                } else {

                                }
                            }
                        });
                    }

                }

            }

        });
    }

}