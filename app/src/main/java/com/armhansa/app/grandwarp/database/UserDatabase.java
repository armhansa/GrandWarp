package com.armhansa.app.grandwarp.database;

import com.armhansa.app.grandwarp.model.holder.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDatabase {

    private DatabaseReference mRef;

    public UserDatabase() {
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();
    }

    public void getOnceUser(String userId, ValueEventListener listener) {
        mRef.child("users").child(userId).addListenerForSingleValueEvent(listener);
    }

    public void addUser(String userId, User user
            , OnSuccessListener successListener
            , OnFailureListener failureListener) {
        mRef.child("users").child(userId).setValue(user)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

}
