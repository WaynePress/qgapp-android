package com.app.jzapp.videoapps.http;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 全局网络请求处理
 * Created by Administrator on 2017/12/21.
 */

public interface GlobalHttpHandler {

    /**
     * 在客户端之前拿到网络请求
     * @param httpResult
     * @param chain
     * @param response
     * @return
     */
    Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, Response response);

    /**
     * 在网络请求之前进行一些处理
     * @param chain
     * @param request
     * @return
     */
    Request onHttpRequestBefore(Interceptor.Chain chain, Request request);
}
