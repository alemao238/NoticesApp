package com.example.dione.noticesapp.utilities;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Donds on 3/15/2017.
 */

public class Helpers {
    private Context context;
    private Toast toast;
    public Helpers(Context context) {
        this.context = context;
    }
    public void showToast(String message) {
        if (toast != null) toast.cancel();
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
