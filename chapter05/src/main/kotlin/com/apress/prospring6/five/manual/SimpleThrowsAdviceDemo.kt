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
package com.apress.prospring6.five.manual

import org.slf4j.LoggerFactory
import org.springframework.aop.ThrowsAdvice
import org.springframework.aop.framework.ProxyFactory
import java.lang.reflect.Method

/**
 * Created by iuliana.cosmina on 09/04/2022
 */
class SimpleThrowsAdviceDemo : ThrowsAdvice {
    @Throws(Throwable::class)
    fun afterThrowing(ex: Exception) {
        LOGGER.debug("***")
        LOGGER.debug("Generic Exception Capture")
        LOGGER.debug("Caught: " + ex.javaClass.name)
        LOGGER.debug("***\n")
    }

    @Throws(Throwable::class)
    fun afterThrowing(method: Method, args: Array<Any?>?, target: Any?, ex: IllegalArgumentException) {
        LOGGER.debug("***")
        LOGGER.debug("IllegalArgumentException Capture")
        LOGGER.debug("Caught: " + ex.javaClass.name)
        LOGGER.debug("Method: " + method.name)
        LOGGER.debug("***\n")
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(SimpleThrowsAdviceDemo::class.java)
        @JvmStatic
        fun main(args: Array<String>) {
            val errorBean = ErrorBean()
            val pf = ProxyFactory()
            pf.setTarget(errorBean)
            pf.addAdvice(SimpleThrowsAdviceDemo())
            val proxy = pf.proxy as ErrorBean
            try {
                proxy.errorProneMethod()
            } catch (ignored: Exception) {
            }
            try {
                proxy.otherErrorProneMethod()
            } catch (ignored: Exception) {
            }
        }
    }
}

internal class ErrorBean {
    @Throws(Exception::class)
    fun errorProneMethod() {
        throw Exception("Generic Exception")
    }

    /**
     * Overrides the original exception (i.e. change the exception thrown to the user).
     * @throws IllegalArgumentException
     */
    @Throws(IllegalArgumentException::class)
    fun otherErrorProneMethod() {
        throw IllegalArgumentException("IllegalArgument Exception")
    }
}