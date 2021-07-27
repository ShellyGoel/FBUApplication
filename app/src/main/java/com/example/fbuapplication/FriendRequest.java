package com.example.fbuapplication;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;

@ParseClassName("FriendRequest")
public class FriendRequest extends  ParseObject {



        public static final String KEY_STATUS = "status";
        public static final String KEY_FROMUSER = "fromUser";
        public static final String KEY_TOUSER = "toUser";


        public String getStatus(){
            return getString(KEY_STATUS);
        }

        public void setStatus(String description){
            put(KEY_STATUS, description);
        }


        public ParseUser getFromUser(){
            return getParseUser(KEY_FROMUSER);
        }

        public void setFromUser(ParseUser user){
            put(KEY_FROMUSER, user);
        }


        public ParseUser getToUser(){
            return getParseUser(KEY_TOUSER);
        }

        public void setToUser(ParseUser user){
            put(KEY_TOUSER, user);
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


