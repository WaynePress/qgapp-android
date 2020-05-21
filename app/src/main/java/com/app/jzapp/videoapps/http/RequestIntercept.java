package com.app.jzapp.videoapps.http;

import android.util.Log;

import com.app.jzapp.videoapps.utils.LogUtils;
import com.app.jzapp.videoapps.utils.Md5Util;
import com.app.jzapp.videoapps.utils.ZipHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by Administrator on 2017/12/21.
 */

public class RequestIntercept implements Interceptor {

    private GlobalHttpHandler mHandler;


    public RequestIntercept(GlobalHttpHandler mHandler) {
        this.mHandler = mHandler;
    }

    private static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }



    @Override
    public Response intercept(Chain chain) throws IOException {
        String timestamp = "";
        StringBuilder s = new StringBuilder();

        Request request = chain.request();
        Request.Builder requestBuilder = request.newBuilder();

        Buffer requestbuffer = new Buffer();
        if (request.body() != null) {
            request.body().writeTo(requestbuffer);
        } else {
        }
        Log.e("cjn", "请求路径:    " + request.url().toString());
//        String timestamp = System.currentTimeMillis() + "";
        String timestampasd = System.currentTimeMillis() + "";

        s.append(timestampasd);
        s.deleteCharAt(s.length() - 1);
        s.deleteCharAt(s.length() - 1);
        s.deleteCharAt(s.length() - 1);
        timestamp = s.toString();
        String randomstr = (int) ((Math.random() * 9 + 1) * 100000) + "";
        if (!request.url().toString().contains("getUserTokenByRandomnum")) {
            String method = request.method();
            if ("POST".equals(method)) {
                FormBody.Builder formBodyBuilder = new FormBody.Builder();

                formBodyBuilder.add("randomstr", randomstr);
                formBodyBuilder.add("timestamp", timestamp.toString());
                formBodyBuilder.add("signature", Md5Util.getMD5(randomstr + timestamp.toString() + AppConfig.KEY).toLowerCase());

                RequestBody formBody = formBodyBuilder.build();
                String postBodyString = bodyToString(request.body());
                postBodyString += ((postBodyString.length() > 0) ? "&" : "") + bodyToString(formBody);
                Log.e("cjn", "requestBody:    " + postBodyString.toString());

                requestBuilder.post(RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"), postBodyString));
                request = requestBuilder.build();


                StringBuilder sb = new StringBuilder();
                if (request.body() instanceof FormBody) {
                    FormBody body = (FormBody) request.body();
                    for (int i = 0; i < body.size(); i++) {
                        sb.append(body.encodedName(i) + "=" + body.encodedValue(i) + ",");
                    }
                    sb.delete(sb.length() - 1, sb.length());
                    Log.e("post请求", "| RequestParams:{" + sb.toString() + "}");
                    Log.e("cjn", "postBodyString:    " + postBodyString.toString());
                }
            } else if ("GET".equals(method)) {
                Map<String, String> queryParamsMap = new HashMap<>();

                queryParamsMap.put("randomstr", randomstr);
                queryParamsMap.put("timestamp", timestamp.toString());
                queryParamsMap.put("signature", Md5Util.getMD5(randomstr + timestamp.toString() + AppConfig.KEY).toLowerCase());

                request = injectParamsIntoUrl(request.url().newBuilder(), requestBuilder, queryParamsMap);
            }
        }
//        //网络未连接
//        if (!NetworkUtils.isConnected()){
//            throw new NetWorkNoConnectedError( );
//        }

        if (mHandler != null)//在请求服务器之前可以拿到request,做一些操作比如给request添加header,如果不做操作则返回参数中的request
            request = mHandler.onHttpRequestBefore(chain, request);

        long t1 = System.nanoTime();
        Response originalResponse = chain.proceed(request);
        long t2 = System.nanoTime();

        Log.e("网络请求时间：", String.valueOf(((t2 - t1) / 1e6d) / 1000) + "秒");
        //读取服务器返回的结果
        ResponseBody responseBody = originalResponse.body();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();

        //获取content的压缩类型
        String encoding = originalResponse.headers().get("Content-Encoding");

        Buffer clone = buffer.clone();
        String bodyString;

        //解析response content
        if (encoding != null && encoding.equalsIgnoreCase("gzip")) {//content使用gzip压缩
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            clone.writeTo(outputStream);
            byte[] bytes = outputStream.toByteArray();
            bodyString = ZipHelper.decompressForGzip(bytes);//解压
            outputStream.close();
        } else if (encoding != null && encoding.equalsIgnoreCase("zlib")) {//content使用zlib压缩
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            clone.writeTo(outputStream);
            byte[] bytes = outputStream.toByteArray();
            bodyString = ZipHelper.decompressToStringForZlib(bytes);//解压
            outputStream.close();
        } else {//content没有被压缩
            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                charset = contentType.charset(charset);
            }
            bodyString = clone.readString(charset);
        }
//        Log.e("bodyString", bodyString);
//        Timber.tag("Result").d(bodyString);

        LogUtils.e("cjn", "请求结果：    " + bodyString);


//        // 如果images返回的为""，则替换为[]
//        if(bodyString != null && bodyString.contains("\"images\":\"\"")){
//            bodyString = bodyString.replaceAll("\"images\":\"\"", "\"images\":[]");
//            Log.i("去引号后 ", bodyString);
//            originalResponse = originalResponse.newBuilder()
//                    .body(ResponseBody.create(originalResponse.body().contentType(), bodyString))
//                    .header("Content-Encoding", "UTF-8")
//                    .build();
//        }
        if (mHandler != null)//这里可以比客户端提前一步拿到服务器返回的结果,可以做一些操作,比如token超时,重新获取
            return mHandler.onHttpResultResponse(bodyString, chain, originalResponse);

        return originalResponse;
    }

    private Request injectParamsIntoUrl(HttpUrl.Builder httpUrlBuilder, Request.Builder requestBuilder, Map<String, String> paramsMap) {
        if (paramsMap.size() > 0) {
            Iterator iterator = paramsMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                httpUrlBuilder.addQueryParameter((String) entry.getKey(), (String) entry.getValue());
            }
            requestBuilder.url(httpUrlBuilder.build());
            return requestBuilder.build();
        }
        return null;
    }
}
