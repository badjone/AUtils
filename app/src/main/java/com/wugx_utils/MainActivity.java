package com.wugx_utils;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.afollestad.materialdialogs.folderselector.FileChooserDialog;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.wugx_autils.base.BaseActivity;
import com.wugx_autils.http.net.common.Constants;
import com.wugx_autils.http.net.common.DownloadUtils;
import com.wugx_autils.http.net.common.UploadFileUtils;
import com.wugx_autils.http.net.download.DownloadListener;
import com.wugx_autils.http.net.exception.ServerResponseException;
import com.wugx_autils.mvp.presenter.BasePresenter;
import com.wugx_autils.util.CameraUtils;
import com.wugx_utils.db.DaoManager;
import com.wugx_utils.db.UserData;
import com.wugx_utils.entity.BasicBean;
import com.wugx_utils.entity.LoginBean;
import com.wugx_utils.entity.UserInfo;
import com.wugx_utils.http.RetrofitHelper;
import com.wugx_utils.http.converter.GsonConverterFactory;
import com.wugx_utils.test.TestActivity2;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

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

                ActivityUtils.startActivity(TestActivity2.class);


//                startActivityForResult(CameraUtils.toGallery(), 6);


//                new FileChooserDialog.Builder(MainActivity.this)
//                        .initialPath("/sdcard/Download")  // changes initial path, defaults to external storage directory
//                        .mimeType("image/*") // Optional MIME type filter
//                        .extensionsFilter(".png", ".jpg") // Optional extension filter, will override mimeType()
//                        .tag("optional-identifier")
//                        .goUpLabel("上一级") // custom go up label, default label is "..."
//                        .show(MainActivity.this); // an AppCompatActivity which implements FileCallback


//                CameraUtils.toGallery(new PermissionApply.CameraPermissionListener() {
//                    @Override
//                    public void success(Intent intent) {
//                        startActivityForResult(intent, 7);
//                    }
//                });

                //测试下载
//                downLoadApk();
//                testlogin();
//                initDb();

//                testRxjava();


            }
        });
    }

    private void testRxjava() {
        int[] i = {8, 0, 1, 5};
        int[] i1 = {5, 3, 9, 8};

        Observable<int[]> o1 = Observable.fromArray(i1).take(2);
        Observable<int[]> o2 = Observable.fromArray(i);
//                .subscribe(new Consumer<Integer>() {
//                    @Override
//                    public void accept(Serializable serializable) throws Exception {
//                        LogUtils.d(">>>" + serializable);
//                    }
//                });


    }

    private void initDb() {
//        DaoManager.getInstance().getDaoSession().getTestBeanDao().deleteAll();
//
////                testlogin();
//        for (int i = 0; i < 5; i++) {
//            TestBean testBean = new TestBean();
////                    testBean.id=(long)i;
//            testBean.name = "名称:" + i;
//            testBean.count = i;
//            DaoManager.getInstance().getDaoSession().getTestBeanDao().insertInTx(testBean);
//        }
//        List<TestBean> list = DaoManager.getInstance().getDaoSession()
//                .queryBuilder(TestBean.class)
//                .where(TestBeanDao.Properties.Count.gt(2))
//                .list();
//
//        LogUtils.d("查询数据库>>>" + list.toString());


//        DaoManager.getInstance().getDaoSession().getUserDataDao().deleteAll();
        for (int i = 0; i < 5; i++) {
            UserData userData = new UserData();
//            userData.id = Long.valueOf(i);
            userData.userId = "ID--" + i + 10;
            userData.userToken = i;
            DaoManager.getInstance().getDaoSession().getUserDataDao().insertOrReplace(userData);
        }
        List<UserData> list = DaoManager.getInstance().getDaoSession()
                .queryBuilder(UserData.class).list();

        LogUtils.d(">>>" + list.toString());
    }

    /**
     * 测试登陆
     */
    private void testlogin() {
        //牧场登陆测试...
        String url = "mobleClaim/login.do";
        Map<String, String> map = new HashMap<>();
        map.put("cellPhoneNum", "18219160104");
        map.put("password", "123456");
        map.put("deviceId", "c4:0b:cb:84:97:70");
        map.put("registrationId", "140fe1da9e80bacb3a2");

        //登陆嵌套获取用户信息
        RetrofitHelper.getApiService(null)
                .toLogin(url, map)
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<LoginBean, ObservableSource<UserInfo>>() {
                    @Override
                    public ObservableSource<UserInfo> apply(LoginBean loginBean) throws Exception {
                        LogUtils.d("登陆成功>>>" + loginBean.toString());
                        if (!TextUtils.isEmpty(loginBean.getToken())) {
                            //登陆成功
                            return RetrofitHelper.getApiService().getUserMember(
                                    loginBean.getToken(), loginBean.getMemberId(), "c4:0b:cb:84:97:70");
                        } else {
                            //登陆失败
                            throw new ServerResponseException("登陆异常", loginBean.getMsg());
                        }
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UserInfo userInfo) {
                        LogUtils.d("获取到用户信息>>" + userInfo.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        LogUtils.d("onError>>" + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void downLoadApk() {
        String url = "https://raw.githubusercontent.com/badjone/CallApp/master/wifi_call_1.0.0.4.apk";

        new DownloadUtils().download(Constants.BASE_URL, url, new DownloadListener() {
            @Override
            public void onProgress(int progress) {
                LogUtils.d("onProgress>>" + progress);
                mBtnCamera.setText("" + progress);
            }

            @Override
            public void onSuccess(File file) {
                LogUtils.d("onSuccess>>", FileUtils.isFileExists(file), FileUtils.getFileSize(file));
                ToastUtils.showShort("下载完成拉>>>" + file.getAbsolutePath());

            }

            @Override
            public void onFail(String message) {
                LogUtils.d("onFail>>" + message);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void upload(File file) {
        String uploadUrl = "http://116.95.255.211:10100/userMember/headImgUrl.do";
        Map<String, String> map = new HashMap<>();
        String token = "542d06a5-52c4-489b-b056-31e8a3a66bc3";
        String memberId = "14";
        String deviceId = "c4:0b:cb:84:97:70";
        map.put("token", token);
        map.put("memberId", memberId);
        map.put("deviceId", deviceId);

        UploadFileUtils.uploadFile(
                this,
                uploadUrl,
                "uploadFile",
                file,
                map,
                GsonConverterFactory.create(),
                new UploadFileUtils.UpFileProgressListener() {
                    @Override
                    public void progress(int progress) {
                        mBtnCamera.setText("" + progress);
                    }

                    @Override
                    public void failure() {
                        LogUtils.d("上传失败>>>");
                        mBtnCamera.setText("failure");
                    }

                    @Override
                    public void success(Object o) {
                        if (o instanceof BasicBean) {
                            BasicBean basicBean = (BasicBean) o;
                            LogUtils.d("上传成功888>>>" + basicBean.msg);
                        }

                        LogUtils.d("上传成功>>>" + o);
                        mBtnCamera.setText("成功" + o);
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
                UploadFileUtils.uploadFile(this, url, "uploadFile", new File(galleryPath), map, GsonConverterFactory.create(),

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

                upload(new File(filPathLocal));
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
