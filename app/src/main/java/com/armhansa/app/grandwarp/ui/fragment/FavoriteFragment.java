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

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<Shop> favShops;
    private ShopDatabase shopDatabase;

    private TextView waitingTxt;

    private SwipeRefreshLayout mSwipeRefresh;

    private final ValueEventListener LISTENER =
            new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataChange: ");
                    Iterator<DataSnapshot> dataSnapshotsFav = dataSnapshot.getChildren().iterator();
                    favShops = new ArrayList<>();
                    while(dataSnapshotsFav.hasNext()) {
                        DataSnapshot dataSnapshotChild = dataSnapshotsFav.next();
                        favShops.add(dataSnapshotChild.getValue(Shop.class));
                    }

                    // specify an adapter (see also next example)
                    mAdapter = new FavoriteAdapter(favShops, getContext(), FavoriteFragment.this);
                    mRecyclerView.setAdapter(mAdapter);

                    waitingTxt.setVisibility(View.GONE);
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

        mRecyclerView = rootView.findViewById(R.id.recycler_favorite);

        waitingTxt = rootView.findViewById(R.id.waitingTxt);

        mSwipeRefresh = rootView.findViewById(R.id.swipeRefresh);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shopDatabase = new ShopDatabase();
                shopDatabase.getOnceAllShops(LISTENER);

                mSwipeRefresh.setRefreshing(false);
            }
        });

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        shopDatabase = new ShopDatabase();
        shopDatabase.getOnceFavoriteShop(FirebaseAuth.getInstance().getUid(), LISTENER);

        return rootView;
    }

    @Override
    public void onClickInItem(String shopName) {
        startActivity(new Intent(getActivity(), ShopDetailActivity.class));
    }
}
