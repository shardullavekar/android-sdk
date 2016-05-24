package com.instamojo.android;

import android.app.Application;

/**
 * Authored by vedhavyas on 24/05/16.
 */
public class InstamojoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        InstamojoSDK.initialize(this);
    }
}
