package com.chensi.editapplication.net

import android.content.Context
import android.text.TextUtils
import android.webkit.WebSettings
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class HeaderInterceptor(private val context: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val newBuilder: Request.Builder = request.newBuilder()
        val token = ""

        if (!TextUtils.isEmpty(token)) {
            newBuilder.addHeader("authorization", token)
        }

        newBuilder.addHeader("version", "V2.0")
        newBuilder.addHeader("Accept", "application/json")
        newBuilder.addHeader("androidVersion", android.os.Build.VERSION.RELEASE)
        newBuilder.addHeader("appVersion", "4.0.3")

        // 巨量统计
        newBuilder.addHeader("os", "0")
        //oaid
        newBuilder.addHeader("oaid", getOaid())
        //android
        newBuilder.addHeader("androidid", ANDROID_ID)
        //deviceId
        newBuilder.addHeader("deviceId", ANDROID_ID)

        //imei
        newBuilder.addHeader(
            "imei", ""
        )

        try {
            newBuilder.addHeader("channel", "hh_gwweb_1")
        } catch (e: java.lang.Exception) {

        }
        newBuilder.removeHeader("UA")
        newBuilder.addHeader("UA", uaStr)


        try {
            return chain.proceed(newBuilder.build())
        } catch (e: IOException) {
            throw e
        }
    }

    private fun getOaid(): String {
        return ""
    }

    private val ANDROID_ID by lazy {
        ""
    }

    private val uaStr by lazy {
        WebSettings.getDefaultUserAgent(context)
    }
}