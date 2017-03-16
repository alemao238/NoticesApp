package com.example.dione.noticesapp.modules.models;

/**
 * Created by Donds on 3/14/2017.
 */

public class AccountsModel {
    private String displayName;
    private String photoUrl;
    private String uid;
    public AccountsModel() {
    }
    public AccountsModel(String displayName, String photoUrl, String uid) {
        this.displayName = displayName;
        this.photoUrl = photoUrl;
        this.uid = uid;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
