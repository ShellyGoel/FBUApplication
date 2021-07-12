package com.example.fbuapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.parse.Parse;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("Y3SvrsAgWAlKviTxUyemv0A1HxnRSZ81JBNz0CV7")
                .clientKey("QwwjT0AYA0fG6vstrKXGENwmKEVUxquAso3utQCz")
                .server("https://fbuapp.b4a.io")
                .build()
        );
    }
}