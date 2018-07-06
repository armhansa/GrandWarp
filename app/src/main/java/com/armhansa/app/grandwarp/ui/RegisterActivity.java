package com.armhansa.app.grandwarp.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.armhansa.app.grandwarp.R;
import com.armhansa.app.grandwarp.model.User;
import com.armhansa.app.grandwarp.validate.PatternValidation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    // View
    private TextView nameTxt;
    private TextView phoneNumberTxt;
    private TextView descriptionTxt;
    private ImageView profileImg;

    // For pick image from user's device
    private static final int PICK_IMAGE = 24;
    private RequestOptions mOption;
    private Bitmap selectedImage;

    // Firebase
    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private StorageReference uploadRef;

    // User
    private User currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        currentUser = User.getInstance();

        // Glide Settings
        mOption = new RequestOptions()
                .centerCrop()
                .fitCenter()
                .circleCrop();

        viewInitial();

        // Firebase
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

    }

    public void viewInitial() {
        String profile = currentUser.getProfile();
        String name = currentUser.getName();
        String phoneNumber = currentUser.getPhoneNumber();

        profileImg = findViewById(R.id.profileImg);
        if(profile != null && !profile.isEmpty())
            Log.d(TAG, "viewInitial: "+profile);
            Glide.with(this).load(profile+"?height=500").into(profileImg);
        nameTxt = findViewById(R.id.nameTxt);
        if(name != null)
            nameTxt.setText(name);
        phoneNumberTxt = findViewById(R.id.phoneNumberTxt);
        if(phoneNumber != null)
            phoneNumberTxt.setText(phoneNumber);
        descriptionTxt = findViewById(R.id.describeTxt);

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSelectImageActivity();
            }
        });

        Button registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void startSelectImageActivity() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image."), PICK_IMAGE);
    }

    private void register() {
        String name = nameTxt.getText().toString();
        String phoneNumber = phoneNumberTxt.getText().toString();
        String description = descriptionTxt.getText().toString();
        if(PatternValidation.isNameValid(name, this)
                && PatternValidation.isPhoneNumberValid(phoneNumber, this)) {
            currentUser.setName(name);
            currentUser.setPhoneNumber(phoneNumber);
            currentUser.setDescription(description);

            if(selectedImage != null) {
                uploadImageToFirebase();
            } else if(currentUser.getProfile() != null) {
                createUserInDatabase();
            } else {
                AlertDialog.Builder builder =
                        new AlertDialog.Builder(this);
                builder.setMessage("You don't select profile image.\nAre you want to use default profile?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        currentUser.setProfile("default");
                        createUserInDatabase();
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
        }
    }

    private void uploadImageToFirebase() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        uploadRef = mStorageRef.child("profiles").child(currentUser.getUserID());
        UploadTask uploadTask = uploadRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                Log.d(TAG, "onSuccess: First");
                uploadRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Log.d(TAG, "onSuccess: "+ uri.toString());
                        Toast.makeText(RegisterActivity.this, "Upload Success!", Toast.LENGTH_LONG).show();

                        currentUser.setProfile(uri.toString());
                        createUserInDatabase();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Fail to Get Url. Please try again."
                                , Toast.LENGTH_LONG).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(RegisterActivity.this, "Upload Fail! Please try again.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void createUserInDatabase() {
        mDatabaseRef.child("users").child(currentUser.getUserID()).setValue(currentUser)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterActivity.this, "Register Fail. Please try again.", Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE) {
            if(resultCode == Activity.RESULT_OK) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    selectedImage = BitmapFactory.decodeStream(imageStream);
                    Glide.with(this).load(selectedImage).apply(mOption).into(profileImg);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(RegisterActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(RegisterActivity.this, "You don't pick image.",Toast.LENGTH_LONG).show();
            }

        }
    }

}
