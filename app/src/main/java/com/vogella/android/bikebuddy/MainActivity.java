package com.vogella.android.bikebuddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    public void handleSelectBikeClick(){
        Intent selectIntent = new Intent(this, SelectBikeActivity.class);
        startActivity(selectIntent);
    }
    public void handleCreateBikeClick(){
        Intent selectIntent = new Intent(this, CreateBikeActivity.class);
        startActivity(selectIntent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button selectBike = (Button) findViewById(R.id.selectBikeButton);
        selectBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSelectBikeClick();
            }
        });

        Button createBike = (Button) findViewById(R.id.createBikeButton);
        createBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCreateBikeClick();
            }
        });
        /* example of how to use the database class to insert a bike,
         update a bikes distance and get the information back from the database

        BikeDbHelper mDbHelper = new BikeDbHelper(getApplicationContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long newRowId = mDbHelper.insertNewBike("tes2t", db);
        int updateValue = mDbHelper.updateBikeDistance("test2", 100, db);
        Log.d("dbtest", "value=" + newRowId + " updateval=" + updateValue);
        */

    }
}
