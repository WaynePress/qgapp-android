package com.app.jzapp.videoapps.fragment.video;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.jzapp.videoapps.MyApplication;
import com.app.jzapp.videoapps.adapter.mv.MVChildFragmentAdapter;
import com.app.jzapp.videoapps.adapter.video.VideoChildFragmentAdapter;
import com.app.jzapp.videoapps.event.CollectVideoEvent;
import com.app.jzapp.videoapps.event.UserLoginOther;
import com.app.jzapp.videoapps.fragment.MainFragment;
import com.app.jzapp.videoapps.fragment.WebFragment;
import com.app.jzapp.videoapps.http.ApiServiceResult;
import com.app.jzapp.videoapps.http.AppConfig;
import com.app.jzapp.videoapps.http.Client;
import com.app.jzapp.videoapps.http.RxsRxSchedulers;
import com.app.jzapp.videoapps.utils.SpaceItemDecoration;
import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.base.BaseBean;
import com.app.jzapp.videoapps.base.MySupportFragment;
import com.app.jzapp.videoapps.bean.TypeBean;
import com.app.jzapp.videoapps.bean.VideoItemBean;
import com.app.jzapp.videoapps.fragment.VideoPlayFragment;
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

public class VideoChildFragment extends MySupportFragment {
    private static final String ARG_TYPE = "arg_type";
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;
    @BindView(R.id.lx_text)
    TextView lx_text;


    private int mType;
    private Unbinder unbinder;

    private VideoChildFragmentAdapter adapter;
    private List<VideoItemBean> data;
    private Map<String, String> mapParameter;

    private TypeBean typeBeans;

    public static VideoChildFragment newInstance(int type, TypeBean typeBeans) {
        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);
        args.putParcelable("typeBeans", typeBeans);
        VideoChildFragment fragment = new VideoChildFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        Bundle args = getArguments();
        if (args != null) {
            mType = args.getInt(ARG_TYPE);
            typeBeans = args.getParcelable("typeBeans");
        }
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
        mapParameter.put("token", AppConfig.TOKEN);
        mapParameter.put("otype", "2");//视频大分类 1：mv 2：视频
        mapParameter.put("firstotype", mType + "");

        recycler.setLayoutManager(new GridLayoutManager(_mActivity, 2));
        recycler.addItemDecoration(new SpaceItemDecoration(15, 0));
        adapter = new VideoChildFragmentAdapter(data);

        View emptyView = View.inflate(_mActivity, R.layout.empty, null);

        adapter.setEmptyView(emptyView);
        recycler.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (data.get(position).getItemType() == VideoChildFragmentAdapter.TYPE_VIDEO) {
                    ((MainFragment) getParentFragment().getParentFragment()).startBrotherFragment(VideoPlayFragment.newInstance(data.get(position).getVid() + ""));
                } else {
                    if (TextUtils.isEmpty(data.get(position).getUrl())) {
                        return;
                    }
                    if (!data.get(position).getUrl().startsWith("http")) {
                        data.get(position).setUrl("http://" + data.get(position).getUrl());
                    }
                    if (!TextUtils.isEmpty(data.get(position).getUrl())) {
                        if (data.get(position).getUrlotype() == 1) {
                            ((MainFragment) getParentFragment().getParentFragment()).startBrotherFragment(WebFragment.newInstance(data.get(position).getUrl()));
                        } else {
                            Intent action = new Intent(Intent.ACTION_VIEW);
                            action.setData(Uri.parse(data.get(position).getUrl()));
                            startActivity(action);
                        }
                    }
                }
            }
        });

        initData();
    }

    private int i = 0;
    private int j = 0;
    private int jFlag = 0;

    private void initData() {
        mapParameter.put("page", page + "");
        Client.getApiService().getVideoList(mapParameter)
                .compose(RxsRxSchedulers.<BaseBean<List<VideoItemBean>>>io_main())
                .subscribe(new ApiServiceResult<List<VideoItemBean>>(getComposite()) {
                    @Override
                    public void onNext(BaseBean<List<VideoItemBean>> bean) {
                        if (bean != null)
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
            i = 0;
            j = 0;
            jFlag = 0;

            data.clear();
            refreshLayout.setEnableLoadmore(true);

            if ((bean.getData() == null || bean.getData().isEmpty()) && !typeBeans.getPics().isEmpty()) {
                for (int k = 0; k < typeBeans.getPics().size(); k++) {
                    change2VideoItemBean(k);
                }
                adapter.notifyDataSetChanged();
            }
        }

        if (bean.getData() == null || bean.getData().isEmpty()) {
            return;
        }

        page++;

        if (typeBeans.getPics().isEmpty()) {
            data.addAll(bean.getData());
        } else {
            for (VideoItemBean itemBean : bean.getData()) {
                if (i % 5 == 0) {
                    if (j >= typeBeans.getPics().size()) {
                        j = 0;
                        jFlag = 1;
                    }

                    change2VideoItemBean(j);
                    j++;
                }
                i++;
                data.add(itemBean);
            }

            if (jFlag == 0 && j < typeBeans.getPics().size()) {
                for (int k = j; k < typeBeans.getPics().size(); k++) {
                    change2VideoItemBean(k);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void change2VideoItemBean(int k) {
        VideoItemBean videoItemBean = new VideoItemBean();
        videoItemBean.setPic(typeBeans.getPics().get(k).getPic());
        videoItemBean.setTitle(typeBeans.getPics().get(k).getTitle());
        videoItemBean.setUrl(typeBeans.getPics().get(k).getUrl());
        videoItemBean.setUrlotype(typeBeans.getPics().get(k).getUrlotype());
        videoItemBean.setUrlotype(typeBeans.getPics().get(k).getUrlotype());
        videoItemBean.setItemType(MVChildFragmentAdapter.TYPE_ADVERTISING);
        data.add(videoItemBean);
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

    //在 其他页面 取消了收藏 更新着这边的页面显示
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void collectEvent(CollectVideoEvent event) {
        if (TextUtils.equals(event.getFrom(), "COLLECT") || TextUtils.equals(event.getFrom(), "VIDEOPLAY")) {
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getVid() == event.getCollectBean().getOid()) {
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

    /**
     * 切换用户
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void userInfoEvent(UserLoginOther event) {
        page = 1;
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
