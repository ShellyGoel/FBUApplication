package com.example.fbuapplication.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fbuapplication.R;
import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseUser;

//class to sign up user.
public class SignUpActivity extends AppCompatActivity {

    public static final String TAG = "SignUpActivity";
    private EditText etSignUsername;
    private EditText etSignPassword;
    private EditText etSignEmail;
    private EditText etFullName;
    private Button btnSignLogin;

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
                createUser(username, password, email, full_name);
            }
        });

    }

    public void createUser(String username, String password, String email, String full_name) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.put("full_name", full_name);

        user.signUpInBackground(e -> {
            if (e == null) {

                Snackbar.make(btnSignLogin, "Successful sign up! logging in...", Snackbar.LENGTH_LONG).show();

                goMainActivity();
            } else {

                Log.e(TAG, "issue with sign up", e);

                Snackbar.make(btnSignLogin, e.getMessage() + " Please try again.", Snackbar.LENGTH_LONG).show();

            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);

        finish();

    }

}