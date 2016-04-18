package com.instamojo.android.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.instamojo.android.R;
import com.instamojo.android.activities.FormActivity;
import com.instamojo.android.helpers.CardValidator;
import com.instamojo.android.helpers.Validators;
import com.instamojo.android.models.Card;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass. The {@link Fragment} to get Debit Card details from user.
 *
 *
 * @author vedhavyas
 * @version 1.0
 * @since 14/03/16
 */
public class DebitCardForm extends Fragment implements View.OnClickListener {

    private static final String MONTH_YEAR_SEPARATOR = "/";
    private MaterialEditText cardNumberBox, nameOnCardBox, cvvBox, dateBox;
    private List<MaterialEditText> editTexts;

    /**
     * Creates a new instance of Fragment
     */
    public DebitCardForm() {
        // Required empty public constructor
    }

    private static boolean isEditBoxesValid(List<MaterialEditText> editTexts) {
        boolean allValid = true;
        for (MaterialEditText editText : editTexts) {
            allValid = editText.validate() && allValid;
        }
        return allValid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_debit_card_form, container, false);
        inflateXML(view);
        return view;
    }

    private void inflateXML(View view) {
        cardNumberBox = (MaterialEditText) view.findViewById(R.id.card_number_box);
        cardNumberBox.setNextFocusDownId(R.id.card_date_box);
        cardNumberBox.addTextChangedListener(new CardTextWatcher());
        cardNumberBox.addValidator(new Validators.EmptyFieldValidator());
        cardNumberBox.addValidator(new Validators.CardValidator());
        dateBox = (MaterialEditText) view.findViewById(R.id.card_date_box);
        dateBox.setNextFocusDownId(R.id.name_on_card_box);
        nameOnCardBox = (MaterialEditText) view.findViewById(R.id.name_on_card_box);
        nameOnCardBox.setNextFocusDownId(R.id.cvv_box);
        nameOnCardBox.addValidator(new Validators.EmptyFieldValidator());
        cvvBox = (MaterialEditText) view.findViewById(R.id.cvv_box);
        dateBox.addTextChangedListener(new TextWatcher() {

            private int previousLength = 0, currentLength = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previousLength = dateBox.getText().toString().trim().length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentLength = dateBox.getText().toString().trim().length();
            }

            @Override
            public void afterTextChanged(Editable s) {
                String date = s.toString().trim().replaceAll(" ", "");
                String modifiedDate = date;

                boolean backPressed = previousLength > currentLength;
                if (backPressed) {
                    if (date.length() == 3) {
                        modifiedDate = date.substring(0, 2);
                    }
                } else {
                    if (date.length() == 2) {
                        modifiedDate = date + MONTH_YEAR_SEPARATOR;
                    } else if (previousLength == 2) {
                        modifiedDate = date.substring(0, 2) + MONTH_YEAR_SEPARATOR + date.substring(2, date.length());
                    }
                }

                applyText(dateBox, this, modifiedDate);
                if (modifiedDate.length() == 5 && dateBox.validate()) {
                    nameOnCardBox.requestFocus();
                }
            }
        });

        cardNumberBox.post(new Runnable() {
            @Override
            public void run() {
                cardNumberBox.requestFocus();
                InputMethodManager lManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (lManager != null) {
                    lManager.showSoftInput(cardNumberBox, InputMethodManager.SHOW_IMPLICIT);
                }

            }
        });

        view.findViewById(R.id.checkout).setOnClickListener(this);

        editTexts = Arrays.asList(cardNumberBox, dateBox, nameOnCardBox, cvvBox);
    }

    private void applyText(MaterialEditText editText, TextWatcher watcher, String text) {
        editText.removeTextChangedListener(watcher);
        editText.setText(text);
        editText.setSelection(editText.getText().toString().length());
        editText.addTextChangedListener(watcher);
    }

    private void clearOptionalValidators() {
        cvvBox.clearValidators();
        dateBox.clearValidators();
        cvvBox.setError(null);
        dateBox.setError(null);
    }

    private void addOptionalValidators() {
        dateBox.addValidator(new Validators.EmptyFieldValidator());
        dateBox.addValidator(new Validators.DateValidator());
        cvvBox.addValidator(new Validators.EmptyFieldValidator());
    }

    /**
     * Method to change the state of the UI elements in the form.
     *
     * @param enable True to enable or False to disable.
     */

    public void changeEditBoxesState(boolean enable) {
        cardNumberBox.setEnabled(enable);
        nameOnCardBox.setEnabled(enable);
        dateBox.setEnabled(enable);
        cvvBox.setEnabled(enable);
    }

    private void checkout() {
        if (!isEditBoxesValid(editTexts)) {
            return;
        }

        Card card = new Card();
        card.setCardHolderName(nameOnCardBox.getText().toString().trim());
        String cardNumber = cardNumberBox.getText().toString().trim();
        cardNumber = cardNumber.replaceAll(" ", "");
        card.setCardNumber(cardNumber);
        String date = dateBox.getText().toString().trim();
        if (date.isEmpty()) {
            card.setDate("12/49");
        } else {
            if (!dateBox.validateWith(new Validators.DateValidator())) {
                return;
            } else {
                card.setDate(date);
            }
        }

        String cvv = cvvBox.getText().toString().trim();
        if (cvv.isEmpty()) {
            card.setCvv("111");
        } else {
            card.setCvv(cvv);
        }

        ((FormActivity) getActivity()).checkOutWithCard(this, card);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.checkout) {
            checkout();
        }
    }

    private class CardTextWatcher implements TextWatcher {

        private int limit = -1;
        private int drawable = 0;
        private int previousLength = 0, currentLength = 0;

        public CardTextWatcher() {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            previousLength = cardNumberBox.getText().toString().trim().length();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String card = cardNumberBox.getText().toString();
            card = card.replaceAll(" ", "");
            if (card.length() == 4) {
                drawable = CardValidator.getCardDrawable(card);
                limit = CardValidator.validateCardTypeWithoutLengthForLimit(card);
                if (CardValidator.maestroCard(card)) {
                    clearOptionalValidators();
                } else {
                    addOptionalValidators();
                }
            } else if (card.length() < 4) {
                drawable = 0;
                clearOptionalValidators();
            }

            cardNumberBox.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0);

            if (card.length() == limit) {
                dateBox.requestFocus();
            }
            currentLength = cardNumberBox.getText().toString().trim().length();
        }

        @Override
        public void afterTextChanged(Editable s) {
            String card = s.toString().trim();
            if (card.length() < 4) {
                return;
            }

            String modifiedCard;
            if (currentLength > previousLength) {
                String[] data = card.replaceAll(" ", "").split("");
                modifiedCard = "";
                if (CardValidator.masterCardWithoutLength(card) || CardValidator.visaCardWithoutLength(card)
                        || CardValidator.discoverCardWithoutLength(card)) {
                    for (int index = 1; index < data.length; index++) {
                        modifiedCard = modifiedCard + data[index];
                        if (index == 4 || index == 8 || index == 12) {
                            modifiedCard = modifiedCard + " ";
                        }
                    }
                } else if (CardValidator.amexCardWithoutLength(card)) {
                    for (int index = 1; index < data.length; index++) {
                        modifiedCard = modifiedCard + data[index];
                        if (index == 4 || index == 11) {
                            modifiedCard = modifiedCard + " ";
                        }
                    }
                } else if (CardValidator.dinnersClubIntWithoutLength(card)) {
                    for (int index = 1; index < data.length; index++) {
                        modifiedCard = modifiedCard + data[index];
                        if (index == 4 || index == 10) {
                            modifiedCard = modifiedCard + " ";
                        }
                    }
                } else {
                    modifiedCard = card;
                }
            } else {
                modifiedCard = card;
            }

            applyText(cardNumberBox, this, modifiedCard);
        }
    }

}
