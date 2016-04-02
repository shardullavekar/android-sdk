package com.instamojo.mojosdk.network;

/**
 * Authored by vedhavyas on 11/03/16.
 */

class Urls {
    private static final String SERVER = "https://staging.instamojo.com/v2";

    public static final String MOJO_TRANSACTION_INIT_URL = SERVER + "/integrations/payments/";

    public static String getMojoTransactionInitUrl() {
        return MOJO_TRANSACTION_INIT_URL;
    }

}
