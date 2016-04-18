package com.instamojo.android.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * View Pager adapter for {@link com.instamojo.android.activities.FormActivity}
 *
 *
 * @author vedhavyas
 * @version 1.0
 * @since 14/03/16
 */

public class FormAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;

    /**
     * Initiate the Adapter with {@link FragmentManager} and {@link java.util.ArrayList<Fragment>}.
     *
     * @param fm        {@link FragmentManager} to manage the {@link Fragment}.
     * @param fragments {@link ArrayList<Fragment>} holding {@link com.instamojo.android.fragments.DebitCardForm}
     *                  or {@link com.instamojo.android.fragments.NetBankingForm} or both fragments.
     */

    public FormAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

}
