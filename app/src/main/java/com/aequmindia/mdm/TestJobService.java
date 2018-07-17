package com.aequmindia.mdm;


import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import Interface.Appinstall;
import Pojo.Login;
import Utility.ApiClient;
import Utility.Config;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Nishant on 8/2/18.
 */

public class TestJobService extends Service {

    private static final String TAG = "SyncService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DBHelper db =  new DBHelper(getApplicationContext());
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        final String    username = sharedPreferences.getString(Config.SHARED_PREF_USERNAME, "");

        if (!Network.isConnected(getApplicationContext()))
        {
          //  db.insertEmpMobileOnOffData(username,"Off",currenttime());
            Log.e(TAG,"OFF");
        }
        else {

            UnInstallProcessSetting();
            Log.e(TAG,"ON");
        }

        Log.e(TAG,"job started");

        return START_STICKY;
    }

    public void UnInstallProcessSetting()
    {

        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        final String    username = sharedPreferences.getString(Config.SHARED_PREF_USERNAME, "");

        Appinstall apiService =
                ApiClient.getClient().create(Appinstall.class);
        Call<Login> mService = apiService.UninstalluploadSetting(username,currenttime(),"ON");
        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                response.body();

                Log.e(TAG,"TRUE");
            }
            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                call.cancel();
                //   Toast.makeText(loginactivity.this, "Please check your network connection and internet permission", Toast.LENGTH_LONG).show();
            }
        });
    }

    public String currenttime()
    {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+5:30"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        // you can get seconds by adding  "...:ss" to it
        date.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));

        String localTime = date.format(currentLocalTime);
        Log.e(TAG,localTime);
        return localTime;


    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
