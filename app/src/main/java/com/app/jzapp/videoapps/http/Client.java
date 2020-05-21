package com.app.jzapp.videoapps.http;

import android.text.TextUtils;

import com.app.jzapp.videoapps.base.BaseBean;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * 网络请求配置
 */
public class Client {
    private static final int TOME_OUT = 20;
    public static final int HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 100 * 1024 * 1024;//缓存文件最大值为10Mb
    private String baseUrl;
    private GlobalHttpHandler mHandler;
    private Interceptor[] mInterceptors;
    public File cacheFile;
    private static OkHttpClient client;
    private static Retrofit retrofit;
    private static ApiService apiService;
    public static Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(BaseBean.class, new ResultJsonDeser())
//            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .create();

    public static Builder builder() {
        return new Builder();
    }

    private Client(Builder builder) {
        this.baseUrl = builder.baseUrl;
        this.mHandler = builder.mHandler;
        this.cacheFile = builder.cacheFile;
        this.mInterceptors = builder.interceptors;
        init();
    }

    private void init() {
        client = provideClient(provideCache(provideCacheFile()), provideIntercept());
        retrofit = provideRetrofit(client, baseUrl);
        apiService = retrofit.create(ApiService.class);
    }

    public static ApiService getApiService() {
        return apiService;
    }


    /**
     * 提供缓存地址
     */

    File provideCacheFile() {
        return cacheFile;
    }


    /**
     * @param cache 缓存
     * @return
     * @author: jess
     * @date 8/30/16 1:12 PM
     * @description:提供OkhttpClient
     */
    OkHttpClient provideClient(Cache cache, Interceptor intercept) {
        final OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        return configureClient(okHttpClient, cache, intercept);
    }

    /**
     * @param client
     * @param httpUrl
     * @return
     * @author: jess
     * @date 8/30/16 1:13 PM
     * @description: 提供retrofit
     */
    Retrofit provideRetrofit(OkHttpClient client, String httpUrl) {
        final Retrofit.Builder builder = new Retrofit.Builder();
        return configureRetrofit(builder, client, httpUrl);
    }


    Cache provideCache(File cacheFile) {
        return new Cache(cacheFile, HTTP_RESPONSE_DISK_CACHE_MAX_SIZE);//设置缓存路径和大小
    }

    Interceptor provideIntercept() {
        //        return new CustomerIntercept(mHandler);//打印请求信息的拦截器
        return new RequestIntercept(mHandler);
    }


    /**
     * @param builder
     * @param client
     * @param httpUrl
     * @return
     * @author: jess
     * @date 8/30/16 1:15 PM
     * @description:配置retrofit
     */
    private Retrofit configureRetrofit(Retrofit.Builder builder, OkHttpClient client, String httpUrl) {

        return builder.baseUrl(httpUrl)//域名
                .client(client)//设置okhttp
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//使用rxjava
                .addConverterFactory(GsonConverterFactory.create(gson))//使用Gson
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }

    /**
     * 配置okhttpclient
     *
     * @param okHttpClient
     * @return
     */
    private OkHttpClient configureClient(OkHttpClient.Builder okHttpClient, Cache cache, Interceptor intercept) {
        OkHttpClient.Builder builder = okHttpClient.connectTimeout(TOME_OUT, TimeUnit.SECONDS)
                .readTimeout(TOME_OUT, TimeUnit.SECONDS).cache(cache)//设置缓存
                .addNetworkInterceptor(new StethoInterceptor())
                .addInterceptor(intercept);
        if (mInterceptors != null && mInterceptors.length > 0) {//如果外部提供了interceptor的数组则遍历添加
            for (Interceptor interceptor : mInterceptors) {
                builder.addInterceptor(interceptor);
            }
        }
        return builder.build();
    }


    public static class Builder {

        private String baseUrl;

        private Interceptor[] interceptors;

        private GlobalHttpHandler mHandler;

        private File cacheFile;

        private Builder() {

        }

        public Builder baseUrl(String baseUrl) {
            if (TextUtils.isEmpty(baseUrl))
                throw new IllegalArgumentException("baseUrl can not be empty!");
            this.baseUrl = baseUrl;
            return this;
        }

        //添加拦截器
        public Builder interceptors(Interceptor[] interceptors) {
            this.interceptors = interceptors;
            return this;
        }

        //处理Http网络请求
        public Builder globalHttpHandler(GlobalHttpHandler mHandler) {
            this.mHandler = mHandler;
            return this;
        }

        //缓存文件夹
        public Builder cacheFile(File cacheFile) {
            if (cacheFile == null || !cacheFile.exists())
                try {
                    throw new FileNotFoundException("The cacheFile must be exit!!!");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            this.cacheFile = cacheFile;

            return this;
        }

        public Client build() {
            if (TextUtils.isEmpty(baseUrl)) {
                throw new IllegalArgumentException("baseUrl is Required!");
            }
            return new Client(this);
        }
    }
}
