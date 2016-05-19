package com.adamjaynick.datemyson.Modules;

import android.content.Context;
import android.content.SharedPreferences;

import com.facebook.AccessToken;

/**
 * Created by amora on 5/18/2016.
 */
public class ProfileManager {
    private static final String TAG = "ProfileManager";

    private static final String PREFS_FILE = "profile";
    private static final String PREF_TOKEN = "ProfileManager.token";

    private static ProfileManager    sProfileManager;
    private        Context           mAppContext;
    private        SharedPreferences mSharedPreferences;
    private        String            mTokenString;

    public static ProfileManager get(Context c){

        if(sProfileManager == null){
            sProfileManager = new ProfileManager(c.getApplicationContext());
        }

        return sProfileManager;

    }

    private ProfileManager(Context appContext){

        mAppContext        = appContext;
        mSharedPreferences = mAppContext.getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        mTokenString       = mSharedPreferences.getString(PREF_TOKEN, null);
    }

    public void setToken(String tokenString){
        mTokenString = tokenString;
        mSharedPreferences.edit().putString(PREF_TOKEN, tokenString);
    }

    public String getToken(){

        return mTokenString;
    }

}
