package com.example.fbuapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Random;

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
        // set the title and overview
        //Log.i("postdetails",createdAt);
        tvDate.setText(createdAt);
        tvDescription.setMovementMethod(new ScrollingMovementMethod());
        tvDescription.setText(description);

        Random r = new Random();
        int idClicked = r.nextInt(4);
        int stickyNote;

        switch (idClicked)
        {
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