package io.github.kotelliada.flickrlient.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class ConnectionUtils {
    public static boolean isNetworkAvailableAndConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = connectivityManager.getActiveNetworkInfo() != null;
        return isNetworkAvailable && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}