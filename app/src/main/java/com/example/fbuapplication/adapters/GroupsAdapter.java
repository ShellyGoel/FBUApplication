package com.example.fbuapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fbuapplication.ParseModels.Group;
import com.example.fbuapplication.R;
import com.example.fbuapplication.activities.GroupDetailsActivity;
import com.example.fbuapplication.activities.MessageDetailsActivity;
import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Date;
import java.util.List;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ViewHolder> {
    private Context context;

    private List<Group> addGroups;
    private int adapterPosition;


    public int getAdapterPosition() {
        return adapterPosition;
    }

    public int getPosition() {
        return position;
    }

    private int position;

    public GroupsAdapter(Context context, List<Group> addGroups) {
        this.context = context;
        this.addGroups = addGroups;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_group, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Group addGroup = addGroups.get(position);
        try {
            holder.bind(addGroup);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return addGroups.size();
    }


    public void removeItem(int position) {
        addGroups.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Group addGroup, int position) {
        addGroups.add(position, addGroup);
        notifyItemInserted(position);
    }



    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView btnRemove;
        private TextView tvGroupName;
        private TextView tvGroupAddedDate;

        public static final String TAG = "InboxAdapter";

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnRemove = itemView.findViewById(R.id.btnRemove);
            tvGroupName = itemView.findViewById(R.id.tvGroupName);
            tvGroupAddedDate = itemView.findViewById(R.id.tvGroupAddedDate);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // gets item position
                    int position = getAdapterPosition();

                    // make sure the position is valid, i.e. actually exists in the view
                    if (position != RecyclerView.NO_POSITION) {
                        // get the movie at the position, this won't work if the class is static
                        Group addGroup = addGroups.get(position);
                        Date createdAt = addGroup.getCreatedAt();
                        String timeAgo = Group.calculateTimeAgo(createdAt);

                        Intent intent = new Intent(context, GroupDetailsActivity.class);
                        // serialize the movie using parceler, use its short name as a key
                        intent.putExtra("createdAt", timeAgo);
                        intent.putExtra("groupMembers","TO:DO");
                        intent.putExtra("introMessage",addGroup.getIntroMessage());
                        intent.putExtra("assignedUser","TO:DO");
                        intent.putExtra("category",addGroup.getCategory());


                        // show the activity
                        context.startActivity(intent);


                    }
                }
            });
        }


        public void bind(Group addGroup) throws ParseException {
            // Bind the addGroup data to the view elements

            ParseObject textMessageContent=addGroup.getFromUser().fetchIfNeeded();
            tvGroupName.setText((textMessageContent.get("username").toString()));

            Glide.with(context).load(R.drawable.ic_baseline_person_remove_24).into(btnRemove);
            itemView.setBackgroundColor(itemView.getResources().getColor(R.color.white));

            Date createdAt = addGroup.getCreatedAt();
            String timeAgo = Group.calculateTimeAgo(createdAt);
            tvGroupAddedDate.setText(timeAgo);





            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ParseUser currentUser = ParseUser.getCurrentUser();

                    //check which wall to pin to
                    adapterPosition = getPosition();
                    itemView.setBackgroundColor(itemView.getResources().getColor(R.color.gray_out));

                    Glide.with(context).load(R.drawable.ic_baseline_person_remove_243).into(btnRemove);


                   // addGroup.setStatus("unfriended");

                    addGroup.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e != null){
                                Log.e(TAG, "Error while accepting friend request",e);
                                //Toast.makeText(context, "Error in pinning note!", Toast.LENGTH_SHORT).show();
                                Snackbar.make(btnRemove, "Error while unfriending", Snackbar.LENGTH_LONG).show();

                            }

                        }
                    });


                }
            });
        }


    }

    /* Within the RecyclerView.Adapter class */

    // Clean all elements of the recycler
    public void clear() {
        addGroups.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Group> list) {
        addGroups.addAll(list);
        notifyDataSetChanged();
    }

    public int getSize() {
        return addGroups.size();
    }



}