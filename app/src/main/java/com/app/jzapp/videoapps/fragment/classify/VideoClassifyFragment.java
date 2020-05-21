package com.app.jzapp.videoapps.fragment.classify;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.jzapp.videoapps.MyApplication;
import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.adapter.classify.VideoClassifyFragmentAdapter;
import com.app.jzapp.videoapps.base.BaseBean;
import com.app.jzapp.videoapps.base.MySupportFragment;
import com.app.jzapp.videoapps.bean.TypeBean;
import com.app.jzapp.videoapps.bean.VideoItemBean;
import com.app.jzapp.videoapps.fragment.MainFragment;
import com.app.jzapp.videoapps.http.ApiServiceResult;
import com.app.jzapp.videoapps.http.Client;
import com.app.jzapp.videoapps.http.RxsRxSchedulers;
import com.app.jzapp.videoapps.utils.SizeUtils;
import com.app.jzapp.videoapps.utils.SpaceItemDecoration;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 *
 */

public class VideoClassifyFragment extends MySupportFragment {
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.flow)
    FlowLayout flow;
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinator;
    @BindView(R.id.lx_text)
    TextView lx_text;



    private Unbinder unbinder;

    private VideoClassifyFragmentAdapter adapter;
    private List<VideoItemBean> data;
    private List<TypeBean> types;

    public static VideoClassifyFragment newInstance() {
        VideoClassifyFragment fragment = new VideoClassifyFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_video_classify, container, false);
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
            coordinator.setVisibility(View.GONE);
        } else {
            lx_text.setVisibility(View.GONE);
            coordinator.setVisibility(View.VISIBLE);
        }


        data = new ArrayList<>();
        types = new ArrayList<>();

        recycler.setLayoutManager(new GridLayoutManager(_mActivity, 3));
        recycler.addItemDecoration(new SpaceItemDecoration(15, 0));
        adapter = new VideoClassifyFragmentAdapter(data);
        View emptyView = View.inflate(_mActivity, R.layout.empty, null);
        adapter.setEmptyView(emptyView);
        recycler.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ((MainFragment) getParentFragment().getParentFragment()).startBrotherFragment(ClassifyDetailFragment.newInstance(data.get(position).getTitle(), data.get(position).getVid() + ""));
            }
        });

        initVideoClassifyData();
    }

    //分类--视频 分类
    private void initVideoClassifyData() {
        Client.getApiService().getVideoOtype("5", page + "", "200")
                .compose(RxsRxSchedulers.<BaseBean<List<TypeBean>>>io_main())
                .subscribe(new ApiServiceResult<List<TypeBean>>(getComposite()) {
                    @Override
                    public void onNext(BaseBean<List<TypeBean>> bean) {
                        if (bean != null && bean.getData() != null && !bean.getData().isEmpty())
                            setVideoClassifyData(bean);

                    }
                });
    }

    private void setVideoClassifyData(BaseBean<List<TypeBean>> bean) {

        for (int i = 0; i < bean.getData().size(); i++) {
            if (i < 6) {
                VideoItemBean videoItemBean = new VideoItemBean();
                videoItemBean.setPic(bean.getData().get(i).getPic());
                videoItemBean.setTitle(bean.getData().get(i).getOtypename());
                videoItemBean.setVid(bean.getData().get(i).getOid());
                videoItemBean.setIs_free(0);
                data.add(videoItemBean);
            } else {
                types.add(bean.getData().get(i));
            }
        }
        adapter.notifyDataSetChanged();

        if (!types.isEmpty()) {
            for (TypeBean typeBean : types) {
                TextView textView = buildLabel(typeBean );
                flow.addView(textView);
            }
        }
    }

    private TextView buildLabel(final TypeBean typeBean) {
        String str = typeBean.getOtypename();
        final TextView textView = new TextView(_mActivity);
        textView.setText(str);
        textView.setTextColor(_mActivity.getResources().getColor(R.color.c_646464));
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        textView.setPadding(SizeUtils.dp2px(15), 0, SizeUtils.dp2px(15), 0);

        Resources resources = _mActivity.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.shape_box_20).mutate();
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(getResources().getColor(R.color.c_EC72AD)));

        textView.setBackground(drawable);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, SizeUtils.dp2px(25));
        textView.setLayoutParams(lp);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainFragment) getParentFragment().getParentFragment()).startBrotherFragment(ClassifyDetailFragment.newInstance(typeBean.getOtypename(), typeBean.getOid() + ""));
            }
        });

        return textView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
