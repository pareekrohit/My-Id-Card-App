package com.institudeidcard.user.institudeidmakerapp.SharedPreference_class;

import android.content.Context;
import android.content.SharedPreferences;

public class StudentSavePref {

    private static Context context;

    public StudentSavePref(Context context) {
        this.context = context;
    }

    public final static String PREFS_NAME = "appname_prefs";

    public static void setStr(String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getStr(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(key, "DNF");
    }

    //For Edit and insert status passing
    public static void setStatus(String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getStatus(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(key, "DNF");
    }

    //for SID Passing
    public static void setSID(String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getSID(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        return prefs.getString(key, "DNF");
    }

    public static void setStudList(String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(key, value);
        editor.commit();
    }

    public static String getStudList(String key) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);

        return prefs.getString(key, "DNF");

    }

}

/*   // To Save a value

            StudentSavePref savepref = new StudentSavePref(mctx);
            savepref.setStr("mobile",mobile);

     // To Retreive a value

             savepref.getStr("mobile");

*/