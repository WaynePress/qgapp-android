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
import com.app.jzapp.videoapps.bean.LoginBean;
import com.app.jzapp.videoapps.event.LoginSuccessedEvent;
import com.app.jzapp.videoapps.http.ApiServiceResult;
import com.app.jzapp.videoapps.http.Client;
import com.app.jzapp.videoapps.http.DialogTransformer;
import com.app.jzapp.videoapps.http.RxsRxSchedulers;
import com.app.jzapp.videoapps.utils.DialogUtils;
import com.app.jzapp.videoapps.utils.SPUtils;
import com.jakewharton.rxbinding3.view.RxView;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

import static com.app.jzapp.videoapps.http.AppConfig.SP_NAME;
import static com.app.jzapp.videoapps.http.AppConfig.SP_TOKEN;
import static com.app.jzapp.videoapps.http.AppConfig.TOKEN;

public class LoginFragment extends BaseBackFragment {
    @BindView(R.id.paddingView)
    View paddingView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.et_psw)
    EditText etPsw;
    @BindView(R.id.tv_login)
    TextView tvLogin;

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_current_account)
    TextView mTvCurrentAccount;

    private AlertDialog loginDialog;

    private Map<String, String> map;

    public static LoginFragment newInstance(String account) {
        LoginFragment fragment = new LoginFragment();
        Bundle bundle = new Bundle();
        bundle.putString("account", account);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_login;
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
        title.setText(getString(R.string.change_account));

        mTvCurrentAccount.setText(String.format(getString(R.string.current_account), getArguments().getString("account")));
    }

    @Override
    protected void initData() {

    }


    @OnClick(R.id.tv_login)
    void login() {
        map = new HashMap<>();

        String phone = etPhone.getText().toString().trim();
//        if (TextUtils.isEmpty(phone)) {
//            ToastUtils.showShortToast(getResources().getString(R.string.input_phone));
//            return;
//        } else if (!RegexUtils.isMobileSimple(phone)) {
//            ToastUtils.showShortToast(getResources().getString(R.string.phone_error));
//            return;
//        }
        map.put("number", phone);
        map.put("token", TOKEN);

        String psw = etPsw.getText().toString().trim();
        if (!TextUtils.isEmpty(psw)) {
            map.put("safecode", psw);
        }


        RxView.clicks(tvLogin).throttleFirst(3, TimeUnit.SECONDS);
        dialogTransformer = new DialogTransformer(_mActivity);
        Client.getApiService().login(map).compose(RxsRxSchedulers.<BaseBean<LoginBean>>io_main())
                .compose(dialogTransformer.transformer())
                .subscribe(new ApiServiceResult<LoginBean>(getComposite()) {
                    @Override
                    public void onNext(BaseBean<LoginBean> bean) {
                        super.onNext(bean);
                        if (bean != null && bean.getData() != null) {
                            new SPUtils(SP_NAME).putString(SP_TOKEN, bean.getData().getToken());
                            TOKEN = bean.getData().getToken();
                            EventBus.getDefault().post(new LoginSuccessedEvent());
                            showLoginSuccessedDialog();
                        }
                    }
                });
    }

    private void showLoginSuccessedDialog() {
        View view = View.inflate(_mActivity, R.layout.dialog_login_successed, null);

        view.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
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
