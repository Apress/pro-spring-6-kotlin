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
package com.apress.prospring6.three.generator

import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.beans.factory.config.BeanDefinition
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.context.annotation.*
import org.springframework.stereotype.Component
import java.util.*

/**
 * Created by iuliana.cosmina on 08/03/2022
 */
object BeanNameGerneratorDemo {
    private val logger: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(BeanNameGerneratorDemo::class.java)
    @JvmStatic
    fun main(args: Array<String>) {
        val ctx = AnnotationConfigApplicationContext(BeanNamingCfg::class.java)
        ctx.beanDefinitionNames.forEach { beanName ->
            logger.debug(beanName)
        }
        try {
            val sb = ctx.getBean("simpleBean")
        } catch (nsb: NoSuchBeanDefinitionException) {
            logger.debug(" Bean '{}' could not be found.", nsb.beanName)
        }
    }
}

@Configuration
@ComponentScan(nameGenerator = SimpleBeanNameGenerator::class)
internal open class BeanNamingCfg {
    @Bean
    open fun anotherSimpleBean(): SimpleBean {
        return SimpleBean()
    }
}

@Component
internal class SimpleBean

internal class SimpleBeanNameGenerator : AnnotationBeanNameGenerator() {
    override fun buildDefaultBeanName(definition: BeanDefinition, registry: BeanDefinitionRegistry): String {
        val beanName = definition.beanClassName.substring(definition.beanClassName.lastIndexOf(".") + 1).lowercase()
        val uid = UUID.randomUUID().toString().replace("-", "").substring(0, 8)
        return "$beanName-$uid"
    }
}