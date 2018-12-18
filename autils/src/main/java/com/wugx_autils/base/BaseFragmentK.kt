package com.wugx_autils.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.NonNull
import android.support.v4.content.ContextCompat
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.blankj.utilcode.util.SnackbarUtils
import com.blankj.utilcode.util.Utils
import com.kingja.loadsir.core.LoadService
import com.kingja.loadsir.core.LoadSir
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.trello.rxlifecycle2.components.support.RxFragment
import com.wugx_autils.R
import com.wugx_autils.callback.EmptyCallback
import com.wugx_autils.callback.ErrorCallback
import com.wugx_autils.mvp.view.BaseView
import com.wugx_autils.util.UiUtils

/**
 *
 *
 *@author Wugx
 *@date   2018/12/18
 */
abstract class BaseFragmentK : RxFragment(), BaseView {
    override fun showLoading(msg: String?) {
        UiUtils.showLoadDialog(msg)
    }

    override fun hideLoading() {
        UiUtils.dismissLoadDialog()
    }

    override fun showToast(o: Any?) {
    }

    lateinit var baseActivity: BaseActivityK

    private lateinit var tvTitle: TextView

    open fun isShowTitle(): Boolean = false

    private lateinit var smartRefreshLayout: SmartRefreshLayout

    open fun isRebound(): Boolean = true
    open fun isUseLoadSir() = false

    private lateinit var layoutView: View
    private lateinit var layoutTitle: View
    private lateinit var loadService: LoadService<Any>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //        baseActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        layoutView = setView(inflater)
        return if (isUseLoadSir()) {
            initLoadSir()
            loadService.loadLayout
        } else {
            layoutView
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI(layoutView)
        loadData()
        setListener()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            loadData()
            setListener()
        }
    }

    @NonNull
    private fun setView(inflater: LayoutInflater): LinearLayout {
        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1f)
        val linearLayout = LinearLayout(baseActivity)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.layoutParams = params

        if (isShowTitle()) {
            layoutTitle = View.inflate(baseActivity, R.layout.layout_title, null)
            val p = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            initActionBar()
            linearLayout.addView(layoutTitle, p)
        }

        var view = inflater.inflate(layoutId(), null)
        view?.let {
            it.layoutParams = params
            it.setBackgroundResource(R.color.gray_f2f2f2)
            if (isRebound()) {
                smartRefreshLayout = SmartRefreshLayout(baseActivity)
                smartRefreshLayout.setBackgroundColor(ContextCompat.getColor(Utils.getApp(), R.color.gray_f2f2f2))
                smartRefreshLayout.layoutParams = params
                //            setSmartRefreshLayoutCommon(smartRefreshLayout);
                smartRefreshLayout.addView(view, params)
                linearLayout.addView(smartRefreshLayout, params)
            } else {
                linearLayout.addView(view, params)
            }
        }
        return linearLayout
    }

    private lateinit var mToolBar: Toolbar
    private lateinit var actionbar: ActionBar
    private fun initActionBar() {
        mToolBar = layoutTitle.findViewById(R.id.toolbar_layout) as Toolbar
        tvTitle = layoutTitle.findViewById(R.id.tv_title) as TextView

        baseActivity.setSupportActionBar(mToolBar)
        actionbar = baseActivity.supportActionBar!!
        actionbar.setDisplayShowHomeEnabled(false)
        actionbar.setHomeAsUpIndicator(R.drawable.ic_arrow_back)

        //set back icon
//            actionbar!!.setDisplayHomeAsUpEnabled(true)

        mToolBar.title = ""
        val defaultTitle = baseActivity.title.toString()
        if (TextUtils.isEmpty(defaultTitle)) {
            tvTitle.setText(R.string.app_name)
        } else {
            tvTitle.text = defaultTitle
        }
    }

    open fun setTitleBack() {
        if (isShowTitle()) actionbar.setDisplayHomeAsUpEnabled(true)
    }

    fun setTvTitle(txt: String) {
        if (TextUtils.isEmpty(txt)) return
        tvTitle?.let {
            it.text = txt
        }
    }

    fun setTvTitle(txtId: Int) {
        if (txtId == 0) return
        tvTitle.text = Utils.getApp().resources.getString(txtId)
    }

    fun getSmartRefreshLayout(): SmartRefreshLayout? {
        return if (isRebound()) smartRefreshLayout else null
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        baseActivity = context as BaseActivityK
    }

    fun findViewById(resId: Int): View {
        return layoutView.findViewById(resId)
    }

    private var mTvErrorMsg: TextView? = null
    private var mTvEmpty: TextView? = null
    private fun initLoadSir() {
        loadService = LoadSir.getDefault().register(layoutView) {
            // 重新加载逻辑
            loadData()
        }

        loadService?.let {
            it.setCallBack(ErrorCallback::class.java) { _, view -> mTvErrorMsg = view.findViewById(R.id.tv_error_msg) }
            it.setCallBack(EmptyCallback::class.java) { _, view -> mTvEmpty = view.findViewById(R.id.tv_empty) }
        }
    }


    @LayoutRes
    protected abstract fun layoutId(): Int

    /**
     * 网络请求等
     */
    protected abstract fun loadData()

    /**
     * UI
     */
    protected abstract fun initUI(v: View)

    open fun setListener() {}

    override fun showError(msg: String) {
        showMsg(msg)
    }


    override fun showException(msg: String) {
        val view = baseActivity.window.decorView.findViewById<View>(android.R.id.content)
        SnackbarUtils.with(view).setMessage(msg).showError()
    }


    override fun showMsg(msg: String) {
        val view = baseActivity.window.decorView.findViewById<View>(android.R.id.content)
        SnackbarUtils.with(view).setMessage(msg).showWarning()
    }


    override fun onDestroy() {
        super.onDestroy()
//        activity?.let { BaseApplication.getRefWatcher(it)?.watch(activity) }
    }
}