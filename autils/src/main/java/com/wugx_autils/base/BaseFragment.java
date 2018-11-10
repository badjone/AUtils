package com.wugx_autils.base;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.Utils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.trello.rxlifecycle2.components.RxFragment;
import com.wugx_autils.R;
import com.wugx_autils.mvp.presenter.BasePresenter;
import com.wugx_autils.mvp.view.BaseView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * fragment基类
 *
 * @author wugx
 * @date 2018/1/24.
 */

public abstract class BaseFragment<V, T extends BasePresenter<V>> extends RxFragment implements BaseView {
    protected Context mContext;
    protected BaseActivity baseActivity;
    protected View view;

    protected T presenter;

    protected abstract T createPresenterFromFt();

    Unbinder unbinder;

    private boolean isViewCreated;
    //Fragment对用户可见的标记
    private boolean isUIVisible;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        baseActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        presenter = createPresenterFromFt();
        if (presenter != null) presenter.attachView((V) this);
        LinearLayout linearLayout = setView(inflater);
        unbinder = ButterKnife.bind(this, linearLayout);

        return linearLayout;
    }

    /**
     * 取消预加载
     */
    private void lazyLoad() {
        //这里进行双重标记判断,是因为setUserVisibleHint会多次回调,并且会在onCreateView执行前回调,必须确保onCreateView加载完毕且页面可见,才加载数据
        if (isViewCreated && isUIVisible) {
            loadData();
            setListener();
            //数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false;
            isUIVisible = false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isViewCreated = false;
        isUIVisible = false;

        if (presenter != null) {
            presenter.detachView();
            presenter = null;
        }
        unbinder.unbind();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见
        if (isVisibleToUser) {
            isUIVisible = true;
            lazyLoad();
        } else {
            isUIVisible = false;
        }
    }


    @NonNull
    private LinearLayout setView(LayoutInflater inflater) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1);
        LinearLayout linearLayout = new LinearLayout(baseActivity);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(params);

        if (isShowTitle()) {
            View layout_title = View.inflate(baseActivity, R.layout.layout_title, null);
            ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            initActionBar(layout_title);
            linearLayout.addView(layout_title, p);
        }

        view = inflater.inflate(layoutId(), null);
        view.setLayoutParams(params);
        view.setBackgroundResource(android.R.color.white);
        if (isRebound()) {
            smartRefreshLayout = new SmartRefreshLayout(baseActivity);
            smartRefreshLayout.setLayoutParams(params);
//            setSmartRefreshLayoutCommon(smartRefreshLayout);
            if (view != null) smartRefreshLayout.addView(view, params);
            linearLayout.addView(smartRefreshLayout, params);
        } else {
            if (view != null) linearLayout.addView(view, params);
        }
        return linearLayout;
    }

    private Toolbar mToolbar;
    private TextView tvTitle;
    private ActionBar mActionBar;

    private void initActionBar(View v) {
        mToolbar = v.findViewById(R.id.toolbar_layout);
        tvTitle = v.findViewById(R.id.tv_title);
        if (mToolbar != null) {
            baseActivity.setSupportActionBar(mToolbar);
            mActionBar = baseActivity.getSupportActionBar();
            mActionBar.setDisplayShowTitleEnabled(false);
            if (mActionBar != null) {
                //set back button
                mActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            }
        }
    }

    public boolean isShowTitle() {
        return false;
    }

    public void setTvTitle(String txt) {
        if (TextUtils.isEmpty(txt)) return;
        tvTitle.setText(txt);
    }

    public void setTvTitle(int txtId) {
        if (txtId == 0 || tvTitle == null) return;
        tvTitle.setText(Utils.getApp().getResources().getString(txtId));
    }

    private SmartRefreshLayout smartRefreshLayout;

    public boolean isRebound() {
        return true;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        isViewCreated = true;
        lazyLoad();
    }

    public SmartRefreshLayout getSmartRefreshLayout() {
        if (isRebound()) return smartRefreshLayout;
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        baseActivity = (BaseActivity) context;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public View findViewById(int resId) {
        return view.findViewById(resId);
    }

    protected abstract int layoutId();

    protected abstract void loadData();

    public void setListener() {
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void showException(String msg) {

    }

    @Override
    public void showLoading(String msg) {
        baseActivity.showLoading(msg);
    }

    @Override
    public void hideLoading() {
        baseActivity.hideLoading();
    }

    @Override
    public void showNetError() {

    }
}
