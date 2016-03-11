package com.instamojo.mojosdk.network;

import android.support.annotation.NonNull;

import com.instamojo.mojosdk.callbacks.RequestCallBack;
import com.instamojo.mojosdk.models.Transaction;

/**
 * Authored by vedhavyas on 11/03/16.
 */

public class MojoRequest {

    private static final String AUTH_TOKEN = "X-AUTH-TOKEN";
    private Transaction transaction;
    private RequestCallBack callBack;

    /**
     * Network MojoRequest to retrieve the necessary data from Server
     *
     * @param transaction - Transaction model with all the mandatory fields set
     * @param callBack    - Callback interface for the Asynchronous Network Call
     */
    public MojoRequest(@NonNull Transaction transaction, @NonNull RequestCallBack callBack) {
        this.transaction = transaction;
        this.callBack = callBack;
    }

    /**
     * Executes the call to the server and callbacks on the callback passed with either updated transaction or error
     */
    public void execute() {
        //// TODO: 11/03/16
    }
}
