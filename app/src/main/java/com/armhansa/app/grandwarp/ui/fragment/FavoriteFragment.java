package com.armhansa.app.grandwarp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.armhansa.app.grandwarp.R;
import com.armhansa.app.grandwarp.model.Shop;
import com.armhansa.app.grandwarp.database.ShopDatabase;
import com.armhansa.app.grandwarp.model.holder.FavShops;
import com.armhansa.app.grandwarp.model.holder.User;
import com.armhansa.app.grandwarp.recycler_adapter.FavoriteAdapter;
import com.armhansa.app.grandwarp.ui.ShopDetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

public class FavoriteFragment extends Fragment implements FavoriteAdapter.ChatListener {

    private static final String TAG = "FavoriteFragment";

    // RecyclerView
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // Getter shop
    private ShopDatabase mShopDatabase;
    private FavShops favShops;

    // View
    private TextView waitingTxt;
    private SwipeRefreshLayout mSwipeRefresh;

    // User
    private User currentUser;

    // RequestCode for OnActivityResults()
    private final int VIEW_DETAILS = 36;

    private final ValueEventListener LISTENER =
            new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    prepareAndUpdate(dataSnapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w(TAG, "Failed to read value.", databaseError.toException());
                    Toast.makeText(getContext(), "Failed to read value.", Toast.LENGTH_LONG).show();
                }
            };

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);

        initialView(rootView);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        favShops = FavShops.getInstance();

        if(favShops.size() > 0) {
            // specify an adapter (see also next example)
            mAdapter = new FavoriteAdapter(favShops.getShops(), getContext(), FavoriteFragment.this);
            mRecyclerView.setAdapter(mAdapter);

            waitingTxt.setVisibility(View.INVISIBLE);
        } else {
            mRecyclerView.setVisibility(View.INVISIBLE);
            waitingTxt.setText("Not Have now.");
        }

        mShopDatabase = new ShopDatabase();

        currentUser = User.getInstance();

        return rootView;
    }

    private void initialView(View rootView) {
        mRecyclerView = rootView.findViewById(R.id.recycler_favorite);

        waitingTxt = rootView.findViewById(R.id.waitingTxt);

        mSwipeRefresh = rootView.findViewById(R.id.swipeRefresh);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateRecycler();
            }
        });
    }

    private void updateRecycler() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        waitingTxt.setText(R.string.wait_a_minutes);
        waitingTxt.setVisibility(View.VISIBLE);

        mShopDatabase.getOnceFavoriteShop(currentUser.getUserID(), LISTENER); // success goto prepareAndUpdate()

        mSwipeRefresh.setRefreshing(false);
    }


    private void prepareAndUpdate(DataSnapshot dataSnapshot) {
        Iterator<DataSnapshot> dataSnapshotsFavorite = dataSnapshot.getChildren().iterator();

        favShops.clear();
        while(dataSnapshotsFavorite.hasNext()) {
            DataSnapshot dataSnapshotChild = dataSnapshotsFavorite.next();
            favShops.add(dataSnapshotChild.getValue(Shop.class));
        }

        if(favShops.size() == 0) {
            waitingTxt.setText("Not Have now.");
        } else {
            // specify an adapter (see also next example)
            mAdapter = new FavoriteAdapter(favShops.getShops(), getContext(), FavoriteFragment.this);
            mRecyclerView.setAdapter(mAdapter);

            mRecyclerView.setVisibility(View.VISIBLE);
            waitingTxt.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClickInItem(String shopName) {
        // Set Data to new Activity




        startActivityForResult(new Intent(getActivity(), ShopDetailActivity.class), VIEW_DETAILS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == VIEW_DETAILS) {
            updateRecycler();
        }
    }
}
