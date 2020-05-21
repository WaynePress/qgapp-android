package com.app.jzapp.videoapps;

import com.app.jzapp.videoapps.bean.VideoItemBean;
import com.app.video.videoapps.R;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class FiltrateFragmentAdapter extends BaseMultiItemQuickAdapter<VideoItemBean, BaseViewHolder> {


    public FiltrateFragmentAdapter(List<VideoItemBean> data) {
        super(data);
        setDefaultViewTypeLayout(R.layout.item_mv);
    }

    @Override
    protected void convert(final BaseViewHolder holder, VideoItemBean item) {

    }
}