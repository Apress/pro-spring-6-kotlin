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
package com.apress.prospring6.four.initmethod

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.BeanCreationException
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created by iuliana.cosmina on 20/03/2022
 */
class Singer {
    var name: String? = null
        set(value) {
            logger.info("Calling setName for bean of type {}.", Singer::class.java)
            field = value
        }

    var age = Int.MIN_VALUE
        set(value) {
            logger.info("Calling setAge for bean of type {}.", Singer::class.java)
            field = value
        }

    private fun init() {
        logger.info("Initializing bean")
        if (name == null) {
            logger.info("Using default name")
            name = DEFAULT_NAME
        }
        require(age != Int.MIN_VALUE) { "You must set the age property of any beans of type " + Singer::class.java }
    }

    override fun toString(): String {
        return "name=${name}, age=${name}"
    }

    companion object {
        private val logger = LoggerFactory.getLogger(Singer::class.java)
        private const val DEFAULT_NAME = "No Name"
    }
}

@Configuration
open class SingerConfiguration {
    @Bean(initMethod = "init")
    open fun singerOne(): Singer {
        val singer = Singer()
        singer.name = "John Mayer"
        singer.age = 43
        return singer
    }

    @Bean(initMethod = "init")
    open fun singerTwo(): Singer {
        val singer = Singer()
        singer.age = 42
        return singer
    }

    @Bean(initMethod = "init")
    open fun singerThree(): Singer {
        val singer = Singer()
        singer.name = "John Butler"
        return singer
    }
}

object InitMethodDemo {
    private val logger = LoggerFactory.getLogger(InitMethodDemo::class.java)
    @JvmStatic
    fun main(args: Array<String>) {
        val ctx = AnnotationConfigApplicationContext(SingerConfiguration::class.java)
        getBean("singerOne", ctx)
        getBean("singerTwo", ctx)
        getBean("singerThree", ctx)
    }

    fun getBean(beanName: String?, ctx: ApplicationContext): Singer? {
        return try {
            val bean = ctx.getBean(beanName) as Singer
            logger.info("Found: {}", bean)
            bean
        } catch (ex: BeanCreationException) {
            logger.error("An error occured in bean configuration: " + ex.message)
            null
        }
    }
}