package com.instamojo.android.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * UPIOptions Class to hold the details of a UPISubmission options.
 *
 *
 * @author vedhavyas
 * @version 1.2.4
 * @since 20/10/16
 */

public class UPIOptions implements Parcelable {

    private String url;

    public UPIOptions(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    protected UPIOptions(Parcel in) {
        url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(url);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<UPIOptions> CREATOR = new Parcelable.Creator<UPIOptions>() {
        @Override
        public UPIOptions createFromParcel(Parcel in) {
            return new UPIOptions(in);
        }

        @Override
        public UPIOptions[] newArray(int size) {
            return new UPIOptions[size];
        }
    };
}