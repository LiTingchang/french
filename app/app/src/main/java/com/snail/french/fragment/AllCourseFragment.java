package com.snail.french.fragment;

/**
 * Created by litingchang on 16-2-2.
 */
public class AllCourseFragment extends BaseCourseFragment {


    @Override
    String getType() {
        return "AllCourseFragment";
    }

    @Override
    String getRequestAction() {
        return "course/list";
    }
}
