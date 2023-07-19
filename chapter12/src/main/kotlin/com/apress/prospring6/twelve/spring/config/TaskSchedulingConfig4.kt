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
package com.apress.prospring6.twelve.spring.config

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.FilterType
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.util.ErrorHandler
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.RejectedExecutionHandler
import java.util.concurrent.ThreadPoolExecutor

/**
 * Created by iuliana on 29/10/2022
 */
@Configuration
@ComponentScan(
    basePackages = ["com.apress.prospring6.twelve.spring"],
    excludeFilters = [ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = [TaskSchedulingConfig::class, TaskSchedulingConfig2::class, TaskSchedulingConfig3::class]
    )]
)
@EnableScheduling
open class TaskSchedulingConfig4 {
    @Bean
    open fun taskScheduler(): TaskScheduler {
        val tpts = ThreadPoolTaskScheduler()
        tpts.poolSize = 3
        tpts.threadNamePrefix = "tsc4-"
        tpts.setErrorHandler(LoggingErrorHandler("tsc4"))
        tpts.setRejectedExecutionHandler(RejectedTaskHandler())
        return tpts
    }
}

internal class LoggingErrorHandler(private val name: String) : ErrorHandler {
    override fun handleError(t: Throwable) {
        LOGGER.error(
            "[{}]: task failed because {}",
            name, t.message, t
        )
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(LoggingErrorHandler::class.java)
    }
}

internal class RejectedTaskHandler : RejectedExecutionHandler {
    private val rejectedTasks: MutableMap<Runnable, Int> = ConcurrentHashMap()
    override fun rejectedExecution(r: Runnable, executor: ThreadPoolExecutor) {
        LOGGER.info(" >>  check for resubmission.")
        var submit = true
        if (rejectedTasks.containsKey(r)) {
            val submittedCnt = rejectedTasks[r]!!
            if (submittedCnt > 5) {
                submit = false
            } else {
                rejectedTasks[r] = rejectedTasks[r]!! + 1
            }
        } else {
            rejectedTasks[r] = 1
        }
        if (submit) {
            executor.execute(r)
        } else {
            LOGGER.error(">> Task {} cannot be re-submitted.", r.toString())
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(RejectedTaskHandler::class.java)
    }
}