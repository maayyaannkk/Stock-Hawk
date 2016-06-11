package com.sam_chordas.android.stockhawk;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by maayy on 04-06-2016.
 */
public class MyApplication extends Application {
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
