package com.app.jzapp.videoapps.adapter.my;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app.jzapp.videoapps.fragment.my.PoliciesProvisionsChildFragment;

public class PoliciesProvisionsFragmentAdapter extends FragmentPagerAdapter {


    private String[] mTitles;

    public PoliciesProvisionsFragmentAdapter(FragmentManager fm, String[] mTitles) {
        super(fm);
        this.mTitles = mTitles;
    }


    @Override
    public Fragment getItem(int position) {
        return PoliciesProvisionsChildFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}