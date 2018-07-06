package com.armhansa.app.grandwarp.model;

import java.util.ArrayList;

public class UserShops {

    private static UserShops userShopsInstance;

    private ArrayList<Shop> shops;

    private UserShops() {
        shops = new ArrayList<>();
    }

    public static UserShops getInstance() {
        if(userShopsInstance == null) {
            userShopsInstance = new UserShops();
        }
        return userShopsInstance;
    }

    public ArrayList<Shop> getShops() {
        return shops;
    }

    public Shop getShop(int index) {
        return shops.get(index);
    }

    public void add(Shop shop) {
        shops.add(shop);
    }
    public void delete(Shop shop) {
        shops.remove(shop);
    }
    public int size() {
        return shops.size();
    }
    public void clear() {
        shops.clear();
    }

}
