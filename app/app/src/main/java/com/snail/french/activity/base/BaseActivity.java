package com.snail.french.activity.base;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.snail.french.FrenchApp;
import com.snail.french.activity.helper.ActivityHelper;
import com.snail.french.activity.impl.IBaseActivity;

/**
 * Created by litingchang on 15-12-7.
 */
public class BaseActivity extends AppCompatActivity implements IBaseActivity{

    private ActivityHelper mActivityHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityHelper = new ActivityHelper(this);
        FrenchApp.getApp().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FrenchApp.getApp().removeActivity(this);
    }

    @Override
    public void alert(String title, String msg, String positive, DialogInterface.OnClickListener positiveListener, String negative, DialogInterface.OnClickListener negativeListener) {
        mActivityHelper.alert(title, msg, positive, positiveListener, negative, negativeListener);
    }

    @Override
    public void alert(String title, String msg, String positive, DialogInterface.OnClickListener positiveListener, String negative, DialogInterface.OnClickListener negativeListener, Boolean isCanceledOnTouchOutside) {
        mActivityHelper.alert(title, msg, positive, positiveListener, negative, negativeListener,
                isCanceledOnTouchOutside);
    }

    @Override
    public void showProgressDialog(String msg) {
        mActivityHelper.showProgressDialog(msg);
    }

    @Override
    public void showProgressDialog(String msg, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        mActivityHelper.showProgressDialog(msg, cancelable, cancelListener);
    }

    @Override
    public void dismissProgressDialog() {
        mActivityHelper.dismissProgressDialog();
    }
}
