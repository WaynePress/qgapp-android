package com.app.jzapp.videoapps.fragment.my;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.base.BaseBackFragment;
import com.app.jzapp.videoapps.base.BaseBean;
import com.app.jzapp.videoapps.http.ApiServiceResult;
import com.app.jzapp.videoapps.http.Client;
import com.app.jzapp.videoapps.http.DialogTransformer;
import com.app.jzapp.videoapps.http.RxsRxSchedulers;
import com.app.jzapp.videoapps.utils.ToastUtils;
import com.jakewharton.rxbinding3.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;

import static com.app.jzapp.videoapps.http.AppConfig.TOKEN;

public class ChangeBindAccountFragment extends BaseBackFragment {
    @BindView(R.id.paddingView)
    View paddingView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_psw)
    EditText etPsw;
    @BindView(R.id.tv_send)
    TextView tvSend;

    public static ChangeBindAccountFragment newInstance() {
        ChangeBindAccountFragment fragment = new ChangeBindAccountFragment();
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_change_bind_account;
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
        title.setText(getString(R.string.email_bind));
    }

    @OnClick(R.id.tv_send)
    void send() {
        String psw = etPsw.getText().toString().trim();
        if (TextUtils.isEmpty(psw) || psw.length() < 6) {
            ToastUtils.showShortToast(getResources().getString(R.string.input_psw));
            return;
        }
        RxView.clicks(tvSend).throttleFirst(3, TimeUnit.SECONDS);
        dialogTransformer = new DialogTransformer(_mActivity);
        Client.getApiService().confirmPwd(TOKEN, psw)
                .compose(RxsRxSchedulers.<BaseBean>io_main())
                .compose(dialogTransformer.transformer())
                .subscribe(new ApiServiceResult(getComposite()) {
                    @Override
                    public void onNext(BaseBean bean) {
                        if (bean != null) {

                        }
                    }
                });
    }
}
