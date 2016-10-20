package com.instamojo.android.models;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Custom Error class for the SDK.
 *
 * @author vedhavyas
 * @version 1.0
 * @since 14/03/16
 */
public class Errors {

    public static Exception getAppropriateError(String body) {
        try {
            JSONObject errorObject = new JSONObject(body);
            if (errorObject.has("success")) {
                return new AuthenticationError(body);
            }

            return new ValidationError(body);
        } catch (JSONException e) {
            return new ServerError(body);
        }

    }

    /**
     * Exception to be used when the cause of failure is related to Network.
     */
    public static final class ConnectionError extends Exception {
        /**
         * Constructor for the class.
         *
         * @param message Exception Message.
         */
        public ConnectionError(String message) {
            super(message);
        }
    }

    /**
     * Exception to be used when the cause of the failure is related to form errors from MojoServer.
     */
    public static final class ServerError extends Exception {
        /**
         * Constructor for the Class.
         *
         * @param message Exception message
         */
        public ServerError(String message) {
            super(message);
        }
    }

    /**
     * Exception to be used when the access token passed is invalid/expired
     */
    public static final class AuthenticationError extends Exception {

        /**
         * Constructor for the class.
         *
         * @param message Exception message
         */

        public AuthenticationError(String message) {
            super(message);
        }
    }

    /**
     * Exception to be used for all the validations
     */
    public static final class ValidationError extends Exception {

        private boolean validTransactionID = true;
        private boolean validRedirectURL = true;
        private boolean validWebhook = true;
        private boolean validName = true;
        private boolean validPhone = true;
        private boolean validEmail = true;
        private boolean validAmount = true;

        /**
         * Constructor for the Class.
         *
         * @param message Should be in the JSON format with appropriate error keys and values.
         */
        public ValidationError(String message) throws JSONException {
            super(message);
            invalidateMessage(message);
        }

        private void invalidateMessage(String message) throws JSONException {
            JSONObject errorObject = new JSONObject(message);
            if (errorObject.has("transaction_id")) {
                validTransactionID = false;
            }

            if (errorObject.has("redirect_url")) {
                validRedirectURL = false;
            }

            if (errorObject.has("phone")) {
                validPhone = false;
            }

            if (errorObject.has("email")) {
                validEmail = false;
            }

            if (errorObject.has("amount")) {
                validAmount = false;
            }

            if (errorObject.has("name")) {
                validName = false;
            }

            if (errorObject.has("webhook_url")) {
                validWebhook = false;
            }
        }


        /**
         * @return false if transaction ID is not unique. Else true
         */
        public boolean isValidTransactionID() {
            return validTransactionID;
        }

        /**
         * @return false if the redirect url is not valid. Else true.
         */
        public boolean isValidRedirectURL() {
            return validRedirectURL;
        }

        /**
         * @return false if the name is empty/exceeded character limit. Else true.
         */
        public boolean isValidName() {
            return validName;
        }

        /**
         * @return false if the phone number is invalid. Else true.
         */
        public boolean isValidPhone() {
            return validPhone;
        }

        /**
         * @return false if the email is invalid. Else true.
         */
        public boolean isValidEmail() {
            return validEmail;
        }

        /**
         * @return false if the amount is less than Rs.9 or has more than two decimal places
         */
        public boolean isValidAmount() {
            return validAmount;
        }

        /**
         * @return false if the webhook is invalid
         */
        public boolean isValidWebhook() {
            return validWebhook;
        }
    }
}
