package com.example.tasty.Utils;

import android.util.Log;

import com.example.tasty.Models.RestaurantModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public final class GoogleMapUtis {

    public static void addMarkers(GoogleMap googleMap,  List<RestaurantModel> restaurantModelList){
        if(googleMap != null && restaurantModelList != null){
            googleMap.clear();
        }

        List<MarkerOptions> lstMarkers = new ArrayList<MarkerOptions>();

        for (RestaurantModel restaurant : restaurantModelList){
            LatLng latLng = new LatLng(restaurant.getLatitude(), restaurant.getLongitude());
            MarkerOptions marker = addMarker(googleMap,latLng, restaurant.getName(),false,0);
            lstMarkers.add(marker);
        }
    }

    public static  void addMarkerAndCenterCamera(GoogleMap googleMap,LatLng position, String title) {
        MarkerOptions marker = addMarker(googleMap, position,title,true, BitmapDescriptorFactory.HUE_BLUE);

        centerCamera(googleMap, marker);
    }

    public static MarkerOptions addMarker(GoogleMap googleMap, LatLng latLng, String title, boolean displayTitle, float color){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title(title);

        if(displayTitle){
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(color));
        }

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
        Log.d("map", "map");
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

    public  static void centerCamera(GoogleMap googleMap, MarkerOptions marker){
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 11);
        googleMap.animateCamera(cameraUpdate);
    }

    public static void clearMarkers(GoogleMap googleMap, List<Marker> markers){
        if(googleMap != null){
            googleMap.clear();
            markers.clear();
        }
    }

    public static  void addPolylineToMap(GoogleMap googleMap, List<LatLng> latLngs){
        PolylineOptions options = new PolylineOptions();
        for(LatLng latLng : latLngs){
            options.add(latLng);
        }
        googleMap.addPolyline(options);

        if(latLngs.size() > 1){
            LatLng origin = latLngs.get(0);
            LatLng destination = latLngs.get(latLngs.size()-1);

            addMarker(googleMap, destination, "Destino", true, BitmapDescriptorFactory.HUE_RED);
            addMarker(googleMap, origin, "Origen", true, BitmapDescriptorFactory.HUE_BLUE);
        }

    }

    public static void fixZoomForLatLngs(GoogleMap googleMap, List<LatLng> latlngs){
        if(latlngs!= null && latlngs.size() > 0){
            LatLngBounds.Builder bc = new LatLngBounds.Builder();

            for(LatLng latLng : latlngs){
                bc.include(latLng);
            }

            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bc.build(),50),4000,null);
        }
    }
}