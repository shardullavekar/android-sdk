package com.instamojo.android.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.instamojo.android.activities.PaymentActivity;
import com.instamojo.android.helpers.Logger;
import com.instamojo.android.network.JavaScriptInterface;

import org.json.JSONException;
import org.json.JSONObject;

import in.juspay.godel.ui.JuspayBrowserFragment;

/**
 * JuspaySafe Brwoser Fragment extending {@link in.juspay.godel.ui.JuspayBrowserFragment}.
 */
public class JusPaySafeBrowser extends JuspayBrowserFragment {

    /**
     * Creates new instance of Fragment.
     */
    public JusPaySafeBrowser() {
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

            }

            @Override
            public void transactionCancelled(JSONObject jsonObject) throws JSONException {
                ((PaymentActivity) getActivity()).returnResult(Activity.RESULT_CANCELED);
            }
        });
        getWebView().addJavascriptInterface(new JavaScriptInterface(getActivity()), "AndroidScriptInterface");
        getWebView().getSettings().setJavaScriptEnabled(true);
        Logger.logDebug(this.getClass().getSimpleName(), "Loaded Webview");
    }
}
