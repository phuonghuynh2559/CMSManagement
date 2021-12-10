package com.ChildMonitoringSystem.managerapp.internet_checking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

public class InternetChecking {
    public static int Checknet(Context ctx) {
        boolean wifiConected;
        boolean mobileConnected;
        if (ctx == null)
            return 0;
        ConnectivityManager con = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (con == null)
            return 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Network network = con.getActiveNetwork();
            if (network == null)
                return 0;

            NetworkCapabilities networkCapabilities = con.getNetworkCapabilities(network);
            wifiConected = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI);
            mobileConnected = networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR);

            return checkType(wifiConected,mobileConnected);

        } else {
            NetworkInfo networkInfo = con.getActiveNetworkInfo();
            if (networkInfo !=null && networkInfo.isConnected()){
                wifiConected = networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
                mobileConnected = networkInfo.getType() == ConnectivityManager.TYPE_MOBILE;

                return checkType(wifiConected,mobileConnected);
            }
            return 0;
        }
    }
    private static int checkType(boolean a, boolean b){
        return a ? 1:2 ;
    }
}
