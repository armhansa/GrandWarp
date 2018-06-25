package com.armhansa.app.grandwarp.ui.fragment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.armhansa.app.grandwarp.R;
import com.armhansa.app.grandwarp.model.Message;
import com.armhansa.app.grandwarp.recycler.ChatAdapter;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mRecyclerView = findViewById(R.id.recycler_chat);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Repeat with get Data
        ArrayList<Message> message = new ArrayList<>();

        // specify an adapter (see also next example)
        mAdapter = new ChatAdapter(message);
        mRecyclerView.setAdapter(mAdapter);
    }
}
