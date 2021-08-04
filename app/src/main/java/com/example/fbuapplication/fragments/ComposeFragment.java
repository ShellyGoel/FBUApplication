package com.example.fbuapplication.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.fbuapplication.BuildConfig;
import com.example.fbuapplication.ParseModels.Message;
import com.example.fbuapplication.R;
import com.example.fbuapplication.activities.LoginActivity;
import com.example.fbuapplication.fragments.dialogFragments.DoNotSendDialogFragment;
import com.facebook.login.LoginManager;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ComposeFragment extends Fragment implements DoNotSendDialogFragment.DoNotSendDialogListener {

    //TODO: add onAttach
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    //    private File photoFile;
//    public String photoFileName = "photo.jpg";
    public static final String TAG = "composeFragment";
    public static final String[] randomCompliments = {"You look great today.", "You’re a smart cookie.", "You have impeccable manners.", "You have the best laugh.", "You are appreciated.", "You’re strong.", "Your perspective is refreshing.", "You’re an awesome friend.", "You light up the room.", "You should be proud of yourself.", "You’re more helpful than you realize.", "You have a great sense of humor.", "Your kindness is a balm to all who encounter it.", "On a scale from 1 to 10, you’re an 11.", "You are brave.", "You have the courage of your convictions.", "You are making a difference.", "You’re like sunshine on a rainy day.", "You bring out the best in other people.", "You’re a great listener.", "Everything would be better if more people were like you!", "When you’re not afraid to be yourself is when you’re most incredible.", "You’re wonderful.", "Jokes are funnier when you tell them.", "You’re better than a triple-scoop ice cream cone. With sprinkles.", "You’re one of a kind!", "You’re inspiring.", "If you were a box of crayons, you’d be the giant name-brand one with the built-in sharpener.", "You should be thanked more often. So thank you!!", "Our community is better because you’re in it.", "Someone is getting through something hard right now because you’ve got their back.", "You have the best ideas.", "You always know how to find that silver lining.", "Everyone gets knocked down sometimes, but you always get back up and keep going.", "You’re a candle in the darkness.", "You’re a great example to others.", "You always know just what to say.", "You’re always learning new things and trying to better yourself, which is awesome.", "You could survive a Zombie apocalypse.", "You’re more fun than bubble wrap.", "When you make a mistake, you fix it.", "You’re great at figuring stuff out.", "Your voice is magnificent.", "The people you love are lucky to have you in their lives.", "You’re like a breath of fresh air.", "You’re so thoughtful.", "Your creative potential seems limitless.", "Actions speak louder than words, and yours tell an incredible story.", "Somehow you make time stop and fly at the same time.", "When you make up your mind about something, nothing stands in your way.", "You seem to really know who you are.", "Any team would be lucky to have you on it.", "In high school I bet you were voted “most likely to keep being awesome.”", "I bet you do the crossword puzzle in ink.", "Babies and small animals probably love you.", "There’s ordinary, and then there’s you.", "You’re someone’s reason to smile.", "You’re even better than a unicorn, because you’re real.", "The way you treasure your loved ones is incredible.", "You’re really something special.", "You’re a gift to those around you."};
    boolean shouldDelete;
    private Button btnLogout;
    private EditText etMessageFromSender;
    //private EditText etRecipient;
    private Button btnSubmit;
    private Button btnRandCompliment;
    private AutoCompleteTextView autocomplete;
    private List<String> getAllUsernames;
    private TextView tvCompose;
    private int sent1;
    private double sent2;
    private boolean shouldMessageSend;
    private Button btnSendText;
    private String compose_description;
    private String compose_recipient;
    private TextView tvMessageBodyToSend;
    private TextView tvDateToSend;
    private ImageView ivStickyNoteImageDetailsToSend;
    private ConstraintLayout clSendText;
    private View stickyNote;
    private String stringToSend;

    // The onCreateView method is called when Fragment should create its View object hierarchy,
    // either dynamically or via XML layout inflation.
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        stickyNote = inflater.inflate(R.layout.stickynotetosend, parent, false);

        return inflater.inflate(R.layout.fragment_compose, parent, false);

    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.view.findViewById()(R.id.etFoo);
        super.onViewCreated(view, savedInstanceState);

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            //your codes here

        }

        btnSendText = view.findViewById(R.id.btnSendText);

        autocomplete = view.findViewById(R.id.autoCompleteReceiver);
        btnLogout = view.findViewById(R.id.btnLogout);
        btnRandCompliment = view.findViewById(R.id.btnRandCompliment);
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

        if (getArguments() != null) {

            String userToSend = getArguments().getString("sendingTo");
            autocomplete.setText(userToSend);
        }

        clSendText = stickyNote.findViewById(R.id.clSendText);
        tvDateToSend = stickyNote.findViewById(R.id.tvDateToSend);
        tvMessageBodyToSend = stickyNote.findViewById(R.id.tvMessageBodyToSend);
        ivStickyNoteImageDetailsToSend = stickyNote.findViewById(R.id.ivStickyNoteImageDetailsToSend);

        ParseQuery<ParseUser> query = ParseUser.getQuery();

        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {
                if (e == null) {

                    for (ParseUser p : objects) {
                        getAllUsernames.add(p.getUsername());
                    }

                    if (getActivity() != null) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                                (getContext(), android.R.layout.select_dialog_item, getAllUsernames);

                        autocomplete.setThreshold(2);
                        autocomplete.setAdapter(adapter);
                    } else {
                        Log.e(TAG, "Activity is null!");
                    }
                } else {

                    Log.e(TAG, "Error: " + e.getMessage());
                }
            }
        });

        btnSendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String textMessage = etMessageFromSender.getText().toString();
                tvMessageBodyToSend.setText(textMessage);

                Date createdAt = Calendar.getInstance().getTime();
                String timeAgo = Message.calculateTimeAgo(createdAt);

                tvDateToSend.setText("You're invited by " + ParseUser.getCurrentUser().get("full_name").toString() + " to join this app!");

                clSendText.setDrawingCacheEnabled(true);

                clSendText.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                        View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                clSendText.layout(0, 0, clSendText.getMeasuredWidth(), clSendText.getMeasuredHeight());

                clSendText.buildDrawingCache(true);
                Bitmap b = Bitmap.createBitmap(clSendText.getDrawingCache());

                String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), b, "Emoticon2", null);
                if (path != null) {
                    Uri fileUri = Uri.parse(path);
                    sendIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
                    sendIntent.setType("image/png");
                    clSendText.setDrawingCacheEnabled(false);
                    if (sendIntent.resolveActivity(getContext().getPackageManager()) != null) {
                        startActivity(sendIntent);
                    }

                }

            }
        });

        btnRandCompliment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etMessageFromSender.setText(randomCompliment());
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSubmit.setBackgroundDrawable(getResources().getDrawable(R.color.teal_200));
                String description = etMessageFromSender.getText().toString();
                if (description.isEmpty()) {

                    Snackbar.make(btnSubmit, "Message cannot be empty", Snackbar.LENGTH_LONG).show();

                    return;
                }

                if (description.length() > 770) {
                    Snackbar.make(btnSubmit, "Message exceeds 770 character length limit", Snackbar.LENGTH_LONG).show();

                    return;

                }

                String recipient = autocomplete.getText().toString();

                if (recipient.isEmpty()) {

                    Snackbar.make(btnSubmit, "Recipient cannot be empty", Snackbar.LENGTH_LONG).show();
                    return;
                }

                compose_description = description;
                compose_recipient = recipient;

                //Keeping for future use for more fine-grained categorization of messages!
                //perspective_api();

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
                RequestBody body = RequestBody.create(mediaType, "text=" + decoded);
                Request request = new Request.Builder()
                        .url("https://text-sentiment.p.rapidapi.com/analyze")
                        .post(body)
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("x-rapidapi-key", BuildConfig.CONSUMER_SECRET_KEY)
                        .addHeader("x-rapidapi-host", "text-sentiment.p.rapidapi.com")
                        .build();

                Response response = null;

                try {
                    response = client.newCall(request).execute();
                    try {
                        String jsonData = response.body().string();
                        JSONObject properties = null;
                        properties = new JSONObject(jsonData);

                        String pos = properties.getString("pos");
                        String neg = properties.getString("neg");
                        String mid = properties.getString("mid");
                        String pos_percent = properties.getString("pos_percent");
                        String mid_percent = properties.getString("mid_percent");
                        String neg_percent = properties.getString("neg_percent");

                        sent_val[0] = Integer.parseInt(neg_percent.substring(0, neg_percent.length() - 1));
                        sent1 = Integer.parseInt(neg_percent.substring(0, neg_percent.length() - 1));
                        if (Integer.parseInt(neg_percent.substring(0, neg_percent.length() - 1)) > 50) {

                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                    if (getActivity() != null) {

                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                            }
                        });

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                double[] sent_score = {1.0};
                OkHttpClient client1 = new OkHttpClient();
                MediaType mediaType1 = MediaType.parse("application/json");
                String placeholder1 = "{\n    \"documents\": [\n        {\n            \"id\": \"1\",\n            \"text\": \"" + description + "\"\n        }\n    ]\n}";
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

                        JSONObject result = properties.getJSONObject("result");
                        JSONArray documents = result.getJSONArray("documents");

                        String sentiment_score = documents.getJSONObject(0).getString("sentiments_score");
                        sent_score[0] = Double.parseDouble(sentiment_score);
                        sent2 = Double.parseDouble(sentiment_score);

                        if (getActivity() != null) {

                            getActivity().runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                }
                            });

                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (sent1 >= 50 || sent2 < 0.5) {

                    FragmentManager fm = getFragmentManager();
                    DoNotSendDialogFragment doNotSendDialogFragment = new DoNotSendDialogFragment();

                    doNotSendDialogFragment.setTargetFragment(ComposeFragment.this, 300);
                    doNotSendDialogFragment.show(fm, "toSend");

                } else {
                    prepareMessage(getRecipient(), getDescription());
                }

            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logOut();
                ParseUser.logOut();
                ParseUser currentUser = ParseUser.getCurrentUser();

                goLoginActivity();
            }
        });

    }

    private void perspective_api() {
        try {
            String url = "https://commentanalyzer.googleapis.com/v1alpha1/comments:analyze?key=", key = BuildConfig.PERSPECTIVE_KEY;

            final URL serverUrl = new URL(url + key);
            URLConnection urlConnection = serverUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) urlConnection;
            httpConnection.setRequestMethod("POST");
            httpConnection.setRequestProperty("Content-Type", "application/json");
            httpConnection.setDoOutput(true);
            BufferedWriter httpRequestBodyWriter = new BufferedWriter(new
                    OutputStreamWriter(httpConnection.getOutputStream()));
            httpRequestBodyWriter.write("{\"comment\": {\"text\": \"" + getDescription() + "\"},"
                    + "\"requestedAttributes\": {\"TOXICITY\": {}, \"SEVERE_TOXICITY\": {},\"IDENTITY_ATTACK\": {},\"INSULT\": {},\"THREAT\": {},\"SEXUALLY_EXPLICIT\": {},\"FLIRTATION\": {}}}");
            httpRequestBodyWriter.flush();
            httpRequestBodyWriter.close();
            System.out.println("CODE : " + httpConnection.getResponseCode());

            BufferedReader responseBuffer = new BufferedReader(new InputStreamReader((httpConnection.getInputStream())));
            String output;
            while ((output = responseBuffer.readLine()) != null) {
                System.out.println(output);
            }

            httpConnection.disconnect();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

    }

    private String randomCompliment() {
        Random r = new Random();
        int ind = r.nextInt(randomCompliments.length);
        return randomCompliments[ind];
    }

    private void showEditDialog() {
        FragmentManager fm = getFragmentManager();
        DoNotSendDialogFragment doNotSendDialogFragment = new DoNotSendDialogFragment();

        doNotSendDialogFragment.setTargetFragment(ComposeFragment.this, 300);
        doNotSendDialogFragment.show(fm, "toSend");

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

        RequestBody body = RequestBody.create(mediaType, "text=" + decoded);
        Request request = new Request.Builder()
                .url("https://text-sentiment.p.rapidapi.com/analyze")
                .post(body)
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .addHeader("x-rapidapi-key", BuildConfig.CONSUMER_SECRET_KEY)
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

                        String pos = properties.getString("pos");
                        String neg = properties.getString("neg");
                        String mid = properties.getString("mid");
                        String pos_percent = properties.getString("pos_percent");
                        String mid_percent = properties.getString("mid_percent");
                        String neg_percent = properties.getString("neg_percent");

                        sent_val[0] = Integer.parseInt(neg_percent.substring(0, neg_percent.length() - 1));
                        sent1 = Integer.parseInt(neg_percent.substring(0, neg_percent.length() - 1));
                        if (Integer.parseInt(neg_percent.substring(0, neg_percent.length() - 1)) > 50) {

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (getActivity() != null) {

                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                            }
                        });

                    }
                }
            }

        });
        return sent_val[0];
    }

    public double getSentiment2(String description) {
        OkHttpClient client = new OkHttpClient();

        double[] sent_score = {1.0};

        MediaType mediaType = MediaType.parse("application/json");
        String placeholder = "{\n    \"documents\": [\n        {\n            \"id\": \"1\",\n            \"text\": \"" + description + "\"\n        }\n    ]\n}";
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

                        JSONObject result = properties.getJSONObject("result");
                        JSONArray documents = result.getJSONArray("documents");

                        String sentiment_score = documents.getJSONObject(0).getString("sentiments_score");
                        sent_score[0] = Double.parseDouble(sentiment_score);
                        sent2 = Double.parseDouble(sentiment_score);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (getActivity() != null) {

                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                            }
                        });

                    }
                }
            }

        });

        return sent_score[0];
    }

    private void prepareMessage(String recipient, String description) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        List<ParseUser> userListFinal = new ArrayList<>();

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", recipient);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userList, ParseException e) {
                if (e == null) {

                    userListFinal.addAll(userList);
                    if (userListFinal.isEmpty()) {

                        Snackbar.make(btnSubmit, "Please select a valid user!", Snackbar.LENGTH_LONG).show();
                        etMessageFromSender.setText("");

                        autocomplete.setText("");
                    } else {
                        ParseUser recipientUser = userListFinal.get(0);
                        saveMessage(description, currentUser, recipientUser);
                    }

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
                if (e != null) {
                    Log.e(TAG, "Error while saving", e);

                    Snackbar.make(btnSubmit, "Error while sending message", Snackbar.LENGTH_LONG).show();

                } else {

                    Snackbar.make(btnSubmit, "Message Sent!", Snackbar.LENGTH_LONG).show();

                    ParseUser.getCurrentUser().put("num_messages_sent", (Integer) ParseUser.getCurrentUser().get("num_messages_sent") + 1);
                    ParseUser.getCurrentUser().saveInBackground();

                }

                etMessageFromSender.setText("");

                autocomplete.setText("");

            }
        });

    }

    private void goLoginActivity() {
        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivity(i);

        getActivity().finish();

    }

    @Override
    public void onFinishDoNotSendDialog(boolean toSend) {

        shouldMessageSend = toSend;
        if (toSend) {
            prepareMessage(getRecipient(), getDescription());
        } else {
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

