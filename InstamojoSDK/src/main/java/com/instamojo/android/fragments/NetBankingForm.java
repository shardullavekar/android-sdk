package com.instamojo.android.fragments;


import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.instamojo.android.R;
import com.instamojo.android.activities.FormActivity;
import com.instamojo.android.adapters.SpinnerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass. The {@link Fragment} to show Netbamking options to User.
 *
 *
 * @author vedhavyas
 * @version 1.0
 * @since 14/03/16
 */
public class NetBankingForm extends Fragment {

    public static final String BANKS = "banks";
    private View favBanks;
    private LinearLayout favBanks1, favBanks2;
    private HashMap<String, String> banks, favBanksAvailable, otherBanks;
    private ArrayList<String> otherBanksName;
    private FormActivity formActivity;
    private AppCompatSpinner otherBanksSpinner;
    private String selectedBank = "";
    private boolean rogueCall = true;

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
        View view = inflater.inflate(R.layout.fragment_net_banking_form, container, false);
        formActivity = (FormActivity) getActivity();
        banks = (HashMap<String, String>) getArguments().getSerializable(BANKS);
        if (banks == null) {
            formActivity.returnResult(Activity.RESULT_CANCELED);
            return view;
        }
        inflateXML(view);
        loadFavBanks();
        invalidateFavBanks();
        invalidateOtherBanks();
        return view;
    }

    private void inflateXML(View view) {
        favBanks = view.findViewById(R.id.fav_banks);
        favBanks1 = (LinearLayout) view.findViewById(R.id.fav_banks_1);
        favBanks2 = (LinearLayout) view.findViewById(R.id.fav_banks_2);
        otherBanksSpinner = (AppCompatSpinner) view.findViewById(R.id.other_banks_spinner);
        otherBanksSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    if (rogueCall) {
                        rogueCall = false;
                        return;
                    }
                    selectedBank = "";
                    return;
                }
                String selectedName = otherBanksName.get(position);
                for (Map.Entry<String, String> entry : otherBanks.entrySet()) {
                    if (entry.getKey().equalsIgnoreCase(selectedName)) {
                        selectedBank = entry.getValue();
                        invalidateFavBanks();
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        view.findViewById(R.id.checkout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedBank.isEmpty()) {
                    return;
                }

                formActivity.checkOutWithNetBanking(selectedBank);
            }
        });
    }

    private void loadFavBanks() {
        favBanksAvailable = new HashMap<>();
        otherBanks = new HashMap<>();
        for (Map.Entry<String, String> entry : banks.entrySet()) {
            String shortCode = getBankShort(entry.getValue());
            if (shortCode != null) {
                favBanksAvailable.put(shortCode, entry.getValue());
            } else {
                otherBanks.put(entry.getKey(), entry.getValue());
            }
        }

    }

    private void invalidateOtherBanks() {
        if (otherBanks.size() == 0) {
            otherBanksSpinner.setVisibility(View.GONE);
            return;
        }

        otherBanksName = new ArrayList<>(this.otherBanks.keySet());
        Collections.sort(otherBanksName);
        otherBanksName.add(0, "Select Other Bank");
        SpinnerAdapter adapter = new SpinnerAdapter(getContext(), otherBanksName);
        otherBanksSpinner.setAdapter(adapter);
    }

    private void invalidateFavBanks() {
        if (favBanksAvailable.size() == 0) {
            favBanks.setVisibility(View.GONE);
            return;
        }

        favBanks1.removeAllViews();
        favBanks2.removeAllViews();

        for (final Map.Entry<String, String> entry : favBanksAvailable.entrySet()) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.bank_layout,
                    new LinearLayout(getContext()), false);
            ImageButton bank = (ImageButton) view.findViewById(R.id.bank_image);
            if (entry.getValue().equalsIgnoreCase(selectedBank)) {
                bank.setBackgroundResource(R.drawable.square_selected);
            }
            bank.setImageDrawable(getBankDrawable(entry.getValue()));
            bank.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedBank = entry.getValue();
                    invalidateFavBanks();
                    rogueCall = true;
                    otherBanksSpinner.setSelection(0);
                }
            });
            TextView bankName = (TextView) view.findViewById(R.id.bank_name);
            bankName.setText(entry.getKey());

            if (favBanks1.getChildCount() == 3) {
                favBanks2.addView(view);
            } else {
                favBanks1.addView(view);
            }
        }

        if (favBanks1.getChildCount() == 0) {
            favBanks1.setVisibility(View.GONE);
        }

        if (favBanks2.getChildCount() == 0) {
            favBanks2.setVisibility(View.GONE);
        }

    }

    private Drawable getBankDrawable(String bankCode) {
        switch (bankCode) {
            case "1":
                return ContextCompat.getDrawable(getContext(), R.drawable.ic_sbi);
            case "2":
                return ContextCompat.getDrawable(getContext(), R.drawable.ic_icici);
            case "3":
                return ContextCompat.getDrawable(getContext(), R.drawable.ic_hdfc);
            case "4":
                return ContextCompat.getDrawable(getContext(), R.drawable.ic_axis);
            case "5":
                return ContextCompat.getDrawable(getContext(), R.drawable.ic_kotak);
            case "6":
                return ContextCompat.getDrawable(getContext(), R.drawable.ic_pnb);

            default:
                return null;
        }
    }

    private String getBankShort(String bankCode) {
        switch (bankCode) {
            case "1":
                return "SBI";
            case "2":
                return "ICICI";
            case "3":
                return "HDFC";
            case "4":
                return "Axis";
            case "5":
                return "Kotak";
            case "6":
                return "PNB";

            default:
                return null;
        }
    }

}
