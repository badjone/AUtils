package com.wugx_utils;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.folderselector.FileChooserDialog;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.wugx_autils.base.BaseActivity;
import com.wugx_autils.mvp.presenter.BasePresenter;
import com.wugx_autils.util.CameraUtils;
import com.wugx_autils.util.PermissionApply;
import com.wugx_utils.entity.BasicBean;
import com.wugx_utils.http.UploadFileUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends BaseActivity implements FileChooserDialog.FileCallback {

    private static final String TAG = "主页>>";
    private Button mBtnCamera;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        initView();
        mBtnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ZXUtils.ScanCode(MainActivity.this);
//
//                CameraUtils.takePhoto(new CameraUtils.CameraPermissionListener() {
//                    @Override
//                    public void success(Intent intent) {
//                        startActivityForResult(intent, 3);
//                    }
//                });


//                CameraUtils.takeVideo2(new CameraUtils.CameraPermissionListener() {
//                    @Override
//                    public void success(Intent intent) {
//                        startActivityForResult(intent, 2);
//                    }
//                });

//                ActivityUtils.startActivity(TestActivity.class);


//                startActivityForResult(CameraUtils.toGallery(), 6);


//                new FileChooserDialog.Builder(MainActivity.this)
//                        .initialPath("/sdcard/Download")  // changes initial path, defaults to external storage directory
//                        .mimeType("image/*") // Optional MIME type filter
//                        .extensionsFilter(".png", ".jpg") // Optional extension filter, will override mimeType()
//                        .tag("optional-identifier")
//                        .goUpLabel("上一级") // custom go up label, default label is "..."
//                        .show(MainActivity.this); // an AppCompatActivity which implements FileCallback


                CameraUtils.toGallery(new PermissionApply.CameraPermissionListener() {
                    @Override
                    public void success(Intent intent) {
                        startActivityForResult(intent, 7);
                    }
                });
//获取父目录  


//                http:
////218.240.149.148:10100/userMember/headImgUrl.do
//
//                token = ad0619c9 - 9e03 - 42d 6 - 8 af9 - fcb77c1824d7 memberId = 16
//                deviceId = c4 % 3 A0b % 3 Acb % 3 A84 % 3 A97 % 3 A70


            }
        });
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 2) {
//            CameraUtils.getVideoDataResult(resultCode, data, new CameraUtils.VideoDataListener() {
//                @Override
//                public void success(double videoLength, String videoSavePath) {
//                    Log.i(TAG, "success: 视频录制>>" + videoSavePath);
//                }
//
//                @Override
//                public void failure(String msg) {
//                    Log.i(TAG, "success: 视频录制失败>>");
//                }
//            });
//        } else if (requestCode == 3) {
//            String takePhoto = CameraUtils.getTakePhoto();
//            LogUtils.d("拍摄的照片路径>>" + takePhoto);
//        }

//        ZXUtils.onResult(requestCode, data, new ZXUtils.ZXListener() {
//            @Override
//            public void scanResult(String result) {
//                System.out.println("扫描成功>>" + result);
//            }
//
//            @Override
//            public void selectResult(Bitmap mBitmap, String result) {
//                System.out.println("选择");
//            }
//        });

        if (requestCode == 6) {
            String galleryPath = CameraUtils.getGalleryPath(data);
            LogUtils.d("选择文件", data, galleryPath);


            String url = "userMember/headImgUrl.do";

            Map<String, String> map = new HashMap<>();
            map.put("token", "ad0619c9-9e03-42d6-8af9-fcb77c1824d7");
            map.put("memberId", "16");
            map.put("deviceId", "c4:0b:cb:84:97:70");

            if (!TextUtils.isEmpty(galleryPath)) {
                UploadFileUtils.uploadFile(this, url, "uploadFile", new File(galleryPath), map,

//                        new UploadFileUtils.UpFileProgressListener() {
//                    @Override
//                    public void progress(int progress) {
//                    }
//
//                    @Override
//                    public void failure() {
//                        LogUtils.d("上传失败");
//                    }
//
//                    @Override
//                    public void success() {
//
//                        LogUtils.d("上传成功");
//                    }
//                }

                        new UploadFileUtils.UpFileProgressListener<BasicBean>() {
                            @Override
                            public void progress(int progress) {

                            }

                            @Override
                            public void failure() {

                            }

                            @Override
                            public void success(BasicBean o) {
                                LogUtils.d("上传成功" + o.toString());
                            }
                        }

                );
            }

        } else if (requestCode == 7) {
            LogUtils.d("获取路径>>>" + data);

            if (data != null) {
                String filPathLocal = CameraUtils.getGalleryPath(data);
                LogUtils.d("获取到文件路径>>>", FileUtils.isFileExists(filPathLocal), filPathLocal);
            }
        }
    }

    private void initView() {
        mBtnCamera = findViewById(R.id.btn_camera);
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void onFileSelection(@NonNull FileChooserDialog dialog, @NonNull File file) {
        final String tag = dialog.getTag(); // gets tag set from Builder, if you use multiple dialogs
        LogUtils.d("dialog选择文件", FileUtils.isFileExists(file), file.getAbsolutePath());
    }

    @Override
    public void onFileChooserDismissed(@NonNull FileChooserDialog dialog) {

    }
}
