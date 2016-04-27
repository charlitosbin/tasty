package com.example.restaurantui.Utils;


import android.content.Context;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.view.View;

public final class Util {

    public static String getDeviceId(Context context){
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static Snackbar createSnackbar(View ll, String title){
        Snackbar snack = Snackbar.make(ll,title,Snackbar.LENGTH_LONG);

        return snack;
    }
}
