package com.app.jzapp.videoapps.base;

import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.app.jzapp.videoapps.AllHandle;
import com.app.jzapp.videoapps.bean.LoginBean;
import com.app.jzapp.videoapps.bean.UserBean;
import com.app.jzapp.videoapps.event.UserInfoEvent;
import com.app.jzapp.videoapps.http.ApiServiceResult;
import com.app.jzapp.videoapps.http.AppConfig;
import com.app.jzapp.videoapps.http.Client;
import com.app.jzapp.videoapps.http.DialogTransformer;
import com.app.jzapp.videoapps.http.RxsRxSchedulers;
import com.app.jzapp.videoapps.utils.SPUtils;
import com.app.jzapp.videoapps.utils.ToastUtils;
import com.app.video.videoapps.R;
import com.gyf.barlibrary.ImmersionBar;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.ObservableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Function;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * 懒加载
 * Created by YoKeyword on 16/6/5.
 */
public abstract class BaseMainFragment extends SupportFragment {
    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    protected View mRootView;
    //对subscription进行管理 防止内存泄漏
//    public CompositeSubscription mCompositeSubscription;
    protected CompositeDisposable composite;
    protected ImmersionBar mImmersionBar;
    protected Unbinder unbinder;

    private FrameLayout flContent;
    protected DialogTransformer dialogTransformer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mRootView = inflater.inflate(R.layout.container_layout, container, false);
        allHandle = AllHandle.getInstance();

        addView();

        initView();

        initData();

        setListener();

        return mRootView;
    }

//    //进行统一管理
//    protected void addSubscription(Subscription subscription) {
//        if (mCompositeSubscription == null) {
//            mCompositeSubscription = new CompositeSubscription();
//        }
//        mCompositeSubscription.add(subscription);
//    }

    public CompositeDisposable getComposite() {
        if (composite == null) {
            composite = new CompositeDisposable();
        }
        return composite;
    }

    //取消注册，以避免内存泄露
    public void onUnSubscribe() {
        if (composite != null) {
            composite.clear();
        }
    }

    private void addView() {
        flContent = mRootView.findViewById(R.id.fl_content);
        View contentView = View.inflate(_mActivity, setLayoutId(), null);
        flContent.addView(contentView);
        unbinder = ButterKnife.bind(this, mRootView);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        setStatusBar();
    }

    private void setStatusBar() {
        if (statusBarDarkFont()) {
            initImmersionBar();
            mImmersionBar.statusBarDarkFont(true, 0.2f);
        }

        int statusBarColor = statusBarColor();
        if (statusBarColor != 0) {
            initImmersionBar();
            mImmersionBar.statusBarColor(statusBarColor);
        }

        View statusBarView = setStatusBarView();
        if (statusBarView != null) {
            initImmersionBar();
            mImmersionBar.statusBarView(statusBarView);
        }

        if (mImmersionBar != null) {
            mImmersionBar.init();
        }
    }

    private void initImmersionBar() {
        if (mImmersionBar == null) {
            mImmersionBar = ImmersionBar.with(this);
            mImmersionBar.keyboardEnable(true);  //解决软键盘与底部输入框冲突问题
        }
    }

    /**
     * 设置状态栏颜色
     *
     * @return
     */
    protected int statusBarColor() {
        return R.color.colorPrimary;
    }

    /**
     * 单独在标题栏的位置增加view，高度为状态栏的高度
     * Sets status bar view.
     */
    protected View setStatusBarView() {
        return null;
    }


    /**
     * 如果当前设备支持状态栏字体变色，会设置成黑色，不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
     *
     * @return
     */
    protected boolean statusBarDarkFont() {
        return true;
    }

    /**
     * Sets layout id.
     *
     * @return the layout id
     */
    protected abstract int setLayoutId();

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    /**
     * view与数据绑定
     */
    protected void initView() {
    }

    /**
     * 设置监听
     */
    protected void setListener() {
    }

    /**
     * 处理回退事件
     *
     * @return
     */
    @Override
    public boolean onBackPressedSupport() {
        if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
            _mActivity.finish();
            NotificationManager manager = (NotificationManager) _mActivity.getSystemService(Context.NOTIFICATION_SERVICE);
            manager.cancelAll();
        } else {
            TOUCH_TIME = System.currentTimeMillis();
            Toast.makeText(_mActivity, R.string.press_again_exit, Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
        onUnSubscribe();
        if (dialogTransformer != null) {
            dialogTransformer.dismiss();
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("" + this.getClass().getName()); //统计页面("MainScreen"为页面名称，可自定义)
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("" + this.getClass().getName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mImmersionBar != null) {
            mImmersionBar.destroy();
        }
    }

    /**
     * 随机登录并获取用户信息
     */
    protected void login() {
        dialogTransformer = new DialogTransformer(_mActivity);
        Client.getApiService().randomUser("2")
                .flatMap(new Function<BaseBean<LoginBean>, ObservableSource<BaseBean<UserBean>>>() {
                    @Override
                    public ObservableSource<BaseBean<UserBean>> apply(BaseBean<LoginBean> bean) throws Exception {
                        if (bean != null && bean.getData() != null) {
                            new SPUtils(AppConfig.SP_NAME).putString(AppConfig.SP_TOKEN, bean.getData().getToken());
                            AppConfig.TOKEN = bean.getData().getToken();
                            return Client.getApiService().getUserInfo(AppConfig.TOKEN);
                        } else {
                            ToastUtils.showShortToast(bean.getMsg());
                            return null;
                        }
                    }
                })
                .compose(RxsRxSchedulers.<BaseBean<UserBean>>io_main())
                .compose(dialogTransformer.transformer())
                .subscribe(new ApiServiceResult<UserBean>(getComposite()) {
                    @Override
                    public void onNext(BaseBean<UserBean> bean) {
                        super.onNext(bean);
                        if (bean != null && bean.getData() != null)
                            AppConfig.USERBEAN = bean.getData();

                        subInviteCode();
                    }
                });
    }

    private void subInviteCode() {
        try{
            ClipboardManager cm = (ClipboardManager) _mActivity.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData mClipData = cm.getPrimaryClip();
            String code = mClipData.getItemAt(0).getText().toString();
            Log.e("cjn",""+code);

            if (TextUtils.isEmpty(AppConfig.TOKEN)||TextUtils.isEmpty(code)||!code.contains("qgtv=")){
                return;
            }
            Log.e("cjn",""+code.split("qgtv=")[1]);
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

    /**
     * 获取用户信息
     */
    public AllHandle allHandle;

    protected void getUserInfo() {
        Client.getApiService().getUserInfo(AppConfig.TOKEN)
                .compose(RxsRxSchedulers.<BaseBean<UserBean>>io_main())
                .subscribe(new ApiServiceResult<UserBean>(getComposite()) {
                    @Override
                    public void onNext(BaseBean<UserBean> bean) {
                        super.onNext(bean);
                        Log.e("cjn", "看卡这的数据" + bean);
                        if (bean != null && bean.getData() != null) {
                            AppConfig.USERBEAN = bean.getData();
                            EventBus.getDefault().post(new UserInfoEvent());
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        login();
                    }
                });
    }
}
