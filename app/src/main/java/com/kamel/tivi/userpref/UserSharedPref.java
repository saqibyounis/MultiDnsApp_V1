package com.kamel.tivi.userpref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class UserSharedPref {

    public static SharedPreferences.Editor getSharedPrefEditor(Context context){

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = pref.edit();

        return editor;
    }
    public static SharedPreferences getSharedPref(Context context){
        SharedPreferences pref =  PreferenceManager.getDefaultSharedPreferences(context);

        return pref;
    }

}
