package com.example.dione.noticesapp.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Donds on 3/14/2017.
 */

public class SharedPreferenceManager {
    private SharedPreferences sharedPreferences;
    private static String FILE_KEY = "shared_preferences";
    public SharedPreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences(FILE_KEY, Context.MODE_PRIVATE);
    }
    public void saveStringPreference(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
    public String getStringPreference(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }
    public void clearPreference() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
