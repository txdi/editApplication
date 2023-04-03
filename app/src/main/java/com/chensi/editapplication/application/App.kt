package com.chensi.editapplication.application

import android.content.Context
import com.chensi.editapplication.BuildConfig
import com.chensi.editapplication.init.LoadUtil
import com.chensi.editapplication.net.HeaderInterceptor
import com.chensi.editapplication.net.HttpLoggingInterceptor
import com.chensi.editapplication.net.RetrofitFactory

/**
 * 文件名：App <br/>
 *
 * @author chensi
 * @since 2022/10/28 11:43
 */
class App : BaseApplication() {

    companion object {
        lateinit var application: BaseApplication
        var startTime: Long = 0L
    }

    override fun attachBaseContext(base: Context?) {
        startTime = System.currentTimeMillis()
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
        application = this
        initRetrofit(this)
        LoadUtil.load(this)
    }

    private fun initRetrofit(app: App) {
        RetrofitFactory.addInterceptor(HeaderInterceptor(app))
        RetrofitFactory.addInterceptor(HttpLoggingInterceptor())
//        if (BuildConfig.DEBUG) {
//            RetrofitFactory.addInterceptor(OriginConfigHostInterceptor())
//        }
    }

}