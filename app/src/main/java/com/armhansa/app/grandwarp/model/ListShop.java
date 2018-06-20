package com.armhansa.app.grandwarp.model;

import java.util.ArrayList;

public class ListShop {

    private ArrayList<Shop> myShops;

    public ArrayList<Shop> getMyShops() {
        return myShops;
    }
    public void setMyShops(ArrayList<Shop> myShops) {
        this.myShops = myShops;
    }

    public void addShop(Shop shop) {
        myShops.add(shop);
    }

}
