package com.app.jzapp.videoapps.fragment.classify;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.adapter.classify.ClassifyDetailChildFragmentAdapter;
import com.app.jzapp.videoapps.base.BaseBackFragment;
import com.app.jzapp.videoapps.base.BaseBean;
import com.app.jzapp.videoapps.base.MySupportFragment;
import com.app.jzapp.videoapps.bean.VideoItemBean;
import com.app.jzapp.videoapps.fragment.VideoPlayFragment;
import com.app.jzapp.videoapps.http.ApiServiceResult;
import com.app.jzapp.videoapps.http.Client;
import com.app.jzapp.videoapps.http.RxsRxSchedulers;
import com.app.jzapp.videoapps.utils.SpaceItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.app.jzapp.videoapps.http.AppConfig.TOKEN;

public class ClassifyDetailChildFragment extends MySupportFragment {
    private static final String ARG_FROM = "arg_from";
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;

    private int mFrom;
    private String secondotype;
    private Unbinder unbinder;

    private ClassifyDetailChildFragmentAdapter adapter;
    private List<VideoItemBean> data;

    private Map<String, String> mapParameter;


    public static ClassifyDetailChildFragment newInstance(int position, String secondotype) {
        Bundle args = new Bundle();
        args.putInt(ARG_FROM, position);
        args.putString("secondotype", secondotype);
        ClassifyDetailChildFragment fragment = new ClassifyDetailChildFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mFrom = args.getInt(ARG_FROM);
            secondotype = args.getString("secondotype");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.recyclerlayout, container, false);
            unbinder = ButterKnife.bind(this, mRootView);
            initView();
        } else {
            unbinder = ButterKnife.bind(this, mRootView);
        }

        return mRootView;
    }

    private void initView() {
        setRefreshLayout();
        data = new ArrayList<>();
        mapParameter = new HashMap<>();
        mapParameter.put("token", TOKEN);
        mapParameter.put("secondotype", secondotype);
        mapParameter.put("bestotype", (mFrom + 1) + "");

        recycler.setLayoutManager(new GridLayoutManager(_mActivity, 2));
        recycler.addItemDecoration(new SpaceItemDecoration(15, 0));


        if (data.isEmpty()) {
            refreshLayout.setEnableLoadmore(false);
        }

        adapter = new ClassifyDetailChildFragmentAdapter(data);
        View emptyView = View.inflate(_mActivity, R.layout.empty, null);
        adapter.setEmptyView(emptyView);
        recycler.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ((BaseBackFragment) getParentFragment()).start(VideoPlayFragment.newInstance(data.get(position).getVid() + ""));
            }
        });

        initData();
    }

    private void initData() {
        mapParameter.put("page", page + "");
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
                initData();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
