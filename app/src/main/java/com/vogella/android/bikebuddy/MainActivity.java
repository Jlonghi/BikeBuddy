package com.vogella.android.bikebuddy;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* example of how to use the database class to insert a bike,
         update a bikes distance and get the information back from the database

        BikeDbHelper mDbHelper = new BikeDbHelper(getApplicationContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        long newRowId = mDbHelper.insertNewBike("test", db);
        int updateValue = mDbHelper.updateBikeDistance("test", 100, db);
        Log.d("dbtest", "value=" + newRowId + " updateval=" + updateValue);

        Bundle bike = mDbHelper.getBike("test", db);

        bike.getLong("id");
        */
    }
}
