package com.instamojo.android;

import android.content.Context;

import com.instamojo.android.helpers.Logger;
import com.instamojo.android.network.Urls;

/**
 * Authored by vedhavyas on 24/05/16.
 */
public class InstamojoSDK {
    public static void initialize(Context appContext) {
        Logger.initialize(appContext);
        Urls.initialize(appContext);
    }
}
