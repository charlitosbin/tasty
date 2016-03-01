package com.example.tasty.Utils;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.example.tasty.Models.RestaurantModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public final class MarkerUtil {

    public static void addMarkers(GoogleMap googleMap,  List<RestaurantModel> restaurantModelList){
        if(googleMap != null && restaurantModelList != null){
            googleMap.clear();
        }

        List<MarkerOptions> lstMarkers = new ArrayList<MarkerOptions>();

        for (RestaurantModel restaurant : restaurantModelList){
            LatLng latLng = new LatLng(restaurant.getLatitude(), restaurant.getLongitude());
            MarkerOptions marker = addMarker(googleMap,latLng, restaurant.getName(),false);
            lstMarkers.add(marker);
        }

        centerCamera(googleMap,lstMarkers);
    }

    public static MarkerOptions addMarker(GoogleMap googleMap, LatLng latLng, String title, boolean displayTitle){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(title);

        Marker m = googleMap.addMarker(markerOptions);

        if(displayTitle){
            m.showInfoWindow();
        }

        return  markerOptions;
    }

    public static void addMarker(GoogleMap googleMap, RestaurantModel restaurant){
        LatLng position = new LatLng(restaurant.getLatitude(),restaurant.getLongitude());

        googleMap.addMarker(new MarkerOptions()
        .position(position)
        .title(restaurant.getName()));
        Log.d("map","map");
    }

    public static void setupCamera(GoogleMap googleMap, LatLng latLng, int zoom){
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        googleMap.moveCamera(cameraUpdate);
    }

    public static void centerCamera(GoogleMap googleMap, List<MarkerOptions> markers){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(MarkerOptions marker : markers){
            builder.include(marker.getPosition());
        }

        LatLngBounds bounds = builder.build();

        int padding = 0;
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.animateCamera(cameraUpdate);

    }
}