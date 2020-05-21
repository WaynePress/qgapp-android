package com.app.jzapp.videoapps.adapter.collect;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app.jzapp.videoapps.fragment.collect.CollectMVFragment;
import com.app.jzapp.videoapps.fragment.collect.CollectVideoFragment;
import com.app.jzapp.videoapps.fragment.collect.StarFragment;

public class CollectFragmentAdapter extends FragmentPagerAdapter {


    private String[] mTitles;

    public CollectFragmentAdapter(FragmentManager fm, String[] mTitles) {
        super(fm);
        this.mTitles = mTitles;
    }


    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return StarFragment.newInstance();
        else if (position == 1)
            return CollectMVFragment.newInstance();
        else
            return CollectVideoFragment.newInstance( );
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