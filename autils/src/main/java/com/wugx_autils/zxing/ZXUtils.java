package com.wugx_autils.zxing;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.uuzuche.lib_zxing.DisplayUtil;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.wugx_autils.R;
import com.wugx_autils.util.PermissionApply;


/**
 * 二维码扫描工具类
 * <p>
 * <p>
 * (1.注意添加相机权限
 * 2.在onActivityResult中调用{@link #onResult(int, Intent, ZXListener)})
 *
 * @author wugx
 * @data 2018/1/5.
 */
public class ZXUtils {
    /**
     * 扫描跳转Activity RequestCode
     */
    public static final int REQUEST_CODE = 111;
    /**
     * 选择系统图片Request Code
     */
    public static final int REQUEST_IMAGE = 112;

    @SuppressLint("WrongConstant")
    public static void ScanCode(final Activity context, final int... args) {
        PermissionApply.request(PermissionApply.CAMERA_PERMISSION, "扫描二维码", new PermissionApply.PermissionListener() {
            @Override
            public void success() {
                if (args.length <= 0) {
                    initDisplayOpinion();
                    //默认扫描界面
                    Intent intent = new Intent(Utils.getApp(), CaptureActivity.class);
                    context.startActivityForResult(intent, REQUEST_CODE);
                } else {
                    //定制扫描界面
                    Intent intent = new Intent(Utils.getApp(), CustomScanActivity.class);
                    context.startActivityForResult(intent, REQUEST_CODE);
                }
            }
        });
    }


    /**
     * 默认二维码扫描 必要的初始化
     */
    private static void initDisplayOpinion() {
        DisplayMetrics dm = Utils.getApp().getResources().getDisplayMetrics();
        DisplayUtil.density = dm.density;
        DisplayUtil.densityDPI = dm.densityDpi;
        DisplayUtil.screenWidthPx = dm.widthPixels;
        DisplayUtil.screenhightPx = dm.heightPixels;
        DisplayUtil.screenWidthDip = DisplayUtil.px2dip(Utils.getApp(), dm.widthPixels);
        DisplayUtil.screenHightDip = DisplayUtil.px2dip(Utils.getApp(), dm.heightPixels);
    }

    /**
     * 从图库选择二维码图片进行扫描
     *
     * @param context
     */
    public static void SelectImgCode(Activity context) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        context.startActivityForResult(intent, REQUEST_IMAGE);
    }

    /**
     * * 生成二维码
     *
     * @param content 要生成的内容
     * @param isLogo  中间是否带logo
     * @return
     */
    public static Bitmap createCode(String content, boolean isLogo) {
//        Bitmap bitmap = CodeUtils.createImage(content, 400, 400, isLogo ? BitmapFactory.decodeResource(Utils.getApp().getResources(), R.mipmap.ic_launcher) : null);

        Bitmap bitmap = CodeUtils.createImage(content, 400, 400, null);
        return bitmap;
    }


    public static void onResult(int requestCode, Intent data, final ZXListener zxListener) {
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    if (zxListener != null) {
                        zxListener.scanResult(result);
                    }
                    LogUtils.d("解析结果:" + result);
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    ToastUtils.showShort(R.string.txt_qcode_resolve_faile);
                }
            }
        }
        /**
         * 选择系统图片并解析
         */
        else if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    CodeUtils.analyzeBitmap(ImagePathUtil.getImageAbsolutePath(Utils.getApp(), uri),
                            new CodeUtils.AnalyzeCallback() {
                                @Override
                                public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                                    LogUtils.d("解析结果:" + result);
                                    if (zxListener != null) {
                                        zxListener.selectResult(mBitmap, result);
                                    }
                                }

                                @Override
                                public void onAnalyzeFailed() {
                                    LogUtils.d("解析二维码失败");
                                    ToastUtils.showShort(R.string.txt_qcode_resolve_faile);
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        /* else if (requestCode == REQUEST_CAMERA_PERM) {
            Toast.makeText(this, "从设置页面返回...", Toast.LENGTH_SHORT)
                    .show();
        }*/
    }

    public interface ZXListener {
        /**
         * 扫描二维码结果
         *
         * @param result
         */
        void scanResult(String result);

        /**
         * 选择图片二维码结果
         *
         * @param mBitmap
         * @param result
         */
        void selectResult(Bitmap mBitmap, String result);
    }
}
