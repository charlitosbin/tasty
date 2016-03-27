package com.example.tasty.Services;


import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;

import com.example.tasty.MainActivity;
import com.example.tasty.Models.PlacesModel;
import com.example.tasty.Models.RestaurantModel;
import com.example.tasty.R;
import com.example.tasty.Utils.Util;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GoogleApiServices extends AsyncTask<URL, String, Void> {

    private Context context;
    private MainActivity.GoogleApiServicesImplementation callback;

    private String restaurantType;
    private LatLng latLng;

    private String strGnericUrl = "https://maps.googleapis.com/maps/api/place/textsearch/json";

    private static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    public GoogleApiServices(Context context, LatLng latLng, String restaurantType){
        this.restaurantType = restaurantType;
        this.context = context;
        this.latLng = latLng;
    }

    public void setCallback(MainActivity.GoogleApiServicesImplementation callback){
        this.callback = callback;
    }

    private String getCoordParam(LatLng latLng)
    {
        return latLng.latitude + "," + latLng.longitude;
    }
    private String getRestaurantType(){
        return restaurantType;
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
            url.put("query",getRestaurantType());
            url.put("sensor",false);
            url.put("location",getCoordParam(latLng));
            url.put("radius",10000);
            url.put("key", context.getResources().getString(R.string.google_api_web));

            HttpRequest request = requestFactory.buildGetRequest(url);
            HttpResponse httpResponse = request.execute();
            PlacesModel placesModel = httpResponse.parseAs(PlacesModel.class);
            transformPlacesToRestaurants(placesModel);

        }catch (Exception ex){
            ex.printStackTrace();
        }

        return null;
    }

    private void transformPlacesToRestaurants(PlacesModel placesModel){
        if(!placesModel.results.isEmpty()){
            List<RestaurantModel> restaurants = new ArrayList<RestaurantModel>();

            for(int i = 0; i < placesModel.results.size(); i++){
               PlacesModel.Result result =  placesModel.results.get(i);
                RestaurantModel restaurant = new RestaurantModel();
                restaurant.setName(result.name);
                restaurant.setAddress(Util.getRestaurantAddress(result.formattedAddress));
                restaurant.setZipCode(Util.getRestaurantZipCode(result.formattedAddress));

                int x = 1+2;

            }
        }
    }

}
