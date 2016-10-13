package com.instamojo.android.activities;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.instamojo.android.R;
import com.instamojo.android.fragments.JusPaySafeBrowser;
import com.instamojo.android.helpers.Constants;
import com.instamojo.android.helpers.Logger;

import in.juspay.godel.browser.JuspayWebViewClient;
import in.juspay.godel.ui.JuspayBrowserFragment;
import in.juspay.godel.ui.JuspayWebView;

/**
 * Activity subclass extending {@link BaseActivity}. Activity for {@link JusPaySafeBrowser} fragment.
 *
 * @author vedhavyas
 * @version 1.0
 * @since 14/03/16
 */
public class PaymentActivity extends BaseActivity {

    private JuspayBrowserFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_instamojo);
        inflateXML();
        showFragment();
    }

    private void inflateXML() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Logger.logDebug(this.getClass().getSimpleName(), "Inflated XML");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showFragment() {
        final Bundle sourceArgs = getIntent().getBundleExtra(Constants.PAYMENT_BUNDLE);
        if (sourceArgs == null) {
            Logger.logError(this.getClass().getSimpleName(), "Payment bundle is Null");
            returnResult(RESULT_CANCELED);
            return;
        }
        currentFragment = (JuspayBrowserFragment) getSupportFragmentManager().findFragmentById(R.id.juspay_browser_fragment);
        JuspayBrowserFragment.JuspayWebviewCallback juspayWebviewCallback = new JuspayBrowserFragment.JuspayWebviewCallback() {
            public void webviewReady() {
                JuspayWebView juspayWebView = currentFragment.getWebView();
                juspayWebView.setWebViewClient(new JuspayWebViewClient(juspayWebView, currentFragment));
                currentFragment.startPaymentWithArguments(sourceArgs);
            }
        };
        currentFragment.setupJuspayWebviewCallbackInterface(juspayWebviewCallback);
        Logger.logDebug(this.getClass().getSimpleName(), "Loaded Fragment - " + currentFragment.getClass().getSimpleName());
    }

    @Override
    public void onBackPressed() {
        Logger.logDebug(this.getClass().getSimpleName(), "Invoking Juspay Cancel Payment Handler");
        currentFragment.juspayBackPressedHandler(true);
    }
}
