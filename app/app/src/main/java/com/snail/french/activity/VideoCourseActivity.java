package com.snail.french.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.snail.french.R;
import com.snail.french.activity.base.BaseActivity;
import com.snail.french.fragment.AllCourseFragment;
import com.snail.french.fragment.BaseCourseFragment;
import com.snail.french.fragment.PersonalCourseFragment;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by litingchang on 16-2-1.
 */
public class VideoCourseActivity extends BaseActivity {

    @Bind(R.id.courser_table_layout)
    RadioGroup courserTableLayout;

    private static final int TAB_ID_ALL = 0;
    private static final int TAB_ID_MINE = 1;
    private int currentTabID;

    AllCourseFragment allCourseFragment;
    PersonalCourseFragment personalCourseFragment;

    private Map<Integer, BaseCourseFragment> fragmentMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courser);
        ButterKnife.bind(this);

        allCourseFragment = new AllCourseFragment();
        personalCourseFragment = new PersonalCourseFragment();

        fragmentMap.put(TAB_ID_ALL, allCourseFragment);
        fragmentMap.put(TAB_ID_MINE, personalCourseFragment);

        currentTabID = TAB_ID_ALL;
        selectFragment(TAB_ID_ALL);


        courserTableLayout.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {
                    case R.id.courser_all_tab:
                        selectFragment(TAB_ID_ALL);
                        break;
                    case R.id.courser_mine_tab:
                        selectFragment(TAB_ID_MINE);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void selectFragment(int id) {

        FragmentTransaction fragmentTransaction = VideoCourseActivity.this.getSupportFragmentManager().beginTransaction();
        fragmentMap.get(currentTabID).onPause();
        Fragment fragment = fragmentMap.get(id);
        if (fragment.isAdded()) {
            fragment.onResume();
        } else {
            fragmentTransaction.add(R.id.content_courser, fragment);
        }
        for (Map.Entry entry : fragmentMap.entrySet()) {
            Fragment iteratorFragment = (Fragment) entry.getValue();
            if (entry.getKey().equals(id)) {
                fragmentTransaction.show(iteratorFragment);
            } else {
                fragmentTransaction.hide(iteratorFragment);
            }
        }
        fragmentTransaction.commit();
        currentTabID = id;
    }

    public static void launch(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, VideoCourseActivity.class);
        context.startActivity(intent);

    }
}
