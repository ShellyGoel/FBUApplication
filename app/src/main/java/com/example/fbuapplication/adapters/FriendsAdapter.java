package com.example.fbuapplication.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fbuapplication.ParseModels.FriendRequest;
import com.example.fbuapplication.R;
import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Date;
import java.util.List;

//adapter to populate data in friends recyclerView
public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {
    private final Context context;

    private final List<FriendRequest> addFriends;
    private int adapterPosition;
    private int position;

    public FriendsAdapter(Context context, List<FriendRequest> addFriends) {
        this.context = context;
        this.addFriends = addFriends;
    }

    public int getAdapterPosition() {
        return adapterPosition;
    }

    public int getPosition() {
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_friends, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FriendRequest addFriendRequest = addFriends.get(position);
        try {
            holder.bind(addFriendRequest);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return addFriends.size();
    }

    public void removeItem(int position) {
        addFriends.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(FriendRequest addFriendRequest, int position) {
        addFriends.add(position, addFriendRequest);
        notifyItemInserted(position);
    }

    // Clean all elements of the recycler
    public void clear() {
        addFriends.clear();
        notifyDataSetChanged();
    }

    /* Within the RecyclerView.Adapter class */

    // Add a list of items -- change to type used
    public void addAll(List<FriendRequest> list) {
        addFriends.addAll(list);
        notifyDataSetChanged();
    }

    public int getSize() {
        return addFriends.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public static final String TAG = "InboxAdapter";
        private final ImageView btnRemove;
        private final TextView tvFriendName;
        private final TextView tvFriendAddedDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnRemove = itemView.findViewById(R.id.btnRemove);
            tvFriendName = itemView.findViewById(R.id.tvFriendName);
            tvFriendAddedDate = itemView.findViewById(R.id.tvFriendAddedDate);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // gets item position
                    int position = getAdapterPosition();

                    // make sure the position is valid, i.e. actually exists in the view
                    if (position != RecyclerView.NO_POSITION) {
                        // get the movie at the position, this won't work if the class is static
                        FriendRequest addFriendRequest = addFriends.get(position);
                        Date createdAt = addFriendRequest.getCreatedAt();
                        String timeAgo = FriendRequest.calculateTimeAgo(createdAt);

                    }
                }
            });
        }

        public void bind(FriendRequest addFriendRequest) throws ParseException {
            // Bind the addFriendRequest data to the view elements

            ParseObject textMessageContent = addFriendRequest.getFromUser().fetchIfNeeded();
            tvFriendName.setText((textMessageContent.get("username").toString()));

            Glide.with(context).load(R.drawable.ic_baseline_person_remove_24).into(btnRemove);
            itemView.setBackgroundColor(itemView.getResources().getColor(R.color.white));

            Date createdAt = addFriendRequest.getCreatedAt();
            String timeAgo = FriendRequest.calculateTimeAgo(createdAt);
            tvFriendAddedDate.setText(timeAgo);

            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ParseUser currentUser = ParseUser.getCurrentUser();

                    //check which wall to pin to
                    adapterPosition = getPosition();
                    itemView.setBackgroundColor(itemView.getResources().getColor(R.color.gray_out));

                    Glide.with(context).load(R.drawable.ic_baseline_person_remove_243).into(btnRemove);

                    addFriendRequest.setStatus("unfriended");

                    addFriendRequest.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.e(TAG, "Error while accepting friend request", e);

                                Snackbar.make(btnRemove, "Error while unfriending", Snackbar.LENGTH_LONG).show();

                            }

                        }
                    });

                }
            });
        }

    }

}