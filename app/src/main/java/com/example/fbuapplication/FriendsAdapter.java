package com.example.fbuapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fbuapplication.fragments.DecidePinnedWallDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Date;
import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {
    private Context context;

    private List<FriendRequest> addFriends;
    private int adapterPosition;


    public int getAdapterPosition() {
        return adapterPosition;
    }

    public int getPosition() {
        return position;
    }

    private int position;

    public FriendsAdapter(Context context, List<FriendRequest> addFriends) {
        this.context = context;
        this.addFriends = addFriends;
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



    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView btnRemove;
        private TextView tvFriendName;
        private TextView tvFriendAddedDate;

        public static final String TAG = "InboxAdapter";

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

            ParseObject textMessageContent=addFriendRequest.getFromUser().fetchIfNeeded();
            tvFriendName.setText((textMessageContent.get("username").toString()));

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
        addFriends.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<FriendRequest> list) {
        addFriends.addAll(list);
        notifyDataSetChanged();
    }

    public int getSize() {
        return addFriends.size();
    }



}