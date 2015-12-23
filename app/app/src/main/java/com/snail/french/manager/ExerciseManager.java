package com.snail.french.manager;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.snail.french.model.exercise.Exerciseresponse;
import com.snail.french.model.exercise.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by litingchang on 15-12-23.
 */
public class ExerciseManager {

    private Map<Integer, Integer> answerMap;

    private Map<String, Exerciseresponse> exerciseresponseMap;

    private Exerciseresponse exerciseresponse;

    private static ExerciseManager exerciseManager;
    private ExerciseManager() {
        answerMap = new HashMap<>();
        exerciseresponseMap = new HashMap<>();
    }

    public static ExerciseManager getInstance() {
        if(exerciseManager == null) {
            synchronized (ExerciseManager.class) {
                if(exerciseManager ==null) {
                    exerciseManager = new ExerciseManager();
                }
            }
        }
        return exerciseManager;
    }

    public void putExerciseresponse(String key, Exerciseresponse exerciseresponse) {
        exerciseresponseMap.put(key, exerciseresponse);
    }

    public void setExerciseresponse(Exerciseresponse exerciseresponse) {
        this.exerciseresponse = exerciseresponse;
    }

    public void addAnswer(int id, int index) {
        answerMap.put(id, index);
    }

    public void clean() {
        exerciseresponseMap.clear();
        answerMap.clear();
        exerciseresponse = null;
    }

    public String getAnswerJsonString() {
        Map<String, List<Map<Integer, Integer>>> container = new HashMap<>();

        List<Map<Integer, Integer>> list = new ArrayList<>();
        for (Question question : exerciseresponse.questions) {
            int id = question.id;
            Map<Integer, Integer> map = new HashMap<>();
            if(answerMap.containsKey(id)) {
                map.put(id, question.content_data.answer_index == answerMap.get(id)
                        ? 1 : 0 );
            } else {
                map.put(id, -1);
            }
            list.add(map);
        }

        container.put("question", list);

        return JSON.toJSONString(container);
//        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(container));
//        return jsonObject.toJSONString();
    }
}
