package io.github.kotelliada.flickrlient;

import android.app.Application;

import io.github.kotelliada.flickrlient.utils.QueryPreferences;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        QueryPreferences.clearPreferences(this);
    }
}