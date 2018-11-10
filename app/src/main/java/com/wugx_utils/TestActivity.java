package com.wugx_utils;

import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.wugx_autils.base.BaseActivity;
import com.wugx_utils.test.TestPresenter;

import butterknife.BindView;

import static com.wugx_utils.test.TestContract.TestView;

/**
 * @author Wugx
 * @date 2018/11/8
 */
public class TestActivity extends BaseActivity<TestView, TestPresenter> implements TestView {

    @BindView(R.id.tv_test_info)
    TextView mTvTestInfo;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void initData() {
        showLoading("---");
        LogUtils.d("test>>initData");
        presenter.showInfo();

    }

    @Override
    public boolean isUseLoadService() {
        return true;
    }

    @Override
    protected TestPresenter createPresenter() {
        return new TestPresenter(this);
    }

    @Override
    public void showDatas(String result) {
        LogUtils.d("test>>showDatas");
        hideLoading();
        mTvTestInfo.setText(result);
    }

    @Override
    public void showError(String msg) {
    }


//    public void uploadFile1(String url, String fileKey, File file, Map<String, String> parms) {
//        //文件路径
//
//        RequestBody fileBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        MultipartBody.Builder builder = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM);
//        for (String parmKey : parms.keySet()) {
//            builder.addFormDataPart(parmKey, parms.get(parmKey));
//        }
//        builder.addFormDataPart(fileKey, file.getName(), fileBody);
//
//        List<MultipartBody.Part> partList = builder.build().parts();
//        RetrofitHelper.getApiService()
//                .uploadFiles(url, partList)
//                .subscribeOn(Schedulers.io())
//                .compose(this.<BasicBean>bindToLifecycle())
//                .compose(ProgressUtils.<BasicBean>applyProgressBar(baseActivity, "上传文件中..."))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new DefaultObserver<BasicBean>() {
//                    @Override
//                    public void onSuccess(BasicBean response) {
////                        ToastUtils.show("文件上传成功");
//                    }
//                });
//    }

}
