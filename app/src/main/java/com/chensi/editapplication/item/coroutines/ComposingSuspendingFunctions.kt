package com.chensi.editapplication.item.coroutines

import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime


object ComposingSuspendingFunctions {

    //依照顺序执行
    fun sequentialByDefault() {
        runBlocking {
            val time = measureTimeMillis {
                val one = doSomethingUsefulOne()
                val two = doSomethingUsefulTwo()
                println("The answer is ${one + two}")

            }
            println("Completed in $time ms")
        }
    }

    fun concurrentUsingAsync() {
        runBlocking {
            val time = measureTimeMillis {
                val one = async { doSomethingUsefulOne() }
                val two = async { doSomethingUsefulTwo() }
                println("The answer is ${one.await() + two.await()}")
            }
            println("Completed in $time ms")
        }
    }

    fun lazyStartedAsync() {
        runBlocking {
            val time = measureTimeMillis {
                val one = async(start = CoroutineStart.LAZY) { doSomethingUsefulOne() }
                val two = async(start = CoroutineStart.LAZY) { doSomethingUsefulTwo() }
                one.start()
                two.start()
                println("The answer is ${one.await() + two.await()}")
            }
            println("Completed in $time ms")
        }
    }

    fun asyncStyleFunction() {
        val time = measureTimeMillis {
            val one = somethingUsefulOneAsync()
            val two = somethingUsefulTwoAsync()
            runBlocking {
                println("The answer is ${one.await() + two.await()}")
            }
        }
        println("Completed in $time ms")
    }

    fun structuredConcurrencyWithAsync() {
        runBlocking {
            val time = measureTimeMillis {
                println("The answer is ${concurrentSum()}")
            }
            println("Completed in $time ms")
        }
    }

    fun structuredConcurrencyWithAsyncThrowError() {
        runBlocking {
            try {
                failedConcurrentSum()
            } catch (e: ArithmeticException) {
                println("Computation failed with ArithmeticException")
            }
        }
    }

    private suspend fun doSomethingUsefulOne(): Int {
        delay(1000L) // pretend we are doing something useful here
        return 13
    }

    private suspend fun doSomethingUsefulTwo(): Int {
        delay(1000L) // pretend we are doing something useful here, too
        return 29
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun somethingUsefulOneAsync() = GlobalScope.async {
        doSomethingUsefulOne()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun somethingUsefulTwoAsync() = GlobalScope.async {
        doSomethingUsefulTwo()
    }

    private suspend fun concurrentSum(): Int = coroutineScope {
        val one = async { doSomethingUsefulOne() }
        val two = async { doSomethingUsefulTwo() }
        one.await() + two.await()
    }

    private suspend fun failedConcurrentSum(): Int = coroutineScope {
        val one = async<Int> {
            try {
                delay(Long.MAX_VALUE)
                42
            } finally {
                println("First child was cancelled")
            }
        }
        val two = async<Int> {
            println("Second child throws an exception")
            throw ArithmeticException()
        }
        one.await() + two.await()
    }
}