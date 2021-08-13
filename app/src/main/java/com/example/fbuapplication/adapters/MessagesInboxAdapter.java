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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fbuapplication.ParseModels.Message;
import com.example.fbuapplication.R;
import com.example.fbuapplication.activities.MessageDetailsActivity;
import com.example.fbuapplication.fragments.dialogFragments.DecidePinnedWallDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.Date;
import java.util.List;

//adapter to populate data in inbox recyclerView
public class MessagesInboxAdapter extends RecyclerView.Adapter<MessagesInboxAdapter.ViewHolder> implements DecidePinnedWallDialogFragment.DecidePinnedWallDialogListener {
    private final Context context;
    private final List<Message> messages;
    private final Fragment inboxFragment;
    private Message currentMessage;
    private int adapterPosition;
    private int position;

    public MessagesInboxAdapter(Context context, List<Message> messages, Fragment inboxFragment) {
        this.context = context;
        this.messages = messages;
        this.inboxFragment = inboxFragment;
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_message_inbox, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messages.get(position);
        currentMessage = message;
        holder.bind(message, position);
        position = position;
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

    public Message getCurrentMessage() {
        return currentMessage;
    }

    @Override
    public void onFinishDecidePinnedWallDialog(boolean[] toSend) {

        if (toSend[0]) {
            getCurrentMessage().setIsKudos(true);

        }
        if (toSend[1]) {
            getCurrentMessage().setIsmemories(true);
        }
        if (toSend[2]) {
            getCurrentMessage().setIsGoals(true);
        }

        getCurrentMessage().saveInBackground();
    }

    // Clean all elements of the recycler
    public void clear() {
        messages.clear();
        notifyDataSetChanged();
    }

    /* Within the RecyclerView.Adapter class */

    // Add a list of items -- change to type used
    public void addAll(List<Message> list) {
        messages.addAll(list);
        notifyDataSetChanged();
    }

    public int getSize() {
        return messages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public static final String TAG = "InboxAdapter";
        private final ImageView btnPin;
        private final TextView tvMessageBody;
        private final TextView tvDate;

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
                        intent.putExtra("caption", message.getMessageBody());
                        // show the activity
                        context.startActivity(intent);
                    }
                }
            });
        }

        public void bind(Message message, int position) {
            // Bind the message data to the view elements
            tvMessageBody.setText(message.getMessageBody());
            Date createdAt = message.getCreatedAt();
            String timeAgo = Message.calculateTimeAgo(createdAt);
            tvDate.setText(timeAgo);
//            ParseFile image = message.getImage();
//            if (image != null) {
//                Glide.with(context).load(image.getUrl()).into(ivImage);
//            }

            if (position % 2 == 0) {
                tvMessageBody.setTextColor(itemView.getResources().getColor(R.color.teal_400));

            } else {
                tvMessageBody.setTextColor(itemView.getResources().getColor(R.color.title));

            }

            //TODO: Check this!
            if (message.getIsPinned()) {
                Glide.with(context).load(R.drawable.ic_baseline_push_pin_clicked).into(btnPin);
            } else {
                Glide.with(context).load(R.drawable.ic_baseline_push_pin_24).into(btnPin);
            }

            //ADD UNREAD STUFF
            //message is already read
            if (!message.getIsUnread()) {
                //gray out that view
                itemView.setBackgroundColor(itemView.getResources().getColor(R.color.gray_out));
            } else {
                //gray out that view
                itemView.setBackgroundColor(itemView.getResources().getColor(R.color.white));
            }
            btnPin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ParseUser currentUser = ParseUser.getCurrentUser();
                    //currentUser.add("main_wall_notes", tvMessageBody.getText());
                    //currentUser.add("main_wall_messages",message);

                    //check which wall to pin to
                    adapterPosition = getPosition();

                    FragmentActivity activity = (FragmentActivity) (context);
                    FragmentManager fm = activity.getSupportFragmentManager();
                    DecidePinnedWallDialogFragment decidePinnedWallDialogFragment = new DecidePinnedWallDialogFragment();
                    decidePinnedWallDialogFragment.setTargetFragment(inboxFragment, 300);
                    decidePinnedWallDialogFragment.show(fm, "toSend");

//                    FragmentManager fm = ((AppCompatActivity)context).getSupportFragmentManager();
//                    DecidePinnedWallDialogFragment decidePinnedWallDialogFragment = new DecidePinnedWallDialogFragment();//;EditNameDialog.newInstance("Some Title");
//                    // SETS the target fragment for use later when sending results
//
//                    decidePinnedWallDialogFragment.setTargetFragment(inboxFragment, 300);
//                    decidePinnedWallDialogFragment.show(fm, "toSend");

//                    decidePinnedWallDialogFragment.setTargetAc(inboxFragment, 300);
//                    decidePinnedWallDialogFragment.show(fm, "toSend");

                    message.setIsPinned(true);
                    Glide.with(context).load(R.drawable.ic_baseline_push_pin_clicked).into(btnPin);

                    message.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                Log.e(TAG, "Error while pinning note", e);

                                Snackbar.make(btnPin, "Error while pinning note", Snackbar.LENGTH_LONG).show();

                            }

                        }
                    });

                }
            });
        }

    }

}