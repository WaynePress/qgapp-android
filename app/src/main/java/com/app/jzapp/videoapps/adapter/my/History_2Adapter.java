package com.app.jzapp.videoapps.adapter.my;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.bean.TInmeData;
import com.app.jzapp.videoapps.utils.GlideUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.Formatter;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;

public class History_2Adapter extends BaseMultiItemQuickAdapter<TInmeData, BaseViewHolder> {


    @BindView(R.id.recycler)
    RecyclerView recycler;

    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;

    @BindView(R.id.history_text)
    TextView history_text;

    FragmentActivity _mActivity;


    private History_2Adapter adapter;

    public History_2Adapter(List<TInmeData> data, FragmentActivity _mActivity) {
        super(data);
        setDefaultViewTypeLayout(R.layout.item_histroy2);
        this._mActivity = _mActivity;
    }

    @Override
    protected void convert(BaseViewHolder helper, TInmeData item) {

        helper.setText(R.id.tv_title, item.getTitle());
        helper.setText(R.id.tv_time, "您看到了" + stringForTime(Long.parseLong(item.getLooktime())));
        ImageView imageView = helper.getView(R.id.iv_iamge);
        GlideUtils.loadImagView(mContext, item.getPic(), imageView);


    }
//    stringForTime(

    protected String stringForTime(long timeMs) {
        if (timeMs <= 0 || timeMs >= 24 * 60 * 60 * 1000) {
            return "00:00:00";
        }
        long totalSeconds = timeMs / 1000;
        int seconds = (int) (totalSeconds % 60);
        int minutes = (int) ((totalSeconds / 60) % 60);
        int hours = (int) (totalSeconds / 3600);
        StringBuilder stringBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(stringBuilder, Locale.getDefault());
        return mFormatter.format("%02d小时%02d分%02d秒", hours, minutes, seconds).toString();
    }
}
