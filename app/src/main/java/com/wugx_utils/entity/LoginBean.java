package com.wugx_utils.entity;

/**
 * Created by yangxing on 2018/3/5.
 */

public class LoginBean {
    private String token;
    private String memberId;
    private String msg;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "LoginBean{" +
                "tocken='" + token + '\'' +
                ", memberId='" + memberId + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
