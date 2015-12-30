package com.snail.french.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.snail.french.FrenchApp;
import com.snail.french.R;
import com.snail.french.activity.base.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by litingchang on 15-12-9.
 */
public class AboutActivity extends BaseActivity {

    @Bind(R.id.version_name)
    TextView versionName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        versionName.setText("V" + FrenchApp.getApp().getVersionName());
    }

    public static void launch(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }
}
