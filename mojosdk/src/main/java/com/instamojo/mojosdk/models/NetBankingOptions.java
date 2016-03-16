package com.instamojo.mojosdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.HashMap;

/**
 * Authored by vedhavyas on 15/03/16.
 */
public class NetBankingOptions implements Parcelable {
    @SuppressWarnings("unused")
    public static final Parcelable.Creator<NetBankingOptions> CREATOR = new Parcelable.Creator<NetBankingOptions>() {
        @Override
        public NetBankingOptions createFromParcel(Parcel in) {
            return new NetBankingOptions(in);
        }

        @Override
        public NetBankingOptions[] newArray(int size) {
            return new NetBankingOptions[size];
        }
    };
    private String url;
    private HashMap<String, String> banks;

    public NetBankingOptions(@NonNull String url, @NonNull HashMap<String, String> banks) {
        this.url = url;
        this.banks = banks;
    }

    @SuppressWarnings("unchecked")
    protected NetBankingOptions(Parcel in) {
        url = in.readString();
        int size = in.readInt();
        if (size == 0) {
            return;
        }

        banks = new HashMap<>();
        for (int i = 0; i < size; i++) {
            String key = in.readString();
            String value = in.readString();
            banks.put(key, value);
        }
    }

    public String getUrl() {
        return url;
    }

    public HashMap<String, String> getBanks() {
        return banks;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        if (banks == null) {
            return;
        }
        dest.writeInt(banks.size());
        for (HashMap.Entry<String, String> entry : banks.entrySet()) {
            dest.writeString(entry.getKey());
            dest.writeString(entry.getValue());
        }
    }
}
