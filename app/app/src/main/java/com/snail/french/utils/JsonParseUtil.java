package com.snail.french.utils;

import com.snail.french.model.status.StatusResponse;
import com.snail.french.model.status.PItem;
import com.snail.french.model.status.Status;

import org.json.JSONObject;

/**
 * Created by litingchang on 15-12-22.
 *
 //        String str = "{ \"accuracy\": 0, \"exercise_days\": 3, \"exercise_question_number\": 0, \"forcast_score\": 0, \"level\": \"A1\", \"status\": { \"G\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"status\": { \"31\": { \"A1\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 102 }, \"A2\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 114 }, \"B1\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 128 }, \"B2\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 147 }, \"C1\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 126 }, \"C2\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 137 } } }, \"total_quesstion_num\": 754 }, \"L\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"status\": { \"30\": { \"A1\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 65 }, \"A2\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 76 }, \"B1\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 169 }, \"B2\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 186 }, \"C1\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 138 }, \"C2\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 94 } } }, \"total_quesstion_num\": 728 }, \"R\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"status\": { \"32\": { \"A1\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 157 }, \"A2\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 104 }, \"B1\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 169 }, \"B2\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 171 }, \"C1\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 136 }, \"C2\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 107 } } }, \"total_quesstion_num\": 844 } }, \"total_score\": 699 }";
 //        JSONObject jsonObject = null;
 //
 //        try {
 //            jsonObject = new JSONObject(str);
 //        }catch (Exception e) {
 //
 //        }
 //        String [] names = {"G", "L", "R"};
 //        String [] types = {"31", "30", "32"};
 //        String [][] subTypes = {{"A1","A2","B1","B2","C1","C2"}, {"A1","A2","B1","B2","C1","C2"}, {"A1","A2","B1","B2","C1","C2"}};
 //        CResponse response = JsonParseUtil.parseCresponse(names, types, subTypes, jsonObject);
 //


 //        String EStr = "{ \"accuracy\": 0, \"exercise_days\": 3, \"exercise_question_number\": 0, \"forcast_score\": 0, \"level\": \"A1\", \"status\": { \"G\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"status\": { \"9\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 182 }, \"10\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 30 }, \"17\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 30 }, \"18\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 99 } }, \"total_quesstion_num\": 341 }, \"L\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"status\": { \"1\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 65 }, \"2\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 237 }, \"3\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 120 }, \"4\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 23 }, \"5\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 76 }, \"6\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 42 }, \"7\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 40 } }, \"total_quesstion_num\": 603 }, \"R\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"status\": { \"11\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 170 }, \"12\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 188 }, \"13\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 36 }, \"14\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 35 }, \"15\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 51 }, \"16\": { \"correct_num\": 0, \"exercise_question_number\": 0, \"total_quesstion_num\": 26 } }, \"total_quesstion_num\": 506 } }, \"total_score\": 900 }";
 //                JSONObject jsonObject = null;
 //
 //        try {
 //            jsonObject = new JSONObject(EStr);
 //        }catch (Exception e) {
 //
 //        }
 //        String [] names = {"G", "L", "R"};
 //        String [][] subTypes = {{"9","10","17","18"}, {"1","2","3","4","5","6","7"}, {"11","12","13","14","15","16"}};
 //        CResponse response = JsonParseUtil.parseEResponse(names, subTypes, jsonObject);

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
 //        CResponse response = JsonParseUtil.parseEResponse(names, subTypes, jsonObject);
 */
public class JsonParseUtil {

    public static Status parseStatus(String type, String subType,
                                     JSONObject jsonObject) {
        Status status = new Status();
        status.type = type;
        status.subType = subType;
        status.correct_num = jsonObject.optInt("correct_num");
        status.exercise_question_number = jsonObject.optInt("exercise_question_number");
        status.total_quesstion_num = jsonObject.optInt("total_quesstion_num");

        return status;
    }

    // C
    public static PItem parsePItem(String name, String type, String[] subType,
                                   JSONObject jsonObject) {
        PItem pItem = new PItem();
        pItem.name = name;

        pItem.correct_num = jsonObject.optInt("correct_num");
        pItem.exercise_question_number = jsonObject.optInt("exercise_question_number");
        pItem.total_quesstion_num = jsonObject.optInt("total_quesstion_num");

        JSONObject jsonObject1 = jsonObject.optJSONObject("status");

        if (jsonObject1 != null) {

            JSONObject jsonObject2 = jsonObject1.optJSONObject(type);

            if (jsonObject2 != null) {
                for (String sb : subType) {
                    JSONObject jsonObject3 = jsonObject2.optJSONObject(sb);

                    if (jsonObject3 != null) {
                        Status status = parseStatus(type, sb, jsonObject3);
                        pItem.statusList.add(status);
                    }
                }
            }
        }
        return pItem;
    }

    public static StatusResponse parseCresponse(String[] pNames, String[] pTypes, String[][] pSubTypes,
                                           JSONObject jsonObject) {
        StatusResponse cResponse = new StatusResponse();
        cResponse.accuracy = jsonObject.optInt("accuracy");
        cResponse.exercise_days = jsonObject.optInt("exercise_days");
        cResponse.exercise_question_number = jsonObject.optInt("exercise_question_number");
        cResponse.forcast_score = jsonObject.optInt("forcast_score");
        cResponse.level = jsonObject.optString("level");
        cResponse.total_score = jsonObject.optInt("total_score");

        JSONObject jsonObject1 = jsonObject.optJSONObject("status");

        int i = 0;
        if (jsonObject1 != null) {
            for (String pName : pNames) {
                JSONObject jsonObject2 = jsonObject1.optJSONObject(pName);

                if (jsonObject2 != null) {
                    PItem item = parsePItem(pName, pTypes[i], pSubTypes[i], jsonObject2);
                    cResponse.pItemList.add(item);
                }
                ++i;
            }
        }

        return cResponse;
    }


    // E S
    public static PItem parsePItem2(String name, String[] types,
                                    JSONObject jsonObject) {
        PItem pItem = new PItem();
        pItem.name = name;

        pItem.correct_num = jsonObject.optInt("correct_num");
        pItem.exercise_question_number = jsonObject.optInt("exercise_question_number");
        pItem.total_quesstion_num = jsonObject.optInt("total_quesstion_num");

        JSONObject jsonObject1 = jsonObject.optJSONObject("status");

        if (jsonObject1 != null) {

            for (String tp : types) {
                JSONObject jsonObject2 = jsonObject1.optJSONObject(tp);

                if (jsonObject2 != null) {
                    Status status = parseStatus(tp, "", jsonObject2);
                    pItem.statusList.add(status);
                }
            }

        }
        return pItem;
    }


    public static StatusResponse parseEResponse(String[] pNames, String[][] pTypes,
                                           JSONObject jsonObject) {
        StatusResponse cResponse = new StatusResponse();
        cResponse.accuracy = jsonObject.optInt("accuracy");
        cResponse.exercise_days = jsonObject.optInt("exercise_days");
        cResponse.exercise_question_number = jsonObject.optInt("exercise_question_number");
        cResponse.forcast_score = jsonObject.optInt("forcast_score");
        cResponse.level = jsonObject.optString("level");
        cResponse.total_score = jsonObject.optInt("total_score");

        JSONObject jsonObject1 = jsonObject.optJSONObject("status");

        int i = 0;
        if (jsonObject1 != null) {
            for (String pName : pNames) {
                JSONObject jsonObject2 = jsonObject1.optJSONObject(pName);

                if (jsonObject2 != null) {
                    PItem item = parsePItem2(pName, pTypes[i], jsonObject2);
                    cResponse.pItemList.add(item);
                }
                ++i;
            }
        }

        return cResponse;
    }
}
