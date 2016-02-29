package com.example.tasty.Utils;

import android.util.Log;

import com.example.tasty.Models.RestaurantModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public final class MarkerUtil {

    public static void addMarker(GoogleMap googleMap, RestaurantModel restaurant){
        LatLng position = new LatLng(restaurant.getLatitude(),restaurant.getLongitude());

        googleMap.addMarker(new MarkerOptions()
        .position(position)
        .title(restaurant.getName()));
        Log.d("map","map");
    }
}