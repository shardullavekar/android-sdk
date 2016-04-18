package com.instamojo.android.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.instamojo.android.R;
import com.instamojo.android.adapters.FormAdapter;
import com.instamojo.android.callbacks.JusPayRequestCallback;
import com.instamojo.android.fragments.DebitCardForm;
import com.instamojo.android.fragments.JusPaySafeBrowser;
import com.instamojo.android.fragments.NetBankingForm;
import com.instamojo.android.models.Card;
import com.instamojo.android.models.NetBankingOptions;
import com.instamojo.android.models.Transaction;
import com.instamojo.android.network.Request;

import java.util.ArrayList;

/**
 * Form activity extending the {@link BaseActivity}. Activity holding {@link DebitCardForm}
 * and {@link NetBankingForm} fragments.
 *
 * @author vedhavyas
 * @version 1.0
 * @since 14/03/16
 */
public class FormActivity extends BaseActivity implements View.OnClickListener {

    /**
     * Extra key for the {@link Transaction} object that is sent through Bundle.
     */
    public static final String TRANSACTION = "transaction";


    private ViewPager pager;
    private View debitIndicator, netBankingIndicator;
    private Transaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        inflateXML();
        loadFragments();
    }

    @Override
    public void onBackPressed() {
        returnResult(RESULT_CANCELED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 9) {
            returnResult(data.getExtras(), resultCode);
        }
    }

    private void inflateXML() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        debitIndicator = findViewById(R.id.debit_indicator);
        netBankingIndicator = findViewById(R.id.net_banking_indicator);
        findViewById(R.id.debit_card).setOnClickListener(this);
        findViewById(R.id.net_banking).setOnClickListener(this);
        pager = (ViewPager) findViewById(R.id.view_pager);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        debitIndicator.setVisibility(View.VISIBLE);
                        netBankingIndicator.setVisibility(View.GONE);
                        break;
                    case 1:
                        hideKeyboard();
                        netBankingIndicator.setVisibility(View.VISIBLE);
                        debitIndicator.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * Request the order url from Juspay using the {@link Request Object}.
     * The response JSON will contain the final url that will be passed on to JuspaySafe Browser for
     * next step.
     *
     * @param debitCardFormFragment Fragment instance used to restore the editbox states in the fragment
     *                              before moving to JuspaySafe.
     * @param card                  Validated Card user object. No futher validations will be done from here and
     *                              assumes proper validation is already done.
     */

    public void checkOutWithCard(final DebitCardForm debitCardFormFragment, Card card) {
        hideKeyboard();
        debitCardFormFragment.changeEditBoxesState(false);
        final ProgressDialog dialog = ProgressDialog.show(this, "", getString(R.string.please_wait), true, false);
        Request request = new Request(transaction, card, new JusPayRequestCallback() {
            @Override
            public void onFinish(Bundle bundle, Exception error) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        debitCardFormFragment.changeEditBoxesState(true);
                    }
                });
                dialog.dismiss();
                if (error != null) {
                    Toast.makeText(FormActivity.this, R.string.error_message_juspay,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                startPaymentActivity(bundle);
            }
        });
        request.execute();
    }

    private void startPaymentActivity(Bundle bundle) {
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtras(getIntent());
        intent.putExtra(PaymentActivity.PAYMENT_BUNDLE, bundle);
        startActivityForResult(intent, 9);
    }

    /**
     * Start the JupaySafe with the Netbanking url and bank code as
     * received when order is generated.
     *
     * @param bankCode Code for the User selected bank.
     *                 Codes list can be retrieved from {@link NetBankingOptions#getBanks()} if not null.
     */

    public void checkOutWithNetBanking(String bankCode) {
        Bundle bundle = new Bundle();
        bundle.putString(JusPaySafeBrowser.URL, transaction.getNetBankingOptions().getUrl());
        bundle.putString(JusPaySafeBrowser.POST_DATA, transaction.
                getNetBankingOptions().getPostData(transaction.getAuthToken(), bankCode));
        startPaymentActivity(bundle);
    }

    private void loadFragments() {
        transaction = getIntent().getParcelableExtra(TRANSACTION);
        if (transaction == null) {
            returnResult(RESULT_CANCELED);
        }
        ArrayList<Fragment> fragments = new ArrayList<>();
        if (transaction.getDebitCardOptions() != null) {
            fragments.add(new DebitCardForm());
        }

        if (transaction.getNetBankingOptions() != null) {
            NetBankingForm netBankingForm = new NetBankingForm();
            Bundle args = new Bundle();
            args.putSerializable(NetBankingForm.BANKS, transaction.getNetBankingOptions().getBanks());
            netBankingForm.setArguments(args);
            fragments.add(netBankingForm);
        }

        if (fragments.size() < 1) {
            returnResult(RESULT_CANCELED);
        } else if (fragments.size() == 1) {
            findViewById(R.id.pager_strip).setVisibility(View.GONE);
        }

        FormAdapter adapter = new FormAdapter(getSupportFragmentManager(), fragments);
        pager.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.debit_card) {
            pager.setCurrentItem(0, true);
        } else {
            pager.setCurrentItem(1, true);
        }

    }

}
