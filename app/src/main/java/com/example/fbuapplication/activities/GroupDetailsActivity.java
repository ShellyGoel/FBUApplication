package com.example.fbuapplication.activities;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fbuapplication.R;
import com.example.fbuapplication.fragments.ComposeFragment;

//class to see group details for each group
public class GroupDetailsActivity extends AppCompatActivity {

    TextView tvDate;
    TextView tvGroupMembers;
    TextView tvIntroMessage;
    Button tvAssignedUser;
    TextView tvCategory;
    TextView tvGroupName;
    ImageView ivGroupStickyNoteImageDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_details);

        FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        final ComposeFragment composeFragment = new ComposeFragment();
        tvDate = findViewById(R.id.tvGroupAddedDate);
        tvGroupMembers = findViewById(R.id.tvGroupMembers);
        tvIntroMessage = findViewById(R.id.tvIntroMessage);
        tvAssignedUser = findViewById(R.id.tvAssignedUser);
        tvCategory = findViewById(R.id.tvCategory);
        tvGroupName = findViewById(R.id.tvGroupDetailsName);
        ivGroupStickyNoteImageDetails = findViewById(R.id.ivGroupStickyNoteImageDetails);

        String createdAt = getIntent().getStringExtra("createdAt");
        String groupMembers = getIntent().getStringExtra("groupMembers");

        String introMessage = getIntent().getStringExtra("introMessage");
        String assignedUser = getIntent().getStringExtra("assignedUser");
        String category = getIntent().getStringExtra("category");
        String groupName = getIntent().getStringExtra("groupName");

        tvDate.setText("Created " + createdAt + " ago");

        tvGroupMembers.setMovementMethod(new ScrollingMovementMethod());
        tvGroupMembers.setText("Group Members: " + groupMembers);

        tvIntroMessage.setMovementMethod(new ScrollingMovementMethod());
        tvIntroMessage.setText("Intro Message: " + introMessage);

        tvAssignedUser.setMovementMethod(new ScrollingMovementMethod());
        tvAssignedUser.setText("Assigned User: " + assignedUser);

        tvCategory.setMovementMethod(new ScrollingMovementMethod());
        tvCategory.setText("Category: " + category);

        tvGroupName.setMovementMethod(new ScrollingMovementMethod());
        tvGroupName.setText("Group Name: " + groupName);

        tvAssignedUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("sendingTo", assignedUser);
                composeFragment.setArguments(bundle);
                fragmentTransaction.add(R.id.frameLayout, composeFragment).commit();

            }
        });

    }
}