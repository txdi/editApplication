package com.chensi.editapplication.cache

import android.app.Application
import java.io.File

/**
 *  获得缓存目录
 *  1，glide缓存
 *  2，图片缓存（临时生成的头像，照片等等）
 *  3，下载的缓存
 *  4，http的缓存
 *  5，其他缓存
 *
 *  有需要的自取，没必要去申请读写内存卡的权限
 * **/
object CacheUtil {

    private var context: Application? = null

    fun initCatch(context: Application) {
        this.context = context
    }

    fun getHttpTempDir(): File {
        checkContext()
        val httpTemp = File(getTempDir(), "/httpCatch")
        if (httpTemp.exists() && httpTemp.isFile) {
            httpTemp.delete()
        }
        if (!httpTemp.exists()) {
            httpTemp.mkdirs()
        }
        return httpTemp
    }

    private fun checkContext() {
        if (context == null) throw RuntimeException("please init first")
    }

    fun getTempDir(): File? = context?.cacheDir
}