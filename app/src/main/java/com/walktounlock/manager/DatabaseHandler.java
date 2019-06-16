package com.walktounlock.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.walktounlock.model.App;
import com.walktounlock.model.Distance;
import com.walktounlock.model.User;

import java.util.ArrayList;

/**
 * Created by M Umer Saleem on 9/6/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "appInfo.db";

    // Apps table name
    private static final String TABLE_APP = "app";

    //Location table name
    private static final String TABLE_LOCATION="location";

    //Location table name
    private static final String TABLE_USER="user";

    //Location table clumn names
    private static final String KEY_LOC_ID = "id";
    private static final String KEY_DATE = "kdate";
    private static final String KEY_TOTAL_DISTANCE = "tdistance";


    //User table column names
    private static final String KEY_USER_ID="id";
    private static final String KEY_USER_NAME="name";
    private static final String KEY_USER_EMAIL="email";
    private static final String KEY_USER_PASS="pass";
    private static final String KEY_USER_PIN="pin";


    // Apps Table Columns names
    private static final String KEY_APP_ID = "id";
    private static final String KEY_PACKAGE_NAME = "name";
    private static final String KEY_STATUS = "status";
    private static final String KEY_LIMIT = "dlimit";
    private static final String KEY_CHECK = "acheck";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_APPS_TABLE = "CREATE TABLE " + TABLE_APP + "("
                + KEY_APP_ID + " INTEGER PRIMARY KEY,"
                + KEY_PACKAGE_NAME + " TEXT,"
                + KEY_STATUS + " TEXT,"
                + KEY_LIMIT + " TEXT,"
                + KEY_CHECK + " TEXT" + ")";
        db.execSQL(CREATE_APPS_TABLE);

        String CREATE_LOC_TABLE = "CREATE TABLE " + TABLE_LOCATION + "("
                + KEY_LOC_ID + " INTEGER PRIMARY KEY,"
                + KEY_DATE + " TEXT UNIQUE,"
                + KEY_TOTAL_DISTANCE + " TEXT" + ")";
        db.execSQL(CREATE_LOC_TABLE);


        String CREATE_USER_TABLE = "CREATE TABLE " + TABLE_USER + "("
                + KEY_USER_ID + " INTEGER PRIMARY KEY,"
                + KEY_USER_NAME + " TEXT,"
                + KEY_USER_EMAIL + " TEXT,"
                + KEY_USER_PASS + " TEXT,"
                + KEY_USER_PIN + " TEXT" + ")";
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }
    public boolean addApp(App app) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PACKAGE_NAME, app.getName());
        values.put(KEY_STATUS, app.getStatus());
        values.put(KEY_LIMIT, app.getLimit());
        values.put(KEY_CHECK, app.getCheck());

        // Inserting Row
        long id=db.insert(TABLE_APP, null, values);
        db.close(); // Closing database connection
        if (id>0)
            return true;
        else
            return false;
    }

    public boolean addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, user.getName());
        values.put(KEY_USER_EMAIL, user.getEmail());
        values.put(KEY_USER_PASS, user.getPassword());
        values.put(KEY_USER_PIN, user.getPin());


        // Inserting Row
        long id=db.insert(TABLE_USER, null, values);
        db.close(); // Closing database connection
        if (id>0)
            return true;
        else
            return false;
    }

    public boolean addDistance(Distance distance) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, distance.getDate());
        values.put(KEY_TOTAL_DISTANCE, distance.getTotalDistance());


        // Inserting Row
        long id=db.insert(TABLE_LOCATION, null, values);
        db.close(); // Closing database connection
        if (id>0)
            return true;
        else
            return false;
    }

    public ArrayList<App> getAllApps() {
        ArrayList<App> appList = new ArrayList<App>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_APP;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                App app = new App();
                app.setId(Integer.parseInt(cursor.getString(0)));
                app.setName(cursor.getString(1));
                app.setStatus(cursor.getString(2));
                app.setLimit(cursor.getString(3));
                app.setCheck(cursor.getString(4));

                // Adding contact to list
                appList.add(app);
            } while (cursor.moveToNext());
        }

        // return contact list
        return appList;
    }
    public ArrayList<Distance> getAllDistance() {
        ArrayList<Distance> distanceArrayList = new ArrayList<Distance>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LOCATION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Distance distance = new Distance();
                distance.setId(cursor.getString(0));
                distance.setDate(cursor.getString(1));
                distance.setTotalDistance(cursor.getString(2));

                // Adding contact to list
                distanceArrayList.add(distance);
            } while (cursor.moveToNext());
        }

        // return contact list
        return distanceArrayList;
    }

    public App getApp(String name) {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_APP +" WHERE "+ KEY_PACKAGE_NAME +"='"+name+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        cursor.moveToFirst();
        App app = new App();
        app.setName("");
        app.setStatus("false");
        app.setLimit("0");
        app.setCheck("0");
        if (cursor.moveToFirst()) {
            app.setId(Integer.parseInt(cursor.getString(0)));
            app.setName(cursor.getString(cursor.getColumnIndex(KEY_PACKAGE_NAME)));
            app.setStatus(cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
            app.setLimit(cursor.getString(cursor.getColumnIndex(KEY_LIMIT)));
            app.setCheck(cursor.getString(cursor.getColumnIndex(KEY_CHECK)));
        }
        return app;
    }

    public User getUser(String name) {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USER +" WHERE "+ KEY_USER_NAME +"='"+name+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        cursor.moveToFirst();
        User user=new User();
        if (cursor.moveToFirst()) {
            user.setId(Integer.parseInt(cursor.getString(0)));
            user.setName(cursor.getString(cursor.getColumnIndex(KEY_USER_NAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndex(KEY_USER_EMAIL)));
            user.setPassword(cursor.getString(cursor.getColumnIndex(KEY_USER_PASS)));
            user.setPin(cursor.getString(cursor.getColumnIndex(KEY_USER_PIN)));
        }
        return user;
    }

    public Distance getDistance(String date) {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_LOCATION +" WHERE "+ KEY_DATE +"='"+date+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        cursor.moveToFirst();
        Distance distance = new Distance();
        if (cursor.moveToFirst()) {
            distance.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            distance.setTotalDistance(cursor.getString(cursor.getColumnIndex(KEY_TOTAL_DISTANCE)));
        }
        return distance;
    }

    public int updateApp(App app) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PACKAGE_NAME, app.getName());
        values.put(KEY_STATUS, app.getStatus());
        values.put(KEY_LIMIT,app.getLimit());
        values.put(KEY_CHECK, app.getCheck());



        // updating row
        return db.update(TABLE_APP, values, KEY_PACKAGE_NAME + " = ?",
                new String[] { String.valueOf(app.getName()) });
    }

    public int updateAppCheck(App app) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PACKAGE_NAME, app.getName());
        values.put(KEY_CHECK, app.getCheck());



        // updating row
        return db.update(TABLE_APP, values, KEY_PACKAGE_NAME + " = ?",
                new String[] { String.valueOf(app.getName()) });
    }

    public int updateDistance(Distance distance) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TOTAL_DISTANCE, distance.getTotalDistance());
        values.put(KEY_DATE, distance.getDate());
        // updating row
        return db.update(TABLE_LOCATION, values, KEY_DATE + " = ?",
                new String[] { String.valueOf(distance.getDate()) });
    }




    public void deleteUser(App app) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_APP, KEY_APP_ID + " = ?",
                new String[] { String.valueOf(app.getId()) });
        db.close();
    }
    public boolean checkApp(String name, String status) {

        // array of columns to fetch
        String[] columns = {
                KEY_APP_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = KEY_PACKAGE_NAME + " = ?" + " AND " + KEY_STATUS + " = ?";

        // selection arguments
        String[] selectionArgs = {name, status};

        Cursor cursor = db.query(TABLE_APP, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public boolean isCheck(String name,String check) {

        // array of columns to fetch
        String[] columns = {
                KEY_APP_ID
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = KEY_PACKAGE_NAME + " = ?" + " AND " + KEY_CHECK + " = ?";

        // selection arguments
        String[] selectionArgs = {name, check};

        Cursor cursor = db.query(TABLE_APP, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

}
