package com.example.fbuapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.fbuapplication.R;
import com.facebook.AccessToken;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.FacebookRequestError;
import com.facebook.LoginStatusCallback;
import com.facebook.login.LoginManager;
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

import com.facebook.GraphRequest;
//import com.parse.facebook.ParseFacebookUtils;
import org.json.JSONException;


import dyanamitechetan.vusikview.VusikView;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button sign_up;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private Boolean wrongButtonClicked;
    AccessToken accessToken;


    private static final String TAG = "LoginActivity";
//
//    private static final String EMAIL = "email";
   public static final String PROFILE = "public_profile";
//    private static final String AUTH_TYPE = "rerequest";

    private static final String EMAIL = "email";

    private CallbackManager mCallbackManager;
    private VusikView vusikView;



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
       // ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.login_button);
        etUsername = findViewById(R.id.etSignUsername);
        etPassword = findViewById(R.id.etSignPassword);
        btnLogin = findViewById(R.id.btnSignLogin);
        sign_up = findViewById(R.id.sign_up);

        wrongButtonClicked = false;


        accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

//        vusikView = findViewById(R.id.vusik);
//        vusikView.start();
//
//        vusikView = (VusikView) findViewById(R.id.vusik);
//        int[]  myImageList = new int[]{R.drawable._removal_ai__tmp_60ebbf1103f00, R.drawable._removal_ai__tmp_60ebbf43c2076, R.drawable._removal_ai__tmp_60ebbf5282318, R.drawable._removal_ai__tmp_60ebbf5fd350d};
//        vusikView
//                .setImages(myImageList)
//                .start();




        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        // If you are using in a fragment, call loginButton.setFragment(LoginActivity.this);

        // Callback registration

        callbackManager = CallbackManager.Factory.create();


        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code

                        Log.i(TAG, "Success");
                        getUserDetailFromFB();
                    }

                    @Override
                    public void onCancel() {
                        // App code

                        Log.i(TAG, "Cancelled");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code

                        Log.i(TAG, "Error"+exception);
                    }
                });





        LoginManager.getInstance().retrieveLoginStatus(this, new LoginStatusCallback() {
            @Override
            public void onCompleted(AccessToken accessToken) {
                // User was previously logged in, can log them in directly here.
                // If this callback is called, a popup notification appears that says
                // "Logged in as <User Name>"
                Log.i(TAG, "Success LOGIN");

                GraphRequest request = GraphRequest.newMeRequest(accessToken, (object, response) -> {
                    FacebookRequestError error = response.getError();
                    if (error != null) {
                        // handle your error
                        return;
                    }

                    try {
                        loginUser(object.getString("name"), "facebook_user", false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                });
            }
            @Override
            public void onFailure() {
                // No access token could be retrieved for the user
            }
            @Override
            public void onError(Exception exception) {
                // An error occurred
            }
        });


//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getUserDetailFromFB();
//            }
//        });

        if(ParseUser.getCurrentUser()!=null){
            //means that someone is signed in already, go to main activity directly
            goMainActivity();

        }


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //vusikView.pauseNotesFall();

               //wrongButtonClicked = true;
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                //vusikView.resumeNotesFall();
                loginUser(username, password,true);
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goSignUpActivity();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "2");

                getUserDetailFromFB();
            }
        });

    }

    public boolean isLoggedIn(){
        AccessToken aToken = AccessToken.getCurrentAccessToken();
        return aToken != null;
    }

    private void loginUser(String username, String password,boolean isAppButton){
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

                    if(user.get("facebook_login") !=null && (boolean) user.get("facebook_login") && isAppButton){
                        Snackbar.make(btnLogin, "You have to login via facebook.", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    else {
                        goMainActivity();
                        //Toast.makeText(LoginActivity.LoginActivity.this, "Success!", Toast.LENGTH_LONG).show();
                        Snackbar.make(btnLogin, "Success! Logging in...", Snackbar.LENGTH_LONG).show();
                    }

                }
            }
        });
    }

    private void goMainActivity(){
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);
        //finish login activity once we have navigated to the next activity
        finish();

    }

    private void goSignUpActivity(){
        Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(i);

    }

    private void getUserDetailFromFB() {
        Log.i(TAG, "entered");



        AccessToken accessToken1 = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(accessToken1, (object, response) -> {
            FacebookRequestError error = response.getError();
            if (error != null) {
                // handle your error
                return;
            }


            if (isLoggedIn()) {
                try {
                    Log.i(TAG, "already logged" + object.toString());

                    loginUser(object.getString("name"), "facebook_user", false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {


            Log.i(TAG, object.toString());


            if (ParseUser.getCurrentUser() == null) {
                Log.i(TAG, "2" + object.toString());

                ParseUser user = new ParseUser();
                try {
                    if (object.has("name")) {
                        user.setUsername(object.getString("name"));
                        user.put("full_name", object.getString("name"));
                    } else {
                        user.setUsername("");
                    }
                    if (object.has("email"))
                        user.setEmail(object.getString("email"));
                    else {
                        user.setEmail("");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                user.put("facebook_login", true);
                user.setPassword("facebook_user");
                Log.i(TAG, "2" + object.toString());

                user.signUpInBackground(e -> {
                    if (e == null) {
                        // Hooray! Let them use the app now.
                        Log.i(TAG, "Successful signup");
                        //Toast.makeText(this, "Successful sign up! logging in...", Toast.LENGTH_LONG).show();
                        //Snackbar.make(btnLogin, "Successful sign up! logging in...", Snackbar.LENGTH_LONG).show();
                        Log.i(TAG, "2" + object.toString());

                        goMainActivity();
                    } else {
                        // Sign up didn't succeed. Look at the ParseException
                        // to figure out what went wrong
                        Log.e(TAG, "issue with sign up", e);
                        //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        // Snackbar.make(btnLogin, e.getMessage() + " Please try again.", Snackbar.LENGTH_LONG).show();

                    }
                });

            } else {
                Log.i(TAG, "3" + object.toString());

                goMainActivity();
            }

        }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }




}
