package com.app.jzapp.videoapps.adapter.classify;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app.jzapp.videoapps.fragment.classify.ClassifyDetailChildFragment;

public class ClassifyDetailFragmentAdapter extends FragmentPagerAdapter {


    private String[] mTitles;
    private String secondotype;

    public ClassifyDetailFragmentAdapter(FragmentManager fm, String[] mTitles, String secondotype) {
        super(fm);
        this.mTitles = mTitles;
        this.secondotype = secondotype;
    }


    @Override
    public Fragment getItem(int position) {
        return ClassifyDetailChildFragment.newInstance(position, secondotype);
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