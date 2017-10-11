package com.instamojo.android;

import android.content.Context;
import android.util.Log;

import com.instamojo.android.helpers.Constants;
import com.instamojo.android.helpers.Logger;
import com.instamojo.android.network.Urls;

/**
 * SDK Base Class.
 */

public class Instamojo {

    private static Instamojo instance;
    private Context appContext;

    public Instamojo(Context appContext) {
        this.appContext = appContext;
    }

    /**
     * Initialises the previous session if exists
     *
     * @param appContext Application Context
     */
    public static void initialize(Context appContext) {
        instance = new Instamojo(appContext);
        Logger.setLogLevel(Log.WARN);
        Urls.setBaseUrl(Constants.DEFAULT_BASE_URL);
    }

    /**
     * Sets the log level for the session
     *
     * @param logLevel Android LOG class values
     */
    public static void setLogLevel(int logLevel) {
        if (!isInitialised()) {
            return;
        }
        Logger.setLogLevel(logLevel);
    }

    /**
     * Sets the base url for all network calls
     *
     * @param baseUrl URl
     */
    public static void setBaseUrl(String baseUrl) {
        if (!isInitialised()) {
            return;
        }
        Urls.setBaseUrl(baseUrl);
    }

    public static boolean isInitialised() {
        if (instance != null) {
            return true;
        }

        Log.e("Instamojo SDK", "Initialise the SDK with Application Context.");
        return false;
    }

    /**
     * @return Current instance
     */
    public static Instamojo getInstance() {
        return instance;
    }

    /**
     * @return Application context
     */
    public Context getAppContext() {
        return appContext;
    }
}
