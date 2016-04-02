package com.instamojo.mojosdk.network;

import android.app.Activity;
import android.os.Bundle;

import com.instamojo.mojosdk.activities.PaymentActivity;

/**
 * Authored by vedhavyas on 02/04/16.
 */
public class JavaScriptInterface {

    private Activity activity;

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
