package com.app.jzapp.videoapps.fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.app.jzapp.videoapps.AllHandle;
import com.app.jzapp.videoapps.MyCaptureActivity;
import com.app.jzapp.videoapps.event.LoginSuccessedEvent;
import com.app.jzapp.videoapps.event.SetPSWEvent;
import com.app.jzapp.videoapps.event.ShareSuccessedEvent;
import com.app.jzapp.videoapps.event.UserInfoEvent;
import com.app.jzapp.videoapps.fragment.my.BindAccountFragment;
import com.app.jzapp.videoapps.fragment.my.CodedLockFragment;
import com.app.jzapp.videoapps.fragment.my.ComplaintLineFragment;
import com.app.jzapp.videoapps.fragment.my.DownloadFragment;
import com.app.jzapp.videoapps.fragment.my.HistoryWatchFragment;
import com.app.jzapp.videoapps.fragment.my.LoginFragment;
import com.app.jzapp.videoapps.fragment.my.QRCodeFragment;
import com.app.jzapp.videoapps.fragment.my.SetterPSWFragment;
import com.app.jzapp.videoapps.fragment.my.VIPFragment;
import com.app.jzapp.videoapps.fragment.my.WithdrawDepositFragment;
import com.app.jzapp.videoapps.http.ApiServiceResult;
import com.app.jzapp.videoapps.http.AppConfig;
import com.app.jzapp.videoapps.http.Client;
import com.app.jzapp.videoapps.http.DialogTransformer;
import com.app.jzapp.videoapps.http.RxsRxSchedulers;
import com.app.jzapp.videoapps.utils.AppUtils;
import com.app.jzapp.videoapps.utils.IntentUtils;
import com.app.jzapp.videoapps.utils.KeyboardUtils;
import com.app.jzapp.videoapps.utils.MQGlideImageLoader4;
import com.app.jzapp.videoapps.utils.SPUtils;
import com.app.jzapp.videoapps.utils.ToastUtils;
import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.base.BaseBean;
import com.app.jzapp.videoapps.base.BaseMainFragment;
import com.app.jzapp.videoapps.bean.LoginBean;
import com.app.jzapp.videoapps.bean.UserBean;
import com.app.jzapp.videoapps.event.BindAccountSuccessedEvent;
import com.app.jzapp.videoapps.event.UserLoginOther;
import com.app.jzapp.videoapps.fragment.MyFragmentPermissionsDispatcher;
import com.app.jzapp.videoapps.utils.DialogUtils;
import com.app.jzapp.videoapps.utils.GlideUtils;
import com.meiqia.meiqiasdk.imageloader.MQImage;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * 我的
 */
@RuntimePermissions
public class MyFragment extends BaseMainFragment {
    private static final int REQUEST_CODE = 10086;
    @BindView(R.id.paddingView)
    View paddingView;
    @BindView(R.id.iv_oval)
    ImageView ivOval;
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.tv_account)
    TextView tvAccount;
    @BindView(R.id.ll_account)
    LinearLayout llAccount;
    @BindView(R.id.tv_bind_account)
    TextView tvBindAccount;
    @BindView(R.id.ll_bind_account)
    LinearLayout llBindAccount;
    @BindView(R.id.tv_account_number)
    TextView tvAccountNumber;
    @BindView(R.id.tv_watch_day)
    TextView tvWatchDay;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.tv_people_num)
    TextView tvPeopleNum;
    @BindView(R.id.tv_app_version)
    TextView tvAppVersion;
    @BindView(R.id.psw_switch)
    Switch pswSwitch;
    @BindView(R.id.ll_buy_vip)
    LinearLayout mLlBuyVip;
    @BindView(R.id.ll_share)
    LinearLayout mLlShare;
    @BindView(R.id.iv_qrcode)
    ImageView mIvQrcode;
    @BindView(R.id.iv_sacn_qrcode)
    ImageView mIvSacnQrcode;
    @BindView(R.id.tv_find_account)
    TextView mTvFindAccount;
    @BindView(R.id.tv_coded_lock)
    TextView mTvCodedLock;
    @BindView(R.id.tv_request_video)
    TextView mTvRequestVideo;
    @BindView(R.id.tv_history_watch)
    TextView mTvHistoryWatch;
    @BindView(R.id.tv_download)
    TextView mTvDownload;
    @BindView(R.id.tv_invitation_code)
    TextView mTvInvitationCode;
    @BindView(R.id.tv_customer_services)
    TextView mTvCustomerServices;
    @BindView(R.id.tv_car_crowd)
    TextView mTvCarCrowd;
    @BindView(R.id.iv_help)
    ImageView mIvHelp;
    @BindView(R.id.ll)
    LinearLayout mLl;
    @BindView(R.id.tv_show)
    TextView mTvShow;
    @BindView(R.id.rl_head)
    RelativeLayout mRlHead;
    @BindView(R.id.iv_oval_vip)
    ImageView mIvOvalVip;
    @BindView(R.id.iv_header_vip)
    ImageView mIvHeaderVip;
    @BindView(R.id.tv_month_des)
    TextView mTvMonthDes;
    @BindView(R.id.rl_head_vip)
    RelativeLayout mRlHeadVip;
    @BindView(R.id.tv_vip_type)
    TextView tvVipType;
    @BindView(R.id.ll_daili2)
    LinearLayout ll_daili2;


    private Dialog loadingDialog;

    private boolean isChangeUser;


    private AllHandle mHandler;

    public static MyFragment newInstance() {
        MyFragment fragment = new MyFragment();
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.fragment_my;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZXingLibrary.initDisplayOpinion(_mActivity);
        EventBus.getDefault().register(this);

    }


    // 单个权限
    @NeedsPermission(Manifest.permission.CAMERA)
    void showCamera() {
        Intent intent = new Intent(_mActivity, MyCaptureActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**
         * 处理二维码扫描结果
         */
        Log.e("cjn", "GGGGGGG" + requestCode + "       " + REQUEST_CODE);
        if (requestCode == REQUEST_CODE) {
            Log.e("cjn", "EEEEEE");
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    Log.e("cjn", "BBBBBBB");
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    Log.e("cjn", "CCCCCCC");
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    qrCodeLogin(result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Log.e("cjn", "DDDDDDD");
                    Toast.makeText(_mActivity, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
        Log.e("cjn", "FFFFFFF");
    }

    private void qrCodeLogin(String result) {
        dialogTransformer = new DialogTransformer(_mActivity);
        Client.getApiService().getToken(result + "&&flag=1").flatMap(new Function<BaseBean<String>, ObservableSource<BaseBean<LoginBean>>>() {
            @Override
            public ObservableSource<BaseBean<LoginBean>> apply(BaseBean<String> bean) throws Exception {
                if (!bean.getCode().equals("0")) {
                    Log.e("cjn", "bean.getMsg()" + bean.getMsg().toString());
                    ToastUtils.showShortToastSafe(bean.getMsg());
                }
                return Client.getApiService().doLogin(bean.getData());
            }
        }).flatMap(new Function<BaseBean<LoginBean>, ObservableSource<BaseBean<UserBean>>>() {
            @Override
            public ObservableSource<BaseBean<UserBean>> apply(BaseBean<LoginBean> bean) throws Exception {
                if (bean.getCode().equals("0")) {
                    if (bean != null && bean.getData() != null) {
                        new SPUtils(AppConfig.SP_NAME).putString(AppConfig.SP_TOKEN, bean.getData().getToken());
                        AppConfig.TOKEN = bean.getData().getToken();
                    }
                }
                return Client.getApiService().getUserInfo(AppConfig.TOKEN);
            }
        })
                .compose(RxsRxSchedulers.<BaseBean<UserBean>>io_main())
                .compose(dialogTransformer.transformer())
                .subscribe(new ApiServiceResult<UserBean>(getComposite()) {
                    @Override
                    public void onNext(BaseBean<UserBean> bean) {
                        super.onNext(bean);
                        if (bean != null && bean.getData() != null) {
                            Log.e("cjn", "bean.getData()" + bean.getData().toString());
                            EventBus.getDefault().post(new UserLoginOther());
                            AppConfig.USERBEAN = bean.getData();
                            initData();
                        }
                    }
                });
    }

    // 向用户说明为什么需要这些权限（可选）
    @OnShowRationale(Manifest.permission.CAMERA)
    void showRationaleForCamera(final PermissionRequest request) {
        new AlertDialog.Builder(_mActivity)
                .setMessage(R.string.permission_camera_rationale)
                .show();
    }

    // 单个权限
    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showWriteExternalStorage() {
        MQConfig.isShowClientAvatar = true;
        HashMap<String, String> clientInfo = new HashMap<>();
        clientInfo.put("name", AppConfig.USERBEAN.getRandomnum());
        clientInfo.put("avatar", AppConfig.USERBEAN.getPic());
        Intent intent = new MQIntentBuilder(_mActivity)
                .setCustomizedId(AppConfig.USERBEAN.getRandomnum()) // 相同的 id 会被识别为同一个顾客
                .setClientInfo(clientInfo) // 设置顾客信息 PS: 这个接口只会生效一次,如果需要更新顾客信息,需要调用更新接口
                .updateClientInfo(clientInfo) // 更新顾客信息 PS: 如果客服在工作台更改了顾客信息，更新接口会覆盖之前的内容
                .build();
        startActivity(intent);
    }

    // 向用户说明为什么需要这些权限（可选）
    @OnShowRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    void showRationaleForWriteExternalStorage(final PermissionRequest request) {
        new AlertDialog.Builder(_mActivity)
                .setMessage(R.string.permission_writeexternalstorage_rationale)
                .show();
    }

    @Override
    protected void initView() {

    }

//    public void initdata11() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(1500);
//                    mHandler.sendEmptyMessage(0);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//
//
//    }


    @Override
    protected void initData() {
        super.initData();
        tvAppVersion.setText(AppUtils.getAppVersionName(_mActivity));
        pswSwitch.setChecked(new SPUtils(AppConfig.SP_NAME).getBoolean(AppConfig.PSW_SWITCH));
        if (AppConfig.USERBEAN != null) {
            ImageView imageView;
            if (AppConfig.USERBEAN.getIs_vip() == 1) {//会员

                mRlHeadVip.setVisibility(View.VISIBLE);
                mRlHead.setVisibility(View.GONE);
                imageView = mIvHeaderVip;

                tvVipType.setText(AppConfig.USERBEAN.getVipotype().toString());
                if (AppConfig.USERBEAN.getVipotype().toString().equals("月卡")) {
                    mRlHeadVip.setBackground(getResources().getDrawable(R.mipmap.vip_month));
                }
                if (AppConfig.USERBEAN.getVipotype().toString().equals("季卡")) {
                    mRlHeadVip.setBackground(getResources().getDrawable(R.mipmap.vip_quarter));
                }
                if (AppConfig.USERBEAN.getVipotype().toString().equals("年卡")) {
                    mRlHeadVip.setBackground(getResources().getDrawable(R.mipmap.vip_yead));
                }

//                switch (USERBEAN.getVipotype()) {
//                    case 1:
//                        tvVipType.setText(getString(R.string.vip_month));
//                        break;
//                    case 5:
//                        tvVipType.setText(getString(R.string.vip_quarter));
//                        break;
//                    case 10:
//                        tvVipType.setText(getString(R.string.vip_year));
//                        break;
//                }
            } else {
                mRlHead.setVisibility(View.VISIBLE);
                mRlHeadVip.setVisibility(View.GONE);
                imageView = ivHeader;
            }

            if (!TextUtils.isEmpty(AppConfig.USERBEAN.getPic())) {

                GlideUtils.loadCircleImagView(_mActivity, AppConfig.USERBEAN.getPic(), imageView);
            } else
                GlideUtils.loadCircleImagView(_mActivity, R.mipmap.head, imageView);

            //每日追剧次数
            tvWatchDay.setText("每日追剧次数" + AppConfig.USERBEAN.getLookedcount() + "/" + AppConfig.USERBEAN.getLookcount());
//            tvWatchDay.setText(String.format(getString(R.string.residue_num), USERBEAN.getLookedcount() + "/" + USERBEAN.getLookcount()));

            //余额
            tvMoney.setText(AppConfig.USERBEAN.getResidual_asset());
//            tvMoney.setText(String.format(getString(R.string.show_head_3), USERBEAN.getResidual_asset()));
            //已推广认输
            tvPeopleNum.setText("已推广" + AppConfig.USERBEAN.getSharecount());
//            tvPeopleNum.setText(String.format(getString(R.string.show_head_4), USERBEAN.getSharecount()));

            if (TextUtils.isEmpty(AppConfig.USERBEAN.getMobile())) {
                tvAccountNumber.setText(getString(R.string.up_watch_number));
            } else {
                tvAccountNumber.setText(AppConfig.USERBEAN.getMobile());
            }
//            //6暂时不确定这个问题
            GlideUtils.loadCircleImagView(_mActivity, AppConfig.USERBEAN.getPic(), ivHeader);
            GlideUtils.loadCircleImagView(_mActivity, AppConfig.USERBEAN.getPic(), mIvHeaderVip);
        } else {
            GlideUtils.loadCircleImagView(_mActivity, R.mipmap.head, ivHeader);
            getUserInfo();
        }

    }


    @Override
    protected View setStatusBarView() {
        return paddingView;
    }

    @Override
    protected int statusBarColor() {
        return R.color.white;
    }

    @OnClick(R.id.iv_help)
    void help() {
        showHelpDialog();
    }

    @OnClick(R.id.tv_car_crowd)
    void crowd() {
        startActivity(IntentUtils.getViewIntent(AppConfig.CAR_CROWD));

    }

    /**
     * 招募代理
     */
    @OnClick(R.id.ll_daili2)
    void daili() {
        ((MainFragment) getParentFragment()).startBrotherFragment(WebFragment.newInstance(AppConfig.getUrl()+"upload/zhaomu/index.html"));

    }


    @OnClick(R.id.ll_buy_vip)
    void vip() {
        ((MainFragment) getParentFragment()).startBrotherFragment(VIPFragment.newInstance());
    }

    /**
     * 提现
     */

    @OnClick(R.id.ll_withdraw_deposit)
    void withdrawDeposit() {
        ((MainFragment) getParentFragment()).startBrotherFragment(WithdrawDepositFragment.newInstance());
    }

    @OnClick(R.id.iv_qrcode)
    void qrcode() {
        ((MainFragment) getParentFragment()).startBrotherFragment(QRCodeFragment.newInstance(AppConfig.USERBEAN.getRandomnum()));
    }

    @OnClick(R.id.iv_sacn_qrcode)
    void sacnQRCode() {
        MyFragmentPermissionsDispatcher.showCameraWithPermissionCheck(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MyFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnClick(R.id.ll_account)
    void changeAccount() {
        ((MainFragment) getParentFragment()).startBrotherFragment(LoginFragment.newInstance(AppConfig.USERBEAN.getRandomnum()));
    }

    @OnClick(R.id.tv_find_account)
    void findAccount() {
        ((MainFragment) getParentFragment()).startBrotherFragment(BindAccountFragment.newInstance(true));
    }

    @OnClick(R.id.ll_bind_account)
    void bindAccount() {
        ((MainFragment) getParentFragment()).startBrotherFragment(BindAccountFragment.newInstance(false));
    }

    @OnClick(R.id.tv_coded_lock)
    void codedLock() {
        ((MainFragment) getParentFragment()).startBrotherFragment(CodedLockFragment.newInstance(AppConfig.USERBEAN.getIs_safe() == 1));
    }

//    @OnClick(R.id.ll_change_bind_account)
//    void changeBindAccount() {
//        ((MainFragment) getParentFragment()).startBrotherFragment(ChangeBindAccountFragment.newInstance());
//    }

//    @OnClick(R.id.tv_change_psw)
//    void changePsw() {
//        ((MainFragment) getParentFragment()).startBrotherFragment(ChangePswFragment.newInstance());
//    }

//    @OnClick(R.id.tv_project_management)
//    void projectManagement() {
//        ((MainFragment) getParentFragment()).startBrotherFragment(ProjectManagementFragment.newInstance());
//    }

    @OnClick(R.id.tv_request_video)
    void complaintLine() {
        ((MainFragment) getParentFragment()).startBrotherFragment(ComplaintLineFragment.newInstance());
    }

    /**
     * 历史记录
     */
    @OnClick(R.id.tv_history_watch)
    void historyWatch() {
        ((MainFragment) getParentFragment()).startBrotherFragment(HistoryWatchFragment.newInstance());
    }

    /**
     * 下载管理
     */
    @OnClick(R.id.tv_download)
    void download() {
        ((MainFragment) getParentFragment()).startBrotherFragment(DownloadFragment.newInstance());
    }

    /**
     * 推广分享
     */
    @OnClick(R.id.ll_share)
    void share() {
        ((MainFragment) getParentFragment()).startBrotherFragment(WebFragment.newInstance("http://youkuav.cc/appsetup/sharelink.php?token="+ AppConfig.TOKEN));
//        ((MainFragment) getParentFragment()).startBrotherFragment(ShareFragment.newInstance());
    }

    @OnClick(R.id.psw_switch)
    void pswSwitch() {
        if (pswSwitch.isChecked()) {
            ((MainFragment) getParentFragment()).startBrotherFragment(SetterPSWFragment.newInstance());
        } else {
            new SPUtils(AppConfig.SP_NAME).putBoolean(AppConfig.PSW_SWITCH, false);
            new SPUtils(AppConfig.SP_NAME).putString(AppConfig.PSW, "");
        }
        pswSwitch.setChecked(false);
    }

    @OnClick(R.id.tv_invitation_code)
    void invitationCode() {
        showInvitationCodeDialog();
    }

    @OnClick(R.id.tv_customer_services)
    void customerServices() {
        MQImage.setImageLoader(new MQGlideImageLoader4());
        MyFragmentPermissionsDispatcher.showWriteExternalStorageWithPermissionCheck(this);
    }

    /**
     * 设置密码锁成功
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setPSWEvent(SetPSWEvent event) {
        pswSwitch.setChecked(true);
    }

    /**
     * 登录成功
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginSuccessedEvent(LoginSuccessedEvent event) {
        getUserInfo();
        isChangeUser = true;
    }

    /**
     * 绑定手机成功
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void bindAccountSuccessedEvent(BindAccountSuccessedEvent event) {
        getUserInfo();
    }

    /**
     * 获取用户信息成功
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void userInfoEvent(UserInfoEvent event) {
        initData();
        if (isChangeUser) {
            isChangeUser = false;
            EventBus.getDefault().post(new UserLoginOther());
        }
    }

    /**
     * 输入邀请码
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void shareSuccessed(ShareSuccessedEvent event) {
        getUserInfo();
    }

    private void showInvitationCodeDialog() {
        dialogTransformer = new DialogTransformer(_mActivity);
        View view = View.inflate(_mActivity, R.layout.dialog_input_code, null);
        final EditText etCode = view.findViewById(R.id.et_code);
        view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.dismiss();
                loadingDialog = null;
                KeyboardUtils.hideSoftInput(_mActivity);
            }
        });
        view.findViewById(R.id.tv_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.dismiss();
                loadingDialog = null;
                KeyboardUtils.hideSoftInput(_mActivity);
            }
        });
        view.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = etCode.getText().toString().trim();
                if (TextUtils.isEmpty(code)) {
                    ToastUtils.showShortToast(getString(R.string.input_invitation_code3));
                } else {
                    subInviteCode(code);
                }
            }
        });
        loadingDialog = new DialogUtils().showDialog(_mActivity, view, false);
    }

    private void subInviteCode(String code) {
        Client.getApiService().subInviteCode(AppConfig.TOKEN, code)
                .compose(RxsRxSchedulers.<BaseBean>io_main())
                .compose(dialogTransformer.transformer())
                .subscribe(new ApiServiceResult(getComposite()) {
                    @Override
                    public void onNext(BaseBean bean) {
                        Log.e("cjn", "看看这的数据" + bean.toString());
                        if (bean != null) {
                            ToastUtils.showShortToast(bean.getMsg());

                            loadingDialog.dismiss();
                            loadingDialog = null;
                            KeyboardUtils.hideSoftInput(_mActivity);
                        }
                    }
                });
    }

    private void showHelpDialog() {
        LayoutInflater inflater = LayoutInflater.from(_mActivity);
        View v = inflater.inflate(R.layout.dialog_help, null);// 得到加载view
        LinearLayout layout = v.findViewById(R.id.dialog_view);// 加载布局

        loadingDialog = new Dialog(_mActivity, R.style.loading_dialog);// 创建自定义样式dialog

        loadingDialog.setCancelable(true);// 不可以用“返回键”取消
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
        loadingDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }


}
