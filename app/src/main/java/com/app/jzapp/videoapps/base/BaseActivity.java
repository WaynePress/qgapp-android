package com.app.jzapp.videoapps.base;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.app.jzapp.videoapps.utils.Utils;
import com.app.video.videoapps.R;
import com.gyf.barlibrary.ImmersionBar;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import me.yokeyword.fragmentation.SupportActivity;

public abstract class BaseActivity extends SupportActivity {
    private Unbinder unbinder;

    protected ImmersionBar mImmersionBar;

    private FrameLayout container;
    private View statusBar;

    //对subscription进行管理 防止内存泄漏
//    public CompositeSubscription mCompositeSubscription;
    protected CompositeDisposable composite;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base);
        addView();
        unbinder = ButterKnife.bind(this);

        initImmersionBar();

        initView();

        initData();

        setListener();

        Utils.init(this);
    }

    protected void addView() {
        container = findViewById(R.id.fl_container);
        statusBar = findViewById(R.id.statusBar);
        if (setLayoutId() != 0) {
            View content = View.inflate(getBaseContext(), setLayoutId(), null);
            container.addView(content);
        }
    }

    /**
     * 初始化状态栏 导航栏 jar
     */
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this);
        mImmersionBar.keyboardEnable(true);  //解决软键盘与底部输入框冲突问题
        if (statusBar != null) {
            mImmersionBar.statusBarView(statusBar);
        }
        int statusBarColor = statusBarColor();

        if (statusBarColor != 0) {
            statusBar.setBackgroundColor(getResources().getColor(statusBarColor));
            mImmersionBar.statusBarColor(statusBarColor);
        }

        if (statusBarDarkFont()) {
            mImmersionBar.statusBarDarkFont(true, 0.2f);
        }

        mImmersionBar.init();
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


    //进行统一管理
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

    /**
     * 设置布局
     *
     * @return
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

    protected void initToolbarNav(Toolbar toolbar) {
        Resources resources = getResources();
        Drawable drawable = resources.getDrawable(R.drawable.ic_chevron_left_black_24dp);
        toolbar.setNavigationIcon(drawable);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null) {
            unbinder.unbind();
        }
        onUnSubscribe();
    }
}
