package com.example.fbuapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class MessageDetailsActivity extends AppCompatActivity {

    TextView tvDate;
    TextView tvDescription;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);
        tvDate = findViewById(R.id.tvDate);
        tvDescription = findViewById(R.id.tvMessageBody);

        String createdAt = getIntent().getStringExtra("createdAt");
        String description = getIntent().getStringExtra("caption");
        // set the title and overview
        //Log.i("postdetails",createdAt);
        tvDate.setText(createdAt);
        tvDescription.setText(description);

    }
}