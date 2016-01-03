package com.snail.french.activity.others;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.snail.french.FrenchApp;
import com.snail.french.R;
import com.snail.french.activity.login.LoginActivity;
import com.snail.french.activity.base.BaseActivity;
import com.snail.french.userinfo.UserInfoManager;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by litingchang on 15-12-9.
 */
public class SettingActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.logout)
    void onLogoutClicked() {
        UserInfoManager.logout(SettingActivity.this);
        FrenchApp.getApp().cleanActivityStack();
        LoginActivity.launch(SettingActivity.this);
    }

    @OnClick(R.id.feedback)
    void onFeedBackClicked() {
        FeedbackActivity.launch(SettingActivity.this);
    }

    @OnClick(R.id.about)
    void onAboutClicked() {
        AboutActivity.launch(SettingActivity.this);
    }

    public static void launch(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

}
