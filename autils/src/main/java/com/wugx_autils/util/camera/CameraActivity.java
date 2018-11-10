/*
 * Copyright 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wugx_autils.util.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.wugx_autils.R;
import com.wugx_autils.R2;
import com.wugx_autils.util.CameraUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 视频录制
 * <p>
 * Created by chenzhihui on 2016/08/18
 * 由于Camera在SDK 21的版本被标为Deprecated,建议使用新的Camera2类来实现
 * 但是由于Camera2这个类要求minSDK大于21,所以依旧使用Camera这个类进行实现
 */
public class CameraActivity extends AppCompatActivity {

    @BindView(R2.id.ll_camera_layout)
    LinearLayout mLlCameraLayout;
    @BindView(R2.id.img_camera_switch)
    ImageView mImgCameraSwitch;
    @BindView(R2.id.img_capture)
    ImageView mImgCapture;
    @BindView(R2.id.img_flash)
    ImageView mImgFlash;
    @BindView(R2.id.c_count)
    Chronometer mCCount;


    //是否录制中...
    private boolean isRecording;
    private MediaRecorder mMediaRecorder;
    //camera2需要api>21，为兼容老版本，用hardware.Camera
    private Camera mCamera;
    private CameraPreview mCpPreview;
    //对焦点击焦点区域大小
    private static final int FOCUS_AREA_SIZE = 500;
    //摄像头方向
    private static boolean cameraFront = false;
    //闪光灯
    private static boolean isFlash = false;
    //视频文件输出路径
    private String url_file;

    //最大录制时间15秒
    private static final int MAX_RECORD_TIME = 15;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);

        init();
    }

    //检查设备是否有摄像头
    private boolean hasCamera(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

    public void onResume() {
        super.onResume();
        if (!hasCamera(getApplicationContext())) {
            //这台设备没有发现摄像头
            Toast.makeText(getApplicationContext(), R.string.dont_have_camera_error
                    , Toast.LENGTH_SHORT).show();
            setResult(CameraUtils.RESULT_CODE_FOR_RECORD_VIDEO_FAILED);
            releaseMediaRecorder();
            releaseCamera();
            finish();
        }
        if (mCamera == null) {
            releaseCamera();
            final boolean frontal = cameraFront;

            int cameraId = findFrontFacingCamera();
            if (cameraId < 0) {
                //前置摄像头不存在
                LogUtils.e(R.string.dont_have_front_camera);
//                switchCameraListener = new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Toast.makeText(CameraActivity.this, R.string.dont_have_front_camera, Toast.LENGTH_SHORT).show();
//                    }
//                };

                //尝试寻找后置摄像头
                cameraId = findBackFacingCamera();
                if (isFlash) {
                    mCpPreview.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    mImgFlash.setImageResource(R.drawable.ic_flash_on_white);
                }
            } else if (!frontal) {
                cameraId = findBackFacingCamera();
                if (isFlash) {
                    mCpPreview.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    mImgFlash.setImageResource(R.drawable.ic_flash_on_white);
                }
            }

            mCamera = Camera.open(cameraId);
            mCpPreview.refreshCamera(mCamera);
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private void init() {

        mCpPreview = new CameraPreview(this, mCamera);
        mLlCameraLayout.addView(mCpPreview);
        mLlCameraLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    try {
                        focusOnTouch(event);
                    } catch (Exception e) {
                        LogUtils.d(getString(R.string.fail_when_camera_try_autofocus, e.toString()));
                    }
                }
                return true;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isRecording) {
            mMediaRecorder.stop();
            if (mCCount != null && mCCount.isActivated())
                mCCount.stop();
            releaseMediaRecorder();
            isRecording = false;
            File mp4 = new File(url_file);
            if (mp4.exists() && mp4.isFile()) {
                mp4.delete();
            }
        }
        setResult(CameraUtils.RESULT_CODE_FOR_RECORD_VIDEO_CANCEL);
        releaseMediaRecorder();
        releaseCamera();
        finish();
        return super.onKeyDown(keyCode, event);
    }

    private void focusOnTouch(MotionEvent event) {
        if (mCamera != null) {
            Camera.Parameters parameters = mCamera.getParameters();
            if (parameters.getMaxNumMeteringAreas() > 0) {
                Rect rect = calculateFocusArea(event.getX(), event.getY());
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                List<Camera.Area> meteringAreas = new ArrayList<Camera.Area>();
                meteringAreas.add(new Camera.Area(rect, 800));
                parameters.setFocusAreas(meteringAreas);
                mCamera.setParameters(parameters);
                mCamera.autoFocus(mAutoFocusTakePictureCallback);
            } else {
                mCamera.autoFocus(mAutoFocusTakePictureCallback);
            }
        }
    }

    private Rect calculateFocusArea(float x, float y) {
        int left = clamp(Float.valueOf((x / mCpPreview.getWidth()) * 2000 - 1000).intValue(), FOCUS_AREA_SIZE);
        int top = clamp(Float.valueOf((y / mCpPreview.getHeight()) * 2000 - 1000).intValue(), FOCUS_AREA_SIZE);
        return new Rect(left, top, left + FOCUS_AREA_SIZE, top + FOCUS_AREA_SIZE);
    }

    private int clamp(int touchCoordinateInCameraReper, int focusAreaSize) {
        int result;
        if (Math.abs(touchCoordinateInCameraReper) + focusAreaSize / 2 > 1000) {
            if (touchCoordinateInCameraReper > 0) {
                result = 1000 - focusAreaSize / 2;
            } else {
                result = -1000 + focusAreaSize / 2;
            }
        } else {
            result = touchCoordinateInCameraReper - focusAreaSize / 2;
        }
        return result;
    }

    private Camera.AutoFocusCallback mAutoFocusTakePictureCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                // do something...
                Log.i("对焦", "success!");
            } else {
                // do something...
                Log.i("对焦", "fail!");
            }
        }
    };

    @OnClick({R2.id.img_camera_switch, R2.id.img_capture, R2.id.img_flash})
    public void onViewClicked(View view) {
        int i = view.getId();
        if (i == R.id.img_camera_switch) {
            switchCamera();

        } else if (i == R.id.img_capture) {
            startRecord();

        } else if (i == R.id.img_flash) {
            openFlash();

        }
    }

    private void openFlash() {
        if (!isRecording && !cameraFront) {
            if (isFlash) {
                isFlash = false;
                mImgFlash.setImageResource(R.drawable.ic_flash_off_white);
                setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            } else {
                isFlash = true;
                mImgFlash.setImageResource(R.drawable.ic_flash_on_white);
                setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            }
        }
    }

    //闪光灯
    public void setFlashMode(String mode) {
        try {
            if (getPackageManager().hasSystemFeature(
                    PackageManager.FEATURE_CAMERA_FLASH)
                    && mCamera != null
                    && !cameraFront) {

                mCpPreview.setFlashMode(mode);
                mCpPreview.refreshCamera(mCamera);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), R.string.changing_flashLight_mode,
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void switchCamera() {
        if (!isRecording) {
            int camerasNumber = Camera.getNumberOfCameras();
            if (camerasNumber > 1) {
                releaseCamera();
                chooseCamera();
            } else {
                //只有一个摄像头不允许切换
                Toast.makeText(getApplicationContext(), R.string.only_have_one_camera
                        , Toast.LENGTH_SHORT).show();
            }
        }
    }


    //选择摄像头
    public void chooseCamera() {
        if (cameraFront) {
            //当前是前置摄像头
            int cameraId = findBackFacingCamera();
            if (cameraId >= 0) {
                // open the backFacingCamera
                // set a picture callback
                // refresh the preview
                mCamera = Camera.open(cameraId);
                // mPicture = getPictureCallback();
                mCpPreview.refreshCamera(mCamera);
            }
        } else {
            //当前为后置摄像头
            int cameraId = findFrontFacingCamera();
            if (cameraId >= 0) {
                // open the backFacingCamera
                // set a picture callback
                // refresh the preview
                mCamera = Camera.open(cameraId);
                if (isFlash) {
                    isFlash = false;
                    mImgFlash.setImageResource(R.drawable.ic_flash_off_white);
                    mCpPreview.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                }
                // mPicture = getPictureCallback();
                mCpPreview.refreshCamera(mCamera);
            }
        }
    }

    /**
     * 找前置摄像头,没有则返回-1
     *
     * @return cameraId
     */
    private int findFrontFacingCamera() {
        int cameraId = -1;
        //获取摄像头个数
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i;
                cameraFront = true;
                break;
            }
        }
        return cameraId;
    }

    /**
     * 找后置摄像头,没有则返回-1
     *
     * @return cameraId
     */
    private int findBackFacingCamera() {
        int cameraId = -1;
        //获取摄像头个数
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                cameraFront = false;
                break;
            }
        }
        return cameraId;
    }

    private void startRecord() {
        if (isRecording) {
            //如果正在录制点击这个按钮表示录制完成
            stopPlay();
        } else {
            startPlay();
        }
    }

    private void startPlay() {
        //准备开始录制视频
        if (!prepareVideoRecorder()) {
            ToastUtils.showShort(getString(R.string.camera_init_fail));
            setResult(CameraUtils.RESULT_CODE_FOR_RECORD_VIDEO_FAILED);
            releaseMediaRecorder();
            releaseCamera();
            finish();
        }
        //开始录制视频
        runOnUiThread(new Runnable() {
            public void run() {
                // If there are stories, add them to the table
                try {
                    mMediaRecorder.start();
                    startChronometer();
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        changeRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    } else {
                        changeRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    }
                    mImgCapture.setImageResource(R.drawable.player_stop);
                } catch (final Exception ex) {
                    Log.i("---", "Exception in thread");
                    setResult(CameraUtils.RESULT_CODE_FOR_RECORD_VIDEO_FAILED);
                    releaseMediaRecorder();
                    releaseCamera();
                    finish();
                }
            }
        });
        isRecording = true;
    }

    private void stopPlay() {
        mMediaRecorder.stop(); //停止
        stopChronometer();
        mImgCapture.setImageResource(R.drawable.player_record);
        changeRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        ToastUtils.showShort(R.string.video_captured);
        isRecording = false;

        releaseMediaRecorder();
        releaseCamera();
        //setResult
        Intent intent = new Intent();
        intent.putExtra(CameraUtils.INTENT_EXTRA_VIDEO_PATH, url_file);
        setResult(CameraUtils.RESULT_CODE_FOR_RECORD_VIDEO_SUCCEED, intent);
        finish();
    }

    private boolean prepareVideoRecorder() {
        mMediaRecorder = new MediaRecorder();

        // Step 1: Unlock and set camera to MediaRecorder
        mCamera.stopPreview();
        mCamera.unlock();
        mMediaRecorder.setCamera(mCamera);

        // Step 2: Set sources
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

//        mMediaRecorder.setVideoEncodingBitRate(5 * 1024 * 1024); //设置编码比特率即可

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (cameraFront) {
                mMediaRecorder.setOrientationHint(270);
            } else {
                mMediaRecorder.setOrientationHint(90);
            }
        }

        // Step 3: Set output format and encoding (for versions prior to API Level 8)
        CamcorderProfile camcorderProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
        camcorderProfile.videoFrameWidth = 320;
        camcorderProfile.videoFrameHeight = 240;

//        camcorderProfile.videoFrameWidth = 640;
//        camcorderProfile.videoFrameHeight = 480;
//      camcorderProfile.videoFrameRate = 15;
        camcorderProfile.videoCodec = MediaRecorder.VideoEncoder.H264;
//        camcorderProfile.videoBitRate=5 * 1024 * 1024;
//        camcorderProfile.audioCodec = MediaRecorder.AudioEncoder.AAC;
        camcorderProfile.fileFormat = MediaRecorder.OutputFormat.MPEG_4;

        mMediaRecorder.setProfile(camcorderProfile);
//        mediaRecorder.setVideoSize(640, 480);

        // Step 4: Set output file
        if (getVideoFilePath()) return false;
        mMediaRecorder.setOutputFile(url_file);

        // Step 5: Set the preview output
        mMediaRecorder.setPreviewDisplay(mCpPreview.getHolder().getSurface());

        // Step 6: Prepare configured MediaRecorder
        mMediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
            @Override
            public void onError(MediaRecorder mr, int what, int extra) {
                LogUtils.d("录像异常>>>", what, extra, mr.toString());
            }
        });
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            LogUtils.d("DEBUG", "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            LogUtils.d("DEBUG", "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }

    private boolean getVideoFilePath() {
        String phonePath;
        if (isSDCardEnableByEnvironment()) {
            phonePath = getSDCardPathByEnvironment();
        } else {
            phonePath = PathUtils.getDataPath();
        }
        File dirFile = new File(phonePath, "ranch_video");
        if (!FileUtils.createOrExistsDir(dirFile)) {
            ToastUtils.showShort("创建视频路径失败，请检查是否授予文件存储权限");
            LogUtils.d("创建视频路径失败，请检查是否授予文件存储权限");
            return true;
        }

        File outVideoFile = new File(dirFile, "outVideo.mp4");
        if (!FileUtils.createFileByDeleteOldFile(outVideoFile)) {
            LogUtils.d("创建视频文件失败");
            return true;
        }
        url_file = outVideoFile.getAbsolutePath();
        return false;
    }

    public static boolean isSDCardEnableByEnvironment() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * Return the path of sdcard by environment.
     *
     * @return the path of sdcard by environment
     */
    public static String getSDCardPathByEnvironment() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return "";
    }


    private void releaseMediaRecorder() {
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            mCamera.lock();
        }
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }


    //计时器
    private void startChronometer() {
        mCCount.setVisibility(View.VISIBLE);
        final long startTime = SystemClock.elapsedRealtime();
        mCCount.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer arg0) {
                long countUp = (SystemClock.elapsedRealtime() - startTime) / 1000;
//                if (countUp % 2 == 0) {
//                    mChronoRecordingImage.setVisibility(View.VISIBLE);
//                } else {
//                    mChronoRecordingImage.setVisibility(View.INVISIBLE);
//                }
//                LogUtils.d("拍摄时间>>" + countUp);
                if (countUp >= MAX_RECORD_TIME) {
                    //拍摄时间超时，退出
                    stopPlay();
                }
                String asText = String.format("%02d", countUp / 60) + ":" + String.format("%02d", countUp % 60);
                mCCount.setText(asText);
            }
        });
        mCCount.start();
    }

    private void stopChronometer() {
        mCCount.stop();
        mCCount.setVisibility(View.INVISIBLE);
    }

    /**
     * 改变方向
     *
     * @param orientation
     */
    private void changeRequestedOrientation(int orientation) {
        setRequestedOrientation(orientation);
    }

}

