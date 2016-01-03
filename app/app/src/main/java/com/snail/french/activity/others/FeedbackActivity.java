package com.snail.french.activity.others;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.snail.french.R;
import com.snail.french.activity.base.BaseActivity;
import com.snail.french.model.RResponse;
import com.snail.french.net.http.StickerHttpClient;
import com.snail.french.net.http.StickerHttpResponseHandler;
import com.snail.french.utils.StringUtils;
import com.snail.french.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by litingchang on 15-12-9.
 */
public class FeedbackActivity extends BaseActivity {


    @Bind(R.id.feedback_content)
    EditText feedbackContent;
    @Bind(R.id.feedback_contact)
    EditText feedbackContact;
    @Bind(R.id.feedback_submit)
    TextView feedbackSubmit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.feedback_submit)
    void submitFeedback() {

        final String content = StringUtils.deleteWhitespace(feedbackContent.getText().toString());
        if (StringUtils.isEmpty(content)) {
            ToastUtil.shortToast(this, "请输入反馈内容");
            return;
        }

        String contact = StringUtils.deleteWhitespace(feedbackContact.getText().toString());

        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("content", content);
            jsonObject.put("phone", contact);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StickerHttpClient.getInstance()
                .postJson(FeedbackActivity.this, "feedback", jsonObject, new TypeReference<RResponse>() {
                        }.getType(),
                        new StickerHttpResponseHandler<RResponse>() {
                            @Override
                            public void onStart() {
                                showProgressDialog("发送中。。。");
                            }

                            @Override
                            public void onSuccess(RResponse response) {

                                if (response.r == 0) {
                                    ToastUtil.shortToast(FeedbackActivity.this, "发送成功");
                                    FeedbackActivity.this.finish();
                                }

                            }

                            @Override
                            public void onFailure(String message) {
                                ToastUtil.shortToast(FeedbackActivity.this, "发送失败，请稍候重试");
                            }

                            @Override
                            public void onFinish() {
                                dismissProgressDialog();
                            }
                        });

    }

    public static void launch(Context context) {
        Intent intent = new Intent(context, FeedbackActivity.class);
        context.startActivity(intent);
    }
}
