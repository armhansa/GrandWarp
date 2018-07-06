package com.armhansa.app.grandwarp.model;

import android.net.Uri;

import com.armhansa.app.grandwarp.validate.PatternValidation;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;

public class User {

    @Exclude
    private static User userInstance;

    @Exclude
    private String userID;

    private String name;
    private String phoneNumber;
    private String profile;
    private String description;

    private User() {}

    public static User getInstance() {
        if(userInstance == null) {
            userInstance = new User();
        }
        return userInstance;
    }

    public static void setInstance(User user) {
        userInstance = user;
    }

    public void setProviderData(FirebaseUser mUser) {
        String displayName = mUser.getDisplayName();
        Uri photoUrl = mUser.getPhotoUrl();
        String phoneNumber = mUser.getPhoneNumber();

        setUserID(mUser.getUid());
        if(displayName != null) setName(displayName);
        if(photoUrl != null) setProfile(photoUrl.toString());
        if(phoneNumber != null) {
            setPhoneNumber(phoneNumber);
        }
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getUserID() {
        return userID;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        if(phoneNumber.length() > 10) {
            int length = phoneNumber.length();
            phoneNumber = phoneNumber.substring(length-9, length);
            phoneNumber = "0"+phoneNumber;
        }
        this.phoneNumber = phoneNumber;
    }

    public String getProfile() {
        return profile;
    }
    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

}
