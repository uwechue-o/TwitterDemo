package com.dmt.twitterdemo;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by ouwechue on 3/21/17.
 */

public class MyApplication extends Application {

    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
