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

@ParseClassName("GroupToMembers")
public class GroupToMembers extends ParseObject {


    public static final String KEY_USERNAME = "username";
    public static final String KEY_ASSIGNEDUSER = "assignedUser";
    public static final String KEY_GROUPID = "groupID";


    public String getAssignedUser(){
        return getString(KEY_ASSIGNEDUSER);
    }

    public void setAssignedUser(String assignedUser){
        put(KEY_ASSIGNEDUSER, assignedUser);
    }


    public String getUsername(){
        return getString(KEY_USERNAME);
    }

    public void setUsername(String user){
        put(KEY_USERNAME, user);
    }


    public Group getGroupID(){
        return (Group) getParseObject(KEY_GROUPID);
    }

    public void setGroupID(Group group){
        put(KEY_GROUPID, group);
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


