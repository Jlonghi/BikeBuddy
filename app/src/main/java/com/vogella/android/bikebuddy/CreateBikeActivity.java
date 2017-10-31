package com.vogella.android.bikebuddy;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CreateBikeActivity extends AppCompatActivity {

    public void handleCreateBikeButton(){
        TextView bikeNameView = (TextView) findViewById(R.id.bikeName);
        String bikeName = bikeNameView.getText().toString();

        BikeDbHelper mDbHelper = new BikeDbHelper(getApplicationContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        mDbHelper.insertNewBike(bikeName, db);

        db.close();

        // TODO: 10/23/2017 add send to start trip activity

        SQLiteDatabase db2 = mDbHelper.getReadableDatabase();
        Intent selectIntent = new Intent(this, OptionActivity.class);
        selectIntent.putExtras(mDbHelper.getBike(bikeName,db2));
        startActivity(selectIntent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bike);

        Button createBike = (Button) findViewById(R.id.createBikeButton);
        createBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCreateBikeButton();
            }
        });
    }

}
