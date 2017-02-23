package com.instamojo.android.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * EMIOptions object that holds an array {@link EMIBank} and order details
 *
 * @author vedhavyas
 * @version 1.1
 * @since 12/07/16
 */
public class EMIOptions implements Parcelable {

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
    private String merchantID;
    private String orderID;
    private String url;
    private ArrayList<EMIBank> emiBanks = new ArrayList<>();
    private String selectedBankCode;
    private int selectedTenure = -1;

    public EMIOptions(String merchantID, String orderID, String url, ArrayList<EMIBank> emiBanks) {
        this.merchantID = merchantID;
        this.orderID = orderID;
        this.url = url;
        this.emiBanks = emiBanks;
    }

    protected EMIOptions(Parcel in) {
        merchantID = in.readString();
        orderID = in.readString();
        url = in.readString();
        selectedBankCode = in.readString();
        selectedTenure = in.readInt();
        int emiBanksSize = in.readInt();
        if (emiBanksSize == 0) {
            return;
        }

        emiBanks = new ArrayList<>();
        for (int i = 0; i < emiBanksSize; i++) {
            emiBanks.add((EMIBank) in.readParcelable(EMIBank.class.getClassLoader()));
        }

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

    public ArrayList<EMIBank> getEmiBanks() {
        return emiBanks;
    }

    public void setEmiBanks(ArrayList<EMIBank> emiBanks) {
        this.emiBanks = emiBanks;
    }

    public String getSelectedBankCode() {
        return selectedBankCode;
    }

    public void setSelectedBankCode(String selectedBankCode) {
        this.selectedBankCode = selectedBankCode;
    }

    public int getSelectedTenure() {
        return selectedTenure;
    }

    public void setSelectedTenure(int selectedTenure) {
        this.selectedTenure = selectedTenure;
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
        dest.writeString(selectedBankCode);
        dest.writeInt(selectedTenure);

        if (emiBanks.size() < 1) {
            dest.writeInt(0);
            return;
        }

        dest.writeInt(emiBanks.size());
        for (EMIBank emiBank : emiBanks) {
            dest.writeParcelable(emiBank, flags);
        }
    }
}