package com.example.fbuapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.snackbar.Snackbar;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.Arrays;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import android.app.AlertDialog;
import android.app.ProgressDialog;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.parse.facebook.ParseFacebookUtils;
import org.json.JSONException;

import java.util.Collection;

import dyanamitechetan.vusikview.VusikView;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button sign_up;
    private LoginButton mLoginButton;



    private static final String TAG = "LoginActivity";

    private static final String EMAIL = "email";
    public static final String PROFILE = "public_profile";
    private CallbackManager mCallbackManager;
    private VusikView vusikView;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginButton = findViewById(R.id.login_button);
        etUsername = findViewById(R.id.etSignUsername);
        etPassword = findViewById(R.id.etSignPassword);
        btnLogin = findViewById(R.id.btnSignLogin);
        sign_up = findViewById(R.id.sign_up);

        vusikView = findViewById(R.id.vusik);
        vusikView.start();

        vusikView = (VusikView) findViewById(R.id.vusik);
        int[]  myImageList = new int[]{R.drawable._removal_ai__tmp_60ebbf1103f00, R.drawable._removal_ai__tmp_60ebbf43c2076, R.drawable._removal_ai__tmp_60ebbf5282318, R.drawable._removal_ai__tmp_60ebbf5fd350d};
        vusikView
                .setImages(myImageList)
                .start();
        //FB SDK with Parse:

        mCallbackManager = CallbackManager.Factory.create();
        // If you are using in a fragment, call mLoginButton.setFragment(this);

        // Callback registration
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                FaceBookLogin();
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });


        if(ParseUser.getCurrentUser()!=null){
            //means that someone is signed in already, go to main activity directly
            goMainActivity();

        }


        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FaceBookLogin();
            }
        });



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vusikView.pauseNotesFall();


                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                vusikView.resumeNotesFall();
                loginUser(username, password);
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goSignUpActivity();
            }
        });

    }

//
//    public void parseFbLogin(){
//
//
//        List<String> permissions = Arrays.asList("basic_info", "user_about_me",
//                "user_relationships", "user_birthday", "user_location");
//
//        ParseFacebookUtils.logInWithReadPermissionsInBackground(LoginActivity.this, permissions, new LogInCallback() {
//            @Override
//            public void done(ParseUser user, ParseException err) {
//                if (user == null) {
//                    Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
//                } else if (user.isNew()) {
//                    Log.d("MyApp", "User signed up and logged in through Facebook!");
//                } else {
//                    Log.d("MyApp", "User logged in through Facebook!");
////                            getUserDetailsFromParse();
//                }
//            }
//        });
//    }

    public void FaceBookLogin(){
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Please, wait a moment.");
        dialog.setMessage("Logging in...");
        dialog.show();
        Collection<String> permissions = Arrays.asList("public_profile", "email");
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, (user, err) -> {
            dialog.dismiss();
            if (err != null) {
                Log.e("FacebookLoginExample", "done: ", err);
                Toast.makeText(this, err.getMessage(), Toast.LENGTH_LONG).show();
            } else if (user == null) {
                Toast.makeText(this, "The user cancelled the Facebook login.", Toast.LENGTH_LONG).show();
                Log.d("FacebookLoginExample", "Uh oh. The user cancelled the Facebook login.");
            } else if (user.isNew()) {
                Toast.makeText(this, "User signed up and logged in through Facebook.", Toast.LENGTH_LONG).show();
                Log.d("FacebookLoginExample", "User signed up and logged in through Facebook!");
                getUserDetailFromFB();
            } else {
                Toast.makeText(this, "User logged in through Facebook.", Toast.LENGTH_LONG).show();
                Log.d("FacebookLoginExample", "User logged in through Facebook!");
                showAlert("Oh, you!", "Welcome back!");
            }
        });
    }

    private void loginUser(String username, String password){
        //TODO: navigate to the main activity if the user has signed in properly
        //want to use inBackground bc we don't want to execute on main thread (would prevent user to do anything else). Want to do on a background thread instead
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(e != null){

                    Snackbar.make(btnLogin, e.getMessage()+" Please try again.", Snackbar.LENGTH_LONG).show();
                    Log.e(TAG, "issue with login" + e,e);

                    YoYo.with(Techniques.Shake)
                            .duration(100)
                            .repeat(3)
                            .playOn(etUsername);

                    YoYo.with(Techniques.Shake)
                            .duration(100)
                            .repeat(3)
                            .playOn(etPassword);

                }
                else {
                    goMainActivity();
                    //Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_LONG).show();
                    Snackbar.make(btnLogin, "Success! Logging in...", Snackbar.LENGTH_LONG).show();


                }
            }
        });
    }

    private void goMainActivity(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        //finish login activity once we have navigated to the next activity
        finish();

    }

    private void goSignUpActivity(){
        Intent i = new Intent(this, SignUpActivity.class);
        startActivity(i);

    }
    private void getUserDetailFromFB() {
        GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), (object, response) -> {
            ParseUser user = ParseUser.getCurrentUser();
            try {
                if (object.has("name"))
                    user.setUsername(object.getString("name"));
                if (object.has("email"))
                    user.setEmail(object.getString("email"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            user.saveInBackground(e -> {
                if (e == null) {
                    showAlert("First Time Login!", "Welcome!");
                } else
                    showAlert("Error", e.getMessage());
            });
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void showAlert(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> {
                    dialog.cancel();
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                });
        AlertDialog ok = builder.create();
        ok.show();
    }

}
