package com.app.jzapp.videoapps.fragment.my;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.base.BaseBackFragment;
import com.app.jzapp.videoapps.base.BaseBean;
import com.app.jzapp.videoapps.http.ApiServiceResult;
import com.app.jzapp.videoapps.http.Client;
import com.app.jzapp.videoapps.http.DialogTransformer;
import com.app.jzapp.videoapps.http.RxsRxSchedulers;
import com.app.jzapp.videoapps.utils.RegexUtils;
import com.app.jzapp.videoapps.utils.ToastUtils;
import com.jakewharton.rxbinding3.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

public class ForgetPswFragment extends BaseBackFragment {
    @BindView(R.id.paddingView)
    View paddingView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;
    @BindView(R.id.et_old_psw)
    EditText etOldPsw;
    @BindView(R.id.et_new_psw)
    EditText etNewPsw;
    @BindView(R.id.et_psw)
    EditText etPsw;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;

    public static ForgetPswFragment newInstance() {
        ForgetPswFragment fragment = new ForgetPswFragment();
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_forget_psw;
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
        title.setText(getString(R.string.forget_psw));
    }

    @OnClick(R.id.tv_confirm)
    void confirm() {
        String phone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showShortToast(getResources().getString(R.string.input_phone_emial));
            return;
        } else if (!RegexUtils.isMobileSimple(phone) && !RegexUtils.isEmail(phone)) {
            ToastUtils.showShortToast(getResources().getString(R.string.phone_email_error));
            return;
        }
        String oldPsw = etOldPsw.getText().toString().trim();
        if (TextUtils.isEmpty(oldPsw) || oldPsw.length() < 6) {
            ToastUtils.showShortToast(getResources().getString(R.string.input_psw_old));
            return;
        }
        String newPsw = etNewPsw.getText().toString().trim();
        if (TextUtils.isEmpty(newPsw) || newPsw.length() < 6) {
            ToastUtils.showShortToast(getResources().getString(R.string.input_psw_new));
            return;
        }
        String psw = etPsw.getText().toString().trim();
        if (TextUtils.isEmpty(psw) || psw.length() < 6) {
            ToastUtils.showShortToast(getResources().getString(R.string.input_psw_again));
            return;
        }
        if (!TextUtils.equals(newPsw, psw)) {
            ToastUtils.showShortToast(getResources().getString(R.string.psw_unlike));
            return;
        }

        RxView.clicks(tvConfirm).throttleFirst(3, TimeUnit.SECONDS);
        dialogTransformer = new DialogTransformer(_mActivity);
        Client.getApiService().forgetPwd(phone, psw, oldPsw)
                .compose(RxsRxSchedulers.<BaseBean>io_main())
                .compose(dialogTransformer.transformer())
                .subscribe(new ApiServiceResult(getComposite()) {
                    @Override
                    public void onNext(BaseBean bean) {
                        super.onNext(bean);
                        if (bean != null && TextUtils.equals(bean.getCode(), "0")) {
                            ToastUtils.showShortToast(bean.getMsg());

                            _mActivity.onBackPressed();
                        }
                    }
                });
    }
}
