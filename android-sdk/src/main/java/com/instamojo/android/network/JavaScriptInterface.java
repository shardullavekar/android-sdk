package com.instamojo.android.network;

import android.app.Activity;
import android.os.Bundle;

import com.instamojo.android.activities.PaymentActivity;
import com.instamojo.android.helpers.Constants;
import com.instamojo.android.helpers.Logger;

/**
 * JavaScript interface to transfer control from Webview to Application.
 *
 *
 * @author vedhavyas
 * @version 1.0
 * @since 14/03/16
 */
public class JavaScriptInterface {

    private Activity activity;

    /**
     * Constructor for ScriptInterface.
     *
     * @param activity This activity must be a subclass of {@link com.instamojo.android.activities.BaseActivity}.
     */
    public JavaScriptInterface(Activity activity) {
        this.activity = activity;
    }

    @android.webkit.JavascriptInterface
    public void onTransactionComplete(String status, String orderID) {
        Logger.logDebug(activity, this.getClass().getSimpleName(), "Received Call to Javascript Interface");
        if (!status.equalsIgnoreCase("true")) {
            ((PaymentActivity) activity).returnResult(Activity.RESULT_CANCELED);
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(Constants.ORDER_ID, orderID);
        bundle.putString(Constants.TRANSACTION_STATUS, status);
        Logger.logDebug(activity, this.getClass().getSimpleName(), "Returning result back to caller");
        ((PaymentActivity) activity).returnResult(bundle, Activity.RESULT_OK);
    }
}
