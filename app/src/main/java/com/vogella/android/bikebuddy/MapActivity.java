package com.vogella.android.bikebuddy;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.location.places.GeoDataApi;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionApi;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    //declaring the key variables
    long sTime;
    long dist = 0;
    private GoogleMap mMap;

    Location firstLoc, secondLoc;

    private FusedLocationProviderClient mFusedLocationProviderClient;
    LocationRequest mLocationRequest;
    boolean mRequestingLocationUpdates;

    GeoDataClient mGeoDataClient;
    PlaceDetectionClient mPlaceDetectionClient;
    JSONObject jsonRes;
    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
    class RetrieveBikeStores extends AsyncTask<String, Void, String>{
       protected String doInBackground(String... strUrl){
            try {
                URL url = new URL(strUrl[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                String response = convertStreamToString(in);
                return response;
            }catch (IOException e){
                return "exception thrown";
            }
        }
        protected void onPostExecute(String res){
            try{
                System.out.println(res);
                jsonRes = new JSONObject(res);
                Iterator<?> keys = jsonRes.keys();
                while(keys.hasNext()){
                    String key = (String)keys.next();
                    if(key.equals("results")) {
                        JSONArray resultArray = (JSONArray)jsonRes.get(key);
                        for(int i = 0; i < resultArray.length(); i++) {
                            JSONObject resObj  = (JSONObject) resultArray.get(i);
                            JSONObject geometry = (JSONObject) resObj.get("geometry");
                            JSONObject loc = (JSONObject) geometry.get("location");
                            LatLng latLng = new LatLng(Double.parseDouble(loc.get("lat").toString()),Double.parseDouble(loc.get("lng").toString()));
                            //placing marker
                            mMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title(resObj.get("name").toString()));
                        }
                    }
                    else
                        System.out.println(jsonRes.get(key).toString());
                }
            }catch(JSONException e){}
        }
    }
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
    private LocationCallback mLocationCallback;

    //updating the data when the user ends their ride
    public void handleEndRideButton(){
        long eTime = SystemClock.elapsedRealtime();
        long dTime = eTime - sTime;
        long elapsed = dTime / 1000;
        Bundle bike = getIntent().getExtras();
        Bundle bike2 = new Bundle();
        bike2.putLong("id", bike.getLong("id"));
        bike2.putString("bikeName", bike.getString("bikeName"));

        BikeDbHelper mDbHelper = new BikeDbHelper(getApplicationContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        if(elapsed > bike.getLong("longestDuration")){
            mDbHelper.updateBikeDuration(bike.getString("bikeName"), elapsed, db);
            bike2.putLong("longestDuration", elapsed);
        }
        else{
            bike2.putLong("longestDuration", bike.getLong("longestDuration"));
        }

        if(dist > bike.getLong("longestDistance")){
            mDbHelper.updateBikeLongestDistance(bike.getString("bikeName"), (int)dist, db);
            bike2.putLong("longestDistance", dist);
        }
        else{
            bike2.putLong("longestDistance", bike.getLong("longestDistance"));
        }

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
        if(spinner.getSelectedItem().toString().equals("Bike Stores")){
            Toast toast = Toast.makeText(this, "Bike Stores to be implemented", Toast.LENGTH_SHORT);
            toast.show();
            try {
                mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            LatLng curr = new LatLng(location.getLatitude(), location.getLongitude());
                            //String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + location.getLatitude() + "," + location.getLongitude() + "&radius=500&type=bicycle_store&key=%20AIzaSyC4MUJhXp4fBBr2Tfi395Ea5ebP4Ot30Dc";
                            new RetrieveBikeStores().execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + location.getLatitude() + "," + location.getLongitude() + "&radius=1000&type=bicycle_store&key=%20AIzaSyC4MUJhXp4fBBr2Tfi395Ea5ebP4Ot30Dc");

                        }
                    }
                });
            }catch(SecurityException e){}

        }
        else if(spinner.getSelectedItem().toString() == "Indoor Parking"){
            Toast toast = Toast.makeText(this, "Bike Stores to be implemented", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if(spinner.getSelectedItem().toString() == "Outdoor Parking"){
            Toast toast = Toast.makeText(this, "Bike Stores to be implemented", Toast.LENGTH_SHORT);
            toast.show();
        }
        else if(spinner.getSelectedItem().toString().equals("Ring Parking")){
            //fetching file
            File inputWorkbook = new File(String.valueOf(R.raw.installs_2011));
            InputStream is = getResources().openRawResource(R.raw.installs_2011);

            //if(inputWorkbook.exists()){
                try {
                    //conveerting file to input stream
                    //FileInputStream myInput = new FileInputStream(inputWorkbook);
                    //making file system

                    POIFSFileSystem myFileSystem = new POIFSFileSystem(is);
                    //making the file now recognized as an excel workbook
                    HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
                    //getting the first sheet int the workbook
                    HSSFSheet mySheet = myWorkBook.getSheetAt(0);
                    //getting an iterator to select each row
                    Iterator rowIterator = mySheet.rowIterator();

                    while(rowIterator.hasNext()){
                        //fetching row
                        HSSFRow row = (HSSFRow) rowIterator.next();
                        //getting cell
                        Iterator cellIterator = row.cellIterator();
                        //setting values
                        HSSFCell cellAddress = (HSSFCell) cellIterator.next();
                        HSSFCell cellSpaces = (HSSFCell) cellIterator.next();
                        Geocoder geocoder = new Geocoder(this);
                        Address address = (Address) geocoder.getFromLocationName(cellAddress.toString(),5).get(0);
                        if(address != null) {
                            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                            //placing marker
                            mMap.addMarker(new MarkerOptions()
                                    .position(latLng)
                                    .title(cellAddress.toString() + " has " + cellSpaces.toString() + " spaces"));
                        }
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
           // }
        }
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void startLocationUpdates() {
        try {
            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback,
                    null /* Looper */);
        }catch(SecurityException e){

        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        sTime = SystemClock.elapsedRealtime();

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mGeoDataClient = Places.getGeoDataClient(this, null);

        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);


        createLocationRequest();

        mRequestingLocationUpdates = true;
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                //put update functions in here
                for (Location location : locationResult.getLocations()) {
                    LatLng curr = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curr, 15));

                    //speed and distance calculation
                    secondLoc = location;

                    if(firstLoc != null) {
                        //calculate distance traveled in 1 second
                        float distance = firstLoc.distanceTo(secondLoc);
                        System.out.println("distance: " + distance);
                        //set speed
                        TextView speed = (TextView) findViewById(R.id.speed);
                        speed.setText(String.format("%.2f m/s",distance));

                        //update total distance
                        dist += distance;
                    }

                    firstLoc = secondLoc;
                }
            };
        };
//        performing the required permission checks for the app
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
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }

        mMap = googleMap;

        mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    LatLng curr = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curr, 15));
                }
            }
        });
        mMap.setMyLocationEnabled(true);
    }
}
