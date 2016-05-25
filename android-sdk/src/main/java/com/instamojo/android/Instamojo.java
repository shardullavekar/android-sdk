package com.instamojo.android;

import com.instamojo.android.helpers.Logger;
import com.instamojo.android.network.Urls;

/**
 * SDK Base Class.
 *
 * @author vedhavyas
 * @version 1.0
 * @since 14/03/16
 */

public class Instamojo {

    public static void setLogLevel(int logLevel) {
        Logger.setLogLevel(logLevel);
    }

    public static void setBaseUrl(String baseUrl) {
        Urls.setBaseUrl(baseUrl);
    }
}
