package com.instamojo.mojosdk.models;

import android.support.annotation.NonNull;

import java.util.HashMap;

/**
 * Authored by vedhavyas on 11/03/16.
 */

public class Transaction {

    private static final String URL = "url";
    private static final String MERCHANT_ID = "merchant_id";
    private String orderID;
    private String name;
    private String email;
    private String phone;
    private String amount;
    private String purpose;
    private String authToken;
    private HashMap<String, String> extraParams = new HashMap<>();
    private HashMap<String, String> debitCardOptions;
    private HashMap<String, String> netBankingOptions;

    /**
     * Transaction model with all the Mandatory Parameters passed
     *
     * @param name      - Name of the buyer
     * @param email     - Email of the buyer
     * @param phone     - Phone number of the buyer
     * @param amount    - Transaction amount
     * @param purpose   - Transaction purpose
     * @param authToken - Auth token generated using Private Auth
     */
    public Transaction(@NonNull String name, @NonNull String email, @NonNull String phone,
                       @NonNull String amount, @NonNull String purpose, @NonNull String authToken) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.amount = amount;
        this.purpose = purpose;
        this.authToken = authToken;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    /**
     * Extra params that needs to be sent along with MojoRequest
     *
     * @param key   - Parameter key
     * @param value - Parameter value
     */
    public void setExtraParam(String key, String value) {
        this.extraParams.put(key, value);
    }

    public String getExtraParam(String key) {
        return this.extraParams.get(key);
    }

    public void setDebitCardOptions(HashMap<String, String> debitCardOptions) {
        this.debitCardOptions = debitCardOptions;
    }

    public void setNetBankingOptions(HashMap<String, String> netBankingOptions) {
        this.netBankingOptions = netBankingOptions;
    }

    /**
     * Check if the current transaction has Debit card Payment Option
     *
     * @return true if available else false
     */
    public boolean isDebitCardAvailable() {
        return this.debitCardOptions != null;
    }

    /**
     * Check if the current transaction has Netbanking Payment option
     *
     * @return true if available else false
     */
    public boolean isNetBankingAvailable() {
        return this.netBankingOptions != null;
    }

    /**
     * Return merchant ID of the transaction. Should check {@link #isDebitCardAvailable()} before proceeding
     *
     * @return merchant ID if debit option is available else {@code null}
     */
    public String getMerchantId() {
        return this.debitCardOptions.get(MERCHANT_ID);
    }

    /**
     * Return payment url of the transaction. Should check {@link #isDebitCardAvailable()} before proceeding
     *
     * @return payment url if debit option is available else {@code null}
     */
    public String getDebitCardUrl() {
        return this.debitCardOptions.get(URL);
    }

    //// TODO: 11/03/16 Implement netbanking methods
}
