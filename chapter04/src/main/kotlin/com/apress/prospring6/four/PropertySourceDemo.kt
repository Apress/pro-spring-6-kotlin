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
package com.apress.prospring6.four

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.StandardEnvironment
import org.springframework.core.io.support.ResourcePropertySource

/**
 * Created by iuliana.cosmina on 03/04/2022
 */
internal class AppProperty {
    @set:Autowired
    var applicationHome: String? = null

    @set:Autowired
    var userHome: String? = null

    override fun toString(): String {
        return "applicationHome=${applicationHome}, userHome=${userHome}"
    }
}

@Configuration
@PropertySource("classpath:application.properties")
internal open class PropDemoConfig {
    @Autowired
    var environment: StandardEnvironment? = null
    @PostConstruct
    open fun configPriority() {
        val rps = environment!!.propertySources.stream()
            .filter { ps: org.springframework.core.env.PropertySource<*>? -> ps is ResourcePropertySource }
            .findAny().orElse(null) as ResourcePropertySource
        environment!!.propertySources.addFirst(rps)
    }

    @Bean
    open fun appProperty(): AppProperty {
        return AppProperty()
    }
}

object PropertySourceDemo {
    private val logger = LoggerFactory.getLogger(PropertySourceDemo::class.java)
    @JvmStatic
    fun main(args: Array<String>) {
        val ctx = AnnotationConfigApplicationContext(PropDemoConfig::class.java)
        val appProperty = ctx.getBean("appProperty", AppProperty::class.java)
        logger.info("Outcome: {}", appProperty)
    }
}