package com.app.jzapp.videoapps.adapter.my;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.bean.WithdrawalRecordBean;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class WithdrawalRecordFragmentAdapter extends BaseMultiItemQuickAdapter<WithdrawalRecordBean, BaseViewHolder> {

    public WithdrawalRecordFragmentAdapter(List<WithdrawalRecordBean> data) {
        super(data);
        setDefaultViewTypeLayout(R.layout.item_withdrawal_record);
    }

    @Override
    protected void convert(BaseViewHolder helper, WithdrawalRecordBean item) {
        helper.setText(R.id.tv_time, item.getTime());
        helper.setText(R.id.tv_money, "-" + item.getCash_asset() + "元");
        helper.setText(R.id.tv_name, item.getUname());
        helper.setText(R.id.tv_card_id, item.getBankcard());
    }
}
