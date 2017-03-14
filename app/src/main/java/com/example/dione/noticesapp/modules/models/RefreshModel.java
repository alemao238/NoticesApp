package com.example.dione.noticesapp.modules.models;

/**
 * Created by Donds on 3/13/2017.
 */

public class RefreshModel {
    private boolean refresh;
    public RefreshModel(){}
    public RefreshModel(boolean refresh) {
        this.refresh = refresh;
    }

    public boolean isRefresh() {
        return refresh;
    }

    public void setRefresh(boolean refresh) {
        this.refresh = refresh;
    }


}
