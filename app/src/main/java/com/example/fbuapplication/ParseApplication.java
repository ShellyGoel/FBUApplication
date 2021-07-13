package com.example.fbuapplication;


import android.app.Application;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Register your parse models
        ParseObject.registerSubclass(Message.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("Y3SvrsAgWAlKviTxUyemv0A1HxnRSZ81JBNz0CV7")
                .clientKey("QwwjT0AYA0fG6vstrKXGENwmKEVUxquAso3utQCz")
                .server("https://fbuapp.b4a.io")
                .build()
        );
    }
}
