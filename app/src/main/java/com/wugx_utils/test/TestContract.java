package com.wugx_utils.test;

import com.wugx_autils.mvp.view.BaseView;

/**
 * @author Wugx
 * @date 2018/11/8
 */
public interface TestContract {
    interface TestView extends BaseView {
        void showDatas(String result);
    }
}
