package com.example.tasty.Utils;


import android.location.Geocoder;
import android.support.design.widget.Snackbar;
import android.widget.LinearLayout;
import android.content.Context;
import android.location.Address;

import com.example.tasty.Models.RestaurantModel;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;

public final class Util {

    public static void example()
    {
        System.out.println("hola");
    }

    public static Snackbar createSnackbar(LinearLayout ll, String title){
        Snackbar snack = Snackbar.make(ll,title,Snackbar.LENGTH_LONG);

        return snack;
    }

    public static LatLng getCoordFromAddress(Context context, String address){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        LatLng latLng = null;

        try {
            List<Address> lstAddress = geocoder.getFromLocationName(address,1);
            if(lstAddress.size() > 0){
                latLng = new LatLng(lstAddress.get(0).getLatitude(),
                        lstAddress.get(0).getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return latLng;
    }

    public static RestaurantModel getAddressFromGeo(Context context, LatLng latLng){
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        RestaurantModel restaurantModel = null;

        List<Address> lstAddress = null;
        try {
            lstAddress = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
            if(lstAddress.size() > 0){
                Address address = lstAddress.get(0);
                String strAddress = address.getAddressLine(0);
                String city = address.getLocality();
                String state = address.getAdminArea();
                String country = address.getCountryName();
                String zipCode = address.getPostalCode();
                String name = address.getFeatureName();

                restaurantModel = new RestaurantModel();
                restaurantModel.setName(name);
                restaurantModel.setAddress(strAddress);
                restaurantModel.setCity(city);
                restaurantModel.setState(state);
                restaurantModel.setCountry(country);
                restaurantModel.setZipCode(zipCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  restaurantModel;
    }

    public static String getRestaurantAddress(String formattedAddress){
        if(!formattedAddress.isEmpty()) {
            String[] strSplit = formattedAddress.split(",");
            if(strSplit.length > 2)
                return  strSplit[0] +", "+ strSplit[1];
        }

        return "";
    }

    public static String getRestaurantZipCode(String formattedAddress){
        if(!formattedAddress.isEmpty()){
            String[] strSplit = formattedAddress.split(",");
            if(strSplit.length > 3){
                String[] strZip  = strSplit[2].split(" ");
                if(strZip.length > 0){
                    return strSplit[0];
                }
            }
        }

        return "";
    }


    public static class PolyUtil{
        public static List<LatLng> decode(final String encodedPath) {
            int len = encodedPath.length();

            // For speed we preallocate to an upper bound on the final length, then
            // truncate the array before returning.
            final List<LatLng> path = new ArrayList<LatLng>();
            int index = 0;
            int lat = 0;
            int lng = 0;

            while (index < len) {
                int result = 1;
                int shift = 0;
                int b;
                do {
                    b = encodedPath.charAt(index++) - 63 - 1;
                    result += b << shift;
                    shift += 5;
                } while (b >= 0x1f);
                lat += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

                result = 1;
                shift = 0;
                do {
                    b = encodedPath.charAt(index++) - 63 - 1;
                    result += b << shift;
                    shift += 5;
                } while (b >= 0x1f);
                lng += (result & 1) != 0 ? ~(result >> 1) : (result >> 1);

                path.add(new LatLng(lat * 1e-5, lng * 1e-5));
            }

            return path;
        }

    }
}