package com.wugx.kotlin_autils

import com.blankj.utilcode.util.LogUtils
import com.wugx_autils.base.BaseActivityK
import kotlinx.android.synthetic.main.activity_test.*

/**
 *
 *
 *@author Wugx
 *@date   2018/12/18
 */
class TestActivity : BaseActivityK(), TestContract.IView {
    override fun showDatas(content: String) {
        tv_test_txt.text = content
        LogUtils.d(">>>>" + content)
    }

    val presenter by lazy { TestPresenter() }
    override fun getLayoutId(): Int = R.layout.activity_test

    override fun initData() {
        presenter.attachView(this)
        mActionBar!!.setDisplayHomeAsUpEnabled(true)
        presenter.req()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}