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

import org.slf4j.LoggerFactory
import org.springframework.aop.IntroductionAdvisor
import org.springframework.aop.framework.ProxyFactory

/**
 * Created by iuliana.cosmina on 17/04/2022
 */
object IntroductionDemo {
    private val LOGGER = LoggerFactory.getLogger(IntroductionDemo::class.java)
    @JvmStatic
    fun main(args: Array<String>) {
        val target = Contact().apply {
            name = "John Mayer"
        }
        val advisor: IntroductionAdvisor = IsModifiedAdvisor()
        val pf = ProxyFactory().apply {
            setTarget(target)
            addAdvisor(advisor)
            isOptimize = true
        }

        val o = pf.proxy
        val proxy = o as? Contact
        val proxyInterface = o as? IsModified

        LOGGER.info("Is Contact? => {} ", proxy is Contact)
        LOGGER.info("Is IsModified? => {} ", proxy is IsModified)
        LOGGER.info("Has been modified? => {} ", proxyInterface?.isModified)
        when(proxy){ is Contact ->  proxy.name = "John Mayer" }
        LOGGER.info("Has been modified? => {} ", proxyInterface?.isModified)
        when(proxy){ is Contact ->  proxy.name = "Ben Barnes" }
        LOGGER.info("Has been modified? => {} ", proxyInterface?.isModified)
    }
}