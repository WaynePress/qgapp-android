package com.app.jzapp.videoapps.fragment.my;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.adapter.my.PoliciesProvisionsFragmentAdapter;
import com.app.jzapp.videoapps.base.BaseBackFragment;

import butterknife.BindView;

public class PoliciesProvisionsFragment extends BaseBackFragment {
    @BindView(R.id.paddingView)
    View paddingView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    public static PoliciesProvisionsFragment newInstance() {
        PoliciesProvisionsFragment fragment = new PoliciesProvisionsFragment();
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_policies_provisions;
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
        initToolbarNav(toolbar);

        toolbar.setTitle("");
        title.setText(getString(R.string.policies_provisions));

        String[] tab = new String[]{getResources().getString(R.string.terms_for_usage), getResources().getString(R.string.privacy_policy)};
        for (int i = 0; i < tab.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(tab[i]));
        }
        tabLayout.setTabTextColors(getResources().getColor(R.color.black), getResources().getColor(R.color.c_EC72AD));

        tabLayout.setupWithViewPager(viewPager);

        PoliciesProvisionsFragmentAdapter adapter = new PoliciesProvisionsFragmentAdapter(getChildFragmentManager(), tab);
        viewPager.setAdapter(adapter);
    }


}
