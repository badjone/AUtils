package com.wugx_autils.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.blankj.utilcode.util.ActivityUtils;
import com.wugx_autils.custom.BaseDialog;
import com.wugx_autils.custom.LoadingDialog;

/**
 * 一些 ui显示的公共类
 *
 * @author Wugx
 * @date 2018/11/8
 */
public class UiUtils {
    /**
     * 显示加载中dialog
     *
     * @param msg
     */
    public static void showLoadDialog(String msg) {
        AppCompatActivity topActivity = (AppCompatActivity) ActivityUtils.getTopActivity();
        if (topActivity == null) return;
        LoadingDialog fragment = LoadingDialog.newInstance(msg);
        FragmentManager fm = topActivity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment curr_dialog = fm.findFragmentByTag("loading_dialog");
        //如果存在，则移除掉
        if (curr_dialog != null) {
            ft.remove(curr_dialog).commit();
        }
        ft.add(fragment, "loading_dialog");
        ft.commitAllowingStateLoss();
        ft.show(fragment);
    }

    public static void dismissLoadDialog(){
        AppCompatActivity topActivity = (AppCompatActivity) ActivityUtils.getTopActivity();
        if (topActivity == null) return;
        FragmentManager fm = topActivity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment curr_dialog = fm.findFragmentByTag("loading_dialog");
        //如果存在，则移除掉
        if (curr_dialog != null) {
            ft.remove(curr_dialog).commit();
        }
    }


    public static void showBaseDialog(BaseDialog.DialogBaseListener listener, String... params) {
        AppCompatActivity topActivity = (AppCompatActivity) ActivityUtils.getTopActivity();
        if (topActivity == null) return;
        String title = null, content = null, next = null, cancel = null;
        switch (params.length) {
            case 1:
                title = params[0];
                break;
            case 2:
                title = params[0];
                content = params[1];
                break;
            case 3:
                title = params[0];
                content = params[1];
                next = params[2];
                break;
            case 4:
                title = params[0];
                content = params[1];
                next = params[2];
                cancel = params[3];
                break;
        }
        BaseDialog fragment = BaseDialog.newInstance(title, content, next, cancel)
                .setListener(listener);
        FragmentManager fm = topActivity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment curr_dialog = fm.findFragmentByTag("base_dialog");
        //如果存在，则移除掉
        if (curr_dialog != null) {
            ft.remove(curr_dialog).commit();
        }
        ft.add(fragment, "base_dialog");
        ft.commitAllowingStateLoss();
        ft.show(fragment);
    }

}
