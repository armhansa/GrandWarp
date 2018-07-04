package com.armhansa.app.grandwarp.ui;

import android.app.Activity;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;
    private StorageReference uploadRef;

    private TextView usernameTxt;
    private TextView phoneNumberTxt;
    private TextView descriptionTxt;
    private ImageView profileImg;
    private Button registerBtn;

    private static final int PICK_IMAGE = 24;
    private RequestOptions mOption;
    private Bitmap selectedImage;

    private User registerUser;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userId = FirebaseAuth.getInstance().getUid();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        usernameTxt = findViewById(R.id.usernameTxt);
        phoneNumberTxt = findViewById(R.id.phoneNumberTxt);
        descriptionTxt = findViewById(R.id.describeTxt);
        profileImg = findViewById(R.id.profileImg);
        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromGallery();
            }
        });
        registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        mOption = new RequestOptions()
                .centerCrop()
                .fitCenter()
                .circleCrop();

        registerUser = User.getInstance();

    }

    private void register() {
        registerUser.setUsername(usernameTxt.getText().toString());
        registerUser.setPhoneNumber(phoneNumberTxt.getText().toString());
        registerUser.setDescription(descriptionTxt.getText().toString());
        uploadImageToFirebase();
    }

    private void uploadImageToFirebase() {
        if(selectedImage != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            uploadRef = mStorageRef.child("profiles").child(userId);
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

                            registerUser.setProfile(uri.toString());
                            createUserToDatabase();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterActivity.this, "Fail to Get Url Please try again."
                                    , Toast.LENGTH_LONG).show();
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    Toast.makeText(RegisterActivity.this, "Upload Fail!", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Toast.makeText(this, "Please choose your profiles!", Toast.LENGTH_LONG).show();
        }
    }

    private void createUserToDatabase() {
        mDatabaseRef.child("users").child(userId).setValue(registerUser)
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

    private void selectImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image."), PICK_IMAGE);
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
                Toast.makeText(RegisterActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(RegisterActivity.this, "You haven't picked Image",Toast.LENGTH_LONG).show();
        }
    }
}
