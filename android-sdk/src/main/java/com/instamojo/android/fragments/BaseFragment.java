package com.instamojo.android.fragments;

import android.support.v4.app.Fragment;
import android.view.View;

/**
 * An abstract Fragment
 *
 * @author vedhavyas
 * @version 1.0
 * @since 14/03/16
 */
public abstract class BaseFragment extends Fragment {

    /**
     * Attach views to Java in this method
     *
     * @param view rootview of the fragment
     */
    public void inflateXML(View view) {
        // No logic
    }

    /**
     * @return Name of the fragment
     */
    public String getFragmentName() {
        return "";
    }
}
