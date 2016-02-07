package com.snail.french.fragment;

import android.support.v4.app.Fragment;

import com.alibaba.fastjson.TypeReference;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.snail.french.activity.TestActivity;
import com.snail.french.constant.FrenchKind;
import com.snail.french.model.status.StatusResponse;
import com.snail.french.net.http.StickerHttpClient;
import com.snail.french.net.http.StickerHttpResponseHandler;
import com.snail.french.userinfo.UserInfoManager;
import com.snail.french.utils.JsonParseUtil;
import com.snail.french.utils.LogUtil;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by litingchang on 15-12-11.
 */
public class TcfFragment extends BaseMainFragment{

    public static  TcfFragment newInstance() {
        return new TcfFragment();
    }

    @Override
    public FrenchKind getKind() {
        return FrenchKind.TCF;
    }

    @Override
    public void initData() {

        StickerHttpClient.getInstance()
                .addAutorization(UserInfoManager.getAccessToken(getActivity()))
                .get("q/C/exercise_status", null, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        String responseString;

                        try {
                            responseString = new String(responseBody, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            return;
                        } catch (NullPointerException e) {
                            return;
                        }

                        LogUtil.d("TcfFragment", responseString);

                        JSONObject jsonObject = null;

                        try {
                            jsonObject = new JSONObject(responseString);
                        }catch (Exception e) {
                            e.printStackTrace();
                            jsonObject = null;
                            return;
                        }

                        if(jsonObject == null) {
                            return;
                        }

                        String [] names = {"L", "G", "R"};
                        String [] types = {"30", "31", "32"};
                        String [][] subTypes = {{"A1","A2","B1","B2","C1","C2"}, {"A1","A2","B1","B2","C1","C2"}, {"A1","A2","B1","B2","C1","C2"}};
                        StatusResponse response = JsonParseUtil.parseCresponse(names, types, subTypes, jsonObject);

                        setData(response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
    }
}
