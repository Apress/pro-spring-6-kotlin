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
package com.apress.prospring6.three.valinject

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.stereotype.Component

/**
 * Created by iuliana.cosmina on 06/03/2022
 */
@Component("injectSimpleSpEL")
class InjectSimpleSpELDemo {
    @Value("#{injectSimpleConfig.name.toUpperCase()}")
    private val name: String? = null

    @Value("#{injectSimpleConfig.age + 1}")
    private val age = 0

    @Value("#{injectSimpleConfig.height}")
    private val height = 0f

    @Value("#{injectSimpleConfig.developer}")
    private val developer = false

    @Value("#{injectSimpleConfig.ageInSeconds}")
    private val ageInSeconds: Long? = null
    override fun toString(): String {
        return """
            Name: $name
            Age: $age
            Age in Seconds: $ageInSeconds
            Height: $height
            Is Developer?: $developer
            """.trimIndent()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val ctx = AnnotationConfigApplicationContext()
            ctx.register(InjectSimpleConfig::class.java, InjectSimpleSpELDemo::class.java)
            ctx.refresh()
            val simple = ctx.getBean("injectSimpleSpEL") as InjectSimpleSpELDemo
            println(simple)
        }
    }
}

@Component("injectSimpleConfig")
internal class InjectSimpleConfig {
    val name = "John Mayer"
    val age = 40
    val height = 1.92f
    val isDeveloper = false
    val ageInSeconds = 1241401112L
}