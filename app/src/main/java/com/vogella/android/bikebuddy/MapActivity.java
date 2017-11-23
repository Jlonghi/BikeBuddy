package com.vogella.android.bikebuddy;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    //declaring the key variables
    long sTime;
    long dist = 0;
    private GoogleMap mMap;

    Location firstLoc, secondLoc;

    //declaring locationManager for location updating
    LocationManager locationManager;

    //defining locationListener to track the persons location
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //update map and stats here
            //move camera
            LatLng currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLoc));
            //reset variables

            secondLoc = location;

            if(firstLoc != null) {
                //calculate distance traveled in 1 second
                float distance = firstLoc.distanceTo(secondLoc);

                //set speed
                TextView speed = (TextView) findViewById(R.id.speed);
                speed.setText(distance + " m/s");

                //update total distance
                dist += distance;
            }

            firstLoc = secondLoc;
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    //not recognizing requestLocationUpdates for some reason
    //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    //updating the data when the user ends their ride
    public void handleEndRideButton(){
        long eTime = SystemClock.elapsedRealtime();
        long dTime = eTime - sTime;
        long elapsed = dTime / 1000;
        Bundle bike = getIntent().getExtras();
        Bundle bike2 = new Bundle();
        bike2.putLong("id", bike.getLong("id"));
        bike2.putString("bikeName", bike.getString("bikeName"));
        if(elapsed > bike.getLong("longestDuration")){
            BikeDbHelper mDbHelper = new BikeDbHelper(getApplicationContext());
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            mDbHelper.updateBikeDuration(bike.getString("bikeName"), elapsed, db);
            db.close();
            bike2.putLong("longestDuration", elapsed);
        }
        else{
            bike2.putLong("longestDuration", bike.getLong("longestDuration"));
        }

        if(dist > bike.getLong("longestDistance")){
            BikeDbHelper mDbHelper = new BikeDbHelper(getApplicationContext());
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            mDbHelper.updateBikeLongestDistance(bike.getString("bikeName"), (int)dist, db);
            db.close();
            bike2.putLong("longestDistance", dist);
        }
        else{
            bike2.putLong("longestDistance", bike.getLong("longestDistance"));
        }

        BikeDbHelper mDbHelper = new BikeDbHelper(getApplicationContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        mDbHelper.updateBikeDistance(bike.getString("bikeName"), (int)(dist + bike.getLong("distance")), db);
        db.close();

        bike2.putLong("distance", bike.getLong("distance") + dist);
        Intent selectIntent = new Intent(this, OptionActivity.class);
        selectIntent.putExtras(bike2);
        startActivity(selectIntent);
    }

    //perform search on the map
    public void handleSearchButton(){
        Spinner spinner = (Spinner)findViewById(R.id.searchType);
        if(spinner.getSelectedItem().toString() == "Bike Stores"){

        }
        else if(spinner.getSelectedItem().toString() == "Indoor Parking"){

        }
        else if(spinner.getSelectedItem().toString() == "Outdoor Parking"){

        }
        else if(spinner.getSelectedItem().toString() == "Ring Parking"){

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        sTime = SystemClock.elapsedRealtime();

        //defining the location manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 3, locationListener);
        firstLoc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        //performing the required permission checks for the app
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 12345);
        }else{
            System.out.println("permission was already granted! Getting Location...");
        }

        //setting the on click listeners
        Button endRide = (Button) findViewById(R.id.endR);
        endRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleEndRideButton();
            }
        });

        Button search = (Button) findViewById(R.id.searchBtn);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSearchButton();
            }
        });

        //defining the values for the spinner
        Spinner spinner = (Spinner) findViewById(R.id.searchType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.searches, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);

        //setting up the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    //setting map default location
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }
        mMap.setMyLocationEnabled(true);
    }
}
