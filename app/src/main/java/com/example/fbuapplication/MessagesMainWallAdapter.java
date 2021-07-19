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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessagesMainWallAdapter extends RecyclerView.Adapter<MessagesMainWallAdapter.ViewHolder> {
    private Context context;
    private List<Message> messages;

    public MessagesMainWallAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    // method for filtering our recyclerview items.
    public void setFilter(List<Message> countryModels){
        messages = new ArrayList<>();
        messages.addAll(countryModels);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message_mainwall, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message post = messages.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {


        private ImageView ivStickyNoteImage;
        private TextView tvMessageBody;
        private TextView tvDate;

        public ViewHolder(@NonNull View messageView) {
            super(messageView);
            ivStickyNoteImage = messageView.findViewById(R.id.ivStickyNoteImageDetails);
            tvMessageBody = messageView.findViewById(R.id.tvMessageBody);
            tvDate = messageView.findViewById(R.id.tvDate);

            messageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("MessageDetailsActivity", String.format("going to details"));
                    // gets message position
                    int position = getAdapterPosition();
                    // make sure the position is valid, i.e. actually exists in the view
                    if (position != RecyclerView.NO_POSITION) {
                        // get the movie at the position, this won't work if the class is static
                        Message message = messages.get(position);
                        Date createdAt = message.getCreatedAt();
                        String timeAgo = Message.calculateTimeAgo(createdAt);
                        // tvDate.setText(timeAgo);

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
            Glide.with(context).load(R.mipmap.ic_stickynote_foreground).into(ivStickyNoteImage);
//            ParseFile image = message.getImage();
//            if (image != null) {
//                Glide.with(context).load(image.getUrl()).into(ivImage);
//            }


        }

    }

    /* Within the RecyclerView.Adapter class */

    // Clean all elements of the recycler
    public void clear() {
        messages.clear();
        notifyDataSetChanged();
    }

    // Add a list of messages -- change to type used
    public void addAll(List<Message> list) {
        messages.addAll(list);
        notifyDataSetChanged();
    }



}