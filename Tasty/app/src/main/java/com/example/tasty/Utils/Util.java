package com.example.tasty.Utils;


import android.support.design.widget.Snackbar;
import android.widget.LinearLayout;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.ArrayList;

public final class Util {

    public static void example()
    {
        System.out.println("hola");
    }

    public static Snackbar createSnackbar(LinearLayout ll, String title){
        Snackbar snack = Snackbar.make(ll,title,Snackbar.LENGTH_LONG);

        return snack;
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