/*
Freeware License, some rights reserved

Copyright (c) 2023 Iuliana Cosmina

Permission is hereby granted, free of charge, to anyone obtaining a copy
of this software and associated documentation files (the "Software"),
to work with the Software within the limits of freeware distribution and fair use.
This includes the rights to use, copy, and modify the Software for personal use.
Users are also allowed and encouraged to submit corrections and modifications
to the Software for the benefit of other users.

It is not allowed to reuse,  modify, or redistribute the Software for
commercial use in any way, or for a user's educational materials such as books
or blog articles without prior permission from the copyright holder.

The above copyright notice and this permission notice need to be included
in all copies or substantial portions of the software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS OR APRESS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

*/
package com.apress.prospring6.twelve.classic

import java.util.*
import java.util.List
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

/**
 * Created by iuliana on 24/10/2022
 */
object ClassicDemo {
    //private static final Logger LOGGER = LoggerFactory.getLogger(ClassicDemo.class);
    @JvmStatic
    fun main(args: Array<String>) {
        val arr = Random().ints(100000, 0, 500000).toArray()
        //LOGGER.info("Starting Array: {} " , Arrays.toString(arr));
        val algsMonitor = ThreadPoolMonitor()
        val monitor = Thread(algsMonitor)
        val executor = ThreadPoolExecutor(2, 4, 0L, TimeUnit.MILLISECONDS, LinkedBlockingQueue())
        algsMonitor.executor = executor
        listOf(
            BubbleSort(arr),
            InsertionSort(arr),
            HeapSort(arr),
            MergeSort(arr),
            QuickSort(arr),
            ShellSort(arr)
        ).forEach(executor::execute)
        monitor.start()
        executor.shutdown()
        try {
            executor.awaitTermination(30, TimeUnit.MINUTES)
        } catch (e: InterruptedException) {
            throw RuntimeException(e)
        }
    }
}
