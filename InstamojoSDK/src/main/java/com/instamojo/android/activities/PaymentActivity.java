package com.instamojo.android.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.instamojo.android.R;
import com.instamojo.android.fragments.JusPaySafeBrowser;
import com.instamojo.android.helpers.Logger;

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
        Logger.logDebug(this, this.getClass().getSimpleName(), "Inflated XML");
    }

    private void showFragment() {
        Bundle sourceArgs = getIntent().getBundleExtra(PAYMENT_BUNDLE);
        if (sourceArgs == null) {
            Logger.logError(this.getClass().getSimpleName(), "Payment bundle is Null");
            returnResult(RESULT_CANCELED);
            return;
        }

        JusPaySafeBrowser fragment = new JusPaySafeBrowser();
        fragment.setArguments(sourceArgs);
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        currentFragment = fragment;
        Logger.logDebug(this, this.getClass().getSimpleName(), "Loaded Fragment - " + fragment.getClass().getSimpleName());
    }

    @Override
    public void onBackPressed() {
        if (currentFragment instanceof JusPaySafeBrowser) {
            Logger.logDebug(this, this.getClass().getSimpleName(), "Invoking Juspay Cancel Payment Handler");
            ((JusPaySafeBrowser) currentFragment).juspayBackPressedHandler(true);
        } else {
            returnResult(RESULT_CANCELED);
        }
    }
}
