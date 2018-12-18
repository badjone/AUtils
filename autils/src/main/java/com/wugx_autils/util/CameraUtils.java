package com.wugx_autils.util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.PathUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.UriUtils;
import com.blankj.utilcode.util.Utils;
import com.wugx_autils.util.camera.CameraActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

/**
 * 相机，拍照，摄像,相册
 * <p>
 * 注意：7.0以上权限
 *
 * @author Wugx
 * @date 2018/10/10
 */

public class CameraUtils {
    private static final String SAVETAKEPHOTO = "save_take_photo";
    private static final String SAVECROPPHOTO = "save_crop_photo";

    public static final int MAX_VIDEO_TIME_LENGTH = 15;

    /**
     * 拍照
     *
     * @return
     */
    public static void takePhoto(final PermissionApply.CameraPermissionListener listener) {
        PermissionApply.request(PermissionApply.CAMERA_PERMISSION, "拍照", new PermissionApply.PermissionListener() {
            @Override
            public void success() {
                File file = getOutputMediaFileNew(1);
                SPUtils.getInstance().put(SAVETAKEPHOTO, file.getAbsolutePath());

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, file2Uri(file));
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                listener.success(intent);
            }
        });
    }

    /**
     * onActivityResult中使用
     * 获取拍照完文件路径
     *
     * @return 照片文件路径
     */
    public static String getTakePhoto() {
        return SPUtils.getInstance().getString(SAVETAKEPHOTO);
    }

    /**
     * 录视频
     */
    public static void takeVideo(final PermissionApply.CameraPermissionListener listener) {
        PermissionApply.request(PermissionApply.AUDIO_PERMISSION, "录视频", new PermissionApply.PermissionListener() {
            @Override
            public void success() {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);//Intent action type for requesting a video from an existing camera application.
                //设置视频录制的最长时间
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, MAX_VIDEO_TIME_LENGTH);//限制录制时间(10秒=10)
                Uri fileUri = Uri.fromFile(getOutputMediaFileNew(2));
                //        Uri fileUri = getOutputMediaFileUri();  // create a file to save the video

                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);  // set the image file name
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high
                listener.success(intent);
            }
        });

    }

    /**
     * 使用原生相机录像
     * <p>
     * <p>
     * onActivityResult中使用
     * 获取视频文件路径
     *
     * @param data onActivityResult 的intent
     * @return 视频文件path
     */
    public static String getTakeVedio(Intent data) {
        if (data == null || data.getData() == null) {
            ToastUtils.showShort("视频文件不存在");
            return null;
        }

        String path = getPath(data.getData());
        File file = new File(path);
        //拍摄后的video文件
//        File file = UriUtils.uri2File(data.getData(), "file");
        if (!file.exists()) {
            ToastUtils.showShort("视频文件不存在");
            return null;
        }
        return file.getAbsolutePath();
    }

    /**
     * @param type 1图片 2视频
     * @return
     */
    private static File getOutputMediaFileNew(int type) {
        File vedioPath = null;
        String dirPath;
        if (StringUtils.isEmpty(PathUtils.getExternalDcimPath())) {
            dirPath = PathUtils.getDataPath();
        } else {
            dirPath = PathUtils.getExternalDcimPath();
        }
        File dirFile = new File(dirPath, type == 1 ? "RancherVideo" : "RancherImage");
        if (dirFile.exists()) {
            FileUtils.deleteAllInDir(dirFile);
        }

        boolean orExistsDir = FileUtils.createOrExistsDir(dirFile);
        if (orExistsDir) {
            @SuppressLint("SimpleDateFormat")
            String dateTimes = TimeUtils.date2String(new Date(), new
                    SimpleDateFormat("yyyyMMdd_HHmmss"));
            vedioPath = new File(dirFile.getAbsolutePath(), (type == 1 ? "IMG_" : "VID_") + dateTimes + (type == 1 ? ".jpg" : ".mp4"));
//            saveVedioFile = vedioPath;
        }
        return vedioPath;
    }

    /**
     * 裁剪图片
     */
    public static Intent crop(File file) {
        File CropPhoto = new File(PathUtils.getExternalDcimPath(), "ranchImgCrop.jpg");//这个是创建一个截取后的图片路径和名称。
        Intent intent = new Intent("com.android.camera.action.CROP");
        Uri outPutUri = Uri.fromFile(CropPhoto);
        //sdk>=24
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setDataAndType(file2Uri(file), "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
            intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            intent.setDataAndType(file2Uri(file), "image/*");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
        }
        SPUtils.getInstance().put(SAVECROPPHOTO, getPath(outPutUri));

        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 400);
        intent.putExtra("return-data", false);
        intent.putExtra("noFaceDetection", false);//去除默认的人脸识别，否则和剪裁匡重叠
        intent.putExtra("outputFormat", "JPEG");
        //intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());// 图片格式

        return intent;
    }

    /**
     * onActivityResult中使用
     * 获取裁剪照片路径
     *
     * @return 裁剪文件路径
     */
    public static String getCropPath() {
        return SPUtils.getInstance().getString(SAVECROPPHOTO);
    }


    /**
     * onActivityResult中使用
     * 获取选择照片的 Intent
     *
     * @return
     */
    public static void toGallery(final PermissionApply.CameraPermissionListener listener) {
        PermissionApply.request(PermissionApply.FILE_PERMISSION, "文件", new PermissionApply.PermissionListener() {
            @Override
            public void success() {
                //默认打开系统目录
                File parentFlie = new File(PathUtils.getRootPath());
                Intent intentNew = new Intent(Intent.ACTION_GET_CONTENT);
                intentNew.setDataAndType(UriUtils.file2Uri(parentFlie), "image/*");
                intentNew.addCategory(Intent.CATEGORY_OPENABLE);
                listener.success(intentNew);
            }
        });

//        return new Intent(
//                Intent.ACTION_PICK,
//                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }

    /**
     * 从相册选择图片的路径
     *
     * @param data
     * @return
     */
    public static String getGalleryPath(Intent data) {
        if (data == null) return null;
        return getPath(data.getData());
    }

    /**
     * 获取从文件中选择照片的 Intent
     *
     * @return
     */
    public static Intent getPickIntentWithDocuments() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        return intent.setType("image*//*");
    }


    @SuppressLint("NewApi")
    private static String getPath(final Uri uri) {
        Context context = Utils.getApp();
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;

    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;

    }

    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static Uri file2Uri(@NonNull final File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String authority = AppUtils.getAppPackageName() + ".fileProvider";
            return FileProvider.getUriForFile(Utils.getApp(), authority, file);
        } else {
            return Uri.fromFile(file);
        }
    }

    /**
     * 根据文件类型打开文件
     *
     * @param file
     * @return
     */
    public static Intent openFile(File file) {
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //设置intent的Action属性
            intent.setAction(Intent.ACTION_VIEW);
            //获取文件file的MIME类型
            String type = getMIMEType(file);
            //设置intent的data和Type属性。
//            intent.setDataAndType(/*uri*/Uri.fromFile(file), type);
            Uri uriForFile;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uriForFile = FileProvider.getUriForFile(Utils.getApp(),
                        "com.jinkun_innovation.pastureland.fileProvider", file);
            } else {
                uriForFile = Uri.fromFile(file);
            }
            intent.setDataAndType(uriForFile, type);
            //跳转
            return intent;
        } catch (Exception e) {
            ToastUtils.showShort("不能打开文件");
            return null;
        }
    }

    private String getFileSize(String path) {
        File f = new File(path);
        if (!f.exists()) {
            return "0 MB";
        } else {
            long size = f.length();
            return (size / 1024f) / 1024f + "MB";
        }
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file
     */
    private static String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        /* 获取文件的后缀名*/
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) {
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    private static final String[][] MIME_MapTable = {
            //{后缀名，MIME类型}
            {".3gp", "video/3gpp"},
            {".apk", "application/vnd.android.package-archive"},
            {".asf", "video/x-ms-asf"},
            {".avi", "video/x-msvideo"},
            {".bin", "application/octet-stream"},
            {".bmp", "image/bmp"},
            {".c", "text/plain"},
            {".class", "application/octet-stream"},
            {".conf", "text/plain"},
            {".cpp", "text/plain"},
            {".doc", "application/msword"},
            {".docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe", "application/octet-stream"},
            {".gif", "image/gif"},
            {".gtar", "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h", "text/plain"},
            {".htm", "text/html"},
            {".html", "text/html"},
            {".jar", "application/java-archive"},
            {".java", "text/plain"},
            {".jpeg", "image/jpeg"},
            {".jpg", "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log", "text/plain"},
            {".m3u", "audio/x-mpegurl"},
            {".m4a", "audio/mp4a-latm"},
            {".m4b", "audio/mp4a-latm"},
            {".m4p", "audio/mp4a-latm"},
            {".m4u", "video/vnd.mpegurl"},
            {".m4v", "video/x-m4v"},
            {".mov", "video/quicktime"},
            {".mp2", "audio/x-mpeg"},
            {".mp3", "audio/x-mpeg"},
            {".mp4", "video/mp4"},
            {".mpc", "application/vnd.mpohun.certificate"},
            {".mpe", "video/mpeg"},
            {".mpeg", "video/mpeg"},
            {".mpg", "video/mpeg"},
            {".mpg4", "video/mp4"},
            {".mpga", "audio/mpeg"},
            {".msg", "application/vnd.ms-outlook"},
            {".ogg", "audio/ogg"},
            {".pdf", "application/pdf"},
            {".png", "image/png"},
            {".pps", "application/vnd.ms-powerpoint"},
            {".ppt", "application/vnd.ms-powerpoint"},
            {".pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop", "text/plain"},
            {".rc", "text/plain"},
            {".rmvb", "audio/x-pn-realaudio"},
            {".rtf", "application/rtf"},
            {".sh", "text/plain"},
            {".tar", "application/x-tar"},
            {".tgz", "application/x-compressed"},
            {".txt", "text/plain"},
            {".wav", "audio/x-wav"},
            {".wma", "audio/x-ms-wma"},
            {".wmv", "audio/x-ms-wmv"},
            {".wps", "application/vnd.ms-works"},
            {".xml", "text/plain"},
            {".z", "application/x-compress"},
            {".zip", "application/x-zip-compressed"},
            {"", "*/*"}
    };


    private static final int REQUEST_CODE_FOR_PERMISSIONS = 0;//

    private static final int REQUEST_CODE_FOR_RECORD_VIDEO = 1;//录制视频请求码
    public static final int RESULT_CODE_FOR_RECORD_VIDEO_SUCCEED = 2;//视频录制成功
    public static final int RESULT_CODE_FOR_RECORD_VIDEO_FAILED = 3;//视频录制出错
    public static final int RESULT_CODE_FOR_RECORD_VIDEO_CANCEL = 4;//取消录制
    public static final String INTENT_EXTRA_VIDEO_PATH = "intent_extra_video_path";//录制的视频路径

    /**
     * 自定义视频录制
     *
     * @return
     */
    public static void takeVideo2(final PermissionApply.CameraPermissionListener listener) {
        PermissionApply.request(PermissionApply.AUDIO_PERMISSION, "录像", new PermissionApply.PermissionListener() {
            @Override
            public void success() {
                if (listener != null)
                    listener.success(new Intent(Utils.getApp(), CameraActivity.class));
            }
        });
    }

    /**
     * 对应 takeVideo2
     *
     * @param resultCode
     * @param data
     * @param listener
     */
    public static void getTakeVideo2(int resultCode, Intent data, VideoDataListener listener) {
        if (listener == null) return;
        String currentInputVideoPath;
        //录制视频
        if (resultCode == RESULT_CODE_FOR_RECORD_VIDEO_SUCCEED) {

            //录制成功
            String videoPath = data.getStringExtra(INTENT_EXTRA_VIDEO_PATH);
            if (!TextUtils.isEmpty(videoPath)) {
                currentInputVideoPath = videoPath;

                MediaMetadataRetriever retr = new MediaMetadataRetriever();
                retr.setDataSource(currentInputVideoPath);
                String time = retr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);//获取视频时长
                //7680
                double videoLength;
                try {
                    videoLength = Double.parseDouble(time) / 1000.00;
                } catch (Exception e) {
                    e.printStackTrace();
                    videoLength = 0.00;
                }
                listener.success(videoLength, currentInputVideoPath);
            }
        } else if (resultCode == RESULT_CODE_FOR_RECORD_VIDEO_FAILED) {
            //录制失败
            currentInputVideoPath = "";
            listener.failure("录制失败");
        }
    }

    public interface VideoDataListener {
        void success(double videoLength, String videoSavePath);

        void failure(String msg);
    }


}
