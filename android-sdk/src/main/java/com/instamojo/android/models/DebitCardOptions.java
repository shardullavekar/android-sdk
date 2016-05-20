package com.instamojo.android.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * DebitCardOptions object that holds the Card transaction information received from Mojo Server
 * for a particular order.
 *
 *
 * @author vedhavyas
 * @version 1.0
 * @since 14/03/16
 */
public class DebitCardOptions implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DebitCardOptions> CREATOR = new Parcelable.Creator<DebitCardOptions>() {
        @Override
        public DebitCardOptions createFromParcel(Parcel in) {
            return new DebitCardOptions(in);
        }

        @Override
        public DebitCardOptions[] newArray(int size) {
            return new DebitCardOptions[size];
        }
    };
    private String orderID;
    private String url;
    private String merchantID;

    /**
     * Constructor for DebitCardOptions.
     *
     * @param orderID    Order ID of the Order. Should not be Null.
     * @param merchantID Merchant ID of the transaction. Should not be Null.
     * @param url        Juspay Url to get the final 3D-secure Url.
     */
    public DebitCardOptions(@NonNull String orderID, @NonNull String merchantID, @NonNull String url) {
        this.orderID = orderID;
        this.url = url;
        this.merchantID = merchantID;
    }

    protected DebitCardOptions(Parcel in) {
        orderID = in.readString();
        url = in.readString();
        merchantID = in.readString();
    }

    /**
     * @return orderId of the current transaction.
     */
    public String getOrderID() {
        return orderID;
    }

    /**
     * @return Juspay url for the current transaction.
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return Merchant ID for the current transaction.
     */
    public String getMerchantID() {
        return merchantID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(orderID);
        dest.writeString(url);
        dest.writeString(merchantID);
    }
}
