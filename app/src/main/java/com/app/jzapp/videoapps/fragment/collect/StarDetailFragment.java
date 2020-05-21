package com.app.jzapp.videoapps.fragment.collect;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.Barrier;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.adapter.classify.StarVideoAdapter;
import com.app.jzapp.videoapps.base.BaseBackFragment;
import com.app.jzapp.videoapps.base.BaseBean;
import com.app.jzapp.videoapps.bean.CollectBean;
import com.app.jzapp.videoapps.bean.StarBean;
import com.app.jzapp.videoapps.bean.VideoItemBean;
import com.app.jzapp.videoapps.event.CollectStarEvent;
import com.app.jzapp.videoapps.fragment.VideoPlayFragment;
import com.app.jzapp.videoapps.http.ApiServiceResult;
import com.app.jzapp.videoapps.http.Client;
import com.app.jzapp.videoapps.http.RxsRxSchedulers;
import com.app.jzapp.videoapps.utils.GlideUtils;
import com.app.jzapp.videoapps.utils.SpaceItemDecoration;
import com.app.jzapp.videoapps.utils.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

import static com.app.jzapp.videoapps.http.AppConfig.TOKEN;

/**
 * 明星介绍
 */

public class StarDetailFragment extends BaseBackFragment {
    private static final String ARG_FROM = "sid";

    @BindView(R.id.paddingView)
    View paddingView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_usercount)
    TextView tvUsercount;
    @BindView(R.id.barrier)
    Barrier barrier;
    @BindView(R.id.tv_collect)
    TextView tvCollect;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.tv_production)
    TextView tvProduction;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;


    private StarVideoAdapter adapter;
    private List<VideoItemBean> data;

    private String sid;
    private boolean isSetHeadData;
    private StarBean.Star star;

    public static StarDetailFragment newInstance(String sid) {
        StarDetailFragment fragment = new StarDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FROM, sid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            sid = args.getString(ARG_FROM);
        }
    }


    @Override
    protected View setStatusBarView() {
        return paddingView;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.frament_star_detail;
    }

    @Override
    protected int statusBarColor() {
        return R.color.white;
    }

    @Override
    protected void initView() {
        super.initView();

        initToolbarNav(toolbar);

        toolbar.setTitle("");
        title.setText(getString(R.string.star_introduce));

        setRefreshLayout();
        data = new ArrayList<>();

        recycler.setLayoutManager(new GridLayoutManager(_mActivity, 2));
        recycler.addItemDecoration(new SpaceItemDecoration(15, 0));
        adapter = new StarVideoAdapter(data);

        recycler.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                start(VideoPlayFragment.newInstance(data.get(position).getVid() + ""));
            }
        });

    }

    @Override
    protected void initData() {
        Client.getApiService().getStarInfo(TOKEN, sid, page + "")
                .compose(RxsRxSchedulers.<BaseBean<StarBean>>io_main())
                .subscribe(new ApiServiceResult<StarBean>(getComposite()) {
                    @Override
                    public void onNext(BaseBean<StarBean> bean) {
                        if (bean != null && bean.getData() != null)
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

    private void setData(BaseBean<StarBean> bean) {
        if (page == 1) {
            data.clear();
            refreshLayout.setEnableLoadmore(true);
        }

        if (bean.getData().getStar() != null && !isSetHeadData) {
            isSetHeadData = true;
            star = bean.getData().getStar();
            GlideUtils.loadImagView(_mActivity, bean.getData().getStar().getPic(), ivHeader);
            tvName.setText(bean.getData().getStar().getUname());
            tvContent.setText(String.format(getString(R.string.star_production), bean.getData().getStar().getCount()));
            tvUsercount.setText(String.format(getString(R.string.star_usercount), bean.getData().getStar().getUsercount()));
            tvProduction.setText(String.format(getString(R.string.star_production2), bean.getData().getStar().getCount()));

            //是否已收藏 1：已收藏 0：未收藏
            if (bean.getData().getStar().getIs_collect() == 1) {
                setCollected(tvCollect);
            } else {
                setNoCollected(tvCollect);
            }
        }
        if (bean.getData().getDataTmp() != null && !bean.getData().getDataTmp().isEmpty()) {
            page++;
            data.addAll(bean.getData().getDataTmp());
        }
        adapter.notifyDataSetChanged();
    }

    @OnClick(R.id.tv_collect)
    void setCollect() {
        if (star.getIs_collect() == 1) {
            delCollect();
        } else {
            addCollect();
        }
    }

    private void addCollect() {
        Client.getApiService().addCollect(TOKEN, star.getSid() + "", "1")
                .compose(RxsRxSchedulers.<BaseBean>io_main())
                .throttleFirst(3, TimeUnit.SECONDS)
                .subscribe(new ApiServiceResult() {
                    @Override
                    public void onNext(BaseBean bean) {
                        if (bean != null && bean.getCode().equals("0")) {
                            star.setIs_collect(1);

                            CollectBean collectBean = new CollectBean();
                            collectBean.setOid(star.getSid());
                            collectBean.setName(star.getUname());
                            collectBean.setPic(star.getPic());

                            EventBus.getDefault().post(new CollectStarEvent("STARDETAIL", true, collectBean));
                            setCollected(tvCollect);
                            ToastUtils.showShortToast(bean.getMsg());
                        }
                    }
                });
    }

    private void delCollect() {
        Client.getApiService().delCollect(TOKEN, star.getSid() + "", "1")
                .compose(RxsRxSchedulers.<BaseBean>io_main())
                .throttleFirst(3, TimeUnit.SECONDS)
                .subscribe(new ApiServiceResult() {
                    @Override
                    public void onNext(BaseBean bean) {
                        if (bean != null && bean.getCode().equals("0")) {
                            star.setIs_collect(0);

                            CollectBean collectBean = new CollectBean();
                            collectBean.setOid(star.getSid());

                            EventBus.getDefault().post(new CollectStarEvent("STARDETAIL", false, collectBean));
                            setNoCollected(tvCollect);
                            ToastUtils.showShortToast(bean.getMsg());
                        }
                    }
                });
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
                initData();
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                initData();
            }
        });
    }

    private void setNoCollected(TextView tvCollect) {
        tvCollect.setTextColor(_mActivity.getResources().getColor(R.color.c_ff6c00));
        Drawable drawable = _mActivity.getResources().getDrawable(R.drawable.shape_box_20).mutate();
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(_mActivity.getResources().getColor(R.color.c_ff6c00)));
        tvCollect.setBackground(drawable);
    }

    private void setCollected(TextView tvCollect) {
        tvCollect.setTextColor(_mActivity.getResources().getColor(R.color.white));

        Drawable drawable = _mActivity.getResources().getDrawable(R.drawable.shape_my_vip);
        tvCollect.setBackground(drawable);
    }


}
