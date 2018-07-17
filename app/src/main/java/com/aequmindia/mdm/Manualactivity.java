package com.aequmindia.mdm;

import android.Manifest;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class Manualactivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
            GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
        private TextView latitudeTextView,longitudeTextView;
        private Location mylocation;
        private GoogleApiClient googleApiClient;
        Double lat,lon;
    String sublat;
    private DevicePolicyManager mDevicePolicyManager;
    private ComponentName mComponentName;



    private final static int REQUEST_CHECK_SETTINGS_GPS=0x1;
        private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x2;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.manual);
        latitudeTextView=(TextView)findViewById(R.id.latitudeTextView);
        longitudeTextView=(TextView)findViewById(R.id.longitudeTextView);
        mDevicePolicyManager = (DevicePolicyManager)getSystemService(
                Context.DEVICE_POLICY_SERVICE);
        mComponentName = new ComponentName(this, Admin.class);


        turnGpson();
        setUpGClient();

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.activity);
        getSupportActionBar().setDisplayUseLogoEnabled(true);



    }

        private synchronized void setUpGClient() {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, 0, this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
            googleApiClient.connect();
        }

        @Override
        public void onLocationChanged(Location location) {
            mylocation = location;
            if (mylocation != null) {
                Double latitude=mylocation.getLatitude();
                Double longitude=mylocation.getLongitude();
                lat = mylocation.getLatitude();
                lon = mylocation.getLongitude();
                latitudeTextView.setText("Latitude : "+latitude);
                longitudeTextView.setText("Longitude : "+longitude);


                Log.e("SUBLAT",lat +"        "+lon);
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);

              //  block();

                //Or Do whatever you want with your location 28.5110613
            }
        }

        public void block()
        {
            if (sublat.equals("28.524") ||sublat .equals("28.511")|| sublat .equals("19.786")|| sublat .equals ("19.788")||sublat.equals ("19.782"))
                                    {
                                        mDevicePolicyManager.setCameraDisabled(mComponentName, true);
                                        Log.e("Manualactivity","camerablock");
                                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                        startActivity(intent);

                                    }
                                    else
                                    {
                                        Log.e("Manualactivity","camera Unblock");
                                    }


        }

        @Override
        public void onConnected(Bundle bundle) {
            checkPermissions();
        }

        @Override
        public void onConnectionSuspended(int i) {
            //Do whatever you need
            //You can display a message here
        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            //You can display a message here
        }

        private void getMyLocation(){
            if(googleApiClient!=null) {
                if (googleApiClient.isConnected()) {
                    int permissionLocation = ContextCompat.checkSelfPermission(Manualactivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION);
                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                        mylocation =                     LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                        LocationRequest locationRequest = new LocationRequest();
                        locationRequest.setInterval(3000);
                        locationRequest.setFastestInterval(3000);
                        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                                .addLocationRequest(locationRequest);
                        builder.setAlwaysShow(true);
                        LocationServices.FusedLocationApi
                                .requestLocationUpdates(googleApiClient, locationRequest, this);
                        PendingResult<LocationSettingsResult> result =
                                LocationServices.SettingsApi
                                        .checkLocationSettings(googleApiClient, builder.build());
                        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {

                            @Override
                            public void onResult(LocationSettingsResult result) {
                                final Status status = result.getStatus();
                                switch (status.getStatusCode()) {
                                    case LocationSettingsStatusCodes.SUCCESS:
                                        // All location settings are satisfied.
                                        // You can initialize location requests here.
                                        int permissionLocation = ContextCompat
                                                .checkSelfPermission(Manualactivity.this,
                                                        Manifest.permission.ACCESS_FINE_LOCATION);
                                        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                            mylocation = LocationServices.FusedLocationApi
                                                    .getLastLocation(googleApiClient);
                                        }
                                        break;
                                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                        // Location settings are not satisfied.
                                        // But could be fixed by showing the user a dialog.
                                        try {
                                            // Show the dialog by calling startResolutionForResult(),
                                            // and check the result in onActivityResult().
                                            // Ask to turn on GPS automatically
                                            status.startResolutionForResult(Manualactivity.this,
                                                    REQUEST_CHECK_SETTINGS_GPS);
                                        } catch (IntentSender.SendIntentException e) {
                                            // Ignore the error.
                                        }
                                        break;
                                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                        // Location settings are not satisfied.
                                        // However, we have no way
                                        // to fix the
                                        // settings so we won't show the dialog.
                                        // finish();
                                        break;
                                }
                            }
                        });
                    }
                }
            }
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            switch (requestCode) {
                case REQUEST_CHECK_SETTINGS_GPS:
                    switch (resultCode) {
                        case Activity.RESULT_OK:
                            getMyLocation();
                            break;
                        case Activity.RESULT_CANCELED:
                            finish();
                            break;
                    }
                    break;
            }
        }

        private void checkPermissions(){
            int permissionLocation = ContextCompat.checkSelfPermission(Manualactivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION);
            List<String> listPermissionsNeeded = new ArrayList<>();
            if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
                if (!listPermissionsNeeded.isEmpty()) {
                    ActivityCompat.requestPermissions(this,
                            listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
                }
            }else{
                getMyLocation();
            }

        }

        @Override
        public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
            int permissionLocation = ContextCompat.checkSelfPermission(Manualactivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION);
            if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                getMyLocation();
            }
        }

    public void turnGpson()
    {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getApplicationContext()).addApi(LocationServices.API).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

            builder.setAlwaysShow(true); // this is the key ingredient

            PendingResult result = LocationServices.SettingsApi.
                    checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback
                    (new ResultCallback<LocationSettingsResult>()


                    {

                        @Override

                        public void onResult(LocationSettingsResult result)

                        {


                            final Status status = result.getStatus();

                            final LocationSettingsStates state = result.getLocationSettingsStates();

                            switch (status.getStatusCode())

                            {
                                //28.52443,28.5110613,19.786327,19.788365,19.782559,

                                case LocationSettingsStatusCodes.SUCCESS:
//
//                                    if (lat == 28.52443||lat == 28.5110613|| lat == 19.78637|| lat == 19.788365||lat == 19.782559)
//                                    {
//                                        mDevicePolicyManager.setCameraDisabled(mComponentName, true);
//                                        Log.e("Manualactivity","camerablock");
//
//                                    }
//                                    else
//                                    {
//                                        Log.e("Manualactivity","camera Unblock");
//                                    }
//


                                    //  mDevicePolicyManager.setCameraDisabled(mComponentName, true);
                                    Log.e("GPS","Success");

                                    break;

                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    try {
                                        status.startResolutionForResult(Manualactivity.this, 1000);

                                        Log.e("GPS","Resolution");


                                    } catch (IntentSender.SendIntentException e)

                                    {

                                    }

                                    break;

                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                                    break;

                            }
                        }

                    });

        }


        googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).build();

    }




    }