package com.example.fbuapplication.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.fbuapplication.FriendRequest;
import com.example.fbuapplication.Message;
import com.example.fbuapplication.R;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.SocketOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.facebook.login.LoginManager;
import com.parse.ParseUser;
import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment implements SelectCameraFragment.SelectCameraDialogListener{

    public static final String TAG = "profileFragment";

    private ImageView ivProfileImage;
    private TextView tvUsername;
    private  TextView tvNumSent;
    private TextView tvMotivationalQuote;
    private View viewProfile;
    private  TextView tvNumUnread;
    private Button btnAddFriend;
    private Button btnAddGroup;


    private boolean chooseCamera;
   // private static int RESULT_LOAD_IMAGE = 1;

    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    public static final int GALLERY_IMAGE_ACTIVITY_REQUEST_CODE = 13;

    private File photoFile;
    public String photoFileName = "photo.jpg";

    public static final String KEY_PROFILE_PICTURE  = "profile_picture";


    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_profile,parent, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        tvUsername = view.findViewById(R.id.tvUsername);
        tvNumSent = view.findViewById(R.id.tvNumSent);
        ivProfileImage = view.findViewById(R.id.ivProfileImage);
        tvMotivationalQuote = view.findViewById(R.id.tvMotivationalQuote);
        tvNumUnread = view.findViewById(R.id.tvNumUnread);
        chooseCamera = true;
        btnAddFriend = view.findViewById(R.id.btnAddFriend);
        btnAddGroup  = view.findViewById(R.id.btnAddGroup);

//        tvUsername.setText("Welcome " + ParseUser.getCurrentUser().getUsername().toString() +"!");


        if(ParseUser.getCurrentUser().get("full_name")==null){
            ParseUser.getCurrentUser().put("full_name","User");
        }

        if(ParseUser.getCurrentUser().get("num_messages_sent")==null){
            ParseUser.getCurrentUser().put("num_messages_sent",0);
        }

        ParseFile img_user = (ParseFile) ParseUser.getCurrentUser().get(KEY_PROFILE_PICTURE);
        if(img_user!=null){
            Glide.with(requireContext()).load(img_user.getUrl()).into(ivProfileImage);

        }
        tvUsername.setText("Welcome " + ParseUser.getCurrentUser().get("full_name").toString() +"!");
        tvNumSent.setText("Number of notes sent: " + ParseUser.getCurrentUser().get("num_messages_sent"));


        if(!tvNumUnread.equals("")) {
            int numRead = numNewMessages();
        }

        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



//                ParseQuery<FriendRequest> query = ParseQuery.getQuery(FriendRequest.class);
//
//                query.whereEqualTo(FriendRequest.KEY_FROMUSER, ParseUser.getCurrentUser());
//                query.whereEqualTo(FriendRequest.KEY_TOUSER, true);
//
//                query.findInBackground(new FindCallback<Message>() {
//                    @Override
//                    public void done(List<Message> messages, ParseException e) {
//                        // check for errors
//                        if (e != null) {
//                            Log.e(TAG, "Issue with getting messages", e);
//                            //Snackbar.make(rvInboxMessages, "Issue with getting messages. Please try again.", Snackbar.LENGTH_LONG).show();
//                            return;
//                        }
//
//                        else{
//                            numUnread[0] = messages.size();
//                            tvNumUnread.setText("You have "+messages.size() + " new messages!");
//
//                        }
//
//                    }
//
//                });

            }
        });

        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                viewProfile = arg0;


                FragmentManager fm = getFragmentManager();
                SelectCameraFragment selectCameraFragment = new SelectCameraFragment();//;EditNameDialog.newInstance("Some Title");
                // SETS the target fragment for use later when sending results
                selectCameraFragment.setTargetFragment(ProfileFragment.this, 300);
                selectCameraFragment.show(fm, "toSend");


//
//
//                if(chooseCamera) {
//                    onLaunchCamera(arg0);
//                    ParseUser currentUser = ParseUser.getCurrentUser();
//                    updateUserWithPhoto(currentUser, photoFile);
//                }
//
//                else{
//                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(i, GALLERY_IMAGE_ACTIVITY_REQUEST_CODE);
//                }

            }
        });

        getMotivationalQuote();

    }

    public void getMotivationalQuote() {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\n    \"key1\": \"value\",\n    \"key2\": \"value\"\n}");
        Request request = new Request.Builder()
                .url("https://motivational-quotes1.p.rapidapi.com/motivation")
                .post(body)
                .addHeader("content-type", "application/json")
                .addHeader("x-rapidapi-key", "37e97d30f9mshe0dd0b011989d8ap19a372jsn27c489c1c486")
                .addHeader("x-rapidapi-host", "motivational-quotes1.p.rapidapi.com")
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
                    //JSONObject properties = null;

                        //properties = new JSONObject(jsonData);
                        //String key1 = properties.getString("key1");
                        //String key2 = properties.getString("key2");


                    if(getActivity()!=null) {


                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                // Stuff that updates the UI
                                tvMotivationalQuote.setText(jsonData);

                            }
                        });

                    }
                }
            }

    });
    }

        public void onLaunchCamera(View view) {
        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        //where do you want to put the output (in the EXTRA_OUTPUT), passing in fileProvider which wraps photo path
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    //invoked when child activitt returns to parent activity
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        //need a call to superclass
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
               ivProfileImage.setImageBitmap(takenImage);
            } else { // Result was a failure
                //Toast.makeText(getContext(), "Picture wasn't taken!", Toast.LENGTH_SHORT).show();
                Snackbar.make(ivProfileImage, "Picture wasn't taken!", Snackbar.LENGTH_LONG).show();

            }
        }

        if (requestCode == GALLERY_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            ivProfileImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            ParseUser currentUser = ParseUser.getCurrentUser();
            updateUserWithPhoto(currentUser, new File(picturePath));
        }
    }

    // Returns the File for a photo stored on disk given the fileName
    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }

        // Return the file target for the photo based on filename
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    private void updateUserWithPhoto(ParseUser currentUser, File photoFile) {

        ParseFile img = new ParseFile(photoFile);

        img.saveInBackground(new SaveCallback() {
            public void done(ParseException e) {
                // If successful add file to user and signUpInBackground
                if(null == e)
                    currentUser.put(KEY_PROFILE_PICTURE, img);

            }
        });

        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Log.e(TAG, "Error while uploading profile picture",e);
                    //Toast.makeText(getContext(), "Error while saving profile picture!", Toast.LENGTH_SHORT).show();
                    Snackbar.make(ivProfileImage, "Error while saving profile picture!", Snackbar.LENGTH_LONG).show();

                }
                else {
                    Log.i(TAG, "Profile picture upload was successful!");
                    //Glide.with(requireContext()).load(img.getUrl()).into(ivProfileImage);
                }
                //ivProfileImage.setImageResource(new ParseFile(photoFile));
            }
        });


    }

    @Override
    public void onFinishSelectCameraDialog(boolean chooseCamera) {

        if(chooseCamera) {
            onLaunchCamera(getCameraView());
            ParseUser currentUser = ParseUser.getCurrentUser();
            updateUserWithPhoto(currentUser, photoFile);
        }

        else{
            verifyStoragePermissions(getActivity());
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, GALLERY_IMAGE_ACTIVITY_REQUEST_CODE);


        }

    }


    public View getCameraView(){
        return viewProfile;
    }

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    protected int numNewMessages() {

        int[] numUnread = {0};
        // specify what type of data we want to query - Message.class
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);

        query.whereEqualTo(Message.KEY_RECIEVER, ParseUser.getCurrentUser());
        query.whereEqualTo(Message.KEY_ISUNREAD, true);

        query.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> messages, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting messages", e);
                //Snackbar.make(rvInboxMessages, "Issue with getting messages. Please try again.", Snackbar.LENGTH_LONG).show();
                    return;
                }

                else{
                    numUnread[0] = messages.size();
                    tvNumUnread.setText("You have "+messages.size() + " new messages!");

                }

            }

        });

        System.out.println("NUM "+numUnread[0]);
        return numUnread[0];
    }

}
