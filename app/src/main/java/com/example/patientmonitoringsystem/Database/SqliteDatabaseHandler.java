package com.example.patientmonitoringsystem.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.patientmonitoringsystem.Models.Patient;
import java.util.ArrayList;

public class SqliteDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PatientDb";
    private static final String TABLE_NAME_PATIENT = "Patient";

    private static final String KEY_TIME = "time";
    private static final String KEY_DATE = "date";
    private static final String KEY_TEMP_PROB = "temprob";
    private static final String KEY_BPM = "bpm";
    private static final String KEY_HUMIDITY = "humidity";
    private static final String KEY_TEMP = "tempt";

    public SqliteDatabaseHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATION_TABLE_PATIENT = "CREATE TABLE Patient ( time TEXT PRIMARY KEY, "+"date TEXT, "+"temprob TEXT, "+"bpm TEXT, "+"humidity TEXT, "+"tempt TEXT)";
        try{
            db.execSQL(CREATION_TABLE_PATIENT);

        }catch (SQLException e)
        {
            Log.e("ErrorCreation", e.toString());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_PATIENT);
        this.onCreate(db);
    }

    public void addPatient(Patient patient) {
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues value = new ContentValues();
        try{
            value.put(KEY_TIME, patient.getTime());
            value.put(KEY_DATE, patient.getDate());
            value.put(KEY_TEMP_PROB, patient.getTemp_prob());
            value.put(KEY_BPM, patient.getBpm());
            value.put(KEY_HUMIDITY, patient.getHumidity());
            value.put(KEY_TEMP, patient.getTemperature());
            db.insert(TABLE_NAME_PATIENT, null, value);
            Log.v("Insert", "sucess"+patient.getTime());
            db.close();
        }
        catch (SQLException e)
        {
            Log.v("InsertError", e.toString());
        }
    }

    public ArrayList<Patient> getAllData() {
        ArrayList<Patient> arrayList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME_PATIENT;
        SQLiteDatabase db = this .getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Patient contact = new Patient();
                contact.setTime(cursor.getString(0));
                contact.setDate(cursor.getString(1));
                contact.setTemp_prob(cursor.getString(2));
                contact.setBpm(cursor.getString(3));
                contact.setHumidity(cursor.getString(4));
                contact.setTemperature(cursor.getString(5));
                arrayList.add(contact);
            }while (cursor.moveToNext());
            cursor.close();
        }
        return arrayList;
    }
}
