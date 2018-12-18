package com.wugx_autils.custom;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.StringUtils;
import com.wugx_autils.R;

/**
 * title msg next cancel 基本样式的dialog
 *
 * @author Wugx
 * @date 2018/12/18
 */
public class BaseDialog extends DialogFragment {
    private String dialogTitle, dialogContent, dialogNext, dialogCancel;
    private android.support.v7.widget.AppCompatTextView mTvDialogBaseTitle;
    private android.support.v7.widget.AppCompatTextView mTvDialogBaseContent;
    private android.support.v7.widget.AppCompatButton mBtnDialogBaseNext;
    private android.support.v7.widget.AppCompatButton mBtnDialogBaseCancel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            dialogTitle = bundle.getString("base_dialog_title");
            dialogContent = bundle.getString("base_dialog_msg");
            dialogNext = bundle.getString("base_dialog_next");
            dialogCancel = bundle.getString("base_dialog_cancel");
        }
    }

    public static BaseDialog newInstance(String title, String msg, String next, String cancel) {
        Bundle args = new Bundle();
        args.putString("base_dialog_title", title);
        args.putString("base_dialog_msg", msg);
        args.putString("base_dialog_next", next);
        args.putString("base_dialog_cancel", cancel);
        BaseDialog fragment = new BaseDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.style_dialog_base);
        View layoutView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_base, null);
        initView(layoutView);
        initData();
        dialog.setContentView(layoutView);
        setAttr(dialog);
        initListener();
        return dialog;
    }

    private void initData() {
        if (!StringUtils.isEmpty(dialogTitle)) {
            mTvDialogBaseTitle.setText(dialogTitle);
        }
        if (!StringUtils.isEmpty(dialogContent)) {
            mTvDialogBaseContent.setText(dialogContent);
        }
        if (!StringUtils.isEmpty(dialogNext)) {
            mBtnDialogBaseNext.setText(dialogNext);
        }
        if (!StringUtils.isEmpty(dialogCancel)) {
            mBtnDialogBaseCancel.setText(dialogCancel);
        }
    }

    private void initListener() {
        mBtnDialogBaseNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.next();
                dismiss();
            }
        });
        mBtnDialogBaseCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) mListener.cancel();
                dismiss();
            }
        });
    }

    private void setAttr(Dialog dialog) {
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.dialog_anim_zoom);
        WindowManager.LayoutParams lp = window.getAttributes();
        //设置dialog大小为宽的0.3
        int screenW = (int) (ScreenUtils.getScreenWidth() * 0.75);
        lp.width = screenW;
//        lp.height = screenW;
    }

    private DialogBaseListener mListener;

    public BaseDialog setListener(DialogBaseListener listener) {
        mListener = listener;
        return this;
    }

    private void initView(View v) {
        mTvDialogBaseTitle = v.findViewById(R.id.tv_dialog_base_title);
        mTvDialogBaseContent = v.findViewById(R.id.tv_dialog_base_content);
        mBtnDialogBaseNext = v.findViewById(R.id.btn_dialog_base_next);
        mBtnDialogBaseCancel = v.findViewById(R.id.btn_dialog_base_cancel);
    }

    public interface DialogBaseListener {
        void next();

        void cancel();
    }

}
