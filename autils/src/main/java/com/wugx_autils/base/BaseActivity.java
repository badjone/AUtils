package com.wugx_autils.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SnackbarUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.kingja.loadsir.core.Transport;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.wugx_autils.R;
import com.wugx_autils.callback.EmptyCallback;
import com.wugx_autils.callback.ErrorCallback;
import com.wugx_autils.callback.LoadingCallback;
import com.wugx_autils.mvp.presenter.BasePresenter;
import com.wugx_autils.mvp.view.BaseView;

import butterknife.ButterKnife;

/**
 * @author wugx
 * @date 2018/9/18.
 */

public abstract class BaseActivity<V, P extends BasePresenter> extends RxAppCompatActivity implements BaseView {
    protected abstract int getLayoutId();

    protected abstract void initData();

    protected P presenter;

    protected abstract P createPresenter();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null) presenter.detachView();
    }

    private SmartRefreshLayout smartRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = createPresenter();
        if (presenter != null) presenter.attachView((V) this);

        //init layout
        View contentLayout = setBaseLayout();
        ButterKnife.bind(this);
        if (isUseLoadService()) {
            initLoadSir(contentLayout);
        }
        initData();
        initListener();
    }

    @Nullable
    private View setBaseLayout() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        View contentLayout =getLayoutInflater().inflate(getLayoutId(), null);
        if (isShowTitle()) {
            View layout_title = View.inflate(this, R.layout.layout_title, null);
            ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            initActionBar(layout_title);
            layout.addView(layout_title, p);
        }

        if (isRebound()) {
            smartRefreshLayout = new SmartRefreshLayout(this);
            smartRefreshLayout.setLayoutParams(parms);
//            setSmartRefreshLayoutCommon(smartRefreshLayout);
            if (contentLayout != null) smartRefreshLayout.addView(contentLayout, parms);
            layout.addView(smartRefreshLayout, parms);
        } else {
            layout.addView(contentLayout, parms);
        }
        setContentView(layout, parms);
        return contentLayout;
    }

    public SmartRefreshLayout getSmartRefreshLayout() {
        if (isRebound()) return smartRefreshLayout;
        return null;
    }

    public boolean isUseLoadService() {
        return false;
    }

    public void initListener() {

    }

    public boolean isRebound() {
        return true;
    }

    public boolean isShowTitle() {
        return true;
    }

    private Toolbar mToolbar;
    protected ActionBar mActionBar;
    private TextView tvTitle;

    private void initActionBar(View v) {
        mToolbar = v.findViewById(R.id.toolbar_layout);
        tvTitle = v.findViewById(R.id.tv_title);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mActionBar = getSupportActionBar();
            mActionBar.setDisplayShowTitleEnabled(false);
            if (mActionBar != null) {
                //set back button
                mActionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
            }

            String defaultTitle = getTitle().toString();
            if (TextUtils.isEmpty(defaultTitle)) {
                tvTitle.setText(R.string.app_name);
            } else {
                tvTitle.setText(defaultTitle);
            }
        }
    }


    public void setTitle(String title) {
        if (!isShowTitle()) return;
        if (tvTitle != null && !TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //back button
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showMsg(String msg) {
        View view = getWindow().getDecorView().findViewById(android.R.id.content);
        SnackbarUtils.with(view).setMessage(msg).show();
    }

    @Override
    public void showToast(Object o) {
        if (o instanceof Integer) {
            ToastUtils.showShort((Integer) o);
        } else {
            ToastUtils.showShort(String.valueOf(o));
        }
    }

    @Override
    public void showLoading(String msg) {
//        getProgressDialog().setMessage(msg).show();

        if (isUseLoadService() && mLoadService != null) {
            mLoadService.showCallback(LoadingCallback.class);
        }
    }

    @Override
    public void hideLoading() {
        if (isUseLoadService() && mLoadService != null) {
            mLoadService.showSuccess();
        }
    }

    @Override
    public void showNetError() {
        LogUtils.d("showNetError:");
        if (isUseLoadService() && mLoadService != null) {
            mTvErrorMsg.setText(getString(R.string.txt_net_error_tips));
            mLoadService.showCallback(ErrorCallback.class);
        }
    }

    @Override
    public void showException(String msg) {
        if (isUseLoadService() && mLoadService != null) {
            //show exception info
            mTvErrorMsg.setText(msg);
            mLoadService.showCallback(ErrorCallback.class);
        }
    }

    private LoadService mLoadService;
    private TextView mTvErrorMsg, mTvEmpty;

    private void initLoadSir(Object o) {
        mLoadService = LoadSir.getDefault().register(o, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {
                mLoadService.showCallback(LoadingCallback.class);
            }
        });

        mLoadService.setCallBack(ErrorCallback.class, new Transport() {
            @Override
            public void order(Context context, View view) {
                mTvErrorMsg = view.findViewById(R.id.tv_error_msg);
            }
        });

        mLoadService.setCallBack(EmptyCallback.class, new Transport() {
            @Override
            public void order(Context context, View view) {
                mTvEmpty = view.findViewById(R.id.tv_empty);
            }
        });
    }

}
