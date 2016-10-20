package com.instamojo.android.callbacks;


import android.os.Bundle;

import com.instamojo.android.models.UPISubmissionResponse;

/**
 * Callback for UPISubmission Submission Method.
 *
 * @author vedhavyas
 * @version 1.2.4
 * @since 20/10/16
 */

public interface UPICallback {
    void onSubmission(UPISubmissionResponse upiSubmissionResponse, Exception e);

    void onStatusCheckComplete(Bundle bundle, boolean paymentComplete, Exception e);
}
