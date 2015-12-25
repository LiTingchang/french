package com.snail.french.activity;

import android.os.Bundle;

import com.snail.french.R;
import com.snail.french.activity.base.BaseActivity;

import butterknife.ButterKnife;

/**
 * Created by litingchang on 15-12-9.
 */
public class ResultActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);
    }
}
