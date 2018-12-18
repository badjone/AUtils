package com.wugx_autils.base

import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import butterknife.ButterKnife
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SnackbarUtils
import com.blankj.utilcode.util.ToastUtils
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity
import com.wugx_autils.R
import com.wugx_autils.callback.EmptyCallback
import com.wugx_autils.callback.ErrorCallback
import com.wugx_autils.callback.LoadingCallback
import com.wugx_autils.mvp.view.BaseView

/**
 *
 *
 *@author Wugx
 *@date   2018/12/18
 */
abstract class BaseActivityK : RxAppCompatActivity(),BaseView {
    abstract fun getLayoutId(): Int
    abstract fun initData()


    private var smartRefreshLayout: SmartRefreshLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        presenter = createPresenter()
//        if (presenter != null) presenter.attachView(this as V)

        //init layout
        val contentLayout = setBaseLayout()
        ButterKnife.bind(this)
        if (isUseLoadService()) {
            initLoadSir(contentLayout)
        }
        initData()
        initListener()
    }

    private fun setBaseLayout(): View? {
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        val parms = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f)
        val contentLayout = layoutInflater.inflate(getLayoutId(), null)
        if (isShowTitle()) {
            val layout_title = View.inflate(this, R.layout.layout_title, null)
            val p = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            initActionBar(layout_title)
            layout.addView(layout_title, p)
        }

        if (isRebound()) {
            smartRefreshLayout = SmartRefreshLayout(this)
            smartRefreshLayout!!.layoutParams = parms
            //            setSmartRefreshLayoutCommon(smartRefreshLayout);
            if (contentLayout != null) smartRefreshLayout!!.addView(contentLayout, parms)
            layout.addView(smartRefreshLayout, parms)
        } else {
            layout.addView(contentLayout, parms)
        }
        setContentView(layout, parms)
        return contentLayout
    }

    fun getSmartRefreshLayout(): SmartRefreshLayout? {
        return if (isRebound()) smartRefreshLayout else null
    }

    fun isUseLoadService(): Boolean {
        return false
    }

    fun initListener() {}

    fun isRebound(): Boolean {
        return true
    }

    fun isShowTitle(): Boolean {
        return true
    }

    private var mToolbar: Toolbar? = null
    protected var mActionBar: ActionBar? = null
    private var tvTitle: TextView? = null

    private fun initActionBar(v: View) {
        mToolbar = v.findViewById(R.id.toolbar_layout)
        tvTitle = v.findViewById(R.id.tv_title)
        if (mToolbar != null) {
            setSupportActionBar(mToolbar)
            mActionBar = supportActionBar
            mActionBar!!.setDisplayShowTitleEnabled(false)
            //set back button
            mActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            //显示左侧返回按钮
            //            mActionBar.setDisplayHomeAsUpEnabled(true);

            val defaultTitle = title.toString()
            if (TextUtils.isEmpty(defaultTitle)) {
                tvTitle!!.setText(R.string.app_name)
            } else {
                tvTitle!!.text = defaultTitle
            }
        }
    }

    fun setTitle(title: String) {
        if (!isShowTitle()) return
        if (tvTitle != null && !TextUtils.isEmpty(title)) {
            tvTitle!!.text = title
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val i = item.itemId
        if (i == android.R.id.home) {//back button
            onBackPressed()
            //                finish();
        } else if (i == R.id.menu_right) {
            setTitleRightListener()
        } else {

        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_common, menu)
        val item = menu.findItem(R.id.menu_right)
        val showTitleRight = isShowTitleRight(item)
        item.isVisible = showTitleRight
        //        item.setIcon(ContextCompat.getDrawable(Utils.getApp(), android.R.drawable.ic_menu_add));
        return true
    }

    /**
     * 是否显示右侧按钮
     *
     * @param item
     * @return
     */
    fun isShowTitleRight(item: MenuItem): Boolean {
        //do some thing... eg 改变图标等等
        return false
    }

    /**
     * toolbar右侧按钮点击事件简单调用，具体参见[Toolbar]
     */
    fun setTitleRightListener() {}

    override fun showMsg(msg: String) {
        val view = window.decorView.findViewById<View>(android.R.id.content)
        SnackbarUtils.with(view).setMessage(msg).show()
    }

    override fun showToast(o: Any) {
        if (o is Int) {
            ToastUtils.showShort(o)
        } else {
            ToastUtils.showShort(o.toString())
        }
    }

    override fun showLoading(msg: String) {
        //        getProgressDialog().setMessage(msg).show();

        if (isUseLoadService() && mLoadService != null) {
            mLoadService!!.showCallback(LoadingCallback::class.java)
        }
    }

    override fun hideLoading() {
        if (isUseLoadService() && mLoadService != null) {
            mLoadService!!.showSuccess()
        }
    }

    override fun showNetError() {
        LogUtils.d("showNetError:")
        if (isUseLoadService() && mLoadService != null) {
            mTvErrorMsg!!.text = getString(R.string.txt_net_error_tips)
            mLoadService!!.showCallback(ErrorCallback::class.java)
        }
    }

    override fun showException(msg: String) {
        if (isUseLoadService() && mLoadService != null) {
            //show exception info
            mTvErrorMsg!!.text = msg
            mLoadService!!.showCallback(ErrorCallback::class.java)
        }
    }

    override fun showError(msg: String) {
        showMsg(msg)
    }

    private var mLoadService: LoadService<*>? = null
    private var mTvErrorMsg: TextView? = null
    private var mTvEmpty:TextView? = null

    private fun initLoadSir(o: Any?) {
        mLoadService = LoadSir.getDefault().register(o) { mLoadService!!.showCallback(LoadingCallback::class.java) }

        mLoadService!!.setCallBack(ErrorCallback::class.java) { context, view -> mTvErrorMsg = view.findViewById(R.id.tv_error_msg) }

        mLoadService!!.setCallBack(EmptyCallback::class.java) { context, view -> mTvEmpty = view.findViewById<TextView>(R.id.tv_empty) }
    }
}