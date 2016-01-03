package com.snail.french.activity.others;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.snail.french.R;
import com.snail.french.activity.base.BaseActivity;
import com.snail.french.view.CommonTitle;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by litingchang on 15-12-9.
 */
public class WebActivity extends BaseActivity {

    private static final String TITLE = "title";
    private static final String URL = "url";

    @Bind(R.id.titlebar)
    CommonTitle titlebar;
    @Bind(R.id.web_view)
    WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);

        String url = getIntent().getStringExtra(URL);

        webView.setBackgroundColor(Color.WHITE);
        webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        webView.getSettings().setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });

        webView.loadUrl(url);
    }

    public static void launch(Context context, String title, String url) {
        Intent intent = new Intent();
        intent.setClass(context, WebActivity.class);
        intent.putExtra(TITLE, title);
        intent.putExtra(URL, url);
        context.startActivity(intent);

    }
}
