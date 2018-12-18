package com.wugx_autils.zxing;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.constraint.Guideline;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.blankj.utilcode.util.BarUtils;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.wugx_autils.R;
import com.wugx_autils.R2;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 二维码扫描
 * <p>
 * Created by Wugx on 2018/8/27.
 */

public class CustomScanActivity extends AppCompatActivity {
    @BindView(R2.id.fl_scan_content)
    FrameLayout mFlScanContent;
    @BindView(R2.id.cb_scan_light)
    CheckBox mCbScanLight;
    @BindView(R2.id.line)
    Guideline mLine;
    @BindView(R2.id.cb_scan_input)
    CheckBox mCbScanInput;
    @BindView(R2.id.img_scan_back)
    ImageView mImgScanBack;
    private CaptureFragment captureFragment;

    public static final int OPEN_ISBN_ACTIVITY = 0xFF16;
    private InputMethodManager imm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qode);
        ButterKnife.bind(this);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        loadData();
    }


    protected void loadData() {
        BarUtils.setStatusBarColor(this, getResources().getColor(R.color.transparent), 0);
        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.my_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_scan_content, captureFragment).commit();
    }

    @OnClick({R2.id.cb_scan_light, R2.id.cb_scan_input, R2.id.img_scan_back})
    public void onclick(View v) {
        int i = v.getId();
        if (i == R.id.cb_scan_light) {
            if (!isOpen) {
                CodeUtils.isLightEnable(true);
                isOpen = true;
            } else {
                CodeUtils.isLightEnable(false);
                isOpen = false;
            }

        } else if (i == R.id.cb_scan_input) {
            startActivityForResult(new Intent(this, ISBNActivity.class), OPEN_ISBN_ACTIVITY);

        } else if (i == R.id.img_scan_back) {
            finish();

        }
    }

    public static boolean isOpen = false;

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            setResult(RESULT_OK, resultIntent);
            finish();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OPEN_ISBN_ACTIVITY) {
            if (data == null) return;
            String isbn = data.getStringExtra("isbn");
            if (!TextUtils.isEmpty(isbn)) {
                isbnSearch(isbn);
            }
        }
    }

    private void isbnSearch(String isbn) {
        Intent intent = new Intent();
        intent.putExtra(CodeUtils.RESULT_STRING, isbn);
        intent.putExtra(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
