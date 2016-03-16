package com.instamojo.mojosdk.fragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.instamojo.mojosdk.R;
import com.instamojo.mojosdk.activities.PaymentActivity;


public class JusPayBrowser extends Fragment {

    private static final String content = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title></title>\n" +
            "</head>\n" +
            "<body>\n" +
            "<script>MojoScriptInterface.onTransactionComplete(\"12345\", \"Success\")</script>\n" +
            "</body>\n" +
            "</html>";
    private WebView webView;

    public JusPayBrowser() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_juspay_browser, container, false);
        inflateXML(view);
        return view;
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void inflateXML(View view) {
        webView = (WebView) view.findViewById(R.id.web_view);
        webView.setWebViewClient(new WebViewClient());
        webView.addJavascriptInterface(new JavaScriptInterface(), "MojoScriptInterface");
        webView.getSettings().setJavaScriptEnabled(true);
        final ProgressDialog dialog = ProgressDialog.show(getActivity(), "", "Processing...", true, false);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                webView.loadData(content, "text/html; charset=UTF-8;", null);
            }
        }, 3000);
    }

    private class JavaScriptInterface {

        public JavaScriptInterface() {
        }

        @android.webkit.JavascriptInterface
        public void onTransactionComplete(String paymentID, String status) {
            //// TODO: 15/03/16 do a check here for the payment success or failure
            Intent intent = new Intent();
            intent.putExtra(PaymentActivity.ORDER_ID, paymentID);
            intent.putExtra(PaymentActivity.TRANSACTION_STATUS, status);
            ((PaymentActivity) getActivity()).returnResult(intent, Activity.RESULT_OK);
        }
    }
}