package com.tixon.hyperboloidtesttask;

import java.util.ArrayList;

public class Task {
    int id;
    long date;
    double latitude, longitude;
    String title, text, longText, locationText;
    ArrayList<String> prices;
    ArrayList<String> descriptions;

    public Task(double lat, double lng, int id, long date, String title, String text, String longText, String locationText) {
        prices = new ArrayList<>();
        descriptions = new ArrayList<>();
        this.latitude = lat;
        this.longitude = lng;
        this.id = id;
        this.date = date;
        this.title = title;
        this.text = text;
        this.longText = longText;
        this.locationText = locationText;
    }

    public void addPrice(int price, String description) {
        prices.add(String.valueOf(price));
        descriptions.add(description);
    }
}
