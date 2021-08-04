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
    private final Context context;
    private final MainWallInterface mListener;
    ExplosionField explosionField;
    boolean explod = true;
    private List<Message> messages;
    private int clickedVal;

    public MessagesMainWallAdapter(Context context, List<Message> messages, MainWallInterface mListener) {
        this.context = context;
        this.messages = messages;
        this.mListener = mListener;
        explosionField = ExplosionField.attach2Window((Activity) context);
    }

    @Override
    public boolean onLongClick(View v) {

        if (mListener.onWork() == R.id.action_goals) {
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

    public void setFilter(List<Message> filteredMessages) {
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

    public void clear() {
        messages.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Message> list) {
        messages.addAll(list);
        notifyDataSetChanged();
    }

    /* Within the RecyclerView.Adapter class */

    public interface MainWallInterface {
        int onWork();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivStickyNoteImage;
        private final TextView tvMessageBody;
        private final TextView tvDate;
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

                    int position = getAdapterPosition();

                    if (position != RecyclerView.NO_POSITION) {

                        Message message = messages.get(position);
                        Date createdAt = message.getCreatedAt();
                        String timeAgo = Message.calculateTimeAgo(createdAt);

                        Intent intent = new Intent(context, MessageDetailsActivity.class);

                        intent.putExtra("createdAt", timeAgo);
                        intent.putExtra("caption", message.getMessageBody());

                        context.startActivity(intent);
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    return true;
                }
            });

        }

        public void bind(Message message) {

            tvMessageBody.setText(message.getMessageBody());
            Date createdAt = message.getCreatedAt();
            String timeAgo = Message.calculateTimeAgo(createdAt);
            tvDate.setText(timeAgo);

            MainWallFragment mainfragment = new MainWallFragment();

            int idClicked = mListener.onWork();
            int stickyNote;

            System.out.println("HERE: " + idClicked);
            switch (idClicked) {

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

                default:
                    stickyNote = R.drawable._removal_ai__tmp_60ebbf1103f00;
                    break;
            }

            Glide.with(context).load(stickyNote).into(ivStickyNoteImage);

        }

    }

}