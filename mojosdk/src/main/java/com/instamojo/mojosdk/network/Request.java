package com.instamojo.mojosdk.network;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.instamojo.mojosdk.callbacks.JusPayRequestCallback;
import com.instamojo.mojosdk.callbacks.MojoRequestCallBack;
import com.instamojo.mojosdk.models.Card;
import com.instamojo.mojosdk.models.DebitCardOptions;
import com.instamojo.mojosdk.models.Errors;
import com.instamojo.mojosdk.models.NetBankingOptions;
import com.instamojo.mojosdk.models.Transaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Authored by vedhavyas on 11/03/16.
 */

public class Request {

    private Transaction transaction;
    private MojoRequestCallBack mojoRequestCallBack;
    private JusPayRequestCallback jusPayRequestCallback;
    private Card card;

    /**
     * Network Request to retrieve the necessary data from Server
     *
     * @param transaction         - Transaction model with all the mandatory fields set\
     * @param mojoRequestCallBack - Callback interface for the Asynchronous Network Call
     */
    public Request(@NonNull Transaction transaction, @NonNull MojoRequestCallBack mojoRequestCallBack) {
        this.transaction = transaction;
        this.mojoRequestCallBack = mojoRequestCallBack;
    }

    public Request(@NonNull Transaction transaction, @NonNull Card card, @NonNull JusPayRequestCallback jusPayRequestCallback) {
        this.card = card;
        this.transaction = transaction;
        this.jusPayRequestCallback = jusPayRequestCallback;
    }

    /**
     * Executes the call to the server and callbacks on the callback passed with either updated Data or error
     */
    public void execute() {
        if (card == null) {
            executeMojoRequest();
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
                .add("save_to_locker", "true")
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
                jusPayRequestCallback.onFinish(null, new Errors.ConnectionException(e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body().string();
                response.body().close();
                try {
                    Bundle bundle = parseJusPayResponse(responseBody);
                    jusPayRequestCallback.onFinish(bundle, null);
                } catch (JSONException e) {
                    jusPayRequestCallback.onFinish(null, e);
                }
            }
        });

    }

    private Bundle parseJusPayResponse(String responseBody) throws JSONException {
        JSONObject responseObject = new JSONObject(responseBody);
        String url = responseObject.getJSONObject("payment").getJSONObject("authentication").getString("url");
        final Bundle args = new Bundle();
        args.putString("url", url);
        args.putString("merchantId", transaction.getDebitCardOptions().getMerchantID());
        args.putString("orderId", transaction.getDebitCardOptions().getOrderID());
        return args;
    }

    private void executeMojoRequest() {
        OkHttpClient client = new OkHttpClient();

        FormBody.Builder builder = new FormBody.Builder()
                .add("buyer_name", transaction.getBuyerName())
                .add("buyer_email", transaction.getBuyerEmail())
                .add("purpose", transaction.getPurpose())
                .add("buyer_phone", transaction.getBuyerPhone())
                .add("amount", transaction.getAmount())
                .add("currency", transaction.getCurrency())
                .add("seller_id", transaction.getSellerID())
                .add("mode", transaction.getMode());
        if (transaction.getWebHook() != null) {
            builder.add("webhook", transaction.getWebHook());
        }
        RequestBody body = builder.build();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(Urls.MOJO_TRANSACTION_INIT_URL)
                .addHeader("Authorization", "Bearer " + transaction.getAuthToken())
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mojoRequestCallBack.onFinish(transaction, new Errors.ConnectionException(e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response r) throws IOException {
                String response = r.body().string();
                r.body().close();
                try {
                    updateTransactionDetails(response);
                    mojoRequestCallBack.onFinish(transaction, null);
                } catch (Exception e) {
                    mojoRequestCallBack.onFinish(transaction, new Errors.ServerException(response));
                }
            }
        });
    }

    private void updateTransactionDetails(String responseBody) throws JSONException {
        JSONObject responseObject = new JSONObject(responseBody);
        transaction.setId(responseObject.getString("id"));
        transaction.setPaymentRequestID(responseObject.getString("payment_request_id"));
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

}
