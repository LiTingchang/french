package com.snail.french.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.snail.french.R;
import com.snail.french.activity.base.BaseActivity;
import com.snail.french.manager.ExerciseManager;
import com.snail.french.model.exercise.Question;
import com.snail.french.model.exercise.ResultResponse;
import com.snail.french.net.http.StickerHttpClient;
import com.snail.french.net.http.StickerHttpResponseHandler;
import com.snail.french.userinfo.UserInfoManager;
import com.snail.french.utils.ToastUtil;
import com.snail.french.view.BallSelectorPanel;
import com.snail.french.view.CommonTitle;

import java.util.ArrayList;
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

                                if (response.r == 0) {
                                    resultScoreSummary.setVisibility(View.VISIBLE);
                                    resultDetail.setVisibility(View.GONE);

                                    int score = 0;
                                    List<Question> questions = ExerciseManager.getInstance().getExerciseresponse().getQuestions();
                                    Map<Integer, Integer> answerMap =  ExerciseManager.getInstance().getAnswerMap();
                                    for (Question question : questions) {
                                        if(answerMap.containsKey(question.id)) {
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
                                    resultScore.setText(String.valueOf(response.score));
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


    private void showResutSheet() {
        int i = 0;
        for (Pair<String, ArrayList<Question>> questionPair : ExerciseManager.getInstance().getExerciseresponse().getQuestionsPairList()) {
            if (questionPair != null && questionPair.second != null && !questionPair.second.isEmpty()) {
                shellPanels.get(i).setVisibility(View.VISIBLE);
                shellPanels.get(i).init(questionPair.first, i, questionPair.second, true);
                shellPanels.get(i).setOnItemClickedListener(new BallSelectorPanel.OnItemClickedListener() {
                    @Override
                    public void onItemClicked(Question question, int index, int position) {
                        List<Question> questionList = ExerciseManager.getInstance().getExerciseresponse().getQuestions();
                        for (int j = 0; j < questionList.size(); ++j) {
                            if (question.id == questionList.get(j).id) {
                                TestActivity.reLaunch(ResultActivity.this, true, j);
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
        TestActivity.reLaunch(ResultActivity.this, true, 0);
    }

    public static void launch(Context context) {
        Intent intent = new Intent(context, ResultActivity.class);
        context.startActivity(intent);
    }
}
