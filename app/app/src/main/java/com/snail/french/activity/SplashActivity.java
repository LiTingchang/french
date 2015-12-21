package com.snail.french.activity;

import android.app.Activity;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.util.Log;

import com.snail.french.R;
import com.snail.french.activity.base.BaseActivity;
import com.snail.french.userinfo.UserInfoManager;

/**
 * Created by litingchang on 15-12-9.
 */
public class SplashActivity extends BaseActivity {

    CountDownTimer countDownTimer;

    private boolean isStartNext = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        countDownTimer = new CountDownTimer(1000 * 3, 1000) {
            public void onTick(long millisUntilFinished) {
            }
            public void onFinish() {
                doNext();
            }
        };

        Log.e("aaaaaaaaaaa", "oncreate");

    }

    @Override
    protected void onResume() {
        super.onResume();
        countDownTimer.start();

        Log.e("aaaaaaaaaaa", "onresume");

    }

    @Override
    protected void onPause() {
        super.onPause();
        countDownTimer.cancel();

        Log.e("aaaaaaaaaaa", "onpause");
    }

    private void doNext() {

        if(isStartNext) {
            return;
        }

        isStartNext = true;
        if (UserInfoManager.isLogin(SplashActivity.this)) {
            MainActivity.launch(SplashActivity.this);
        } else {
            LoginActivity.launch(SplashActivity.this);
        }
        SplashActivity.this.finish();
    }
}
