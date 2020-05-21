package com.app.jzapp.videoapps.adapter.classify;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app.jzapp.videoapps.fragment.classify.MVClassifyFragment;
import com.app.jzapp.videoapps.fragment.classify.StarClassifyFragment;
import com.app.jzapp.videoapps.fragment.classify.VideoClassifyFragment;

/**
 * 分类碎片适配器
 */

public class ClassifyFragmentAdapter extends FragmentPagerAdapter {


    private String[] mTitles;

    public ClassifyFragmentAdapter(FragmentManager fm, String[] mTitles) {
        super(fm);
        this.mTitles = mTitles;
    }


    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            //明星
            return StarClassifyFragment.newInstance();
        else if (position == 1)
            //mv分类
            return MVClassifyFragment.newInstance();
        else
            //视频分类
            return VideoClassifyFragment.newInstance();
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