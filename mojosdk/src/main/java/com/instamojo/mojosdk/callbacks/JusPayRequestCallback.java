package com.instamojo.mojosdk.callbacks;

import android.os.Bundle;

/**
 * Authored by vedhavyas on 15/03/16.
 */
public interface JusPayRequestCallback {
    void onError(Exception e);

    void onSuccess(Bundle args);
}
