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
package com.apress.prospring6.five

import com.apress.prospring6.five.advanced.SimpleBeforeAdvice
import com.apress.prospring6.five.common.GrammyGuitarist
import com.apress.prospring6.five.common.Guitar
import org.slf4j.LoggerFactory
import org.springframework.aop.Advisor
import org.springframework.aop.ClassFilter
import org.springframework.aop.framework.ProxyFactory
import org.springframework.aop.support.ComposablePointcut
import org.springframework.aop.support.DefaultPointcutAdvisor
import org.springframework.aop.support.StaticMethodMatcher
import java.lang.reflect.Method

/**
 * Created by iuliana.cosmina on 17/04/2022
 */
object ComposablePointcutDemo {
    private val LOGGER = LoggerFactory.getLogger(ComposablePointcutDemo::class.java)
    @JvmStatic
    fun main(args: Array<String>) {
        val johnMayer = GrammyGuitarist()
        val pc = ComposablePointcut(ClassFilter.TRUE, SingMethodMatcher())
        LOGGER.info("Test 1 >> ")
        var proxy = getProxy(pc, johnMayer)
        testInvoke(proxy)
        LOGGER.info("Test 2 >> ")
        pc.union(TalkMethodMatcher())
        proxy = getProxy(pc, johnMayer)
        testInvoke(proxy)
        LOGGER.info("Test 3 >> ")
        pc.intersection(RestMethodMatcher())
        proxy = getProxy(pc, johnMayer)
        testInvoke(proxy)
    }

    private fun getProxy(pc: ComposablePointcut, target: GrammyGuitarist): GrammyGuitarist {
        val advisor: Advisor = DefaultPointcutAdvisor(pc, SimpleBeforeAdvice())
        val pf = ProxyFactory()
        pf.setTarget(target)
        pf.addAdvisor(advisor)
        return pf.proxy as GrammyGuitarist
    }

    private fun testInvoke(proxy: GrammyGuitarist) {
        proxy.sing()
        proxy.sing(Guitar())
        proxy.talk()
        proxy.rest()
    }
}

internal class SingMethodMatcher : StaticMethodMatcher() {
    override fun matches(method: Method, cls: Class<*>): Boolean {
        return method.name.startsWith("si")
    }
}

internal class TalkMethodMatcher : StaticMethodMatcher() {
    override fun matches(method: Method, cls: Class<*>): Boolean {
        return "talk" == method.name
    }
}

internal class RestMethodMatcher : StaticMethodMatcher() {
    override fun matches(method: Method, cls: Class<*>): Boolean {
        return method.name.endsWith("st")
    }
}