package com.instamojo.android.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.instamojo.android.R;
import com.instamojo.android.fragments.JusPaySafeBrowser;

/**
 * Activity subclass extending {@link BaseActivity}. Activity for {@link JusPaySafeBrowser} fragment.
 *
 * @author vedhavyas
 * @version 1.0
 * @since 14/03/16
 */
public class PaymentActivity extends BaseActivity {

    /**
     * Extra Bundle key passed to JuspaySafe browser.
     */
    public static final String PAYMENT_BUNDLE = "payment_bundle";

    /**
     * Extra Bundle key for Order ID which is passed back from SDK.
     */
    public static final String ORDER_ID = "order_id";

    /**
     * Extra Bundle key for Transaction Status which is passed back from SDK.
     */
    public static final String TRANSACTION_STATUS = "transaction_status";

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
        Bundle sourceArgs = getIntent().getBundleExtra(PAYMENT_BUNDLE);
        if (sourceArgs == null) {
            returnResult(RESULT_CANCELED);
            return;
        }

        JusPaySafeBrowser fragment = new JusPaySafeBrowser();
        fragment.setArguments(sourceArgs);
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        currentFragment = fragment;
    }

    @Override
    public void onBackPressed() {
        if (currentFragment instanceof JusPaySafeBrowser) {
            ((JusPaySafeBrowser) currentFragment).juspayBackPressedHandler(true);
        } else {
            returnResult(RESULT_CANCELED);
        }
    }
}
