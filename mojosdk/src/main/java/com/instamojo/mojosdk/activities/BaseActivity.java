package com.instamojo.mojosdk.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Method;

/**
 * Base Activity to all the Activities in the SDK.
 * Implements Calligraphy using reflection. The default font will be applied through context wrappers
 * as defined in the Calligraphy Documentation. Any newly created activity must extend this BaseActivity
 * for the proper application of the default font.
 *
 *
 * @author vedhavyas
 * @version 1.0
 * @since 14/03/16
 */
public abstract class BaseActivity extends AppCompatActivity {

    @SuppressWarnings("unchecked")
    @Override
    protected void attachBaseContext(Context context) {
        Class calligraphyClass = getCalligraphyClass();
        if (calligraphyClass != null) {
            try {
                Method method = calligraphyClass.getMethod("wrap", Context.class);
                super.attachBaseContext((Context) method.invoke(calligraphyClass, context));
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        super.attachBaseContext(context);
    }

    private Class getCalligraphyClass() {
        try {
            return Class.forName("uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper");
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * returnResult is used to pass back the appropriate result to the initiator activity that
     * started this activity for result. The method will call {@link Activity#finish()} and the
     * current activity will be stopped.
     *
     * @param bundle if any extra params that need to be passed back.
     * @param result Appropriate Activity result to be sent back to caller Activity - {@link Activity#RESULT_OK}
     *               on Success or {@link Activity#RESULT_CANCELED} on Failure.
     */

    public void returnResult(Bundle bundle, int result) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        setResult(result, intent);
        finish();
    }

    /**
     * Overloading method for {@link BaseActivity#returnResult(Bundle, int)} with null Bundle.
     *
     * @param result Appropriate Activity result to be sent back to caller Activity - {@link Activity#RESULT_OK}
     *               on Success or {@link Activity#RESULT_CANCELED} on Failure.
     */

    public void returnResult(int result) {
        returnResult(null, result);
    }

    /**
     * Hides the Soft keyboard if activated.
     */

    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
