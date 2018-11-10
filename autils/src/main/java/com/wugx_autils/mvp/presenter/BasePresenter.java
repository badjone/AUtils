package com.wugx_autils.mvp.presenter;

import java.lang.ref.WeakReference;


/**
 * @author wugx
 * @data 2018/5/30.
 */

public class BasePresenter<T> {


    protected WeakReference<T> mReference;

    public void attachView(T view) {
        mReference = new WeakReference<T>(view);
    }

    public void detachView() {
        if (mReference != null) {
            mReference.clear();
            mReference = null;
        }
    }

    public T getView() {
        return mReference.get();
    }




}
