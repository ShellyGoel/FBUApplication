package com.example.fbuapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseUser;


public class SignUpActivity extends AppCompatActivity {


    private EditText etSignUsername;
    private EditText etSignPassword;
    private EditText etSignEmail;
    private EditText etFullName;
    private Button btnSignLogin;
    public static final String TAG = "SignUpActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        etSignUsername = findViewById(R.id.etSignUsername);
        etSignPassword = findViewById(R.id.etSignPassword);
        btnSignLogin = findViewById(R.id.btnSignLogin);
        etSignEmail = findViewById(R.id.etSignEmail);
        etFullName = findViewById(R.id.etSignFullName);


        btnSignLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etSignUsername.getText().toString();
                String password = etSignPassword.getText().toString();
                String email = etSignEmail.getText().toString();
                String full_name = etFullName.getText().toString();
                createUser(username,password, email, full_name);
            }
        });

    }

    public void createUser(String username, String password, String email, String full_name) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.put("full_name", full_name);
        //user.setEmail("email@example.com");

        // Other fields can be set just like any other ParseObject,
        // using the "put" method, like this: user.put("attribute", "its value");
        // If this field does not exists, it will be automatically created

        user.signUpInBackground(e -> {
            if (e == null) {
                // Hooray! Let them use the app now.
                Log.i(TAG, "Successful signup");
                //Toast.makeText(this, "Successful sign up! logging in...", Toast.LENGTH_LONG).show();
                Snackbar.make(btnSignLogin, "Successful sign up! logging in...", Snackbar.LENGTH_LONG).show();

                goMainActivity();
            } else {
                // Sign up didn't succeed. Look at the ParseException
                // to figure out what went wrong
                Log.e(TAG, "issue with sign up",e);
                //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                Snackbar.make(btnSignLogin, e.getMessage()+" Please try again.", Snackbar.LENGTH_LONG).show();

            }
        });
    }
    private void goMainActivity(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        //TODO:finish signup activity once we have navigated to the next activity
        finish();

    }

}