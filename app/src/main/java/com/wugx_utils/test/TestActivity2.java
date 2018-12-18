package com.wugx_utils.test;

import android.text.TextUtils;
import android.view.MenuItem;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.wugx_autils.base.BaseActivity;
import com.wugx_autils.http.net.exception.ServerResponseException;
import com.wugx_autils.mvp.presenter.BasePresenter;
import com.wugx_utils.R;
import com.wugx_utils.entity.LoginBean;
import com.wugx_utils.entity.UserInfo;
import com.wugx_utils.http.RetrofitHelper;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author Wugx
 * @date 2018/12/17
 */
public class TestActivity2 extends BaseActivity {

    private android.widget.TextView mTvTestInfo;
    private android.widget.TextView mTvTestDialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void initData() {
        initView();

//        UiUtils.showLoadDialog(null);
//        UiUtils.showBaseDialog(new BaseDialog.DialogBaseListener() {
//            @Override
//            public void next() {
//
//            }
//
//            @Override
//            public void cancel() {
//
//            }
//        }, null, "是否马上登陆？", "现在就去");
        testlogin();

    }

    @Override
    public boolean isUseLoadService() {
        return false;
    }

    @Override
    public boolean isShowTitleRight(MenuItem item) {
        return true;
    }

    @Override
    public void setTitleRightListener() {
        super.setTitleRightListener();
        ToastUtils.showShort("show");
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    private void initView() {
        mTvTestInfo = findViewById(R.id.tv_test_info);
        mTvTestDialog = findViewById(R.id.tv_test_dialog);
    }


    /**
     * 测试登陆
     */
    private void testlogin() {

        showLoading(null);
        //牧场登陆测试...
        String url = "mobleClaim/login.do";
        Map<String, String> map = new HashMap<>();
        map.put("cellPhoneNum", "18219160104");
        map.put("password", "a123456");
        map.put("deviceId", "c4:0b:cb:84:97:70");
        map.put("registrationId", "140fe1da9e80bacb3a2");

        //登陆嵌套获取用户信息
        RetrofitHelper.getApiService(null)
                .toLogin(url, map)
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<LoginBean, ObservableSource<UserInfo>>() {
                    @Override
                    public ObservableSource<UserInfo> apply(LoginBean loginBean) throws Exception {
                        LogUtils.d("登陆成功>>>" + loginBean.toString());
                        if (!TextUtils.isEmpty(loginBean.getToken())) {
                            //登陆成功
                            return RetrofitHelper.getApiService().getUserMember(
                                    loginBean.getToken(), loginBean.getMemberId(), "c4:0b:cb:84:97:70");
                        } else {
                            showError(loginBean.getMsg());
                            //登陆失败
                            throw new ServerResponseException("登陆异常", loginBean.getMsg());
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UserInfo userInfo) {
                        LogUtils.d("获取到用户信息>>" + userInfo.toString());
                        hideLoading();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        showException(e.getLocalizedMessage());
                        LogUtils.d("onError>>" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void reload() {
        super.reload();
        ToastUtils.showShort("点击重试...");
    }
}
