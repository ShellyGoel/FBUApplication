package com.example.fbuapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fbuapplication.R;

import java.util.Random;

public class GroupDetailsActivity extends AppCompatActivity {

    TextView tvDate;
    TextView tvGroupMembers;
    TextView tvIntroMessage;
    TextView tvAssignedUser;
    TextView tvCategory;
    ImageView ivStickyNoteImageDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);
        tvDate = findViewById(R.id.tvDate);
        tvGroupMembers = findViewById(R.id.tvGroupMembers);
        tvIntroMessage = findViewById(R.id.tvIntroMessage);
        tvAssignedUser = findViewById(R.id.tvAssignedUser);
        tvCategory = findViewById(R.id.tvCategory);

        String createdAt = getIntent().getStringExtra("createdAt");
        String groupMembers = getIntent().getStringExtra("groupMembers");

        String introMessage = getIntent().getStringExtra("introMessage");
        String assignedUser = getIntent().getStringExtra("assignedUser");
        String category = getIntent().getStringExtra("category");

        tvDate.setText(createdAt);

        tvGroupMembers.setMovementMethod(new ScrollingMovementMethod());
        tvGroupMembers.setText(groupMembers);

        tvIntroMessage.setMovementMethod(new ScrollingMovementMethod());
        tvIntroMessage.setText(introMessage);

        tvAssignedUser.setMovementMethod(new ScrollingMovementMethod());
        tvAssignedUser.setText(assignedUser);

        tvCategory.setMovementMethod(new ScrollingMovementMethod());
        tvCategory.setText(category);

        Random r = new Random();

        Glide.with(this).load(R.drawable._removal_ai__tmp_60ebbf1103f00).into(ivStickyNoteImageDetails);
    }
}