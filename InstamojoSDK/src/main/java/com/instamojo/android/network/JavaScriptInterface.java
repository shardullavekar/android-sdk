package com.instamojo.android.network;

import android.app.Activity;
import android.os.Bundle;

import com.instamojo.android.activities.PaymentActivity;

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
        if (!status.equalsIgnoreCase("Credit")) {
            ((PaymentActivity) activity).returnResult(Activity.RESULT_CANCELED);
            return;
        }
        Bundle bundle = new Bundle();
        bundle.putString(PaymentActivity.ORDER_ID, orderID);
        bundle.putString(PaymentActivity.TRANSACTION_STATUS, status);
        ((PaymentActivity) activity).returnResult(bundle, Activity.RESULT_OK);
    }
}
