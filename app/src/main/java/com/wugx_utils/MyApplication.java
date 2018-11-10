package com.wugx_utils;

import com.blankj.utilcode.util.Utils;
import com.wugx_autils.BaseApplication;

/**
 * @author Wugx
 * @date 2018/11/7
 */
public class MyApplication extends BaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Utils.init(this);
    }
}
