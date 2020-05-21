package com.app.jzapp.videoapps.adapter.video;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app.jzapp.videoapps.bean.TypeBean;
import com.app.jzapp.videoapps.fragment.video.VideoChildFragment;

import java.util.List;

public class VideoFragmentAdapter extends FragmentPagerAdapter {


    private List<TypeBean> mTitles;

    public VideoFragmentAdapter(FragmentManager fm, List<TypeBean> mTitles) {
        super(fm);
        this.mTitles = mTitles;
    }


    @Override
    public Fragment getItem(int position) {
        return VideoChildFragment.newInstance(mTitles.get(position).getOid(), mTitles.get(position));
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position).getOtypename();
    }
}