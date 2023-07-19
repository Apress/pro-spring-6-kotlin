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
package com.apress.prospring6.five.performance

import org.slf4j.LoggerFactory
import org.springframework.aop.Advisor
import org.springframework.aop.MethodBeforeAdvice
import org.springframework.aop.framework.Advised
import org.springframework.aop.framework.ProxyFactory
import org.springframework.aop.support.NameMatchMethodPointcutAdvisor
import org.springframework.lang.Nullable
import java.lang.reflect.Method

/**
 * Created by iuliana.cosmina on 17/04/2022
 */
internal class NoOpBeforeAdvice : MethodBeforeAdvice {
    @Throws(Throwable::class)
    override fun before(method: Method, args: Array<out Any>, @Nullable target: Any?) {
        // no-op
    }
}

object ProxyPerfTestDemo {
    private val LOGGER = LoggerFactory.getLogger(ProxyPerfTestDemo::class.java)
    @JvmStatic
    fun main(args: Array<String>) {
        val target: SimpleBean = DefaultSimpleBean()
        val advisor = NameMatchMethodPointcutAdvisor(NoOpBeforeAdvice())
        advisor.setMappedName("advised")
        LOGGER.info("Starting tests ...")
        runCglibTests(advisor, target)
        runCglibFrozenTests(advisor, target)
        runJdkTests(advisor, target)
    }

    private fun runCglibTests(advisor: Advisor, target: SimpleBean) {
        val pf = ProxyFactory()
        pf.isProxyTargetClass = true
        pf.setTarget(target)
        pf.addAdvisor(advisor)
        val proxy = pf.proxy as SimpleBean
        val testResults = test(proxy)
        LOGGER.info(" --- CGLIB (Standard) Test results ---\n {} ", testResults)
    }

    private fun runCglibFrozenTests(advisor: Advisor, target: SimpleBean) {
        val pf = ProxyFactory()
        pf.isProxyTargetClass = true
        pf.setTarget(target)
        pf.addAdvisor(advisor)
        pf.isFrozen = true
        val proxy = pf.proxy as SimpleBean
        val testResults = test(proxy)
        LOGGER.info(" --- CGLIB (Frozen) Test results ---\n {} ", testResults)
    }

    private fun runJdkTests(advisor: Advisor, target: SimpleBean) {
        val pf = ProxyFactory()
        pf.setTarget(target)
        pf.addAdvisor(advisor)
        pf.setInterfaces(SimpleBean::class.java)
        val proxy = pf.proxy as SimpleBean
        val testResults = test(proxy)
        LOGGER.info(" --- JDK Test results ---\n {} ", testResults)
    }

    private fun test(bean: SimpleBean): TestResults {
        val testResults = TestResults()
        var before = System.currentTimeMillis()
        for (x in 0..499999) {
            bean.advised()
        }
        var after = System.currentTimeMillis()
        testResults.advisedMethodTime = after - before
        //-----
        before = System.currentTimeMillis()
        for (x in 0..499999) {
            bean.unadvised()
        }
        after = System.currentTimeMillis()
        testResults.unadvisedMethodTime = after - before
        //-----
        before = System.currentTimeMillis()
        for (x in 0..499999) {
            bean.equals(bean)
        }
        after = System.currentTimeMillis()
        testResults.equalsTime = after - before
        // ----
        before = System.currentTimeMillis()
        for (x in 0..499999) {
            bean.hashCode()
        }
        after = System.currentTimeMillis()
        testResults.hashCodeTime = after - before
        // -----
        val advised = bean as Advised
        before = System.currentTimeMillis()
        for (x in 0..499999) {
            advised.targetClass
        }
        after = System.currentTimeMillis()
        testResults.proxyTargetTime = after - before
        return testResults
    }
}