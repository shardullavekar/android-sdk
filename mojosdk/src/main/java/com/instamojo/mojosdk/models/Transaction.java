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
    private String name;
    private String email;
    private String phone;
    private String amount;
    private String purpose;
    private DebitCardOptions debitCardOptions;
    private NetBankingOptions netBankingOptions;

    /**
     * Transaction model with all the Mandatory Parameters passed
     *
     * @param name      - Name of the buyer
     * @param email     - Email of the buyer
     * @param phone     - Phone number of the buyer
     * @param amount    - Transaction amount
     * @param purpose   - Transaction purpose
     */
    public Transaction(@NonNull String name, @NonNull String email, @NonNull String phone,
                       @NonNull String amount, @NonNull String purpose) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.amount = amount;
        this.purpose = purpose;
    }

    @SuppressWarnings("unchecked")
    protected Transaction(Parcel in) {
        name = in.readString();
        email = in.readString();
        phone = in.readString();
        amount = in.readString();
        purpose = in.readString();
        debitCardOptions = in.readParcelable(DebitCardOptions.class.getClassLoader());
        netBankingOptions = in.readParcelable(NetBankingOptions.class.getClassLoader());
    }

    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(@NonNull String phone) {
        this.phone = phone;
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

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(amount);
        dest.writeString(purpose);
        dest.writeParcelable(debitCardOptions, flags);
        dest.writeParcelable(netBankingOptions, flags);
    }
}