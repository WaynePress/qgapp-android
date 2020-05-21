package com.app.jzapp.videoapps.fragment;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.jzapp.videoapps.http.AppConfig;
import com.app.jzapp.videoapps.utils.Md5Util;
import com.app.jzapp.videoapps.utils.SPUtils;
import com.app.jzapp.videoapps.utils.ToastUtils;
import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.base.BaseMainFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ShowPSWFragment extends BaseMainFragment {
    @BindView(R.id.paddingView)
    View paddingView;
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
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_show)
    TextView tvShow;

    private int count;
    private List<String> list;
    private String firstPSW;

    public static ShowPSWFragment newInstance() {
        ShowPSWFragment fragment = new ShowPSWFragment();
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_show_psw;
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
        list = new ArrayList<>();
    }

    @OnClick({R.id.tv_1, R.id.tv_2, R.id.tv_3, R.id.tv_4, R.id.tv_5, R.id.tv_6, R.id.tv_7, R.id.tv_8, R.id.tv_9, R.id.tv_0, R.id.iv_back,})
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
            case R.id.iv_back:
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
                setSelected(v4);
                firstPSW = list.toString();
                String s = new SPUtils(AppConfig.SP_NAME).getString(AppConfig.PSW);
                if (!TextUtils.equals(s, Md5Util.getMD5(firstPSW))) {
                    setUnSelected(v1);
                    setUnSelected(v2);
                    setUnSelected(v3);
                    setUnSelected(v4);
                    ToastUtils.showShortToast(getString(R.string.psw_error));
                    count = 0;
                    list.clear();
                } else {

                    startWithPop(MainFragment.newInstance());
                }
                break;
        }
    }

    private void setUnSelected(View v) {
        Resources resources = _mActivity.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.shape_box_20).mutate();
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(getResources().getColor(R.color.c_EC72AD)));
        v.setBackground(drawable);

    }

    private void setSelected(View v) {
        Resources resources = _mActivity.getResources();
        Drawable drawable = resources.getDrawable(R.drawable.shape_20).mutate();
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(getResources().getColor(R.color.c_EC72AD)));
        v.setBackground(drawable);
    }
}
