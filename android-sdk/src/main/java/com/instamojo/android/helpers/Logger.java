package com.instamojo.android.helpers;

import android.util.Log;

public class Logger {

    private static int logLevel = Log.WARN;

    /**
     * Logs debug messages if log level is <= Log.DEBUG
     *
     * @param tag     SimpleTag
     * @param message Error Message
     */
    public static void logDebug(String tag, String message) {
        if (logLevel <= Log.DEBUG) {
            Log.d(tag, message);
        }
    }

    /**
     * Logs all the errors
     *
     * @param tag     SimpleTag
     * @param message Error message
     */
    public static void logError(String tag, String message) {
        Log.e(tag, message);
    }

    public static void setLogLevel(int logLevel) {
        Logger.logLevel = logLevel;
    }
}
