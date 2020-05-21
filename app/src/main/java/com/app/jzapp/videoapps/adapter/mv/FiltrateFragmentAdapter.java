package com.app.jzapp.videoapps.adapter.mv;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.base.BaseBean;
import com.app.jzapp.videoapps.bean.CollectBean;
import com.app.jzapp.videoapps.bean.FltrateBean;
import com.app.jzapp.videoapps.bean.VideoItemBean;
import com.app.jzapp.videoapps.event.CollectVideoEvent;
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

public class FiltrateFragmentAdapter extends BaseMultiItemQuickAdapter<VideoItemBean, BaseViewHolder> {

    public static final int TYPE_VIDEO = -0xff;
    public static final int TYPE_HEAD = 1;

    private Map<Integer, Integer> filtrateMap = new HashMap<>();

    public FiltrateFragmentAdapter(List<VideoItemBean> data) {
        super(data);
        addItemType(TYPE_VIDEO, R.layout.item_mv);
        addItemType(TYPE_HEAD, R.layout.item_filtrate_head);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final VideoItemBean item) {
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
            case TYPE_VIDEO:
                ImageView imageView = holder.getView(R.id.image);
                GlideUtils.loadImagView(mContext, item.getPic(), imageView);

                holder.setText(R.id.tv_name, item.getTitle());

                //是否限免 1：限免 0：不限免
                if (item.getIs_free() == 1) {
                    holder.setVisible(R.id.tv_free, true);
                } else {
                    holder.setVisible(R.id.tv_free, false);
                }

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
                break;
        }
    }

    private void addCollect(final ImageView iamge, final VideoItemBean item) {
        Client.getApiService().addCollect(TOKEN, item.getVid() + "", "10")
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

                            EventBus.getDefault().post(new CollectVideoEvent("VIDEO", true, collectBean));
                            iamge.setImageResource(R.drawable.collected);
                            item.setIs_collect(1);
                            ToastUtils.showShortToast(bean.getMsg());
                        }
                    }
                });
    }

    private void delCollect(final ImageView iamge, final VideoItemBean item) {
        Client.getApiService().delCollect(TOKEN, item.getVid() + "", "10")
                .compose(RxsRxSchedulers.<BaseBean>io_main())
                .throttleFirst(3, TimeUnit.SECONDS)
                .subscribe(new ApiServiceResult() {
                    @Override
                    public void onNext(BaseBean bean) {
                        if (bean != null && bean.getCode().equals("0")) {
                            CollectBean collectBean = new CollectBean();
                            collectBean.setOid(item.getVid());

                            EventBus.getDefault().post(new CollectVideoEvent("VIDEO", false, collectBean));
                            iamge.setImageResource(R.drawable.add_collect);
                            item.setIs_collect(0);
                            ToastUtils.showShortToast(bean.getMsg());
                        }
                    }
                });
    }
}