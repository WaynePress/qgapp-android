package com.app.jzapp.videoapps.fragment.my;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.adapter.my.ProjectManagementFragmentAdapter;
import com.app.jzapp.videoapps.base.BaseBackFragment;
import com.app.jzapp.videoapps.base.BaseBean;
import com.app.jzapp.videoapps.bean.OrderBean;
import com.app.jzapp.videoapps.bean.OrderDataBean;
import com.app.jzapp.videoapps.http.ApiServiceResult;
import com.app.jzapp.videoapps.http.Client;
import com.app.jzapp.videoapps.http.RxsRxSchedulers;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.app.jzapp.videoapps.http.AppConfig.TOKEN;

public class ProjectManagementFragment extends BaseBackFragment {
    @BindView(R.id.paddingView)
    View paddingView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;
    @BindView(R.id.tv_open_vip)
    TextView tvOpenVip;
    @BindView(R.id.rl_no_vip)
    RelativeLayout rlNoVip;
    @BindView(R.id.tv_current_project_name)
    TextView tvCurrentProjectName;
    @BindView(R.id.tv_current_project_period_validity)
    TextView tvCurrentProjectPeriodValidity;
    @BindView(R.id.tv_current_project_auto_deduction)
    TextView tvCurrentProjectAutoDeduction;
    @BindView(R.id.tv_current_project_buy_type)
    TextView tvCurrentProjectBuyType;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinator;

    private List<OrderBean> data;

    private ProjectManagementFragmentAdapter adapter;

    public static ProjectManagementFragment newInstance() {
        ProjectManagementFragment fragment = new ProjectManagementFragment();
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_project_management;
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
        title.setText(getString(R.string.project_management));

    }

    // TODO: 2018/11/12  立即升级 的逻辑 升级后 刷新本页面

    @Override
    protected void initData() {
        super.initData();

        setRefreshLayout();

        data = new ArrayList<>();
        recycler.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new ProjectManagementFragmentAdapter(data);
        recycler.setAdapter(adapter);

        getData();
    }

    private void getData() {
        Client.getApiService().getOrderList(TOKEN, page)
                .compose(RxsRxSchedulers.<BaseBean<OrderDataBean>>io_main())
                .subscribe(new ApiServiceResult<OrderDataBean>(getComposite()) {
                    @Override
                    public void onNext(BaseBean<OrderDataBean> bean) {
                        super.onNext(bean);
                        if (bean.getData() != null) {
                            setData(bean.getData());
                        } else {
                            if (page == 1)
                                rlNoVip.setVisibility(View.VISIBLE);
                        }

                        refreshLayout.finishLoadmore();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        refreshLayout.finishLoadmore();
                    }
                });
    }

    private void setData(OrderDataBean data) {
        coordinator.setVisibility(View.VISIBLE);
        if (data.getOrder() != null) {
            if (page == 1) {
                tvCurrentProjectName.setText(data.getOrder().getTitle());
                tvCurrentProjectPeriodValidity.setText(data.getOrder().getVipendtime());
                tvCurrentProjectBuyType.setText(data.getOrder().getPayotype());
            }
        } else {
            collapsingToolbar.setVisibility(View.GONE);
        }

        if (data.getData() != null && !data.getData().isEmpty()) {
            page++;
            this.data.addAll(data.getData());
            adapter.notifyDataSetChanged();
        }

    }


    @OnClick({R.id.tv_open_vip})
    void vip() {
        start(VIPFragment.newInstance());
    }

    private void setRefreshLayout() {
        refreshLayout.setEnableLoadmore(true);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableOverScroll(false);
        refreshLayout.setHeaderHeight(55);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                page = 1;

            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                getData();
            }
        });
    }


}
