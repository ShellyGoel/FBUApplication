package com.example.fbuapplication.adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fbuapplication.ParseModels.Message;
import com.example.fbuapplication.R;
import com.example.fbuapplication.activities.MessageDetailsActivity;
import com.example.fbuapplication.fragments.MainWallFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import tyrantgit.explosionfield.ExplosionField;

public class MessagesMainWallAdapter extends RecyclerView.Adapter<MessagesMainWallAdapter.ViewHolder> implements View.OnLongClickListener {
    private Context context;
    private List<Message> messages;
    private int clickedVal;
    private MainWallInterface mListener;
    ExplosionField explosionField;
    boolean explod = true;


    @Override
    public boolean onLongClick(View v) {
        //if goal, then explode:)

        Log.i("ADAPTER","in long click");

        if(mListener.onWork()== R.id.action_goals) {
            ExplosionField explosionField = ExplosionField.attach2Window((Activity) context);
            reset(v);
            explosionField.explode(v);
            explosionField.clear();
        }
        return true;
    }


    private void reset(View root) {
        if (root instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) root;
            for (int i = 0; i < parent.getChildCount(); i++) {
                explosionField.clear();
                reset(parent.getChildAt(i));
            }
        } else {
            root.setScaleX(1);
            root.setScaleY(1);
            root.setAlpha(1);
        }
    }

    public interface MainWallInterface{
        int onWork (); // Here you can customize the method you want to achieve, generally passed into the variables in the adapter for activity use.
    }



    public MessagesMainWallAdapter(Context context, List<Message> messages, MainWallInterface mListener) {
        this.context = context;
        this.messages = messages;
        this.mListener = mListener;
        explosionField = ExplosionField.attach2Window((Activity) context);
    }

    // method for filtering our recyclerview items.
    public void setFilter(List<Message> filteredMessages){
        messages = new ArrayList<>();
        messages.addAll(filteredMessages);
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
        private FragmentManager fm;
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

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

//                    if(mListener.onWork()==R.id.action_goals) {
//
//
//                        explosionField.explode(v);
//
////                        ExplosionField explosionField = ExplosionField.attach2Window((Activity) context);
//                        reset(v);
//
//        //                explosionField.explode(v);
//
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION) {
//                            // get the movie at the position, this won't work if the class is static
//                            Message message = messages.get(position);
//                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");
//                        //query.whereEqualTo("receiver", ParseUser.getCurrentUser());
//                        query.whereEqualTo("objectId", message.getObjectId());
//                        query.findInBackground(new FindCallback<ParseObject>() {
//                            public void done(List<ParseObject> messages, ParseException e) {
//                                if (e == null) {
//
//                                    // iterate over all messages and delete them
//                                    for (ParseObject message : messages) {
//                                        //message.deleteEventually();
//                                        message.deleteInBackground();
//                                    }
//                                } else {
//                                    Log.d("main wall adapter", e.getMessage());
//                                }
//                            }
//                        });
//                       // explosionField.clear();
//                    }
//                    }
//



                    return true;
                }
            });



        }


        public void bind(Message message) {
            // Bind the message data to the view elements
            tvMessageBody.setText(message.getMessageBody());
            Date createdAt = message.getCreatedAt();
            String timeAgo = Message.calculateTimeAgo(createdAt);
            tvDate.setText(timeAgo);


            MainWallFragment mainfragment = new MainWallFragment();
            //int idClicked = mainfragment.getClickedID();
            //int idClicked = clickedVal;
            int idClicked = mListener.onWork();
            int stickyNote;

            System.out.println("HERE: "+idClicked);
            switch (idClicked)
            {

                case 0:
                    stickyNote = R.drawable._removal_ai__tmp_60ebbf1103f00;
                    break;
                case R.id.action_kudos:
                    stickyNote = R.drawable._removal_ai__tmp_60ebbfcbb11a1;
                    break;
                case R.id.action_memories:
                    stickyNote = R.drawable._removal_ai__tmp_60ebbf6e0f434;

                    break;
                case R.id.action_goals:
                    stickyNote = R.drawable._removal_ai__tmp_60ebbf43c2076;

                    break;
                case R.id.action_wallmain:
                    stickyNote = R.drawable._removal_ai__tmp_60ebbf6e0f434;
//                case R.id.action_search:
//                    stickyNote = ivStickyNoteImage.getResources().getDrawable();
//                    break;
                default:
                    stickyNote = R.drawable._removal_ai__tmp_60ebbf1103f00;
                    break;
            }




            Glide.with(context).load(stickyNote).into(ivStickyNoteImage);



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