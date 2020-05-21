package com.app.jzapp.videoapps;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.jzapp.videoapps.http.ApiServiceResult;
import com.app.jzapp.videoapps.http.AppConfig;
import com.app.jzapp.videoapps.http.Client;
import com.app.jzapp.videoapps.http.DialogTransformer;
import com.app.jzapp.videoapps.http.RxsRxSchedulers;
import com.app.jzapp.videoapps.utils.ToastUtils;
import com.app.jzapp.videoapps.base.BaseActivity;
import com.app.jzapp.videoapps.base.BaseBean;
import com.app.jzapp.videoapps.bean.QRCode;
import com.app.jzapp.videoapps.utils.GlideUtils;
import com.app.video.videoapps.R;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import butterknife.OnClick;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class SplashActivity extends BaseActivity {
    @BindView(R.id.tv_cancle)
    TextView tvCancle;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.ll_show)
    RelativeLayout llShow;
    @BindView(R.id.tv_time)
    TextView tvTime;

    @BindView(R.id.iv_bg)
    ImageView ivBg;

    int time = 5;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_qrcode)
    ImageView ivQrcode;
    @BindView(R.id.tv_lx)
    TextView tv_lx;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        llShow.setVisibility(View.GONE);
        getStatus();
    }


    int count;
    /**
     * 配置服务器
     */
    private void getStatus() {
        tvTime.setText(R.string.server_status_start);
        count++;
        Client.getApiService().getStatus()
                .compose(RxsRxSchedulers.<BaseBean>io_main())
                .compose(new DialogTransformer(this).transformer())
                .subscribe(new ApiServiceResult(getComposite()) {
                    @Override
                    public void onNext(BaseBean bean) {
                        if (bean != null && "0".equals(bean.getCode())) {
                            tvTime.setText(R.string.server_status_end);
                            getAppCon();
                            handler.sendEmptyMessageAtTime(1, 1000);
                        }else {
                            onError(null);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        //接口失败后，更换baseurl
                        if (count < 3 && MyApplication.getApplication() != null) {
                            MyApplication.getApplication().initClient(count == 1 ? AppConfig.BASEURL1 : AppConfig.BASEURL2);
                            getStatus();
                        } else if (count >= 3) {
                            ToastUtils.showShortToast(R.string.url_err);
                        }
                    }
                });


    }
    private void getAppCon() {
        subInviteCode();
        Client.getApiService().getAppCon("2")
                .compose(RxsRxSchedulers.<BaseBean<QRCode>>io_main())
                .compose(new DialogTransformer(this).transformer())
                .subscribe(new ApiServiceResult<QRCode>(getComposite()) {
                    @Override
                    public void onNext(BaseBean<QRCode> bean) {
                        super.onNext(bean);

                        if (bean != null && bean.getData() != null) {
                            setData(bean);
                        }
                    }
                });
    }

    private void subInviteCode() {
        try{
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData mClipData = cm.getPrimaryClip();
            String code = mClipData.getItemAt(0).getText().toString();
            Log.e("subInviteCode",""+code);

            if (TextUtils.isEmpty(AppConfig.TOKEN)||TextUtils.isEmpty(code)||!code.contains("qgtv=")){
                return;
            }
            Log.e("cjn", "看看这的数据" + code.split("qgtv=")[1]);
            Client.getApiService().subInviteCode(AppConfig.TOKEN, code.split("qgtv=")[1])
                    .compose(RxsRxSchedulers.<BaseBean>io_main())
                    .subscribe(new ApiServiceResult(getComposite()) {
                        @Override
                        public void onNext(BaseBean bean) {
                            Log.e("cjn", "看看这的数据" + bean.toString());
                            if (bean != null) {

                            }
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void setData(BaseBean<QRCode> bean) {
        if (llShow != null) {
            llShow.setVisibility(View.VISIBLE);
            tvTitle.setText(bean.getData().getText());
            GlideUtils.loadImagView(this, bean.getData().getPic(), ivQrcode);
            GlideUtils.loadImagView(this, bean.getData().getBgpic(), ivBg);
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置横向(和安卓4.x动画相同)
        return new DefaultHorizontalAnimator();
    }

    /**
     * 初始化状态栏 导航栏 jar
     */
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this)
                .transparentStatusBar()
                .navigationBarColor(R.color.transparent)
                .keyboardEnable(true); //解决软键盘与底部输入框冲突问题
        mImmersionBar.init();
    }

    @OnClick(R.id.tv_cancle)
    void click() {
        llShow.setVisibility(View.GONE);
    }

    @OnClick(R.id.tv_confirm)
    void confirm() {
        llShow.setVisibility(View.GONE);
//        Intent action = new Intent(Intent.ACTION_VIEW);
//        action.setData(Uri.parse(bean.getData()));
//        startActivity(action);
    }

    //离线模式
    @OnClick(R.id.tv_lx)
    void tv_lx() {
        MyApplication.lx = true;
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            time--;
            if (time >= 0) {
                handler.sendEmptyMessageDelayed(1, 1000);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvTime.setText(time + "s");
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvTime.setText(R.string.enter);
                        tvTime.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                                finish();
                            }
                        });
                    }
                });

            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(1);
        handler = null;
    }
}
