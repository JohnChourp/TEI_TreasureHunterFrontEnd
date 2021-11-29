package org.codegrinders.treasure_hunter_mobile.settings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

public class Settings {

    public static int musicVol;
    public static int soundVol;

    public static SharedPreferences settingsPref;
    public static SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public static void init(Context context) {
        settingsPref = context.getSharedPreferences("Settings", 0);
        editor = settingsPref.edit();

        musicVol = settingsPref.getInt("musicVol", 60);
        soundVol = settingsPref.getInt("soundVol", 60);
    }

    public static void saveInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

}