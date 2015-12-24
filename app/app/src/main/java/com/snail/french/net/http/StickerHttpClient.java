package com.snail.french.net.http;

import android.content.Context;
import android.content.Entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.snail.french.utils.LogUtil;
import com.snail.french.utils.StringUtils;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * Created by litingchang on 15-10-5.
 */
public class StickerHttpClient {

    private static final String TAG = StickerHttpClient.class.getSimpleName();

    private static String HOST = "http://woniufr.com/api/";

    private static StickerHttpClient stickerHttpClient;
    private AsyncHttpClient asyncHttpClient;

    private StickerHttpClient() {
        asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(30000);
    }

    public static StickerHttpClient getInstance() {
        if(stickerHttpClient == null) {
            synchronized (StickerHttpClient.class) {
                if (stickerHttpClient == null) {
                    stickerHttpClient = new StickerHttpClient();
                }
            }
        }

        return stickerHttpClient;
    }

    public StickerHttpClient addHeader(String key, String value) {
        asyncHttpClient.addHeader(key, value);
        return stickerHttpClient;
    }

    public StickerHttpClient addAutorization(String accessToken){
        asyncHttpClient.addHeader("HTTP-AUTHORIZATION", accessToken);
        return stickerHttpClient;
    }

    public <T> void get(String action, RequestParams requestParams, Type type,
                               StickerHttpResponseHandler<T> responseHandler) {
        LogUtil.d(TAG, "get -> action:" + action + " \n->requestParams:" + (requestParams != null ? requestParams.toString() : "null"));
        asyncHttpClient.get(HOST + action,
                requestParams, getAsyncHttpResponseHandler(type, responseHandler));
    }

    public <T> void post(String action, RequestParams requestParams, Type type,
                               StickerHttpResponseHandler<T> responseHandler) {
        LogUtil.d(TAG, "post ->action:" + action + " \n->requestParams:" + (requestParams != null ? requestParams.toString() : "null"));
        asyncHttpClient.post(HOST + action,
                requestParams, getAsyncHttpResponseHandler(type, responseHandler));
    }

    public <T> void postJson(Context context, String action, JSONObject jsonParams, Type type,
                         StickerHttpResponseHandler<T> responseHandler) {
        LogUtil.d(TAG, "post ->action:" + action + " \n->requestParams:" + (jsonParams != null ? jsonParams.toString() : "null"));

        if (jsonParams == null) {
            return;
        }

        try {
            StringEntity entity = new StringEntity(jsonParams.toString());
            asyncHttpClient.post(context, HOST + action,
                    entity, "application/json", getAsyncHttpResponseHandler(type, responseHandler));
        } catch (Exception e) {

        }
    }

    public <T> void postJsonString(Context context, String action, String jsonString, Type type,
                             StickerHttpResponseHandler<T> responseHandler) {
        LogUtil.d(TAG, "post ->action:" + action + " \n->requestParams:" + jsonString);

        if (StringUtils.isEmpty(jsonString)) {
            return;
        }

        try {
            StringEntity entity = new StringEntity(jsonString);
            asyncHttpClient.post(context, HOST + action,
                    entity, "application/json", getAsyncHttpResponseHandler(type, responseHandler));
        } catch (Exception e) {

        }
    }


    public void get(String action, RequestParams params, AsyncHttpResponseHandler asyncHttpResponseHandler){
        asyncHttpClient.get(HOST + action,  params, asyncHttpResponseHandler);
    }

//    public <T> void postSync(String action, RequestParams requestParams, Type type,
//                                StickerHttpResponseHandler<T> responseHandler) {
//
//        LogUtil.d(TAG, "post ->action:" + action + " \n->requestParams:" + requestParams.toString());
//
//        HttpClientUtil.getSyncHttpClient().post(HOST + action,
//                requestParams, getAsyncHttpResponseHandler(type, responseHandler));
//    }

    private <T> AsyncHttpResponseHandler getAsyncHttpResponseHandler(final Type type, final StickerHttpResponseHandler<T> responseHandler){
        return new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                responseHandler.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String responseString;
                try {
                    responseString = new String(responseBody, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    LogUtil.e(TAG, "onSuccess -> " + e.toString());
                    responseHandler.onFailure("服务器未响应");
                    return;
                } catch (NullPointerException e) {
                    LogUtil.e(TAG, "onFailure -> " + e.toString());
                    responseHandler.onFailure("服务器异常，请稍候重试");
                    return;
                }

                LogUtil.d(TAG, "onSuccess -> " + "response:" + responseString);

                T responseData = null;
                try {
                    // api 返回的data字段 在请求错误的情况下会返回字符串。
                    responseData = JSON.parseObject(responseString, type);
                } catch (JSONException e) {
                    LogUtil.e(TAG, "onSuccess -> " + e.toString());
                    responseHandler.onFailure("JSON解析错误");
                    return;
                }

                if (responseData == null) {
                    LogUtil.e(TAG, "onSuccess -> responseData is null");
                    responseHandler.onFailure("JSON解析错误");
                    return;
                }

//                if (!responseData.isResult()) {
//                    LogUtil.e(TAG, "onSuccess -> responseData isResult false");
//                    responseHandler.onFailure(responseData.getMessage());
//                    return;
//                }

                responseHandler.onSuccess(responseData);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers,
                                  byte[] responseBody, Throwable error) {

                LogUtil.e(TAG, "onFailure -> statusCode:" + statusCode);

                String responseString;
                try {
                    responseString = new String(responseBody, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    LogUtil.e(TAG, "onFailure -> " + e.toString());
                    responseHandler.onFailure("服务器异常，请稍候重试");
                    return;
                } catch (NullPointerException e) {
                    LogUtil.e(TAG, "onFailure -> " + e.toString());
                    responseHandler.onFailure("服务器异常，请稍候重试");
                    return;
                }

                responseHandler.onFailure("code:" + statusCode + "msg:" + responseString);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                responseHandler.onFinish();
            }
        };
    }
}
