package com.instamojo.android.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * EMIOptions object that holds an array {@link EMIOption} and order details
 *
 * @author vedhavyas
 * @version 1.1
 * @since 12/07/16
 */
public class EMIOptions implements Parcelable {

    private String merchantID;
    private String orderID;
    private String url;
    private ArrayList<EMIOption> emis;

    public EMIOptions(String merchantID, String orderID, String url, ArrayList<EMIOption> emis) {
        this.merchantID = merchantID;
        this.orderID = orderID;
        this.url = url;
        this.emis = emis;
    }

    public String getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(String merchantID) {
        this.merchantID = merchantID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ArrayList<EMIOption> getEmis() {
        return emis;
    }

    public void setEmis(ArrayList<EMIOption> emis) {
        this.emis = emis;
    }

    protected EMIOptions(Parcel in) {
        merchantID = in.readString();
        orderID = in.readString();
        url = in.readString();
        int emiOptionsSize = in.readInt();
        if (emiOptionsSize == 0){
            return;
        }

        emis = new ArrayList<>();
        for(int i=0; i<emiOptionsSize; i++){
            emis.add((EMIOption) in.readParcelable(EMIOption.class.getClassLoader()));
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(merchantID);
        dest.writeString(orderID);
        dest.writeString(url);
        dest.writeInt(emis.size());
        for (EMIOption emiOption : emis){
            dest.writeParcelable(emiOption, flags);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<EMIOptions> CREATOR = new Parcelable.Creator<EMIOptions>() {
        @Override
        public EMIOptions createFromParcel(Parcel in) {
            return new EMIOptions(in);
        }

        @Override
        public EMIOptions[] newArray(int size) {
            return new EMIOptions[size];
        }
    };
}