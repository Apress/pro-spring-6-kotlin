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
package com.apress.prospring6.four.aware

import jakarta.annotation.PreDestroy
import org.slf4j.LoggerFactory
import org.springframework.beans.BeansException
import org.springframework.beans.factory.BeanNameAware
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.support.GenericApplicationContext
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

/**
 * Created by iuliana.cosmina on 25/03/2022
 */
internal class NamedSinger : BeanNameAware {
    private var name: String? = null

    /** @Implements [BeanNameAware.setBeanName]
     */
    override fun setBeanName(beanName: String) {
        name = beanName
    }

    fun sing() {
        logger.info("Singer $name - sing()")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(NamedSinger::class.java)
    }
}

internal class FileManager {
    private var file: Path? = null

    init {
        logger.info("Creating bean of type {}", FileManager::class.java)
        try {
            file = Files.createFile(Path.of("sample"))
        } catch (e: IOException) {
            logger.error("Could not create file")
        }
    }

    @PreDestroy
    @Throws(IOException::class)
    private fun preDestroy() {
        logger.info(
            "Calling preDestroy() on bean of type {}",
            FileManager::class.java
        )
        file?.run{ Files.deleteIfExists(this) }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(FileManager::class.java)
    }
}

internal class ShutdownHookBean : ApplicationContextAware {
    private val ctx: ApplicationContext? = null

    /** @Implements [ApplicationContextAware.setApplicationContext] }
     */
    @Throws(BeansException::class)
    override fun setApplicationContext(ctx: ApplicationContext) {
        if (ctx is GenericApplicationContext) {
            ctx.registerShutdownHook()
        }
    }
}

@ComponentScan
internal class AwareConfig {
    @Bean
    fun johnMayer(): NamedSinger {
        return NamedSinger()
    }

    @Bean
    fun fileManager(): FileManager {
        return FileManager()
    }

    @Bean
    fun shutdownHookBean(): ShutdownHookBean {
        return ShutdownHookBean()
    }
}

object AwareDemo {
    @JvmStatic
    fun main(args: Array<String>) {
        val ctx = AnnotationConfigApplicationContext(AwareConfig::class.java)
        val singer = ctx.getBean(NamedSinger::class.java)
        singer.sing()

        //ctx.registerShutdownHook(); // no longer needed because of the ShutdownHookBean
    }
}