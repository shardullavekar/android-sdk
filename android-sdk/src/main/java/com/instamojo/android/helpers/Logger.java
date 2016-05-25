package com.instamojo.android.helpers;

import android.util.Log;

/**
 * Authored by vedhavyas on 28/04/16.
 */
public class Logger {

    private static int logLevel = Log.WARN;

    public static void logDebug(String tag, String message) {
        if (logLevel <= Log.DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void logError(String tag, String message) {
        Log.e(tag, message);
    }

    public static void setLogLevel(int logLevel) {
        Logger.logLevel = logLevel;
    }
}
