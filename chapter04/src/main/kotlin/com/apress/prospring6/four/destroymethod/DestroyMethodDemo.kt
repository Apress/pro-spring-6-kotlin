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
package com.apress.prospring6.four.destroymethod

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path

/**
 * Created by iuliana.cosmina on 25/03/2022
 */
internal class FileManager {
    private lateinit var file: Path

    init {
        logger.info("Creating bean of type {}", FileManager::class.java)
        try {
            file = Files.createFile(Path.of("sample"))
        } catch (e: IOException) {
            logger.error("Could not create file")
        }
    }

    @Throws(IOException::class)
    private fun destroyMethod() {
        logger.info(
            "Calling destroyMethod() on bean of type {}",
            FileManager::class.java
        )
        Files.deleteIfExists(file)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(FileManager::class.java)
    }
}

@Configuration
internal open class DemoConfig {
    @Bean(destroyMethod = "destroyMethod")
    open fun fileManager(): FileManager {
        return FileManager()
    }
}

object DestroyMethodDemo {
    @JvmStatic
    fun main(args: Array<String>) {
        val ctx = AnnotationConfigApplicationContext(DemoConfig::class.java)
        ctx.close()
    }
}