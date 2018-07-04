package com.armhansa.app.grandwarp.for_test;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.armhansa.app.grandwarp.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class TestActivity extends AppCompatActivity {

    private static final String TAG = "TestActivity";

    private ImageView pictureImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        pictureImg = findViewById(R.id.pictureImg);

//        FirebaseStorage.getInstance().getReference()
//                .child("profiles").child("Test").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                    @Override
//                    public void onSuccess(Uri uri) {
//                        Log.d(TAG, "onSuccess: "+uri.getPath());
//
//                    }
//                });

        Glide.with(TestActivity.this)
                .load("")
                .into(pictureImg);


    }
}
