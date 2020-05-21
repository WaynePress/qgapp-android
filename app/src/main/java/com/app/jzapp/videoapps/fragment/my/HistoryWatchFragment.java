package com.app.jzapp.videoapps.fragment.my;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.adapter.my.HistoryWatchFragmentAdapter;
import com.app.jzapp.videoapps.base.BaseBackFragment;
import com.app.jzapp.videoapps.base.BaseBean;
import com.app.jzapp.videoapps.bean.HistoryVideoBean;
import com.app.jzapp.videoapps.http.ApiServiceResult;
import com.app.jzapp.videoapps.http.Client;
import com.app.jzapp.videoapps.http.RxsRxSchedulers;
import com.app.jzapp.videoapps.utils.DividerItemDecoration;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.app.jzapp.videoapps.http.AppConfig.TOKEN;

/**
 * 观看历史
 */
public class HistoryWatchFragment extends BaseBackFragment {
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

    private HistoryWatchFragmentAdapter adapter;
    private List<HistoryVideoBean> data;


    public static HistoryWatchFragment newInstance() {
        HistoryWatchFragment fragment = new HistoryWatchFragment();
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
        title.setText(getString(R.string.history_watch));

        setRefreshLayout();

        data = new ArrayList<>();

    }

    @Override
    protected void initData() {
        adapter = new HistoryWatchFragmentAdapter(data, _mActivity,this);
        recycler.setLayoutManager(new LinearLayoutManager(_mActivity));
        recycler.addItemDecoration(new DividerItemDecoration(_mActivity, DividerItemDecoration.VERTICAL_LIST));


        View emptyView = View.inflate(_mActivity, R.layout.empty, null);
        TextView text = emptyView.findViewById(R.id.text);
        text.setText("您还没有数据~");
        adapter.setEmptyView(emptyView);

        recycler.setAdapter(adapter);

        getData();
    }

    private void getData() {
        Client.getApiService().getLookLogs(TOKEN, page)
                .compose(RxsRxSchedulers.<BaseBean<List<HistoryVideoBean>>>io_main())
                .subscribe(new ApiServiceResult<List<HistoryVideoBean>>(getComposite()) {
                    @Override
                    public void onNext(BaseBean<List<HistoryVideoBean>> bean) {
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

    private void setData(BaseBean<List<HistoryVideoBean>> bean) {
        page++;
        data.addAll(bean.getData());
        Log.i("cjn", "播放历史查看数据：" + data.toString());
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
