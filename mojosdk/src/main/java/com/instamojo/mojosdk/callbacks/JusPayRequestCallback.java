package com.instamojo.mojosdk.callbacks;

import android.os.Bundle;

/**
 * Authored by vedhavyas on 15/03/16.
 */
public interface JusPayRequestCallback {
    void onFinish(Bundle bundle, Exception error);
}
