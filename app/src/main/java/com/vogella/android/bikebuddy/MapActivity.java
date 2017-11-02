package com.vogella.android.bikebuddy;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MapActivity extends AppCompatActivity {

    long sTime;

    public void handleEndRideButton(){
        long eTime = SystemClock.elapsedRealtime();
        long dTime = eTime - sTime;
        long elapsed = dTime / 1000;
        Bundle bike = getIntent().getExtras();
        if(elapsed > bike.getLong("longestDuration")){
            BikeDbHelper mDbHelper = new BikeDbHelper(getApplicationContext());
            SQLiteDatabase db = mDbHelper.getWritableDatabase();
            mDbHelper.updateBikeDuration(bike.getString("bikeName"), elapsed, db);
            db.close();
            Bundle bike2 = new Bundle();
            bike2.putLong("id", bike.getLong("id"));
            bike2.putString("bikeName", bike.getString("bikeName"));
            bike2.putLong("distance", bike.getLong("distance"));
            bike2.putLong("longestDistance", bike.getLong("longestDistance"));
            bike2.putLong("longestDuration", elapsed);

            Intent selectIntent = new Intent(this, OptionActivity.class);
            selectIntent.putExtras(bike2);
            startActivity(selectIntent);
        }

        Intent selectIntent = new Intent(this, OptionActivity.class);
        selectIntent.putExtras(bike);
        startActivity(selectIntent);
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
    }
}
