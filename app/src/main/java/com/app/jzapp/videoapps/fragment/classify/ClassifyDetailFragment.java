package com.app.jzapp.videoapps.fragment.classify;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.adapter.classify.ClassifyDetailFragmentAdapter;
import com.app.jzapp.videoapps.base.BaseBackFragment;

import butterknife.BindView;

/**
 * 分类之后的页面
 */

public class ClassifyDetailFragment extends BaseBackFragment {
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

    private String mtitle, secondotype;

    public static ClassifyDetailFragment newInstance(String title, String secondotype) {
        ClassifyDetailFragment fragment = new ClassifyDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("secondotype", secondotype);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mtitle = bundle.getString("title");
            secondotype = bundle.getString("secondotype");
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_classify_detail;
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
        title.setText(mtitle);

        String[] mTitles = new String[]{getString(R.string.newest), getString(R.string.hotest2)};

        for (int i = 0; i < mTitles.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(mTitles[i]));
        }

        tabLayout.setTabTextColors(getResources().getColor(R.color.black), getResources().getColor(R.color.c_EC72AD));

        tabLayout.setupWithViewPager(viewPager);

        ClassifyDetailFragmentAdapter adapter = new ClassifyDetailFragmentAdapter(getChildFragmentManager(), mTitles, secondotype);
        viewPager.setAdapter(adapter);
    }


}
