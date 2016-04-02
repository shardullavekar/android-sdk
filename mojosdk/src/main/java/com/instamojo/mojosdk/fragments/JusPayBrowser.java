package com.instamojo.mojosdk.fragments;

/**
 * Authored by vedhavyas on 02/04/16.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.instamojo.mojosdk.activities.PaymentActivity;
import com.instamojo.mojosdk.network.JavaScriptInterface;

import in.juspay.godel.ui.JuspayBrowserFragment;

public class JusPayBrowser extends JuspayBrowserFragment {

    public JusPayBrowser() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadWebView();
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void loadWebView() {
        setupJuspayBackButtonCallbackInterface(new JuspayBackButtonCallback() {
            @Override
            public void transactionCancelled() {
                ((PaymentActivity) getActivity()).returnResult(Activity.RESULT_CANCELED);
            }
        });
        getWebView().addJavascriptInterface(new JavaScriptInterface(getActivity()), "AndroidScriptInterface");
        getWebView().getSettings().setJavaScriptEnabled(true);
    }
}
