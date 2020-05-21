package com.app.jzapp.videoapps.fragment;

import android.app.ActionBar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.app.jzapp.videoapps.adapter.collect.CollectFragmentAdapter;
import com.app.jzapp.videoapps.fragment.mv.SearchFragment;
import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.base.BaseMainFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 收藏列表
 */
public class CollectFragment extends BaseMainFragment {
    @BindView(R.id.ll_search)
    View llSearch;
    @BindView(R.id.paddingView)
    View paddingView;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    public static CollectFragment newInstance() {
        CollectFragment fragment = new CollectFragment();
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_mv;
    }

    @Override
    protected View setStatusBarView() {
        return paddingView;
    }

    @Override
    protected int statusBarColor() {
        return R.color.white;
    }

    @Override
    protected void initView() {
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        float height = _mActivity.getResources().getDimension(R.dimen.x40);
        tabLayout.setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, (int) height));

        llSearch.setVisibility(View.GONE);
        String[] mTitles = getResources().getStringArray(R.array.tab_collect);

        for (int i = 0; i < mTitles.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(mTitles[i]));
        }

        tabLayout.setTabTextColors(getResources().getColor(R.color.black), getResources().getColor(R.color.c_EC72AD));

        tabLayout.setupWithViewPager(viewPager);

        CollectFragmentAdapter adapter = new CollectFragmentAdapter(getChildFragmentManager(), mTitles);
        viewPager.setAdapter(adapter);
    }

    @OnClick(R.id.tv_search)
    void search() {
        ((MainFragment) getParentFragment()).startBrotherFragment(SearchFragment.newInstance());
    }

}
