package com.instamojo.android.network;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.instamojo.android.helpers.Logger;

/**
 * SDK URL Class.
 *
 * @author vedhavyas
 * @version 1.0
 * @since 14/03/16
 */

public class Urls {

    private static Context appContext;
    private static String baseUrl;

    public static void initialize(Context appContext) {
        Urls.appContext = appContext;
        try {
            ApplicationInfo ai = appContext.getPackageManager().getApplicationInfo(
                    appContext.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            baseUrl = bundle.getString("instamojo_sdk_base_url");
            Logger.logDebug("Urls", "Base URL - " + baseUrl);
        } catch (Exception e) {
            throw new RuntimeException("Add <meta-data android:name=\"instamojo_sdk_base_url\" android:value=\"https://www.instamojo.com/\"/> in the manifest file");
        }
    }

    /**
     * @return Order Create URL.
     */
    public static String getOrderCreateUrl() {
        return baseUrl + "v2/gateway/orders/";
    }

    /**
     * @return default redirect URL
     */
    public static String getDefaultRedirectUrl() {
        return baseUrl + "integrations/android/redirect/";
    }

    /**
     * @return base url
     */

    public static String getBaseUrl() {
        return baseUrl;
    }
}
