package com.example.fbuapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.snackbar.Snackbar;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;


public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private Button sign_up;


    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(ParseUser.getCurrentUser()!=null){
            //means that someone is signed in already, go to main activity directly
            goMainActivity();

        }

        etUsername = findViewById(R.id.etSignUsername);
        etPassword = findViewById(R.id.etSignPassword);
        btnLogin = findViewById(R.id.btnSignLogin);
        sign_up = findViewById(R.id.sign_up);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
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
}
