package com.app.jzapp.videoapps.fragment.my;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.base.BaseBackFragment;
import com.app.jzapp.videoapps.base.BaseBean;
import com.app.jzapp.videoapps.http.ApiServiceResult;
import com.app.jzapp.videoapps.http.Client;
import com.app.jzapp.videoapps.http.DialogTransformer;
import com.app.jzapp.videoapps.http.RxsRxSchedulers;

import butterknife.BindView;
import butterknife.OnClick;

import static com.app.jzapp.videoapps.http.AppConfig.TOKEN;

public class VIPPayFragment extends BaseBackFragment {
    @BindView(R.id.paddingView)
    View paddingView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_vip_type)
    TextView tvVipType;
    @BindView(R.id.tv_vip_money)
    TextView tvVipMoney;
    @BindView(R.id.rl_vip_half_year)
    RelativeLayout rlVipHalfYear;
    @BindView(R.id.tv_alipay)
    TextView tvAlipay;
    @BindView(R.id.tv_wechat)
    TextView tvWechat;

    private String type;
    private String payType;
    private int payMoney;
    private int payNum;

    public static VIPPayFragment newInstance(String type) {
        VIPPayFragment fragment = new VIPPayFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getString("type");
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_vip_pay;
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

        title.setText(getString(R.string.buy_vip));

        switch (type) {
            case "year":
                payType = getString(R.string.pay_type_year);
                payMoney = 268;
                payNum = 365;
                break;
            case "quarter":
                payType = getString(R.string.pay_type_half_year);
                payMoney = 90;
                payNum = 68;
                break;
            case "month":
                payType = getString(R.string.pay_type_month);
                payMoney = 30;
                payNum = 30;
                break;
        }

        tvVipType.setText(payType);
        tvVipMoney.setText(String.format(getString(R.string.rmb), payMoney));

    }

    @OnClick({R.id.tv_alipay, R.id.tv_wechat})
    void buy(View view) {
        switch (view.getId()) {
            case R.id.tv_alipay:
                subOrder(1);
                break;
            case R.id.tv_wechat:
                break;
        }
    }

    /**
     * 下订单
     */
    private void subOrder(int i) {
//        Client.getApiService().subOrder(TOKEN, payNum, payMoney, i)
//                .compose(RxsRxSchedulers.<BaseBean<String>>io_main())
//                .compose(new DialogTransformer(_mActivity).transformer())
//                .subscribe(new ApiServiceResult<String>(getComposite()) {
//                    @Override
//                    public void onNext(BaseBean<String> bean) {
//                        super.onNext(bean);
//                        if (bean != null && !TextUtils.isEmpty(bean.getData()))
//                            pay(bean.getData());
//                    }
//                });
    }

    private void pay(String data) {
        Client.getApiService().payOrder(TOKEN, data)
                .compose(RxsRxSchedulers.<BaseBean<String>>io_main())
                .compose(new DialogTransformer(_mActivity).transformer())
                .subscribe(new ApiServiceResult<String>(getComposite()) {
                    @Override
                    public void onNext(BaseBean<String> bean) {
                        super.onNext(bean);
                        if (bean != null && !TextUtils.isEmpty(bean.getData())) {
                            Intent action = new Intent(Intent.ACTION_VIEW);
                            action.setData(Uri.parse(bean.getData()));
                            startActivity(action);
                        }
                    }
                });
    }

}
