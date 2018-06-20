package com.armhansa.app.grandwarp.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.armhansa.app.grandwarp.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TestMessageActivity extends AppCompatActivity {

    private static final String TAG = "TestMessageActivity";

    private DatabaseReference mDbRef;

    ValueEventListener showMessageListener;

    private TextView display;
    private EditText message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_message);

        mDbRef = FirebaseDatabase.getInstance().getReference("message");

        display = findViewById(R.id.display);
        message = findViewById(R.id.message);
        Button uploadBtn = findViewById(R.id.uploadBtn);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFirebase();
            }
        });
        Button logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        setDisplayMessage();

    }

    private void logout() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        mDbRef.removeEventListener(showMessageListener);
                        startActivity(new Intent(TestMessageActivity.this, AuthenticationActivity.class));
                        finish();
                    }
                });
    }

    private void uploadFirebase() {
        // Write a message to the database
        Log.d(TAG, "uploadFirebase: "+message.getText().toString());
        mDbRef.setValue(message.getText().toString());
    }

    private void setDisplayMessage() {
        // Read from the database
        showMessageListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
                if(value != null) {
                    display.setText(value);
                } else {
                    display.setText(R.string.nothing);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
                Toast.makeText(TestMessageActivity.this, "Failed to read value.", Toast.LENGTH_LONG).show();
            }
        };
        mDbRef.addValueEventListener(showMessageListener);

    }

    @Override
    protected void onStop() {
        mDbRef.removeEventListener(showMessageListener);
        super.onStop();
    }

}
