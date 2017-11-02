package com.vogella.android.bikebuddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class StatsActivity extends AppCompatActivity {

    public void handleBackButton(){
        Intent selectIntent = new Intent(this, OptionActivity.class);
        selectIntent.putExtras(getIntent().getExtras());
        startActivity(selectIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Bundle bike = getIntent().getExtras();
        TextView name = (TextView)findViewById(R.id.bName);
        name.setText("Bike Name: " + bike.get("bikeName"));

        TextView dist = (TextView)findViewById(R.id.distance);
        dist.setText("Total Distance Traveled: " + bike.get("distance"));

        TextView longDist = (TextView)findViewById(R.id.LongDistance);
        longDist.setText("Farthest Individual Ride: " + bike.get("longestDistance"));

        TextView longDur = (TextView)findViewById(R.id.longDur);
        longDur.setText("Longest Individual Ride: " + bike.get("longestDuration"));

        Button back = (Button) findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBackButton();
            }
        });
    }
}
