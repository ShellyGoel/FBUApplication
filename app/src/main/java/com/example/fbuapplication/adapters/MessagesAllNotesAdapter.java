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

public class MessagesAllNotesAdapter extends RecyclerView.Adapter<MessagesAllNotesAdapter.ViewHolder> {
    private Context context;
    private List<Integer> messages;

    public MessagesAllNotesAdapter(Context context, List<Integer> messages) {
        this.context = context;
        this.messages = messages;
    }

    // method for filtering our recyclerview items.
    public void setFilter(List<Integer> countryModels){
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

    class ViewHolder extends RecyclerView.ViewHolder {


        private ImageView ivStickyNoteImage;
        private TextView messageNum;

        public ViewHolder(@NonNull View messageView) {
            super(messageView);
            ivStickyNoteImage = messageView.findViewById(R.id.ivStickyNoteImageDetails);
            messageNum = messageView.findViewById(R.id.tvAllNotesNum);
        }


        public void bind(Integer message) {
            messageNum.setVisibility(View.INVISIBLE);

            if(message!= null && message%5==0) {

                messageNum.setVisibility(View.VISIBLE);
                messageNum.setText(message.toString());
                if(message%20==0){
                    messageNum.setTextColor(context.getResources().getColor(R.color.white));
                    messageNum.setTextSize(30);
                    }
                Glide.with(context).load(R.drawable._removal_ai__tmp_60ebbfc098d23).into(ivStickyNoteImage);
            }
            else if(message!= null && message%5==1) {

                Glide.with(context).load(R.drawable._removal_ai__tmp_60ebbf43c2076).into(ivStickyNoteImage);
            }
            else if(message!= null && message%5==2) {
                Glide.with(context).load(R.drawable._removal_ai__tmp_60ebbfb3c4dd3).into(ivStickyNoteImage);
            }

            else if(message!= null && message%5==3) {
                Glide.with(context).load(R.drawable._removal_ai__tmp_60ebbf8c7f2f5).into(ivStickyNoteImage);
            }
            else{
                Glide.with(context).load(R.drawable._removal_ai__tmp_60ebbf1103f00).into(ivStickyNoteImage);

            }

        }

    }

    /* Within the RecyclerView.Adapter class */

    // Clean all elements of the recycler
    public void clear() {
        messages.clear();
        notifyDataSetChanged();
    }

    // Add a list of messages -- change to type used
    public void addAll(List<Integer> list) {
        messages.addAll(list);
        notifyDataSetChanged();
    }



}