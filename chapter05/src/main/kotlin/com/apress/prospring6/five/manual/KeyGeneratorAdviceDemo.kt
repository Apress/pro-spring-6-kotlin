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
import org.springframework.aop.AfterReturningAdvice
import org.springframework.aop.framework.ProxyFactory
import org.springframework.lang.Nullable
import java.lang.reflect.Method
import java.util.*

/**
 * Created by iuliana.cosmina on 09/04/2022
 * Example showing an after-returning advice that throws an exception.
 * It is possible for a cryptographic key generator to generate a key
 * that is considered weak for a particular algorithm. Ideally, the key generator would check for these weak
 * keys, but since the chance of these keys arising is often very small, many generators do not check. By using
 * after-returning advice, we can advise the method that generates the key and performs this additional check.
 */
object KeyGeneratorAdviceDemo {
    private val LOGGER = LoggerFactory.getLogger(KeyGeneratorAdviceDemo::class.java)

    /**
     * As you can see, the KeyGenerator class sometimes generates weak keys, as expected, and
     * WeakKeyCheckAdvice ensures that SecurityException is raised whenever a weak key is encountered.
     * @param args
     */
    @JvmStatic
    fun main(args: Array<String>) {
        val keyGen = keyGenerator
        for (x in 0..9) {
            try {
                val key = keyGen.key
                LOGGER.info("Key: $key")
            } catch (ex: SecurityException) {
                LOGGER.error("Weak Key Generated!") // we swallow the exception for a cleaner output
            }
        }
    }

    private val keyGenerator: KeyGenerator
        get() {
            val target = KeyGenerator()
            val factory = ProxyFactory()
            factory.setTarget(target)
            factory.addAdvice(WeakKeyCheckAdvice())
            return factory.proxy as KeyGenerator
        }
}

internal class KeyGenerator {
    private val rand = Random()
    val key: Long
        get() {
            val x = rand.nextInt(3)
            return if (x == 1) {
                WEAK_KEY
            } else STRONG_KEY
        }

    companion object {
        const val WEAK_KEY = 0xFFFFFFF0000000L
        const val STRONG_KEY = 0xACDF03F590AE56L
    }
}

internal class WeakKeyCheckAdvice : AfterReturningAdvice {
    @Throws(Throwable::class)
    override fun afterReturning(
        @Nullable returnValue: Any?,
        method: Method,
        args: Array<out Any>,
        @Nullable target: Any?
    ) {
        if (target is KeyGenerator && ("getKey" == method.name)) {
            val key = returnValue as Long
            if (key == KeyGenerator.WEAK_KEY) {
                throw SecurityException("Key Generator generated a weak key. Try again")
            }
        }
    }
}