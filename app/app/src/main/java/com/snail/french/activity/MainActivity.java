package com.snail.french.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.snail.french.R;
import com.snail.french.activity.base.BaseActivity;
import com.snail.french.view.CommonTitle;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    @Bind(R.id.titlebar)
    CommonTitle titlebar;
    @Bind(R.id.content_main)
    FrameLayout contentMain;
    @Bind(R.id.drawer_tcf)
    TextView drawerTcf;
    @Bind(R.id.drawer_tef)
    TextView drawerTef;
    @Bind(R.id.drawer_tem_4)
    TextView drawerTem4;
    @Bind(R.id.drawer_guide)
    ImageView drawerGuide;
    @Bind(R.id.drawer_personal)
    ImageView drawerPersonal;


    private Map<Integer, Fragment> fragmentMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.drawer_tcf)
    void tcfSelected() {

    }

    @OnClick(R.id.drawer_tef)
    void tefSelected() {

    }

    @OnClick(R.id.drawer_tem_4)
    void tem4Selected() {

    }

    @OnClick(R.id.drawer_guide)
    void guideClicked() {

    }
    @OnClick(R.id.drawer_personal)
    void personalClicked() {

    }

}
