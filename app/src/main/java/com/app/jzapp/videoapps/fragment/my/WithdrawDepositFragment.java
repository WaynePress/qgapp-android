package com.app.jzapp.videoapps.fragment.my;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.base.BaseBackFragment;
import com.app.jzapp.videoapps.base.BaseBean;
import com.app.jzapp.videoapps.bean.AssetsBean;
import com.app.jzapp.videoapps.http.ApiServiceResult;
import com.app.jzapp.videoapps.http.Client;
import com.app.jzapp.videoapps.http.DialogTransformer;
import com.app.jzapp.videoapps.http.RxsRxSchedulers;
import com.app.jzapp.videoapps.utils.ToastUtils;
import com.app.jzapp.videoapps.view.BottomMenuDialog;

import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.app.jzapp.videoapps.http.AppConfig.TOKEN;

public class WithdrawDepositFragment extends BaseBackFragment {
    @BindView(R.id.paddingView)
    View paddingView;
    @BindView(R.id.tv_achievements)
    TextView mTvAchievements;
    @BindView(R.id.tv_balance)
    TextView mTvBalance;
    @BindView(R.id.tv_proxy_1)
    TextView mTvProxy1;
    @BindView(R.id.tv_proxy_2)
    TextView mTvProxy2;
    @BindView(R.id.tv_proxy_3)
    TextView mTvProxy3;
    @BindView(R.id.tv_proxy_4)
    TextView mTvProxy4;
    @BindView(R.id.tv_returns_detailed)
    TextView mTvReturnsDetailed;
    @BindView(R.id.tv_withdrawal_record)
    TextView mTvWithdrawalRecord;
    @BindView(R.id.tv_bink_card)
    TextView mTvBinkCard;
    @BindView(R.id.tv_withdraw_deposit_charge)
    TextView mTvWithdrawDepositCharge;
    @BindView(R.id.tv_name_of_payee)
    TextView mTvNameOfPayee;
    @BindView(R.id.tv_bank_id)
    TextView mTvBankId;
    @BindView(R.id.tv_money)
    TextView mTvMoney;
    @BindView(R.id.tv_security_code)
    TextView mTvSecurityCode;
    @BindView(R.id.tv_submit)
    TextView mTvSubmit;

    private BottomMenuDialog dialog;

    public static WithdrawDepositFragment newInstance() {
        WithdrawDepositFragment fragment = new WithdrawDepositFragment();
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_withdraw_deposit;
    }

    @Override
    protected View setStatusBarView() {
        return paddingView;
    }

    @Override
    protected int statusBarColor() {
        return R.color.c_EC72AD;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        Client.getApiService().getUserAssetInfo(TOKEN)
                .compose(RxsRxSchedulers.<BaseBean<AssetsBean>>io_main())
                .compose(new DialogTransformer(_mActivity).transformer())
                .subscribe(new ApiServiceResult<AssetsBean>(getComposite()) {
                    @Override
                    public void onNext(BaseBean<AssetsBean> bean) {
                        if (bean != null && bean.getData() != null) {
                            setData(bean.getData());
                        }
                    }
                });
    }

    private void setData(AssetsBean data) {
        mTvProxy1.setText(data.getFirst_level());
        mTvProxy2.setText(data.getSecond_level());
        mTvProxy3.setText(data.getThird_level());
        mTvProxy4.setText(data.getFourth_level());

        mTvAchievements.setText(String.format(getString(R.string.achievements), data.getAsset()));
        mTvBalance.setText(String.format(getString(R.string.balance), data.getResidual_asset()));

        mTvWithdrawDepositCharge.setText(data.getBrokerage() + "%");
    }

    @OnClick(R.id.tv_withdrawal_record)
    void withdrawalRecord() {
        start(WithdrawalRecordFragment.newInstance());
    }

    @OnClick(R.id.tv_returns_detailed)
    void returnsDetailed() {
        start(ReturnsDetailedFragment.newInstance());
    }

    @OnClick(R.id.tv_bink_card)
    void binkCard() {
        if (dialog == null)
            dialog = new BottomMenuDialog(_mActivity).setCanCancel(false).buildWithdrawDeposit();
        dialog.show();
    }


    @OnClick(R.id.tv_submit)
    void submit() {
        String name = mTvNameOfPayee.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showShortToast(getString(R.string.input_name_of_payee));
            return;
        }

        final String bankID = mTvBankId.getText().toString().trim();
        if (TextUtils.isEmpty(bankID)) {
            ToastUtils.showShortToast(getString(R.string.input_bank_id));
            return;
        }

        String money = mTvMoney.getText().toString().trim();
        if (TextUtils.isEmpty(money)) {
            ToastUtils.showShortToast(getString(R.string.input_withdraw_deposit_money));
            return;
        }

        String code = mTvSecurityCode.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            ToastUtils.showShortToast(getString(R.string.input_security_code));
            return;
        }

        Map<String, String> map = new LinkedHashMap<>();
        map.put("token", TOKEN);
        map.put("uname", name);
        map.put("bankcard", bankID);
        map.put("cash_asset", money);
        map.put("safecode", code);

        Client.getApiService().subUserWithdraw(map)
                .compose(RxsRxSchedulers.<BaseBean>io_main())
                .compose(new DialogTransformer(_mActivity).transformer())
                .subscribe(new ApiServiceResult(getComposite()) {
                    @Override
                    public void onNext(BaseBean bean) {
                        if (bean != null) {
                            ToastUtils.showShortToast(bean.getMsg());
                            initData();
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
    }

}
