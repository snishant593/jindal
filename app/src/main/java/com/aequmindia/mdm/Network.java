package com.aequmindia.mdm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Network {

    /**
     * Check Network is available or not
     *
     * @param context
     * @return True if connected, False otherwise.
     */
    public static boolean isConnected(Context context) {
        ConnectivityManager conn = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();
        if (networkInfo != null) return networkInfo.isConnected();

        return false;
    }


}
