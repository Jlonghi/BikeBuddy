package com.vogella.android.bikebuddy;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by joshu on 10/19/2017.
 */

public final class BikeTableContract {
    private BikeTableContract(){}

    public static class BikeEntry implements BaseColumns{
        public static final String TABLE_NAME = "bikes";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DISTANCE = "distance";
    }

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + BikeEntry.TABLE_NAME + " (" +
                    BikeEntry._ID + " INTEGER PRIMARY KEY," +
                    BikeEntry.COLUMN_NAME_NAME + " TEXT UNIQUE," +
                    BikeEntry.COLUMN_NAME_DISTANCE + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + BikeEntry.TABLE_NAME;

}
