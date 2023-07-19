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
package com.apress.prospring6.three.explicit

import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import java.util.*

/**
 * Created by iuliana.cosmina on 09/03/2022
 */
object ExplicitBeanNamingDemo {
    private val logger: org.slf4j.Logger = org.slf4j.LoggerFactory.getLogger(ExplicitBeanNamingDemo::class.java)
    @JvmStatic
    fun main(args: Array<String>) {
        val ctx = AnnotationConfigApplicationContext(BeanNamingCfg::class.java)
        ctx.beanDefinitionNames.forEach { beanName: String? ->
            logger.debug(beanName)
        }
        val simpleBeans = ctx.getBeansOfType(SimpleBean::class.java)
        simpleBeans.forEach { (k: String?, v: SimpleBean?) ->
            val aliases = ctx.getAliases(k)
            if (aliases.isNotEmpty()) {
                logger.debug("Aliases for {} ", k)
                aliases.forEach { a: String? ->
                    logger.debug("\t {}", a)
                }
            }
        }
    }
}

@Configuration
@ComponentScan
internal open class BeanNamingCfg {
    //@Bean(name="simpleBeanTwo")
    //@Bean(value= "simpleBeanTwo")
    @Bean("simpleBeanTwo")
    open fun simpleBean2(): SimpleBean {
        return SimpleBean()
    }

    //@Bean(name= {"simpleBeanThree", "three", "numero_tres"})
    //@Bean(value= {"simpleBeanThree", "three", "numero_tres"})
    @Bean("simpleBeanThree", "three", "numero_tres")
    open fun simpleBean3(): SimpleBean {
        return SimpleBean()
    }
}

@Component("simpleBeanOne")
internal class SimpleBean