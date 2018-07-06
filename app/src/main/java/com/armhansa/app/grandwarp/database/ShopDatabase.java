package com.armhansa.app.grandwarp.database;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShopDatabase {

    private DatabaseReference mRef;

    public ShopDatabase() {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
    }

    public void getOnceAllShops(ValueEventListener listener) {
        mRef.child("shops").orderByChild("status").addListenerForSingleValueEvent(listener);
    }

    public void getOnceManageShop(String uId, ValueEventListener listener) {
        mRef.child("shopOwners").child(uId).addListenerForSingleValueEvent(listener);
    }

    public void getOnceFavoriteShop(String uId, ValueEventListener listener) {
        mRef.child("favorites").child(uId).addListenerForSingleValueEvent(listener);
    }

}
