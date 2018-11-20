package com.wugx_utils.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author Wugx
 * @date 2018/11/20
 */
@Entity
public class UserData {
    //    @Id
//    public Long id;
    public int userToken;
    @Unique
    public String userId;

    @Generated(hash = 221678076)
    public UserData(int userToken, String userId) {
        this.userToken = userToken;
        this.userId = userId;
    }

    @Generated(hash = 1838565001)
    public UserData() {
    }

    @Override
    public String toString() {
        return "UserData{" +
                "userToken=" + userToken +
                ", userId='" + userId + '\'' +
                '}';
    }

    public int getUserToken() {
        return this.userToken;
    }

    public void setUserToken(int userToken) {
        this.userToken = userToken;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
