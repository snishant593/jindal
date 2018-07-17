package com.aequmindia.mdm;




import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import Pojo.PendingModel;


/**
 * Created by Nishant on 16-01-18.
 */

public class DBHelper extends SQLiteOpenHelper {


    private static String DB_PATH = "/data/data/" + loginactivity.class.getPackage().getName() + "/databases/";
    private static final String DATABASE_NAME = "Db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase myDataBase;
    private final Context mycontext;
    private static final boolean D = true;
    DBHelper dbHelper;

    private ContentValues cValues;

    @Override
    public void onCreate(SQLiteDatabase db) {



    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        if (newversion == oldversion + 1) {
        }
        onCreate(db);
    }


    private static final String TAG = "MyActivity";

    @SuppressLint("NewApi")
    public DBHelper(Context context) {

        super(context, DB_PATH + DATABASE_NAME, null, DATABASE_VERSION);
        this.mycontext = context;
    }


    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();


        if (dbExist) {
            //do nothing - database already exist
        } else {

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();

            try {

                copyDataBase();

            } catch (IOException e) {

                throw new Error("Error copying database");

            }
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    public boolean checkDataBase() {

        SQLiteDatabase checkDB = null;
        Cursor res = null;

        try {
            String myPath = DB_PATH + DATABASE_NAME;
            java.io.File f = new java.io.File(myPath);
            f.getParentFile().mkdirs();
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
            // res = checkDB.rawQuery("select * from retail_store ", null);

        } catch (SQLiteException e) {
            e.printStackTrace();

            //database does't exist yet.

        }
        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = mycontext.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DATABASE_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = DB_PATH + DATABASE_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);


    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onConfigure(SQLiteDatabase myDataBase) {

    }



    public ArrayList<PendingModel> getalldataEmployee() {
        ArrayList<PendingModel> Employeelist= new ArrayList<PendingModel>();

        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from Employee_Team1", null);
            if (cursor.moveToFirst()) {
                do {
                    PendingModel pendingModel=new PendingModel();
                    //                    vendornamelist1.add(cursor.getString(cursor.getColumnIndex("Address")));
                    pendingModel.setRadius(cursor.getFloat(cursor.getColumnIndex("Radius")));
//                    vendornamelist1.add(cursor.getString(cursor.getColumnIndex("Status")));
                    pendingModel.setLatitude(cursor.getString(cursor.getColumnIndex("Latitude")));
                    pendingModel.setLongitude(cursor.getString(cursor.getColumnIndex("Longitude")));
                    pendingModel.setLocationName(cursor.getString(cursor.getColumnIndex("Location_Name")));
                    Employeelist.add(pendingModel);
                } while (cursor.moveToNext());
            }

        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }

        return Employeelist;
    }




    public void insertEmpMobileOnOffData(String Emp_Name,String MobileOff,String Status){
        try {
            Log.e("#########","We Are Inside DataBase Class");
            SQLiteDatabase db=this.getWritableDatabase();
            ContentValues cv=new ContentValues();


            Log.e("########","Emp_Name in database:"+Emp_Name);
            Log.e("########","Internet_ON_OFF in database:"+MobileOff);
            Log.e("########","Datein database:"+Status);



            cv.put("Emp_Name",Emp_Name);
            cv.put("Internet_ON_OFF",MobileOff);
            cv.put("Date",Status);




            long result = db.insert("Internet_Service_Off", null, cv);
            Log.e("Message", "############## data inserted and result is " + result);
            //db.close();
        } catch (Exception e) {
            Log.e("Message","##############:"+e);
        }
        //db.close();
    }



    public ArrayList<String> getEmployeeInternetOffTime(){
        ArrayList<String> arraylist = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("select * from Internet_Service_Off",null);
        if (cur.moveToFirst()) {
            do {

                arraylist.add(cur.getString(cur.getColumnIndex("Emp_Name")));
                arraylist.add(cur.getString(cur.getColumnIndex("Internet_ON_OFF")));
                arraylist.add(cur.getString(cur.getColumnIndex("Date")));



                arraylist.add(";");


            } while (cur.moveToNext());
        }

        return arraylist;

    }




}

