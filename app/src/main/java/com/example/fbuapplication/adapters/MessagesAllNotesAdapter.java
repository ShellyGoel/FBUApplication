package com.example.fbuapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fbuapplication.R;

import java.util.ArrayList;
import java.util.List;

//adapter to populate data in all notes page
public class MessagesAllNotesAdapter extends RecyclerView.Adapter<MessagesAllNotesAdapter.ViewHolder> {
    private final Context context;
    private List<Integer> messages;

    public MessagesAllNotesAdapter(Context context, List<Integer> messages) {
        this.context = context;
        this.messages = messages;
    }

    public void setFilter(List<Integer> countryModels) {
        messages = new ArrayList<>();
        messages.addAll(countryModels);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_allnotes, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Integer post = messages.get(position);
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

    /* Within the RecyclerView.Adapter class */

    public void addAll(List<Integer> list) {
        messages.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivStickyNoteImage;
        private final TextView messageNum;

        public ViewHolder(@NonNull View messageView) {
            super(messageView);
            ivStickyNoteImage = messageView.findViewById(R.id.ivStickyNoteImageDetails);
            messageNum = messageView.findViewById(R.id.tvAllNotesNum);
        }

        public void bind(Integer message) {
            messageNum.setVisibility(View.INVISIBLE);

            if (message != null && message % 5 == 0) {

                messageNum.setVisibility(View.VISIBLE);
                messageNum.setTextColor(context.getResources().getColor(R.color.purple_500));
                messageNum.setTextSize(20);
                messageNum.setText(message.toString());
                if (message % 50 == 0) {
                    messageNum.setTextColor(context.getResources().getColor(R.color.white));
                    messageNum.setTextSize(28);
                    Glide.with(context).load(R.drawable.cup).into(ivStickyNoteImage);
                    return;
                }

                if (message % 40 == 0) {
                    messageNum.setTextColor(context.getResources().getColor(R.color.white));
                    messageNum.setTextSize(28);

                    Glide.with(context).load(R.drawable.fresh_folk___plants_14).into(ivStickyNoteImage);
                    return;
                }

                if (message % 30 == 0) {
                    messageNum.setTextColor(context.getResources().getColor(R.color.white));
                    messageNum.setTextSize(28);

                    Glide.with(context).load(R.drawable.fresh_folk___plants_16).into(ivStickyNoteImage);
                    return;
                }

                if (message % 20 == 0) {
                    messageNum.setTextColor(context.getResources().getColor(R.color.white));
                    messageNum.setTextSize(28);

                    Glide.with(context).load(R.drawable.fresh_folk___plants_3).into(ivStickyNoteImage);
                    return;
                }

                if (message % 10 == 0) {
                    messageNum.setTextColor(context.getResources().getColor(R.color.white));
                    messageNum.setTextSize(28);

                    Glide.with(context).load(R.drawable.fresh_folk___plants_15).into(ivStickyNoteImage);
                    return;
                }

                Glide.with(context).load(R.drawable._removal_ai__tmp_60ebbfc098d23).into(ivStickyNoteImage);
            } else if (message != null && message % 5 == 1) {

                Glide.with(context).load(R.drawable._removal_ai__tmp_60ebbf43c2076).into(ivStickyNoteImage);
            } else if (message != null && message % 5 == 2) {
                Glide.with(context).load(R.drawable._removal_ai__tmp_60ebbfb3c4dd3).into(ivStickyNoteImage);
            } else if (message != null && message % 5 == 3) {
                Glide.with(context).load(R.drawable._removal_ai__tmp_60ebbf8c7f2f5).into(ivStickyNoteImage);
            } else {
                Glide.with(context).load(R.drawable._removal_ai__tmp_60ebbf1103f00).into(ivStickyNoteImage);

            }

        }

    }

}