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

import org.slf4j.Logger

/**
 * Created by iuliana on 25/10/2022
 */
class Audit(val LOGGER: Logger) {
    private val runtime = Runtime.getRuntime()
    private var prevTime: Long = 0

    @JvmOverloads
    fun printMemory(message: String? = null) {
        val memory = runtime.totalMemory() - runtime.freeMemory()
        var sb = StringBuilder(" --> ").append(Thread.currentThread().name).append(' ')
        sb = if (message == null) sb.append("Occupied memory is ")
        else sb.append(message).append(' ').append( bytesToMegabytes(memory))
        sb.append("MB")
        LOGGER.debug(sb.toString())
    }

    fun printStats(message: String?) {
        val memory = runtime.totalMemory() - runtime.freeMemory()
        val currentTime = System.currentTimeMillis()
        var sb = StringBuilder(" --> ").append(Thread.currentThread().name).append(' ')
        sb = if (message == null) sb.append("Occupied memory is ")
        else sb.append(message).append(' ').append(bytesToMegabytes(memory))
        if (prevTime > 0) {
            val seconds = (currentTime - prevTime).toFloat() / 1000
            prevTime = currentTime
            sb.append("MB, process took: ").append(seconds).append(" seconds")
        } else {
            sb.append("MB")
            prevTime = currentTime
        }
        LOGGER.debug(sb.toString())
    }

    companion object {
        private const val MEGABYTE = 1024L * 1024L
        fun bytesToMegabytes(bytes: Long) = bytes / MEGABYTE
    }
}
