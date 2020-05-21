package com.app.jzapp.videoapps.adapter.my;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.bean.OrderBean;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class ProjectManagementFragmentAdapter extends BaseMultiItemQuickAdapter<OrderBean, BaseViewHolder> {

    public ProjectManagementFragmentAdapter(List<OrderBean> data) {
        super(data);
        setDefaultViewTypeLayout(R.layout.item_order);
    }

    @Override
    protected void convert(final BaseViewHolder holder, OrderBean item) {
        holder.setText(R.id.tv_type, item.getTitle());
        holder.setText(R.id.tv_time, mContext.getResources().getString(R.string.pay_time) + item.getCreatetime());
        holder.setText(R.id.tv_money, item.getPrice());
    }
}