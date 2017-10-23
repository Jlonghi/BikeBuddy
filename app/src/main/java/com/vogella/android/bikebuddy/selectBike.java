package com.vogella.android.bikebuddy;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class selectBike extends AppCompatActivity {
    List<Bundle> bikes;

    public void getBikes(){
        BikeDbHelper mDbHelper = new BikeDbHelper(getApplicationContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        bikes = mDbHelper.getAllBikes(db);
        db.close();

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.bikeLayout);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //linearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        for(Bundle bike : bikes){
//            TextView newText = new TextView(this);
//            newText.setGravity(Gravity.CENTER_HORIZONTAL);
//            newText.setText(bike.get("bikeName") + ": " + bike.getLong("distance"));
            Button newButton = new Button(this);
            newButton.setText(bike.get("bikeName") + ": " + bike.getLong("distance"));
            linearLayout.addView(newButton, layoutParams);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bike);
        getBikes();
    }

}
