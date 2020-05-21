package com.app.jzapp.videoapps.adapter.video;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.base.BaseBean;
import com.app.jzapp.videoapps.bean.CollectBean;
import com.app.jzapp.videoapps.bean.VideoItemBean;
import com.app.jzapp.videoapps.event.CollectMVEvent;
import com.app.jzapp.videoapps.event.CollectVideoEvent;
import com.app.jzapp.videoapps.http.ApiServiceResult;
import com.app.jzapp.videoapps.http.Client;
import com.app.jzapp.videoapps.http.RxsRxSchedulers;
import com.app.jzapp.videoapps.utils.GlideUtils;
import com.app.jzapp.videoapps.utils.ToastUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.app.jzapp.videoapps.http.AppConfig.TOKEN;

public class RandomVideoAdapter extends BaseMultiItemQuickAdapter<VideoItemBean, BaseViewHolder> {

    public RandomVideoAdapter(List<VideoItemBean> data) {
        super(data);
        setDefaultViewTypeLayout(R.layout.item_video);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final VideoItemBean item) {
        ImageView imageView = holder.getView(R.id.image);
        GlideUtils.loadImagView(mContext, item.getPic(), imageView);

        holder.setText(R.id.tv_name, item.getTitle());

        TextView tvFree = holder.getView(R.id.tv_free);
        Drawable drawable = mContext.getResources().getDrawable(R.drawable.shape_free).mutate();
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        //是否限免 1：限免 0：不限免
        if (item.getIs_free() == 1) {
            DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(mContext.getResources().getColor(R.color.c_F0982E)));
            holder.setText(R.id.tv_free, mContext.getString(R.string.free));
        } else {
            DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(mContext.getResources().getColor(R.color.c_EC72AD)));
            holder.setText(R.id.tv_free, "VIP");
        }
        tvFree.setBackground(drawable);
        //是否收藏 1：已收藏 0：未收藏
        final ImageView ivCollect = holder.getView(R.id.iv_collect);

        if (item.getIs_collect() == 1) {
            ivCollect.setImageResource(R.drawable.collected);
        } else {
            ivCollect.setImageResource(R.drawable.add_collect);
        }
        ivCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getIs_collect() == 1) {
                    delCollect(ivCollect, item);
                } else {
                    addCollect(ivCollect, item);
                }
            }
        });
    }

    private void addCollect(final ImageView iamge, final VideoItemBean item) {
        Client.getApiService().addCollect(TOKEN, item.getVid() + "", item.getOtype() + "")
                .compose(RxsRxSchedulers.<BaseBean>io_main())
                .throttleFirst(3, TimeUnit.SECONDS)
                .subscribe(new ApiServiceResult() {
                    @Override
                    public void onNext(BaseBean bean) {
                        if (bean != null && bean.getCode().equals("0")) {
                            CollectBean collectBean = new CollectBean();
                            collectBean.setOid(item.getVid());
                            collectBean.setName(item.getTitle());
                            collectBean.setPic(item.getPic());
                            collectBean.setIs_free(item.getIs_free());

                            if (item.getOtype() == 10)
                                EventBus.getDefault().post(new CollectVideoEvent("VIDEO", true, collectBean));
                            else
                                EventBus.getDefault().post(new CollectMVEvent("MV", true, collectBean));
                            iamge.setImageResource(R.drawable.collected);
                            item.setIs_collect(1);
                            ToastUtils.showShortToast(bean.getMsg());
                        }
                    }
                });
    }

    private void delCollect(final ImageView iamge, final VideoItemBean item) {
        Client.getApiService().delCollect(TOKEN, item.getVid() + "", item.getOtype() + "")
                .compose(RxsRxSchedulers.<BaseBean>io_main())
                .throttleFirst(3, TimeUnit.SECONDS)
                .subscribe(new ApiServiceResult() {
                    @Override
                    public void onNext(BaseBean bean) {
                        if (bean != null && bean.getCode().equals("0")) {
                            CollectBean collectBean = new CollectBean();
                            collectBean.setOid(item.getVid());

                            if (item.getOtype() == 10)
                                EventBus.getDefault().post(new CollectVideoEvent("VIDEO", false, collectBean));
                            else
                                EventBus.getDefault().post(new CollectMVEvent("MV", false, collectBean));
                            iamge.setImageResource(R.drawable.add_collect);
                            item.setIs_collect(0);
                            ToastUtils.showShortToast(bean.getMsg());
                        }
                    }
                });
    }
}