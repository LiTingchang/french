package com.snail.french.model.exercise;

import java.util.ArrayList;

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

    public ArrayList<Question> getQuestions() {

        ArrayList<Question> allQuestions = new ArrayList<>();

        if(questions != null) {
            allQuestions.addAll(questions);
        }

        if(G != null) {
            allQuestions.addAll(G);
        }

        if(L != null) {
            allQuestions.addAll(L);
        }

        if(R != null) {
            allQuestions.addAll(R);
        }

        if(C != null) {
            allQuestions.addAll(C);
        }

        if(W != null) {
            allQuestions.addAll(W);
        }

        return allQuestions;
    }
}
