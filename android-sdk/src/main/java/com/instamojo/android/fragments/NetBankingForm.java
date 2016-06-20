package com.instamojo.android.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.instamojo.android.R;
import com.instamojo.android.activities.PaymentDetailsActivity;
import com.instamojo.android.helpers.Constants;
import com.instamojo.android.helpers.Logger;
import com.instamojo.android.models.Order;

import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass. The {@link Fragment} to show Net Banking options to User.
 *
 *
 * @author vedhavyas
 * @version 1.0
 * @since 14/03/16
 */
public class NetBankingForm extends BaseFragment implements SearchView.OnQueryTextListener {

    private PaymentDetailsActivity parentActivity;
    private LinearLayout banksContainer;

    /**
     * Creates a new Instance of Fragment.
     */
    public NetBankingForm() {
        // Required empty public constructor
    }


    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_net_banking_form_instamojo, container, false);
        parentActivity = (PaymentDetailsActivity) getActivity();
        inflateXML(view);
        loadBanks("");
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        parentActivity.updateActionBarTitle(R.string.net_banking);
        parentActivity.setShowSearch(true, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        parentActivity.setShowSearch(false, this);
    }

    @Override
    public void inflateXML(View view) {
        banksContainer = (LinearLayout) view.findViewById(R.id.banks_container);
        Logger.logDebug(this.getClass().getSimpleName(), "Inflated XML");
    }

    private void loadBanks(String queryText) {
        banksContainer.removeAllViews();
        for (final Map.Entry<String, String> bank : parentActivity.getOrder().getNetBankingOptions().getBanks().entrySet()) {
            if (!bank.getKey().toLowerCase(Locale.US).contains(queryText.toLowerCase(Locale.US))) {
                continue;
            }
            View bankView = LayoutInflater.from(getContext()).inflate(R.layout.bank_view_instamojo, banksContainer, false);
            ((TextView) bankView.findViewById(R.id.bank_name)).setText(bank.getKey());
            bankView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    Order order = parentActivity.getOrder();
                    bundle.putString(Constants.URL, order.getNetBankingOptions().getUrl());
                    bundle.putString(Constants.POST_DATA, order.getNetBankingOptions().
                            getPostData(order.getAuthToken(), bank.getValue()));
                    parentActivity.startPaymentActivity(bundle);
                }
            });

            banksContainer.addView(bankView);
        }
        Logger.logDebug(this.getClass().getSimpleName(), "Loaded Banks matching Query text - " + queryText);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        loadBanks(query);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        loadBanks(newText);
        return false;
    }
}
