package com.snail.french.fragment;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.snail.french.activity.TestActivity;
import com.snail.french.constant.FrenchKind;
import com.snail.french.model.status.StatusResponse;
import com.snail.french.net.http.StickerHttpClient;
import com.snail.french.userinfo.UserInfoManager;
import com.snail.french.utils.JsonParseUtil;
import com.snail.french.utils.LogUtil;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by litingchang on 15-12-11.
 */
public class Tem4Fragment extends BaseMainFragment{

    public static Tem4Fragment newInstance() {
        return new Tem4Fragment();
    }

    @Override
    public FrenchKind getKind() {
        return FrenchKind.TEM4;
    }

    @Override
    public void initData() {

        StickerHttpClient.getInstance()
                .addAutorization(UserInfoManager.getAccessToken(getActivity()))
                .get("q/S/exercise_status", null, new AsyncHttpResponseHandler() {
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
                        } catch (Exception e) {
                            e.printStackTrace();
                            jsonObject = null;
                            return;
                        }

                        if (jsonObject == null) {
                            return;
                        }

                        String [] names = {"L", "W",  "G", "R", "C",};
                        String [][] subTypes = {{"40","41","42"},{"43","44","46"}, {"45","47"}, {"50"}, {"60"},};
                        StatusResponse response = JsonParseUtil.parseEResponse(names, subTypes, jsonObject);

                        setData(response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
    }
}
