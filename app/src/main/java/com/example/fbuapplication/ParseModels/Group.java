package com.example.fbuapplication.ParseModels;

import android.util.Log;
//
//import androidx.room.ColumnInfo;
//import androidx.room.Ignore;
//import androidx.room.PrimaryKey;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.Date;

@ParseClassName("Group")
public class Group extends ParseObject {

    public static final String KEY_FROMUSER = "fromUser";
    public static final String KEY_TOUSERS = "toUsers";
    public static final String KEY_ASSIGNEDUSER = "assignedUser";

    public static final String KEY_INTROMESSAGE= "introMessage";
    public static final String KEY_CATEGORYSELECTED = "categorySelected";
    public static final String KEY_GROUPNAME = "groupName";


    public String getAssignedUser(){
        return getString(KEY_ASSIGNEDUSER);
    }

    public void setAssignedUser(String assignedUser){
        put(KEY_ASSIGNEDUSER, assignedUser);
    }

    public String getIntroMessage(){
        return getString(KEY_INTROMESSAGE);
    }

    public void setIntroMessage(String message){
        put(KEY_INTROMESSAGE, message);
    }

    public String getCategory(){
        return getString(KEY_CATEGORYSELECTED);
    }

    public void setCategory(String message){
        put(KEY_CATEGORYSELECTED, message);
    }

    public String getGroupName(){
        return getString(KEY_GROUPNAME);
    }

    public void setGroupName(String message){
        put(KEY_GROUPNAME, message);
    }

    public String getToUsers(){
        return getString(KEY_TOUSERS);
    }

    public void setToUsers(String toUsers){
        put(KEY_TOUSERS, toUsers);
    }


    public ParseUser getFromUser(){
        return getParseUser(KEY_FROMUSER);
    }

    public void setFromUser(ParseUser user){
        put(KEY_FROMUSER, user);
    }


//    public JSONArray getToUsers(){
//        return getJSONArray(KEY_TOUSERS);
//    }
//
//    public void setToUsers(ParseUser[] users){
//        put(KEY_TOUSERS, users);
//    }
//



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


