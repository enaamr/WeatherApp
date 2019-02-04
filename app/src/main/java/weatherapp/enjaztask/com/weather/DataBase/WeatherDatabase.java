package weatherapp.enjaztask.com.weather.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import weatherapp.enjaztask.com.weather.Helper.Constant;
import weatherapp.enjaztask.com.weather.Model.WeatherDataModel;


public class WeatherDatabase extends SQLiteOpenHelper {

    private static final String TAG = WeatherDatabase.class.getSimpleName();

    public WeatherDatabase(Context context) {
        super(context, Constant.DB_NAME, null, Constant.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(Constant.CREATE_TABLE_QUERY);
        } catch (SQLException ex) {
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(Constant.DROP_QUERY);
        this.onCreate(db);
        db.close();

    }


    public void addDataInDB(String main, String temp, String hum,
                            String speed, String country, String sunrise, String sunset, String name, String date) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constant.MAIN, main);
        values.put(Constant.TEMP, temp);
        values.put(Constant.HUMIDITY, hum);
        values.put(Constant.SPEED, speed);
        values.put(Constant.COUNTRY, country);
        values.put(Constant.SUNRISE, sunrise);
        values.put(Constant.SUNSET, sunset);
        values.put(Constant.NAME, name);
        values.put(Constant.DATE, date);

        try {

            db.insert(Constant.TABLE_NAME, null, values);
        } catch (Exception e) {
            Log.d(TAG, e.getMessage());
        }
        db.close();

    }

    public List<WeatherDataModel> getAllData() {
        List<WeatherDataModel> contactList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(Constant.GET_PRODUCTS_QUERY, null);


        if (cursor.moveToFirst()) {
            do {
                WeatherDataModel contact = new WeatherDataModel();
                contact.setMain(cursor.getString(cursor.getColumnIndex(Constant.MAIN)));
                contact.setTemp(cursor.getString(cursor.getColumnIndex(Constant.TEMP)));
                contact.setHumidity(cursor.getString(cursor.getColumnIndex(Constant.HUMIDITY)));
                contact.setSpeed(cursor.getString(cursor.getColumnIndex(Constant.SPEED)));
                contact.setCountry(cursor.getString(cursor.getColumnIndex(Constant.COUNTRY)));
                contact.setSunrise(cursor.getString(cursor.getColumnIndex(Constant.SUNRISE)));
                contact.setSunset(cursor.getString(cursor.getColumnIndex(Constant.SUNSET)));
                contact.setName(cursor.getString(cursor.getColumnIndex(Constant.NAME)));
                contact.setDate(cursor.getString(cursor.getColumnIndex(Constant.DATE)));

                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return contactList;
    }

    public void updateValues(String x, String main, String temp, String hum,
                             String speed, String country, String sunrise, String sunset, String name, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Constant.MAIN, main);
        values.put(Constant.TEMP, temp);
        values.put(Constant.HUMIDITY, hum);
        values.put(Constant.SPEED, speed);
        values.put(Constant.COUNTRY, country);
        values.put(Constant.SUNRISE, sunrise);
        values.put(Constant.SUNSET, sunset);
        values.put(Constant.NAME, name);
        values.put(Constant.DATE, date);


        // updating row
        db.update(Constant.TABLE_NAME, values, Constant.KEY_ID + " = ?",
                new String[]{String.valueOf(x)});

        db.close();

    }

    public boolean TableNotEmpty() {
        SQLiteDatabase db = getWritableDatabase();

        Cursor mCursor = db.rawQuery("SELECT * FROM " + Constant.TABLE_NAME, null);
        Boolean rowExists;

        if (mCursor.moveToFirst()) {
            // DO SOMETHING WITH CURSOR
            mCursor.close();
            rowExists = true;
            db.close();

        } else {
            // EMPTY
            mCursor.close();
            rowExists = false;
            db.close();
        }
        db.close();
        return rowExists;
    }
}
