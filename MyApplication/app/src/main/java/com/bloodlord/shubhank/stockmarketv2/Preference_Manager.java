package com.bloodlord.shubhank.stockmarketv2;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Shubhank on 19-10-2016.
 */

public class Preference_Manager {

    SharedPreferences preference;
    SharedPreferences.Editor editor;
    Context context1;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "welcome";
    private static final String FIRST_TIME_LAUNCH = "FirstTimeLaunch";

    public Preference_Manager(Context context) {
        this.context1 = context;
        preference = context1.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = preference.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
            editor.putBoolean(FIRST_TIME_LAUNCH, isFirstTime);
            editor.commit();
    }

    public boolean isFirstTimeLaunch() {
            return preference.getBoolean(FIRST_TIME_LAUNCH, true);
    }

}
