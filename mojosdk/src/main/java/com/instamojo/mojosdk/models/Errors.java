package com.instamojo.mojosdk.models;

/**
 * Authored by vedhavyas on 02/04/16.
 */
public class Errors {

    public static final class ConnectionException extends Exception {
        public ConnectionException(String message) {
            super(message);
        }
    }

    public static final class ServerException extends Exception {
        public ServerException(String message) {
            super(message);
        }
    }
}
