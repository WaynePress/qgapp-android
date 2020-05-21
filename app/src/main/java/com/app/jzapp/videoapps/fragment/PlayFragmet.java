package com.app.jzapp.videoapps.fragment;

import android.os.Bundle;
import android.util.Log;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.base.BaseBackFragment;
import com.app.jzapp.videoapps.utils.SDCardUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

public class PlayFragmet extends BaseBackFragment {
    @BindView(R.id.jzVideoPlayer)
    JZVideoPlayerStandard jzVideoPlayer;


    public static PlayFragmet newInstance(String filedir, String url) {
        Log.e("cjn", "这个下载属性AAAAAAAAAAAAA" + filedir.toString());
        Log.e("cjn", "这个下载属性BBBBBBBBBBBBB" + url.toString());
        Log.e("cjn", "这个下载属性CCCCCCCCCCCCC" + SDCardUtils.getSDPath().toString());


        PlayFragmet fragment = new PlayFragmet();
        Bundle bundle = new Bundle();
        bundle.putString("filedir", filedir);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    protected int setLayoutId() {
        return R.layout.fragment_play;
    }


    //    private void checkPermission() {
//        //检查权限（NEED_PERMISSION）是否被授权 PackageManager.PERMISSION_GRANTED表示同意授权
//        if (ActivityCompat.checkSelfPermission(_mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            //用户已经拒绝过一次，再次弹出权限申请对话框需要给用户一个解释
//            if (ActivityCompat.shouldShowRequestPermissionRationale(_mActivity, Manifest.permission
//                    .WRITE_EXTERNAL_STORAGE)) {
//                Toast.makeText(_mActivity, "请开通相关权限，否则无法正常使用本应用！", Toast.LENGTH_SHORT).show();
//            }
//            //申请权限
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
//
//        } else {
//            Toast.makeText(_mActivity, "授权成功！", Toast.LENGTH_SHORT).show();
//            Log.e("cjn", "checkPermission: 已经授权！");
//        }
    @Override
    protected void initData() {
        super.initData();

        Log.e("cjn", "看看这个拼成的地址" + SDCardUtils.getSDCardPath().toString() + "001.mp4");
//        jzVideoPlayer.setUp(data.getUrl(), JZVideoPlayer.SCREEN_WINDOW_NORMAL, "");
//        jzVideoPlayer.setUp(SDCardUtils.getSDCardPath().toString() + "001.mp4", jzVideoPlayer.SCREEN_WINDOW_FULLSCREEN, "");
//        jzVideoPlayer.setUp(Environment.getExternalStorageDirectory().getAbsolutePath() + "001.mp4", jzVideoPlayer.SCREEN_WINDOW_FULLSCREEN, "");
//        jzVideoPlayer.setUp(getArguments().getString("filedir") + ".mp4", jzVideoPlayer.SCREEN_WINDOW_FULLSCREEN, "");
        File file = new File(getArguments().getString("filedir"));
        Log.e("cjn", "是否存在" + file.exists() + "是有权限" + file.canRead());
        jzVideoPlayer.setUp(getArguments().getString("filedir"), jzVideoPlayer.SCREEN_WINDOW_FULLSCREEN, "");
    }

    ////
    @Override
    public void onDestroy() {
        super.onDestroy();
        JZVideoPlayer.releaseAllVideos();
        EventBus.getDefault().unregister(this);

    }

    //
    @Override
    protected int statusBarColor() {
        return R.color.transparent;
    }

    @OnClick(R.id.iv_back)
    void back() {
        _mActivity.onBackPressed();
    }

}
