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
package com.apress.prospring6.five.pointcut

import com.apress.prospring6.five.common.GoodGuitarist
import com.apress.prospring6.five.common.SimpleAroundAdvice
import com.apress.prospring6.five.common.Singer
import org.slf4j.LoggerFactory
import org.springframework.aop.Advisor
import org.springframework.aop.ClassFilter
import org.springframework.aop.framework.ProxyFactory
import org.springframework.aop.support.DefaultPointcutAdvisor
import org.springframework.aop.support.DynamicMethodMatcherPointcut
import java.lang.reflect.Method

/**
 * Created by iuliana.cosmina on 10/04/2022
 */
internal class SimpleDynamicPointcut : DynamicMethodMatcherPointcut() {
    override fun getClassFilter(): ClassFilter {
        return ClassFilter { cls: Class<*> -> cls == GoodGuitarist::class.java }
    }

    override fun matches(method: Method, targetClass: Class<*>): Boolean {
        logger.debug("Static check for " + method.name)
        return "sing" == method.name
    }

    override fun matches(method: Method, targetClass: Class<*>, vararg args: Any): Boolean {
        logger.debug("Dynamic check for " + method.name)
        if (args.size == 0) {
            return false
        }
        val key = args[0] as String
        return key.equals("C", ignoreCase = true)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SimpleDynamicPointcut::class.java)
    }
}

object DynamicPointcutDemo {
    @JvmStatic
    fun main(args: Array<String>) {
        val target = GoodGuitarist()
        val advisor: Advisor = DefaultPointcutAdvisor(SimpleDynamicPointcut(), SimpleAroundAdvice())
        val pf = ProxyFactory()
        pf.setTarget(target)
        pf.addAdvisor(advisor)
        val proxy = pf.proxy as Singer
        proxy.sing("C")
        proxy.sing("c")
        proxy.sing("E")
        proxy.sing()
    }
}