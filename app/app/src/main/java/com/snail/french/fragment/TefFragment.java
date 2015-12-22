package com.snail.french.fragment;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.snail.french.model.status.StatusResponse;
import com.snail.french.net.http.StickerHttpClient;
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
    public void initData() {

//                String EStr = "{ \"accuracy\": 0, \"exercise_days\": 3, \"exercise_question_number\": 0, \"forcast_score\": 0, \"level\": \"A1\", \"status\": { \"G\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"status\": { \"9\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 182 }, \"10\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 30 }, \"17\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 30 }, \"18\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 99 } }, \"total_quesstion_num\": 341 }, \"L\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"status\": { \"1\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 65 }, \"2\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 237 }, \"3\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 120 }, \"4\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 23 }, \"5\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 76 }, \"6\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 42 }, \"7\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 40 } }, \"total_quesstion_num\": 603 }, \"R\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"status\": { \"11\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 170 }, \"12\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 188 }, \"13\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 36 }, \"14\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 35 }, \"15\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 51 }, \"16\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 26 } }, \"total_quesstion_num\": 506 } }, \"total_score\": 900 }";
//                        JSONObject jsonObject = null;
//
//                try {
//                    jsonObject = new JSONObject(EStr);
//                }catch (Exception e) {
//
//                }
//                String [] names = {"G", "L", "R"};
//                String [][] subTypes = {{"9","10","17","18"}, {"1","2","3","4","5","6","7"}, {"11","12","13","14","15","16"}};
//                StatusResponse response = JsonParseUtil.parseEResponse(names, subTypes, jsonObject);
//        setData(response);

        StickerHttpClient.getInstance()
                .addHeader("HTTP-AUTHORIZATION", "f0d10a1ca71a11e5a899525400587ef4")
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

                        String [] names = {"G", "L", "R"};
                        String [][] subTypes = {{"9","10","17","18"}, {"1","2","3","4","5","6","7"}, {"11","12","13","14","15","16"}};
                        StatusResponse response = JsonParseUtil.parseEResponse(names, subTypes, jsonObject);

                        setData("E", response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
    }
}
