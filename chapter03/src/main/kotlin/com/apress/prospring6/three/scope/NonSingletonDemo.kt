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
package com.apress.prospring6.three.scope

import com.apress.prospring6.three.generator.BeanNameGerneratorDemo
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

/**
 * Created by iuliana.cosmina on 10/03/2022
 */
@Component("nonSingleton")
@Scope(scopeName = "prototype")
internal class Singer(@Value("John Mayer") val name: String = "unknown") {
    override fun toString(): String {
        return "${super.toString()}, name=$name"
    }
}

object NonSingletonDemo {
    private val logger: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(BeanNameGerneratorDemo::class.java)
    @JvmStatic
    fun main(args: Array<String>) {
        val ctx = AnnotationConfigApplicationContext()
        ctx.register(Singer::class.java)
        ctx.refresh()
        val singer1 = ctx.getBean("nonSingleton", Singer::class.java)
        val singer2 = ctx.getBean("nonSingleton", Singer::class.java)
        logger.info("Identity Equal?: " + (singer1 === singer2))
        logger.info("Value Equal:? " + (singer1 == singer2))
        logger.info(singer1.toString())
        logger.info(singer2.toString())
    }
}
