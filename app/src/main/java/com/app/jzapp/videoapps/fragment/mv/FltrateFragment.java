package com.app.jzapp.videoapps.fragment.mv;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.adapter.mv.FiltrateFragmentAdapter;
import com.app.jzapp.videoapps.base.BaseBackFragment;
import com.app.jzapp.videoapps.base.BaseBean;
import com.app.jzapp.videoapps.bean.FltrateBean;
import com.app.jzapp.videoapps.bean.VideoItemBean;
import com.app.jzapp.videoapps.event.FiltrateEvent;
import com.app.jzapp.videoapps.fragment.VideoPlayFragment;
import com.app.jzapp.videoapps.http.ApiServiceResult;
import com.app.jzapp.videoapps.http.Client;
import com.app.jzapp.videoapps.http.RxsRxSchedulers;
import com.app.jzapp.videoapps.utils.HeadSpaceItemDecoration;
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

import static com.app.jzapp.videoapps.adapter.mv.FiltrateFragmentAdapter.TYPE_HEAD;
import static com.app.jzapp.videoapps.bean.VideoItemBean.HEAD;
import static com.app.jzapp.videoapps.http.AppConfig.TOKEN;

public class FltrateFragment extends BaseBackFragment {
    private static final String ARG_TYPE = "arg_type";
    private static final String ARG_OTYPE = "arg_otype";
    private static final String ARG_FROM = "title";
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

    private FiltrateFragmentAdapter adapter;
    private List<VideoItemBean> data;
    private List<FltrateBean> headData;

    private Map<String, String> mapParameter;
    private String mTitle, otype;
    private int mType;

    /**
     * @param mTitle
     * @param type   firstotype
     * @param otype  //视频大分类 1：mv 2：视频
     * @return
     */
    public static FltrateFragment newInstance(String mTitle, int type, String otype) {
        FltrateFragment fragment = new FltrateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FROM, mTitle);
        args.putInt(ARG_TYPE, type);
        args.putString(ARG_OTYPE, otype);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        Bundle args = getArguments();
        if (args != null) {
            mTitle = args.getString(ARG_FROM);
            otype = args.getString(ARG_OTYPE);
            mType = args.getInt(ARG_TYPE);
        }
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
        title.setText(mTitle);

        setRefreshLayout();

        data = new ArrayList<>();

        mapParameter = new HashMap<>();
        mapParameter.put("token", TOKEN);
        mapParameter.put("otype", otype);//视频大分类 1：mv 2：视频
        mapParameter.put("firstotype", mType + "");
    }

    @Override
    protected void initData() {
        getHeadData();
    }

    private void getHeadData() {
        String otype;//筛选分类 1：明星 5：排行 10 其他
        if (TextUtils.equals(mTitle, getString(R.string.rank_list))) {//排行
            otype = "5";
        } else {
            otype = "10";
        }
        Client.getApiService().getScreenOtype(otype)
                .compose(RxsRxSchedulers.<BaseBean<List<FltrateBean>>>io_main())
                .subscribe(new ApiServiceResult<List<FltrateBean>>(getComposite()) {
                    @Override
                    public void onNext(BaseBean<List<FltrateBean>> bean) {
                        if (bean != null && bean.getData() != null && !bean.getData().isEmpty()) {
                            setHeadData(bean.getData());
                        }
                        getData();
                    }
                });
    }

    private void setHeadData(List<FltrateBean> headData) {
        this.headData = headData;
        if (TextUtils.equals(mTitle, getString(R.string.rank_list))) {//排行
            recycler.setLayoutManager(new LinearLayoutManager(_mActivity));
        } else {
            recycler.setLayoutManager(new GridLayoutManager(_mActivity, 2));
            recycler.addItemDecoration(new HeadSpaceItemDecoration(15, 0, headData.size() - 1));
        }

        adapter = new FiltrateFragmentAdapter(data);

        View emptyView = View.inflate(_mActivity, R.layout.empty, null);
        adapter.setEmptyView(emptyView);

        adapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return data.get(position).getSpanSize();
            }
        });
        recycler.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (data.get(position).getItemType() == FiltrateFragmentAdapter.TYPE_VIDEO)
                   start(VideoPlayFragment.newInstance(data.get(position).getVid() + ""));
            }
        });

        for (FltrateBean fltrateBean : headData) {
            VideoItemBean bean = new VideoItemBean(TYPE_HEAD, HEAD);
            bean.setFltrateBean(fltrateBean);
            data.add(bean);
        }
    }

    private void getData() {
        mapParameter.put("page", page + "");
        Client.getApiService().getVideoList(mapParameter)
                .compose(RxsRxSchedulers.<BaseBean<List<VideoItemBean>>>io_main())
                .subscribe(new ApiServiceResult<List<VideoItemBean>>(getComposite()) {
                    @Override
                    public void onNext(BaseBean<List<VideoItemBean>> bean) {
                        if (bean != null && bean.getData() != null && !bean.getData().isEmpty())
                            setData(bean);
                        else {
                            if (page == 1) {
                                for (int i = data.size(); i > headData.size(); i--) {
                                    data.remove(i - 1);
                                }
                                adapter.notifyDataSetChanged();
                            }
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

    private void setData(BaseBean<List<VideoItemBean>> bean) {
        if (page == 1) {
            for (int i = data.size(); i > headData.size(); i--) {
                data.remove(i - 1);
            }
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
                getData();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                getData();
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void filtrateEvent(FiltrateEvent event) {
        if (!TextUtils.isEmpty(event.getScreenotype())) {
            mapParameter.put("screenotype", event.getScreenotype());
        } else {
            mapParameter.remove("screenotype");
        }
        page = 1;
        getData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
