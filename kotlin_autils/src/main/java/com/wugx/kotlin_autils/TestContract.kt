package com.wugx.kotlin_autils

import com.wugx_autils.mvp.view.BaseView

/**
 *
 *
 *@author Wugx
 *@date   2018/12/18
 */
interface TestContract {
    interface IView : BaseView {
        fun showDatas(content: String)
    }
}