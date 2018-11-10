package com.wugx_autils.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.wugx_autils.R;

import java.util.List;

/**
 * 权限申请
 *
 * @author Wugx
 * @date 2018/11/8
 */
public class PermissionApply {
    /**
     * 相机拍照权限
     */
    public static final String[] CAMERA_PERMISSION = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    /**
     * 录像权限
     */
    public static final String[] AUDIO_PERMISSION = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
    };

    public static final String[] FILE_PERMISSION = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };


    @SuppressLint("WrongConstant")
    public static void request(String[] perms, final String permsDesc, final PermissionListener listener) {
        PermissionUtils.permission(perms)
                .rationale(new PermissionUtils.OnRationaleListener() {
                    @Override
                    public void rationale(ShouldRequest shouldRequest) {
                        shouldRequest.again(true);
                    }
                })
                .callback(new PermissionUtils.FullCallback() {
                    @Override
                    public void onGranted(List<String> permissionsGranted) {
                        listener.success();
                    }

                    @Override
                    public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                        if (permissionsDenied != null && permissionsDenied.size() > 0) {
                            ToastUtils.showShort(String.format(Utils.getApp().getString(R.string.permission_denied_tips), permsDesc));
                        }
                        if (permissionsDeniedForever != null && permissionsDeniedForever.size() > 0) {
                            Activity topActivity = ActivityUtils.getTopActivity();
                            if (topActivity != null) {
                                new MaterialDialog.Builder(topActivity)
                                        .content(String.format(Utils.getApp().getString(R.string.permission_deniedforever_tips), permsDesc))
                                        .contentColor(Utils.getApp().getResources().getColor(android.R.color.holo_red_dark))
                                        .canceledOnTouchOutside(false)
                                        .negativeText(R.string.cancel)
                                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            }
                                        })
                                        .positiveText(R.string.txt_set)
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                PermissionUtils.launchAppDetailsSettings();
                                            }
                                        })
                                        .show();
                            }
                        }

                    }
                })
                .request();
    }

    public interface PermissionListener {
        void success();
    }

    /**
     * back intent
     */
    public interface CameraPermissionListener {
        void success(Intent intent);
    }

}
