package com.vogella.android.bikebuddy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import static com.vogella.android.bikebuddy.BikeTableContract.SQL_CREATE_ENTRIES;

/**
 * Created by joshu on 10/20/2017.
 */
//use this class to store information about a bike
public class BikeDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Bike.db";

    public BikeDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    //insert a new bike with 0km in the database
    public long insertNewBike(String bikeName, SQLiteDatabase db){
        ContentValues values = new ContentValues();
        values.put(BikeTableContract.BikeEntry.COLUMN_NAME_NAME, bikeName);
        values.put(BikeTableContract.BikeEntry.COLUMN_NAME_DISTANCE, 0);
        values.put(BikeTableContract.BikeEntry.COLUMN_NAME_LONGEST_DISTANCE, 0);
        values.put(BikeTableContract.BikeEntry.COLUMN_NAME_LONGEST_DURATION, 0);
        long id = db.insert(BikeTableContract.BikeEntry.TABLE_NAME, null, values);
        return id;
    }

    //update an existings bikes distance
    public int updateBikeDistance(String bikeName, int distance, SQLiteDatabase db){
        String strFilter = BikeTableContract.BikeEntry.COLUMN_NAME_NAME+"=?";
        ContentValues values = new ContentValues();
        values.put(BikeTableContract.BikeEntry.COLUMN_NAME_DISTANCE, distance);
        String[] args = new String[]{bikeName};
        return db.update(BikeTableContract.BikeEntry.TABLE_NAME, values, strFilter, args);
    }

    //update an existings bikes duration
    public int updateBikeDuration(String bikeName, long duration, SQLiteDatabase db){
        String strFilter = BikeTableContract.BikeEntry.COLUMN_NAME_NAME+"=?";
        ContentValues values = new ContentValues();
        values.put(BikeTableContract.BikeEntry.COLUMN_NAME_LONGEST_DURATION, duration);
        String[] args = new String[]{bikeName};
        return db.update(BikeTableContract.BikeEntry.TABLE_NAME, values, strFilter, args);
    }

    //returns a bundle of a bike record(id, name, distance)
    public Bundle getBike(String bikeName, SQLiteDatabase db){
        String[] projection = {
                BikeTableContract.BikeEntry._ID,
                BikeTableContract.BikeEntry.COLUMN_NAME_NAME,
                BikeTableContract.BikeEntry.COLUMN_NAME_DISTANCE,
                BikeTableContract.BikeEntry.COLUMN_NAME_LONGEST_DISTANCE,
                BikeTableContract.BikeEntry.COLUMN_NAME_LONGEST_DURATION
        };

        String selection = BikeTableContract.BikeEntry.COLUMN_NAME_NAME + "=?";
        String[] selectionArgs = {bikeName};

        String sortOrder = BikeTableContract.BikeEntry.COLUMN_NAME_NAME + " DESC";

        Cursor cursor = db.query(
                BikeTableContract.BikeEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        Bundle bike = new Bundle();
        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(BikeTableContract.BikeEntry._ID));
            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow(BikeTableContract.BikeEntry.COLUMN_NAME_NAME));
            long distance = cursor.getLong(
                    cursor.getColumnIndexOrThrow(BikeTableContract.BikeEntry.COLUMN_NAME_DISTANCE));
            long longestDistance = cursor.getLong(
                    cursor.getColumnIndexOrThrow(BikeTableContract.BikeEntry.COLUMN_NAME_LONGEST_DISTANCE));
            long longestDuration = cursor.getLong(
                    cursor.getColumnIndexOrThrow(BikeTableContract.BikeEntry.COLUMN_NAME_LONGEST_DURATION));
            bike.putLong("id", itemId);
            bike.putString("bikeName", name);
            bike.putLong("distance", distance);
            bike.putLong("longestDistance", longestDistance);
            bike.putLong("longestDuration", longestDuration);

        }
        cursor.close();

        return bike;

    }
    public  List<Bundle> getAllBikes( SQLiteDatabase db) {
        String[] projection = {
                BikeTableContract.BikeEntry._ID,
                BikeTableContract.BikeEntry.COLUMN_NAME_NAME,
                BikeTableContract.BikeEntry.COLUMN_NAME_DISTANCE,
                BikeTableContract.BikeEntry.COLUMN_NAME_LONGEST_DISTANCE,
                BikeTableContract.BikeEntry.COLUMN_NAME_LONGEST_DURATION
        };

        String sortOrder = BikeTableContract.BikeEntry.COLUMN_NAME_NAME + " DESC";

        Cursor cursor = db.query(
                BikeTableContract.BikeEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
        List<Bundle> bikes = new ArrayList<Bundle>();
        Bundle bike;
        while(cursor.moveToNext()) {
            bike = new Bundle();
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(BikeTableContract.BikeEntry._ID));
            String name = cursor.getString(
                    cursor.getColumnIndexOrThrow(BikeTableContract.BikeEntry.COLUMN_NAME_NAME));
            long distance = cursor.getLong(
                    cursor.getColumnIndexOrThrow(BikeTableContract.BikeEntry.COLUMN_NAME_DISTANCE));
            long longestDistance = cursor.getLong(
                    cursor.getColumnIndexOrThrow(BikeTableContract.BikeEntry.COLUMN_NAME_LONGEST_DISTANCE));
            long longestDuration = cursor.getLong(
                    cursor.getColumnIndexOrThrow(BikeTableContract.BikeEntry.COLUMN_NAME_LONGEST_DURATION));
            bike.putLong("id", itemId);
            bike.putString("bikeName", name);
            bike.putLong("distance", distance);
            bike.putLong("longestDistance", longestDistance);
            bike.putLong("longestDuration", longestDuration);
            bikes.add(bike);
        }
        cursor.close();

        return bikes;
    }
}