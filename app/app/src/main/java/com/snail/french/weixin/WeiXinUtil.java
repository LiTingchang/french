package com.snail.french.weixin;

import android.content.Context;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by litingchang on 16-2-4.
 */
public class WeiXinUtil {

    public static final String AppID = "wxbcff618cea783aed";
    public static final String AppSecret = "d4624c36b6795d1d99dcf0547af5443d";

    private static final String WEIXIN_SCOPE = "snsapi_userinfo";
    private static final String WEIXIN_STATE = "french_state";

    private static WeiXinUtil weiXinUtil;

    private IWXAPI iwxapi;

    private WeiXinUtil() {
    }

    private WeiXinUtil(Context context) {
        iwxapi = WXAPIFactory.createWXAPI(context, AppID, true);
        iwxapi.registerApp(AppID);
    }

    public static WeiXinUtil getWeiXinUtil(Context context) {
        if (weiXinUtil == null) {
            synchronized (WeiXinUtil.class) {
                if(weiXinUtil == null) {
                    weiXinUtil = new WeiXinUtil(context);
                }
            }
        }
        return weiXinUtil;
    }

    public IWXAPI getIWXAPI() {
        return iwxapi;
    }

    public void sendAuth() {
        SendAuth.Req req = new SendAuth.Req();
        req.scope = WEIXIN_SCOPE;
        req.state = WEIXIN_STATE;
        iwxapi.sendReq(req);
    }
}
