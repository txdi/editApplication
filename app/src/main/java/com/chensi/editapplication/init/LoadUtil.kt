package com.chensi.editapplication.init

import android.app.Application
import com.chensi.editapplication.cache.CacheUtil
import com.chensi.editapplication.net.RetrofitFactory

/**
 * 文件名：LoadUtil <br/>
 *
 * @author chensi
 * @since 2022/10/28 14:23
 */
object LoadUtil {


    fun load(app: Application) {
        CacheUtil.initCatch(app)
        initDomain()


    }

    private fun initDomain() {
        RetrofitFactory.initBaseUrl()
    }
}