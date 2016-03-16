package com.instamojo.mojosdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Authored by vedhavyas on 15/03/16.
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

    public String getOrderID() {
        return orderID;
    }

    public String getUrl() {
        return url;
    }

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
