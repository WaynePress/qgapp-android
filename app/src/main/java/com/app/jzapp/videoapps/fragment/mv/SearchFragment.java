package com.app.jzapp.videoapps.fragment.mv;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.adapter.classify.StarVideoAdapter;
import com.app.jzapp.videoapps.base.BaseBackFragment;
import com.app.jzapp.videoapps.base.BaseBean;
import com.app.jzapp.videoapps.bean.VideoItemBean;
import com.app.jzapp.videoapps.fragment.VideoPlayFragment;
import com.app.jzapp.videoapps.http.ApiServiceResult;
import com.app.jzapp.videoapps.http.Client;
import com.app.jzapp.videoapps.http.RxsRxSchedulers;
import com.app.jzapp.videoapps.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.app.jzapp.videoapps.http.AppConfig.TOKEN;

public class SearchFragment extends BaseBackFragment {

    @BindView(R.id.paddingView)
    View paddingView;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.btn_search)
    Button btnSearch;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;

    private StarVideoAdapter adapter;
    private List<VideoItemBean> data;
    private Map<String, String> mapParameter;

    private String searchText;

    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_search;
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
        setRefreshLayout();

        data = new ArrayList<>();
        mapParameter = new HashMap<>();
        mapParameter.put("token", TOKEN);

        recycler.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new StarVideoAdapter(data);

        View emptyView = View.inflate(_mActivity, R.layout.empty, null);
        adapter.setEmptyView(emptyView);

        recycler.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                start(VideoPlayFragment.newInstance(data.get(position).getVid() + ""));
            }
        });

    }

    @OnClick(R.id.iv_back)
    void back() {
        _mActivity.onBackPressed();
    }

    @OnClick(R.id.btn_search)
    void search() {
        searchText = etSearch.getText().toString().trim();
        if (TextUtils.isEmpty(searchText)) {
            ToastUtils.showShortToast(getString(R.string.search_hint));
            return;
        }
        page = 1;
        getSearchData();
    }

    private void getSearchData() {
        mapParameter.put("page", page + "");
        mapParameter.put("search", searchText);
        Client.getApiService().getVideoList(mapParameter)
                .compose(RxsRxSchedulers.<BaseBean<List<VideoItemBean>>>io_main())
                .subscribe(new ApiServiceResult<List<VideoItemBean>>(getComposite()) {
                    @Override
                    public void onNext(BaseBean<List<VideoItemBean>> bean) {
                        if (bean != null && bean.getData() != null && !bean.getData().isEmpty())
                            setData(bean);

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

    private void setData(BaseBean<List<VideoItemBean>> bean) {
        if (page == 1) {
            data.clear();
            refreshLayout.setEnableLoadmore(true);
        }
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
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                getSearchData();
            }
        });
    }


}
