package com.instamojo.android.callbacks;

import android.os.Bundle;

import com.instamojo.android.models.Card;
import com.instamojo.android.models.Transaction;

/**
 * Callback listener that is passed along with {@link com.instamojo.android.models.Transaction}
 * and {@link com.instamojo.android.models.Card} to
 * {@link com.instamojo.android.network.Request#Request(Transaction, Card, JusPayRequestCallback)}.
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
     * @param bundle - Payment bundle that needs to be passed to {@link com.instamojo.android.activities.PaymentActivity}
     *               with Key {@link com.instamojo.android.activities.PaymentActivity#PAYMENT_BUNDLE}.
     * @param error  - Appropriate {@link Exception} extended exception.
     *               {@link com.instamojo.android.models.Errors.ConnectionException} related to all network exceptions.
     */
    void onFinish(Bundle bundle, Exception error);
}
