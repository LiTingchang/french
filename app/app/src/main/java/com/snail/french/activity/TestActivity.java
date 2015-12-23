package com.snail.french.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.snail.french.R;
import com.snail.french.activity.base.BaseActivity;
import com.snail.french.model.exercise.Exerciseresponse;
import com.snail.french.model.exercise.Question;
import com.snail.french.net.http.StickerHttpClient;
import com.snail.french.net.http.StickerHttpResponseHandler;
import com.snail.french.utils.AudioRecorder;
import com.snail.french.utils.StringUtils;
import com.snail.french.view.CommonTitle;

import junit.framework.Test;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by litingchang on 15-12-9.
 */
public class TestActivity extends BaseActivity {

    private static final String PATH = "path";
    private static final String LEVEL = "level";

    @Bind(R.id.titlebar)
    CommonTitle titlebar;
    @Bind(R.id.test_view_pager)
    ViewPager testViewPager;

    private boolean showAnalyzation = false;
    private String path;
    private String level;

    Exerciseresponse exerciseresponse;
    TestPagerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        path = getIntent().getStringExtra(PATH);
        level = getIntent().getStringExtra(LEVEL);

        String action; // q/C/L/30/exercise?q_tcf_level=A1
        if(!StringUtils.isEmpty(level)) {
            action = "q/" + path + "/exercise?q_tcf_level=" + level;
        } else {
            action = "q/" + path + "/exercise";
        }

        StickerHttpClient.getInstance()
                .addHeader("HTTP-AUTHORIZATION", "f0d10a1ca71a11e5a899525400587ef4")
                .get(action, null, new TypeReference<Exerciseresponse>() {
                        }.getType(),
                        new StickerHttpResponseHandler<Exerciseresponse>() {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onSuccess(Exerciseresponse response) {
                                adapter = new TestPagerAdapter(TestActivity.this, response);
                                testViewPager.setAdapter(adapter);
                            }

                            @Override
                            public void onFailure(String message) {

                            }

                            @Override
                            public void onFinish() {

                            }
                        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    //    static class ViewHolder {
//        @Bind(R.id.test_title)
//        TextView title;
//        @Bind(R.id.test_indicator)
//        TextView indicator;
//        @Bind(R.id.test_web_view)
//        WebView webView;
//        @Bind(R.id.test_radio_group)
//        RadioGroup radioGroup;
//
//        public ViewHolder(View view) {
//            ButterKnife.bind(view);
//        }
//    }

    class TestPagerAdapter extends PagerAdapter {

        private Context context;
        private Exerciseresponse exerciseresponse;
        private int selectIndex;

        public TestPagerAdapter(Context context, Exerciseresponse exerciseresponse) {
            this.context = context;
            this.exerciseresponse = exerciseresponse;
        }

        @Override
        public int getCount() {
            if(exerciseresponse != null && exerciseresponse.questions != null) {
                return exerciseresponse.questions.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            if(exerciseresponse == null || exerciseresponse.questions == null
                    || exerciseresponse.questions.isEmpty()) {
                return null;
            }

            Question question = exerciseresponse.questions.get(position);

            View view = LayoutInflater.from(context).inflate(R.layout.view_test_pager, null);
            final TextView title = (TextView) view.findViewById(R.id.test_title);
            TextView indicator = (TextView) view.findViewById(R.id.test_indicator);
            WebView webView = (WebView) view.findViewById(R.id.test_web_view);
            webView.setBackgroundColor(Color.WHITE);
            webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
            webView.getSettings().setJavaScriptEnabled(true);

            webView.loadUrl(question.url);
            title.setText(question.content_data.title);
            indicator.setText((position + 1) + "/" + getCount());

            RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.test_radio_group);
            for (int i = 0; i < question.content_data.option.size(); ++i) {
                String s = question.content_data.option.get(i);
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                radioButton.setVisibility(View.VISIBLE);
                radioButton.setText(s);
            }
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    selectIndex = i;
                    if(position < getCount() - 1) {
                        testViewPager.setCurrentItem(position + 1, true);
                    }
                }
            });


            View analyzationRoot = view.findViewById(R.id.test_analyzation_root);
            TextView result = (TextView) view.findViewById(R.id.test_answer_result);
            TextView source = (TextView) view.findViewById(R.id.test_source);
            TextView analyzation = (TextView) view.findViewById(R.id.test_answer_analyzation);

            result.setText("答案解析：\n正确答案是：" + question.content_data.option.get(question.content_data.answer_index - 1)
                    + "，您的答案是：" + question.content_data.option.get(selectIndex) + "\n"
                    + (question.content_data.answer_index == selectIndex + 1 ? "回答正确" : "回答错误") );
            source.setText("来源：" + question.source);
            analyzation.setText("解析：\n" + question.content_data.answer_analyzation);

            container.addView(view);
            return view;
        }
    }


    public static void launch(Context context, String path, String level) {
        Intent intent = new Intent();
        intent.setClass(context, TestActivity.class);
        intent.putExtra(PATH, path);
        intent.putExtra(LEVEL, level);
        context.startActivity(intent);
    }
}
