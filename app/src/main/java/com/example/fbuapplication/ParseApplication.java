package com.example.fbuapplication;


import android.app.Application;

import com.example.fbuapplication.ParseModels.FriendRequest;
import com.example.fbuapplication.ParseModels.Group;
import com.example.fbuapplication.ParseModels.GroupToMembers;
import com.example.fbuapplication.ParseModels.Message;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.parse.Parse;
import com.parse.ParseObject;
//import com.parse.facebook.ParseFacebookUtils;
//import com.parse.facebook.ParseFacebookUtils;

public class ParseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        //FacebookSdk.sdkInitialize(getApplicationContext());
        // Register your parse models
        ParseObject.registerSubclass(Message.class);
        ParseObject.registerSubclass(FriendRequest.class);
        ParseObject.registerSubclass(Group.class);
        ParseObject.registerSubclass(GroupToMembers.class);

        // Within the Android Application where Parse is initialized
        Parse.enableLocalDatastore(this);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("Y3SvrsAgWAlKviTxUyemv0A1HxnRSZ81JBNz0CV7")
                .clientKey("QwwjT0AYA0fG6vstrKXGENwmKEVUxquAso3utQCz")
                .server("https://fbuapp.b4a.io")
                .build()
        );
        //ParseFacebookUtils.initialize(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

    }
}
