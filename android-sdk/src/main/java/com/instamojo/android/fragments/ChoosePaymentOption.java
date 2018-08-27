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
 */
public class ChoosePaymentOption extends BaseFragment implements View.OnClickListener {

    private static final String FRAGMENT_NAME = "ChoosePaymentOption";
    private PaymentDetailsActivity parentActivity;

    public ChoosePaymentOption() {
        //empty as required
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_payment_instamojo, container, false);
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
        //View debitCardLayout = view.findViewById(R.id.debit_card_layout);
        //View creditCardLayout = view.findViewById(R.id.credit_card_layout);
        View netBankingLayout = view.findViewById(R.id.net_banking_layout);
        View emiLayout = view.findViewById(R.id.emi_layout);
        View walletLayout = view.findViewById(R.id.wallet_layout);
        View upiLayout = view.findViewById(R.id.upi_layout);

        if (order.getNetBankingOptions() == null) {
            Logger.logDebug(this.getClass().getSimpleName(), "Hiding Net banking Layout");
            netBankingLayout.setVisibility(View.GONE);
        }

//         if (order.getCardOptions() == null) {
//             Logger.logDebug(this.getClass().getSimpleName(), "Hiding Debit and Credit Card Layout");
//             debitCardLayout.setVisibility(View.GONE);
//             creditCardLayout.setVisibility(View.GONE);
//         }

        if (order.getEmiOptions() == null) {
            Logger.logDebug(this.getClass().getSimpleName(), "Hiding EMI Layout");
            emiLayout.setVisibility(View.GONE);
        }

        if (order.getWalletOptions() == null) {
            Logger.logDebug(this.getClass().getSimpleName(), "Hiding Wallet Layout");
            walletLayout.setVisibility(View.GONE);
        }

        if (order.getUpiOptions() == null) {
            Logger.logDebug(this.getClass().getSimpleName(), "Hiding UPISubmission layout");
            upiLayout.setVisibility(View.GONE);
        }

        //debitCardLayout.setOnClickListener(this);
        //creditCardLayout.setOnClickListener(this);
        netBankingLayout.setOnClickListener(this);
        emiLayout.setOnClickListener(this);
        walletLayout.setOnClickListener(this);
        upiLayout.setOnClickListener(this);
        Logger.logDebug(this.getClass().getSimpleName(), "Inflated XML");
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.wallet_layout) {
            Logger.logDebug(this.getClass().getSimpleName(), "Starting Wallet Form");
            parentActivity.loadFragment(ListForm.getListFormFragment(ListForm.Mode.Wallet), true);
        } else if (id == R.id.net_banking_layout) {
            Logger.logDebug(this.getClass().getSimpleName(), "Starting Net Banking Form");
            parentActivity.loadFragment(ListForm.getListFormFragment(ListForm.Mode.NetBanking), true);
        } else if (id == R.id.emi_layout) {
            Logger.logDebug(this.getClass().getSimpleName(), "Starting EMI Form");
            parentActivity.loadFragment(new EMIBankList(), true);
        } else if (id == R.id.upi_layout) {
            Logger.logDebug(this.getClass().getSimpleName(), "Starting UPISubmission Form");
            parentActivity.loadFragment(new UPIpaymentFragment(), true);
        } else {
            Logger.logDebug(this.getClass().getSimpleName(), "Starting CardForm");
            //since the user is directly jumping to Card from instead of EMI.
            // We can safely assume that emi is not chosen. Hence, clear all emi related stuff in order
            if (parentActivity.getOrder().getEmiOptions() != null) {
                parentActivity.getOrder().getEmiOptions().setSelectedBankCode(null);
                parentActivity.getOrder().getEmiOptions().setSelectedTenure(-1);
            }

//             if (id == R.id.debit_card_layout) {
//                 parentActivity.loadFragment(CardForm.getCardForm(CardForm.Mode.DebitCard), true);
//             } else {
//                 parentActivity.loadFragment(CardForm.getCardForm(CardForm.Mode.CreditCard), true);
//             }
        }
    }

    @Override
    public String getFragmentName() {
        return FRAGMENT_NAME;
    }
}
