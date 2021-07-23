package com.example.fbuapplication;


import android.util.Log;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;
import androidx.room.Entity;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity
@ParseClassName("Message")
public class Message extends ParseObject {

    public static final String KEY_MESSAGE_BODY = "message_body";
    public static final String KEY_SENDER = "sender";
    public static final String KEY_RECIEVER = "reciever";
    public static final String KEY_ISPINNED = "isPinned";
    public static final String KEY_ISUNREAD = "unread";

    public static final String KEY_ISKUDOS = "isKudos";
    public static final String KEY_ISMEMORIES = "isMemories";
    public static final String KEY_ISGOALS = "isGoals";

    @PrimaryKey(autoGenerate=true)
    String description;

    @ColumnInfo
    Date createdAt;

    @Ignore
    boolean unread;

    @Ignore
    ParseUser receiver;

    @Ignore
    ParseUser sender;

    @Ignore
    boolean isPinned;


    public String getMessageBody(){
        return getString(KEY_MESSAGE_BODY);
    }

    public void setMessageBody(String description){
        put(KEY_MESSAGE_BODY, description);
    }


    public ParseUser getSender(){
        return getParseUser(KEY_SENDER);
    }

    public void setSender(ParseUser user){
        put(KEY_SENDER, user);
    }


    public ParseUser getReceiver(){
        return getParseUser(KEY_RECIEVER);
    }

    public void setReceiver(ParseUser user){
        put(KEY_RECIEVER, user);
    }

    public boolean getIsPinned(){
        return getBoolean(KEY_ISPINNED);
    }

    public void setIsPinned(boolean isPinned){
        put(KEY_ISPINNED, isPinned);
    }

    public boolean getIsUnread(){
        return getBoolean(KEY_ISUNREAD);
    }

    public void setIsUnread(boolean isUnread){
        put(KEY_ISUNREAD, isUnread);
    }

    public boolean getIsKudos(){
        return getBoolean(KEY_ISKUDOS);
    }

    public void setIsKudos(boolean isKudos){
        put(KEY_ISKUDOS, isKudos);
    }

    public boolean getIsMemories(){
        return getBoolean(KEY_ISMEMORIES);
    }

    public void setIsmemories(boolean isMemories){
        put(KEY_ISMEMORIES, isMemories);
    }

    public boolean getIsGoals(){
        return getBoolean(KEY_ISGOALS);
    }

    public void setIsGoals(boolean isGoals){
        put(KEY_ISGOALS, isGoals);
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
