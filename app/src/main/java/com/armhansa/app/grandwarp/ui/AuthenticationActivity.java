package com.armhansa.app.grandwarp.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.armhansa.app.grandwarp.R;
import com.armhansa.app.grandwarp.for_test.MenuForTest;
import com.armhansa.app.grandwarp.model.User;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class AuthenticationActivity extends AppCompatActivity {

    private static final String TAG = "AuthenticationActivity";

    private User user;

    private static final int RC_LOGIN = 123;
    // Choose authentication providers
    private List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.FacebookBuilder().build(),
            new AuthUI.IdpConfig.PhoneBuilder().build());

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        checkSignIn();

    }

    private void login() {
        Log.d(TAG, "login: and Finished()");
//        Toast.makeText(this, user.getPhoneNumber(), Toast.LENGTH_LONG).show();
        startActivity(new Intent(AuthenticationActivity.this, MenuForTest.class));
        finish();
    }

    private void register() {
        Log.d(TAG, "register: ");
        startActivity(new Intent(AuthenticationActivity.this, RegisterActivity.class));
        finish();
    }
    
    private void checkSignIn() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAuth = FirebaseAuth.getInstance();
                mUser = mAuth.getCurrentUser();
                if(mUser == null) {
                    // Create and launch sign-in intent
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    .build(),
                            RC_LOGIN);
                } else {
                    FirebaseDatabase.getInstance().getReference()
                            .child("users").child(mUser.getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    User mUser = dataSnapshot.getValue(User.class);
                                    if(mUser != null) {
                                        login();
                                    } else {
                                        register();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(AuthenticationActivity.this
                                            , "getUserRegistered Fail", Toast.LENGTH_LONG).show();
                                }
                            });

                }
            }
        }, 500);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_LOGIN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if(resultCode == RESULT_OK) {
                // Successfully signed in
                mUser = FirebaseAuth.getInstance().getCurrentUser();
                login();
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the bank button. Otherwise check
                // response.getError().getErrorCode() and handle the error
                if(response != null) {
                    Log.d(TAG, "onActivityResult: Authenticate Error!");
                }
                Toast.makeText(this, "Authenticate Error: ", Toast.LENGTH_LONG).show();
                checkSignIn();
            }
        }
    }
}
