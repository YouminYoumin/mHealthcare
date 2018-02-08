package org.androidtown.mhealthcare;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DBHelper extends SQLiteOpenHelper{
    public static SQLiteDatabase fulldb;
    public int count;
    private String lat = "";
    private String lng = "";

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db){ // Create database and table
        //Creating UserList table
        db.execSQL("CREATE TABLE USERLIST(_id INTEGER PRIMARY KEY , user_id TEXT, password TEXT);");
        //Creating UserInfo table
        db.execSQL("CREATE TABLE USERINFO(user_id TEXT PRIMARY KEY , name TEXT, age INTEGER, auth TEXT, sex TEXT, e_mail TEXT, local TEXT);");
        //Creating PatientInfo table
        db.execSQL("CREATE TABLE PATIENTINFO(patientnum INTEGER PRIMARY KEY, name TEXT, age INTEGER, sex TEXT,  disease TEXT, body_temperature INTEGER, history TEXT, citizenNum TEXT);");
        //Creating Relationship Context table
        db.execSQL("CREATE TABLE RELATIONSHIP(patientnum INTEGER PRIMARY KEY, doctor TEXT, nurse TEXT);");
        //Creating Time Context table
        db.execSQL("CREATE TABLE TIME(auth TEXT PRIMARY KEY, start TEXT, finish TEXT)");
        //Creating Location Context table
        db.execSQL("CREATE TABLE LOCATION(local TEXT PRIMARY KEY, latitude TEXT, longitude TEXT)");
        //Creating Context table
        db.execSQL("CREATE TABLE CONTEXT(auth TEXT, con TEXT)");

        //Insert initial values into UserList table
        db.execSQL("INSERT INTO USERLIST VALUES(1, 'jim45', '1234');");
        db.execSQL("INSERT INTO USERLIST VALUES(2, 'amanda76', '1234');");
        db.execSQL("INSERT INTO USERLIST VALUES(3, 'terry19', '5678');");
        db.execSQL("INSERT INTO USERLIST VALUES(4, 'alice73', '5678')");

        //Insert initial values into UserInfo table
        db.execSQL("INSERT INTO USERINFO VALUES('jim45', 'Jim', 37, 'doctor', 'male', 'james11@gmail.com', 'la trobe');");
        db.execSQL("INSERT INTO USERINFO VALUES('amanda76', 'Amanda', 26, 'nurse', 'female', 'amanda22@gmail.com', 'la trobe');");
        db.execSQL("INSERT INTO USERINFO VALUES('terry19', 'Terry', 41, 'doctor', 'male', 'Terry33@gmail.com', 'la trobe');");
        db.execSQL("INSERT INTO USERINFO VALUES('alice73', 'Alice', 32, 'nurse', 'female', 'Alice44@gmail.com', 'la trobe');");


        //Insert initial values into PatientInfo table
        db.execSQL("INSERT INTO PATIENTINFO VALUES(1, 'James', 18, 'male', 'breast cancer', 36, 'none', '11111111');");
        db.execSQL("INSERT INTO PATIENTINFO VALUES(2, 'Jessy', 27, 'female', 'Fracture', 36, 'eye disease', '22222222');");
        db.execSQL("INSERT INTO PATIENTINFO VALUES(3, 'Micheal', 25, 'male', 'Liver disease', 37, 'none', '33333333');");
        db.execSQL("INSERT INTO PATIENTINFO VALUES(4, 'David', 35, 'male', 'Influenza', 38, 'enteritis', '44444444');");
        db.execSQL("INSERT INTO PATIENTINFO VALUES(5, 'Linda', 22, 'female', 'Influenza', 38, 'capillariasis', '55555555');");
        db.execSQL("INSERT INTO PATIENTINFO VALUES(6, 'Charles', 28, 'male', 'Brain tumor', 36, 'diabetes', '66666666');");
        db.execSQL("INSERT INTO PATIENTINFO VALUES(7, 'Lisa', 32, 'female', 'AIDS', 37, 'anisakiasis', '77777777');");
        db.execSQL("INSERT INTO PATIENTINFO VALUES(8, 'Nicole', 51, 'female', 'Fracture', 36, 'none', '88888888');");

        //Insert initial values into Relationship Context table
        db.execSQL("INSERT INTO RELATIONSHIP VALUES(1, 'jim45', 'amanda76');");
        db.execSQL("INSERT INTO RELATIONSHIP VALUES(2, 'jim45', 'alice73');");
        db.execSQL("INSERT INTO RELATIONSHIP VALUES(3, 'terry19', 'amanda76');");
        db.execSQL("INSERT INTO RELATIONSHIP VALUES(4, 'terry19', 'alice73');");
        db.execSQL("INSERT INTO RELATIONSHIP VALUES(5, 'jim45', 'amanda76');");
        db.execSQL("INSERT INTO RELATIONSHIP VALUES(6, 'jim45', 'alice73');");
        db.execSQL("INSERT INTO RELATIONSHIP VALUES(7, 'terry19', 'amanda76');");
        db.execSQL("INSERT INTO RELATIONSHIP VALUES(8, 'terry19', 'alice73');");

        //Insert initial values into Time Context table
        db.execSQL("INSERT INTO TIME VALUES('doctor', '9-00', '17-00')");
        db.execSQL("INSERT INTO TIME VALUES('nurse', '10-00', '16-00')");

        //Insert initial values into Location Context table
        db.execSQL("INSERT INTO LOCATION VALUES('la trobe', '-37.72', '145.04')");

        //Insert initial values into Context table
        db.execSQL("INSERT INTO CONTEXT VALUES('doctor', 'relationship')");
        db.execSQL("INSERT INTO CONTEXT VALUES('doctor', 'location')");
        db.execSQL("INSERT INTO CONTEXT VALUES('nurse', 'relationship')");
        db.execSQL("INSERT INTO CONTEXT VALUES('nurse', 'time')");
        db.execSQL("INSERT INTO CONTEXT VALUES('nurse', 'location')");

        fulldb = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    public void checkSize(){ // Check size of PATIENTINFO table
        fulldb = getReadableDatabase();
        String sql = "SELECT * FROM PATIENTINFO";
        Cursor c = fulldb.rawQuery(sql, null);
        count = c.getCount();
    }

    public boolean getLogin(String user_id, String password){ // Check whether user can log on
        fulldb = getReadableDatabase();
        String sql = "SELECT * FROM USERLIST WHERE user_id = '" + user_id + "'"; // Get password from USERLIST
        Log.d("whatID", user_id);
        String result = "";

        Cursor c = fulldb.rawQuery(sql, null);
        c.moveToFirst();
        Log.d("Query Result", String.valueOf(c.getInt(0)));

        if(c.getString(2).equals(password)){ // If password is same with parameter
            fulldb.close();
            return true;
        }
        else{ // If password is differennt with parameter
            fulldb.close();
            return false;
        }
    }

    public String getName(String user_id){ // Return name of user
        fulldb = getReadableDatabase();
        String sql = "SELECT * FROM USERINFO WHERE user_id = '" + user_id + "'";

        Cursor c = fulldb.rawQuery(sql, null);
        c.moveToFirst();
        return c.getString(c.getColumnIndex("name")); // Return string in column 'name'
    }

    public String getAuth(String user_id){ // Return authority of user
        fulldb = getReadableDatabase();
        String sql = "SELECT * FROM USERINFO WHERE user_id = '" + user_id + "'";

        Cursor c = fulldb.rawQuery(sql, null);
        c.moveToFirst();
        return c.getString(c.getColumnIndex("auth")); // Return string in column 'auth'
    }

    public ArrayList<String> getList(int index){ // Return list of patient
        fulldb = getReadableDatabase();
        int end = 0;
        ArrayList<String> result = new ArrayList<String>();
        String sql = "";
        Cursor c;

        end = index + 6; // Set range of list
        if(end >= count) // If it is last page, end is same with count
            end = count;

        sql = "SELECT name FROM PATIENTINFO WHERE patientnum BETWEEN '" + index + "' AND '" + end + "'"; // Get patient list that index is between 'index' and 'end'
        c = fulldb.rawQuery(sql, null);
        c.moveToFirst();
        for(int i = index; i <= end; i++){ // Set patient list in ArrayList
            String temp = i + "-" + c.getString(c.getColumnIndex("name"));
            Log.d("temp", temp);
            result.add(temp);
            c.moveToNext();
        }
        return result;
    }

    public ArrayList<String> getSearch(String input){ // Return search result by patient number or name
        fulldb = getReadableDatabase();
        ArrayList<String> result = new ArrayList<String>();
        String sql = "";
        Cursor c;

        try{ // If input is name, change first letter to uppercase
            Log.d("number", Integer.parseInt(input) + "");
        } catch (NumberFormatException e){
            String trans = input.substring(0, 1);
            trans = trans.toUpperCase();
            input = trans + input.substring(1);
        }

        sql = "SELECT name, patientnum FROM PATIENTINFO WHERE patientnum = '" + input + "' OR name = '" + input + "'"; // Search patient with patient number or name
        c = fulldb.rawQuery(sql, null);
        Log.d("count", String.valueOf(c.getCount()));
        c.moveToFirst();
        for(int i = 1; i <= c.getCount(); i++){ // Set patient list with search result
            String temp =  c.getInt(c.getColumnIndex("patientnum"))+ "-" + c.getString(c.getColumnIndex("name"));
            Log.d("temp", temp);
            result.add(temp);
            if(c.getCount() > i + 1){
                c.moveToNext();
            }
        }
        return result;
    }


    public int checkContext(String user_id, String auth, String patientNum, Context mContext) { // Check context by authority
        fulldb = getReadableDatabase();
        String sql = "SELECT con FROM CONTEXT WHERE auth = '" + auth + "'";
        String context = "";
        Cursor c;

        c = fulldb.rawQuery(sql, null);
        c.moveToFirst();
        for (int i = 0; i < c.getCount(); i++) { // Get context about authority
            context = context + "+" + c.getString(c.getColumnIndex("con"));
            c.moveToNext();
        }

        if (context.contains("relationship")) { // If user has 'relationship' context
            boolean check = false;

            sql = "SELECT * FROM RELATIONSHIP WHERE patientnum = " + patientNum;
            c = fulldb.rawQuery(sql, null);
            c.moveToFirst();
            for (int i = 1; i < c.getColumnCount(); i++) { // Check whether user has relationship with patient
                if (c.getString(i).equals(user_id))
                    check = true;
            }
            if (!check)
                return 1;
        }

        if (context.contains("time")) { // If user has 'time' context
            String[] temp = getTime().split("-");
            String[] start;
            String[] finish;
            sql = "SELECT * FROM TIME WHERE auth = '" + auth + "'";
            c = fulldb.rawQuery(sql, null);
            c.moveToFirst();

            start = c.getString(c.getColumnIndex("start")).split("-"); // Get start time
            finish = c.getString(c.getColumnIndex("finish")).split("-"); // Get finish time

            if (Integer.parseInt(temp[3]) < Integer.parseInt(start[0]) || Integer.parseInt(temp[3]) >= Integer.parseInt(finish[0])) { // If time is out of start and finish, return 2
                return 2;
            }
        }

        if (context.contains("location")) { // If user has 'location' context
            String local = "";
            String local_lat = "";
            String local_lng = "";
            sql = "SELECT * FROM USERINFO WHERE user_id = '" + user_id + "'";
            c = fulldb.rawQuery(sql, null);
            c.moveToFirst();

            local = c.getString(c.getColumnIndex("local"));
            sql = "SELECT * FROM LOCATION WHERE local = '" + local + "'";
            c = fulldb.rawQuery(sql, null);
            c.moveToFirst();

            local_lat = c.getString(c.getColumnIndex("latitude")); // Get latitude of setting location
            local_lng = c.getString(c.getColumnIndex("longitude")); // Get longitude of setting location

            LocationManager manager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);
            GPSListener gpsListener = new GPSListener();
            long minTime = 1000 * 60; // check location every 1 minute
            float minDistance = 0;

            try{ // Set GPS to get current location
                manager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        minTime,
                        minDistance,
                        gpsListener);

                Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(lastLocation != null){ // If it is first time of checking location
                    Double latitude = lastLocation.getLatitude(); // Get latitude of current location
                    Double longitude = lastLocation.getLongitude(); // Get longiutde of current location

                    // Convert to string
                    lat = Double.toString(latitude);
                    lng = Double.toString(longitude);

                    String msg = "Lat : " + latitude + "\nLng : " + longitude;
                    Log.d("GPS11", msg);
                    Log.d("Local", "localLat : " + local_lat + "\nlocalLng : " + local_lng);
                }
            } catch(SecurityException e){ // If GPS couldn't be set by SecurityException
                e.printStackTrace();
            }

            if(!lat.startsWith(local_lat) || !lng.startsWith(local_lng)) // If latitude and longitude aren't same with setting location, return 3
                return 3;
        }

        return 0; // If all context is satisfied, return 0
    }

    public ArrayList<String> getResult(String patientnum, String user_id){ // Return patient information
        fulldb = getReadableDatabase();
        String sql = "SELECT * FROM USERINFO WHERE user_id = '" + user_id + "'";
        ArrayList<String> result = new ArrayList<String>();
        Cursor c = fulldb.rawQuery(sql, null);
        c.moveToFirst();

        // Data will be different by authority
        if(c.getString(c.getColumnIndex("auth")).equals("doctor")){ // If user is a doctor, get all information
            sql = "SELECT * FROM PATIENTINFO WHERE patientnum = '" + patientnum + "'";
        }
        else if(c.getString(c.getColumnIndex("auth")).equals("nurse")){ // If user is a nurse, get 5 information
            sql = "SELECT name, age, sex, disease, body_temperature FROM PATIENTINFO WHERE patientnum = '" + patientnum + "'";
        }

        c = fulldb.rawQuery(sql, null);
        String[] columnName = c.getColumnNames();
        c.moveToFirst();
        for(int i = 0; i < c.getColumnCount(); i++){ // Set patient information to list
            String temp = columnName[i] + " - " + c.getString(c.getColumnIndex(columnName[i]));
            Log.d("temp", temp);
            result.add(temp);
        }
        fulldb.close();

        return result;
    }

    public static String getTime(){ // Return current time
        long mNow;
        Date mDate;
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        Log.d("Time", mFormat.format(mDate));
        return mFormat.format(mDate);
    }

    private class GPSListener implements LocationListener{ // Define GPSListener class for using GPS
        public void onLocationChanged(Location location){
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();

            String msg = "Lat : " + latitude + "\nLng : " + longitude;
            Log.d("GPS22", msg);
        }

        public void onProviderDisabled(String provider){

        }

        public void onProviderEnabled(String provider){

        }

        public void onStatusChanged(String provdier, int status, Bundle extras){

        }
    }
}
