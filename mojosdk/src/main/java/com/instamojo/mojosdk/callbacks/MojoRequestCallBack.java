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
     * Gets called when the Network call is finished
     *
     * @param transaction - original transaction if the error is not null
     * @param error   - Cause of Failure
     */
    void onFinish(Transaction transaction, Exception error);

}
