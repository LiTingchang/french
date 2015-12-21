package com.snail.french.model;

/**
 * Created by litingchang on 15-12-21.
 */
public class LoginResponse {


    /**
     * access_token : f0d10a1ca71a11e5a899525400587ef4
     * error_code : 0
     */

    private String access_token;
    private int error_code;

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getAccess_token() {
        return access_token;
    }

    public int getError_code() {
        return error_code;
    }
}
