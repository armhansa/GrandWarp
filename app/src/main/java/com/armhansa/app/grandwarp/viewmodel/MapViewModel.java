package com.armhansa.app.grandwarp.viewmodel;

import android.arch.lifecycle.ViewModel;

import com.armhansa.app.grandwarp.model.Shop;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.ArrayList;

public class MapViewModel extends ViewModel {

    private SupportMapFragment mapFragment;

    private GoogleMap map;
    private ArrayList<Shop> shops;

    public MapViewModel(SupportMapFragment mapFragment) {
        this.mapFragment = mapFragment;
    }

    public ArrayList<Shop> getShops() {
        if(shops == null) {
            shops = new ArrayList<>();
//            loadShops();
        }
        return shops;
    }

    public GoogleMap getMap() {
        if(map == null) {

        }
        return map;
    }
}
