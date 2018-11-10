package com.wugx_autils.http.dialog;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;


/**
 * Created by zhpan on 2017/5/26.
 * Description:
 */

public class DialogUtils {
    //  加载进度的dialog
    private MaterialDialog mProgressDialog;

    /**
     * 显示ProgressDialog
     */
    public void showProgress(Context context, String msg) {
       /* if (context == null || context.isFinishing()) {
            return;
        }*/
        if (mProgressDialog == null) {
            mProgressDialog = new MaterialDialog.Builder(context)
                    .content(msg)
                    .progress(true, 0)
                    .build();
        }
        if (mProgressDialog != null && !mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    /**
     * 显示ProgressDialog
     */
    public void showProgress(Context context) {
        /*if (activity == null || activity.isFinishing()) {
            return;
        }*/
        if (mProgressDialog == null) {
            mProgressDialog = new MaterialDialog.Builder(context)
                    .content("loading...")
                    .progress(true, 0)
                    .build();
        }
        if (mProgressDialog != null && !mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    /**
     * 取消ProgressDialog
     */
    public void dismissProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }


    public MaterialDialog showFileProgressBar(Context context) {
        if (mProgressDialog == null) {
            mProgressDialog = new MaterialDialog.Builder(context)
                    .title("显示进度")
                    .progress(false, 100, true)
                    .show();
        }
        if (mProgressDialog != null && !mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
        return mProgressDialog;
    }
}
