package com.snail.french.model.exercise;


import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by litingchang on 15-12-23.
 */
public class Exerciseresponse {
    public ArrayList<Question> questions;
    public ArrayList<Question> G;
    public ArrayList<Question> L;
    public ArrayList<Question> R;
    public ArrayList<Question> C;
    public ArrayList<Question> W;

    public List<Pair<String, ArrayList<Question>>> getQuestionsPairList() {
        List<Pair<String, ArrayList<Question>>> lists = new ArrayList<>();

        if(questions != null && !questions.isEmpty()) {
            lists.add(Pair.create("", questions));
        }

        if(G != null && !G.isEmpty()) {
            lists.add(Pair.create("语法", G));
        }

        if(L != null && !L.isEmpty()) {
            lists.add(Pair.create("听力", L));
        }

        if(R != null && !R.isEmpty()) {
            lists.add(Pair.create("阅读", R));
        }

        if(C != null && !C.isEmpty()) {
            lists.add(Pair.create("作文", C));
        }

        if(W != null && !W.isEmpty()) {
            lists.add(Pair.create("词汇", W));
        }

        return lists;
    }

    public ArrayList<Question> getQuestions() {

        ArrayList<Question> allQuestions = new ArrayList<>();

        if(questions != null) {
            allQuestions.addAll(expandQuestions(questions));
        }

        if(G != null) {
            allQuestions.addAll(expandQuestions(G));
        }

        if(L != null) {
            allQuestions.addAll(expandQuestions(L));
        }

        if(R != null) {
            allQuestions.addAll(expandQuestions(R));
        }

        if(C != null) {
            allQuestions.addAll(expandQuestions(C));
        }

        if(W != null) {
            allQuestions.addAll(expandQuestions(W));
        }

        return allQuestions;
    }

    private List<Question> expandQuestions(ArrayList<Question> questions) {
        ArrayList<Question> expandQuestions = new ArrayList<>();

        if(questions != null) {
            for (Question question : questions) {
                if (question.sub_questions == null) {
                    expandQuestions.add(question);
                } else {
                    for (Question subQuestion : question.sub_questions) {
                        subQuestion.content_data.title = question.content_data.title;
                        subQuestion.url = question.url;
                        expandQuestions.add(subQuestion);
                    }
                }
            }
        }

        return expandQuestions;
    }
}
