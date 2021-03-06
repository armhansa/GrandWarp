package com.armhansa.app.grandwarp.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.armhansa.app.grandwarp.R;
import com.armhansa.app.grandwarp.database.ShopDatabase;
import com.armhansa.app.grandwarp.model.Shop;
import com.armhansa.app.grandwarp.model.holder.FavShops;
import com.armhansa.app.grandwarp.model.holder.User;
import com.armhansa.app.grandwarp.model.holder.UserShops;
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
import java.util.Iterator;
import java.util.List;

public class AuthenticationActivity extends AppCompatActivity {

    private static final String TAG = "AuthenticationActivity";

    private static final int RC_LOGIN = 123;
    // Choose authentication providers
    private List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.FacebookBuilder().build(),
            new AuthUI.IdpConfig.PhoneBuilder().build());

    // Firebase
    private FirebaseUser mUser;
    private DatabaseReference mDatabaseRef;

    // For show processing
    private ProgressDialog progress;
    private int readyUse;

    // Listener for Download data from database
    private final ValueEventListener USER_LISTENER = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            getUserShops(dataSnapshot);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            createAlertDialog();
        }
    };
    private final ValueEventListener FAV_LISTENER = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            getFavoriteShops(dataSnapshot);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            createAlertDialog();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkSignInStatus();
            }
        }, 500);

        readyUse = 0;
    }

    private void checkSignInStatus() {
        if(mUser == null) {
            startLoginProvider();
            Log.d(TAG, "checkSignInStatus: User is null");
        } else {
            mDatabaseRef.child("users").child(mUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataChange: ");
                    User tmpUser = dataSnapshot.getValue(User.class);
                    if(tmpUser != null) {
                        // Set User from FirebaseDatabase to User Singleton
                        User.setInstance(tmpUser);

                        Log.d(TAG, "onDataChange: has user in db");

                        getShopOwners();
                    } else {

                        Log.d(TAG, "onDataChange: hasn't user in db");

                        startRegister();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(AuthenticationActivity.this
                            , "Get UserRegistered Fail. Try again.", Toast.LENGTH_LONG).show();

                    Log.d(TAG, "onCancelled: ");
                    checkSignInStatus();
                }
            });
        }
    }

    private void startLoginProvider() {
        // Create and launch sign-in intent
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build()
                , RC_LOGIN);
        Log.d(TAG, "startLoginProvider: ");
    }

    private void startRegister() {
        Log.d(TAG, "startRegister: ");

        User currentUser = User.getInstance();
        currentUser.setProviderData(mUser);

        startActivity(new Intent(AuthenticationActivity.this, RegisterActivity.class));
        finish();
    }

    private void getShopOwners() {
        Log.d(TAG, "Login and Finished()");

        User currentUser = User.getInstance();
        currentUser.setUserID(mUser.getUid());

        progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        Log.d(TAG, "------------------------------------------------");
        Log.d(TAG, "*UserAnotherTest: "+currentUser.getName()+"    *");
        Log.d(TAG, "------------------------------------------------");
        ShopDatabase shopDB = new ShopDatabase();
        shopDB.getOnceManageShop(currentUser.getUserID(), USER_LISTENER);
        shopDB.getOnceFavoriteShop(currentUser.getUserID(), FAV_LISTENER);
    }

    private void getUserShops(DataSnapshot dataSnapshot) {
        Iterator<DataSnapshot> dataSnapshotsManage = dataSnapshot.getChildren().iterator();

        UserShops userShops = UserShops.getInstance();
        while(dataSnapshotsManage.hasNext()) {
            DataSnapshot dataSnapshotChild = dataSnapshotsManage.next();
            userShops.add(dataSnapshotChild.getValue(Shop.class));
        }

        progress.dismiss();

        checkPreparedToLogin();
    }

    private void getFavoriteShops(DataSnapshot dataSnapshot) {
        Iterator<DataSnapshot> dataSnapshotsManage = dataSnapshot.getChildren().iterator();

        FavShops favShops = FavShops.getInstance();
        while(dataSnapshotsManage.hasNext()) {
            DataSnapshot dataSnapshotChild = dataSnapshotsManage.next();
            favShops.add(dataSnapshotChild.getValue(Shop.class));
        }

        progress.dismiss();

        checkPreparedToLogin();
    }

    private void checkPreparedToLogin() {
        if(++readyUse == 2) {
            startActivity(new Intent(AuthenticationActivity.this, MainActivity.class));
            finish();
        }
    }

    private void createAlertDialog() {
        progress.dismiss();

        Log.d(TAG, "createAlertDialog: ");
        AlertDialog.Builder builder =
                new AlertDialog.Builder(AuthenticationActivity.this);
        builder.setMessage("We can not prepare the values.\nPlease make sure you are have the internet.");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                getShopOwners();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_LOGIN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if(resultCode == RESULT_OK) {
                // Successfully signed in
                mUser = FirebaseAuth.getInstance().getCurrentUser();
                getShopOwners();
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the bank button. Otherwise check
                // response.getError().getErrorCode() and handle the error
                if(response == null) {
                    finish();
                }
                else {
                    Log.d(TAG, "onActivityResult: Authenticate Error!");
                    Toast.makeText(this, "Authenticate Error: Please try again.", Toast.LENGTH_LONG).show();
                    checkSignInStatus();
                }
            }
        }
    }
}
