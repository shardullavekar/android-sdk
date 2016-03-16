package com.instamojo.mojosdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.instamojo.mojosdk.helpers.CardValidator;

/**
 * Authored by vedhavyas on 12/10/15.
 */
public class Card implements Parcelable {

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Card> CREATOR = new Parcelable.Creator<Card>() {
        @Override
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        @Override
        public Card[] newArray(int size) {
            return new Card[size];
        }
    };
    private String cardHolderName;
    private String cardNumber;
    private String date;
    private String cvv;

    public Card() {
    }

    protected Card(Parcel in) {
        cardHolderName = in.readString();
        cardNumber = in.readString();
        date = in.readString();
        cvv = in.readString();
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(@NonNull String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(@NonNull String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getDate() {
        return date;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(@NonNull String cvv) {
        this.cvv = cvv;
    }

    public String getMonth() {
        return this.date.split("/")[0];
    }

    public String getYear() {
        return this.date.split("/")[1];
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cardHolderName);
        dest.writeString(cardNumber);
        dest.writeString(date);
        dest.writeString(cvv);
    }

    public boolean isCardNameValid() {
        return this.cardHolderName != null && !this.cardHolderName.isEmpty();
    }

    public boolean isDateValid() {
        if (this.date != null) {
            if (!this.date.isEmpty()) {
                return !CardValidator.isDateExpired(this.date);
            } else {
                return !(isCardNumberValid() && !CardValidator.maestroCard(this.cardNumber));
            }
        } else {
            return !(isCardNumberValid() && !CardValidator.maestroCard(this.cardNumber));
        }


    }

    public boolean isCVVValid() {
        if (this.cvv != null) {
            return !this.cvv.isEmpty() || !(isCardNumberValid() && !CardValidator.maestroCard(this.cardNumber));
        } else {
            return !(isCardNumberValid() && !CardValidator.maestroCard(this.cardNumber));
        }
    }

    public boolean isCardNumberValid() {
        int result;
        if (CardValidator.masterCardWithoutLength(cardNumber)
                || CardValidator.dinnersClubIntWithoutLength(cardNumber)
                || CardValidator.visaCardWithoutLength(cardNumber)
                || CardValidator.amexCardWithoutLength(cardNumber)
                || CardValidator.discoverCardWithoutLength(cardNumber)) {
            result = com.instamojo.mojosdk.helpers.CardValidator.isValid(cardNumber, false);
        } else {
            result = com.instamojo.mojosdk.helpers.CardValidator.isValid(cardNumber, true);
        }
        return result != 0;
    }

    public boolean isCardValid() {
        return isCardNameValid() && isDateValid() && isCVVValid() && isCardNameValid();
    }
}