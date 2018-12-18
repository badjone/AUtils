package com.wugx_autils.mvp.presenter;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * @author wugx
 * @data 2018/5/30.
 */

public class BasePresenter<V> {
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    protected WeakReference<V> mReference;

    public void attachView(V view) {
        mReference = new WeakReference<V>(view);
    }

    public void addDisposable(Disposable d) {
        mCompositeDisposable.add(d);
    }

    public void detachView() {
        if (mReference != null) {
            mReference.clear();
            mReference = null;
        }
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
        }
    }

    public V getRootView() {
        return mReference.get();
    }

}
