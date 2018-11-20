package com.wugx_autils;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.kingja.loadsir.core.LoadSir;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.FalsifyFooter;
import com.scwang.smartrefresh.layout.header.FalsifyHeader;
import com.wugx_autils.callback.AnimateCallback;
import com.wugx_autils.callback.CustomCallback;
import com.wugx_autils.callback.EmptyCallback;
import com.wugx_autils.callback.ErrorCallback;
import com.wugx_autils.callback.LoadingCallback;
import com.wugx_autils.callback.TimeoutCallback;

import retrofit2.Converter;

/**
 * @author Wugx
 * @date 2018/11/10
 */
public class Autils {
    private static Autils mAutils;
    public String baseUrl;
    public Converter.Factory factory;

    public static Autils getInstance() {
        if (mAutils == null) {
            synchronized (Autils.class) {
                mAutils = new Autils();
            }
        }
        return mAutils;
    }


//    public Autils(String baseUrl, Converter.Factory factory) {
//        this.baseUrl = baseUrl;
//        this.factory = factory;
//    }

    public Autils init(Application application) {
        Utils.init(application);
        initLoad();
        initSmartRefreshLayout();
        return this;
    }

    public Autils setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    public Autils setFactory(Converter.Factory factory) {
        this.factory = factory;
        return this;
    }

    private void initLoad() {
        LoadSir.beginBuilder()
                .addCallback(new ErrorCallback())
                .addCallback(new EmptyCallback())
                .addCallback(new LoadingCallback())
                .addCallback(new TimeoutCallback())
                .addCallback(new CustomCallback())
                .addCallback(new AnimateCallback())
                .setDefaultCallback(LoadingCallback.class)
                .commit();
    }

    private void initSmartRefreshLayout() {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
//                layout.setPrimaryColorsId(R.color.grey_f2f2f2, android.R.color.darker_gray);//全局设置主题颜色
//                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header

                return new FalsifyHeader(context);
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
//                return new ClassicsFooter(context).setDrawableSize(20);
                return new FalsifyFooter(context);
            }
        });
    }


//    public static class Builder {
//        private String baseUrl;
//        private Converter.Factory factory;
//        private Application mApplication;
//
//        public Builder init(Application application) {
//            this.mApplication = application;
//            return this;
//        }
//
//        public Builder setBaseUrl(String baseUrl) {
//            this.baseUrl = baseUrl;
//            return this;
//        }
//
//        public String getBaseUrl() {
//            return baseUrl;
//        }
//
//        public Builder setFactory(Converter.Factory factory) {
//            this.factory = factory;
//            return this;
//        }
//
//        public Autils build() {
//            Autils autils = new Autils(this.baseUrl, this.factory);
//            autils.init(mApplication);
//            autils.initLoad();
//            autils.initSmartRefreshLayout();
//            return autils;
//        }
//    }
}
