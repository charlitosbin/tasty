package com.example.tasty.Services;


import android.os.AsyncTask;

import com.example.tasty.Models.DirectionsModel;
import com.example.tasty.Utils.MarkerUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.maps.android.PolyUtil;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DirectionServices {

    private static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();


    private GoogleMap googleMap;
    private List<Marker> markers = new ArrayList<Marker>();
    private String strGnericUrl = "http://maps.googleapis.com/maps/api/directions/json";

    private List<LatLng> latLngs = new ArrayList<LatLng>();
    private boolean directionsFetched = false;

    public DirectionServices(GoogleMap googleMap){
        this.googleMap = googleMap;
    }

    public void clearMarkers(){
        MarkerUtil.clearMarkers(googleMap,markers);
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
            try{
                HttpRequestFactory requestFactory = HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                    @Override
                    public void initialize(HttpRequest httpRequest) {
                        httpRequest.setParser(new JsonObjectParser(JSON_FACTORY));
                    }
                });

                GenericUrl url = new GenericUrl(strGnericUrl);
                url.put("origin",origin);
                url.put("destination",destination);
                url.put("sensor",false);

                HttpRequest request = requestFactory.buildGetRequest(url);
                HttpResponse httpResponse = request.execute();
                DirectionsModel directionsModel = httpResponse.parseAs(DirectionsModel.class);

                String encodedPoints = directionsModel.routes.get(0).overviewPolyline.points;
                latLngs = PolyUtil.decode(encodedPoints);

            }catch (Exception ex){
                ex.printStackTrace();
            }

            return null;
        }

        protected  void onProgressUpdate(Integer... progress){}

        protected void onPostExecute(Void result){
            directionsFetched=true;
            MarkerUtil.addPolylineToMap(googleMap,latLngs);
        }

    }
}
