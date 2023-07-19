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
import com.apress.prospring6.five.common.GreatGuitarist
import com.apress.prospring6.five.common.SimpleAroundAdvice
import com.apress.prospring6.five.common.Singer
import org.aopalliance.aop.Advice
import org.springframework.aop.Advisor
import org.springframework.aop.ClassFilter
import org.springframework.aop.Pointcut
import org.springframework.aop.framework.ProxyFactory
import org.springframework.aop.support.DefaultPointcutAdvisor
import org.springframework.aop.support.StaticMethodMatcherPointcut
import java.lang.reflect.Method

/**
 * Created by iuliana.cosmina on 10/04/2022
 */
internal class SimpleStaticPointcut : StaticMethodMatcherPointcut() {
    override fun matches(method: Method, cls: Class<*>): Boolean {
        return "sing" == method.name
    }

    override fun getClassFilter(): ClassFilter {
        return ClassFilter { cls: Class<*> -> cls == GoodGuitarist::class.java }
    }
}

object StaticPointcutDemo {
    @JvmStatic
    fun main(args: Array<String>) {
        val johnMayer = GoodGuitarist()
        val ericClapton = GreatGuitarist()
        val proxyOne: Singer
        val proxyTwo: Singer
        val pc: Pointcut = SimpleStaticPointcut()
        val advice: Advice = SimpleAroundAdvice()
        val advisor: Advisor = DefaultPointcutAdvisor(pc, advice)
        var pf = ProxyFactory()
        pf.addAdvisor(advisor)
        pf.setTarget(johnMayer)
        proxyOne = pf.proxy as Singer
        pf = ProxyFactory()
        pf.addAdvisor(advisor)
        pf.setTarget(ericClapton)
        proxyTwo = pf.proxy as Singer
        proxyOne.sing()
        proxyTwo.sing()
    }
}