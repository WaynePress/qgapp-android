package com.app.jzapp.videoapps.fragment.my;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.adapter.my.WithdrawalRecordFragmentAdapter;
import com.app.jzapp.videoapps.base.BaseBackFragment;
import com.app.jzapp.videoapps.base.BaseBean;
import com.app.jzapp.videoapps.bean.WithdrawalRecordBean;
import com.app.jzapp.videoapps.http.ApiServiceResult;
import com.app.jzapp.videoapps.http.Client;
import com.app.jzapp.videoapps.http.RxsRxSchedulers;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.app.jzapp.videoapps.http.AppConfig.TOKEN;

public class WithdrawalRecordFragment extends BaseBackFragment {
    @BindView(R.id.paddingView)
    View paddingView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;

    private WithdrawalRecordFragmentAdapter adapter;
    private List<WithdrawalRecordBean> data;


    public static WithdrawalRecordFragment newInstance() {
        WithdrawalRecordFragment fragment = new WithdrawalRecordFragment();
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_recycle;
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
        title.setText(getString(R.string.withdrawal_record));

        setRefreshLayout();

        data = new ArrayList<>();

    }

    @Override
    protected void initData() {
        recycler.setLayoutManager(new LinearLayoutManager(_mActivity));
//        recycler.addItemDecoration(new DividerItemDecoration(_mActivity, DividerItemDecoration.VERTICAL_LIST));

        adapter = new WithdrawalRecordFragmentAdapter(data);

        View emptyView = View.inflate(_mActivity, R.layout.empty, null);
        TextView text = emptyView.findViewById(R.id.text);
        text.setText("您还没有数据~");
        adapter.setEmptyView(emptyView);

        recycler.setAdapter(adapter);

        getData();
    }

    private void getData() {
        Client.getApiService().getUserWithdraw(TOKEN, page, "20")
                .compose(RxsRxSchedulers.<BaseBean<List<WithdrawalRecordBean>>>io_main())
                .subscribe(new ApiServiceResult<List<WithdrawalRecordBean>>(getComposite()) {
                    @Override
                    public void onNext(BaseBean<List<WithdrawalRecordBean>> bean) {
                        if (bean != null && bean.getData() != null && !bean.getData().isEmpty()) {
                            setData(bean);
                        }

                        refreshLayout.finishLoadmore();
                        refreshLayout.finishRefreshing();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        refreshLayout.finishLoadmore();
                        refreshLayout.finishRefreshing();
                    }
                });
    }

    private void setData(BaseBean<List<WithdrawalRecordBean>> bean) {
        page++;
        data.addAll(bean.getData());
        adapter.notifyDataSetChanged();
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
                getData();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                getData();
            }
        });
    }

}
