package com.app.jzapp.videoapps.adapter.my;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.bean.AssetDetailBean;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class ReturnsDetailedFragmentAdapter extends BaseMultiItemQuickAdapter<AssetDetailBean, BaseViewHolder> {

    public ReturnsDetailedFragmentAdapter(List<AssetDetailBean> data) {
        super(data);
        setDefaultViewTypeLayout(R.layout.item_returns_detailed);
    }

    @Override
    protected void convert(BaseViewHolder helper, AssetDetailBean item) {
        helper.setText(R.id.tv_time, item.getTime());
        helper.setText(R.id.tv_money, item.getMoney() + "元");
    }
}
