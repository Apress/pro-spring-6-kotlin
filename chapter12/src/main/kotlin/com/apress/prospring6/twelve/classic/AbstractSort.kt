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

import org.slf4j.LoggerFactory

/**
 * Created by iuliana on 25/10/2022
 */
abstract class AbstractSort(arr: IntArray) : IntSortingTask {
    protected var name: String? = null
    protected val arr: IntArray

    init {
        this.arr = IntArray(arr.size)
        System.arraycopy(arr, 0, this.arr, 0, arr.size)
    }

    abstract override fun sort(arr: IntArray)

    override fun run() {
        val startTime = System.currentTimeMillis()
        sort(arr)
        val endTime = System.currentTimeMillis()
        val seconds = (endTime - startTime).toFloat() / 1000
        LOGGER.info("{} Sort Time: {} seconds ", name, seconds)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(AbstractSort::class.java)
    }
}
