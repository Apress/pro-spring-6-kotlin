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
package com.apress.prospring6.twelve.simple

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.task.SimpleAsyncTaskExecutor
import org.springframework.core.task.TaskExecutor
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.stereotype.Component
import java.io.IOException
import java.util.*

/**
 * Created by iuliana on 28/10/2022
 */
@Component
internal class RandomStringPrinter(private val taskExecutor: TaskExecutor) {
    fun executeTask() {
        for (i in 0..9) {
            taskExecutor.execute {
                LOGGER.info(
                    "{}: {}",
                    i,
                    UUID.randomUUID().toString().substring(0, 8)
                )
            }
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(RandomStringPrinter::class.java)
    }
}

@Configuration
@EnableAsync
internal open class AppConfig {
    @Bean
    open fun taskExecutor(): TaskExecutor {
        return SimpleAsyncTaskExecutor()
    }
}

object SimpleAsyncTaskExecutorDemo {
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        AnnotationConfigApplicationContext(
            AppConfig::class.java,
            RandomStringPrinter::class.java
        ).use { ctx ->
            val printer = ctx.getBean(RandomStringPrinter::class.java)
            printer.executeTask()
            System.`in`.read()
        }
    }
}
