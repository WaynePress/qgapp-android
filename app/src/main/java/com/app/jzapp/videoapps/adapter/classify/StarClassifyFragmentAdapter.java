package com.app.jzapp.videoapps.adapter.classify;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.adapter.mv.FiltrateHeadAdapter;
import com.app.jzapp.videoapps.base.BaseBean;
import com.app.jzapp.videoapps.bean.CollectBean;
import com.app.jzapp.videoapps.bean.FltrateBean;
import com.app.jzapp.videoapps.bean.StarItemBean;
import com.app.jzapp.videoapps.event.CollectStarEvent;
import com.app.jzapp.videoapps.event.FiltrateEvent;
import com.app.jzapp.videoapps.http.ApiServiceResult;
import com.app.jzapp.videoapps.http.Client;
import com.app.jzapp.videoapps.http.RxsRxSchedulers;
import com.app.jzapp.videoapps.utils.GlideUtils;
import com.app.jzapp.videoapps.utils.ToastUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.app.jzapp.videoapps.http.AppConfig.TOKEN;

public class StarClassifyFragmentAdapter extends BaseMultiItemQuickAdapter<StarItemBean, BaseViewHolder> {

    public static final int TYPE_STAR = -0xff;
    public static final int TYPE_HEAD = 1;
    private Map<Integer, Integer> filtrateMap = new HashMap<>();

    public StarClassifyFragmentAdapter(List<StarItemBean> data) {
        super(data);
        addItemType(TYPE_STAR, R.layout.item_star);
        addItemType(TYPE_HEAD, R.layout.item_filtrate_head);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final StarItemBean item) {
        switch (holder.getItemViewType()) {
            case TYPE_HEAD:
                holder.setIsRecyclable(false);
                holder.setText(R.id.tv_title, item.getFltrateBean().getOtypename());
                RecyclerView recycler = holder.getView(R.id.recycler);
                LinearLayoutManager manager = new LinearLayoutManager(mContext);
                manager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recycler.setLayoutManager(manager);
                final FiltrateHeadAdapter filtrateHeadAdapter = new FiltrateHeadAdapter(item.getFltrateBean().getParam());
                if (filtrateMap.containsKey(item.getFltrateBean().getOid())) {
                    filtrateHeadAdapter.setSelectOid(filtrateMap.get(item.getFltrateBean().getOid()));
                }
                recycler.setAdapter(filtrateHeadAdapter);
                filtrateHeadAdapter.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        FltrateBean.ParamBean paramBean = item.getFltrateBean().getParam().get(position);
                        if (paramBean.getOid() == filtrateHeadAdapter.getSelectOid()) {
                            filtrateHeadAdapter.setSelectOid(-1);
                            filtrateHeadAdapter.notifyDataSetChanged();
                            filtrateMap.remove(item.getFltrateBean().getOid());
                        } else {
                            filtrateHeadAdapter.setSelectOid(paramBean.getOid());
                            filtrateHeadAdapter.notifyDataSetChanged();
                            filtrateMap.put(item.getFltrateBean().getOid(), paramBean.getOid());
                        }
                        Collection<Integer> collection = filtrateMap.values();
                        String s = collection.toString().replace("[", "").replace("]", "").replace(" ", "");
                        EventBus.getDefault().post(new FiltrateEvent(s));
                    }
                });
                break;
            case TYPE_STAR:
                ImageView image = holder.getView(R.id.image);
                GlideUtils.loadCircleImagView(mContext, item.getPic(), image);

                holder.setText(R.id.tv_name, item.getUname());

                //是否已收藏 1：已收藏 0：未收藏
                final TextView tvCollect = holder.getView(R.id.tv_collect);
                if (item.getIs_collect() == 1) {
                    setCollected(tvCollect);
                } else {
                    setNoCollected(tvCollect);
                }

                tvCollect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.getIs_collect() == 1) {
                            delCollect(tvCollect, item);
                        } else {
                            addCollect(tvCollect, item);
                        }
                    }
                });
                break;
        }
    }

    private void setNoCollected(TextView tvCollect) {
        tvCollect.setTextColor(mContext.getResources().getColor(R.color.c_ff6c00));
        Drawable drawable = mContext.getResources().getDrawable(R.drawable.shape_box_20).mutate();
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(mContext.getResources().getColor(R.color.c_ff6c00)));
        tvCollect.setBackground(drawable);
    }

    private void setCollected(TextView tvCollect) {
        tvCollect.setTextColor(mContext.getResources().getColor(R.color.white));

        Drawable drawable = mContext.getResources().getDrawable(R.drawable.shape_my_vip);
        tvCollect.setBackground(drawable);
    }


    private void addCollect(final TextView tvCollect, final StarItemBean item) {
        Client.getApiService().addCollect(TOKEN, item.getSid() + "", "1")
                .compose(RxsRxSchedulers.<BaseBean>io_main())
                .throttleFirst(3, TimeUnit.SECONDS)
                .subscribe(new ApiServiceResult() {
                    @Override
                    public void onNext(BaseBean bean) {
                        if (bean != null && bean.getCode().equals("0")) {
                            CollectBean collectBean = new CollectBean();
                            collectBean.setOid(item.getSid());
                            collectBean.setName(item.getUname());
                            collectBean.setPic(item.getPic());

                            EventBus.getDefault().post(new CollectStarEvent("CLASSIFY", true, collectBean));
                            setCollected(tvCollect);
                            item.setIs_collect(1);
                            ToastUtils.showShortToast(bean.getMsg());
                        }
                    }
                });
    }

    private void delCollect(final TextView tvCollect, final StarItemBean item) {
        Client.getApiService().delCollect(TOKEN, item.getSid() + "", "1")
                .compose(RxsRxSchedulers.<BaseBean>io_main())
                .throttleFirst(3, TimeUnit.SECONDS)
                .subscribe(new ApiServiceResult() {
                    @Override
                    public void onNext(BaseBean bean) {
                        if (bean != null && bean.getCode().equals("0")) {
                            CollectBean collectBean = new CollectBean();
                            collectBean.setOid(item.getSid());

                            EventBus.getDefault().post(new CollectStarEvent("CLASSIFY", false, collectBean));
                            setNoCollected(tvCollect);
                            item.setIs_collect(0);
                            ToastUtils.showShortToast(bean.getMsg());
                        }
                    }
                });
    }
}