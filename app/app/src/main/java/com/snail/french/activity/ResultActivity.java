package com.snail.french.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.snail.french.R;
import com.snail.french.activity.base.BaseActivity;
import com.snail.french.constant.FrenchKind;
import com.snail.french.constant.NameConstants;
import com.snail.french.manager.ExerciseManager;
import com.snail.french.model.exercise.Question;
import com.snail.french.model.exercise.ResultResponse;
import com.snail.french.net.http.StickerHttpClient;
import com.snail.french.net.http.StickerHttpResponseHandler;
import com.snail.french.userinfo.UserInfoManager;
import com.snail.french.utils.RichTextUtil;
import com.snail.french.utils.StringUtils;
import com.snail.french.utils.ToastUtil;
import com.snail.french.view.BallSelectorPanel;
import com.snail.french.view.CommonTitle;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by litingchang on 15-12-9.
 */
public class ResultActivity extends BaseActivity {

    @Bind(R.id.titlebar)
    CommonTitle titlebar;
    @Bind(R.id.result_analyze)
    TextView resultAnalyze;
    @Bind({R.id.shell_panel_1, R.id.shell_panel_2, R.id.shell_panel_3,
            R.id.shell_panel_4, R.id.shell_panel_5, R.id.shell_panel_6})
    List<BallSelectorPanel> shellPanels;

    @Bind(R.id.result_score_detail)
    View resultDetail;
    @Bind(R.id.result_total)
    TextView resultTotal;
    @Bind(R.id.result_score)
    TextView resultScore;
    @Bind(R.id.result_level)
    TextView resultLevel;
    @Bind(R.id.result_average)
    TextView resultAverage;
    @Bind(R.id.result_defeat)
    TextView resultDefeat;
    @Bind(R.id.result_accuracy)
    TextView resultAccuracy;
    @Bind(R.id.result_summary_score)
    TextView resultSummaryScore;
    @Bind(R.id.result_summary_total)
    TextView resultSummaryTotal;
    @Bind(R.id.result_score_summary)
    LinearLayout resultScoreSummary;
    @Bind(R.id.result_summery)
    TextView resultSummery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);

        StickerHttpClient.getInstance()
                .addAutorization(UserInfoManager.getAccessToken(ResultActivity.this))
                .postJsonString(ResultActivity.this, ExerciseManager.getInstance().getPath(),
                        ExerciseManager.getInstance().getAnswerJsonString(),
                        new TypeReference<ResultResponse>() {
                        }.getType(),
                        new StickerHttpResponseHandler<ResultResponse>() {
                            @Override
                            public void onStart() {
                                showProgressDialog("答案提交中。。。");
                            }

                            @Override
                            public void onSuccess(ResultResponse response) {

                                String title = ExerciseManager.getInstance().getTitle();
                                if(NameConstants.containName(title)) {
                                    title = "专项智能联系(" + title + ")";
                                }

                                SimpleDateFormat fullFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String date = fullFormat.format(new Date(System.currentTimeMillis()));

                                resultSummery.setText(title +"\n交卷时间：" + date);

                                if (response.r == 0) {
                                    resultScoreSummary.setVisibility(View.VISIBLE);
                                    resultDetail.setVisibility(View.GONE);

                                    int score = 0;
                                    List<Question> questions = ExerciseManager.getInstance().getExerciseresponse().getQuestions();
                                    Map<Integer, Integer> answerMap = ExerciseManager.getInstance().getAnswerMap();
                                    for (Question question : questions) {
                                        if (answerMap.containsKey(question.id)) {
                                            if (question.content_data.answer_index - 1 == answerMap.get(question.id)) {
                                                score++;
                                            }
                                        }
                                    }
                                    resultSummaryScore.setText(String.valueOf(score));
                                    resultSummaryTotal.setText("道/" + questions.size() + "道");
                                } else {
                                    resultScoreSummary.setVisibility(View.GONE);
                                    resultDetail.setVisibility(View.VISIBLE);
                                    resultTotal.setText(String.valueOf(response.total_score));

                                    int score = response.score;
                                    if (ExerciseManager.getInstance().getFrenchKind() == FrenchKind.TEF) {
                                        if (score < 0) {
                                            score = 0;
                                        }
                                    }
                                    resultScore.setText(String.valueOf(score));
                                    resultLevel.setText(String.valueOf(response.level));
                                    resultAverage.setText(response.average_score + "\n全站平均得分");
                                    resultDefeat.setText(response.defeat_examinee + "\n已击败考生");
                                    resultAccuracy.setText(response.accuracy + "%\n正确率");
                                }
                                showResutSheet();
                            }

                            @Override
                            public void onFailure(String message) {
                                ToastUtil.shortToast(ResultActivity.this, "提交失败");
                            }

                            @Override
                            public void onFinish() {
                                dismissProgressDialog();
                            }
                        });
    }

    List<RichTextUtil.RichText> richTexts = new ArrayList<>();
    private void showResutSheet() {
        int i = 0;
        for (Pair<String, ArrayList<Question>> questionPair : ExerciseManager.getInstance().getExerciseresponse().getQuestionsPairList()) {
            if (questionPair != null && questionPair.second != null && !questionPair.second.isEmpty()) {
                shellPanels.get(i).setVisibility(View.VISIBLE);

                int total = questionPair.second.size();
                int rigtCounter = 0;
                for (Question question: questionPair.second){
                    if(ExerciseManager.getInstance().getAnswerMap().containsKey(question.id)) {
                        if(question.content_data.answer_index - 1 ==
                                ExerciseManager.getInstance().getAnswerMap().get(question.id)) {
                            rigtCounter++;
                        }
                    }
                }

                String title;
                if(total == 0){
                    title =  "共" + total + "道，" +
                            "答对" + rigtCounter + "道，正确率 0.00%";
                } else {
                    DecimalFormat decimalFormat=new DecimalFormat("0.00");
                    title = "共" + total + "道，" +
                            "答对" + rigtCounter + "道，正确率"
                            + decimalFormat.format((double)rigtCounter / total * 100) + "%";
                }

                shellPanels.get(i).init(questionPair.first, StringUtils.trim(title), i, questionPair.second, true);
                shellPanels.get(i).setOnItemClickedListener(new BallSelectorPanel.OnItemClickedListener() {
                    @Override
                    public void onItemClicked(Question question, int index, int position) {
                        List<Question> questionList = ExerciseManager.getInstance().getExerciseresponse().getQuestions();
                        for (int j = 0; j < questionList.size(); ++j) {
                            if (question.id == questionList.get(j).id) {
                                AnalyzationActivity.launch(ResultActivity.this, j, false);
                                break;
                            }
                        }
                    }
                });
            }
            ++i;
        }
    }

    @OnClick(R.id.result_analyze)
    void analyzeReuslt() {
        AnalyzationActivity.launch(ResultActivity.this, 0, false);
    }

    @OnClick(R.id.result_error_analyze)
    void analyzeErrorReuslt() {
        AnalyzationActivity.launch(ResultActivity.this, 0, true);
    }

    public static void launch(Context context) {
        Intent intent = new Intent(context, ResultActivity.class);
        context.startActivity(intent);
    }
}
