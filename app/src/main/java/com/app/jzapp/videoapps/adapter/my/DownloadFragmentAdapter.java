package com.app.jzapp.videoapps.adapter.my;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.bean.DownLoadBean;
import com.app.jzapp.videoapps.bean.DownLoadInfo;
import com.app.jzapp.videoapps.event.DownloadEvent;
import com.app.jzapp.videoapps.utils.DownLoadSqlUtils;
import com.app.jzapp.videoapps.utils.GlideUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yaoxiaowen.download.DownloadHelper;
import com.yaoxiaowen.download.DownloadStatus;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;

import static com.app.jzapp.videoapps.http.AppConfig.sDownLoadBeans;

public class DownloadFragmentAdapter extends BaseMultiItemQuickAdapter<DownLoadInfo, BaseViewHolder> {

    boolean isEdit;

    DecimalFormat df = new DecimalFormat("0.00");//格式化小数

    public DownloadFragmentAdapter(List<DownLoadInfo> data) {
        super(data);
        setDefaultViewTypeLayout(R.layout.item_download);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final DownLoadInfo item) {
        helper.setText(R.id.tv_title, item.getName());
        Log.e("cjn", "查看下载的属性" + item.toString());
        if (item.getSize() != 0 && item.getStatus() != DownloadStatus.COMPLETE) {
            helper.setText(R.id.tv_progress, "进度:" + df.format((double) item.getDownloadLocation() / (double) item.getSize() * 100) + "%");
        } else if (item.getStatus() == DownloadStatus.COMPLETE) {
            helper.setText(R.id.tv_progress, "进度:100%");
            helper.setText(R.id.tv_status, "状态:已完成");
            helper.setText(R.id.tv_control, "播放");
        }
        if (item.getStatus() == DownloadStatus.COMPLETE) {//下载完成
            helper.setText(R.id.tv_progress, "进度:100%");
            helper.setText(R.id.tv_status, "状态:已完成");
            helper.setText(R.id.tv_control, "播放");
        } else if (item.getStatus() == DownloadStatus.PAUSE) {//下载暂停
            helper.setText(R.id.tv_status, "状态:已暂停");
            helper.setText(R.id.tv_control, "开始");
        } else {//下载暂停 后 立即更新数据库信息
            helper.setText(R.id.tv_status, "状态:下载中");
            helper.setText(R.id.tv_control, "暂停");
        }
        ImageView imageView = helper.getView(R.id.iv_iamge);
        GlideUtils.loadImagView(mContext, item.getIamgeUrl(), imageView);
        if (isEdit) {
            helper.setVisible(R.id.tv_delete, true);
            helper.getView(R.id.tv_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delete(item, helper.getAdapterPosition());
                }
            });
        } else {
            helper.setVisible(R.id.tv_delete, false);
        }

        helper.getView(R.id.tv_control).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getStatus() == DownloadStatus.COMPLETE) {//下载完成
                    play(item);
                } else if (item.getStatus() == DownloadStatus.PAUSE) {//下载暂停
                    File file = new File(item.getFilePath());
                    DownloadHelper.getInstance().addTask(item.getUrl(), file, item.getUrl())
                            .submit(mContext);
                } else {//下载暂停 后 立即更新数据库信息
                    File file = new File(item.getFilePath());
                    DownloadHelper.getInstance().pauseTask(item.getUrl(), file, item.getUrl())
                            .submit(mContext);
                }

            }
        });
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
        notifyDataSetChanged();
    }

    private void delete(DownLoadInfo item, int position) {
        File file = new File(item.getFilePath());
        DownloadHelper.getInstance().pauseTask(item.getUrl(), file, item.getUrl())
                .submit(mContext);
        if (file.exists()) {
            boolean result = file.delete();
            if (result) {
                new DownLoadSqlUtils().delete(item.getUrl());
                for (DownLoadBean bean : sDownLoadBeans) {
                    if (TextUtils.equals(item.getUrl(), bean.getUrl())) {
                        sDownLoadBeans.remove(bean);
                        break;
                    }
                }
                mData.remove(position);
                notifyDataSetChanged();
            }
        } else {
            new DownLoadSqlUtils().delete(item.getUrl());
            for (DownLoadBean bean : sDownLoadBeans) {
                if (TextUtils.equals(item.getUrl(), bean.getUrl())) {
                    sDownLoadBeans.remove(bean);
                    break;
                }
            }

            mData.remove(position);
            notifyDataSetChanged();
        }

    }

    private void play(DownLoadInfo item) {
        EventBus.getDefault().post(new DownloadEvent(0, item.getUrl(), item.getFilePath()));
    }
}
