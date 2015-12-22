package com.snail.french.model.status;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by litingchang on 15-12-22.
 */
public class StatusResponse {

    public int accuracy;
    public int exercise_days;
    public int exercise_question_number;
    public int forcast_score;
    public String level;
    public int total_score;
    public List<PItem> pItemList;

    public StatusResponse() {
        pItemList = new ArrayList<>();
    }
}
