package com.aequmindia.mdm;

import android.*;
import android.app.Activity;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

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
 * Created by Nishant on 7/11/17.
 */

public class Admin extends DeviceAdminReceiver{
    String  username;
    String eol = "\n";
    String phoneNo;
    String message;

    private static final String TAG = "ADMIN";


    void showToast(Context context, CharSequence msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEnabled(Context context, Intent intent) {


        showToast(context, "Device Administrator: Activated");
    }

    @Override
    public CharSequence onDisableRequested(Context context, Intent intent) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        username = sharedPreferences.getString(Config.SHARED_PREF_USERNAME, "");

        Log.e(TAG,username);
        sendSMS();
        UninstallProcessWithRetrofit();
        return  "Company Compliance Policy Breach"+eol+"\n" +
                "Uninstallation of this application will lead you to company non-compliance policy."+eol+eol+
                "Are you sure you want to uninstall it";

    }




    @Override
    public void onDisabled(Context context, Intent intent) {

         showToast(context, "Device Administrator: Deactivated");
    }

    @Override
    public void onPasswordFailed(Context context, Intent intent) {
        showToast(context, "Sample Device Admin: pw failed");
        Vibrator v = (Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);
        // Vibrate for 2 seconds
        v.vibrate(2000);
    }

    @Override
    public void onPasswordSucceeded(Context context, Intent intent) {
        showToast(context, "Welcome Device Owner");


    }


    public void UninstallProcessWithRetrofit(){

        Appinstall apiService =
                ApiClient.getClient().create(Appinstall.class);
        Call<Login> mService = apiService.UnInstallUpload(username,currenttime(),"U");
        mService.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                response.body();

             Log.e(TAG,"App Uninstall" + username);
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

    public void sendSMS() {

        phoneNo = "+919711085752";
        message ="Mr./Ms."+username+"\n" +
                 "You have uninstalled company security application from your phone.\n" +
                 "Date/Time - "+currenttime()+"\n" +
                 "Thanks & Regards,\n" +
                 "Jindal IT Team";
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
           Log.e("Msg Delievered",phoneNo);

        } catch (Exception ex) {

            Log.e("Msg error",ex.getMessage().toString());

            ex.printStackTrace();
        }
    }





}