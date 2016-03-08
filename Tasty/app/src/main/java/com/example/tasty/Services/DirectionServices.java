package com.example.tasty.Services;


import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DirectionServices {

    private static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();


    private GoogleMap googleMap;
    private List<Marker> markers = new ArrayList<Marker>();

    public DirectionServices(GoogleMap googleMap){
        this.googleMap = googleMap;
    }

    public void clearMarkers(){
        if(googleMap != null){
            googleMap.clear();
            markers.clear();
        }
    }

    private class DirectionsFetcher extends AsyncTask<URL, Integer, Void>{

        private String origin;
        private String destination;

        public DirectionsFetcher(String origin, String destination){
            this.origin = origin;
            this.destination = destination;
        }

        @Override
        protected  void onPreExecute(){
            super.onPreExecute();
            clearMarkers();
        }

        @Override
        protected Void doInBackground(URL... params) {
            return null;
        }
    }
}
