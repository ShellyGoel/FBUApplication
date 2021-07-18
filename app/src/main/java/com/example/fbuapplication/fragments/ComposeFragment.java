package com.example.fbuapplication.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.StrictMode;
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
import android.widget.TextView;


import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Request;



public class ComposeFragment extends Fragment implements DoNotSendDialogFragment.DoNotSendDialogListener {

    private Button btnLogout;
    private EditText etMessageFromSender;
    //private EditText etRecipient;
    private Button btnSubmit;
    private AutoCompleteTextView autocomplete;
    private List<String> getAllUsernames;
    private TextView tvCompose;
    private int sent1;
    private double sent2;
    private boolean shouldMessageSend;

    private String compose_description;
    private String compose_recipient;

    boolean shouldDelete;
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
        tvCompose = view.findViewById(R.id.tvCompose);
        //getAllUsernames.add("default");
        sent1 = 0;
        sent2 = 1.0;
        shouldMessageSend = false;

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
                    //Toast.makeText(getContext(),"Description cannot be empty", Toast.LENGTH_SHORT).show();
                    Snackbar.make(btnSubmit, "Message cannot be empty", Snackbar.LENGTH_LONG).show();

                    return;
                }
                //String recipient = etRecipient.getText().toString();
                String recipient = autocomplete.getText().toString();
                if(recipient.isEmpty()){
                    //Toast.makeText(getContext(),"Recipient cannot be empty", Toast.LENGTH_SHORT).show();
                    Snackbar.make(btnSubmit, "Recipient cannot be empty", Snackbar.LENGTH_LONG).show();
                    return;
                }

                //TODO: Adding second sentiment analysis to double check sentiment

//                int sentiment_val1 = getSentiment1(description);
//                double sentiment_val2 = getSentiment2(description);

                compose_description = description;
                compose_recipient = recipient;


                int[] sent_val = {0};
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                String decoded = null;
                try {
                    decoded = URLDecoder.decode(description, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                //https://rapidapi.com/fyhao/api/text-sentiment-analysis-method/
                RequestBody body = RequestBody.create(mediaType, "text="+decoded);
                Request request = new Request.Builder()
                        .url("https://text-sentiment.p.rapidapi.com/analyze")
                        .post(body)
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("x-rapidapi-key",  BuildConfig.CONSUMER_SECRET_KEY)
                        .addHeader("x-rapidapi-host", "text-sentiment.p.rapidapi.com")
                        .build();

                Response response = null;

                    try {
                        response = client.newCall(request).execute();
                        try {
                            String jsonData = response.body().string();
                            JSONObject properties = null;
                            properties = new JSONObject(jsonData);
                            //JSONObject properties = Jobject.getJSONObject("properties");
                            String pos = properties.getString("pos");
                            String neg = properties.getString("neg");
                            String mid = properties.getString("mid");
                            String pos_percent = properties.getString("pos_percent");
                            String mid_percent = properties.getString("mid_percent");
                            String neg_percent = properties.getString("neg_percent");


                            Log.i(TAG, "POSITIVE: " + pos + " NEUTRAL: " + mid + " NEGATIVE: " + neg);
                            Log.i(TAG, "POSITIVE: " + pos_percent + " NEUTRAL: " + mid_percent + " NEGATIVE: " + neg_percent);

                            sent_val[0] = Integer.parseInt(neg_percent.substring(0, neg_percent.length() - 1));
                            sent1 = Integer.parseInt(neg_percent.substring(0, neg_percent.length() - 1));
                            if (Integer.parseInt(neg_percent.substring(0, neg_percent.length() - 1)) > 50) {
                                Log.i(TAG, "too negative!");
                            }
                        }catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }


                        if(getActivity()!=null) {


                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    // Stuff that updates the UI
                                    //tvMotivationalQuote.setText(jsonData);

                                }
                            });

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                //TODO:Sent 2


                double[] sent_score = {1.0};
                OkHttpClient client1 = new OkHttpClient();
                MediaType mediaType1 = MediaType.parse("application/json");
                String placeholder1 = "{\n    \"documents\": [\n        {\n            \"id\": \"1\",\n            \"text\": \""+description+"\"\n        }\n    ]\n}";
                RequestBody body1 = RequestBody.create(mediaType1, placeholder1);
                Request request1 = new Request.Builder()
                        .url("https://sentiments-analysis.p.rapidapi.com/")
                        .post(body1)
                        .addHeader("content-type", "application/json")
                        .addHeader("x-rapidapi-key", "37e97d30f9mshe0dd0b011989d8ap19a372jsn27c489c1c486")
                        .addHeader("x-rapidapi-host", "sentiments-analysis.p.rapidapi.com")
                        .build();


                Response response1 = null;

                    try {
                        response1 = client1.newCall(request1).execute();
                        try {
                            String jsonData = response1.body().string();
                            JSONObject properties = null;
                            properties = new JSONObject(jsonData);
                            //JSONObject properties = Jobject.getJSONObject("properties");
                            JSONObject result = properties.getJSONObject("result");
                            JSONArray documents = result.getJSONArray("documents");
                            //JSONObject doc_0 = documents.getJSONObject("0");
                            String sentiment_score = documents.getJSONObject(0).getString("sentiments_score");
                            sent_score[0] = Double.parseDouble(sentiment_score);
                            sent2 = Double.parseDouble(sentiment_score);
                            Log.i(TAG, "SCORE: "+sent_score[0]);
                            if(getActivity()!=null) {


                                getActivity().runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {

                                        // Stuff that updates the UI
                                        //tvMotivationalQuote.setText(jsonData);

                                    }
                                });

                            }
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                if(sent1>=50 || sent2<0.5){
                    //make a toast because the message might be negative
//
//
//                    Bundle args = new Bundle();
//
//                    args.putString("recipient", recipient);
//                    args.putString("description", description);

//                    DontSendDialogFragment dontSendDialogFragment = new DontSendDialogFragment();
//
////                    dontSendDialogFragment.setArguments(args);
//
//                    dontSendDialogFragment.show(
//                            getChildFragmentManager(), "Dialog");
//

                    FragmentManager fm = getFragmentManager();
                    DoNotSendDialogFragment doNotSendDialogFragment = new DoNotSendDialogFragment();//;EditNameDialog.newInstance("Some Title");
                    // SETS the target fragment for use later when sending results
                    doNotSendDialogFragment.setTargetFragment(ComposeFragment.this, 300);
                    doNotSendDialogFragment.show(fm, "toSend");


//                    if(fm==null && shouldMessageSend){
//                        prepareMessage(recipient,description);
//
//                    }


                }

                else{
                    prepareMessage(getRecipient(), getDescription());
                }



//                ParseUser currentUser = ParseUser.getCurrentUser();
//                List<ParseUser> userListFinal = new ArrayList<>();
//
//                ParseQuery<ParseUser> query = ParseUser.getQuery();
//                query.whereEqualTo("username",recipient);
//                query.findInBackground(new FindCallback<ParseUser>(){
//                    public void done(List<ParseUser> userList, ParseException e) {
//                        if (e == null) {
//                            Log.i(TAG, "Retrieved " + userList.size() + " scores");
//
//                            userListFinal.addAll(userList);
//                            if(userListFinal.isEmpty()){
//                                //Toast.makeText(getContext(),"Please select a valid user!", Toast.LENGTH_LONG).show();
//                                Snackbar.make(btnSubmit, "Please select a valid user!", Snackbar.LENGTH_LONG).show();
//                                etMessageFromSender.setText("");
//                                //etRecipient.setText("");
//                                autocomplete.setText("");
//                            }
//                            else {
//                                ParseUser recipientUser = userListFinal.get(0);
//                                saveMessage(description, currentUser, recipientUser);
//                            }
//                            //message.setSender(userList.get(0));
//                        } else {
//                            Log.e(TAG, "Error: " + e.getMessage());
//                        }
//                    }
//                });


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

    private void showEditDialog() {
        FragmentManager fm = getFragmentManager();
        DoNotSendDialogFragment doNotSendDialogFragment = new DoNotSendDialogFragment();//;EditNameDialog.newInstance("Some Title");
        // SETS the target fragment for use later when sending results
        doNotSendDialogFragment.setTargetFragment(ComposeFragment.this, 300);
        doNotSendDialogFragment.show(fm, "toSend");

        //getToSend()
    }

    public int getSentiment1(String description) {
        int[] sent_val = {0};
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

        String decoded = null;
        try {
            decoded = URLDecoder.decode(description, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //https://rapidapi.com/fyhao/api/text-sentiment-analysis-method/
        RequestBody body = RequestBody.create(mediaType, "text="+decoded);
        Request request = new Request.Builder()
                .url("https://text-sentiment.p.rapidapi.com/analyze")
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("x-rapidapi-key",  BuildConfig.CONSUMER_SECRET_KEY)
                .addHeader("x-rapidapi-host", "text-sentiment.p.rapidapi.com")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String jsonData = response.body().string();
                    JSONObject properties = null;
                    try {
                        properties = new JSONObject(jsonData);
                        //JSONObject properties = Jobject.getJSONObject("properties");
                        String pos = properties.getString("pos");
                        String neg = properties.getString("neg");
                        String mid = properties.getString("mid");
                        String pos_percent = properties.getString("pos_percent");
                        String mid_percent = properties.getString("mid_percent");
                        String neg_percent = properties.getString("neg_percent");


                        Log.i(TAG, "POSITIVE: " + pos + " NEUTRAL: " + mid + " NEGATIVE: " + neg);
                        Log.i(TAG, "POSITIVE: " + pos_percent + " NEUTRAL: " + mid_percent + " NEGATIVE: " + neg_percent);

                        sent_val[0] = Integer.parseInt(neg_percent.substring(0, neg_percent.length() - 1));
                        sent1 = Integer.parseInt(neg_percent.substring(0, neg_percent.length() - 1));
                        if (Integer.parseInt(neg_percent.substring(0, neg_percent.length() - 1)) > 50) {
                            Log.i(TAG, "too negative!");
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if(getActivity()!=null) {


                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                // Stuff that updates the UI
                                //tvMotivationalQuote.setText(jsonData);

                            }
                        });

                    }
                }
            }

        });
        return sent_val[0];
    }

    public double getSentiment2(String description) {OkHttpClient client = new OkHttpClient();

        double[] sent_score = {1.0};

        MediaType mediaType = MediaType.parse("application/json");
        String placeholder = "{\n    \"documents\": [\n        {\n            \"id\": \"1\",\n            \"text\": \""+description+"\"\n        }\n    ]\n}";
        RequestBody body = RequestBody.create(mediaType, placeholder);
        Request request = new Request.Builder()
                .url("https://sentiments-analysis.p.rapidapi.com/")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("x-rapidapi-key", "37e97d30f9mshe0dd0b011989d8ap19a372jsn27c489c1c486")
                .addHeader("x-rapidapi-host", "sentiments-analysis.p.rapidapi.com")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                } else {
                    String jsonData = response.body().string();
                    JSONObject properties = null;
                    try {
                        properties = new JSONObject(jsonData);
                        //JSONObject properties = Jobject.getJSONObject("properties");
                        JSONObject result = properties.getJSONObject("result");
                        JSONArray documents = result.getJSONArray("documents");
                        //JSONObject doc_0 = documents.getJSONObject("0");
                        String sentiment_score = documents.getJSONObject(0).getString("sentiments_score");
                        sent_score[0] = Double.parseDouble(sentiment_score);
                        sent2 = Double.parseDouble(sentiment_score);
                        Log.i(TAG, "SCORE: "+sent_score[0]);

                    }catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if(getActivity()!=null) {


                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                // Stuff that updates the UI
                                //tvMotivationalQuote.setText(jsonData);

                            }
                        });

                    }
                }
            }

        });

        return sent_score[0];
    }



    private void prepareMessage(String recipient, String description){
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
                        //Toast.makeText(getContext(),"Please select a valid user!", Toast.LENGTH_LONG).show();
                        Snackbar.make(btnSubmit, "Please select a valid user!", Snackbar.LENGTH_LONG).show();
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
                    //Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_SHORT).show();
                    Snackbar.make(btnSubmit, "Error while sending message", Snackbar.LENGTH_LONG).show();

                }
                else {
//                    Toast toast = Toast.makeText(getContext(), "Message sent!", Toast.LENGTH_LONG);
//                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 100, 100);
//                    toast.show();
                    Snackbar.make(btnSubmit, "Message Sent!", Snackbar.LENGTH_LONG).show();

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

    @Override
    public void onFinishDoNotSendDialog(boolean toSend) {
        Log.i(TAG, "To send? " + Boolean.toString(toSend));
        shouldMessageSend = toSend;
        if(toSend){
            prepareMessage(getRecipient(), getDescription());
        }
        else{
            Snackbar.make(btnSubmit, "Message not Sent!", Snackbar.LENGTH_LONG).show();

        }


    }

    private String getDescription() {
        return compose_description;
    }

    private String getRecipient() {
        return compose_recipient;
    }


}

