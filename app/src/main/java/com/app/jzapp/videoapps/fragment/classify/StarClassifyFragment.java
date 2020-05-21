package com.app.jzapp.videoapps.fragment.classify;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.jzapp.videoapps.MyApplication;
import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.adapter.classify.StarClassifyFragmentAdapter;
import com.app.jzapp.videoapps.base.BaseBean;
import com.app.jzapp.videoapps.base.MySupportFragment;
import com.app.jzapp.videoapps.bean.FltrateBean;
import com.app.jzapp.videoapps.bean.StarItemBean;
import com.app.jzapp.videoapps.event.CollectStarEvent;
import com.app.jzapp.videoapps.event.FiltrateEvent;
import com.app.jzapp.videoapps.fragment.MainFragment;
import com.app.jzapp.videoapps.fragment.collect.StarDetailFragment;
import com.app.jzapp.videoapps.http.ApiServiceResult;
import com.app.jzapp.videoapps.http.Client;
import com.app.jzapp.videoapps.http.RxsRxSchedulers;
import com.app.jzapp.videoapps.utils.SpaceItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.app.jzapp.videoapps.adapter.mv.FiltrateFragmentAdapter.TYPE_HEAD;
import static com.app.jzapp.videoapps.bean.StarItemBean.HEAD;
import static com.app.jzapp.videoapps.http.AppConfig.TOKEN;

/**
 * 明星列表
 */

public class StarClassifyFragment extends MySupportFragment {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.lx_text)
    TextView lx_text;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;

    private Unbinder unbinder;

    private StarClassifyFragmentAdapter adapter;
    private List<StarItemBean> data;
    private Map<String, String> mapParameter;
    private List<FltrateBean> headData;

    public static StarClassifyFragment newInstance() {
        StarClassifyFragment fragment = new StarClassifyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.recyclerlayout2, container, false);
            unbinder = ButterKnife.bind(this, mRootView);
            initView();
        } else {
            unbinder = ButterKnife.bind(this, mRootView);
        }

        return mRootView;
    }

    private void initView() {

        if (MyApplication.lx == true) {
            lx_text.setVisibility(View.VISIBLE);
            recycler.setVisibility(View.GONE);
        } else {
            lx_text.setVisibility(View.GONE);
            recycler.setVisibility(View.VISIBLE);
        }


        setRefreshLayout();
        data = new ArrayList<>();

        mapParameter = new HashMap<>();
        mapParameter.put("token", TOKEN);

        getHeadData();
    }

    private void getHeadData() {
        String otype = "1";//筛选分类 1：明星 5：排行 10 其他
        Client.getApiService().getScreenOtype(otype)
                .compose(RxsRxSchedulers.<BaseBean<List<FltrateBean>>>io_main())
                .subscribe(new ApiServiceResult<List<FltrateBean>>(getComposite()) {
                    @Override
                    public void onNext(BaseBean<List<FltrateBean>> bean) {
                        if (bean != null && bean.getData() != null && !bean.getData().isEmpty()) {
                            setHeadData(bean.getData());
                        }
                        initData();
                    }
                });
    }

    private void setHeadData(List<FltrateBean> headData) {
        this.headData = headData;
        recycler.setLayoutManager(new GridLayoutManager(_mActivity, 4));
        recycler.addItemDecoration(new SpaceItemDecoration(15, 0));
        adapter = new StarClassifyFragmentAdapter(data);
        View emptyView = View.inflate(_mActivity, R.layout.empty2, null);
        adapter.setEmptyView(emptyView);

        emptyView.findViewById(R.id.tv_look).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainFragment) getParentFragment().getParentFragment()).setSelectBar(2);
            }
        });

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (data.get(position).getItemType() == StarClassifyFragmentAdapter.TYPE_STAR)
                    ((MainFragment) getParentFragment().getParentFragment()).startBrotherFragment(StarDetailFragment.newInstance(data.get(position).getSid() + ""));
            }
        });


        adapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return data.get(position).getSpanSize();
            }
        });
        recycler.setAdapter(adapter);

        for (FltrateBean fltrateBean : headData) {
            StarItemBean bean = new StarItemBean(TYPE_HEAD, HEAD);
            bean.setFltrateBean(fltrateBean);
            data.add(bean);
        }
    }

    private void initData() {
        mapParameter.put("page", page + "");
        Client.getApiService().getStarList(mapParameter)
                .compose(RxsRxSchedulers.<BaseBean<List<StarItemBean>>>io_main())
                .subscribe(new ApiServiceResult<List<StarItemBean>>(getComposite()) {
                    @Override
                    public void onNext(BaseBean<List<StarItemBean>> bean) {
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

    private void setData(BaseBean<List<StarItemBean>> bean) {
        if (page == 1) {
            for (int i = data.size(); i > headData.size(); i--) {
                data.remove(i - 1);
            }
            refreshLayout.setEnableLoadmore(true);
        }
        page++;
        data.addAll(bean.getData());
        adapter.notifyDataSetChanged();
    }

    //在 其他页面 取消了收藏 更新着这边的页面显示
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void collectEvent(CollectStarEvent event) {
        if (TextUtils.equals(event.getFrom(), "COLLECT") || TextUtils.equals(event.getFrom(), "STARDETAIL") || TextUtils.equals(event.getFrom(), "PLAYSTSR")) {
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getSid() == event.getCollectBean().getOid()) {
                    if (event.isAdd()) {
                        data.get(i).setIs_collect(1);
                    } else {
                        data.get(i).setIs_collect(0);
                    }
                    adapter.notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    private void setRefreshLayout() {
        refreshLayout.setEnableLoadmore(true);
        refreshLayout.setEnableRefresh(true);
        refreshLayout.setEnableOverScroll(false);
        refreshLayout.setHeaderHeight(55);
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                page = 1;
                initData();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    void filtrateEvent(FiltrateEvent event) {
        if (!TextUtils.isEmpty(event.getScreenotype())) {
            mapParameter.put("screenotype", event.getScreenotype());
        } else {
            mapParameter.remove("screenotype");
        }
        page = 1;
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
