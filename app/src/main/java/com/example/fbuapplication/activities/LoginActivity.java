package com.example.fbuapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.fbuapplication.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.GraphRequest;
import com.facebook.LoginStatusCallback;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.snackbar.Snackbar;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.json.JSONException;

import java.util.Arrays;

import dyanamitechetan.vusikview.VusikView;

public class LoginActivity extends AppCompatActivity {

    public static final String PROFILE = "public_profile";
    private static final String TAG = "LoginActivity";
    private static final String EMAIL = "email";
    AccessToken accessToken;
    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button sign_up;
    private LoginButton loginButton;
    private CallbackManager callbackManager;

    private Boolean wrongButtonClicked;
    private CallbackManager mCallbackManager;
    private VusikView vusikView;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
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

        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        getUserDetailFromFB();
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException exception) {

                    }
                });

        LoginManager.getInstance().retrieveLoginStatus(this, new LoginStatusCallback() {
            @Override
            public void onCompleted(AccessToken accessToken) {

                GraphRequest request = GraphRequest.newMeRequest(accessToken, (object, response) -> {
                    FacebookRequestError error = response.getError();
                    if (error != null) {
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

            }

            @Override
            public void onError(Exception exception) {

            }
        });

        if (ParseUser.getCurrentUser() != null) {

            goMainActivity();

        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                loginUser(username, password, true);
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

                getUserDetailFromFB();
            }
        });

    }

    public boolean isLoggedIn() {
        AccessToken aToken = AccessToken.getCurrentAccessToken();
        return aToken != null;
    }

    private void loginUser(String username, String password, boolean isAppButton) {

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {

                    Snackbar.make(btnLogin, e.getMessage() + " Please try again.", Snackbar.LENGTH_LONG).show();
                    Log.e(TAG, "issue with login" + e, e);

                    YoYo.with(Techniques.Shake)
                            .duration(100)
                            .repeat(3)
                            .playOn(etUsername);

                    YoYo.with(Techniques.Shake)
                            .duration(100)
                            .repeat(3)
                            .playOn(etPassword);

                } else {

                    if (user.get("facebook_login") != null && (boolean) user.get("facebook_login") && isAppButton) {
                        Snackbar.make(btnLogin, "You have to login via facebook.", Snackbar.LENGTH_LONG).show();
                        return;
                    } else {
                        goMainActivity();

                        Snackbar.make(btnLogin, "Success! Logging in...", Snackbar.LENGTH_LONG).show();
                    }

                }
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(i);

        finish();

    }

    private void goSignUpActivity() {
        Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(i);

    }

    private void getUserDetailFromFB() {

        AccessToken accessToken1 = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newMeRequest(accessToken1, (object, response) -> {
            FacebookRequestError error = response.getError();
            if (error != null) {

                return;
            }

            if (isLoggedIn()) {
                try {

                    loginUser(object.getString("name"), "facebook_user", false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {

                if (ParseUser.getCurrentUser() == null) {

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

                    user.signUpInBackground(e -> {
                        if (e == null) {

                            goMainActivity();
                        } else {

                            Log.e(TAG, "issue with sign up", e);

                        }
                    });

                } else {

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
