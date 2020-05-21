package com.app.jzapp.videoapps.fragment.my;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.adapter.my.DownloadFragmentAdapter;
import com.app.jzapp.videoapps.base.BaseBackFragment;
import com.app.jzapp.videoapps.bean.DownLoadBean;
import com.app.jzapp.videoapps.bean.DownLoadInfo;
import com.app.jzapp.videoapps.event.DownloadEvent;
import com.app.jzapp.videoapps.fragment.PlayFragmet;
import com.app.jzapp.videoapps.utils.DividerItemDecoration;
import com.app.jzapp.videoapps.utils.DownLoadSqlUtils;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.yaoxiaowen.download.DownloadConstant;
import com.yaoxiaowen.download.FileInfo;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.app.jzapp.videoapps.http.AppConfig.sDownLoadBeans;

/**
 * 下载管理
 */
public class DownloadFragment extends BaseBackFragment implements Toolbar.OnMenuItemClickListener {
    @BindView(R.id.paddingView)
    View paddingView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.refreshLayout)
    TwinklingRefreshLayout refreshLayout;

    private DownloadFragmentAdapter adapter;

    MenuItem menuItem;
    boolean isEdit;

    List<DownLoadInfo> data = new ArrayList<>();
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};
    public static DownloadFragment newInstance() {
        DownloadFragment fragment = new DownloadFragment();
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_recycle;
    }

    @Override
    protected View setStatusBarView() {
        return paddingView;
    }

    @Override
    protected int statusBarColor() {
        return R.color.white;
    }
    public static void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void initView() {
        initToolbarNav(toolbar);

        toolbar.setTitle("");
        title.setText(getString(R.string.download));

        setRefreshLayout();
        toolbar.inflateMenu(R.menu.edit);
        menuItem = toolbar.getMenu().findItem(R.id.action_edit);
        toolbar.setOnMenuItemClickListener(this);
    }

    @Override
    protected void initData() {
        verifyStoragePermissions(_mActivity);
        recycler.setLayoutManager(new LinearLayoutManager(_mActivity));
        recycler.addItemDecoration(new DividerItemDecoration(_mActivity, DividerItemDecoration.VERTICAL_LIST));

        sDownLoadBeans = new DownLoadSqlUtils().getBeans();
        for (DownLoadBean bean : sDownLoadBeans) {
            DownLoadInfo downLoadInfo = new DownLoadInfo(bean.getId(), bean.getUrl(), bean.getName(), bean.getFilePath(), bean.getIamgeUrl(), bean.getStatus());
            data.add(downLoadInfo);
        }
        adapter = new DownloadFragmentAdapter(data);

        View emptyView = View.inflate(_mActivity, R.layout.empty, null);
        TextView text = emptyView.findViewById(R.id.text);
        text.setText("目前没有下载视频");
        adapter.setEmptyView(emptyView);

        recycler.setAdapter(adapter);
    }


    private void setRefreshLayout() {
        refreshLayout.setEnableLoadmore(false);
        refreshLayout.setEnableRefresh(false);
        refreshLayout.setEnableOverScroll(false);
        refreshLayout.setHeaderHeight(55);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        regist();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void regist() {
        Log.e("cjn", "这个方法走了吗AAAAAAAAAAAA");
        IntentFilter filter = new IntentFilter();
        for (DownLoadBean bean : sDownLoadBeans) {
            filter.addAction(bean.getUrl());
        }
        _mActivity.registerReceiver(receiver, filter);
    }

    @Override
    public void onDestroyView() {
        Log.e("cjn", "这个方法走了吗BBBBBBBBBBBBBBBBBBB");
        super.onDestroyView();
        _mActivity.unregisterReceiver(receiver);

    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("cjn", "这个方法走了吗~~~~~~~~~~~~~~~~~~~~~~~");
            if (null != intent) {
                Log.e("cjn", "AAAAAAA" + intent.getAction().toString());
                for (DownLoadInfo info : data) {
                    FileInfo fileInfo = (FileInfo) intent.getSerializableExtra(DownloadConstant.EXTRA_INTENT_DOWNLOAD);
                    for (DownLoadBean bean : sDownLoadBeans) {
                        bean.setStatus(fileInfo.getDownloadStatus());
                    }
                    Log.e("cjn", "CCCCCC" + fileInfo.getSize());
                    if (intent.getAction().equals(info.getUrl())) {

                        info.setSize(fileInfo.getSize());
                        Log.e("cjn", "BBBBBBBB" + info.getSize());
                        info.setDownloadLocation(fileInfo.getDownloadLocation());
                        info.setStatus(fileInfo.getDownloadStatus());
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    };


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                if (TextUtils.equals(menuItem.getTitle().toString(), "编辑")) {
                    menuItem.setTitle("完成");
                    isEdit = true;
                } else {
                    isEdit = false;
                    menuItem.setTitle("编辑");
                }
                adapter.setEdit(isEdit);
                break;
        }
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void downloadEvent(DownloadEvent event) {
        start(PlayFragmet.newInstance(event.getFilePath(),event.getUrl().toString()));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
