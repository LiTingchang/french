package com.snail.french.model.exercise;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by litingchang on 15-12-23.
 *
 *
 */
public class Question {
    public int id;
    public String gid;
    public String source;
    public String url;
    public ContentData content_data;
    public Meta meta;
    public ArrayList<Question> sub_questions;
}
