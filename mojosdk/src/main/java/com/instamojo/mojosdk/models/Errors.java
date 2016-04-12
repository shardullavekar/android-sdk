package com.instamojo.mojosdk.models;

/**
 * Custom Error class for the SDK.
 *
 *
 * @author vedhavyas
 * @version 1.0
 * @since 14/03/16
 */
public class Errors {

    /**
     * Exception to be used when the cause of failure is related to Network.
     */
    public static final class ConnectionException extends Exception {
        /**
         * Constructor for the class.
         *
         * @param message Exception Message.
         */
        public ConnectionException(String message) {
            super(message);
        }
    }

    /**
     * Exception to be used when the cause of the failure is related to form errors from MojoServer.
     */
    public static final class ServerException extends Exception {
        /**
         * Constructor for the Class.
         *
         * @param message Should be in the JSON format with appropriate error keys and values.
         */
        public ServerException(String message) {
            super(message);
        }
    }
}
