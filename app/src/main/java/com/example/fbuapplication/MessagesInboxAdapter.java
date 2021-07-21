package com.example.fbuapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fbuapplication.fragments.InboxFragment;
import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Date;
import java.util.List;

public class MessagesInboxAdapter extends RecyclerView.Adapter<MessagesInboxAdapter.ViewHolder> {
    private Context context;
    private List<Message> messages;

    public MessagesInboxAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message_inbox, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }


    public void removeItem(int position) {
        messages.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Message message, int position) {
        messages.add(position, message);
        notifyItemInserted(position);
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView btnPin;
        private TextView tvMessageBody;
        private TextView tvDate;

        public static final String TAG = "InboxAdapter";

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            btnPin = itemView.findViewById(R.id.btnPin);
            tvMessageBody = itemView.findViewById(R.id.tvMessageBody);
            tvDate = itemView.findViewById(R.id.tvDate);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("MessageDetailsActivity", String.format("going to details"));
                    // gets item position
                    int position = getAdapterPosition();
                    // make sure the position is valid, i.e. actually exists in the view
                    if (position != RecyclerView.NO_POSITION) {
                        // get the movie at the position, this won't work if the class is static
                        Message message = messages.get(position);
                        Date createdAt = message.getCreatedAt();
                        String timeAgo = Message.calculateTimeAgo(createdAt);
                        // tvDate.setText(timeAgo);


                        //set message as read since it was clicked on
                        message.setIsUnread(false);
                        message.saveInBackground();
                        // create intent for the new activity
                        Intent intent = new Intent(context, MessageDetailsActivity.class);
                        // serialize the movie using parceler, use its short name as a key
                        intent.putExtra("createdAt", timeAgo);
                        intent.putExtra("caption",message.getMessageBody());
                        // show the activity
                        context.startActivity(intent);
                    }
                }
            });
        }


        public void bind(Message message) {
            // Bind the message data to the view elements
            tvMessageBody.setText(message.getMessageBody());
            Date createdAt = message.getCreatedAt();
            String timeAgo = Message.calculateTimeAgo(createdAt);
            tvDate.setText(timeAgo);
//            ParseFile image = message.getImage();
//            if (image != null) {
//                Glide.with(context).load(image.getUrl()).into(ivImage);
//            }

            //TODO: Check this!
            if(message.getIsPinned()){
                Glide.with(context).load(R.drawable.ic_baseline_push_pin_clicked).into(btnPin);
            }

            else{
                Glide.with(context).load(R.drawable.ic_baseline_push_pin_24).into(btnPin);
            }

            //ADD UNREAD STUFF
            //message is already read
            if(!message.getIsUnread()){
                //gray out that view
                itemView.setBackgroundColor(itemView.getResources().getColor(R.color.gray_out));
            }

            else{
                //gray out that view
                itemView.setBackgroundColor(itemView.getResources().getColor(R.color.white));
            }
            btnPin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Inbox", "pinned message");
                    ParseUser currentUser = ParseUser.getCurrentUser();
                    //currentUser.add("main_wall_notes", tvMessageBody.getText());
                    //currentUser.add("main_wall_messages",message);
                    message.setIsPinned(true);
                    Glide.with(context).load(R.drawable.ic_baseline_push_pin_clicked).into(btnPin);

                    message.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e != null){
                                Log.e(TAG, "Error while pinning note",e);
                                //Toast.makeText(context, "Error in pinning note!", Toast.LENGTH_SHORT).show();
                                Snackbar.make(btnPin, "Error while pinning note", Snackbar.LENGTH_LONG).show();

                            }
                            Log.i(TAG, "Note pinned successfully!");
                        }
                    });
                }
            });
        }


    }

    /* Within the RecyclerView.Adapter class */

    // Clean all elements of the recycler
    public void clear() {
        messages.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Message> list) {
        messages.addAll(list);
        notifyDataSetChanged();
    }



}