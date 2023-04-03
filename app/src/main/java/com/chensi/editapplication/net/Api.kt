package com.chensi.editapplication.net

import retrofit2.http.Body
import retrofit2.http.POST

/**
 * 文件名：Api <br/>
 * 描述：网络请求api
 *
 * @author chensi
 * @since 2022/10/27 15:53
 */
interface Api {

    @POST("/user-auth-api/cpp/v1/auth/sms")
    suspend fun getPhoneCheckCode(@Body map: Map<String, String>)

}