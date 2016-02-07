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
public class TefFragment extends BaseMainFragment{


    public static TefFragment newInstance() {
        return new TefFragment();
    }

    @Override
    public FrenchKind getKind() {
        return FrenchKind.TEF;
    }

    @Override
    public void initData() {

        StickerHttpClient.getInstance()
                .addAutorization(UserInfoManager.getAccessToken(getActivity()))
                .get("q/E/exercise_status", null, new AsyncHttpResponseHandler() {
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

                        String [] names = {"R", "L", "G" };
                        String [][] subTypes = {{"11","12","13","14","15","16"}, {"1","2","3","4","5","6","7"}, {"9","10","17","18"}};
                        StatusResponse response = JsonParseUtil.parseEResponse(names, subTypes, jsonObject);

                        setData(response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
    }
}
