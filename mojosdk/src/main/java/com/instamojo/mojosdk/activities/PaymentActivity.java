package com.instamojo.mojosdk.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.instamojo.mojosdk.R;
import com.instamojo.mojosdk.fragments.JusPayBrowser;

public class PaymentActivity extends BaseActivity {

    public static final String PAYMENT_BUNDLE = "payment_bundle";
    public static final String ORDER_ID = "order_id";
    public static final String TRANSACTION_STATUS = "transaction_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        inflateXML();
        showFragment();
    }


    private void inflateXML() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void showFragment() {
        Bundle args = getIntent().getBundleExtra(PAYMENT_BUNDLE);
        if (args == null) {
            returnResult(RESULT_CANCELED);
        }

        JusPayBrowser jusPayBrowser = new JusPayBrowser();
        jusPayBrowser.setArguments(args);
        getSupportFragmentManager().beginTransaction().add(R.id.container, jusPayBrowser).commit();

    }
}
