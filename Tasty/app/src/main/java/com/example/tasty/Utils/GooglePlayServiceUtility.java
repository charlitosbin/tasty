package com.example.tasty.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class GooglePlayServiceUtility {
    private static final String LOGTAG="ServiceUtil";

    public static boolean isPlayServiceAvailable(Context context){
        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);

        switch (isAvailable){
            case ConnectionResult.SUCCESS :
                Log.i(LOGTAG, "Connected to service");
                return true;
            case ConnectionResult.SERVICE_MISSING:
                Log.i(LOGTAG, "Service missing");
                break;
            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                break;
            case ConnectionResult.SERVICE_INVALID:
                Log.i(LOGTAG, "Service invalid");
                break;
            default:
                Log.i(LOGTAG, "Different value: " + isAvailable);
                break;
        }

        Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable,
                (Activity)context, 1);
        dialog.show();

        return false;
    }
}
