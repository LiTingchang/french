package com.snail.french.manager;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.snail.french.constant.FrenchKind;
import com.snail.french.model.exercise.Exerciseresponse;
import com.snail.french.model.exercise.Question;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Created by litingchang on 15-12-23.
 */
public class ExerciseManager {

    private FrenchKind frenchKind;
    private String path = "";
    private String title = "";
    private String exerciseName;

    private boolean needSubmit;

    private Exerciseresponse exerciseresponse;
    private Map<String, Exerciseresponse> exerciseresponseMap;
    private Map<Integer, Integer> answerMap;

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

    public FrenchKind getFrenchKind() {
        return frenchKind;
    }

    public void setFrenchKind(FrenchKind frenchKind) {
        this.frenchKind = frenchKind;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setExerciseresponse(Exerciseresponse exerciseresponse) {
        this.exerciseresponse = exerciseresponse;
    }

    public Exerciseresponse getExerciseresponse() {
        return exerciseresponse;
    }

    public void addAnswer(int id, int index) {
        answerMap.put(id, index);
    }

    public Map<Integer, Integer> getAnswerMap() {
        return answerMap;
    }

    public void clean() {
        frenchKind = FrenchKind.TCF;
        path = "";
        title = "";
        exerciseresponseMap.clear();
        answerMap.clear();
        exerciseresponse = null;
    }

    public String getAnswerJsonString() {
        Map<String, List<Map<Integer, Integer>>> container = new HashMap<>();

        // questions
        if(exerciseresponse.questions != null) {
            container.put("questions", getAnswerList(exerciseresponse.questions));
            return objToString(container);
        }

        // G L R C W
        container.put("G", getAnswerList(exerciseresponse.G));
        container.put("L", getAnswerList(exerciseresponse.L));
        container.put("R", getAnswerList(exerciseresponse.R));
        container.put("C", getAnswerList(exerciseresponse.C));
        container.put("W", getAnswerList(exerciseresponse.W));

        Map<String, Object> result = new HashMap<>();
        result.put("questions", container);

        return objToString(result);
    }

    private String objToString(Object container) {
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(container));
        return jsonObject.toJSONString();
    }

    private List<Map<Integer, Integer>> getAnswerList(ArrayList<Question> questions) {

        List<Map<Integer, Integer>> list = new ArrayList<>();

        if(questions != null) {
            for (Question question : questions) {
                if(question.sub_questions != null && !question.sub_questions.isEmpty()) {
                    for (Question question1 : question.sub_questions) {
                        fitAnswerList(list, question);
                    }
                } else {
                    fitAnswerList(list, question);
                }
            }
        }

        return list;
    }

    private void fitAnswerList(List<Map<Integer, Integer>> list, Question question) {
        int id = question.id;
        Map<Integer, Integer> map = new HashMap<>();
        if(answerMap.containsKey(id)) {
            map.put(id, question.content_data.answer_index - 1 == answerMap.get(id)
                    ? 1 : 0 );
        } else {
            map.put(id, -1);
        }
        list.add(map);
    }


    public ArrayList<Question> getErrorQuesrions() {
        ArrayList<Question> allQuestions = exerciseresponse.getQuestions();

        if(allQuestions.isEmpty()) {
            return null;
        }

        ArrayList<Question> errorQuestions = new ArrayList<>();

        Map<Integer, Integer> answerMap = getAnswerMap();
        if(answerMap == null || answerMap.isEmpty()) {
            return null;
        }

        for (Question question : allQuestions) {

            if(answerMap.containsKey(question.id)) {
                if(question.content_data.answer_index - 1 != answerMap.get(question.id)) {
                    errorQuestions.add(question);
                }
            }
        }

        return errorQuestions;
    }

    public boolean isNeedSubmit() {
        return needSubmit;
    }

    public void setNeedSubmit(boolean needSubmit) {
        this.needSubmit = needSubmit;
    }
}
