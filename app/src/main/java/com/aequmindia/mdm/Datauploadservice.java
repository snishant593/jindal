package com.aequmindia.mdm;


import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Nishant on 27/12/17.
 */

public class Datauploadservice extends Service {

    DBHelper db;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        db = new DBHelper(getApplicationContext());
        uploadData();
        return START_STICKY;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }




    public void uploadData() {
        class uploadData extends AsyncTask<Void, Void, String> {
            ArrayList<String> get_EmpInternetOffime = new ArrayList<>();




            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                get_EmpInternetOffime  = db.getEmployeeInternetOffTime();


            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
            @Override
            protected String doInBackground(Void... params) {

                HashMap<String,String> uploadEmpInOutData = new HashMap<>();


                StringBuilder sb = new StringBuilder(128);
                for (String value : get_EmpInternetOffime ) {
                    if (sb.length() > 0) {
                        sb.append("|");
                    }
                    sb.append(value);
                }
                sb.insert(0, "|");


                SQLiteDatabase DataBase;
                File db_path = new File("/data/data/" + MainActivity.class.getPackage().getName() + "/databases/Db");
                db_path.getParentFile().mkdirs();
                DataBase = SQLiteDatabase.openOrCreateDatabase(db_path, null);          //Opens database in writable mode.


                uploadEmpInOutData.put("ON_OFF_details", String.valueOf(sb));

                Log.e("DatauploadService", String.valueOf(sb));
                JSONParserSync jsonParserSync = new JSONParserSync();
                JSONObject s = jsonParserSync.sendPostRequest("http://35.194.196.229:8080/JindalApi/EmployeeInternetOnOFFPipe.jsp",uploadEmpInOutData);
                Log.e("DatauploadService",""+s+sb);
                try {
                    String succes = s.getString("success");
                    if(succes.equals("1")) {
                        //.d("Resposnse", succes + " " + "retail_videodata");
                        //  DataBase.execSQL("update retail_videodata set S_FLAG='1'");
                    }
                }catch (Exception e){e.printStackTrace();}

                return null;
            }
        }
        uploadData uploadData= new uploadData();
        uploadData.execute();
    }



//    public void uploaddata() {
//
//        get_EmpInOutTime = db.getEmployeeInOutTime();
//
//        HashMap<String,String> uploadEmpInOutData = new HashMap<>();
//
//
//        StringBuilder sb = new StringBuilder(128);
//        {
//            for (String value : get_EmpInOutTime) {
//                if (sb.length() > 0) {
//                    sb.append("|");
//                }
//                sb.append(value);
//            }
//            sb.insert(0, "|");
//
//
////            SQLiteDatabase DataBase;
////            File db_path = new File("/data/data/" + PendingFragment.class.getPackage().getName() + "/databases/Db");
////            db_path.getParentFile().mkdirs();
////            DataBase = SQLiteDatabase.openOrCreateDatabase(db_path, null);          //Opens database in writable mode.
//
//
//            uploadEmpInOutData.put("In_Out_details", String.valueOf(sb));
//
//            Log.e("Service IN_OUT_DATA", String.valueOf(sb));
//            JSONParserSync jsonParserSync = new JSONParserSync();
//            JSONObject s = jsonParserSync.sendPostRequest("http://35.194.196.229:8080/AequmEmployeeAttendance/AequmAPI/Upload_Time_In_Out.jsp", uploadEmpInOutData);
//            Log.e("Datauploadservice", "" + s + sb);
//            try {
//                String succes = s.getString("success");
//                if (succes.equals("1")) {
//
//
//                    //.d("Resposnse", succes + " " + "retail_videodata");
//                    //  DataBase.execSQL("update retail_videodata set S_FLAG='1'");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//    }
}
