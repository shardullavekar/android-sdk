package com.instamojo.android;

import android.app.Application;

/**
 * Authored by vedhavyas on 25/05/16.
 */
public class InstamojoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Instamojo.initialize(this);
    }
}
