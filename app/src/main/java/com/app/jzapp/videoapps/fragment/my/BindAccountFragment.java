package com.app.jzapp.videoapps.fragment.my;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.base.BaseBackFragment;
import com.app.jzapp.videoapps.base.BaseBean;
import com.app.jzapp.videoapps.event.BindAccountSuccessedEvent;
import com.app.jzapp.videoapps.http.ApiServiceResult;
import com.app.jzapp.videoapps.http.Client;
import com.app.jzapp.videoapps.http.DialogTransformer;
import com.app.jzapp.videoapps.http.RxsRxSchedulers;
import com.app.jzapp.videoapps.utils.DialogUtils;
import com.app.jzapp.videoapps.utils.RegexUtils;
import com.app.jzapp.videoapps.utils.ToastUtils;
import com.jakewharton.rxbinding3.view.RxView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

import static com.app.jzapp.videoapps.http.AppConfig.TOKEN;

/**
 * 切换账号
 */
public class BindAccountFragment extends BaseBackFragment {
    @BindView(R.id.paddingView)
    View paddingView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_bind)
    TextView tvBind;
    @BindView(R.id.tv_code)
    TextView tvCode;

    private AlertDialog loginDialog;

    private Map<String, String> map;

    private String flag = "1";

    public static BindAccountFragment newInstance(boolean isFindAccount) {
        BindAccountFragment fragment = new BindAccountFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isFindAccount", isFindAccount);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_bind_account;
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
        if (getArguments().getBoolean("isFindAccount")) {
            title.setText(getString(R.string.find_account));
            flag = "2";
        } else
            title.setText(getString(R.string.bind_phone));
    }


    @OnClick(R.id.tv_code)
    void getVerifycode() {

        final String phone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showShortToast(getResources().getString(R.string.input_phone));
            return;
        } else if (!RegexUtils.isMobileSimple(phone)) {
            ToastUtils.showShortToast(getResources().getString(R.string.phone_error));
            return;
        }

        RxView.clicks(tvCode).throttleFirst(3, TimeUnit.SECONDS);//在一秒内只取第一次点击

        map = new HashMap<>();
        map.put("token", TOKEN);
        map.put("mobile", phone);
        map.put("flag", flag);

        dialogTransformer = new DialogTransformer(_mActivity);
        Client.getApiService().getVerifycode(map)
                .compose(RxsRxSchedulers.<BaseBean>io_main())
                .compose(dialogTransformer.transformer())
                .subscribe(new ApiServiceResult(getComposite()) {
                    @Override
                    public void onNext(BaseBean bean) {
                        if (bean != null&& bean.getMsg() != null) {
                            ToastUtils.showShortToast(bean.getMsg());
                        }
                    }
                });
    }

    @OnClick(R.id.tv_bind)
    void bind() {
        final String phone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showShortToast(getResources().getString(R.string.input_phone));
            return;
        } else if (!RegexUtils.isMobileSimple(phone)) {
            ToastUtils.showShortToast(getResources().getString(R.string.phone_error));
            return;
        }

        final String code = etCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            ToastUtils.showShortToast(getResources().getString(R.string.input_code2));
            return;
        }

        RxView.clicks(tvBind).throttleFirst(3, TimeUnit.SECONDS);//在一秒内只取第一次点击

        map = new HashMap<>();
        map.put("token", TOKEN);
        map.put("mobile", phone);
        map.put("verifycode", code);
        map.put("flag", flag);

        dialogTransformer = new DialogTransformer(_mActivity);
        Client.getApiService().doUserMobile(map)
                .compose(RxsRxSchedulers.<BaseBean>io_main())
                .compose(dialogTransformer.transformer())
                .subscribe(new ApiServiceResult(getComposite()) {
                    @Override
                    public void onNext(BaseBean bean) {
                        if (bean != null) {
                            EventBus.getDefault().post(new BindAccountSuccessedEvent(phone));

                            showBindSuccessedDialog(bean.getMsg());
                        }
                    }
                });
    }

    private void showBindSuccessedDialog(String s) {
        View view = View.inflate(_mActivity, R.layout.dialog_login_successed, null);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        TextView tvConfirm = view.findViewById(R.id.tv_confirm);
        TextView tvDescription = view.findViewById(R.id.tv_description);
        tvTitle.setText(getString(R.string.hint));
        tvConfirm.setText(getString(R.string.know));
        tvDescription.setText(s);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginDialog.dismiss();
                loginDialog = null;

                _mActivity.onBackPressed();
            }
        });
        loginDialog = new DialogUtils().showDialog(_mActivity, view, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (loginDialog != null && loginDialog.isShowing()) {
            loginDialog.dismiss();
            loginDialog = null;
        }
    }
}
