package com.wugx_utils.test;

import com.wugx_autils.mvp.presenter.BasePresenter;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * @author Wugx
 * @date 2018/11/8
 */
public class TestPresenter extends BasePresenter<TestContract.TestView> {

    TestContract.TestView mView;

    public TestPresenter(TestContract.TestView view) {
        mView = view;
    }

    public void showInfo(){


        Observable.timer(5, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        // TODO: 2018/10/8 判断登陆状态
                        String result = "我是网络请求的信息";
                        mView.showDatas(result);
                    }
                });

    }
}
