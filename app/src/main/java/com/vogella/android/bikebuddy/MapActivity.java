package com.vogella.android.bikebuddy;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MapActivity extends AppCompatActivity {

    long sTime;
    long dist = 0;

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
        selectIntent.putExtras(bike);
        startActivity(selectIntent);
    }

    public void handleSearchButton(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        sTime = SystemClock.elapsedRealtime();

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

        Spinner spinner = (Spinner) findViewById(R.id.searchType);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.searches, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
    }
}
