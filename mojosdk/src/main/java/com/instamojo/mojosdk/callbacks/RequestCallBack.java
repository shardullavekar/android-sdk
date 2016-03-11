package com.instamojo.mojosdk.callbacks;

import com.instamojo.mojosdk.models.Transaction;

/**
 * Authored by vedhavyas on 11/03/16.
 */

/**
 * Callback for the network MojoRequest
 */
public interface RequestCallBack {
    /**
     * Gets called when the Network call fails
     *
     * @param transaction - original transaction
     * @param exception   - Cause of Failure
     */
    void onError(Transaction transaction, Exception exception);

    /**
     * Gets called on Successful network call
     *
     * @param transaction - Transaction with additional payment options retrived from server
     */
    void onSuccess(Transaction transaction);
}
