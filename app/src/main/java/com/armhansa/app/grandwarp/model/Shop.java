package com.armhansa.app.grandwarp.model;

public class Shop {

    private int key;
    private String name;
    private String type;
    private String status;  // On - Off - Off in 10min
    private String describe;
    private String picture;
    private LatLng location;
    private String openTime;
    private String closeTime;
    private int chatNotSeen;

    public int getKey() {
        return key;
    }
    public void setKey(int key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescribe() {
        return describe;
    }
    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getPicture() {
        return picture;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }

    public LatLng getLocation() {
        return location;
    }
    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getOpenTime() {
        return openTime;
    }
    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }
    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public int getChatNotSeen() {
        return chatNotSeen;
    }
    public void setChatNotSeen(int chatNotSeen) {
        this.chatNotSeen = chatNotSeen;
    }

}
