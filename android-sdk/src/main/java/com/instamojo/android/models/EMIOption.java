package com.instamojo.android.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

/**
 * CardOptions object that holds the Card transaction information received from Mojo Server
 * for a particular order.
 *
 *
 * @author vedhavyas
 * @version 1.1
 * @since 12/07/16
 */

public class EMIOption implements Parcelable {
    private String bankName;
    private String bankCode;
    private HashMap<Integer, Integer> rates;

    public EMIOption(String bankName, String bankCode, HashMap<Integer, Integer> rates) {
        this.bankName = bankName;
        this.bankCode = bankCode;
        this.rates = rates;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public HashMap<Integer, Integer> getRates() {
        return rates;
    }

    public void setRates(HashMap<Integer, Integer> rates) {
        this.rates = rates;
    }

    protected EMIOption(Parcel in) {
        bankName = in.readString();
        bankCode = in.readString();
        int ratesSize = in.readInt();
        if (ratesSize == 0){
            return;
        }
        rates = new HashMap<>();
        for (int i=0; i< ratesSize; i++){
            int tenure = in.readInt();
            int interest = in.readInt();
            rates.put(tenure, interest);
        }

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bankName);
        dest.writeString(bankCode);
        dest.writeInt(rates.size());
        for (Map.Entry<Integer, Integer> entry : rates.entrySet()){
            dest.writeInt(entry.getKey());
            dest.writeInt(entry.getValue());
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<EMIOption> CREATOR = new Parcelable.Creator<EMIOption>() {
        @Override
        public EMIOption createFromParcel(Parcel in) {
            return new EMIOption(in);
        }

        @Override
        public EMIOption[] newArray(int size) {
            return new EMIOption[size];
        }
    };
}