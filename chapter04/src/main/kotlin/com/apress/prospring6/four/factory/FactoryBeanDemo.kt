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
package com.apress.prospring6.four.factory

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import java.security.MessageDigest

/**
 * Created by iuliana.cosmina on 26/03/2022
 */
@Configuration
@ComponentScan
internal open class MessageDigestConfig {
    @Bean
    open fun shaDigest() =
        MessageDigestFactoryBean().apply {
            algorithmName = "SHA1"
        }

    @Bean
    open fun defaultDigest() = MessageDigestFactoryBean()

    @Bean
    @Throws(Exception::class)
    open fun digester() =
        MessageDigester().apply {
            digest1 = shaDigest().getObject()
            digest2 = defaultDigest().getObject()
        }
}

object FactoryBeanDemo {
    private val LOGGER = LoggerFactory.getLogger(FactoryBeanDemo::class.java)
    @JvmStatic
    fun main(args: Array<String>) {
        val ctx = AnnotationConfigApplicationContext(MessageDigestConfig::class.java)
        val digester = ctx.getBean(
            "digester",
            MessageDigester::class.java
        )
        digester.digest("Hello World!")
        LOGGER.debug("-------------------------------------")
        val factoryBean = ctx.getBean("&shaDigest") as MessageDigestFactoryBean
        try {
            val shaDigest: MessageDigest = factoryBean.getObject()!!
            LOGGER.info("Explicit use digest bean: {}", shaDigest.digest("Hello world".toByteArray()))
        } catch (ex: Exception) {
            LOGGER.error("Could not find MessageDigestFactoryBean ", ex)
        }
        ctx.close()
    }
}