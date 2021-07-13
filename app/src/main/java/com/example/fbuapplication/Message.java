package com.example.fbuapplication;


import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("Message")
public class Message extends ParseObject {

    public static final String KEY_MESSAGE_BODY = "message_body";
    public static final String KEY_UNREAD = "unread";
    public static final String KEY_SENDER = "sender";
    public static final String KEY_RECIEVER = "reciever";

    public String getMessageBody(){
        return getString(KEY_MESSAGE_BODY);
    }

    public void setMessageBody(String description){
        put(KEY_MESSAGE_BODY, description);
    }

    public boolean getUnread(){
        return getBoolean(KEY_UNREAD);
    }

    public void setUnread(boolean unread){
        put(KEY_UNREAD,unread);
    }

    public ParseUser getSender(){
        return getParseUser(KEY_SENDER);
    }

    public void setSender(ParseUser user){
        put(KEY_SENDER, user);
    }


    public ParseUser getReciever(){
        return getParseUser(KEY_RECIEVER);
    }

    public void setReciever(ParseUser user){
        put(KEY_RECIEVER, user);
    }



    public static String calculateTimeAgo(Date createdAt) {

        int SECOND_MILLIS = 1000;
        int MINUTE_MILLIS = 60 * SECOND_MILLIS;
        int HOUR_MILLIS = 60 * MINUTE_MILLIS;
        int DAY_MILLIS = 24 * HOUR_MILLIS;

        try {
            createdAt.getTime();
            long time = createdAt.getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + " m";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " h";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + " d";
            }
        } catch (Exception e) {
            Log.i("Error:", "getRelativeTimeAgo failed", e);
            e.printStackTrace();
        }

        return "";
    }

}
