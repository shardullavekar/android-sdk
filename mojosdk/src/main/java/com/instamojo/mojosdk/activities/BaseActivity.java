package com.instamojo.mojosdk.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Method;

/**
 * Authored by vedhavyas on 14/03/16.
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

    public void returnResult(Bundle bundle, int result) {
        Intent intent = new Intent();
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        setResult(result, intent);
        finish();
    }

    public void returnResult(int result) {
        returnResult(null, result);
    }

    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
