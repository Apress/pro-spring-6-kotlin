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
package com.apress.prospring6.five.introduction

import org.aopalliance.intercept.MethodInvocation
import org.springframework.aop.support.DelegatingIntroductionInterceptor
import java.io.Serial
import java.lang.reflect.Method
import java.util.function.Predicate

/**
 * Created by iuliana.cosmina on 17/04/2022
 */
class IsModifiedMixin : DelegatingIntroductionInterceptor(), IsModified {
    override var isModified = false
        private set
    private val methodCache = mutableMapOf<Method, Method?>()
    private val isSetter =
        Predicate { invocation: MethodInvocation ->
            invocation.method.name.startsWith("set") && (invocation.arguments.size == 1)
        }

    @Throws(Throwable::class)
    override fun invoke(invocation: MethodInvocation): Any? {
        if (!isModified) {
            if (isSetter.test(invocation)) {
                val getter = getGetter(invocation.method)
                if (getter != null) {
                    val newVal = invocation.arguments[0]
                    val oldVal = getter.invoke(invocation.getThis())
                    isModified = if (newVal == null && oldVal == null) {
                        false
                    } else if (newVal == null || oldVal == null) {
                        true
                    } else {
                        newVal != oldVal
                    }
                }
            }
        }
        return super.invoke(invocation)
    }

    private fun getGetter(setter: Method): Method? {
        var getter = methodCache[setter]
        return getter ?:
            try {
                val getterName = setter.name.replaceFirst("set".toRegex(), "get")
                getter = setter.declaringClass.getMethod(getterName)
                synchronized(methodCache) { methodCache.put(setter, getter) }
                getter
            } catch (ex: NoSuchMethodException) {
                null
            }
    }

    companion object {
        @Serial
        private val serialVersionUID = 2L
    }
}