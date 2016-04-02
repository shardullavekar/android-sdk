package com.instamojo.mojosdk.fragments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.instamojo.mojosdk.R;
import com.instamojo.mojosdk.network.JavaScriptInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class NetBankingBrowser extends Fragment {

    public static final String URL = "url";
    public static final String BANK_CODE = "bank_code";
    public static final String TOKEN = "token";
    private WebView webView;

    public NetBankingBrowser() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_net_banking_browser, container, false);
        webView = (WebView) view.findViewById(R.id.web_view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadWebView();
    }

    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})
    private void loadWebView() {
        Bundle bundle = getArguments();
        String url = bundle.getString(URL);
        String token = bundle.getString(TOKEN);
        String bankCode = bundle.getString(BANK_CODE);
        String postData = "access_token=" + token + "&bank_code=" + bankCode;
        webView.addJavascriptInterface(new JavaScriptInterface(getActivity()), "AndroidScriptInterface");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.postUrl(url, postData.getBytes());
    }

    private class WebViewClient extends android.webkit.WebViewClient {

        private ProgressDialog progressDialog;

        public WebViewClient() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setIndeterminate(true);
            progressDialog.setCancelable(false);
            progressDialog.setMessage("loading...");
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            progressDialog.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            progressDialog.dismiss();
        }
    }
}
