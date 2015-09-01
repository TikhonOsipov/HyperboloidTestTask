package com.tixon.hyperboloidtesttask;

import java.util.ArrayList;

public class Task {
    int id, price, zoomLevel;
    long date;
    double latitude, longitude;
    boolean translation;
    String title, text, longText, durationLimitText, locationText;
    ArrayList<String> prices;
    ArrayList<String> descriptions;

    public Task() {
        prices = new ArrayList<>();
        descriptions = new ArrayList<>();
    }
}
