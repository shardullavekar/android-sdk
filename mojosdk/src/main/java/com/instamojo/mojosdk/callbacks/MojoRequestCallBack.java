package com.instamojo.mojosdk.callbacks;

import com.instamojo.mojosdk.models.Transaction;

/**
 * Callback listener that is passed along with {@link com.instamojo.mojosdk.models.Transaction} to
 * {@link com.instamojo.mojosdk.network.Request#Request(Transaction, MojoRequestCallBack)}.
 *
 *
 * @author vedhavyas
 * @version 1.0
 * @since 14/03/16
 */

public interface MojoRequestCallBack {
    /**
     * Called on finishing the order creation with MojoServer.
     * {@link Transaction} will be unchanged if the error is not null.
     * @param transaction - original transaction if the error is not null else
     *                      {@link Transaction#debitCardOptions} and {@link Transaction#netBankingOptions}
     *                      will be updated from the response.
     * @param error   - Appropriate {@link Exception} extended exception.
     *              {@link com.instamojo.mojosdk.models.Errors.ConnectionException} related to all network exceptions
     *              and {@link com.instamojo.mojosdk.models.Errors.ServerException} related to all
     *              form level validation errors from MojoServer.
     */
    void onFinish(Transaction transaction, Exception error);

}
