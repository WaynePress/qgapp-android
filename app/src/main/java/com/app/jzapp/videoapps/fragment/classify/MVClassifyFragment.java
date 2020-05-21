package com.app.jzapp.videoapps.fragment.classify;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.jzapp.videoapps.MyApplication;
import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.adapter.classify.MVClassifyFragmentAdapter;
import com.app.jzapp.videoapps.base.BaseBean;
import com.app.jzapp.videoapps.base.MySupportFragment;
import com.app.jzapp.videoapps.bean.TypeBean;
import com.app.jzapp.videoapps.bean.VideoItemBean;
import com.app.jzapp.videoapps.fragment.MainFragment;
import com.app.jzapp.videoapps.http.ApiServiceResult;
import com.app.jzapp.videoapps.http.Client;
import com.app.jzapp.videoapps.http.RxsRxSchedulers;
import com.app.jzapp.videoapps.utils.SpaceItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * mv分类
 */

public class MVClassifyFragment extends MySupportFragment {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;

    @BindView(R.id.lx_text)
    TextView lx_text;


    private Unbinder unbinder;

    private MVClassifyFragmentAdapter adapter;
    private List<VideoItemBean> data;

    private Map<String, String> mapParameter;

    public static MVClassifyFragment newInstance() {
        MVClassifyFragment fragment = new MVClassifyFragment();
        return fragment;
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

        if (MyApplication.lx == true) {
            lx_text.setVisibility(View.VISIBLE);
            recycler.setVisibility(View.GONE);
        } else {
            lx_text.setVisibility(View.GONE);
            recycler.setVisibility(View.VISIBLE);
        }
        setRefreshLayout();
        data = new ArrayList<>();

        recycler.setLayoutManager(new GridLayoutManager(_mActivity, 3));
        recycler.addItemDecoration(new SpaceItemDecoration(15, 0));

        adapter = new MVClassifyFragmentAdapter(data);
        View emptyView = View.inflate(_mActivity, R.layout.empty, null);

        adapter.setEmptyView(emptyView);
        recycler.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ((MainFragment) getParentFragment().getParentFragment()).startBrotherFragment(ClassifyDetailFragment.newInstance(data.get(position).getTitle(), data.get(position).getVid() + ""));
            }
        });
        initMVClassifyData();

    }

    //分类--MV 分类
    private void initMVClassifyData() {
        Client.getApiService().getVideoOtype("1", page + "", "10")
                .compose(RxsRxSchedulers.<BaseBean<List<TypeBean>>>io_main())
                .subscribe(new ApiServiceResult<List<TypeBean>>(getComposite()) {
                    @Override
                    public void onNext(BaseBean<List<TypeBean>> bean) {
                        if (bean != null && bean.getData() != null && !bean.getData().isEmpty())
                            setMVClassifyData(bean);

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

    private void setMVClassifyData(BaseBean<List<TypeBean>> bean) {
        if (page == 1) {
            data.clear();
            refreshLayout.setEnableLoadmore(true);
        }
        page++;
        for (TypeBean typeBean : bean.getData()) {
            VideoItemBean videoItemBean = new VideoItemBean();
            videoItemBean.setPic(typeBean.getPic());
            videoItemBean.setTitle(typeBean.getOtypename());
            videoItemBean.setVid(typeBean.getOid());
            videoItemBean.setIs_free(0);
            data.add(videoItemBean);
        }

        adapter.notifyDataSetChanged();
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
                initMVClassifyData();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                initMVClassifyData();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
