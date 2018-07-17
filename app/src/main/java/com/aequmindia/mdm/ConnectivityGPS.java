package com.aequmindia.mdm;


import android.app.Activity;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Nishant on 7/12/17.
 */

public class ConnectivityGPS extends Service {
//
    private DatabaseReference ref;
    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mComponentName;



    @Override
    public void onCreate() {
        super.onCreate();

        mDevicePolicyManager = (DevicePolicyManager) getSystemService(
                Context.DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(this, Admin.class);
         ref = FirebaseDatabase.getInstance().getReference();
        if (!checkConnection()) {

            mDevicePolicyManager.setCameraDisabled(mComponentName, true);

            Toast.makeText(getApplicationContext(), " GPS OFF", Toast.LENGTH_SHORT).show();

//            Intent i = new Intent();
//            i.setClass(this, Manualactivity.class);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(i);
            Log.e("connection","off");



        }

        else
        {
            mDevicePolicyManager.setCameraDisabled(mComponentName, false);
            Intent i = new Intent();
            i.setClass(this, Manualactivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            Log.e("connection","on");

        }
        stopSelf();
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    private boolean checkConnection() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }
}





