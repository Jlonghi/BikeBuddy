package com.vogella.android.bikebuddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class OptionActivity extends AppCompatActivity {

    public void handleStartRideButton(){
        Intent selectIntent = new Intent(this, MapActivity.class);
        selectIntent.putExtras(getIntent().getExtras());
        startActivity(selectIntent);
    }

   /* public void handleStatsButton(){
        Intent selectIntent = new Intent(this, StatsActivity.class);
        selectIntent.putExtras(getIntent().getExtras());
        startActivity(selectIntent);
    }*/

    public void handleChangeRideButton(){
        Intent selectIntent = new Intent(this, MainActivity.class);
        startActivity(selectIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        Bundle bike = getIntent().getExtras();

        System.out.println("Bike: " + bike.get("longestDuration"));

        TextView name = (TextView)findViewById(R.id.bName);
        name.setText("Bike Name: " + bike.get("bikeName"));

        TextView dist = (TextView)findViewById(R.id.distance);
        dist.setText("Total Distance Traveled: " + bike.get("distance") + " km");

        TextView longDist = (TextView)findViewById(R.id.LongDistance);
        longDist.setText("Farthest Individual Ride: " + bike.get("longestDistance") + " km");

        TextView longDur = (TextView)findViewById(R.id.longDur);
        if(bike.getLong("longestDuration") > 60){
            long min = bike.getLong("longestDuration") / 60;
            long sec = bike.getLong("longestDuration") % 60;
            longDur.setText("Longest Individual Ride: " + min + " Minutes and " + sec + " Seconds");
        }
        else {
            longDur.setText("Longest Individual Ride: " + bike.get("longestDuration") + " Seconds");
        }

        Button startRide = (Button) findViewById(R.id.StartButton);
        startRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleStartRideButton();
            }
        });

       /* Button stats = (Button) findViewById(R.id.StatButton);
        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleStatsButton();
            }
        });*/

        Button ChangeRide = (Button) findViewById(R.id.ChangeButton);
        ChangeRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleChangeRideButton();
            }
        });
    }
}
