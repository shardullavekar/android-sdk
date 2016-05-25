package com.instamojo.android.helpers;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

/**
 * Authored by vedhavyas on 28/04/16.
 */
public class Logger {

    private static final String DEBUG = "debug";

    private static boolean isDebuggable = true;
    private static Context appContext;

    public static void initialize(Context appContext) {
        Logger.appContext = appContext;
        isDebuggable = getLogLevel(appContext);
    }

    public static void logDebug(String tag, String data) {
        if (isDebuggable) {
            Log.d(tag, data);
        }
    }

    public static void logError(String tag, String errorMessage) {
        Log.e(tag, errorMessage);
    }

    private static boolean getLogLevel(Context context) {
        boolean debug = false;
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            String logLevel = bundle.getString("instamojo_sdk_log_level");
            debug = logLevel.equalsIgnoreCase(DEBUG);
        } catch (Exception e) {
            //ignore - meta data is missing
        }

        if (debug) {
            Log.d("Instamojo SDK", "Log level is set to Debug. Change it error when pushing to Play store");
        }

        return debug;
    }
}
