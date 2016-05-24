package com.instamojo.android.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.instamojo.android.R;
import com.instamojo.android.activities.PaymentDetailsActivity;
import com.instamojo.android.helpers.Logger;
import com.instamojo.android.models.Order;

/**
 * Fragment holds the available Payment options for the User
 *
 * @author vedhavyas
 * @version 1.0
 * @since 14/03/16
 */
public class ChoosePaymentOption extends BaseFragment implements View.OnClickListener {

    private static final String FRAGMENT_NAME = "ChoosePaymentOption";
    private PaymentDetailsActivity parentActivity;

    public ChoosePaymentOption() {
        //empty as required
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_payment, container, false);
        parentActivity = (PaymentDetailsActivity) getActivity();
        inflateXML(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        parentActivity.updateActionBarTitle(R.string.title_fragment_choose_payment_option);
    }

    @Override
    public void inflateXML(View view) {
        Order order = parentActivity.getOrder();
        View cardLayout = view.findViewById(R.id.card_layout);
        View netBankingLayout = view.findViewById(R.id.net_banking_layout);

        if (order.getNetBankingOptions() == null) {
            Logger.logDebug(this.getClass().getSimpleName(), "Hiding Net banking Layout");
            netBankingLayout.setVisibility(View.GONE);
        }

        if (order.getCardOptions() == null) {
            Logger.logDebug(this.getClass().getSimpleName(), "Hiding Card Layout");
            cardLayout.setVisibility(View.GONE);
        }

        cardLayout.setOnClickListener(this);
        netBankingLayout.setOnClickListener(this);
        Logger.logDebug(this.getClass().getSimpleName(), "Inflated XML");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.card_layout) {
            Logger.logDebug(this.getClass().getSimpleName(), "Starting CardForm");
            parentActivity.loadFragment(new CardForm(), true);
        } else {
            Logger.logDebug(this.getClass().getSimpleName(), "Starting Net Banking Form");
            parentActivity.loadFragment(new NetBankingForm(), true);
        }
    }

    @Override
    public String getFragmentName() {
        return FRAGMENT_NAME;
    }
}
