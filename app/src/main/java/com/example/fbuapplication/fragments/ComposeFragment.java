package com.example.fbuapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fbuapplication.BuildConfig;
import com.example.fbuapplication.JClient;
import com.example.fbuapplication.LoginActivity;
import com.example.fbuapplication.Message;
import com.example.fbuapplication.R;

import android.content.Intent;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;
import com.google.gson.internal.LazilyParsedNumber;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Request;



public class ComposeFragment extends Fragment {

    private Button btnLogout;
    private EditText etMessageFromSender;
    //private EditText etRecipient;
    private Button btnSubmit;
    private AutoCompleteTextView autocomplete;
    private List<String> getAllUsernames;

    //TODO: add onAttach
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
//    private File photoFile;
//    public String photoFileName = "photo.jpg";
    public static final String TAG = "composeFragment";

    JClient sentimentClient;

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

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }

        autocomplete = view.findViewById(R.id.autoCompleteReceiver);
        btnLogout = view.findViewById(R.id.btnLogout);
        etMessageFromSender = view.findViewById(R.id.etMessageFromSender);
        //etRecipient = view.findViewById(R.id.etRecipient);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnSubmit.setBackgroundColor(getResources().getColor(R.color.teal_700));
        getAllUsernames = new ArrayList<>();
        //getAllUsernames.add("default");

        sentimentClient = new JClient();
        ParseQuery<ParseUser> query = ParseUser.getQuery();

        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {
                    // The query was successful.
                    for(ParseUser p: objects){
                        getAllUsernames.add(p.getUsername());
                    }

                    if(getActivity()!= null) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                (getContext(), android.R.layout.select_dialog_item, getAllUsernames);

                        autocomplete.setThreshold(2);
                        autocomplete.setAdapter(adapter);
                    }
                    else{
                        Log.e(TAG,"Activity is null!");
                    }
                } else {
                    // Something went wrong.
                    Log.e(TAG, "Error: " + e.getMessage());
                }
            }
        });



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSubmit.setBackgroundDrawable(getResources().getDrawable(R.color.teal_200));
                String description = etMessageFromSender.getText().toString();
                if(description.isEmpty()){
                    Toast.makeText(getContext(),"Description cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                //String recipient = etRecipient.getText().toString();
                String recipient = autocomplete.getText().toString();
                if(recipient.isEmpty()){
                    Toast.makeText(getContext(),"Recipient cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                String decoded = null;
                try {
                    decoded = URLDecoder.decode(description, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
//                RequestBody body = RequestBody.create(mediaType, "text=I%20am%20not%20really%20happy");

                //https://rapidapi.com/fyhao/api/text-sentiment-analysis-method/
                RequestBody body = RequestBody.create(mediaType, "text="+decoded);
                Request request = new Request.Builder()
                        .url("https://text-sentiment.p.rapidapi.com/analyze")
                        .post(body)
                        .addHeader("content-type", "application/x-www-form-urlencoded")

//                        .addHeader("x-rapidapi-key", "37e97d30f9mshe0dd0b011989d8ap19a372jsn27c489c1c486")
                        .addHeader("x-rapidapi-key",  BuildConfig.CONSUMER_SECRET_KEY)
                        .addHeader("x-rapidapi-host", "text-sentiment.p.rapidapi.com")
                        .build();

                try {
                    Response response = client.newCall(request).execute();
                    String jsonData = response.body().string();
                    JSONObject properties = new JSONObject(jsonData);
                    //JSONObject properties = Jobject.getJSONObject("properties");
                    String pos = properties.getString("pos");
                    String neg = properties.getString("neg");
                    String mid = properties.getString("mid");
                    String pos_percent = properties.getString("pos_percent");
                    String mid_percent = properties.getString("mid_percent");
                    String neg_percent = properties.getString("neg_percent");


                    Log.i(TAG, "POSITIVE: "+ pos+ " NEUTRAL: " + mid + " NEGATIVE: "+neg);
                    Log.i(TAG, "POSITIVE: "+ pos_percent+ " NEUTRAL: " + mid_percent + " NEGATIVE: "+neg_percent);

                    if(Integer.parseInt(neg_percent.substring(0,neg_percent.length()-1))>50){
                        Log.i(TAG, "too negative!");

                    }
                }
                catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

//                if(photoFile == null || ivMessageImage.getDrawable()==null){
//                    Toast.makeText(getContext(),"There is no image!", Toast.LENGTH_SHORT).show();
//                    return;
//                }



                ParseUser currentUser = ParseUser.getCurrentUser();
                List<ParseUser> userListFinal = new ArrayList<>();

                ParseQuery<ParseUser> query = ParseUser.getQuery();
                query.whereEqualTo("username",recipient);
                query.findInBackground(new FindCallback<ParseUser>(){
                    public void done(List<ParseUser> userList, ParseException e) {
                        if (e == null) {
                            Log.i(TAG, "Retrieved " + userList.size() + " scores");

                            userListFinal.addAll(userList);
                            if(userListFinal.isEmpty()){
                                Toast.makeText(getContext(),"Please select a valid user!", Toast.LENGTH_LONG).show();
                                etMessageFromSender.setText("");
                                //etRecipient.setText("");
                                autocomplete.setText("");
                            }
                            else {
                                ParseUser recipientUser = userListFinal.get(0);
                                saveMessage(description, currentUser, recipientUser);
                            }
                            //message.setSender(userList.get(0));
                        } else {
                            Log.e(TAG, "Error: " + e.getMessage());
                        }
                    }
                });


            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();
                //TODO: check if user is null
                goLoginActivity();
            }
        });


    }


    private void saveMessage(String description, ParseUser currentUser, ParseUser recipientUser) {


        Message message = new Message();
        message.setMessageBody(description);
        message.setSender(currentUser);
        message.setReceiver(recipientUser);



        message.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Log.e(TAG, "Error while saving",e);
                    Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_SHORT).show();
                }
                else {
//                    Toast toast = Toast.makeText(getContext(), "Message sent!", Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 100, 100);
//                    toast.show();
                    Snackbar.make(btnLogout, "Message Sent!", Snackbar.LENGTH_LONG).show();

                    Log.i(TAG, "Message was successful!");
                }
                //Snackbar.make(btnLogout, "Message Sent!", Snackbar.LENGTH_INDEFINITE).show();

                //reset field
                etMessageFromSender.setText("");
                //etRecipient.setText("");
                autocomplete.setText("");

                // below line is to display our snackbar with action.

            }
        });



    }

    //changed getContext() to getActivity(), You should use getActivity() to launch an Activity from Fragment.
    private void goLoginActivity(){
        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivity(i);
        //TODO:finish main activity once we have navigated to the next activity
        //want to close current fragment forever
        getActivity().finish();

    }

}