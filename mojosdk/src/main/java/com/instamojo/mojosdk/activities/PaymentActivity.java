package com.instamojo.mojosdk.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.instamojo.mojosdk.R;
import com.instamojo.mojosdk.fragments.JusPayBrowser;
import com.instamojo.mojosdk.fragments.NetBankingBrowser;

public class PaymentActivity extends BaseActivity {

    public static final String PAYMENT_BUNDLE = "payment_bundle";
    public static final String ORDER_ID = "order_id";
    public static final String TRANSACTION_STATUS = "transaction_status";
    public static final String METHOD = "method";
    private Fragment currentFragment;

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
        Method method = (Method) getIntent().getSerializableExtra(METHOD);
        Bundle sourceArgs = getIntent().getBundleExtra(PAYMENT_BUNDLE);
        if (sourceArgs == null) {
            returnResult(RESULT_CANCELED);
            return;
        }

        Fragment fragment;
        if (method.equals(Method.Juspay)) {
            fragment = new JusPayBrowser();
        } else {
            fragment = new NetBankingBrowser();
        }
        fragment.setArguments(sourceArgs);
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        currentFragment = fragment;
    }

    @Override
    public void onBackPressed() {
        if (currentFragment instanceof JusPayBrowser) {
            ((JusPayBrowser) currentFragment).juspayBackPressedHandler(true);
        } else {
            returnResult(RESULT_CANCELED);
        }
    }

    public enum Method {
        Juspay,
        NetBanking
    }
}
