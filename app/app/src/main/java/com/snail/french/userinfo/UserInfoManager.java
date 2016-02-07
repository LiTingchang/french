package com.snail.french.userinfo;

import android.content.Context;
import android.text.TextUtils;

import com.snail.french.utils.PreferencesUtils;

/**
 * Created by litingchang on 15-12-21.
 */
public class UserInfoManager {

    private static final String CACHED_PHONE = "cached_phone";
    private static final String ACCESS_TOKEN = "access_token";

    public static boolean isLogin(Context context)  {
        return !TextUtils.isEmpty(getAccessToken(context));
    }

    public static void logout(Context context)  {
        saveAccessToken(context, "");
    }

//    public static String getAccessToken() {
//        return "f0d10a1ca71a11e5a899525400587ef4";
//    }

    public static void cachePhoneNumber(Context context, String phone) {
        PreferencesUtils.putString(context, CACHED_PHONE, phone);
    }

    public static String getCachedPhoneNumber(Context context) {
        return  PreferencesUtils.getString(context, CACHED_PHONE);
    }

    public static boolean hasCachedPhone(Context context) {
        return !TextUtils.isEmpty(getCachedPhoneNumber(context));
    }

    public static void saveAccessToken(Context context, String token) {
        PreferencesUtils.putString(context, ACCESS_TOKEN, token);
    }

    public static String getAccessToken(Context context) {
        return  PreferencesUtils.getString(context, ACCESS_TOKEN);
    }
}
