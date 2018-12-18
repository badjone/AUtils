package com.wugx.kotlin_autils

import android.app.Application
import com.wugx_autils.AUtils

/**
 *
 *
 *@author Wugx
 *@date   2018/12/18
 */
class BaseApplication : Application() {
    val baseUrl = "http://116.95.255.211:10100/"
    override fun onCreate() {
        super.onCreate()
        AUtils.getInstance().init(this).setBaseUrl(baseUrl)
    }
}