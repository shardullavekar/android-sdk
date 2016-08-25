package com.instamojo.android.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Authored by vedhavyas on 25/08/16.
 */

public class Wallet implements Parcelable {
    private String name;
    private String imageURL;
    private String walletID;

    public Wallet(String name, String imageURL, String walletID) {
        this.name = name;
        this.imageURL = imageURL;
        this.walletID = walletID;
    }

    public String getName() {
        return name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getWalletID() {
        return walletID;
    }

    protected Wallet(Parcel in) {
        name = in.readString();
        imageURL = in.readString();
        walletID = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(imageURL);
        dest.writeString(walletID);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Wallet> CREATOR = new Parcelable.Creator<Wallet>() {
        @Override
        public Wallet createFromParcel(Parcel in) {
            return new Wallet(in);
        }

        @Override
        public Wallet[] newArray(int size) {
            return new Wallet[size];
        }
    };
}
