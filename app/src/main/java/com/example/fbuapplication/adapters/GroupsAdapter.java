package com.example.fbuapplication.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fbuapplication.ParseModels.Group;
import com.example.fbuapplication.ParseModels.GroupToMembers;
import com.example.fbuapplication.R;
import com.example.fbuapplication.activities.GroupDetailsActivity;
import com.parse.ParseException;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.ViewHolder> {
    private final Context context;

    private final List<GroupToMembers> addGroupToMembers;
    private int adapterPosition;
    private int position;

    public GroupsAdapter(Context context, List<GroupToMembers> addGroupToMembers) {
        this.context = context;
        this.addGroupToMembers = addGroupToMembers;
    }

    public int getAdapterPosition() {
        return adapterPosition;
    }

    public int getPosition() {
        return position;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_group, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GroupToMembers GroupToMembers = addGroupToMembers.get(position);
        try {

            holder.bind(GroupToMembers, position);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return addGroupToMembers.size();
    }

    public void removeItem(int position) {
        addGroupToMembers.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(GroupToMembers addGroupToMembers, int position) {
        addGroupToMembers.add(String.valueOf(position), addGroupToMembers);
        notifyItemInserted(position);
    }

    // Clean all elements of the recycler
    public void clear() {
        addGroupToMembers.clear();
        notifyDataSetChanged();
    }

    /* Within the RecyclerView.Adapter class */

    // Add a list of items -- change to type used
    public void addAll(List<GroupToMembers> list) {
        addGroupToMembers.addAll(list);
        notifyDataSetChanged();
    }

    public int getSize() {
        return addGroupToMembers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public static final String TAG = "InboxAdapter";
        private final ImageView btnRemove;
        private final TextView tvGroupToMembersName;
        private final TextView tvGroupToMembersAddedDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnRemove = itemView.findViewById(R.id.btnGroupRemove);
            tvGroupToMembersName = itemView.findViewById(R.id.tvGroupName);
            tvGroupToMembersAddedDate = itemView.findViewById(R.id.tvGroupAddedDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // gets item position
                    int position = getAdapterPosition();

                    // make sure the position is valid, i.e. actually exists in the view
                    if (position != RecyclerView.NO_POSITION) {
                        // get the movie at the position, this won't work if the class is static
                        GroupToMembers groupToMembers = addGroupToMembers.get(position);
                        Date createdAt = groupToMembers.getCreatedAt();
                        String timeAgo = GroupToMembers.calculateTimeAgo(createdAt);

                        Intent intent = new Intent(context, GroupDetailsActivity.class);
                        // serialize the movie using parceler, use its short name as a key
                        intent.putExtra("createdAt", timeAgo);

                        Group g = groupToMembers.getGroupID();
                        intent.putExtra("groupMembers", g.getToUsers().substring(0, g.getToUsers().length() - 2));
                        intent.putExtra("introMessage", g.getIntroMessage());
                        intent.putExtra("assignedUser", groupToMembers.getAssignedUser());
                        intent.putExtra("category", g.getCategory());
                        intent.putExtra("groupName", g.getGroupName());

                        // show the activity
                        context.startActivity(intent);

                    }
                }
            });
        }

        public void bind(GroupToMembers addGroupToMembers, int position) throws ParseException {
            // Bind the addGroupToMembers data to the view elements

            GroupToMembers gtm = addGroupToMembers.fetchIfNeeded();
            Group g = gtm.getGroupID().fetchIfNeeded();//.getGroupID();
            tvGroupToMembersName.setText(g.getGroupName());

            if (position % 2 == 0) {
                Glide.with(context).load(R.drawable.ic_baseline_people_24).into(btnRemove);
            } else {
                Glide.with(context).load(R.drawable.ic_baseline_people_243).into(btnRemove);
            }
            itemView.setBackgroundColor(itemView.getResources().getColor(R.color.white));

            Date createdAt = addGroupToMembers.getCreatedAt();
            String timeAgo = GroupToMembers.calculateTimeAgo(createdAt);
            tvGroupToMembersAddedDate.setText(timeAgo);

//            btnRemove.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    ParseUser currentUser = ParseUser.getCurrentUser();
//
//                    //check which wall to pin to
//                    adapterPosition = getPosition();
//                    itemView.setBackgroundColor(itemView.getResources().getColor(R.color.gray_out));
//
//                    Glide.with(context).load(R.drawable.ic_baseline_person_remove_243).into(btnRemove);
//
//
//                   // addGroupToMembers.setStatus("unfriended");
//
//                    addGroupToMembers.saveInBackground(new SaveCallback() {
//                        @Override
//                        public void done(ParseException e) {
//                            if(e != null){
//                                Log.e(TAG, "Error while accepting friend request",e);
//
//                                Snackbar.make(btnRemove, "Error while unfriending", Snackbar.LENGTH_LONG).show();
//
//                            }
//
//                        }
//                    });
//
//
//                }
//            });
        }

    }

}