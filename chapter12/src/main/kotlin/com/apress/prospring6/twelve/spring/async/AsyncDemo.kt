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
package com.apress.prospring6.twelve.spring.async

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.io.IOException
import java.util.concurrent.ExecutionException
import java.util.concurrent.Executor

/**
 * Created by iuliana on 30/10/2022
 */
@Configuration
@EnableAsync
@ComponentScan
internal open class AsyncConfig {
    @Bean
    open fun asyncService(): AsyncService {
        return AsyncServiceImpl()
    }
}

/*@Configuration
@EnableAsync
@ComponentScan
internal class AsyncConfig : AsyncConfigurer {
    override fun getAsyncExecutor(): Executor {
        val tpts = ThreadPoolTaskExecutor().apply {
            corePoolSize = 2
            maxPoolSize = 10
            threadNamePrefix = "tpte2-"
            queueCapacity = 5
        }
        tpts.initialize()
        return tpts
    }

    @Bean
    fun asyncService(): AsyncService {
        return AsyncServiceImpl()
    }
}*/


object AsyncDemo {
    private val LOGGER = LoggerFactory.getLogger(AsyncDemo::class.java)
    @Throws(IOException::class, ExecutionException::class, InterruptedException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        AnnotationConfigApplicationContext(AsyncConfig::class.java).use { ctx ->
            val asyncService = ctx.getBean("asyncService", AsyncService::class.java)
            for (i in 0..4) {
                asyncService.asyncTask()
            }
            val result1 = asyncService.asyncWithReturn("John Mayer")
            val result2 = asyncService.asyncWithReturn("Eric Clapton")
            val result3 = asyncService.asyncWithReturn("BB King")
            Thread.sleep(6000)
            LOGGER.info(" >> Result1: " + result1!!.get())
            LOGGER.info(" >> Result2: " + result2!!.get())
            LOGGER.info(" >> Result3: " + result3!!.get())
            System.`in`.read()
        }
    }
}
