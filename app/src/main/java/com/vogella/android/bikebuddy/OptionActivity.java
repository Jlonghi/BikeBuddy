package com.vogella.android.bikebuddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OptionActivity extends AppCompatActivity {

    public void handleStartRideButton(){
        Intent selectIntent = new Intent(this, MapActivity.class);
        selectIntent.putExtras(getIntent().getExtras());
        startActivity(selectIntent);
    }

    public void handleStatsButton(){
        Intent selectIntent = new Intent(this, StatsActivity.class);
        selectIntent.putExtras(getIntent().getExtras());
        startActivity(selectIntent);
    }

    public void handleChangeRideButton(){
        Intent selectIntent = new Intent(this, MainActivity.class);
        startActivity(selectIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);

        Button startRide = (Button) findViewById(R.id.StartButton);
        startRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleStartRideButton();
            }
        });

        Button stats = (Button) findViewById(R.id.StatButton);
        stats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleStatsButton();
            }
        });

        Button ChangeRide = (Button) findViewById(R.id.ChangeButton);
        ChangeRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleChangeRideButton();
            }
        });
    }
}
