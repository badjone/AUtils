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
import com.wugx_autils.http.net.common.RetrofitUtils;

import java.util.Map;

import retrofit2.Converter;

/**
 * @author Wugx
 * @date 2018/11/10
 */
public class AUtils {
    private static AUtils mAUtils;
    public String baseUrl;
    public Converter.Factory factory;
    public Map<String, String> reqParams;

    private boolean isInit = false;

    public static AUtils getInstance() {
        if (mAUtils == null) {
            synchronized (AUtils.class) {
                mAUtils = new AUtils();
            }
        }
        return mAUtils;
    }

    public AUtils init(Application application) {
        Utils.init(application);
        initLoad();
        initSmartRefreshLayout();
        isInit = true;
        return this;
    }

    /**
     * {@link RetrofitUtils#getRetrofitBuilder(String, Converter.Factory)}
     *
     * @param baseUrl
     * @return
     */
    public AUtils setBaseUrl(String baseUrl) {
        if (!isInit) {
            throw new RuntimeException("清先调用 init 方法");
        }
        this.baseUrl = baseUrl;
        return this;
    }

    /**
     * {@link RetrofitUtils#getRetrofitBuilder(String, Converter.Factory)}
     *
     * @param factory
     * @return
     */
    public AUtils setFactory(Converter.Factory factory) {
        this.factory = factory;
        return this;
    }

    /**
     * 公共网络请求参数
     * {@link RetrofitUtils#getOkHttpClientBuilder()}
     *
     * @param params
     * @return
     */
    public AUtils setCommonParams(Map<String, String> params) {
        this.reqParams = params;
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

}
