package com.snail.french.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.snail.french.FrenchApp;
import com.snail.french.R;
import com.snail.french.activity.base.BaseActivity;
import com.snail.french.userinfo.UserInfoManager;

import butterknife.Bind;
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

    public static void launch(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

}
