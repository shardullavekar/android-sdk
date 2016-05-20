package com.instamojo.android.callbacks;

import com.instamojo.android.models.Order;

/**
 * Callback listener that is passed along with {@link Order} to
 * {@link com.instamojo.android.network.Request#Request(Order, OrderRequestCallBack)}.
 *
 *
 * @author vedhavyas
 * @version 1.0
 * @since 14/03/16
 */

public interface OrderRequestCallBack {
    /**
     * Called on finishing the order creation with MojoServer.
     * {@link Order} will be unchanged if the error is not null.
     * @param order - original order if the error is not null else
     *                      {@link Order#debitCardOptions} and {@link Order#netBankingOptions}
     *                      will be updated from the response.
     * @param error   - Appropriate {@link Exception} extended exception.
     *              {@link com.instamojo.android.models.Errors.ConnectionException} related to all network exceptions
     *              and {@link com.instamojo.android.models.Errors.ServerException} related to all
     *              form level validation errors from MojoServer.
     */
    void onFinish(Order order, Exception error);

}
