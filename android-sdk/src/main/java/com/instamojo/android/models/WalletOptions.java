package com.instamojo.android.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;

/**
 * Authored by vedhavyas on 25/08/16.
 */

public class WalletOptions implements Parcelable {
    private String url;
    private ArrayList<Wallet> wallets = new ArrayList<>();

    public WalletOptions(String url, ArrayList<Wallet> wallets) {
        this.url = url;
        this.wallets = wallets;
    }

    public String getUrl() {
        return url;
    }

    public ArrayList<Wallet> getWallets() {
        return wallets;
    }

    /**
     * PostData to be posted with Netbanking URl.
     *
     * @param authToken AuthToken retrieved using Dev's Client auth keys.
     *                  Should not use these keys in Application.
     * @param walletID  Wallet ID of the wallet user selected.
     * @return string with form query format.
     */
    public String getPostData(@NonNull String authToken, @NonNull String walletID) {
        return "access_token=" + authToken + "&wallet_id=" + walletID;
    }

    protected WalletOptions(Parcel in) {
        url = in.readString();
        int walletsSize = in.readInt();
        if (walletsSize == 0){
            return;
        }
        wallets = new ArrayList<>();
        for (int i=0; i<walletsSize; i++){
            wallets.add((Wallet) in.readParcelable(Wallet.class.getClassLoader()));
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
        dest.writeInt(wallets.size());
        for (Wallet wallet : wallets){
            dest.writeParcelable(wallet, flags);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<WalletOptions> CREATOR = new Parcelable.Creator<WalletOptions>() {
        @Override
        public WalletOptions createFromParcel(Parcel in) {
            return new WalletOptions(in);
        }

        @Override
        public WalletOptions[] newArray(int size) {
            return new WalletOptions[size];
        }
    };
}