package com.snail.french.fragment;

/**
 * Created by litingchang on 16-2-2.
 */
public class PersonalCourseFragment extends BaseCourseFragment {


    @Override
    String getType() {
        return "PersonalCourseFragment";
    }

    @Override
    String getRequestAction() {
        return "course/mine";
    }
}
