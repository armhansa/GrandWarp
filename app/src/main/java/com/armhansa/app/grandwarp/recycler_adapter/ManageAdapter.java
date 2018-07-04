package com.armhansa.app.grandwarp.recycler_adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.armhansa.app.grandwarp.R;
import com.armhansa.app.grandwarp.model.ManageShop;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class ManageAdapter extends RecyclerView.Adapter<ManageAdapter.ViewHolder> {

    private ArrayList<ManageShop> manageShops;
    private Context context;

    private RequestOptions mOption;

    public interface ChatListener {
        void onClickInItem(String shopName);
    }
    private ChatListener listener;

    public ManageAdapter(ArrayList<ManageShop> manageShops
            , Context context
            , ChatListener listener) {
        this.manageShops = manageShops;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ManageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_manage_shop, parent, false);
        ViewHolder vh = new ViewHolder(v);

        mOption = new RequestOptions()
                .centerCrop()
                .fitCenter()
                .circleCrop();

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ManageAdapter.ViewHolder holder, final int position) {

        ImageView shopImg = holder.shopImg;
        TextView nameTxt = holder.nameTxt;
        TextView timeTxt = holder.timeTxt;
        TextView statusTxt = holder.statusTxt;
        TextView chatTxt = holder.chatTxt;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickInItem(manageShops.get(position).getName());
            }
        });

        String picture = manageShops.get(getItemCount()-position-1).getPicture();
        if(!picture.equals("default")) {
            Glide.with(context)
                    .load(manageShops.get(getItemCount()-position-1).getPicture())
                    .apply(mOption)
                    .into(shopImg);
        }

        nameTxt.setText(manageShops.get(getItemCount()-position-1).getName());

        timeTxt.setText(String.format("%s - %s"
                , manageShops.get(getItemCount()-position-1).getOpenTime()
                , manageShops.get(getItemCount()-position-1).getCloseTime()));

        String status = manageShops.get(getItemCount()-position-1).getStatus();
        switch (status) {
            case "open":
                statusTxt.setTextColor(Color.GREEN);
                break;
            case "close":
                statusTxt.setTextColor(Color.RED);
                break;
        }
        statusTxt.setText(status);

        chatTxt.setText(String.valueOf(manageShops.get(position).getChatNotSeen()));

    }

    @Override
    public int getItemCount() {
        return manageShops.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView shopImg;
        TextView nameTxt;
        TextView timeTxt;
        TextView statusTxt;
        TextView chatTxt;

        public ViewHolder(View itemView) {
            super(itemView);
            shopImg = itemView.findViewById(R.id.shopImg);
            nameTxt = itemView.findViewById(R.id.nameTxt);
            timeTxt = itemView.findViewById(R.id.timeTxt);
            statusTxt = itemView.findViewById(R.id.statusTxt);
            chatTxt = itemView.findViewById(R.id.chatTxt);
        }
    }

}
