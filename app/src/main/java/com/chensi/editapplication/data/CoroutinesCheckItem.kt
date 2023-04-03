package com.chensi.editapplication.data

import com.chensi.editapplication.item.coroutines.ComposingSuspendingFunctions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull


fun getCheckItems(): List<CoroutinesCheckItem> {
    return listOf(
        CoroutinesCheckItem(
            "运行一个取消的协程"
        ) {
            MainScope().launch {
                println("main: run cancelCoroutines")
                val job = launch {
                    repeat(1000) { i ->
                        println("job: I'm sleeping $i ...")
                        delay(500L)
                    }
                }
                delay(1300L) // 延迟一段时间
                println("main: I'm tired of waiting!")
                job.cancel() // 取消该作业
                job.join() // 等待作业执行结束
                println("main: Now I can quit.")
            }
        },
        CoroutinesCheckItem(
            "协程的协作取消 含有计算的"
        ) {
            MainScope().launch {
                println("main: run cancelCoroutinesMulWithCalculate")
                val startTime = System.currentTimeMillis()
                val job = launch(Dispatchers.Default) {
                    var nextPrintTime = startTime
                    var i = 0
                    while (i < 5) { // 一个执行计算的循环，只是为了占用 CPU
                        // 每秒打印消息两次
                        if (System.currentTimeMillis() >= nextPrintTime) {
                            println("job: I'm sleeping ${i++} ...")
                            nextPrintTime += 500L
                        }
                    }
                }
                delay(1300L) // 等待一段时间
                println("main: I'm tired of waiting!")
                job.cancelAndJoin() // 取消一个作业并且等待它结束
                println("main: Now I can quit.")
            }
        },
        CoroutinesCheckItem(
            "协程的协作取消 不含计算的"
        ) {
            MainScope().launch {
                println("main: run cancelCoroutinesMulWithoutCalculate")
                val job = launch(Dispatchers.Default) {
                    var i = 0
                    while (i < 5) { // 一个执行计算的循环，只是为了占用 CPU
                        // 每秒打印消息两次
                        println("job: I'm sleeping ${i++} ...")
                        delay(500L)
                    }
                }
                delay(1300L) // 等待一段时间
                println("main: I'm tired of waiting!")
                job.cancelAndJoin() // 取消一个作业并且等待它结束
                println("main: Now I can quit.")
            }
        },
        CoroutinesCheckItem(
            "取消内部含有计算步骤的协程"
        ) {
            MainScope().launch {
                val startTime = System.currentTimeMillis()
                val job = launch(Dispatchers.Default) {
                    var nextPrintTime = startTime
                    var i = 0
                    while (isActive) {
                        if (System.currentTimeMillis() >= nextPrintTime) {
                            println("job: I'm sleeping ${i++} ...")
                            nextPrintTime += 500L
                        }
                    }
                }
                delay(1300L)
                println("main: I'm tired of waiting!")
                job.cancelAndJoin()
                println("main: Now I can quit.")
            }
        },
        CoroutinesCheckItem(
            "在 finally 中释放资源"
        ) {
            MainScope().launch {
                val job = launch {
                    try {
                        repeat(1000) { i ->
                            println("job: I'm sleeping $i...")
                            delay(500L)
                        }
                    } finally {
                        println("job: I'm running finally")
                    }
                }
                delay(1300L)
                println("main: I'm tired of waiting!")
                job.cancelAndJoin() // 取消该作业并且等待它结束
                println("main: Now I can quit.")
            }
        },
        CoroutinesCheckItem("运行不能取消的代码快") {
            MainScope().launch {
                val job = launch {
                    try {
                        repeat(1000) { i ->
                            println("job:I'm sleeping $i... ")
                            delay(500L)
                        }

                    } finally {
                        withContext(NonCancellable) {
                            println("job: I'm running finally")
                            delay(1000L)
                            println("job: And I've just delayed for 1 sec because I'm non-cancellable")
                        }
                    }
                }
                delay(1300L) // 延迟一段时间
                println("main: I'm tired of waiting!")
                job.cancelAndJoin() // 取消该作业并等待它结束
                println("main: Now I can quit.")
            }
        },
        CoroutinesCheckItem("超时") {
            MainScope().launch {
                withTimeout(1300L) {
                    repeat(100) { i ->
                        println("I'm sleeping $i...")
                        delay(500L)
                    }
                }
            }
        },
        CoroutinesCheckItem("超时时返回null") {
            MainScope().launch {
                val result = withTimeoutOrNull(1300L) {
                    repeat(1000) { i ->
                        println("")
                        delay(500L)
                    }
                    "Done"
                }
                println("Result is $result")
            }
        },
        CoroutinesCheckItem("异步超时和资源") {
            runBlocking {
                repeat(100_000) {
                    launch {
                        val resource = withTimeout(60) {
                            delay(50)
                            Resource()
                        }
                        resource.close()
                    }
                }
            }
            println("acquired is $acquired")
        },
        CoroutinesCheckItem("异步超时和资源 正确关闭了相关资源") {
            runBlocking {
                repeat(100_000) {
                    launch {
                        var resource: Resource? = null
                        try {
                            withTimeout(60) {
                                delay(50)
                                resource = Resource()
                            }
                        } finally {
                            resource?.close()

                        }
                    }
                }
            }
            println("acquired is $acquired")
        },
        CoroutinesCheckItem("组合挂起函数 默认顺序调用") {
            ComposingSuspendingFunctions.sequentialByDefault()
        },
        CoroutinesCheckItem("组合挂起函数 异步调用") {
            ComposingSuspendingFunctions.concurrentUsingAsync()
        },
        CoroutinesCheckItem("组合挂起函数 懒加载启动") {
            ComposingSuspendingFunctions.lazyStartedAsync()
        },
        CoroutinesCheckItem("组合挂起函数 异步风格函数") {
            ComposingSuspendingFunctions.asyncStyleFunction()
        },
        CoroutinesCheckItem("组合挂起函数 异步风格函数2") {
            ComposingSuspendingFunctions.structuredConcurrencyWithAsync()
        },
        CoroutinesCheckItem("组合挂起函数 取消始终通过协程的层次结构来进行传递") {
            ComposingSuspendingFunctions.structuredConcurrencyWithAsyncThrowError()
        },
    )
}

class CoroutinesCheckItem(
    val itemName: String,
    val action: () -> Unit
)

var acquired = 0

class Resource {
    init {
        acquired++
    }

    fun close() {
        acquired--
    }
}