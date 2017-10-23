package com.vogella.android.bikebuddy;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class selectBike extends AppCompatActivity {
    List<Bundle> bikes;

    // TODO: 10/23/2017 select bike click
    public void selectBike(String bikeName){
        //start activity here and send in bike name
    }
    // TODO: 10/23/2017 create bike click
    public void createNewBike(){

    }

    //load the bikes from the database into the view
    public void getBikes(){
        BikeDbHelper mDbHelper = new BikeDbHelper(getApplicationContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        bikes = mDbHelper.getAllBikes(db);
        db.close();

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.bikeLayout);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        if(!bikes.isEmpty()) {
            for (Bundle bike : bikes) {
                Button newButton = new Button(this);
                newButton.setText(bike.get("bikeName") + ": " + bike.getLong("distance"));
                linearLayout.addView(newButton, layoutParams);

                //custom listner for passing params in
                newButton.setOnClickListener(new BikeClickListner(bike.getString("bikeName")));
            }
        }
        else{
            TextView newText = new TextView(this);
            newText.setText("You have not created any bikes yet!");
            newText.setGravity(Gravity.CENTER_HORIZONTAL);

            linearLayout.setGravity(Gravity.CENTER_VERTICAL);
            linearLayout.addView(newText, layoutParams);

            Button newButton = new Button(this);
            newButton.setText("Create a Bike");
            newButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createNewBike();
                }
            });

            linearLayout.addView(newButton, layoutParams);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bike);
        getBikes();
    }
    //custom listner to pass parameters in
    public class BikeClickListner implements View.OnClickListener{
        String bikeName;
        public BikeClickListner(String bikeName){
            this.bikeName = bikeName;
        }

        @Override
        public void onClick(View v){
            selectBike(bikeName);
        }
    }
}
