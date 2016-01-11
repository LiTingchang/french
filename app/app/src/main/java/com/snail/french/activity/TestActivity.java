package com.snail.french.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import com.snail.french.model.exercise.Exerciseresponse;
import com.snail.french.model.exercise.Question;
import com.snail.french.net.http.StickerHttpClient;
import com.snail.french.net.http.StickerHttpResponseHandler;
import com.snail.french.userinfo.UserInfoManager;
import com.snail.french.utils.LogUtil;
import com.snail.french.utils.StringUtils;
import com.snail.french.utils.ToastUtil;
import com.snail.french.view.CommonTitle;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by litingchang on 15-12-9.
 */
public class TestActivity extends BaseActivity {

    private static final int SHEET_REQUEST_CODE = 101;
    private static final String PATH = "path";
    private static final String TITLE = "title";

    private static final String SHOW_ANALYZATION = "show_analyzation";
    private static final String PAGE_INDEX = "page_index";

    @Bind(R.id.titlebar)
    CommonTitle titlebar;
    @Bind(R.id.test_view_pager)
    ViewPager testViewPager;

    private boolean showAnalyzation = false;
    private int pageIndex;
    private String path;
    private String title;

//    private Exerciseresponse exerciseresponse;
    private TestPagerAdapter adapter;

    private int currentPageId = 0;

    private HashMap<Integer, Boolean> favMap = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);

        path = getIntent().getStringExtra(PATH);
        title = getIntent().getStringExtra(TITLE);

        ExerciseManager.getInstance().setPath(path);
        ExerciseManager.getInstance().setTitle(title);


        titlebar.setTitleText(title);
        titlebar.setOnTitleClickListener(new CommonTitle.TitleClickListener() {
            @Override
            public void onLeftClicked(View parent, View v) {

            }

            @Override
            public void onRightClicked(View parent, View v) {
                favQuestion();
            }

            @Override
            public void onRight2Clicked(View parent, View v) {
                SheetActivity.launch(TestActivity.this, SHEET_REQUEST_CODE);
            }
        });


        requestData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showAnalyzation = intent.getBooleanExtra(SHOW_ANALYZATION, false);
        pageIndex = intent.getIntExtra(PAGE_INDEX, -1);

        if(showAnalyzation) {
            titlebar.setRight2Visibility(View.GONE);
        }

        adapter.notifyDataSetChanged();
        if(pageIndex >= 0 && pageIndex < adapter.getCount()) {
            testViewPager.setCurrentItem(pageIndex);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopWebView();
    }

    @Override
    protected void onDestroy() {
        ExerciseManager.getInstance().clean();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SHEET_REQUEST_CODE) {
            this.finish();
        }
    }

    private void stopWebView() {
        try {
            View view = testViewPager.findViewWithTag(currentPageId);
            if (view != null) {
                WebView webView = (WebView) view.findViewById(R.id.test_web_view);
                if (webView != null) {
                    webView.reload();

//                    Class.forName("android.webkit.WebView")
//                            .getMethod("onPause", (Class[]) null)
//                            .invoke(webView, (Object[]) null);
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
            question = ExerciseManager.getInstance().getExerciseresponse().getQuestions().get(currentPageId);
        } catch (Exception e) {
            e.printStackTrace();
            question = null;
        }

        if(question == null) {
            ToastUtil.shortToast(this, "收藏失败，请页面加载完成后重试");
            return;
        }

        String favPath = "q/" + question.id + "/fav";
        StickerHttpClient.getInstance()
                .addAutorization(UserInfoManager.getAccessToken(TestActivity.this))
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
                                    ToastUtil.shortToast(TestActivity.this, "收藏成功");
                                    favMap.put(currentPageId, true);
                                    titlebar.setRightImageResource(R.drawable.title_un_collect);
                                } else {
                                    ToastUtil.shortToast(TestActivity.this, "收藏失败");
                                }
                            }

                            @Override
                            public void onFailure(String message) {
                                ToastUtil.shortToast(TestActivity.this, "收藏失败");
                            }

                            @Override
                            public void onFinish() {
                                dismissProgressDialog();
                            }
                        });
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

            int chechedId = -1;
            if(ExerciseManager.getInstance().getAnswerMap().containsKey(question.id)) {
                chechedId = ExerciseManager.getInstance().getAnswerMap().get(question.id);
            }

            final RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.test_radio_group);
            for (int i = 0; i < question.content_data.option.size(); ++i) {
                String s = question.content_data.option.get(i);
                RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);
                radioButton.setVisibility(View.VISIBLE);
                radioButton.setText(s);

                if(chechedId == i) {
                    radioButton.setChecked(true);
                } else {
                    radioButton.setChecked(false);
                }

                final int j = i;
                radioButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectIndex = j;

                        ExerciseManager.getInstance().addAnswer(question.id, selectIndex);

                        setSelecterEnabled(radioGroup, false);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(position < getCount() - 1) {
                                    testViewPager.setCurrentItem(position + 1, true);
                                } else {
                                    //  最后一个，启动答题卡页面
                                    SheetActivity.launch(TestActivity.this, SHEET_REQUEST_CODE);
                                }
                                setSelecterEnabled(radioGroup, true);
                            }
                        }, 300);

                    }
                });
            }

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

            if(showAnalyzation) {
                setSelecterEnabled(radioGroup, false);
                analyzationRoot.setVisibility(View.VISIBLE);

                TextView result = (TextView) view.findViewById(R.id.test_answer_result);
                TextView source = (TextView) view.findViewById(R.id.test_source);
                TextView analyzation = (TextView) view.findViewById(R.id.test_answer_analyzation);

                try {
                    int answerIndex = question.content_data.answer_index - 1;
                    int myIndex = 0;
                    if( ExerciseManager.getInstance().getAnswerMap().containsKey(question.id)) {
                        myIndex = ExerciseManager.getInstance().getAnswerMap().get(question.id);
                    }
                     result.setText("答案解析：\n正确答案是：" + subChoiceTitle(question.content_data.option.get(answerIndex))
                            + "，您的答案是：" + subChoiceTitle(question.content_data.option.get(myIndex)) + "\n"
                            + (answerIndex == myIndex ? "回答正确" : "回答错误"));
                    source.setText("来源：" + question.source);
                    analyzation.setText("解析：\n" + question.content_data.answer_analyzation);
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.e("TestPagerAdapter", e.toString());
                }

            } else {
                if(!radioGroup.isEnabled()) {
                    setSelecterEnabled(radioGroup, true);
                }
                analyzationRoot.setVisibility(View.GONE);
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

    public static void reLaunch(Context context, Boolean showAnalyzation, int index) {

        Intent intent = new Intent();
        intent.setClass(context, TestActivity.class);
        intent.putExtra(SHOW_ANALYZATION, showAnalyzation);
        intent.putExtra(PAGE_INDEX, index);
        context.startActivity(intent);

    }

    public static void showResultLaunch(Context context, Boolean showAnalyzation, int index) {
        Intent intent = new Intent();
        intent.setClass(context, TestActivity.class);
        intent.putExtra(SHOW_ANALYZATION, showAnalyzation);
        intent.putExtra(PAGE_INDEX, index);
        context.startActivity(intent);
    }
}
