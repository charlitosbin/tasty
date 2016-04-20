package com.example.restaurantui.Utils;


import android.content.Context;
import android.provider.Settings;

public final class Util {

    public static String getDeviceId(Context context){
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }
}
