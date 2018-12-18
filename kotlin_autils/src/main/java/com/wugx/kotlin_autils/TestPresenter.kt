package com.wugx.kotlin_autils

import com.wugx_autils.http.net.common.DefaultObserver
import com.wugx_autils.mvp.presenter.BasePresenter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 *
 *
 *@author Wugx
 *@date   2018/12/18
 */
class TestPresenter : BasePresenter<TestContract.IView>() {

    fun req() {
        rootView.showLoading("玩命加载中...")

        Observable.interval(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : DefaultObserver<Long>() {
                    override fun onSuccess(response: Long?) {
                        //add disposable
                        addDisposable(mDisposable)

                        rootView.hideLoading()
                        rootView.showDatas(">>>${response}")
                    }
                })
    }

}