package com.instamojo.mojosdk.callbacks;

import com.instamojo.mojosdk.models.Transaction;

/**
 * Authored by vedhavyas on 11/03/16.
 */

/**
 * Callback for the network Request
 */
public interface MojoRequestCallBack {
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
     * @param transaction - Transaction with additional payment options retrieved from server
     */
    void onSuccess(Transaction transaction);
}
