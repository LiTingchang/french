package com.snail.french.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.TypeReference;
import com.snail.french.R;
import com.snail.french.activity.MainActivity;
import com.snail.french.activity.base.BaseActivity;
import com.snail.french.model.LoginResponse;
import com.snail.french.net.http.StickerHttpClient;
import com.snail.french.net.http.StickerHttpResponseHandler;
import com.snail.french.userinfo.UserInfoManager;
import com.snail.french.utils.StringUtils;
import com.snail.french.utils.ToastUtil;
import com.snail.french.weixin.WeiXinUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * Created by litingchang on 15-12-9.
 */
public class LoginActivity extends BaseActivity {

    @Bind(R.id.login_input_name)
    EditText loginInputName;
    @Bind(R.id.clean_phone)
    ImageView cleanPhone;
    @Bind(R.id.login_input_password)
    EditText loginInputPassword;
    @Bind(R.id.clean_password)
    ImageView cleanPassword;
    @Bind(R.id.login_btn)
    TextView loginBtn;
    @Bind(R.id.new_user)
    TextView newUser;
    @Bind(R.id.forget_password)
    TextView forgetPassword;
    @Bind(R.id.weixin_auth)
    ImageView weixinAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        loginInputName.setText(UserInfoManager.getCachedPhoneNumber(LoginActivity.this));
    }

    @OnTextChanged(R.id.login_input_name)
    void onPhpneTextChaged(CharSequence text) {
        if (!text.toString().isEmpty()) {
            cleanPhone.setVisibility(View.VISIBLE);
        } else {
            cleanPhone.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(R.id.clean_phone)
    void onCleanPhoneClicked() {
        loginInputName.setText("");
    }

    @OnTextChanged(R.id.login_input_password)
    void onPasswordTextChanged(CharSequence text) {
        if (!text.toString().isEmpty()) {
            cleanPassword.setVisibility(View.VISIBLE);
        } else {
            cleanPassword.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(R.id.clean_password)
    void onCleanPasswordClicked() {
        loginInputPassword.setText("");
    }

    @OnClick(R.id.login_btn)
    void onLoginClicked() {

        final String phoneNumber = StringUtils.deleteWhitespace(loginInputName.getText().toString());
        if (StringUtils.isEmpty(phoneNumber)) {
            ToastUtil.shortToast(this, "请输入手机号，不可包含空格");
            return;
        }

        UserInfoManager.cachePhoneNumber(LoginActivity.this, phoneNumber);


        String password = StringUtils.deleteWhitespace(loginInputPassword.getText().toString());
        if (StringUtils.isEmpty(password)) {
            ToastUtil.shortToast(this, "请输入登录密码，不可包含空格");
            return;
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone_number", phoneNumber);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        StickerHttpClient.getInstance()
                .postJson(LoginActivity.this, "user/login", jsonObject, new TypeReference<LoginResponse>() {
                        }.getType(),
                        new StickerHttpResponseHandler<LoginResponse>() {
                            @Override
                            public void onStart() {
                                showProgressDialog("登录中。。。");
                            }

                            @Override
                            public void onSuccess(LoginResponse response) {

                                if (response.error_code == 0 && response.access_token != null) {

                                    UserInfoManager.saveAccessToken(LoginActivity.this, response.access_token);

                                    MainActivity.launch(LoginActivity.this);
                                    LoginActivity.this.finish();
                                } else {
                                    ToastUtil.shortToast(LoginActivity.this, "登录失败，请检查手机号码或密码是否正确");
                                }
                            }

                            @Override
                            public void onFailure(String message) {
                                ToastUtil.shortToast(LoginActivity.this, "登录失败，请检查手机号码或密码是否正确");
                            }

                            @Override
                            public void onFinish() {
                                dismissProgressDialog();
                            }
                        });
    }

    @OnClick(R.id.new_user)
    void onNewUserClocked() {
        RegistActivity.launch(LoginActivity.this);
    }

    @OnClick(R.id.forget_password)
    void onForgetPassword() {
        ForgetPasswordActivity.launch(LoginActivity.this);
    }

    @OnClick(R.id.weixin_auth)
    void onWeiXinAuth() {
        WeiXinUtil.getWeiXinUtil(this).sendAuth();
    }

    public static void launch(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
