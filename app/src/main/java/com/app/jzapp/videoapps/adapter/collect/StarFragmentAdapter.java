package com.app.jzapp.videoapps.adapter.collect;

import android.view.View;
import android.widget.ImageView;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.base.BaseBean;
import com.app.jzapp.videoapps.bean.CollectBean;
import com.app.jzapp.videoapps.event.CollectStarEvent;
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

public class StarFragmentAdapter extends BaseMultiItemQuickAdapter<CollectBean, BaseViewHolder> {


    public StarFragmentAdapter(List<CollectBean> data) {
        super(data);
        setDefaultViewTypeLayout(R.layout.item_star);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final CollectBean item) {
        ImageView image = holder.getView(R.id.image);
        GlideUtils.loadImagView(mContext, item.getPic(), image);

        holder.setText(R.id.tv_name, item.getName());

        holder.getView(R.id.tv_collect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delCollect(item, holder.getAdapterPosition());
            }
        });
    }

    private void delCollect(final CollectBean item, final int position) {
        Client.getApiService().delCollect(TOKEN, item.getOid() + "", "1")
                .compose(RxsRxSchedulers.<BaseBean>io_main())
                .throttleFirst(3, TimeUnit.SECONDS)
                .subscribe(new ApiServiceResult() {
                    @Override
                    public void onNext(BaseBean bean) {
                        if (bean != null && bean.getCode().equals("0")) {
                            mData.remove(position);
                            notifyItemRemoved(position);
                            EventBus.getDefault().post(new CollectStarEvent("COLLECT", false, item));
                            ToastUtils.showShortToast(bean.getMsg());
                        }
                    }
                });
    }
}