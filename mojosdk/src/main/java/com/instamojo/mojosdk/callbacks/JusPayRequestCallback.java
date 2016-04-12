package com.instamojo.mojosdk.callbacks;

import android.os.Bundle;

import com.instamojo.mojosdk.models.Card;
import com.instamojo.mojosdk.models.Transaction;

/**
 * Callback listener that is passed along with {@link com.instamojo.mojosdk.models.Transaction}
 * and {@link com.instamojo.mojosdk.models.Card} to
 * {@link com.instamojo.mojosdk.network.Request#Request(Transaction, Card, JusPayRequestCallback)}.
 *
 *
 * @author vedhavyas
 * @version 1.0
 * @since 14/03/16
 */

public interface JusPayRequestCallback {
    /**
     * Called on finish of the network call to Juspay with card and order details.
     * Pass on the bundle only after validating error to be null.
     *
     * @param bundle - Payment bundle that needs to be passed to {@link com.instamojo.mojosdk.activities.PaymentActivity}
     *               with Key {@link com.instamojo.mojosdk.activities.PaymentActivity#PAYMENT_BUNDLE}.
     * @param error  - Appropriate {@link Exception} extended exception.
     *               {@link com.instamojo.mojosdk.models.Errors.ConnectionException} related to all network exceptions.
     */
    void onFinish(Bundle bundle, Exception error);
}
