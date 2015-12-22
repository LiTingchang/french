package com.snail.french.model.c;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by litingchang on 15-12-22.
 */
public class CResponse {

    public int accuracy;
    public int exercise_days;
    public int exercise_question_number;
    public int forcast_score;
    public String level;
    public int total_score;
    public List<PItem> pItemList;

    public CResponse() {
        pItemList = new ArrayList<>();
    }
}
