package com.chensi.editapplication.item.flow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chensi.editapplication.MenuAdapter
import com.chensi.editapplication.data.MenuData
import com.chensi.editapplication.databinding.ActivityFlowBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.flow.toSet
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.lang.Exception
import java.lang.RuntimeException

/**
 * 文件名：FlowCheckActivity
 * 描述：kotlin flow
 *
 * @author chensi
 * @since 2023/5/22 15:00
 */
class FlowCheckActivity : AppCompatActivity() {

    private val mBinding: ActivityFlowBinding by lazy {
        ActivityFlowBinding.inflate(layoutInflater)
    }

    private val mAdapter by lazy {
        MenuAdapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        mBinding.rvMain.layoutManager = LinearLayoutManager(this)
        mBinding.rvMain.adapter = mAdapter
        mAdapter.setOnItemClickListener { adapter, _, position ->
            val menu = adapter.getItem(position) as? MenuData
            menu?.callback?.invoke(this)
        }
        initData()
    }

    private fun initData() {
        mAdapter.setList(arrayListOf<MenuData>().apply {
            add(MenuData("基本使用", "") {
                MainScope().launch {
                    flow {
                        for (i in 1..5) {
                            delay(500)
                            emit(i)
                        }
                    }.collect {
                        println("i == $it")
                    }
                }
            })

            add(MenuData("flow builder flowOf", "") {
                MainScope().launch {
                    flowOf(1, 2, 3, 4, 5).onEach {
                        delay(100)
                    }.collect {
                        println("i == $it")
                    }
                }
            })

            add(MenuData("flow builder asFlow", "") {
                MainScope().launch {
                    listOf(1, 2, 3, 4, 5).asFlow().onEach {
                        delay(100)
                    }.collect {
                        println("i == $it")
                    }
                }
            })

            add(MenuData("flow builder channelFlow", "") {
                MainScope().launch {
                    channelFlow {
                        for (i in 1..5) {
                            delay(100)
                            send(i)
                        }
                    }.collect {
                        println("i == $it")
                    }
                }
            })

            add(MenuData("flow builder channelFlow 异步非阻塞特征", "") {
                MainScope().launch {
                    channelFlow {
                        for (i in 1..15) {
                            delay(500)
                            send(i)
                        }
                    }.onCompletion { println("send end") }
                        .collect {
                            delay(100)
                            println("i == $it")
                        }
                }
            })

            add(MenuData("flow builder flowOn", "") {
                MainScope().launch {
                    flow {
                        for (i in 1..5) {
                            delay(100)
                            emit(i)
                        }
                    }.flowOn(Dispatchers.IO)
                        .collect {
                            println("${Thread.currentThread().name}  i == $it")
                        }
                }
            })

            add(MenuData("flow builder cancel", "") {
                MainScope().launch {
                    withTimeoutOrNull(2500) {
                        flow {
                            for (i in 1..5) {
                                delay(1000)
                                emit(i)
                            }
                        }.collect {
                            println("${Thread.currentThread().name}  i == $it")
                        }
                    }
                    println("Done")
                }
            })

            add(MenuData("flow builder 与Sequence区别 1", "") {
                MainScope().launch {

                    launch {
                        for (j in 1..5) {
                            delay(100)
                            println("I'm not blocked $j")
                        }
                    }

                    listOf(1, 2, 3, 4, 5).asFlow().onEach {
                        delay(100)
                    }.collect {
                        println("i == $it")
                    }

                    println("Done")
                }
            })

            add(MenuData("flow builder 与Sequence区别 2", "") {
                MainScope().launch {
                    launch {
                        for (j in 1..5) {
                            delay(100)
                            println("I'm blocked $j")
                        }
                    }

                    sequence {
                        for (i in 1..5) {
                            Thread.sleep(100)
                            yield(i)
                        }
                    }.forEach {
                        println("i == $it")
                    }

                    println("Done")
                }
            })

            add(MenuData("flow builder Backpressure", "") {
                MainScope().launch {
                    listOf(1, 2, 3, 4, 5)
                        .asFlow()
//                        .buffer()
//                        .conflate()

                        .collect {
                            println(it)
                        }
                    println("Done")
                }
            })

            add(MenuData("flow builder 异常处理 try catch", "") {
                MainScope().launch {
                    flow {
                        emit(1)
                        emit(2)
                        try {
                            throw RuntimeException()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        emit(3)
                    }.collect {
                        println(it)
                    }
                    println("Done")
                }
            })

            add(MenuData("flow builder 异常处理 catch 操作符 onCompletion", "") {
                MainScope().launch {
                    flow {
                        emit(1)
                        emit(2)
                        throw RuntimeException()
                    }.onCompletion { cause ->
                        if (cause != null) {
                            print("Flow completed exceptionally $cause")
                        } else {
                            println("Done")
                        }
                    }
                        .catch { println("catch exception") }
                        .collect { println(it) }
                }
            })

            add(MenuData("flow builder 操作符 retry", "") {
                MainScope().launch {

                    listOf(1, 2, 3, 4, 5, 6).asFlow()
                        .onEach {
                            if (it == 3) {
                                throw RuntimeException("Error on $it")
                            }
                        }.retry(2) {
                            if (it is RuntimeException) {
                                return@retry true
                            }
                            false
                        }
                        .onEach { println("Emitting $it") }
                        .catch { it.printStackTrace() }
                        .collect()
                }
            })

            add(MenuData("flow builder 操作符 retryWhen", "") {
                MainScope().launch {

                    listOf(1, 2, 3, 4, 5, 6).asFlow()
                        .onEach {
                            if (it == 3) {
                                throw RuntimeException("Error on $it")
                            }
                        }.retryWhen { cause, attempt ->
                            attempt < 2
                        }
                        .onEach { println("Emitting $it") }
                        .catch { it.printStackTrace() }
                        .collect()
                }
            })

            add(MenuData("flow builder Flow Lifecycle", "") {
                MainScope().launch {

                    listOf(1, 2, 3, 4, 5, 6).asFlow()
                        .onEach {
                            if (it == 3) {
                                throw RuntimeException("Error on $it")
                            }
                        }
                        .onStart { println("Starting flow") }
                        .onEach { println("On each $it") }
                        .catch { println("Exception : ${it.message}") }
                        .onCompletion { println("Flow completed") }
                        .collect()
                }
            })

            add(MenuData("flow builder 线程切换", "") {
                //flow 处理数据所用线程与启动他的协程相关
                //flow 发射数据所用线程可通过flowOn设置
                CoroutineScope(Dispatchers.IO).launch {
                    listOf(1, 2, 3, 4, 5, 6).asFlow()
                        .onEach {
                            println("onEach before flowOn Thread on ${Thread.currentThread().name} number $it")
                        }
                        .flowOn(Dispatchers.Main)
                        .onEach {
                            println("onEach after flowOn Thread on ${Thread.currentThread().name} number $it")
                        }
                        .collect {
                            println("collect Thread on ${Thread.currentThread().name} number $it")
                        }
                }
            })

            add(MenuData("flow builder flatMapMerge", "") {
                MainScope().launch {
                    val result = arrayListOf<Int>()
                    for (i in 1..600) {
                        result.add(i)
                    }

                    result.asFlow()
                        .flatMapMerge {
                            flow {
                                emit(it)
                            }.flowOn(Dispatchers.IO)
                        }
                        .buffer()
                        .collect {
                            println("$it")
                        }
                }
            })

            add(MenuData("flow builder transform", "") {
                MainScope().launch {
                    val result = arrayListOf<Int>()
                    for (i in 1..60) {
                        result.add(i)
                    }

                    result.asFlow()
                        .transform {
                            emit(it * 2)
                            delay(100)
                            emit(it * it)
                        }
                        .buffer()
                        .collect {
                            println("$it")
                        }
                }
            })

            add(MenuData("flow builder transform emit 多种类数据", "") {
                MainScope().launch {
                    val result = arrayListOf<Int>()
                    for (i in 1..60) {
                        result.add(i)
                    }

                    result.asFlow()
                        .transform {
                            emit(it * 2)
                            delay(100)
                            emit("$it * $it")
                        }
                        .buffer()
                        .collect {
                            println("$it")
                        }
                }
            })

            add(MenuData("flow builder take", "") {
                MainScope().launch {
                    val result = arrayListOf<Int>()
                    for (i in 1..60) {
                        result.add(i)
                    }

                    result.asFlow()
                        .take(2)
                        .buffer()
                        .collect {
                            println("$it")
                        }
                }
            })

            add(MenuData("flow builder reduce", "") {
                MainScope().launch {
                    val sum = (1..20).asFlow()
                        .map { it * it }
                        .reduce { a, b ->
                            a + b
                        }
                    println("$sum")
                }
            })

            add(MenuData("flow builder fold", "") {
                MainScope().launch {
                    val sum = (1..20).asFlow()
                        .map { it * it }
                        .fold(0) { a, b ->
                            a + b
                        }
                    println("$sum")
                }
            })

            add(MenuData("flow builder fold", "") {
                MainScope().launch {
                    val sum = (1..20).asFlow()
                        .map { it * it }
                        .fold(1) { a, b ->
                            a * b
                        }
                    println("$sum")
                }
            })

            add(MenuData("flow builder zip", "") {
                MainScope().launch {
                    val flowNumber = (1..6).asFlow()
                    val flowChar = listOf("a", "b", "c", "d", "e", "f", "g").asFlow()

                    flowNumber.zip(flowChar) { number, char ->
                        "$number and $char"
                    }.collect {
                        println(it)
                    }
                }
            })
        })
    }


}