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

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.core.io.Resource
import java.io.BufferedReader
import java.io.File
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors

/**
 * Created by iuliana.cosmina on 27/03/2022
 */
object ResourceDemo {
    private val LOGGER = LoggerFactory.getLogger(ResourceDemo::class.java)
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        val ctx = AnnotationConfigApplicationContext()
        val baseDir = File(System.getProperty("java.io.tmpdir"))
        val filePath = Files.createFile(Path.of(baseDir.absolutePath, "test.txt"))
        Files.writeString(filePath, "Hello World!")
        filePath.toFile().deleteOnExit()
        val res1 = ctx.getResource("file://$filePath")
        displayInfo(res1)
        val res2 = ctx.getResource("classpath:test.txt")
        displayInfo(res2)
        val res3 = ctx.getResource("http://iuliana-cosmina.com")
        displayInfo(res3)
    }

    @Throws(Exception::class)
    private fun displayInfo(res: Resource) {
        LOGGER.info("Resource class: {}", res.javaClass)
        LOGGER.info("Resource URL content: {}",
            BufferedReader(InputStreamReader(res.url.content as InputStream)).lines().parallel()
                .collect(Collectors.joining("\n"))
        )
        LOGGER.info(" -------------")
    }
}