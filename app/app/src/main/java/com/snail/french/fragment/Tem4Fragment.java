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
public class Tem4Fragment extends BaseMainFragment{

    public static Tem4Fragment newInstance() {
        return new Tem4Fragment();
    }

    @Override
    public void initData() {
//        String SStr = "{ \"accuracy\": 0, \"exercise_days\": 3, \"exercise_question_number\": 0, \"forcast_score\": 0, \"level\": \"\", \"status\": { \"C\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"status\": { \"60\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 9 } }, \"total_quesstion_num\": 9 }, \"G\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"status\": { \"45\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 437 }, \"47\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 210 } }, \"total_quesstion_num\": 647 }, \"L\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"status\": { \"40\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 10 }, \"41\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 100 }, \"42\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 110 } }, \"total_quesstion_num\": 220 }, \"R\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"status\": { \"50\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 264 } }, \"total_quesstion_num\": 264 }, \"W\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"status\": { \"43\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 50 }, \"44\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 50 }, \"46\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 110 } }, \"total_quesstion_num\": 210 } }, \"total_score\": 100 }";
//        JSONObject jsonObject = null;
//
//        try {
//            jsonObject = new JSONObject(SStr);
//        }catch (Exception e) {
//
//        }
//        String [] names = {"C", "G", "L", "R", "W"};
//        String [][] subTypes = {{"60"}, {"45","47"}, {"40","41","42"}, {"50"}, {"43","44","46"}};
//        StatusResponse response = JsonParseUtil.parseEResponse(names, subTypes, jsonObject);
//
//        setData(response);


        StickerHttpClient.getInstance()
                .addHeader("HTTP-AUTHORIZATION", "f0d10a1ca71a11e5a899525400587ef4")
                .get("q/S/exercise_status", new AsyncHttpResponseHandler() {
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

                        String [] names = {"C", "G", "L", "R", "W"};
                        String [][] subTypes = {{"60"}, {"45","47"}, {"40","41","42"}, {"50"}, {"43","44","46"}};
                        StatusResponse response = JsonParseUtil.parseEResponse(names, subTypes, jsonObject);

                        setData(response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
    }
}
