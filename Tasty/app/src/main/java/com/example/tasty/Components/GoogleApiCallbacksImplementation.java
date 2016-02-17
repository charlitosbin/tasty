package com.example.tasty.Components;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.tasty.MainActivity;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class GoogleApiCallbacksImplementation implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    MainActivity.GoogleGetLocationImplementation callback;
    GoogleApiClient mGoogleApiClient;
    Context mContext;

    public GoogleApiCallbacksImplementation(Context context) {
        this.mContext = context;
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient){
        this.mGoogleApiClient = googleApiClient;
    }

    public void setCallback(MainActivity.GoogleGetLocationImplementation callback){
        this.callback = callback;
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        callback.DrawLocation(location);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("FAILEE", "onConnectionFailed: ");
    }
}
