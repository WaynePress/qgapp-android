package com.app.jzapp.videoapps.fragment.my;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.base.BaseBackFragment;
import com.app.jzapp.videoapps.event.SetPSWEvent;
import com.app.jzapp.videoapps.utils.Md5Util;
import com.app.jzapp.videoapps.utils.SPUtils;
import com.app.jzapp.videoapps.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.app.jzapp.videoapps.http.AppConfig.PSW;
import static com.app.jzapp.videoapps.http.AppConfig.PSW_SWITCH;
import static com.app.jzapp.videoapps.http.AppConfig.SP_NAME;

public class SetterPSWFragment extends BaseBackFragment {
    @BindView(R.id.paddingView)
    View paddingView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.v_1)
    View v1;
    @BindView(R.id.v_2)
    View v2;
    @BindView(R.id.v_3)
    View v3;
    @BindView(R.id.v_4)
    View v4;
    @BindView(R.id.tv_1)
    TextView tv1;
    @BindView(R.id.tv_2)
    TextView tv2;
    @BindView(R.id.tv_3)
    TextView tv3;
    @BindView(R.id.tv_4)
    TextView tv4;
    @BindView(R.id.tv_5)
    TextView tv5;
    @BindView(R.id.tv_6)
    TextView tv6;
    @BindView(R.id.tv_7)
    TextView tv7;
    @BindView(R.id.tv_8)
    TextView tv8;
    @BindView(R.id.tv_9)
    TextView tv9;
    @BindView(R.id.tv_0)
    TextView tv0;
    @BindView(R.id.tv_back)
    TextView tvBack;

    private int count;
    private List<String> list;
    private String firstPSW, SecondPSW;
    private boolean isAgain;

    public static SetterPSWFragment newInstance() {
        SetterPSWFragment fragment = new SetterPSWFragment();
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_setter_psw;
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
    protected void initView() {
        initToolbarNav(toolbar);

        toolbar.setTitle("");
        title.setText(getString(R.string.setter_psw));
        list = new ArrayList<>();
    }

    @OnClick({R.id.tv_1, R.id.tv_2, R.id.tv_3, R.id.tv_4, R.id.tv_5, R.id.tv_6, R.id.tv_7, R.id.tv_8, R.id.tv_9, R.id.tv_0, R.id.tv_back,})
    void click(View v) {
        switch (v.getId()) {
            case R.id.tv_1:
                count++;
                list.add("1");
                break;
            case R.id.tv_2:
                count++;
                list.add("2");
                break;
            case R.id.tv_3:
                count++;
                list.add("3");
                break;
            case R.id.tv_4:
                count++;
                list.add("4");
                break;
            case R.id.tv_5:
                count++;
                list.add("5");
                break;
            case R.id.tv_6:
                count++;
                list.add("6");
                break;
            case R.id.tv_7:
                count++;
                list.add("7");
                break;
            case R.id.tv_8:
                count++;
                list.add("8");
                break;
            case R.id.tv_9:
                count++;
                list.add("9");
                break;
            case R.id.tv_0:
                count++;
                list.add("0");
                break;
            case R.id.tv_back:
                if (count > 0) {
                    count--;
                    list.remove(list.size() - 1);
                }
                break;
        }

        switch (count) {
            case 0:
                setUnSelected(v1);
                break;
            case 1:
                setSelected(v1);
                setUnSelected(v2);
                break;
            case 2:
                setSelected(v2);
                setUnSelected(v3);
                break;
            case 3:
                setSelected(v3);
                setUnSelected(v4);
                break;
            case 4:
                if (isAgain) {
                    setSelected(v4);
                    SecondPSW = list.toString();
                    if (!TextUtils.equals(firstPSW, SecondPSW)) {
                        ToastUtils.showShortToast(getString(R.string.psw_unlike));
                        return;
                    }
                    new SPUtils(SP_NAME).putBoolean(PSW_SWITCH, true);
                    new SPUtils(SP_NAME).putString(PSW, Md5Util.getMD5(SecondPSW));
                    ToastUtils.showShortToast(getString(R.string.set_psw_successed));
                    EventBus.getDefault().post(new SetPSWEvent());
                    _mActivity.onBackPressed();
                }
                setUnSelected(v1);
                setUnSelected(v2);
                setUnSelected(v3);
                setUnSelected(v4);
                count = 0;
                firstPSW = list.toString();
                list.clear();
                isAgain = true;
                break;
        }
    }

    private void setUnSelected(View v) {
        v.setVisibility(View.INVISIBLE);
    }

    private void setSelected(View v) {
        v.setVisibility(View.VISIBLE);
    }
}
