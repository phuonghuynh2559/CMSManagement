package com.ChildMonitoringSystem.managerapp.sharereferen;

import android.content.Context;
import android.content.SharedPreferences;

public class MyShareReference {
    private static final String MY_SHARE  = "MY_SHARE";
    private Context mContext;

    public MyShareReference(Context mContext) {
        this.mContext = mContext;
    }

    public void putValueString(String key, String value)
    {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE,0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public String getValueString(String key)
    {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE,0);
        return sharedPreferences.getString(key,"");
    }
    public String getValueString11(String key)
    {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(MY_SHARE,0);
        return sharedPreferences.getString(key,"");
    }
}
