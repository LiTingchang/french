package com.snail.french.activity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import com.devbrackets.android.exomedia.EMVideoView;
import com.snail.french.FrenchApp;
import com.snail.french.R;
import com.snail.french.activity.base.BaseActivity;
import com.snail.french.utils.StringUtils;
import com.snail.french.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by litingchang on 15-12-9.
 */
public class ExoPlayerActivity extends BaseActivity implements MediaPlayer.OnPreparedListener {

    private static final String EXT_RRL = "ext_url";

    @Bind(R.id.video_play_activity_video_view)
    EMVideoView emVideoView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exoplayer);
        ButterKnife.bind(this);

        emVideoView.setOnPreparedListener(this);

        String url = getIntent().getStringExtra(EXT_RRL);
        if(!StringUtils.isEmpty(url)) {
            emVideoView.setVideoURI(Uri.parse(url));
        } else {
            ToastUtil.shortToast(this, "视频地址错误，暂时无法播放");
            finish();
        }

    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        emVideoView.start();
    }

    public static void launch(Context context, String url) {
        Intent intent = new Intent(context, ExoPlayerActivity.class);
        intent.putExtra(EXT_RRL, url);
        context.startActivity(intent);
    }
}
