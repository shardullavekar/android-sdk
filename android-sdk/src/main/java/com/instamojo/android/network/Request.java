package com.instamojo.android.network;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.instamojo.android.BuildConfig;
import com.instamojo.android.callbacks.JusPayRequestCallback;
import com.instamojo.android.callbacks.OrderRequestCallBack;
import com.instamojo.android.helpers.CardValidator;
import com.instamojo.android.helpers.Constants;
import com.instamojo.android.helpers.Logger;
import com.instamojo.android.models.Card;
import com.instamojo.android.models.CardOptions;
import com.instamojo.android.models.EMIOption;
import com.instamojo.android.models.EMIOptions;
import com.instamojo.android.models.Errors;
import com.instamojo.android.models.NetBankingOptions;
import com.instamojo.android.models.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
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

    private Order order;
    private OrderRequestCallBack orderRequestCallBack;
    private JusPayRequestCallback jusPayRequestCallback;
    private Card card;

    /**
     * Network Request to create an order ID from Instamojo server.
     *
     * @param order Order model with all the mandatory fields set.
     * @param orderRequestCallBack Callback interface for the Asynchronous Network Call.
     */
    public Request(@NonNull Order order, @NonNull OrderRequestCallBack orderRequestCallBack) {
        this.order = order;
        this.orderRequestCallBack = orderRequestCallBack;
    }

    /**
     * Network Request to get order details from Juspay for JuspaySafeBrowser.
     *
     * @param order           Order model with all the mandatory fields set.
     * @param card                  Card with all the proper validations done.
     * @param jusPayRequestCallback Callback for Asynchronous network call.
     */
    public Request(@NonNull Order order, @NonNull Card card, @NonNull JusPayRequestCallback jusPayRequestCallback) {
        this.card = card;
        this.order = order;
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

        //For maestro, add the default values if empty
        if (CardValidator.maestroCard(card.getCardNumber())) {
            if (card.getDate() == null || card.getDate().isEmpty()) {
                card.setDate("12/49");
            }

            if (card.getCvv() == null || card.getCvv().isEmpty()) {
                card.setDate("111");
            }
        }

        RequestBody body = new FormBody.Builder()
                .add("order_id", order.getCardOptions().getOrderID())
                .add("merchant_id", order.getCardOptions().getMerchantID())
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
                .url(order.getCardOptions().getUrl())
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.logError(this.getClass().getSimpleName(), "Error while making Juspay request - " + e.getMessage());
                jusPayRequestCallback.onFinish(null, new Errors.ConnectionError(e.getMessage()));
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
                    jusPayRequestCallback.onFinish(null, new Errors.ServerError(e.getMessage()));
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
        args.putString(Constants.URL, url);
        args.putString(Constants.MERCHANT_ID, order.getCardOptions().getMerchantID());
        args.putString(Constants.ORDER_ID, order.getCardOptions().getOrderID());
        return args;
    }

    private void executeInstamojoRequest() {
        OkHttpClient client = new OkHttpClient();

        FormBody.Builder builder = new FormBody.Builder()
                .add("name", order.getBuyerName())
                .add("email", order.getBuyerEmail())
                .add("amount", order.getAmount())
                .add("description", order.getDescription())
                .add("phone", order.getBuyerPhone())
                .add("currency", order.getCurrency())
                .add("transaction_id", order.getTransactionID())
                .add("redirect_url", order.getRedirectionUrl())
                .add("advanced_payment_options", "true")
                .add("mode", order.getMode());
        RequestBody body = builder.build();
        HashMap<String, String> headers = new HashMap<>();
        headers.put("User-Agent", getUserAgent());
        headers.put("Authorization", "Bearer " + order.getAuthToken());

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(Urls.getOrderCreateUrl())
                .removeHeader("User-Agent")
                .headers(Headers.of(headers))
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.logError(this.getClass().getSimpleName(), "Error while making Instamojo request - " + e.getMessage());
                orderRequestCallBack.onFinish(order, new Errors.ConnectionError(e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response r) {
                String responseBody = "";
                try {
                    responseBody = r.body().string();
                    r.body().close();
                    updateTransactionDetails(responseBody);
                    orderRequestCallBack.onFinish(order, null);
                } catch (IOException e) {
                    Logger.logError(this.getClass().getSimpleName(), "Error while making Instamojo request - " + e.getMessage());
                    orderRequestCallBack.onFinish(order, new Errors.ServerError(e.getMessage()));
                } catch (JSONException e) {
                    Logger.logError(this.getClass().getSimpleName(), "Error while making Instamojo request - " + e.getMessage());
                    orderRequestCallBack.onFinish(order, Errors.getAppropriateError(responseBody));
                }
            }
        });
    }

    private void updateTransactionDetails(String responseBody) throws JSONException {
        JSONObject responseObject = new JSONObject(responseBody);
        JSONObject orderObject = responseObject.getJSONObject("order");
        order.setId(orderObject.getString("id"));
        order.setTransactionID(orderObject.getString("transaction_id"));
        order.setResourceURI(orderObject.getString("resource_uri"));

        JSONObject paymentOptionsObject = responseObject.getJSONObject("payment_options");
        if (paymentOptionsObject.has("card_options") && !paymentOptionsObject.isNull("card_options")) {
            JSONObject cardOptions = paymentOptionsObject.getJSONObject("card_options");
            JSONObject submissionData = cardOptions.getJSONObject("submission_data");
            String merchantID = submissionData.getString("merchant_id");
            String orderID = submissionData.getString("order_id");
            String paymentURL = cardOptions.getString("submission_url");
            order.setCardOptions(new CardOptions(orderID, merchantID, paymentURL));
        }

        if (paymentOptionsObject.has("netbanking_options") && !paymentOptionsObject.isNull("netbanking_options")) {
            JSONObject netbankingOptions = paymentOptionsObject.getJSONObject("netbanking_options");
            String nbURL = netbankingOptions.getString("submission_url");
            JSONArray banksArray = netbankingOptions.getJSONArray("choices");
            HashMap<String, String> banks = new HashMap<>();
            JSONObject bank;
            for (int i = 0; i < banksArray.length(); i++) {
                bank = banksArray.getJSONObject(i);
                banks.put(bank.getString("name"), bank.getString("id"));
            }
            order.setNetBankingOptions(new NetBankingOptions(nbURL, banks));
        }

        if (paymentOptionsObject.has("emi_options") && !paymentOptionsObject.isNull("emi_options")){
            JSONObject emiOptionsRaw = paymentOptionsObject.getJSONObject("emi_options");
            JSONArray emiListRaw = emiOptionsRaw.getJSONArray("emi_list");
            EMIOption emiOption;
            JSONObject emiOptionRaw;
            JSONArray ratesRaw;
            JSONObject rateRaw;
            ArrayList<EMIOption> emis = new ArrayList<>();
            for(int i=0; i<emiListRaw.length(); i++){
                emiOptionRaw = emiListRaw.getJSONObject(i);
                String bankName = emiOptionRaw.getString("bank_name");
                String bankCode = emiOptionRaw.getString("bank_code");
                HashMap<Integer, Integer> rates = new HashMap<>();
                ratesRaw = emiOptionRaw.getJSONArray("rates");
                for(int j=0; j<ratesRaw.length(); j++){
                    rateRaw = ratesRaw.getJSONObject(j);
                    int tenure = rateRaw.getInt("tenure");
                    int interest = rateRaw.getInt("interest");
                    rates.put(tenure, interest);
                }
                emiOption = new EMIOption(bankName, bankCode, rates);
                emis.add(emiOption);
            }
            String url = emiOptionsRaw.getString("submission_url");
            JSONObject submissionData = emiOptionsRaw.getJSONObject("submission_data");
            String merchantID = submissionData.getString("merchant_id");
            String orderID = submissionData.getString("order_id");
            order.setEmiOptions(new EMIOptions(merchantID, orderID, url, emis));
        }
    }

    private String getUserAgent() {
        return "Instamojo Android SDK;" + Build.MODEL + ";" + Build.BRAND + ";" + Build.VERSION.SDK_INT
                + ";" + BuildConfig.APPLICATION_ID + ";" + BuildConfig.VERSION_NAME + ";" + BuildConfig.VERSION_CODE;
    }

}
