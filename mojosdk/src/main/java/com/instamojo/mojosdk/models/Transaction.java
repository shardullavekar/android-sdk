package com.instamojo.mojosdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Transaction Class to hold the details of a Transaction.
 *
 *
 * @author vedhavyas
 * @version 1.0
 * @since 14/03/16
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
    private String buyerName;
    private String buyerEmail;
    private String buyerPhone;
    private String amount;
    private String purpose;
    private String currency;
    private String webHook;
    private String mode;
    private String authToken;
    private String resourceURI;
    private DebitCardOptions debitCardOptions;
    private NetBankingOptions netBankingOptions;

    /**
     * Transaction model with all the Mandatory Parameters passed.
     *
     * @param buyerName Name of the buyer.
     * @param buyerEmail Email of the buyer.
     * @param buyerPhone Phone number of the buyer.
     * @param amount Transaction amount.
     * @param purpose Transaction purpose.
     * @param authToken App access token generated using client id and secret.
     *
     */
    public Transaction(@NonNull String buyerName, @NonNull String buyerEmail, @NonNull String buyerPhone,
                       @NonNull String amount, @NonNull String purpose,
                       @NonNull String authToken) {
        this.buyerName = buyerName;
        this.buyerEmail = buyerEmail;
        this.buyerPhone = buyerPhone;
        this.amount = amount;
        this.purpose = purpose;
        this.currency = "INR";
        this.mode = "Android_SDK";
        this.authToken = authToken;
    }

    @SuppressWarnings("unchecked")
    protected Transaction(Parcel in) {
        id = in.readString();
        buyerName = in.readString();
        buyerEmail = in.readString();
        buyerPhone = in.readString();
        amount = in.readString();
        purpose = in.readString();
        currency = in.readString();
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
        dest.writeString(buyerName);
        dest.writeString(buyerEmail);
        dest.writeString(buyerPhone);
        dest.writeString(amount);
        dest.writeString(purpose);
        dest.writeString(currency);
        dest.writeString(mode);
        dest.writeString(webHook);
        dest.writeString(authToken);
        dest.writeString(resourceURI);
        dest.writeParcelable(debitCardOptions, flags);
        dest.writeParcelable(netBankingOptions, flags);
    }

    /**
     * @return buyer name if available else null.
     */
    public String getBuyerName() {
        return buyerName;
    }

    /**
     * @param buyerName Buyer Name for this transaction. Must not be null.
     */
    public void setBuyerName(@NonNull String buyerName) {
        this.buyerName = buyerName;
    }

    /**
     * @return buyer email if available else null.
     */
    public String getBuyerEmail() {
        return buyerEmail;
    }

    /**
     * @param buyerEmail Email of the buyer for this transaction. Must not be null.
     */
    public void setBuyerEmail(@NonNull String buyerEmail) {
        this.buyerEmail = buyerEmail;
    }

    /**
     * @return buyer phone for this transaction.
     */
    public String getBuyerPhone() {
        return buyerPhone;
    }

    /**
     * @param buyerPhone Phone number of the buyer for this transaction. Must not be null.
     */
    public void setBuyerPhone(@NonNull String buyerPhone) {
        this.buyerPhone = buyerPhone;
    }

    /**
     * @return transaction amount if available else null.
     */
    public String getAmount() {
        return amount;
    }

    /**
     * @param amount Transaction amount for this Transaction. Must not be null.
     */
    public void setAmount(@NonNull String amount) {
        this.amount = amount;
    }

    /**
     * @return Purpose of the Transaction if available else null.
     */
    public String getPurpose() {
        return purpose;
    }

    /**
     * @param purpose Purpose of this transaction. Must not be null.
     */
    public void setPurpose(@NonNull String purpose) {
        this.purpose = purpose;
    }

    /**
     * @return type of currency. Ex: INR(Default).
     */
    public String getCurrency() {
        return currency;
    }

    /**
     * @return Mode of the Transaction.
     */
    public String getMode() {
        return mode;
    }

    /**
     * @return webhook for this transaction.
     */
    public String getWebHook() {
        return webHook;
    }

    /**
     * @param webHook Weeb hook for this transaction. Will be redirected to this URL
     *                after payment. Should not be called unless You know what you are doing.
     */
    public void setWebHook(@NonNull String webHook) {
        this.webHook = webHook;
    }

    /**
     * @return authToken generated for this URL.
     */
    public String getAuthToken() {
        return authToken;
    }

    /**
     * @param authToken Auth token generated using clients secret keys.
     */
    public void setAuthToken(@NonNull String authToken) {
        this.authToken = authToken;
    }

    /**
     * @return Transaction ID if available else null.
     */
    public String getId() {
        return id;
    }

    /**
     * @param id Order ID of this transaction. Must not be null.
     */
    public void setId(@NonNull String id) {
        this.id = id;
    }

    /**
     * @return resourceURI of the Seller.
     */
    public String getResourceURI() {
        return resourceURI;
    }

    /**
     * @param resourceURI resource URI of the seller. Must not be null.
     */
    public void setResourceURI(@NonNull String resourceURI) {
        this.resourceURI = resourceURI;
    }

    /**
     * @return {@link DebitCardOptions} if available. Else null.
     */
    public DebitCardOptions getDebitCardOptions() {
        return debitCardOptions;
    }

    /**
     * @param debitCardOptions Debit card options for this transaction. Can be null.
     */
    public void setDebitCardOptions(DebitCardOptions debitCardOptions) {
        this.debitCardOptions = debitCardOptions;
    }

    /**
     * @return {@link NetBankingOptions} if available. Else null.
     */
    public NetBankingOptions getNetBankingOptions() {
        return netBankingOptions;
    }

    /**
     * @param netBankingOptions Netbanking options for this Transaction. Can be null.
     */
    public void setNetBankingOptions(NetBankingOptions netBankingOptions) {
        this.netBankingOptions = netBankingOptions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

}