package com.instamojo.mojosdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Authored by vedhavyas on 11/03/16.
 */

public class Transaction implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Transaction> CREATOR = new Parcelable.Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    private String id;
    private String paymentRequestID;
    private String buyerName;
    private String buyerEmail;
    private String buyerPhone;
    private String amount;
    private String purpose;
    private String currency;
    private String sellerID;
    private String webHook;
    private String mode;
    private String authToken;
    private String resourceURI;
    private DebitCardOptions debitCardOptions;
    private NetBankingOptions netBankingOptions;

    /**
     * Transaction model with all the Mandatory Parameters passed
     *
     * @param buyerName      - Name of the buyer
     * @param buyerEmail     - Email of the buyer
     * @param buyerPhone     - Phone number of the buyer
     * @param amount    - Transaction amount
     * @param purpose   - Transaction purpose
     * @param sellerID - seller id
     * @param authToken - App access token generated using client id and secret
     *
     */
    public Transaction(@NonNull String buyerName, @NonNull String buyerEmail, @NonNull String buyerPhone,
                       @NonNull String amount, @NonNull String purpose, @NonNull String sellerID,
                       @NonNull String authToken) {
        this.buyerName = buyerName;
        this.buyerEmail = buyerEmail;
        this.buyerPhone = buyerPhone;
        this.amount = amount;
        this.purpose = purpose;
        this.sellerID = sellerID;
        this.currency = "INR";
        this.mode = "Android_SDK";
        this.authToken = authToken;
    }

    @SuppressWarnings("unchecked")
    protected Transaction(Parcel in) {
        id = in.readString();
        paymentRequestID = in.readString();
        buyerName = in.readString();
        buyerEmail = in.readString();
        buyerPhone = in.readString();
        amount = in.readString();
        purpose = in.readString();
        currency = in.readString();
        sellerID = in.readString();
        mode = in.readString();
        webHook = in.readString();
        authToken = in.readString();
        resourceURI = in.readString();
        debitCardOptions = in.readParcelable(DebitCardOptions.class.getClassLoader());
        netBankingOptions = in.readParcelable(NetBankingOptions.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(paymentRequestID);
        dest.writeString(buyerName);
        dest.writeString(buyerEmail);
        dest.writeString(buyerPhone);
        dest.writeString(amount);
        dest.writeString(purpose);
        dest.writeString(currency);
        dest.writeString(sellerID);
        dest.writeString(mode);
        dest.writeString(webHook);
        dest.writeString(authToken);
        dest.writeString(resourceURI);
        dest.writeParcelable(debitCardOptions, flags);
        dest.writeParcelable(netBankingOptions, flags);
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(@NonNull String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerEmail() {
        return buyerEmail;
    }

    public void setBuyerEmail(@NonNull String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    public String getBuyerPhone() {
        return buyerPhone;
    }

    public void setBuyerPhone(@NonNull String buyerPhone) {
        this.buyerPhone = buyerPhone;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(@NonNull String amount) {
        this.amount = amount;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(@NonNull String purpose) {
        this.purpose = purpose;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(@NonNull String currency) {
        this.currency = currency;
    }

    public String getSellerID() {
        return sellerID;
    }

    public void setSellerID(@NonNull String sellerID) {
        this.sellerID = sellerID;
    }

    public String getMode() {
        return mode;
    }

    public String getWebHook() {
        return webHook;
    }

    public void setWebHook(@NonNull String webHook) {
        this.webHook = webHook;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(@NonNull String authToken) {
        this.authToken = authToken;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPaymentRequestID() {
        return paymentRequestID;
    }

    public void setPaymentRequestID(String paymentRequestID) {
        this.paymentRequestID = paymentRequestID;
    }

    public String getResourceURI() {
        return resourceURI;
    }

    public void setResourceURI(String resourceURI) {
        this.resourceURI = resourceURI;
    }

    public DebitCardOptions getDebitCardOptions() {
        return debitCardOptions;
    }

    public void setDebitCardOptions(DebitCardOptions debitCardOptions) {
        this.debitCardOptions = debitCardOptions;
    }

    public NetBankingOptions getNetBankingOptions() {
        return netBankingOptions;
    }

    public void setNetBankingOptions(NetBankingOptions netBankingOptions) {
        this.netBankingOptions = netBankingOptions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

}