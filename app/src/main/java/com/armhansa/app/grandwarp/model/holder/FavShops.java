package com.armhansa.app.grandwarp.model.holder;

import com.armhansa.app.grandwarp.model.Shop;

import java.util.ArrayList;

public class FavShops {

    private static FavShops favShopsInstance;

    private ArrayList<Shop> shops;

    private FavShops() {
        shops = new ArrayList<>();
    }

    public static FavShops getInstance() {
        if(favShopsInstance == null) {
            favShopsInstance = new FavShops();
        }
        return favShopsInstance;
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
