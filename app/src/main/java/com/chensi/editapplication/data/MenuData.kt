package com.chensi.editapplication.data

import android.content.Context
import android.content.Intent
import android.util.Log
import com.chensi.editapplication.item.constraintlayout.ConstraintLayoutActivity
import com.chensi.editapplication.item.coroutines.CoroutinesCheckActivity
import com.chensi.editapplication.item.flow.FlowCheckActivity
import com.chensi.editapplication.item.motionlayout.MotionLayoutActivity
import com.chensi.editapplication.net.Api
import com.chensi.editapplication.net.RetrofitFactory
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import retrofit2.create

/**
 * 文件名：MenuData <br/>
 *
 * @author chensi
 * @since 2022/10/28 16:07
 */

fun getMenu(): List<MenuData> {
    return listOf(
        MenuData("请求验证码", "check code") {
            val api: Api = RetrofitFactory.retrofit.create()
            MainScope().launch {
                kotlin.runCatching {
                    api.getPhoneCheckCode(
                        mapOf(
                            "phone" to "13616083363",
                            "type" to "login"
                        )
                    )
                }.onSuccess {
                    Log.v("check code", it.toString())
                }.onFailure {
                    Log.e("check code error", it.toString())
                }
            }
        },
        MenuData("constraint layout 验证", "constraintLayout check") {
            it.startActivity(Intent(it, ConstraintLayoutActivity::class.java))
        },
        MenuData("motion layout 动画", "motionLayout animate") {
            it.startActivity(Intent(it, MotionLayoutActivity::class.java))
        },
        MenuData("coroutines check", "coroutines") {
            it.startActivity(Intent(it, CoroutinesCheckActivity::class.java))
        },
        MenuData("kotlin flow", "kotlin_flow") {
            it.startActivity(Intent(it, FlowCheckActivity::class.java))
        }
    )
}


class MenuData(val name: String, val id: String, val callback: (context: Context) -> Unit)