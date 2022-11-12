package ca.tnoah.frc.scouting;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {
    public static final String SETTINGS = "settings";

    public static SharedPreferences get(Context context, String name) {
        return context.getSharedPreferences(name, 0);
    }
}