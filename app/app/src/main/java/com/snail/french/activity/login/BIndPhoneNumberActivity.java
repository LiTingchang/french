package com.snail.french.activity.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.snail.french.R;
import com.snail.french.activity.base.BaseActivity;
import com.snail.french.model.CodeResponse;
import com.snail.french.model.LoginResponse;
import com.snail.french.net.http.StickerHttpClient;
import com.snail.french.net.http.StickerHttpResponseHandler;
import com.snail.french.userinfo.UserInfoManager;
import com.snail.french.utils.StringUtils;
import com.snail.french.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by litingchang on 2/6/16.
 */
public class BindPhoneNumberActivity extends BaseActivity {

    @Bind(R.id.bind_input_name)
    EditText bindInputName;
    @Bind(R.id.clean_phone)
    ImageView cleanPhone;
    @Bind(R.id.bind_verify_code)
    EditText bindVerifyCode;
    @Bind(R.id.get_verify_code)
    TextView getVerifyCode;

    @Bind(R.id.bind_btn)
    TextView bindBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
        ButterKnife.bind(this);
    }

    @OnTextChanged(R.id.bind_input_name)
    void onPhpneTextChaged(CharSequence text) {
        if(!text.toString().isEmpty()) {
            cleanPhone.setVisibility(View.VISIBLE);
            if(text.toString().length() != 11) {
                getVerifyCode.setTextColor(getResources().getColor(R.color.text_gray));
                getVerifyCode.setEnabled(false);
            } else {
                getVerifyCode.setEnabled(true);
                getVerifyCode.setTextColor(getResources().getColor(R.color.text_red));
            }
        } else {
            cleanPhone.setVisibility(View.INVISIBLE);
            getVerifyCode.setTextColor(getResources().getColor(R.color.text_gray));
            getVerifyCode.setEnabled(true);
        }
    }
    @OnClick(R.id.clean_phone)
    void onCleanPhoneClicked() {
        bindInputName.setText("");
    }

    @OnClick(R.id.get_verify_code)
    void onGetVerifyCodeClicked() {
        final String phoneNumber = StringUtils.deleteWhitespace(bindInputName.getText().toString());
        if (StringUtils.isEmpty(phoneNumber)) {
            ToastUtil.shortToast(this, "请输入手机号，不可包含空格");
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("phone_number", phoneNumber);
        } catch (JSONException e) {
            e.printStackTrace();
            ToastUtil.shortToast(BindPhoneNumberActivity.this, "程序异常，请检查输入后重试");
            return;
        }

        StickerHttpClient.getInstance()
                .postJson(BindPhoneNumberActivity.this, "user/verify_code", jsonObject, new TypeReference<CodeResponse>() {
                        }.getType(),
                        new StickerHttpResponseHandler<CodeResponse>() {
                            @Override
                            public void onStart() {
                                showProgressDialog("验证码发送中。。。");
                            }

                            @Override
                            public void onSuccess(CodeResponse response) {

                                if (response.error_code == 0) {
                                    ToastUtil.shortToast(BindPhoneNumberActivity.this, "验证码已发送");
                                } else {
                                    ToastUtil.shortToast(BindPhoneNumberActivity.this, "发送失败，请检查手机号是否正确");
                                }
                            }

                            @Override
                            public void onFailure(String message) {
                                ToastUtil.shortToast(BindPhoneNumberActivity.this, "发送失败，请检查手机号是否正确");
                            }

                            @Override
                            public void onFinish() {
                                dismissProgressDialog();
                            }
                        });
    }

    @OnClick(R.id.login_btn)
    void onLoginClicked() {


        final String phoneNumber = StringUtils.deleteWhitespace(bindInputName.getText().toString());
        if (StringUtils.isEmpty(phoneNumber)) {
            ToastUtil.shortToast(this, "请输入手机号，不可包含空格");
            return;
        }

        String verifyCode = StringUtils.deleteWhitespace(bindVerifyCode.getText().toString());
        if (StringUtils.isEmpty(verifyCode)) {
            ToastUtil.shortToast(this, "请输入验证码，不可包含空格");
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("phone_number", phoneNumber);
            jsonObject.put("verify_code", verifyCode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StickerHttpClient.getInstance()
                .postJson(BindPhoneNumberActivity.this, "user/set_phonenumber", jsonObject, new TypeReference<LoginResponse>() {
                        }.getType(),
                        new StickerHttpResponseHandler<LoginResponse>() {
                            @Override
                            public void onStart() {
                                showProgressDialog("绑定中。。。");
                            }

                            @Override
                            public void onSuccess(LoginResponse response) {

                                if (response.error_code == 0) {
                                    if(response.access_token != null) {
                                        UserInfoManager.cachePhoneNumber(BindPhoneNumberActivity.this, phoneNumber);
                                    } else {
                                        ToastUtil.shortToast(BindPhoneNumberActivity.this, "绑定失败，请稍候重试");
                                    }
                                }
                            }

                            @Override
                            public void onFailure(String message) {
                                ToastUtil.shortToast(BindPhoneNumberActivity.this, "绑定失败，请稍候重试");
                            }

                            @Override
                            public void onFinish() {
                                dismissProgressDialog();
                            }
                        });

    }

    public static void launchForResult(Activity activity, int requestCode) {
        Intent intent = new Intent(activity, BindPhoneNumberActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }
}
