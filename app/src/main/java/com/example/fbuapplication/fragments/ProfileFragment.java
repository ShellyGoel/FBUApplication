package com.example.fbuapplication.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fbuapplication.Message;
import com.example.fbuapplication.MessagesInboxAdapter;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment{

    public static final String TAG = "profileFragment";

    private ImageView ivProfileImage;
    private TextView tvUsername;
    private  TextView tvNumSent;

    private static int RESULT_LOAD_IMAGE = 1;

    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    private File photoFile;
    public String photoFileName = "photo.jpg";

    public static final String KEY_PROFILE_PICTURE  = "profile_picture";

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

//        tvUsername.setText("Welcome " + ParseUser.getCurrentUser().getUsername().toString() +"!");


        if(ParseUser.getCurrentUser().get("full_name")==null){
            ParseUser.getCurrentUser().put("full_name","User");
        }

        if(ParseUser.getCurrentUser().get("num_messages_sent")==null){
            ParseUser.getCurrentUser().put("num_messages_sent",0);
        }
        tvUsername.setText("Welcome " + ParseUser.getCurrentUser().get("full_name").toString() +"!");
        tvNumSent.setText("Number of notes sent: " + ParseUser.getCurrentUser().get("num_messages_sent"));
        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                onLaunchCamera(arg0);
                ParseUser currentUser = ParseUser.getCurrentUser();
                updateUserWithPhoto(currentUser, photoFile);


//                Intent i = new Intent(
//                        Intent.ACTION_PICK,
//                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(i, RESULT_LOAD_IMAGE);
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
        currentUser.put(KEY_PROFILE_PICTURE, new ParseFile(photoFile));

        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Log.e(TAG, "Error while uploading profile picture",e);
                    //Toast.makeText(getContext(), "Error while saving profile picture!", Toast.LENGTH_SHORT).show();
                    Snackbar.make(ivProfileImage, "Error while saving profile picture!", Snackbar.LENGTH_LONG).show();

                }
                Log.i(TAG, "Profile picture upload was successful!");
            }
        });

    }

    public void onActivityGalleryResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            ivProfileImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
    }


}
