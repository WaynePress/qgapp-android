package com.app.jzapp.videoapps.adapter.my;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.app.jzapp.videoapps.fragment.my.HistoryWatchFragment;
import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.bean.HistoryVideoBean;
import com.app.jzapp.videoapps.fragment.VideoPlayFragment;
import com.app.jzapp.videoapps.utils.DividerItemDecoration;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.List;

import butterknife.BindView;

public class HistoryWatchFragmentAdapter extends BaseMultiItemQuickAdapter<HistoryVideoBean, BaseViewHolder> {


    RecyclerView recycler;
    ListView list_list;

    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;

    @BindView(R.id.history_text)
    TextView history_text;

    FragmentActivity _mActivity;
    HistoryWatchFragment fragment;

    private History_2Adapter adapter;

    public HistoryWatchFragmentAdapter(List<HistoryVideoBean> data, FragmentActivity _mActivity, HistoryWatchFragment fragment) {
        super(data);
        setDefaultViewTypeLayout(R.layout.item_histroy);
        this._mActivity = _mActivity;
        this.fragment = fragment;
    }

    @Override
    protected void convert(BaseViewHolder helper, final HistoryVideoBean item) {
        helper.setText(R.id.history_text, item.getTime());
        adapter = new History_2Adapter(item.getData(), _mActivity);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.e("ddd",""+item.getData().get(position).getVid());
                fragment.start(VideoPlayFragment.newInstance(item.getData().get(position).getVid() + ""));
            }
        });

//        list_list.findViewById(R.id.list_list);
         recycler = helper.getView(R.id.recycler);
        recycler.setLayoutManager(new LinearLayoutManager(_mActivity));
        recycler.addItemDecoration(new DividerItemDecoration(_mActivity, DividerItemDecoration.VERTICAL_LIST));

        recycler.setAdapter(adapter);

//        helper.setText(R.id.tv_title, item.getTitle());
//        helper.setText(R.id.tv_time, "您看到了" + stringForTime(item.getLooktime()));
//        ImageView imageView = helper.getView(R.id.iv_iamge);
//        GlideUtils.loadImagView(mContext, item.getPic(), imageView);


    }


//    protected String stringForTime(long timeMs) {
//        if (timeMs <= 0 || timeMs >= 24 * 60 * 60 * 1000) {
//            return "00:00:00";
//        }
//        long totalSeconds = timeMs / 1000;
//        int seconds = (int) (totalSeconds % 60);
//        int minutes = (int) ((totalSeconds / 60) % 60);
//        int hours = (int) (totalSeconds / 3600);
//        StringBuilder stringBuilder = new StringBuilder();
//        Formatter mFormatter = new Formatter(stringBuilder, Locale.getDefault());
//        return mFormatter.format("%02d小时%02d分%02d秒", hours, minutes, seconds).toString();
//    }

}
