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

public class FriendRequestsAdapter extends RecyclerView.Adapter<FriendRequestsAdapter.ViewHolder> {
    private Context context;

    private List<FriendRequest> addFriendRequests;
    private int adapterPosition;


    public int getAdapterPosition() {
        return adapterPosition;
    }

    public int getPosition() {
        return position;
    }

    private int position;

    public FriendRequestsAdapter(Context context, List<FriendRequest> addFriendRequests) {
        this.context = context;
        this.addFriendRequests = addFriendRequests;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_friend_requests, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FriendRequest addFriendRequest = addFriendRequests.get(position);
        try {
            holder.bind(addFriendRequest);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return addFriendRequests.size();
    }


    public void removeItem(int position) {
        addFriendRequests.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(FriendRequest addFriendRequest, int position) {
        addFriendRequests.add(position, addFriendRequest);
        notifyItemInserted(position);
    }



    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView btnAccept;
        private TextView tvFriendRequest;
        private TextView tvFriendDate;

        public static final String TAG = "InboxAdapter";

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            tvFriendRequest = itemView.findViewById(R.id.tvFriendRequest);
            tvFriendDate = itemView.findViewById(R.id.tvFriendDate);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                      // gets item position
                    int position = getAdapterPosition();

                    // make sure the position is valid, i.e. actually exists in the view
                    if (position != RecyclerView.NO_POSITION) {
                        // get the movie at the position, this won't work if the class is static
                        FriendRequest addFriendRequest = addFriendRequests.get(position);
                        Date createdAt = addFriendRequest.getCreatedAt();
                        String timeAgo = FriendRequest.calculateTimeAgo(createdAt);


                    }
                }
            });
        }


        public void bind(FriendRequest addFriendRequest) throws ParseException {
            // Bind the addFriendRequest data to the view elements

            ParseObject textMessageContent=addFriendRequest.getFromUser().fetchIfNeeded();
            tvFriendRequest.setText((textMessageContent.get("username").toString()));

            Date createdAt = addFriendRequest.getCreatedAt();
            String timeAgo = FriendRequest.calculateTimeAgo(createdAt);
            tvFriendDate.setText(timeAgo);





            btnAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ParseUser currentUser = ParseUser.getCurrentUser();

                    //check which wall to pin to
                    adapterPosition = getPosition();

                    //TODO: Check this!

                    Glide.with(context).load(R.drawable.ic_baseline_person_add_24).into(btnAccept);
                    addFriendRequest.setStatus("accepted");

                    addFriendRequest.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e != null){
                                Log.e(TAG, "Error while accepting friend request",e);
                                //Toast.makeText(context, "Error in pinning note!", Toast.LENGTH_SHORT).show();
                                Snackbar.make(btnAccept, "Error while accepting friend request", Snackbar.LENGTH_LONG).show();

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
        addFriendRequests.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<FriendRequest> list) {
        addFriendRequests.addAll(list);
        notifyDataSetChanged();
    }

    public int getSize() {
        return addFriendRequests.size();
    }



}