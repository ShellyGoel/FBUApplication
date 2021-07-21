package com.example.fbuapplication;


import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.facebook.ParseFacebookUtils;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FacebookSdk.sdkInitialize(getApplicationContext());
        // Register your parse models
        ParseObject.registerSubclass(Message.class);
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("Y3SvrsAgWAlKviTxUyemv0A1HxnRSZ81JBNz0CV7")
                .clientKey("QwwjT0AYA0fG6vstrKXGENwmKEVUxquAso3utQCz")
                .server("https://fbuapp.b4a.io")
                .build()
        );
        ParseFacebookUtils.initialize(this);

    }
}
