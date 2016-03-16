package com.instamojo.mojosdk.network;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.instamojo.mojosdk.callbacks.JusPayRequestCallback;
import com.instamojo.mojosdk.callbacks.MojoRequestCallBack;
import com.instamojo.mojosdk.models.Card;
import com.instamojo.mojosdk.models.DebitCardOptions;
import com.instamojo.mojosdk.models.NetBankingOptions;
import com.instamojo.mojosdk.models.Transaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * Authored by vedhavyas on 11/03/16.
 */

public class Request {

    private static final String AUTH_TOKEN = "X-AUTH-TOKEN";
    private static final String NAME = "name";
    private static final String EMAIL = "email";
    private static final String PURPOSE = "purpose";
    private static final String PHONE = "phone";
    private static final String AMOUNT = "amount";

    private Transaction transaction;
    private MojoRequestCallBack mojoRequestCallBack;
    private String authToken;
    private JusPayRequestCallback jusPayRequestCallback;
    private Card card;

    /**
     * Network Request to retrieve the necessary data from Server
     *
     * @param transaction         - Transaction model with all the mandatory fields set
     * @param authToken           - Authentication token generated using private keys
     * @param mojoRequestCallBack - Callback interface for the Asynchronous Network Call
     */
    public Request(@NonNull Transaction transaction, @NonNull String authToken, @NonNull MojoRequestCallBack mojoRequestCallBack) {
        this.transaction = transaction;
        this.authToken = authToken;
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

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(transaction.getDebitCardOptions().getUrl())
                .post(body)
                .build();

        //// TODO: 15/03/16 make network call here
        String mockData = "{\n" +
                "  \"order_id\": \"M2090913801\",\n" +
                "  \"txn_id\": \"test_instamojo-M2090913801-1\",\n" +
                "  \"status\": \"PENDING_VBV\",\n" +
                "  \"payment\": {\n" +
                "    \"authentication\": {\n" +
                "      \"method\": \"GET\",\n" +
                "      \"url\": \"https:\\/\\/api.juspay.in\\/vbv\\/mc_secure?merchantId=test_instamojo&pan=4242424242424242&expirydate=1025&purchaseAmount=1200&displayAmount=INR 12.0&currencyVal=356&exponent=2&txnId=test_instamojo-M2090913801-1&shoppingContext=test_instamojo-M2090913801-1&recur_freq=&recur_end=&installment=&cardNumber=4242424242424242&orderId=M2090913801\"\n" +
                "    }\n" +
                "  }\n" +
                "}";

        try {
            Bundle args = parseJusPayResponse(mockData);
            jusPayRequestCallback.onSuccess(args);
        } catch (JSONException e) {
            jusPayRequestCallback.onError(e);
        }

    }

    private void executeMojoRequest() {
        OkHttpClient client = new OkHttpClient();

        RequestBody body = new FormBody.Builder()
                .add(NAME, transaction.getName())
                .add(EMAIL, transaction.getEmail())
                .add(PURPOSE, transaction.getPurpose())
                .add(PHONE, transaction.getPhone())
                .add(AMOUNT, transaction.getAmount())
                .build();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(Urls.MOJO_TRANSACTION_INIT_URL)
                .addHeader(AUTH_TOKEN, authToken)
                .post(body)
                .build();

        String mockData = "{\n" +
                "  \"payment\": {\n" +
                "    \"id\": \"M82104299\",\n" +
                "    \"amount\": \"10.00\",\n" +
                "    \"status\": \"PEN\",\n" +
                "    \"purpose\": \"Test purpose\",\n" +
                "    \"phone\": \"+919930684816\",\n" +
                "    \"name\": \"Vedhavyas\"\n" +
                "  },\n" +
                "  \"payment_options\": {\n" +
                "    \"debit_card\": {\n" +
                "      \"url\": \"https:\\/\\/api.juspay.in\\/txns\",\n" +
                "      \"merchant_id\": \"test_instamojo\",\n" +
                "      \"order_id\": \"M8210429901\"\n" +
                "    },\n" +
                "    \"net_banking\":{\n" +
                "\t\"url\" : \"urlhere\",\n" +
                "\t\"banks\":[\n" +
                "\t\t{ \n" +
                "\t\t     \"code\":\"ICICI\",\n" +
                "\t\t     \"name\":\"ICICI\"\n" +
                "\t\t},\n" +
                "\t\t{ \n" +
                "\t\t     \"code\":\"HDFC\",\n" +
                "\t\t     \"name\":\"HDFC\"\n" +
                "\t\t},\n" +
                "\t\t{ \n" +
                "\t\t     \"code\":\"SBI\",\n" +
                "\t\t     \"name\":\"SBI\"\n" +
                "\t\t},\n" +
                "\t\t{ \n" +
                "\t\t     \"code\":\"Axis\",\n" +
                "\t\t     \"name\":\"Axis\"\n" +
                "\t\t},\n" +
                "\t\t{ \n" +
                "\t\t     \"code\":\"PNB\",\n" +
                "\t\t     \"name\":\"PNB\"\n" +
                "\t\t},\n" +
                "\t\t{ \n" +
                "\t\t     \"code\":\"Kotak\",\n" +
                "\t\t     \"name\":\"Kotak\"\n" +
                "\t\t},\n" +
                "\t\t{ \n" +
                "\t\t     \"code\":\"BLAH\",\n" +
                "\t\t     \"name\":\"Blah\"\n" +
                "\t\t}\n" +
                "\t]\n" +
                "    }\n" +
                "  }\n" +
                "}\n";
        //// TODO: 14/03/16 make an actual network call here
        try {
            parseMojoResponse(mockData);
            mojoRequestCallBack.onSuccess(transaction);
        } catch (JSONException e) {
            mojoRequestCallBack.onError(transaction, e);
        }
    }

    private void parseMojoResponse(String responseBody) throws JSONException {
        JSONObject response = new JSONObject(responseBody);
        JSONObject paymentOptions = response.getJSONObject("payment_options");
        if (!paymentOptions.isNull("debit_card")) {
            JSONObject debitCard = paymentOptions.getJSONObject("debit_card");
            String orderID = debitCard.getString("order_id");
            String merchantID = debitCard.getString("merchant_id");
            String url = debitCard.getString("url");
            transaction.setDebitCardOptions(new DebitCardOptions(orderID, merchantID, url));
        }

        if (!paymentOptions.isNull("net_banking")) {
            JSONObject netBanking = paymentOptions.getJSONObject("net_banking");
            String url = netBanking.getString("url");
            JSONArray bankObjects = netBanking.getJSONArray("banks");
            HashMap<String, String> banks = new HashMap<>();
            for (int i = 0; i < bankObjects.length(); i++) {
                JSONObject bank = bankObjects.getJSONObject(i);
                banks.put(bank.getString("code"), bank.getString("name"));
            }
            transaction.setNetBankingOptions(new NetBankingOptions(url, banks));
        }
    }

    private Bundle parseJusPayResponse(String result) throws JSONException {
        JSONObject response = new JSONObject(result);
        String url = response.getJSONObject("payment").getJSONObject("authentication").getString("url");
        final Bundle args = new Bundle();
        args.putString("url", url);
        args.putString("merchantId", transaction.getDebitCardOptions().getMerchantID());
        args.putString("orderId", transaction.getDebitCardOptions().getOrderID());
        return args;
    }

}
