package com.armhansa.app.grandwarp.ui;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.armhansa.app.grandwarp.R;
import com.armhansa.app.grandwarp.database.ShopDatabase;
import com.armhansa.app.grandwarp.model.Shop;
import com.armhansa.app.grandwarp.model.holder.UserShops;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateShopStatusActivity extends AppCompatActivity {

    private static final String TAG = "UpdateShopStatus";

    // View
    private TextView nameTxt;
    private ImageButton switchBtn;


    // Getter Shop
    private ShopDatabase mShopDatabase;

    // Shops
    private Shop shop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_shop_status);

        int shopIndex = getIntent().getIntExtra("shopIndex", -1);

        if(shopIndex == -1) {
            Toast.makeText(this, "Error: Con not access shop", Toast.LENGTH_LONG).show();
            finish();
        } else {
            shop = UserShops.getInstance().getShop(shopIndex);
            initialView();

        }

    }

    private void initialView() {
        nameTxt = findViewById(R.id.nameTxt);
        nameTxt.setText(shop.getName());
        switchBtn = findViewById(R.id.switchBtn);
        if(shop.getStatus().equals("Close")) {
            switchBtn.setBackgroundColor(Color.RED);
        } else {
            switchBtn.setBackgroundColor(Color.GREEN);
        }
    }

    public void switchNow(View view) {
        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference shopRef = mDatabaseRef.child("shops").orderByChild("name").equalTo(shop.getName()).getRef();
        Log.d(TAG, "switchNow: "+shopRef.getParent());
        if(shop.getStatus().equals("Close")) {
            switchBtn.setBackgroundColor(Color.RED);
        } else {
            switchBtn.setBackgroundColor(Color.GREEN);
        }
    }

    public void switchIn10Min(View view) {
    }

    public void switchIn20Min(View view) {
    }

    public void deleteShop(View view) {
    }

    public void openShopInfo(View view) {
    }
}
