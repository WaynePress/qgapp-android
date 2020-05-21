package com.app.jzapp.videoapps.adapter.classify;

import android.widget.ImageView;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.bean.VideoItemBean;
import com.app.jzapp.videoapps.utils.GlideUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class VideoClassifyFragmentAdapter extends BaseMultiItemQuickAdapter<VideoItemBean, BaseViewHolder> {

    public VideoClassifyFragmentAdapter(List<VideoItemBean> data) {
        super(data);
        setDefaultViewTypeLayout(R.layout.item_video_classify);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final VideoItemBean item) {
        ImageView imageView = holder.getView(R.id.image);
        GlideUtils.loadImagView(mContext, item.getPic(), imageView);

        holder.setText(R.id.tv_name, item.getTitle());
    }

}