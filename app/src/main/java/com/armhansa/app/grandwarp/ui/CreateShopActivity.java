package com.armhansa.app.grandwarp.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.armhansa.app.grandwarp.R;
import com.armhansa.app.grandwarp.model.LatLng;
import com.armhansa.app.grandwarp.model.ManageShop;
import com.armhansa.app.grandwarp.model.Shop;
import com.armhansa.app.grandwarp.model.User;
import com.armhansa.app.grandwarp.model.UserShops;
import com.armhansa.app.grandwarp.validate.PatternValidation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class CreateShopActivity extends AppCompatActivity {

    private static final String TAG = "CreateShopActivity";

    // Firebase
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private StorageReference uploadRef;

    // For Pick Shop Image
    private RequestOptions mOption;
    private static final int PICK_IMAGE = 1;
    private Bitmap selectedImage;

    // Get Last Location
    private FusedLocationProviderClient mFusedLocationClient;

    // View
    private TextView nameTxt;
    private TextView typeTxt;
    private TextView openTimeTxt;
    private TextView closeTimeTxt;
    private TextView describeTxt;
    private ImageView profileImg;

    // New Shop Object
    private Shop newShop;

    // User
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_shop);

        viewInitial();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        mOption = new RequestOptions()
                .centerCrop()
                .fitCenter()
                .circleCrop();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        currentUser = User.getInstance();

    }

    private void viewInitial() {
        nameTxt = findViewById(R.id.nameTxt);
        typeTxt = findViewById(R.id.typeTxt);
        openTimeTxt = findViewById(R.id.openTimeTxt);
        closeTimeTxt = findViewById(R.id.closeTimeTxt);
        describeTxt = findViewById(R.id.describeTxt);
        profileImg = findViewById(R.id.profileImg);
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromGallery();
            }
        });
        Button createBtn = findViewById(R.id.createBtn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAlertDialog();
            }
        });
    }

    private void selectImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image."), PICK_IMAGE);
    }

    private void createAlertDialog() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);
        builder.setMessage("Make sure your current location is your shop?");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                createNewShop();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void createNewShop() {
        newShop = new Shop();
        int passed = 0; // if still equal 0 mean pass

        String name = nameTxt.getText().toString();
        String type = typeTxt.getText().toString();
        String openTime = openTimeTxt.getText().toString();
        String closeTime = closeTimeTxt.getText().toString();

        if(!PatternValidation.isNameValid(name, this)) passed--;
        if(type.isEmpty()) passed--;
        if(openTime.isEmpty()) passed--;
        if(closeTime.isEmpty()) passed--;

        if(passed == 0) {
            newShop.setName(name);
            newShop.setType(type);
            newShop.setOpenTime(openTime);
            newShop.setCloseTime(closeTime);
            newShop.setDescribe(describeTxt.getText().toString());
            newShop.setChatNotSeen(0);
            // Add status from openTime and CloseTime
            newShop.setStatus("Close");
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            LatLng currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
                            newShop.setLocation(currentLoc);

                            if(selectedImage != null) {
                                uploadImageToFirebase();
                            } else {
                                Toast.makeText(CreateShopActivity.this
                                        , "Please choose your profiles!"
                                        , Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateShopActivity.this
                        , "Can not get your current location.\nPlease try again."
                        , Toast.LENGTH_LONG).show();
            }
        });
    }

    private void uploadImageToFirebase() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        uploadRef = mStorageRef.child("shopImage").child(nameTxt.getText().toString());
        UploadTask uploadTask = uploadRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                Log.d(TAG, "onSuccess: First");
                uploadRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadUrl = uri;
                        Log.d(TAG, "onSuccess: "+downloadUrl.toString());
                        Toast.makeText(CreateShopActivity.this, "Upload Success!", Toast.LENGTH_LONG).show();

                        newShop.setPicture(downloadUrl.toString());
                        createShopToDatabase();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateShopActivity.this, "Fail to Get Url Please try again."
                                , Toast.LENGTH_LONG).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(CreateShopActivity.this, "Upload Fail!", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void createShopToDatabase() {
        String key = mDatabaseRef.child("shops").push().getKey();

        UserShops.getInstance().add(newShop);
        mDatabaseRef.child("shops").child(key).setValue(newShop);
        mDatabaseRef.child("shopOwners").child(currentUser.getUserID())
                .child(key).setValue(newShop);

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);
                Glide.with(this).load(selectedImage).apply(mOption).into(profileImg);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(CreateShopActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(CreateShopActivity.this, "You don't picked image",Toast.LENGTH_LONG).show();
        }

    }
}
