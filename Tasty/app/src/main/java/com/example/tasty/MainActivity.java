package com.example.tasty;

import android.Manifest;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.tasty.Activities.ChatActivity;
import com.example.tasty.Components.GoogleApiCallbacksImplementation;
import com.example.tasty.Components.RestaurantAdapter;
import com.example.tasty.Components.RestaurantRvItemClickListener;
import com.example.tasty.Models.RestaurantModel;
import com.example.tasty.Services.DirectionServices;
import com.example.tasty.Services.DummyServices;
import com.example.tasty.Services.GoogleApiServices;
import com.example.tasty.Utils.DropDownAnim;
import com.example.tasty.Utils.GoogleMapUtis;
import com.example.tasty.Utils.Util;
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


    private LinearLayout llMain;
    private MapFragment mapFragment;
    private GoogleMap googleMap;
    private SearchView searchView;
    private GoogleApiClient mGoogleApiClient;
    private GoogleApiCallbacksImplementation mGoogleApliClientImplementation;
    private DummyServices dummyServices;

    private RecyclerView restaurantRv;
    private RestaurantAdapter adapter;

    private LatLng currentPosition;

    interface GoogleGetLocationCallback{
        void DrawLocation(Location location);
    }

    interface  GoogleApiServicesCallback{
        void DrawList(Context context, List<RestaurantModel> restaurants);
    }

    public class GoogleGetLocationImplementation implements GoogleGetLocationCallback{
        @Override
        public void DrawLocation(Location location) {
            Log.d("Callback", "I'm inside callback");
            Log.d("Latitude", String.valueOf(location.getLatitude()));
            Log.d("Longitude", String.valueOf(location.getLongitude()));

            if(location != null) {
                currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
                GoogleMapUtis.addMarkerAndCenterCamera(googleMap, currentPosition, "Aqui estas");
            }
        }
    }

    public class GoogleApiServicesImplementation implements  GoogleApiServicesCallback{
        @Override
        public void DrawList(Context context, final List<RestaurantModel> restaurants){
            if(restaurants != null && restaurants.size() > 0){
                if(googleMap != null){
                    GoogleMapUtis.addMarkers(googleMap, restaurants);
                    GoogleMapUtis.addMarkerAndCenterCamera(googleMap, currentPosition, "Posicion actual");
                    adapter = new RestaurantAdapter(restaurants);
                    restaurantRv.setAdapter(adapter);
                    restaurantRv.addOnItemTouchListener(
                            new RestaurantRvItemClickListener(context, new RestaurantRvItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    LatLng destinationPosition = new LatLng(restaurants.get(position).getLatitude(),
                                            restaurants.get(position).getLongitude());

                                    new DirectionServices(googleMap, destinationPosition, currentPosition).execute();
                                }
                            })
                    );
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setVariables();
        setRecyclerViewProperties();

        this.dummyServices = new DummyServices();

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
        }else if (id == R.id.chat_with_friends) {
            showChatScreen("Carlos");
            //showPopup();
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

        this.googleMap.setMyLocationEnabled(false);
        this.googleMap.getUiSettings().setCompassEnabled(false);
        this.googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        this.googleMap.getUiSettings().setMapToolbarEnabled(false);
    }

    @Override
    protected void onStop(){
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    private void setVariables(){
        llMain = (LinearLayout)findViewById(R.id.mainLL);
        restaurantRv = (RecyclerView)findViewById(R.id.restaurantRv);
    }

    private void setRecyclerViewProperties(){
        if(restaurantRv != null){
            LinearLayoutManager llm = new LinearLayoutManager(this);
            restaurantRv.setLayoutManager(llm);
        }
    }

    private void showChatScreen(String nickname){
        Intent chatIntent = new Intent(this, ChatActivity.class);
        chatIntent.putExtra("nickname",nickname);

        startActivity(chatIntent);
    }

    private void setGoogleMap(){
        try {
            if(googleMap == null) {
                mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.mainMap);
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
        DropDownAnim a = new DropDownAnim(mapFragment.getView(),1800,false);
        a.setDuration(500);
        mapFragment.getView().startAnimation(a);

        if(!query.isEmpty()) {
            hideKeyboard();
            if (currentPosition != null) {
                GoogleApiServices googleApiServices = new GoogleApiServices(this,currentPosition,query);
                GoogleApiServicesImplementation googleApiServicesImplementation = new GoogleApiServicesImplementation();
                googleApiServices.setCallback(googleApiServicesImplementation);
                googleApiServices.execute();

            } else {
                Snackbar snack = Util.createSnackbar(llMain, getResources().getString(R.string.location_service_error));
                snack.show();
            }
        }else{
            Snackbar snack = Util.createSnackbar(llMain, getResources().getString(R.string.query_null_error));
            snack.show();
        }
    }

    private void activateGeolocation()
    {
        if(mGoogleApiClient != null){
            CheckEnableGPS();
        }
    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromInputMethod(view.getWindowToken(), 0);
        }
    }

    private void CheckEnableGPS() {
        String provider = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(!provider.equals("")){
            mGoogleApiClient.connect();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Enable GPS");  // GPS not found
            builder.setMessage("The app needs GPS to be enabled do you want to enable it in the settings? "); // Want to enable?
            builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                }
            });
            builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                }
            });
            builder.setCancelable(false);
            builder.create().show();
            return;
        }
    }

    private void showPopup(){

        LinearLayout viewGroup = (LinearLayout)findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.name_popup,viewGroup);

        final PopupWindow popup = new PopupWindow(this);
        popup.setContentView(layout);
        popup.setFocusable(true);

        popup.showAtLocation(layout, Gravity.CENTER, 0, 0);

        Button submit = (Button)layout.findViewById(R.id.nickanmeButton);
        final TextView nicknameInput = (TextView)layout.findViewById(R.id.nicknameInput);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popup.dismiss();
                showChatScreen(nicknameInput.getText().toString());
            }
        });
    }
}
