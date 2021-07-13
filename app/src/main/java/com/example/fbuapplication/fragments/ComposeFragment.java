package com.example.fbuapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fbuapplication.LoginActivity;
import com.example.fbuapplication.Message;
import com.example.fbuapplication.R;

import android.content.Intent;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;


public class ComposeFragment extends Fragment {

    private Button btnLogout;
    private EditText etMessageFromSender;
    private EditText etRecipient;
    private Button btnSubmit;


    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
//    private File photoFile;
//    public String photoFileName = "photo.jpg";
    public static final String TAG = "composeFragment";

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_compose,parent, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.view.findViewById()(R.id.etFoo);
        super.onViewCreated(view, savedInstanceState);
        btnLogout = view.findViewById(R.id.btnLogout);
        etMessageFromSender = view.findViewById(R.id.etMessageFromSender);
        etRecipient = view.findViewById(R.id.etRecipient);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = etMessageFromSender.getText().toString();
                if(description.isEmpty()){
                    Toast.makeText(getContext(),"Description cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                String recipient = etRecipient.getText().toString();
                if(recipient.isEmpty()){
                    Toast.makeText(getContext(),"Recipient cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

//                if(photoFile == null || ivMessageImage.getDrawable()==null){
//                    Toast.makeText(getContext(),"There is no image!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                ParseUser currentUser = ParseUser.getCurrentUser();
                saveMessage(description,currentUser, recipient);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();
                goLoginActivity();
            }
        });


    }


    private void saveMessage(String description, ParseUser currentUser, String recipientUser) {
        Message post = new Message();
        post.setMessageBody(description);
        post.setSender(currentUser);
//        ParseQuery<ParseUser> query = ParseUser.getQuery();
//
////        A ParseQuery can also be used to retrieve a single object whose id is known, through
////        the ParseQuery.getInBackground(String) method, using a GetCallback. For example, this sample
////        code fetches an object of class "MyClass" and id myId. It calls a different function depending
////        on whether the fetch succeeded or not.
//        query.whereEqualTo("username",recipientUser);
//        query.findInBackground(new FindCallback<ParseObject>() {
//                                   public void done(List<ParseObject> objects, ParseException e) {
//                                       if (e == null) {
//                                           //objectsWereRetrievedSuccessfully(objects);
//                                       } else {
//                                           //objectRetrievalFailed();
//                                       }
//                                   }
//                               };

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", recipientUser);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> recipientList, ParseException e) {
                if (e == null) {
                    Log.d("compose ", "Retrieved " + recipientList.size() + " recipient list");
                    post.setReciever(recipientList.get(0));
                } else {
                    Log.d("compose ", "Error: " + e.getMessage());
                }
            }
        });

        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Log.e(TAG, "Error while saving",e);
                    Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "Message was successful!");
                //reset field
                etMessageFromSender.setText("");
                etRecipient.setText("");
//                //set as empty resource ID
//                ivMessageImage.setImageResource(0);

            }
        });
    }

    private void goLoginActivity(){
        Intent i = new Intent(getContext(), LoginActivity.class);
        startActivity(i);
        //TODO:finish main activity once we have navigated to the next activity
        // finish();

    }
//
//    private void queryMessages(){
//        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
//        query.include(Message.KEY_USER);
//        query.findInBackground(new FindCallback<Message>() {
//            @Override
//            public void done(List<Message> posts, ParseException e) {
//                if(e != null){
//                    Log.e(TAG, "Issue with getting posts", e);
//                }
//                else{
//                    for(Message post: posts){
//                        Log.i(TAG, "Message: "+post.getDescription()+ ", username: " + post.getUser().getUsername());
//                    }
//                }
//            }
//        });
//    }
}