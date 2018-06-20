package com.armhansa.app.grandwarp.model;

import java.util.ArrayList;

public class Shop {

    private String id;
    private String name;
    private String type;
    private String status;  // On - Off - Off in 10min
    private String describe;
    private ArrayList<String> pictures;
    private Location location;
    private ArrayList<Event> events;
//   private ArrayList<Promotion> promotions;
    private ArrayList<String> ownerIDs;

}
