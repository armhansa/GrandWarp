package com.armhansa.app.grandwarp.model;

import java.util.ArrayList;

public class User {

    private String id;
    private String name;
    private String tel;
    private String status;
    private String profile;
    private ArrayList<String> shops;
//    private ArrayList<String> following;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }
    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getProfile() {
        return profile;
    }
    public void setProfile(String profile) {
        this.profile = profile;
    }

    public ArrayList<String> getShops() {
        return shops;
    }
    public void setShops(ArrayList<String> shops) {
        this.shops = shops;
    }

}
