package com.instamojo.android.network;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.instamojo.android.BuildConfig;
import com.instamojo.android.callbacks.JusPayRequestCallback;
import com.instamojo.android.callbacks.OrderRequestCallBack;
import com.instamojo.android.fragments.JusPaySafeBrowser;
import com.instamojo.android.helpers.Logger;
import com.instamojo.android.models.Card;
import com.instamojo.android.models.DebitCardOptions;
import com.instamojo.android.models.Errors;
import com.instamojo.android.models.NetBankingOptions;
import com.instamojo.android.models.Transaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Network Request Class.
 *
 *
 * @author vedhavyas
 * @version 1.0
 * @since 14/03/16
 */

public class Request {

    private Transaction transaction;
    private OrderRequestCallBack orderRequestCallBack;
    private JusPayRequestCallback jusPayRequestCallback;
    private Card card;

    /**
     * Network Request to create an order ID from Instamojo server.
     *
     * @param transaction Transaction model with all the mandatory fields set.
     * @param orderRequestCallBack Callback interface for the Asynchronous Network Call.
     */
    public Request(@NonNull Transaction transaction, @NonNull OrderRequestCallBack orderRequestCallBack) {
        this.transaction = transaction;
        this.orderRequestCallBack = orderRequestCallBack;
    }

    /**
     * Network Request to get order details from Juspay for JuspaySafeBrowser.
     *
     * @param transaction           Transaction model with all the mandatory fields set.
     * @param card                  Card with all the proper validations done.
     * @param jusPayRequestCallback Callback for Asynchronous network call.
     */
    public Request(@NonNull Transaction transaction, @NonNull Card card, @NonNull JusPayRequestCallback jusPayRequestCallback) {
        this.card = card;
        this.transaction = transaction;
        this.jusPayRequestCallback = jusPayRequestCallback;
    }

    /**
     * Executes the call to the server and calls the callback with  {@link Exception} if failed.
     */
    public void execute() {
        if (card == null) {
            executeInstamojoRequest();
            return;
        }

        executeJusPayRequest();
    }

    private void executeJusPayRequest() {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add("order_id", transaction.getDebitCardOptions().getOrderID())
                .add("merchant_id", transaction.getDebitCardOptions().getMerchantID())
                .add("payment_method_type", "CARD")
                .add("card_number", card.getCardNumber())
                .add("name_on_card", card.getCardHolderName())
                .add("card_exp_month", card.getMonth())
                .add("card_exp_year", card.getYear())
                .add("card_security_code", card.getCvv())
                .add("save_to_locker", card.canSaveCard() ? "true" : "false")
                .add("redirect_after_payment", "true")
                .add("format", "json")
                .build();

        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url(transaction.getDebitCardOptions().getUrl())
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.logError(this.getClass().getSimpleName(), "Error while making Juspay request - " + e.getMessage());
                jusPayRequestCallback.onFinish(null, new Errors.ConnectionException(e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) {
                String responseBody;
                try {
                    responseBody = response.body().string();
                    response.body().close();
                    Bundle bundle = parseJusPayResponse(responseBody);
                    jusPayRequestCallback.onFinish(bundle, null);
                } catch (IOException e) {
                    Logger.logError(this.getClass().getSimpleName(), "Error while making Juspay request - " + e.getMessage());
                    jusPayRequestCallback.onFinish(null, new Errors.ConnectionException(e.getMessage()));
                } catch (JSONException e) {
                    Logger.logError(this.getClass().getSimpleName(), "Error while making Juspay request - " + e.getMessage());
                    jusPayRequestCallback.onFinish(null, e);
                }
            }
        });

    }

    private Bundle parseJusPayResponse(String responseBody) throws JSONException {
        JSONObject responseObject = new JSONObject(responseBody);
        String url = responseObject.getJSONObject("payment").getJSONObject("authentication").getString("url");
        final Bundle args = new Bundle();
        args.putString(JusPaySafeBrowser.URL, url);
        args.putString(JusPaySafeBrowser.MERCHANT_ID, transaction.getDebitCardOptions().getMerchantID());
        args.putString(JusPaySafeBrowser.ORDER_ID, transaction.getDebitCardOptions().getOrderID());
        return args;
    }

    private void executeInstamojoRequest() {
        OkHttpClient client = new OkHttpClient();

        FormBody.Builder builder = new FormBody.Builder()
                .add("buyer_name", transaction.getBuyerName())
                .add("buyer_email", transaction.getBuyerEmail())
                .add("purpose", transaction.getPurpose())
                .add("buyer_phone", transaction.getBuyerPhone())
                .add("amount", transaction.getAmount())
                .add("currency", transaction.getCurrency())
                .add("mode", transaction.getMode());
        if (transaction.getWebHook() != null) {
            builder.add("webhook", transaction.getWebHook());
        }
        RequestBody body = builder.build();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("User-Agent", getUserAgent());
        headers.put("Authorization", "Bearer " + transaction.getAuthToken());

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(Urls.MOJO_TRANSACTION_INIT_URL)
                .removeHeader("User-Agent")
                .headers(Headers.of(headers))
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.logError(this.getClass().getSimpleName(), "Error while making Instamojo request - " + e.getMessage());
                orderRequestCallBack.onFinish(transaction, new Errors.ConnectionException(e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response r) {
                String responseBody = "";
                try {
                    responseBody = r.body().string();
                    r.body().close();
                    updateTransactionDetails(responseBody);
                    orderRequestCallBack.onFinish(transaction, null);
                } catch (IOException e) {
                    Logger.logError(this.getClass().getSimpleName(), "Error while making Instamojo request - " + e.getMessage());
                    orderRequestCallBack.onFinish(transaction, new Errors.ConnectionException(e.getMessage()));
                } catch (JSONException e) {
                    Logger.logError(this.getClass().getSimpleName(), "Error while making Instamojo request - " + e.getMessage());
                    orderRequestCallBack.onFinish(transaction, new Errors.ServerException(responseBody));
                }
            }
        });
    }

    private void updateTransactionDetails(String responseBody) throws JSONException {
        JSONObject responseObject = new JSONObject(responseBody);
        transaction.setId(responseObject.getString("id"));
        transaction.setResourceURI(responseObject.getString("resource_uri"));
        if (responseObject.has("resource_cards")) {
            JSONObject resourceCards = responseObject.getJSONObject("resource_cards");
            String merchantID = resourceCards.getString("juspay_merchant_id");
            String orderID = resourceCards.getString("juspay_order_id");
            String paymentURL = resourceCards.getString("juspay_payment_uri");
            transaction.setDebitCardOptions(new DebitCardOptions(orderID, merchantID, paymentURL));
        }

        if (responseObject.has("resource_netbanking")) {
            JSONObject resourceNB = responseObject.getJSONObject("resource_netbanking");
            String nbURL = resourceNB.getString("submission_uri");
            JSONArray banksArray = resourceNB.getJSONArray("banks");
            HashMap<String, String> banks = new HashMap<>();
            JSONObject bank;
            for (int i = 0; i < banksArray.length(); i++) {
                bank = banksArray.getJSONObject(i);
                banks.put(bank.getString("name"), bank.getString("id"));
            }
            transaction.setNetBankingOptions(new NetBankingOptions(nbURL, banks));
        }
    }

    private String getUserAgent() {
        return "Android Mojo SDK;" + Build.MODEL + ";" + Build.BRAND + ";" + Build.VERSION.SDK_INT
                + ";" + BuildConfig.APPLICATION_ID + ";" + BuildConfig.VERSION_NAME + ";" + BuildConfig.VERSION_CODE;
    }

}
