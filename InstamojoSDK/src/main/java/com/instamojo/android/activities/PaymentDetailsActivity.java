package com.instamojo.android.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.instamojo.android.R;
import com.instamojo.android.fragments.BaseFragment;
import com.instamojo.android.fragments.ChoosePaymentOption;
import com.instamojo.android.models.Transaction;

/**
 * Payment Details Activity extends the {@link BaseActivity}. Activity lets user to choose a Payment method
 *
 * @author vedhavyas
 * @version 1.0
 * @since 14/03/16
 */
public class PaymentDetailsActivity extends BaseActivity {

    /**
     * Extra key for the {@link Transaction} object that is sent through Bundle.
     */
    public static final String TRANSACTION = "transaction";

    private Transaction transaction;
    private boolean showSearch;
    private SearchView.OnQueryTextListener onQueryTextListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);
        inflateXML();
        loadFragments();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (showSearch) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_payment_options, menu);

            SearchManager searchManager = (SearchManager)
                    getSystemService(Context.SEARCH_SERVICE);
            MenuItem searchMenuItem = menu.findItem(R.id.search);
            SearchView searchView = (SearchView) searchMenuItem.getActionView();

            searchView.setSearchableInfo(searchManager.
                    getSearchableInfo(getComponentName()));
            searchView.setSubmitButtonEnabled(true);
            if (onQueryTextListener != null) {
                searchView.setOnQueryTextListener(onQueryTextListener);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Start the payment activity with given bundle
     *
     * @param bundle Bundle with either card/netbanking url
     */
    public void startPaymentActivity(Bundle bundle) {
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtras(getIntent());
        intent.putExtra(PaymentActivity.PAYMENT_BUNDLE, bundle);
        startActivityForResult(intent, 9);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            returnResult(RESULT_CANCELED);
        } else {
            getSupportFragmentManager().popBackStackImmediate();
        }
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
        updateActionBar();
    }

    /**
     * @return The current Transaction
     */
    public Transaction getTransaction() {
        return transaction;
    }

    private void loadFragments() {
        transaction = getIntent().getParcelableExtra(TRANSACTION);
        if (transaction == null) {
            returnResult(RESULT_CANCELED);
            return;
        }

        loadFragment(new ChoosePaymentOption(), false);
    }

    /**
     * Load the given fragment to the support fragment manager
     *
     * @param fragment       Fragment to be added. Must be a subclass of {@link BaseFragment}
     * @param addToBackStack Whether to add this fragment to back stack
     */
    public void loadFragment(BaseFragment fragment, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.replace(R.id.container, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(fragment.getFragmentName());
        }
        fragmentTransaction.commit();
    }

    /**
     * Show the search icon in the actionbar
     *
     * @param showSearch        Show the search icon in action action bar
     * @param queryTextListener {@link android.support.v7.widget.SearchView.OnQueryTextListener} to listen for the query string
     */
    public void setShowSearch(boolean showSearch, SearchView.OnQueryTextListener queryTextListener) {
        this.showSearch = showSearch;
        this.onQueryTextListener = queryTextListener;
        invalidateOptionsMenu();
    }


}
