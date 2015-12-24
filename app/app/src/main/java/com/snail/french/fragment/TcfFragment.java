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
//                String str = "{ \"accuracy\": 0, \"exercise_days\": 3, \"exercise_question_number\": 0, \"forcast_score\": 0, \"level\": \"A1\", \"status\": { \"G\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"status\": { \"31\": { \"A1\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 102 }, \"A2\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 114 }, \"B1\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 128 }, \"B2\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 147 }, \"C1\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 126 }, \"C2\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 137 } } }, \"total_quesstion_num\": 754 }, \"L\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"status\": { \"30\": { \"A1\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 65 }, \"A2\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 76 }, \"B1\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 169 }, \"B2\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 186 }, \"C1\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 138 }, \"C2\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 94 } } }, \"total_quesstion_num\": 728 }, \"R\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"status\": { \"32\": { \"A1\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 157 }, \"A2\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 104 }, \"B1\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 169 }, \"B2\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 171 }, \"C1\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 136 }, \"C2\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 107 } } }, \"total_quesstion_num\": 844 } }, \"total_score\": 699 }";
//                JSONObject jsonObject = null;
//
//                try {
//                    jsonObject = new JSONObject(str);
//                }catch (Exception e) {
//
//                }
//                String [] names = {"G", "L", "R"};
//                String [] types = {"31", "30", "32"};
//                String [][] subTypes = {{"A1","A2","B1","B2","C1","C2"}, {"A1","A2","B1","B2","C1","C2"}, {"A1","A2","B1","B2","C1","C2"}};
//                StatusResponse response = JsonParseUtil.parseCresponse(names, types, subTypes, jsonObject);
//
//        setData(response);


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

                        String [] names = {"G", "L", "R"};
                        String [] types = {"31", "30", "32"};
                        String [][] subTypes = {{"A1","A2","B1","B2","C1","C2"}, {"A1","A2","B1","B2","C1","C2"}, {"A1","A2","B1","B2","C1","C2"}};
                        StatusResponse response = JsonParseUtil.parseCresponse(names, types, subTypes, jsonObject);

                        setData("C", response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
    }
}
