package com.example.tasty;

import android.Manifest;
import android.app.SearchManager;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.tasty.Components.GoogleApiCallbacksImplementation;
import com.example.tasty.Models.RestaurantModel;
import com.example.tasty.Services.DummyServices;
import com.example.tasty.Utils.MarkerUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;


import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback
{

    private MapFragment mapFragment;
    private GoogleMap googleMap;
    private SearchView searchView;
    private GoogleApiClient mGoogleApiClient;
    private GoogleApiCallbacksImplementation mGoogleApliClientImplementation;
    private DummyServices dummyServices;
    private List<RestaurantModel> restaurantModelList;

    private LatLng currentPosition;

    interface GoogleGetLocationCallback{
        void DrawLocation(Location location);
    }

    public class GoogleGetLocationImplementation implements GoogleGetLocationCallback{
        @Override
        public void DrawLocation(Location location) {
            Log.d("Callback", "I'm inside callback");
            Log.d("Latitude", String.valueOf(location.getLatitude()));
            Log.d("Longitude", String.valueOf(location.getLongitude()));
            if(location != null)
                currentPosition = new LatLng(location.getLatitude(),location.getLongitude());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dummyServices = new DummyServices();

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setGoogleMap();
        buildGoogleApiClient();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setQueryHint(getResources().getString(R.string.search_by_food));

        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                makeSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_geolocation){
            activateGeolocation();
        }else if (id == R.id.filter_option_restaurant) {
            //TODO: When user filter by restaurant
            searchView.setQueryHint(getResources().getString(R.string.search_by_food));
            return true;
        }else if(id == R.id.filter_option_money){
            //TODO: When user filter by money
            searchView.setQueryHint(getResources().getString(R.string.search_by_price));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        this.googleMap = map;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        this.googleMap.setMyLocationEnabled(true);
    }

    @Override
    protected void onStop(){
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void setGoogleMap(){
        try {
            if(googleMap == null) {
                mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.main_map);
                GoogleMapOptions mapOptions = new GoogleMapOptions()
                        .mapType(GoogleMap.MAP_TYPE_NORMAL)
                        .zoomControlsEnabled(true)
                        .compassEnabled(true);

                mapFragment.getMapAsync(this);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected  synchronized  void buildGoogleApiClient() {
        Log.d("Building", "Building googleAPIClient");
        if(mGoogleApiClient == null){
            GoogleGetLocationImplementation callbackImplementation = new GoogleGetLocationImplementation();

            mGoogleApliClientImplementation = new GoogleApiCallbacksImplementation(this);
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(mGoogleApliClientImplementation)
                    .addOnConnectionFailedListener(mGoogleApliClientImplementation)
                    .addApi(LocationServices.API)
                    .build();
            mGoogleApliClientImplementation.setGoogleApiClient(mGoogleApiClient);
            mGoogleApliClientImplementation.setCallback(callbackImplementation);
        }
    }

    private void makeSearch(String query)
    {
        System.out.println(query);
    }

    private void activateGeolocation()
    {
        if(mGoogleApiClient != null){
            mGoogleApiClient.connect();
        }

        Log.d("Button press", "GPS Press");
        restaurantModelList = dummyServices.GetRestaurants("mexican");
        MarkerUtil.addMarkers(googleMap,restaurantModelList);
        MarkerUtil.addMarker(googleMap,currentPosition, "You are here",true);

    }
}
