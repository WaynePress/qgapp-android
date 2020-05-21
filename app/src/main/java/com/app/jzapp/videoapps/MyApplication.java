package com.app.jzapp.videoapps;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.app.jzapp.videoapps.database.DaoMaster;
import com.app.jzapp.videoapps.database.DaoSession;
import com.app.jzapp.videoapps.database.DownLoadBeanDao;
import com.app.jzapp.videoapps.http.AppConfig;
import com.app.jzapp.videoapps.http.Client;
import com.app.jzapp.videoapps.http.GlobalHttpHandler;
import com.app.jzapp.videoapps.utils.AppUtils;
import com.app.jzapp.videoapps.utils.DownLoadSqlUtils;
import com.app.jzapp.videoapps.utils.FileUtil;
import com.app.jzapp.videoapps.utils.SPUtils;
import com.app.jzapp.videoapps.utils.Utils;
import com.app.video.videoapps.BuildConfig;
import com.app.jzapp.videoapps.bean.DownLoadBean;
import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.umeng.commonsdk.UMConfigure;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import com.crashlytics.android.answers.Answers;
import io.fabric.sdk.android.Fabric;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class MyApplication extends Application {
    private static Context context;
    private static MyApplication application;

    public static DownLoadBeanDao sDownLoadBeanDao;
    public static boolean lx = false;

    public static int  biao_chao =0;
    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        Utils.init(this);

        RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                //异常处理
                throwable.printStackTrace();
                Log.e("cjn","accept error");
            }
        });
        context = getApplicationContext();
        application = this;
        initClient(AppConfig.BASEURL);

        AppConfig.PHONE = new SPUtils(AppConfig.SP_NAME).getString(AppConfig.SP_PHONE);
        AppConfig.USERID = new SPUtils(AppConfig.SP_NAME).getString(AppConfig.SP_USERID);
        AppConfig.TOKEN = new SPUtils(AppConfig.SP_NAME).getString(AppConfig.SP_TOKEN);

        if (TextUtils.equals(BuildConfig.BUILD_TYPE, "debug"))
            Stetho.initializeWithDefaults(this);


        initUMeng();
        initPush();
//        initMQ();
        initGreenDao();
//        final Fabric fabric = new Fabric.Builder(this)
//                .kits(new Crashlytics())
//                .debuggable(true)
//                .build();
//        Fabric.with(fabric);
        Fabric.with(this, new Crashlytics());
        Fabric.with(this, new Answers());

        Log.i("cjn", getSHA1Signature(context));
        Log.i("cjn", AppUtils.getAppSignatureSHA1(context));

    }



    public String getSHA1Signature(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);

            byte[] cert = info.signatures[0].toByteArray();

            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            return hexString.toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 初始化GreenDao,直接在Application中进行初始化操作
     */
    private void initGreenDao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "download.db");
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        sDownLoadBeanDao = daoSession.getDownLoadBeanDao();

        AppConfig.sDownLoadBeans = new DownLoadSqlUtils().getBeans();
        for (DownLoadBean bean : AppConfig.sDownLoadBeans) {
            if (bean.getStatus() != AppConfig.COMPLETE) {
                bean.setStatus(AppConfig.PAUSE);
            }
        }
    }

    public DownLoadBeanDao getDownLoadBeanDao() {
        return sDownLoadBeanDao;
    }

    /**
     * 美恰
     */
    private void initMQ() {
//        MQConfig.init(this, MQ, new OnInitCallback() {
//            @Override
//            public void onSuccess(String clientId) {
//            }
//
//            @Override
//            public void onFailure(int code, String message) {
//                Toast.makeText(context, "int MQ failure", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void initUMeng() {
        UMConfigure.init(this, AppConfig.UMENGT_KEY, "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");//58edcfeb310c93091c000be2 5965ee00734be40b580001a0
//        UMConfigure.setLogEnabled(true);
//        PlatformConfig.setWeixin(AppConfig.WX_ID, AppConfig.WX_SECERT);
//        PlatformConfig.setQQZone(AppConfig.QQ_ID, AppConfig.QQ_SECERT);
    }

    private void initPush() {
//        PushUtil.getInstance().initJpush(this);
    }

    public static Context getContext() {
        return context;
    }

    public static MyApplication getApplication() {
        return application;
    }

    /**
     * 创建网络请求
     */
    public void initClient(String baseUrl) {
        new SPUtils(AppConfig.SP_NAME).putString(AppConfig.SP_BASE_URL,baseUrl);
        Client.builder()
                .baseUrl(baseUrl)
                .cacheFile(FileUtil.getCacheFile(this))
                .globalHttpHandler(getHandler())
                .interceptors(getInterceptors())
                .build();
    }

    private Interceptor[] getInterceptors() {
        return null;
    }

    private GlobalHttpHandler getHandler() {
        return new GlobalHttpHandler() {
            @Override
            public Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, Response response) {
                Request request = chain.request();
                HttpUrl url = request.url();
//                if (response.code() == 502 || response.code() == 404) {
//                    Intent intent = new Intent();
//                    intent.setAction(context.getPackageName() + ".net");
//                    //发送广播
//                    context.sendBroadcast(intent);
//                } else if (response.code() == 504) {
//                    String msg = MyApplication.getContext().getString(R.string.error_net_504);
//                    ToastUtils.showShortToastSafe(msg);
//                }
                return response;
            }

            @Override
            public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
                //获取请求request
                Request build = null;
                try {
                    build = request.newBuilder()
//                            .addHeader("Content-Type", "application/json;charset=utf-8")
//                            .addHeader("Cookie", Cookie)
//                            .addHeader("Qsc-Token", MyApplication.token==null?"":MyApplication.token)
                            .url(URLDecoder.decode(request.url().url().toString(), "utf-8"))
                            .build();

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return build;
            }
        };
    }
}
