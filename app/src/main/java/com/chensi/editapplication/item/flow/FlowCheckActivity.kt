package com.chensi.editapplication.item.flow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.chensi.editapplication.MenuAdapter
import com.chensi.editapplication.data.MenuData
import com.chensi.editapplication.databinding.ActivityFlowBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.flow.toSet
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull

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

        })
    }


}