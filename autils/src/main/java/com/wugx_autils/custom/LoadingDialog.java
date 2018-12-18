package com.wugx_autils.custom;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.blankj.utilcode.util.ScreenUtils;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.wugx_autils.R;

/**
 * loading dialog
 *
 * @author Wugx
 * @date 2018/12/17
 */
public class LoadingDialog extends DialogFragment {
    private View layout;
    private android.widget.TextView mTvDialogMsg;
    private String msg;
    private android.widget.ProgressBar mPbDialog;

    public static LoadingDialog newInstance(String msg) {
        Bundle args = new Bundle();
        args.putString("msg", msg);
        LoadingDialog fragment = new LoadingDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.selectorDialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        layout = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_loading, null);
        initView();
        dialog.setContentView(layout);
        dialog.setCanceledOnTouchOutside(false);

        DoubleBounce threeBounce = new DoubleBounce();
        mPbDialog.setIndeterminateDrawable(threeBounce);
        if (!TextUtils.isEmpty(msg))
            mTvDialogMsg.setText(msg);
        setAttr(dialog);
        return dialog;
    }

    /**
     * 设置属性
     */
    private void setAttr(Dialog dialog) {
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置dialog大小为宽的0.3
        int screenW = (int) (ScreenUtils.getScreenWidth() * 0.3);
        lp.width = screenW;
        lp.height = screenW;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            msg = bundle.getString("msg");
        }
    }

    private void initView() {
        mTvDialogMsg = layout.findViewById(R.id.tv_dialog_msg);
        mPbDialog = layout.findViewById(R.id.pb_dialog);
    }

}
