package com.snail.french.activity;

import android.content.Context;
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
import com.snail.french.view.CommonTitle;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by litingchang on 15-12-9.
 */
public class ForgetPasswordActivity extends BaseActivity {

    @Bind(R.id.login_input_name)
    EditText loginInputName;
    @Bind(R.id.clean_phone)
    ImageView cleanPhone;
    @Bind(R.id.input_verify_code)
    EditText inputVerifyCode;
    @Bind(R.id.get_verify_code)
    TextView getVerifyCode;
    @Bind(R.id.login_input_password)
    EditText loginInputPassword;
    @Bind(R.id.clean_password)
    ImageView cleanPassword;
    @Bind(R.id.login_btn)
    TextView loginBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_step1);
        ButterKnife.bind(this);
    }

    @OnTextChanged(R.id.login_input_name)
    void onPhpneTextChaged(CharSequence text) {
        if(!text.toString().isEmpty()) {
            cleanPhone.setVisibility(View.VISIBLE);
            if(text.toString().length() != 11) {
                getVerifyCode.setTextColor(getResources().getColor(R.color.text_red));
                getVerifyCode.setEnabled(false);
            } else {
                getVerifyCode.setEnabled(true);
                getVerifyCode.setTextColor(getResources().getColor(R.color.text_gray));
            }
        } else {
            cleanPhone.setVisibility(View.INVISIBLE);
            getVerifyCode.setTextColor(getResources().getColor(R.color.text_gray));
            getVerifyCode.setEnabled(true);
        }
    }
    @OnClick(R.id.clean_phone)
    void onCleanPhoneClicked() {
        loginInputName.setText("");
    }

    @OnTextChanged(R.id.login_input_password)
    void onPasswordTextChanged(CharSequence text) {
        if(!text.toString().isEmpty()) {
            cleanPassword.setVisibility(View.VISIBLE);
        } else {
            cleanPassword.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(R.id.clean_password)
    void onCleanPasswordClicked() {
        loginInputPassword.setText("");
    }

    @OnClick(R.id.get_verify_code)
    void onGetVerifyCodeClicked() {
        final String phoneNumber = StringUtils.deleteWhitespace(loginInputName.getText().toString());
        if (StringUtils.isEmpty(phoneNumber)) {
            ToastUtil.shortToast(this, "请输入手机号，不可包含空格");
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("phone_number", phoneNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StickerHttpClient.getInstance()
                .postJson(ForgetPasswordActivity.this, "user/verify_code", jsonObject, new TypeReference<CodeResponse>() {
                        }.getType(),
                        new StickerHttpResponseHandler<CodeResponse>() {
                            @Override
                            public void onStart() {
                                showProgressDialog("验证码发送中。。。");
                            }

                            @Override
                            public void onSuccess(CodeResponse response) {

                                if (response.error_code == 0) {
                                    ToastUtil.shortToast(ForgetPasswordActivity.this, "验证码已发送");
                                } else {
                                    ToastUtil.shortToast(ForgetPasswordActivity.this, "发送失败，请检查手机号是否正确");
                                }
                            }

                            @Override
                            public void onFailure(String message) {
                                ToastUtil.shortToast(ForgetPasswordActivity.this, "发送失败，请检查手机号是否正确");
                            }

                            @Override
                            public void onFinish() {
                                dismissProgressDialog();
                            }
                        });
    }

    @OnClick(R.id.login_btn)
    void onLoginClicked() {


        final String phoneNumber = StringUtils.deleteWhitespace(loginInputName.getText().toString());
        if (StringUtils.isEmpty(phoneNumber)) {
            ToastUtil.shortToast(this, "请输入手机号，不可包含空格");
            return;
        }

        UserInfoManager.cachePhoneNumber(ForgetPasswordActivity.this, phoneNumber);

        String verifyCode = StringUtils.deleteWhitespace(inputVerifyCode.getText().toString());
        if (StringUtils.isEmpty(verifyCode)) {
            ToastUtil.shortToast(this, "请输入验证码，不可包含空格");
            return;
        }

        String password = StringUtils.deleteWhitespace(loginInputPassword.getText().toString());
        if (StringUtils.isEmpty(password)) {
            ToastUtil.shortToast(this, "请输入登录密码，不可包含空格");
            return;
        }


        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("phone_number", phoneNumber);
            jsonObject.put("verify_code", verifyCode);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StickerHttpClient.getInstance()
                .postJson(ForgetPasswordActivity.this, "user/forget_password", jsonObject, new TypeReference<LoginResponse>() {
                        }.getType(),
                        new StickerHttpResponseHandler<LoginResponse>() {
                            @Override
                            public void onStart() {
                                showProgressDialog("重置密码。。。");
                            }

                            @Override
                            public void onSuccess(LoginResponse response) {

                                if (response.error_code == 0) {
                                    if(response.access_token != null) {
                                        UserInfoManager.saveAccessToken(ForgetPasswordActivity.this, response.access_token);
                                        MainActivity.launch(ForgetPasswordActivity.this);
                                        ForgetPasswordActivity.this.finish();
                                        ToastUtil.shortToast(ForgetPasswordActivity.this, "重置成功");
                                    } else {
                                        ToastUtil.shortToast(ForgetPasswordActivity.this, "重置失败，请稍候重试");
                                    }
                                } else if (response.error_code == 1){
                                    ToastUtil.shortToast(ForgetPasswordActivity.this, "重置失败，请稍候重试");
                                } else {
                                    ToastUtil.shortToast(ForgetPasswordActivity.this, "重置失败，请稍候重试");
                                }

                            }

                            @Override
                            public void onFailure(String message) {
                                ToastUtil.shortToast(ForgetPasswordActivity.this, "重置失败，请稍候重试");
                            }

                            @Override
                            public void onFinish() {
                                dismissProgressDialog();
                            }
                        });

    }

    public static void launch(Context context) {
        Intent intent = new Intent(context, ForgetPasswordActivity.class);
        context.startActivity(intent);
    }

}
