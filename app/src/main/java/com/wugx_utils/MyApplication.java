package com.wugx_utils;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.wugx_autils.AUtils;
import com.wugx_utils.db.DaoManager;

import static com.wugx_utils.constant.Constant.baseUrl;

/**
 * @author Wugx
 * @date 2018/11/7
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        Utils.init(this);
        DaoManager.getInstance().initGreenDao();
        initAutils();
    }

    private void initAutils() {
        AUtils.getInstance()
                .init(this)
                .setBaseUrl(baseUrl)
//                .setCommonParams()
        ;
    }
}
