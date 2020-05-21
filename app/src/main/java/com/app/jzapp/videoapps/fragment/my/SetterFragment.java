package com.app.jzapp.videoapps.fragment.my;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.base.BaseBackFragment;
import com.app.jzapp.videoapps.event.SetPSWEvent;
import com.app.jzapp.videoapps.utils.AppUtils;
import com.app.jzapp.videoapps.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

import static com.app.jzapp.videoapps.http.AppConfig.AUTOPLAY_SWITCH;
import static com.app.jzapp.videoapps.http.AppConfig.PSW;
import static com.app.jzapp.videoapps.http.AppConfig.PSW_SWITCH;
import static com.app.jzapp.videoapps.http.AppConfig.SP_NAME;

public class SetterFragment extends BaseBackFragment {
    @BindView(R.id.paddingView)
    View paddingView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.psw_switch)
    Switch pswSwitch;
    @BindView(R.id.autoplay_switch)
    Switch autoplaySwitch;
    @BindView(R.id.tv_app_version)
    TextView tvAppVersion;

    public static SetterFragment newInstance() {
        SetterFragment fragment = new SetterFragment();
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_setter;
    }

    @Override
    protected View setStatusBarView() {
        return paddingView;
    }

    @Override
    protected int statusBarColor() {
        return R.color.white;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {
        initToolbarNav(toolbar);

        toolbar.setTitle("");
        title.setText(getString(R.string.setter));

        pswSwitch.setChecked(new SPUtils(SP_NAME).getBoolean(PSW_SWITCH));
        autoplaySwitch.setChecked(new SPUtils(SP_NAME).getBoolean(AUTOPLAY_SWITCH));

    }

    @Override
    protected void initData() {
        tvAppVersion.setText(AppUtils.getAppVersionName(_mActivity));
    }

    @OnClick(R.id.autoplay_switch)
    void autoplaySwitch() {
        new SPUtils(SP_NAME).putBoolean(AUTOPLAY_SWITCH, autoplaySwitch.isChecked());
    }

    @OnClick(R.id.psw_switch)
    void pswSwitch() {
        if (pswSwitch.isChecked()) {
            start(SetterPSWFragment.newInstance());
        } else {
            new SPUtils(SP_NAME).putBoolean(PSW_SWITCH, false);
            new SPUtils(SP_NAME).putString(PSW, "");
        }
        pswSwitch.setChecked(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setPSWEvent(SetPSWEvent event) {
        pswSwitch.setChecked(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
