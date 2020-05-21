package com.app.jzapp.videoapps;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.app.jzapp.videoapps.http.ApiServiceResult;
import com.app.video.videoapps.R;
import com.app.jzapp.videoapps.base.BaseActivity;
import com.app.jzapp.videoapps.base.BaseBean;
import com.app.jzapp.videoapps.bean.DownLoadBean;
import com.app.jzapp.videoapps.bean.LoginBean;
import com.app.jzapp.videoapps.bean.UserBean;
import com.app.jzapp.videoapps.fragment.MainFragment;
import com.app.jzapp.videoapps.fragment.ShowPSWFragment;
import com.app.jzapp.videoapps.http.AppConfig;
import com.app.jzapp.videoapps.http.Client;
import com.app.jzapp.videoapps.http.RxsRxSchedulers;
import com.app.jzapp.videoapps.http.UpdateAppHttpUtil;
import com.app.jzapp.videoapps.utils.AppUtils;
import com.app.jzapp.videoapps.utils.DownLoadSqlUtils;
import com.app.jzapp.videoapps.utils.SPUtils;
import com.app.jzapp.videoapps.utils.ToastUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;
import com.yaoxiaowen.download.DownloadConstant;
import com.yaoxiaowen.download.DownloadStatus;
import com.yaoxiaowen.download.FileInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

import static com.app.jzapp.videoapps.http.AppConfig.PSW_SWITCH;
import static com.app.jzapp.videoapps.http.AppConfig.SP_NAME;
import static com.app.jzapp.videoapps.http.AppConfig.SP_TOKEN;
import static com.app.jzapp.videoapps.http.AppConfig.TOKEN;
import static com.app.jzapp.videoapps.http.AppConfig.USERBEAN;
import static com.app.jzapp.videoapps.http.AppConfig.sDownLoadBeans;


public class MainActivity extends BaseActivity {

    @Override
    protected int setLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        TOKEN = new SPUtils(SP_NAME).getString(SP_TOKEN);
        if (TextUtils.isEmpty(TOKEN)) {
            Client.getApiService().randomUser("2")
                    .flatMap(new Function<BaseBean<LoginBean>, ObservableSource<BaseBean<UserBean>>>() {
                        @Override
                        public ObservableSource<BaseBean<UserBean>> apply(BaseBean<LoginBean> bean) throws Exception {
                            if (bean != null && bean.getData() != null) {
                                new SPUtils(SP_NAME).putString(SP_TOKEN, bean.getData().getToken());
                                TOKEN = bean.getData().getToken();
                                return Client.getApiService().getUserInfo(TOKEN);
                            } else {
                                ToastUtils.showShortToast(bean.getMsg());
                                return null;
                            }
                        }
                    })
                    .compose(RxsRxSchedulers.<BaseBean<UserBean>>io_main())
                    .subscribe(new ApiServiceResult<UserBean>(getComposite()) {
                        @Override
                        public void onNext(BaseBean<UserBean> bean) {
                            super.onNext(bean);
                            if (bean != null && bean.getData() != null) {
                                USERBEAN = bean.getData();
                                if (findFragment(MainFragment.class) == null) {
                                    Log.e("cjn", "这个啥时候走啊");
                                    loadRootFragment(R.id.fl_container, MainFragment.newInstance());
                                }
                                subInviteCode();
                            }
                        }
                    });
        } else {
            if (new SPUtils(SP_NAME).getBoolean(PSW_SWITCH)) {


                loadRootFragment(R.id.fl_container, ShowPSWFragment.newInstance());
            } else if (findFragment(MainFragment.class) == null) {
                //正常模式
                loadRootFragment(R.id.fl_container, MainFragment.newInstance());
            }
        }

        getVersion();

    }


    @Override
    public void onBackPressedSupport() {
        // 对于 4个类别的主Fragment内的回退back逻辑,已经在其onBackPressedSupport里各自处理了
        super.onBackPressedSupport();
    }

    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        // 设置横向(和安卓4.x动画相同)
        return new DefaultHorizontalAnimator();
    }

    /**
     * 初始化状态栏 导航栏 jar
     */
    protected void initImmersionBar() {
        mImmersionBar = ImmersionBar.with(this)
                .transparentStatusBar()
                .navigationBarColor(R.color.transparent)
                .keyboardEnable(true); //解决软键盘与底部输入框冲突问题
        mImmersionBar.init();
    }

    //QQ与新浪不需要添加Activity，但需要在使用QQ分享或者授权的Activity中，添加：
    //注意onActivityResult不可在fragment中实现，如果在fragment中调用登录或分享，需要在fragment依赖的Activity中实现
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    public void regist() {
        IntentFilter filter = new IntentFilter();
        for (DownLoadBean bean : sDownLoadBeans) {
            filter.addAction(bean.getUrl());
        }
        registerReceiver(receiver, filter);
    }

    public void unregist() {
        try {
            unregisterReceiver(receiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != intent) {
                for (DownLoadBean bean : sDownLoadBeans) {
                    if (intent.getAction().equals(bean.getUrl())) {
                        FileInfo firstFileInfo = (FileInfo) intent.getSerializableExtra(DownloadConstant.EXTRA_INTENT_DOWNLOAD);

                        Log.e("cjn", "下载的时候的这个fileinfo" + firstFileInfo.toString());
                        if (firstFileInfo.getDownloadStatus() == DownloadStatus.COMPLETE) {//下载完成 后 立即更新数据库信息
                            mSqlUtils.updataBean(bean.getUrl(), DownloadStatus.COMPLETE);
                        } else if (firstFileInfo.getDownloadStatus() == DownloadStatus.PAUSE) {//下载暂停 后 立即更新数据库信息

                        } else {//其他状态 每5s 更新一次数据库

                        }
                    }
                }
            }
        }
    };
    private DownLoadSqlUtils mSqlUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSqlUtils = new DownLoadSqlUtils();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregist();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    protected void getVersion() {
//        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        final String appVersion = AppUtils.getAppInfo(this).getVersionName();
        Map<String, String> params = new HashMap<String, String>();

        params.put("os", "1");
        params.put("curversion", appVersion);

        new UpdateAppManager
                .Builder()
                //必须设置，当前Activity
                .setActivity(this)
                //必须设置，实现httpManager接口的对象
                .setHttpManager(new UpdateAppHttpUtil())
                //必须设置，更新地址
                .setUpdateUrl(AppConfig.getUrl() + "api/v1/app/getVersion")

                //以下设置，都是可选
                //设置请求方式，默认get
                .setPost(false)
                //添加自定义参数，默认version=1.0.0（app的versionName）；apkKey=唯一表示（在AndroidManifest.xml配置）
                .setParams(params)
                //设置点击升级后，消失对话框，默认点击升级后，对话框显示下载进度
//                .hideDialogOnDownloading()
                //设置头部，不设置显示默认的图片，设置图片后自动识别主色调，然后为按钮，进度条设置颜色
//                .setTopPic(R.mipmap.top_8)
                //为按钮，进度条设置颜色，默认从顶部图片自动识别。
                //.setThemeColor(ColorUtil.getRandomColor())
                //设置apk下砸路径，默认是在下载到sd卡下/Download/1.0.0/test.apk
//                .setTargetPath(path)
                //设置appKey，默认从AndroidManifest.xml获取，如果，使用自定义参数，则此项无效
                //.setAppKey("ab55ce55Ac4bcP408cPb8c1Aaeac179c5f6f")
                //不显示通知栏进度条
                .dismissNotificationProgress()
                //是否忽略版本
                //.showIgnoreVersion()
                .setIgnoreDefParams(true)
                .build()
                //检测是否有新版本
                .checkNewApp(new UpdateCallback() {
                    /**
                     * 解析json,自定义协议
                     *
                     * @param json 服务器返回的json
                     * @return UpdateAppBean
                     */
                    @Override
                    protected UpdateAppBean parseJson(String json) {
                        UpdateAppBean updateAppBean = new UpdateAppBean();
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            JSONObject data = jsonObject.optJSONObject("data");
                            if (data == null){
                                return updateAppBean;
                            }
                            String new_version = data.optString("new_version");
                            String update = "No";
                            try {
                                String[] newVersions = new_version.split("\\.");
                                String[] appVersions = appVersion.split("\\.");
                                int a = Integer.valueOf(newVersions[0]);
                                int b = Integer.valueOf(newVersions[1]);
                                int c = Integer.valueOf(newVersions[2]);
                                int a1 = Integer.valueOf(appVersions[0]);
                                int b1 = Integer.valueOf(appVersions[1]);
                                int c1 = Integer.valueOf(appVersions[2]);
                                if (a > a1) {
                                    update = "Yes";
                                } else if (a == a1) {
                                    if (b > b1) {
                                        update = "Yes";
                                    } else if (b == b1) {
                                        if (c > c1) {
                                            update = "Yes";
                                        }
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            updateAppBean
                                    //（必须）是否更新Yes,No
                                    .setUpdate(update)
                                    //（必须）新版本号，
                                    .setNewVersion(data.optString("new_version"))
                                    //（必须）下载地址
                                    .setApkFileUrl(data.optString("download_url"))
                                    //（必须）更新内容
                                    .setUpdateLog(data.optString("update_content"))
                                    //大小，不设置不显示大小，可以不设置
//                                    .setTargetSize(jsonObject.optString("target_size"))
                                    //是否强制更新，可以不设置
                                    .setConstraint(data.optInt("is_force") == 1)
                                    //设置md5，可以不设置
//                                    .setNewMd5(data.optString("new_md51"))
                                    ;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return updateAppBean;
                    }

                    /**
                     * 网络请求之前
                     */
                    @Override
                    public void onBefore() {
//                        CProgressDialogUtils.showProgressDialog(JavaActivity.this);
                    }

                    /**
                     * 网路请求之后
                     */
                    @Override
                    public void onAfter() {
//                        CProgressDialogUtils.cancelProgressDialog(JavaActivity.this);
                    }

                    /**
                     * 没有新版本
                     */
                    @Override
                    protected void noNewApp(String error) {
                        super.noNewApp(error);
                    }

                });

    }

    private void subInviteCode() {
        try{
            ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData mClipData = cm.getPrimaryClip();
            String code = mClipData.getItemAt(0).getText().toString();
            Log.e("cjn",""+code);

            if (TextUtils.isEmpty(TOKEN)||TextUtils.isEmpty(code)||!code.contains("qgtv=")){
                return;
            }
            Log.e("cjn",""+code.split("qgtv=")[1]);
            Client.getApiService().subInviteCode(TOKEN, code.split("qgtv=")[1])
                    .compose(RxsRxSchedulers.<BaseBean>io_main())
                    .subscribe(new ApiServiceResult(getComposite()) {
                        @Override
                        public void onNext(BaseBean bean) {
                            Log.e("cjn", "看看这的数据" + bean.toString());
                            if (bean != null) {

                            }
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}