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
import com.armhansa.app.grandwarp.model.Shop;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private  ArrayList<Shop> favShops;
    private Context context;

    private RequestOptions mOption;

    public interface ChatListener {
        void onClickInItem(String shopName);
    }
    private ChatListener listener;

    public FavoriteAdapter(ArrayList<Shop> favShops
            , Context context
            , ChatListener listener) {
        this.favShops = favShops;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shop, parent, false);
        ViewHolder vh = new ViewHolder(v);

        mOption = new RequestOptions()
                .centerCrop()
                .fitCenter()
                .circleCrop();

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        ImageView shopImg = holder.shopImg;
        TextView nameTxt = holder.nameTxt;
        TextView timeTxt = holder.timeTxt;
        TextView statusTxt = holder.statusTxt;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickInItem(favShops.get(position).getName());
            }
        });

        String picture = favShops.get(getItemCount()-position-1).getPicture();
        if(!picture.equals("default")) {
            Glide.with(context)
                    .load(favShops.get(getItemCount()-position-1).getPicture())
                    .apply(mOption)
                    .into(shopImg);
        }

        nameTxt.setText(favShops.get(getItemCount()-position-1).getName());

        timeTxt.setText(String.format("%s - %s"
                , favShops.get(getItemCount()-position-1).getOpenTime()
                , favShops.get(getItemCount()-position-1).getCloseTime()));

        String status = favShops.get(getItemCount()-position-1).getStatus();
        switch (status) {
            case "open":
                statusTxt.setTextColor(Color.GREEN);
                break;
            case "close":
                statusTxt.setTextColor(Color.RED);
                break;
        }
        statusTxt.setText(status);

    }

    @Override
    public int getItemCount() {
        return favShops.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView shopImg;
        TextView nameTxt;
        TextView timeTxt;
        TextView statusTxt;


        public ViewHolder(View itemView) {
            super(itemView);
            shopImg = itemView.findViewById(R.id.shopImg);
            nameTxt = itemView.findViewById(R.id.nameTxt);
            timeTxt = itemView.findViewById(R.id.timeTxt);
            statusTxt = itemView.findViewById(R.id.statusTxt);

        }
    }
}
