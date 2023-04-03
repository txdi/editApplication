package com.chensi.editapplication.net

import com.chensi.editapplication.cache.CacheUtil
import com.google.gson.GsonBuilder
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

/**
 * 文件名：RetrofitFactory <br/>
 * 描述：retrofit工程类
 *
 * @author chensi
 * @since 2022/10/27 15:47
 */
object RetrofitFactory {

    private const val baseUrl = "http://cn-dev02-gw.henhenchina.com/"
    lateinit var retrofit: Retrofit

    private val retrofitMap by lazy {
        HashMap<String, Retrofit>()
    }

    private val okHttpClient by lazy {
        RetrofitUrlManager.getInstance().with(initOkHttpClient()).build()
    }

    fun initBaseUrl() {
        this.retrofit = createRetrofit(baseUrl)
        this.retrofitMap.clear()
    }

    private fun createRetrofit(url: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().registerTypeAdapterFactory(GsonDefaultAdapterFactory()).create()
                )
            )
            .client(okHttpClient)
            .build()
    }

    inline fun <reified T> create(): T {
        val retrofitNow = retrofit ?: throw  IllegalArgumentException("需要先设置baseUrl")
        return retrofitNow.create(T::class.java)
    }

    fun putDomain(key: String, value: String) {
        RetrofitUrlManager.getInstance().putDomain(key, value)
    }

    private val interceptorList by lazy {
        ArrayList<Interceptor>()
    }

    private val netWorkInterceptorList by lazy {
        ArrayList<Interceptor>()
    }

    fun addInterceptor(interceptor: Interceptor) {
        interceptorList.add(interceptor)
    }

    fun addNetworkInterceptor(interceptor: Interceptor) {
        netWorkInterceptorList.add(interceptor)
    }

    private fun initOkHttpClient(): OkHttpClient.Builder {
        val builder = OkHttpClient.Builder()
        for (interceptor in interceptorList) {
            builder.addInterceptor(interceptor)
        }
        for (interceptor in netWorkInterceptorList) {
            builder.addNetworkInterceptor(interceptor)
        }
        builder.readTimeout(12 * 1000, TimeUnit.MILLISECONDS)
            .writeTimeout(12 * 1000, TimeUnit.MILLISECONDS)
            .connectTimeout(12 * 1000, TimeUnit.MILLISECONDS)
            .cache(Cache(CacheUtil.getHttpTempDir(), 20 * 1024 * 1024))
            .retryOnConnectionFailure(true)
        return builder
    }

}