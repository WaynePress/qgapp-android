package com.app.jzapp.videoapps.fragment.my;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.base.BaseBackFragment;
import com.app.jzapp.videoapps.base.BaseBean;
import com.app.jzapp.videoapps.http.ApiServiceResult;
import com.app.jzapp.videoapps.http.AppConfig;
import com.app.jzapp.videoapps.http.Client;
import com.app.jzapp.videoapps.http.DialogTransformer;
import com.app.jzapp.videoapps.http.RxsRxSchedulers;
import com.app.jzapp.videoapps.utils.DialogUtils;
import com.app.jzapp.videoapps.utils.Md5Util;
import com.app.jzapp.videoapps.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.app.jzapp.videoapps.http.AppConfig.KEY;
import static com.app.jzapp.videoapps.http.AppConfig.TOKEN;

public class VIPFragment extends BaseBackFragment {
    @BindView(R.id.paddingView)
    View paddingView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tv_price_month)
    TextView tv_price_month;
    @BindView(R.id.tv_price_quarter)
    TextView tv_price_quarter;
    @BindView(R.id.tv_price_yead)
    TextView tv_price_yead;

    private String payMoney;
    private int payNum;
    private int vipotype;

    private Dialog loadingDialog;


    private String vip_month_money;
    private String vip_season_money;
    private String vip_year_money;


    public static VIPFragment newInstance() {
        VIPFragment fragment = new VIPFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();

    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_vip;
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
        getVip();

    }

    @OnClick({R.id.tv_buy_vip_year, R.id.tv_buy_vip_quarter, R.id.tv_buy_vip_month})
    void buy(View view) {
        switch (view.getId()) {
            case R.id.tv_buy_vip_year:
                payMoney = vip_year_money;
                payNum = 365;
                vipotype = 10;
//                start(VIPPayFragment.newInstance("year"));
                break;
            case R.id.tv_buy_vip_quarter:
                payMoney = vip_season_money;
                payNum = 90;
                vipotype = 5;
//                start(VIPPayFragment.newInstance("quarter"));
                break;
            case R.id.tv_buy_vip_month:
                payMoney = vip_month_money;
                payNum = 30;
                vipotype = 1;
//                start(VIPPayFragment.newInstance("month"));
                break;
        }
        showPayDialog();
    }

    private void showPayDialog() {
        View view = View.inflate(_mActivity, R.layout.dialog_pay, null);
        view.findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.dismiss();
                loadingDialog = null;

            }
        });
        view.findViewById(R.id.tv_balance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.dismiss();
                loadingDialog = null;
                subOrder(3);
            }
        });
        view.findViewById(R.id.tv_alipay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.dismiss();
                loadingDialog = null;
                subOrder(1);
            }
        });
        view.findViewById(R.id.tv_weixin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.dismiss();
                loadingDialog = null;
                subOrder(2);
            }
        });


        loadingDialog = new DialogUtils().showDialog(_mActivity, view, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

    /**
     * 下订单
     */
    private void subOrder(final int i) {
        Client.getApiService().subOrder(TOKEN, payNum, payMoney, i, vipotype)
                .compose(RxsRxSchedulers.<BaseBean<String>>io_main())
                .compose(new DialogTransformer(_mActivity).transformer())
                .subscribe(new ApiServiceResult<String>(getComposite()) {
                    @Override
                    public void onNext(BaseBean<String> bean) {
                        super.onNext(bean);
                        if (bean != null) {
                            if (i == 3) {
                                ToastUtils.showShortToast(bean.getMsg());
                            } else if (!TextUtils.isEmpty(bean.getData())) {
                                pay(bean.getData());
                            }

                        }
                    }
                });
    }

    private void getVip() {
        String timestamp = "";
        StringBuilder s = new StringBuilder();
        String timestampasd = System.currentTimeMillis() + "";

        s.append(timestampasd);
        s.deleteCharAt(s.length() - 1);
        s.deleteCharAt(s.length() - 1);
        s.deleteCharAt(s.length() - 1);
        timestamp = s.toString();
        String randomstr = (int) ((Math.random() * 9 + 1) * 100000) + "";
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("token", TOKEN)
                .addFormDataPart("randomstr", randomstr)
                .addFormDataPart("timestamp", timestamp)
                .addFormDataPart("signature", Md5Util.getMD5(randomstr + timestamp.toString() + KEY).toLowerCase())


                .build();
        final Request request = new Request.Builder()
                .url(AppConfig.getUrl()+"api/v110/app/getVip")
                .post(requestBody)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("cjn", "看看这个是啥");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseStr = response.body().string();
                Log.e("cjn", "成功数据" + responseStr);
                try {
                    JSONObject json = new JSONObject(responseStr);
                    JSONObject jsonObject = json.optJSONObject("data");
                    Log.e("cjn", "看看解析的数据" + jsonObject.optString("vip_month_money"));
                    vip_month_money = jsonObject.optString("vip_month_money");
                    vip_season_money = jsonObject.optString("vip_season_money");
                    vip_year_money = jsonObject.optString("vip_year_money");
                    Message message = handler.obtainMessage();
                    message.what = 1;
                    handler.sendMessage(message);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1 && tv_price_month != null) {
                tv_price_month.setText(vip_month_money);
                tv_price_quarter.setText(vip_season_money);
                tv_price_yead.setText(vip_year_money);
            }

        }
    };

    private void pay(String data) {
        Client.getApiService().payOrder(TOKEN, data)
                .compose(RxsRxSchedulers.<BaseBean<String>>io_main())
                .compose(new DialogTransformer(_mActivity).transformer())
                .subscribe(new ApiServiceResult<String>(getComposite()) {
                    @Override
                    public void onNext(BaseBean<String> bean) {
                        super.onNext(bean);
                        Log.e("cjn", "我看你的数据是哈" + bean);
                        if (bean != null && !TextUtils.isEmpty(bean.getData())) {
                            Intent action = new Intent(Intent.ACTION_VIEW);
                            action.setData(Uri.parse(bean.getData()));
                            startActivity(action);
                        }
                    }
                });
    }

}
