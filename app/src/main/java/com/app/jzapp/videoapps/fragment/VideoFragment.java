package com.app.jzapp.videoapps.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.app.jzapp.videoapps.adapter.video.VideoFragmentAdapter;
import com.app.jzapp.videoapps.fragment.mv.FltrateFragment;
import com.app.jzapp.videoapps.fragment.mv.SearchFragment;
import com.app.jzapp.videoapps.http.ApiServiceResult;
import com.app.jzapp.videoapps.http.Client;
import com.app.jzapp.videoapps.http.DialogTransformer;
import com.app.jzapp.videoapps.http.RxsRxSchedulers;
import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.base.BaseBean;
import com.app.jzapp.videoapps.base.BaseMainFragment;
import com.app.jzapp.videoapps.bean.TypeBean;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 小视频
 */
public class VideoFragment extends BaseMainFragment {
    @BindView(R.id.paddingView)
    View paddingView;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private List<TypeBean> typeBeans;
    private int position;

    public static VideoFragment newInstance() {
        VideoFragment fragment = new VideoFragment();
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
    }


    @Override
    protected void initData() {
        super.initData();
        dialogTransformer = new DialogTransformer(_mActivity);
        Client.getApiService().getListOtype("5")
                .compose(RxsRxSchedulers.<BaseBean<List<TypeBean>>>io_main())
                .compose(dialogTransformer.transformer())
                .subscribe(new ApiServiceResult<List<TypeBean>>(getComposite()) {
                    @Override
                    public void onNext(BaseBean<List<TypeBean>> bean) {
                        if (bean != null && bean.getData() != null && !bean.getData().isEmpty())
                            setData(bean);
                    }
                });
    }

    private void setData(BaseBean<List<TypeBean>> bean) {

        typeBeans = bean.getData();

        for (TypeBean typeBean : typeBeans) {
            tabLayout.addTab(tabLayout.newTab().setText(typeBean.getOtypename()));
        }

        tabLayout.setTabTextColors(getResources().getColor(R.color.black), getResources().getColor(R.color.c_EC72AD));

        tabLayout.setupWithViewPager(viewPager);

        VideoFragmentAdapter adapter = new VideoFragmentAdapter(getChildFragmentManager(), typeBeans);
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void setListener() {
        super.setListener();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                position = tab.getPosition();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * 搜索
     */
    @OnClick(R.id.tv_search)
    void search() {
        ((MainFragment) getParentFragment()).startBrotherFragment(SearchFragment.newInstance());
    }

    /**
     * 筛选
     */
    @OnClick(R.id.tv_filtrate)
    void filtrate() {
        if (typeBeans == null){
            return;
        }
        ((MainFragment) getParentFragment()).startBrotherFragment(FltrateFragment.newInstance(typeBeans.get(position).getOtypename(), typeBeans.get(position).getOid(), "2"));
    }
}
