package com.armhansa.app.grandwarp.recycler;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.armhansa.app.grandwarp.R;
import com.armhansa.app.grandwarp.model.Message;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    private ArrayList<Message> mDataset;

    public ChatAdapter(ArrayList<Message> mDataset) {
        this.mDataset = mDataset;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_message, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.sender.setText(mDataset.get(position).getSender());
        holder.message.setText(mDataset.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView sender;
        TextView message;

        ViewHolder(View itemView) {
            super(itemView);
            sender = itemView.findViewById(R.id.sender);
            message = itemView.findViewById(R.id.message);
        }
    }

}
