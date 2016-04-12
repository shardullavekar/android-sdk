package com.instamojo.mojosdk.helpers;

import android.support.annotation.NonNull;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.rengwuxian.materialedittext.validation.METValidator;

/**
 * {@link MaterialEditText} related Validators.
 * Can be assigned through {@link MaterialEditText#addValidator(METValidator)}
 * and {@link MaterialEditText#validate()} should be called to validate the Edit Text.
 *
 *
 * @author vedhavyas
 * @version 1.0
 * @since 14/03/16
 */
public class Validators {

    /**
     * Empty field validator with Required as Default Message.
     * Use {@link #EmptyFieldValidator(String)} to set a custom error message.
     */
    public static class EmptyFieldValidator extends METValidator {

        public EmptyFieldValidator(String errorMessage) {
            super(errorMessage);
        }

        public EmptyFieldValidator() {
            super("Required");
        }

        @Override
        public boolean isValid(@NonNull CharSequence charSequence, boolean result) {
            return !result;
        }
    }

    /**
     * Date validator to check the expiry date of the card.
     */
    public static class DateValidator extends METValidator {

        public DateValidator() {
            super("Invalid Date");
        }

        @Override
        public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
            return !com.instamojo.mojosdk.helpers.CardValidator.isDateExpired(text.toString());
        }
    }

    /**
     * Card validator to check the validity of the card number with all the Edge cases considered.
     */
    public static class CardValidator extends METValidator {

        public CardValidator() {
            super("Invalid Card");
        }

        @Override
        public boolean isValid(@NonNull CharSequence text, boolean isEmpty) {
            String card = text.toString().trim().replaceAll(" ", "");
            if (card.length() < 4) {
                return false;
            }
            int result;
            if (com.instamojo.mojosdk.helpers.CardValidator.masterCardWithoutLength(card)
                    || com.instamojo.mojosdk.helpers.CardValidator.dinnersClubIntWithoutLength(card)
                    || com.instamojo.mojosdk.helpers.CardValidator.visaCardWithoutLength(card)
                    || com.instamojo.mojosdk.helpers.CardValidator.amexCardWithoutLength(card)
                    || com.instamojo.mojosdk.helpers.CardValidator.discoverCardWithoutLength(card)) {
                result = com.instamojo.mojosdk.helpers.CardValidator.isValid(card, false);
            } else {
                result = com.instamojo.mojosdk.helpers.CardValidator.isValid(card, true);
            }
            return result != 0;
        }
    }
}
