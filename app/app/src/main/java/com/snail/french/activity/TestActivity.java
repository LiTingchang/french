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
import android.webkit.WebView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.snail.french.R;
import com.snail.french.activity.base.BaseActivity;
import com.snail.french.manager.ExerciseManager;
import com.snail.french.model.exercise.Exerciseresponse;
import com.snail.french.model.exercise.Question;
import com.snail.french.net.http.StickerHttpClient;
import com.snail.french.net.http.StickerHttpResponseHandler;
import com.snail.french.userinfo.UserInfoManager;
import com.snail.french.utils.LogUtil;
import com.snail.french.utils.StringUtils;
import com.snail.french.utils.ToastUtil;
import com.snail.french.view.CommonTitle;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by litingchang on 15-12-9.
 */
public class TestActivity extends BaseActivity {

    private static final String PATH = "path";
    private static final String TITLE = "title";

    @Bind(R.id.titlebar)
    CommonTitle titlebar;
    @Bind(R.id.test_view_pager)
    ViewPager testViewPager;

    private boolean showAnalyzation = false;
    String path;
    String title;

    Exerciseresponse exerciseresponse;
    TestPagerAdapter adapter;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        path = getIntent().getStringExtra(PATH);
        title = getIntent().getStringExtra(TITLE);

        ExerciseManager.getInstance().setPath(path);


        titlebar.setTitleText(title);
        titlebar.setOnTitleClickListener(new CommonTitle.TitleClickListener() {
            @Override
            public void onLeftClicked(View parent, View v) {

            }

            @Override
            public void onRightClicked(View parent, View v) {

            }

            @Override
            public void onRight2Clicked(View parent, View v) {
                SheetActivity.launch(TestActivity.this);
            }
        });


        requestData();

    }

    private void requestData() {

        StickerHttpClient.getInstance()
                .addAutorization(UserInfoManager.getAccessToken(TestActivity.this))
                .get(path, null, new TypeReference<Exerciseresponse>() {
                        }.getType(),
                        new StickerHttpResponseHandler<Exerciseresponse>() {
                            @Override
                            public void onStart() {
                                showProgressDialog("数据加载中。。。");
                            }

                            @Override
                            public void onSuccess(Exerciseresponse response) {
                                ExerciseManager.getInstance().setExerciseresponse(response);
                                adapter = new TestPagerAdapter(TestActivity.this, response);
                                testViewPager.setAdapter(adapter);
                            }

                            @Override
                            public void onFailure(String message) {

                            }

                            @Override
                            public void onFinish() {
                                dismissProgressDialog();
                            }
                        });
    }

    @Override
    protected void onDestroy() {
        ExerciseManager.getInstance().clean();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    class TestPagerAdapter extends PagerAdapter {

        private Context context;
        private Exerciseresponse exerciseresponse;
        private int selectIndex = 0;

        public TestPagerAdapter(Context context, Exerciseresponse exerciseresponse) {
            this.context = context;
            this.exerciseresponse = exerciseresponse;
        }

        @Override
        public int getCount() {
            if(exerciseresponse != null) {
                return exerciseresponse.getQuestions().size();
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
            if(exerciseresponse == null
                    || exerciseresponse.getQuestions().isEmpty()) {
                return null;
            }

            final Question question = exerciseresponse.getQuestions().get(position);

            View view = LayoutInflater.from(context).inflate(R.layout.view_test_pager, null);
            final TextView title = (TextView) view.findViewById(R.id.test_title);
            TextView indicator = (TextView) view.findViewById(R.id.test_indicator);
            TextView content = (TextView) view.findViewById(R.id.test_content);
            WebView webView = (WebView) view.findViewById(R.id.test_web_view);
            webView.setBackgroundColor(Color.WHITE);
            webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(question.url);

            title.setText(question.content_data.title);
            indicator.setText((position + 1) + "/" + getCount());
            content.setText(question.content_data.content);

            RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.test_radio_group);
            for (int i = 0; i < question.content_data.option.size(); ++i) {
                String s = question.content_data.option.get(i);
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                radioButton.setVisibility(View.VISIBLE);
                radioButton.setText(s);
            }
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                    for (int i = 0; i < question.content_data.option.size(); ++i) {
                        if(radioGroup.getChildAt(i).getId() == checkedId) {
                            selectIndex = i;
                            break;
                        }
                    }

                    ExerciseManager.getInstance().addAnswer(question.id, selectIndex);

                    if(position < getCount() - 1) {
                        testViewPager.setCurrentItem(position + 1, true);
                    } else {
                        //  最后一个，启动答题卡页面
                        SheetActivity.launch(TestActivity.this);
                    }
                }
            });


            View analyzationRoot = view.findViewById(R.id.test_analyzation_root);
            TextView result = (TextView) view.findViewById(R.id.test_answer_result);
            TextView source = (TextView) view.findViewById(R.id.test_source);
            TextView analyzation = (TextView) view.findViewById(R.id.test_answer_analyzation);

            try {
                int answerIndex = question.content_data.answer_index - 1;
                result.setText("答案解析：\n正确答案是：" + question.content_data.option.get(answerIndex)
                        + "，您的答案是：" + question.content_data.option.get(selectIndex) + "\n"
                        + (answerIndex == selectIndex ? "回答正确" : "回答错误"));
                source.setText("来源：" + question.source);
                analyzation.setText("解析：\n" + question.content_data.answer_analyzation);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e("TestPagerAdapter", e.toString());
            }

            container.addView(view);
            return view;
        }
    }

    public static void launch(Context context, String path, String title) {

        if(StringUtils.isEmpty(path)) {
            return;
        }

        Intent intent = new Intent();
        intent.setClass(context, TestActivity.class);
        intent.putExtra(PATH, path);
        intent.putExtra(TITLE, title);
        context.startActivity(intent);

    }
}
