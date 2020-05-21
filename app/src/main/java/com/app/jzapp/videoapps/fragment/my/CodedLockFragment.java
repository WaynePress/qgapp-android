package com.app.jzapp.videoapps.fragment.my;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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
import com.app.jzapp.videoapps.utils.DialogUtils;
import com.app.jzapp.videoapps.utils.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.app.jzapp.videoapps.http.AppConfig.TOKEN;
import static com.app.jzapp.videoapps.http.AppConfig.USERBEAN;

public class CodedLockFragment extends BaseBackFragment {
    @BindView(R.id.paddingView)
    View paddingView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.ll_head)
    LinearLayout mLlHead;
    @BindView(R.id.et_code)
    EditText mEtCode;
    @BindView(R.id.ll_edit)
    LinearLayout mLlEdit;
    @BindView(R.id.tv_change_coded_lock)
    TextView mTvChangeCodedLock;
    @BindView(R.id.ll_change)
    LinearLayout mLlChange;
    @BindView(R.id.tv_bind)
    TextView mTvBind;

    private AlertDialog loginDialog;

    private String code;

    public static CodedLockFragment newInstance(boolean isSetted) {
        CodedLockFragment fragment = new CodedLockFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isSetted", isSetted);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_coded_lock;
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
        title.setText(getString(R.string.set_coded_lock));
    }

    @Override
    protected void initData() {
        if (getArguments().getBoolean("isSetted")) {
            mLlChange.setVisibility(View.VISIBLE);
        } else {
            showAlertDialog();

            mLlEdit.setVisibility(View.VISIBLE);
            mTvBind.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.tv_change_coded_lock)
    void changeCodedLock() {
        mLlChange.setVisibility(View.GONE);
        mLlEdit.setVisibility(View.VISIBLE);
        mTvBind.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tv_bind)
    void bind() {
        code = mEtCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            ToastUtils.showShortToast(getResources().getString(R.string.input_coded_lock));
            return;
        }

        dialogTransformer = new DialogTransformer(_mActivity);
        Client.getApiService().doSafeCode(TOKEN, code)
                .compose(RxsRxSchedulers.<BaseBean>io_main())
                .compose(dialogTransformer.transformer())
                .subscribe(new ApiServiceResult(getComposite()) {
                    @Override
                    public void onNext(BaseBean bean) {
                        if (bean != null) {
                            USERBEAN.setIs_safe(1);
                            showBindSuccessedDialog(String.format(getString(R.string.coded_lock_successed), code));
                        }
                    }
                });
    }

    private void showAlertDialog() {
        View view = View.inflate(_mActivity, R.layout.dialog_coded_lock, null);
        view.findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginDialog.dismiss();
                loginDialog = null;

                _mActivity.onBackPressed();
            }
        });
        view.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginDialog.dismiss();
                loginDialog = null;
            }
        });
        loginDialog = new DialogUtils().showDialog(_mActivity, view, false);
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
