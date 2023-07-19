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
package com.apress.prospring6.five.advanced

import org.slf4j.LoggerFactory
import org.springframework.aop.Advisor
import org.springframework.aop.Pointcut
import org.springframework.aop.framework.ProxyFactory
import org.springframework.aop.support.ControlFlowPointcut
import org.springframework.aop.support.DefaultPointcutAdvisor

/**
 * Created by iuliana.cosmina on 17/04/2022
 */
class ControlFlowDemo {
    fun run() {
        val target = TestBean()
        val pc: Pointcut = ControlFlowPointcut(ControlFlowDemo::class.java, "test")
        val advisor: Advisor = DefaultPointcutAdvisor(pc, SimpleBeforeAdvice())
        val pf = ProxyFactory()
        pf.setTarget(target)
        pf.addAdvisor(advisor)
        val proxy = pf.proxy as TestBean
        LOGGER.info("\tTrying normal invoke")
        proxy.foo()
        LOGGER.info("\tTrying under ControlFlowDemo.test()")
        test(proxy)
    }

    private fun test(bean: TestBean) {
        bean.foo()
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(ControlFlowDemo::class.java)
        @JvmStatic
        fun main(args: Array<String>) {
            val ex = ControlFlowDemo()
            ex.run()
        }
    }
}