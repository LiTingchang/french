package com.snail.french.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.TypeReference;
import com.snail.french.R;
import com.snail.french.activity.MainActivity;
import com.snail.french.activity.base.BaseActivity;
import com.snail.french.activity.login.LoginActivity;
import com.snail.french.model.LoginResponse;
import com.snail.french.model.weixin.AuthResponse;
import com.snail.french.model.weixin.UserInfoResponse;
import com.snail.french.net.http.StickerHttpClient;
import com.snail.french.net.http.StickerHttpResponseHandler;
import com.snail.french.userinfo.UserInfoManager;
import com.snail.french.utils.StringUtils;
import com.snail.french.utils.ToastUtil;
import com.snail.french.weixin.WeiXinUtil;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler{

    private IWXAPI api;

	private AuthResponse mAuthResponse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    	api = WeiXinUtil.getWeiXinUtil(this).getIWXAPI();

        api.handleIntent(getIntent(), this);
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		setIntent(intent);
        api.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
//		switch (req.getType()) {
//		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
//			goToGetMsg();
//			break;
//		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
//			goToShowMsg((ShowMessageFromWX.Req) req);
//			break;
//		default:
//			break;
//		}
	}

	@Override
	public void onResp(BaseResp resp) {

		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			ToastUtil.shortToast(this, R.string.errcode_success);
			if(resp instanceof SendAuth.Resp) {
				auth(((SendAuth.Resp)resp).code);
			}
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			ToastUtil.shortToast(this, R.string.errcode_cancel);
			finish();
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			ToastUtil.shortToast(this, R.string.errcode_deny);
			finish();
			break;
		default:
			ToastUtil.shortToast(this, R.string.errcode_unknown);
			finish();
			break;
		}
	}
	
	private void auth(String code) {
		StickerHttpClient.getInstance().weixinOauth(code, new TypeReference<AuthResponse>() {
				}.getType(),
				new StickerHttpResponseHandler<AuthResponse>() {
					@Override
					public void onStart() {

					}

					@Override
					public void onSuccess(AuthResponse response) {
						mAuthResponse = response;

						getUserInfo(response);
					}

					@Override
					public void onFailure(String message) {
						ToastUtil.shortToast(WXEntryActivity.this, R.string.errcode_unknown);
						finish();
					}

					@Override
					public void onFinish() {

					}
				});
	}

	private void getUserInfo(AuthResponse response) {
		StickerHttpClient.getInstance().weixinUserInfo(response, new TypeReference<UserInfoResponse>() {
				}.getType(),
				new StickerHttpResponseHandler<UserInfoResponse>() {
					@Override
					public void onStart() {

					}

					@Override
					public void onSuccess(UserInfoResponse response) {
						login(mAuthResponse.access_token, response.openid, response.nickname);
					}

					@Override
					public void onFailure(String message) {
						ToastUtil.shortToast(WXEntryActivity.this, R.string.errcode_unknown);
						finish();
					}

					@Override
					public void onFinish() {

					}
				});
	}

	private void login(String accessToken, String openId, String nickName) {

		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("oauthToken", accessToken);
			jsonObject.put("third_party_uid", openId);
			jsonObject.put("third_party_uname", nickName);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		StickerHttpClient.getInstance()
				.postJson(WXEntryActivity.this, "user/wechat/login", jsonObject, new TypeReference<LoginResponse>() {
						}.getType(),
						new StickerHttpResponseHandler<LoginResponse>() {
							@Override
							public void onStart() {
								showProgressDialog("登录中。。。");
							}

							@Override
							public void onSuccess(LoginResponse response) {

								if (!TextUtils.isEmpty(response.access_token)) {

									UserInfoManager.saveAccessToken(WXEntryActivity.this, response.access_token);
									UserInfoManager.cachePhoneNumber(WXEntryActivity.this, StringUtils.makeSafe(response.phone_number));

									MainActivity.launch(WXEntryActivity.this);
									WXEntryActivity.this.finish();
								} else {
									ToastUtil.shortToast(WXEntryActivity.this, "登录失败，请重试");
								}
							}

							@Override
							public void onFailure(String message) {
								ToastUtil.shortToast(WXEntryActivity.this, "登录失败，请检查手机号码或密码是否正确");
							}

							@Override
							public void onFinish() {
								dismissProgressDialog();
							}
						});
	}

}