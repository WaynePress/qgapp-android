package com.app.jzapp.videoapps.adapter.mv;

import android.widget.TextView;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.bean.FltrateBean;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class FiltrateHeadAdapter extends BaseMultiItemQuickAdapter<FltrateBean.ParamBean, BaseViewHolder> {

    private int selectOid = -1;

    public FiltrateHeadAdapter(List<FltrateBean.ParamBean> data) {
        super(data);
        setDefaultViewTypeLayout(R.layout.item_filtrate_head_item);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final FltrateBean.ParamBean item) {
        holder.setText(R.id.tv_name, item.getOtypename());
        TextView textView = holder.getView(R.id.tv_name);
        if (item.getOid() == selectOid) {
            textView.setTextColor(mContext.getResources().getColor(R.color.c_EC72AD));
        } else {
            textView.setTextColor(mContext.getResources().getColor(R.color.c_646464));
        }
    }

    public void setSelectOid(int selectOid) {
        this.selectOid = selectOid;
    }

    public int getSelectOid() {
        return selectOid;
    }
}