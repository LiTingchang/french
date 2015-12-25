package com.snail.french.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.snail.french.R;
import com.snail.french.activity.base.BaseActivity;
import com.snail.french.manager.ExerciseManager;
import com.snail.french.model.exercise.Question;
import com.snail.french.net.http.StickerHttpClient;
import com.snail.french.net.http.StickerHttpResponseHandler;
import com.snail.french.userinfo.UserInfoManager;
import com.snail.french.utils.ToastUtil;
import com.snail.french.view.BallSelectorPanel;
import com.snail.french.view.CommonTitle;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by litingchang on 15-12-9.
 */
public class SheetActivity extends BaseActivity {

    @Bind(R.id.titlebar)
    CommonTitle titlebar;
    @Bind(R.id.sheet_submit)
    TextView sheetSubmit;
    @Bind({R.id.shell_panel_1, R.id.shell_panel_2, R.id.shell_panel_3,
            R.id.shell_panel_4, R.id.shell_panel_5, R.id.shell_panel_6})
    List<BallSelectorPanel> shellPanels;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet);
        ButterKnife.bind(this);

        int i = 0;
        for (Pair<String, ArrayList<Question>> questionPair : ExerciseManager.getInstance().getExerciseresponse().getQuestionsPairList()) {
            if(questionPair != null && questionPair.second != null && !questionPair.second.isEmpty()) {
                shellPanels.get(i).setVisibility(View.VISIBLE);
                shellPanels.get(i).init(questionPair.first, i, questionPair.second, true);
                shellPanels.get(i).setOnItemClickedListener(new BallSelectorPanel.OnItemClickedListener() {
                    @Override
                    public void onItemClicked(Question question, int index, int position) {
                        List<Question> questionList = ExerciseManager.getInstance().getExerciseresponse().getQuestions();
                        for (int j = 0; j < questionList.size(); ++j) {
                            if(question.id == questionList.get(j).id) {
                                TestActivity.reLaunch(SheetActivity.this, false, j);
                                break;
                            }
                        }
                    }
                });
            }
            ++i;
        }
    }


    @OnClick(R.id.sheet_submit)
    void onSubmitClicked() {
        Log.e("aaaaaaaaaa", ExerciseManager.getInstance().getAnswerJsonString());

        StickerHttpClient.getInstance()
                .addAutorization(UserInfoManager.getAccessToken(SheetActivity.this))
                .postJsonString(SheetActivity.this, ExerciseManager.getInstance().getPath(),
                        ExerciseManager.getInstance().getAnswerJsonString(),
                        new TypeReference<Object>() {
                        }.getType(),
                        new StickerHttpResponseHandler<Object>() {
                            @Override
                            public void onStart() {
                                showProgressDialog("答案提交中。。。");
                            }

                            @Override
                            public void onSuccess(Object response) {
                                ToastUtil.shortToast(SheetActivity.this, "提交成功");
                            }

                            @Override
                            public void onFailure(String message) {
                                ToastUtil.shortToast(SheetActivity.this, "提交失败");
                            }

                            @Override
                            public void onFinish() {
                                dismissProgressDialog();
                            }
                        });
    }


    public static void launch(Context context) {
        Intent intent = new Intent(context, SheetActivity.class);
        context.startActivity(intent);
    }
}
