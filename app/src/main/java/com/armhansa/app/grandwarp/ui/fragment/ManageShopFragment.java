package com.armhansa.app.grandwarp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.armhansa.app.grandwarp.R;
import com.armhansa.app.grandwarp.database.ShopDatabase;
import com.armhansa.app.grandwarp.model.ManageShop;
import com.armhansa.app.grandwarp.model.Shop;
import com.armhansa.app.grandwarp.model.User;
import com.armhansa.app.grandwarp.model.UserShops;
import com.armhansa.app.grandwarp.recycler_adapter.ManageAdapter;
import com.armhansa.app.grandwarp.ui.CreateShopActivity;
import com.armhansa.app.grandwarp.ui.UpdateShopStatusActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class ManageShopFragment extends Fragment implements ManageAdapter.ChatListener {

    private static final String TAG = "ManageShopFragment";

    // RecyclerView
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    // getter Shops
    private ShopDatabase mShopDatabase;
    private UserShops shops;

    // View
    private TextView waitingTxt;
    private ImageView alert;
    private FloatingActionButton fab;
    private SwipeRefreshLayout mSwipeRefresh;

    // User
    private User currentUser;

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

    public ManageShopFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_manage_shop, container, false);

        viewInitial(rootView);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        shops = UserShops.getInstance();

        if(shops.size() != 0) {
            mAdapter = new ManageAdapter(shops.getShops(), getContext(), ManageShopFragment.this);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mRecyclerView.setVisibility(View.INVISIBLE);
            alert.setVisibility(View.VISIBLE);
        }

        mShopDatabase = new ShopDatabase();

        currentUser = User.getInstance();

        return rootView;
    }

    private void viewInitial(View rootView) {
        mRecyclerView = rootView.findViewById(R.id.recycler_manage);

        alert = rootView.findViewById(R.id.alertCreateShopImg);
        waitingTxt = rootView.findViewById(R.id.waitingTxt);

        fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), CreateShopActivity.class), 34);
            }
        });

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
        alert.setVisibility(View.INVISIBLE);
        fab.setVisibility(View.INVISIBLE);
        waitingTxt.setVisibility(View.VISIBLE);

        mShopDatabase.getOnceManageShop(currentUser.getUserID(), LISTENER);

        mSwipeRefresh.setRefreshing(false);
    }

    private void prepareAndUpdate(DataSnapshot dataSnapshot) {
        Log.d(TAG, "onDataChange: begin");

        Iterator<DataSnapshot> dataSnapshotsManage = dataSnapshot.getChildren().iterator();

        shops.clear();
        while(dataSnapshotsManage.hasNext()) {
            DataSnapshot dataSnapshotChild = dataSnapshotsManage.next();
            shops.add(dataSnapshotChild.getValue(Shop.class));
        }

        if(shops.size() == 0) {
            alert.setVisibility(View.VISIBLE);
        } else {
            // specify an adapter (see also next example)
            mAdapter = new ManageAdapter(shops.getShops(), getContext(), ManageShopFragment.this);
            mRecyclerView.setAdapter(mAdapter);

            mRecyclerView.setVisibility(View.VISIBLE);
        }
        fab.setVisibility(View.VISIBLE);
        waitingTxt.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onClickInItem(String shopName) {
        // Set Data to new Activity



        startActivity(new Intent(getActivity(), UpdateShopStatusActivity.class));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 34) {
            updateRecycler();
        }
    }
}
