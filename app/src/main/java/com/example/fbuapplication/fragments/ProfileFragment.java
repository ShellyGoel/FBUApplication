package com.example.fbuapplication.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.example.fbuapplication.ParseModels.Message;
import com.example.fbuapplication.R;
import com.example.fbuapplication.activities.AddFriendActivity;
import com.example.fbuapplication.activities.AddGroupActivity;
import com.example.fbuapplication.activities.AllNotesActivity;
import com.example.fbuapplication.activities.FriendsRequestListActivity;
import com.example.fbuapplication.activities.GroupActivity;
import com.example.fbuapplication.activities.LoginActivity;
import com.example.fbuapplication.fragments.dialogFragments.SelectCameraFragment;
import com.facebook.login.LoginManager;
import com.google.android.material.snackbar.Snackbar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

//Fragment showing user's profile page which includes their profile photo, motivational quote, and navigation to the friends and group features.
public class ProfileFragment extends Fragment implements SelectCameraFragment.SelectCameraDialogListener {

    public static final String TAG = "profileFragment";
    // private static int RESULT_LOAD_IMAGE = 1;
// PICK_PHOTO_CODE is a constant integer
    public final static int PICK_PHOTO_CODE = 1046;
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;
    public static final int GALLERY_IMAGE_ACTIVITY_REQUEST_CODE = 13;
    public static final String KEY_PROFILE_PICTURE = "profile_picture";
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public String photoFileName = "photo.jpg";
    private ImageView ivProfileImage;
    private TextView tvUsername;
    private TextView tvNumSent;
    private TextView tvMotivationalQuote;
    private View viewProfile;
    private TextView tvNumUnread;
    private Button btnAddFriend;
    private Button btnAddGroup;
    private Button btnAcceptFriend;
    private Button btnAcceptGroup;
    private Button btnLogout;
    private boolean chooseCamera;
    private File photoFile;

    /**
     * Checks if the app has permission to write to device storage
     * <p>
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_profile, parent, false);
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
        btnAddGroup = view.findViewById(R.id.btnAddGroup);
        btnAcceptFriend = view.findViewById(R.id.btnAcceptFriend);
        btnAcceptGroup = view.findViewById(R.id.btnAcceptGroup);
        btnLogout = view.findViewById(R.id.btnLogout);

//        tvUsername.setText("Welcome " + ParseUser.getCurrentUser().getUsername().toString() +"!");

        if (ParseUser.getCurrentUser().get("full_name") == null) {
            ParseUser.getCurrentUser().put("full_name", "User");
        }

        if (ParseUser.getCurrentUser().get("num_messages_sent") == null) {
            ParseUser.getCurrentUser().put("num_messages_sent", 0);
        }

        ParseFile img_user = ParseUser.getCurrentUser().getParseFile(KEY_PROFILE_PICTURE);
        if (img_user != null) {
            Glide.with(getContext()).load(img_user.getUrl()).into(ivProfileImage);

        }
        tvUsername.setText("Welcome " + ParseUser.getCurrentUser().get("full_name").toString() + "!");
        tvNumSent.setText("Number of notes sent: " + ParseUser.getCurrentUser().get("num_messages_sent"));

        if (!tvNumUnread.equals("")) {
            int numRead = numNewMessages();
        }

        tvNumSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAllNotesActivity();
            }
        });
        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goFriendRequestActivity();

            }
        });

        btnAcceptFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAcceptFriendRequestActivity();
            }
        });

        btnAddGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAddGroupActivity();
            }
        });

        btnAcceptGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goGroupActivity();
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

                    if (getActivity() != null) {

                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {

                                // Stuff that updates the UI
                                tvMotivationalQuote.setMovementMethod(new ScrollingMovementMethod());
                                tvMotivationalQuote.setText(jsonData);

                            }
                        });

                    }
                }
            }

        });
    }

    // Trigger gallery selection for a photo
    public void onPickPhoto() {

        Log.d(TAG, "failed to create directory2");
        // Create intent for picking a photo from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Bring up gallery to select a photo
            Log.d(TAG, "failed to create directory3");
            startActivityForResult(intent, PICK_PHOTO_CODE);
        }
    }

    public Bitmap loadFromUri(Uri photoUri) {
        Bitmap image = null;
        try {

            if (Build.VERSION.SDK_INT > 27) {

                ImageDecoder.Source source = ImageDecoder.createSource(getContext().getContentResolver(), photoUri);
                image = ImageDecoder.decodeBitmap(source);
            } else {

                image = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), photoUri);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void onLaunchCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        photoFile = getPhotoFileUri(photoFileName);

        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(getContext().getPackageManager()) != null) {

            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Bitmap takenImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());// rotateBitmapOrientation(photoFile.getAbsolutePath());

                ivProfileImage.setImageBitmap(takenImage);
            } else {

                Snackbar.make(ivProfileImage, "Picture wasn't taken!", Snackbar.LENGTH_LONG).show();

            }

        }
        if (requestCode == GALLERY_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
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

    public File getPhotoFileUri(String fileName) {

        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);

        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory");
        }

        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);

        return file;
    }

    private void updateUserWithPhoto(ParseUser currentUser, File photoFile) {

        ParseFile photo = new ParseFile(photoFile);

        Log.i("SAVING", "SAVING IMAGE");
        photo.saveInBackground(new SaveCallback() {
            @Override
            public void done(com.parse.ParseException e) {
                // Handle success or failure here ...

                if (e != null) {
                    Log.i("NOT SAVING", "Error: " + e);
                } else {
                    currentUser.put(KEY_PROFILE_PICTURE, photo);
                    Log.i("SAVING", "SAVING IMAGE");
                    currentUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(com.parse.ParseException e) {
                            // Handle success or failure here ...
                            if (e != null) {
                                Log.i("NOT SAVING", "Error: " + e);
                            } else {
                                Log.i("SAVING", "SAVING IMAGE");
                            }
                        }
                    });
                }
            }
        });

        try {
            currentUser.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onFinishSelectCameraDialog(boolean chooseCamera) {

        if (chooseCamera) {
            onLaunchCamera();
            ParseUser currentUser = ParseUser.getCurrentUser();
            updateUserWithPhoto(currentUser, photoFile);
        } else {
            verifyStoragePermissions(getActivity());
            Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(i, GALLERY_IMAGE_ACTIVITY_REQUEST_CODE);
            Log.d(TAG, "failed to create directory");

        }

    }

    public View getCameraView() {
        return viewProfile;
    }

    protected int numNewMessages() {

        int[] numUnread = {0};

        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);

        query.whereEqualTo(Message.KEY_RECIEVER, ParseUser.getCurrentUser());
        query.whereEqualTo(Message.KEY_ISUNREAD, true);

        query.findInBackground(new FindCallback<Message>() {
            @Override
            public void done(List<Message> messages, ParseException e) {

                if (e != null) {
                    Log.e(TAG, "Issue with getting messages", e);

                    return;
                } else {

                    int count = messages.size();
                    if (count == 1) {
                        tvNumUnread.setText("You have " + count + " new message!");
                    } else {
                        tvNumUnread.setText("You have " + count + " new messages!");
                    }
                }

            }

        });

        return numUnread[0];
    }

    private void goAllNotesActivity() {
        Intent intent = new Intent(getContext(), AllNotesActivity.class);

        getContext().startActivity(intent);

    }

    private void goFriendRequestActivity() {
        Intent intent = new Intent(getContext(), AddFriendActivity.class);

        getContext().startActivity(intent);

    }

    private void goGroupActivity() {
        Intent intent = new Intent(getContext(), GroupActivity.class);

        getContext().startActivity(intent);

    }

    private void goAddGroupActivity() {
        Intent intent = new Intent(getContext(), AddGroupActivity.class);

        getContext().startActivity(intent);

    }

    private void goCreateGroupActivity() {
        Intent intent = new Intent(getContext(), GroupActivity.class);

        getContext().startActivity(intent);

    }

    private void goAcceptFriendRequestActivity() {
        Intent intent = new Intent(getContext(), FriendsRequestListActivity.class);

        getContext().startActivity(intent);

    }

    public Bitmap rotateBitmapOrientation(String photoFilePath) {

        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFilePath, bounds);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(photoFilePath, opts);

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(photoFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);

        return rotatedBitmap;
    }

    private void goLoginActivity() {
        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivity(i);

        getActivity().finish();

    }

}
