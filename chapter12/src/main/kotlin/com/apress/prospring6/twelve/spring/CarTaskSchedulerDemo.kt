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
package com.apress.prospring6.twelve.spring

import com.apress.prospring6.twelve.spring.config.TaskSchedulingConfig
import com.apress.prospring6.twelve.spring.config.TaskSchedulingConfig2
import com.apress.prospring6.twelve.spring.config.TaskSchedulingConfig3
import com.apress.prospring6.twelve.spring.config.TaskSchedulingConfig4
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.support.GenericApplicationContext
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor
import java.io.IOException

/**
 * Created by iuliana on 29/10/2022
 */
object CarTaskSchedulerDemo {
    private val LOGGER = LoggerFactory.getLogger(CarTaskSchedulerDemo::class.java)
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        initCtx(*args).use { ctx ->
            //Arrays.stream(ctx.getBeanDefinitionNames()).forEach(b -> LOGGER.info("{} of type {} ", b, ctx.getBean(b)));
            try {
                val taskScheduler =
                    ctx!!.getBean(ScheduledAnnotationBeanPostProcessor.DEFAULT_TASK_SCHEDULER_BEAN_NAME)
                LOGGER.info(" >>>> 'taskScheduler' found: {}", taskScheduler.javaClass)
            } catch (nbd: NoSuchBeanDefinitionException) {
                LOGGER.debug("No 'taskScheduler' configured!")
            }
            try {
                //Arrays.stream(ctx.getBeanDefinitionNames()).forEach(b -> LOGGER.info("{} of type {} ", b, ctx.getBean(b)));
                val taskExecutor = ctx!!.getBean("taskExecutor")
                LOGGER.info(" >>>> 'taskExecutor' found: {}", taskExecutor.javaClass)
            } catch (nbd: NoSuchBeanDefinitionException) {
                LOGGER.debug("No 'taskExecutor' configured!")
            }
            System.`in`.read()
        }
    }

    private fun initCtx(vararg args: String): GenericApplicationContext? {
        if (args.isEmpty()) {
            return AnnotationConfigApplicationContext(TaskSchedulingConfig::class.java)
        } else if (args.size == 1) {
            if (args[0] == "1") {
                return AnnotationConfigApplicationContext(TaskSchedulingConfig::class.java)
            } else if (args[0] == "2") {
                return AnnotationConfigApplicationContext(TaskSchedulingConfig2::class.java)
            } else if (args[0] == "3") {
                return AnnotationConfigApplicationContext(TaskSchedulingConfig3::class.java)
            } else if (args[0] == "4") {
                return AnnotationConfigApplicationContext(TaskSchedulingConfig4::class.java)
            }
        }
        return null
    }
}
