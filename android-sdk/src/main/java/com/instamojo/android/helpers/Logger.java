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
    private static final String ERROR = "error";

    public static void logDebug(Context context, String tag, String data) {
        if (isDebuggable(context)) {
            Log.d(tag, data);
        }
    }

    public static void logError(String tag, String errorMessage) {
        Log.e(tag, errorMessage);
    }

    private static boolean isDebuggable(Context context) {
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            String logLevel = bundle.getString("instamojo_sdk_log_level");
            return logLevel.equalsIgnoreCase(DEBUG);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("Instamojo SDK", "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e("Instamojo SDK", "Failed to load meta-data, NullPointer: " + e.getMessage());
        }

        return true;
    }
}
