package com.snail.french.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
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
import com.snail.french.model.RResponse;
import com.snail.french.model.exercise.Question;
import com.snail.french.net.http.StickerHttpClient;
import com.snail.french.net.http.StickerHttpResponseHandler;
import com.snail.french.userinfo.UserInfoManager;
import com.snail.french.utils.LogUtil;
import com.snail.french.utils.StringUtils;
import com.snail.french.utils.ToastUtil;
import com.snail.french.view.CommonTitle;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by litingchang on 15-12-9.
 * 答题结果解析页面
 */
public class AnalyzationActivity extends BaseActivity {

    private static final String ONLY_ERROE = "only_error";
    private static final String PAGE_INDEX = "page_index";

    @Bind(R.id.titlebar)
    CommonTitle titlebar;
    @Bind(R.id.test_view_pager)
    ViewPager testViewPager;

    private ArrayList<Question> mQuestions;
    private TestPagerAdapter adapter;

    private boolean onlyError = false;
    private int pageIndex = 0;
    private int currentPageId = 0;

    private HashMap<Integer, Boolean> favMap = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        pageIndex = getIntent().getIntExtra(PAGE_INDEX, -1);
        onlyError = getIntent().getBooleanExtra(ONLY_ERROE, false);

        titlebar.setTitleText(ExerciseManager.getInstance().getTitle());
        titlebar.setOnTitleClickListener(new CommonTitle.TitleClickListener() {
            @Override
            public void onLeftClicked(View parent, View v) {

            }

            @Override
            public void onRightClicked(View parent, View v) {
                ToastUtil.shortToast(AnalyzationActivity.this, "敬请期待");
            }

            @Override
            public void onRight2Clicked(View parent, View v) {
            }
        });
        titlebar.setRight2Visibility(View.GONE);

        if(onlyError) {
            mQuestions = ExerciseManager.getInstance().getErrorQuesrions();
        } else {
            mQuestions = ExerciseManager.getInstance().getExerciseresponse().getQuestions();
        }

        if(mQuestions == null || mQuestions.isEmpty()) {
            ToastUtil.shortToast(this, "无错题");
            finish();
        }

        adapter = new TestPagerAdapter(AnalyzationActivity.this, mQuestions);
        testViewPager.setAdapter(adapter);
        testViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPageId = position;
                if (favMap.containsKey(currentPageId) && favMap.get(currentPageId)) {
                    titlebar.setRightImageResource(R.drawable.title_un_collect);
                } else {
                    titlebar.setRightImageResource(R.drawable.title_collect);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state != 0) {
                    stopWebView();
                }
            }
        });

        if(pageIndex >= 0 && pageIndex < adapter.getCount()) {
            testViewPager.setCurrentItem(pageIndex);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopWebView();
    }

    private void stopWebView() {
        try {
            View view = testViewPager.findViewWithTag(currentPageId);
            if (view != null) {
                WebView webView = (WebView) view.findViewById(R.id.test_web_view);
                if (webView != null) {
                    webView.reload();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void favQuestion() {

        if(favMap.containsKey(currentPageId) && favMap.get(currentPageId)) {
            favMap.put(currentPageId, false);
            titlebar.setRightImageResource(R.drawable.title_collect);
            return;
        }

        Question question;
        try {
            question = mQuestions.get(currentPageId);
        } catch (Exception e) {
            e.printStackTrace();
            question = null;
        }

        if(question == null) {
            ToastUtil.shortToast(this, "收藏失败，请页面加载完成后重试");
            return;
        }

        final String favPath = "q/" + question.id + "/fav";
        StickerHttpClient.getInstance()
                .addAutorization(UserInfoManager.getAccessToken(AnalyzationActivity.this))
                .post(favPath, null, new TypeReference<RResponse>() {
                        }.getType(),
                        new StickerHttpResponseHandler<RResponse>() {
                            @Override
                            public void onStart() {
                                showProgressDialog("提交中。。。");
                            }

                            @Override
                            public void onSuccess(RResponse response) {
                                if(response.r == 0) {
                                    ToastUtil.shortToast(AnalyzationActivity.this, "收藏成功");
                                    favMap.put(currentPageId, true);
                                    titlebar.setRightImageResource(R.drawable.title_un_collect);
                                } else {
                                    ToastUtil.shortToast(AnalyzationActivity.this, "收藏失败");
                                }
                            }

                            @Override
                            public void onFailure(String message) {
                                ToastUtil.shortToast(AnalyzationActivity.this, "收藏失败");
                            }

                            @Override
                            public void onFinish() {
                                dismissProgressDialog();
                            }
                        });
    }

    class TestPagerAdapter extends PagerAdapter {

        private Context context;
        private ArrayList<Question> questions;

        public TestPagerAdapter(Context context, ArrayList<Question> questions) {
            this.context = context;
            this.questions = questions;
        }

        @Override
        public int getCount() {
            if(questions != null) {
                return questions.size();
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
            if(questions == null
                    || questions.isEmpty()) {
                return null;
            }

            final Question question = questions.get(position);

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

            int checkedId = -1;
            if(ExerciseManager.getInstance().getAnswerMap().containsKey(question.id)) {
                checkedId = ExerciseManager.getInstance().getAnswerMap().get(question.id);
            }

            RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.test_radio_group);
            for (int i = 0; i < question.content_data.option.size(); ++i) {
                String s = question.content_data.option.get(i);
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                radioButton.setVisibility(View.VISIBLE);
                radioButton.setText(s);

                if(checkedId == i) {
                    radioButton.setChecked(true);
                } else {
                    radioButton.setChecked(false);
                }
            }
            setSelecterEnabled(radioGroup, false);

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
//                    for (int i = 0; i < question.content_data.option.size(); ++i) {
//                        if(radioGroup.getChildAt(i).getId() == checkedId) {
//                            selectIndex = i;
//                            break;
//                        }
//                    }
//
//                    ExerciseManager.getInstance().addAnswer(question.id, selectIndex);
//
//                    if(position < getCount() - 1) {
//                        testViewPager.setCurrentItem(position + 1, true);
//                    } else {
//                        //  最后一个，启动答题卡页面
//                        SheetActivity.launch(TestActivity.this);
//                    }
                }
            });

            View analyzationRoot = view.findViewById(R.id.test_analyzation_root);

            analyzationRoot.setVisibility(View.VISIBLE);

            TextView result = (TextView) view.findViewById(R.id.test_answer_result);
            TextView source = (TextView) view.findViewById(R.id.test_source);
            TextView analyzation = (TextView) view.findViewById(R.id.test_answer_analyzation);
            TextView original = (TextView) view.findViewById(R.id.test_answer_original);

            try {
                int answerIndex = question.content_data.answer_index - 1;
                int myIndex = 0;
                if( ExerciseManager.getInstance().getAnswerMap().containsKey(question.id)) {
                    myIndex = ExerciseManager.getInstance().getAnswerMap().get(question.id);
                }
                result.setText("答案解析：\n正确答案是：" + subChoiceTitle(question.content_data.option.get(answerIndex))
                        + "    "
                        + (answerIndex == myIndex ? "回答正确" : "回答错误"));
                source.setText("来源：" + question.source);
                analyzation.setText("解析：\n" + question.content_data.answer_analyzation);

                if(!TextUtils.isEmpty(question.content_data.original_text)) {
                    original.setText("听力原文：\n" + question.content_data.original_text);
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtil.e("TestPagerAdapter", e.toString());
            }

            view.setTag(position);
            container.addView(view);
            return view;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    private String subChoiceTitle(String s) {
        return StringUtils.substring(s, 0, 1);
    }

    private void setSelecterEnabled(RadioGroup radioGroup, boolean b) {
        radioGroup.setEnabled(b);
        for (int i = 0; i < radioGroup.getChildCount(); ++i) {
            radioGroup.getChildAt(i).setEnabled(b);
        }
    }

    public static void launch(Context context, int pageIndex, boolean onlyError) {

        if(pageIndex < 0) {
            pageIndex = 0;
        }

        Intent intent = new Intent();
        intent.setClass(context, AnalyzationActivity.class);
        intent.putExtra(PAGE_INDEX, pageIndex);
        intent.putExtra(ONLY_ERROE, onlyError);
        context.startActivity(intent);

    }

}
