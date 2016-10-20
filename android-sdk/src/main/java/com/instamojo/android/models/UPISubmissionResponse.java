package com.instamojo.android.models;

/**
 * Response Model for UPISubmission submission Call.
 *
 * @author vedhavyas
 * @version 1.2.4
 * @since 20/10/16
 */

public class UPISubmissionResponse {

    private String paymentID;
    private int statusCode;
    private String payerVirtualAddress;
    private String payeeVirtualAddress;
    private String statusCheckURL;
    private String upiBank;
    private String statusMessage;

    public UPISubmissionResponse(String paymentID, int statusCode, String payerVirtualAddress, String payeeVirtualAddress, String statusCheckURL, String upiBank, String statusMessage) {
        this.paymentID = paymentID;
        this.statusCode = statusCode;
        this.payerVirtualAddress = payerVirtualAddress;
        this.payeeVirtualAddress = payeeVirtualAddress;
        this.statusCheckURL = statusCheckURL;
        this.upiBank = upiBank;
        this.statusMessage = statusMessage;
    }

    /**
     * Payment ID for this UPISubmission Method
     *
     * @return String
     */
    public String getPaymentID() {
        return paymentID;
    }

    /**
     * Set payment ID for this UPISubmission method
     *
     * @param paymentID String
     */
    public void setPaymentID(String paymentID) {
        this.paymentID = paymentID;
    }

    /**
     * Status Code for this Submission
     *
     * @return int
     */
    public int getStatusCode() {
        return statusCode;
    }

    /**
     * Set status code for this submission
     *
     * @param statusCode int
     */
    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Payer's Virtual Address
     *
     * @return String
     */
    public String getPayerVirtualAddress() {
        return payerVirtualAddress;
    }

    /**
     * Set Payer's Virtual Address
     *
     * @param payerVirtualAddress String
     */
    public void setPayerVirtualAddress(String payerVirtualAddress) {
        this.payerVirtualAddress = payerVirtualAddress;
    }

    /**
     * Payee's Vitual Address
     *
     * @return String
     */
    public String getPayeeVirtualAddress() {
        return payeeVirtualAddress;
    }

    /**
     * Set Payee's Virtual Address
     *
     * @param payeeVirtualAddress String
     */
    public void setPayeeVirtualAddress(String payeeVirtualAddress) {
        this.payeeVirtualAddress = payeeVirtualAddress;
    }

    /**
     * Status Check URl for this Submission
     *
     * @return String
     */
    public String getStatusCheckURL() {
        return statusCheckURL;
    }

    /**
     * Set Status Check URL for this Submission
     *
     * @param statusCheckURL String
     */
    public void setStatusCheckURL(String statusCheckURL) {
        this.statusCheckURL = statusCheckURL;
    }

    /**
     * PSP name of the Payer's Virtual Address
     *
     * @return String
     */
    public String getUpiBank() {
        return upiBank;
    }

    /**
     * Set PSP Name of the Payer's Virtual Address
     *
     * @param upiBank String
     */
    public void setUpiBank(String upiBank) {
        this.upiBank = upiBank;
    }

    /**
     * Status Message for this submission
     *
     * @return String
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * Set Status Message for this submission
     *
     * @param statusMessage String
     */
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
