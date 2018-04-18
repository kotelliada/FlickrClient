package io.github.kotelliada.flickrlient.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class ConnectionUtils {
    public static boolean isNetworkAvailableAndConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = connectivityManager.getActiveNetworkInfo() != null;
        return isNetworkAvailable && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}