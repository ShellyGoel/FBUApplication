package com.example.fbuapplication.activities;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.fbuapplication.R;

import java.util.Random;

//class to view full text of messages in the inbox or on a user's wall
public class MessageDetailsActivity extends AppCompatActivity {

    TextView tvDate;
    TextView tvDescription;
    ImageView ivStickyNoteImageDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        tvDate = findViewById(R.id.tvDate);
        tvDescription = findViewById(R.id.tvMessageBody);
        ivStickyNoteImageDetails = findViewById(R.id.ivStickyNoteImageDetails);

        String createdAt = getIntent().getStringExtra("createdAt");
        String description = getIntent().getStringExtra("caption");

        tvDate.setText(createdAt);
        tvDescription.setMovementMethod(new ScrollingMovementMethod());
        tvDescription.setText(description);

        Random r = new Random();
        int idClicked = r.nextInt(4);
        int stickyNote;

        switch (idClicked) {
            case 0:
                stickyNote = R.drawable._removal_ai__tmp_60ebbfcbb11a1;

                break;
            case 1:
                stickyNote = R.drawable._removal_ai__tmp_60ebbfb3c4dd3;

                break;
            case 2:
                stickyNote = R.drawable._removal_ai__tmp_60ebbf43c2076;

                break;
            case 3:
                stickyNote = R.drawable._removal_ai__tmp_60ebbf6e0f434;

                break;
            default:
                stickyNote = R.drawable._removal_ai__tmp_60ebbf1103f00;
        }

        Glide.with(this).load(stickyNote).into(ivStickyNoteImageDetails);
    }
}