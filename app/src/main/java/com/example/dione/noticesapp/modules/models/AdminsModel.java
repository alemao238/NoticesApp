package com.example.dione.noticesapp.modules.models;

/**
 * Created by Donds on 3/14/2017.
 */

public class AdminsModel {
    private String imageUrl;
    private String name;
    public AdminsModel(String name, String imageUrl) {
        this.imageUrl = imageUrl;
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }
}
