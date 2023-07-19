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
package com.apress.prospring6.eight

import com.apress.prospring6.eight.config.JpaConfig
import com.apress.prospring6.eight.entities.Singer
import com.apress.prospring6.eight.service.SingerService
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import java.util.function.Consumer

/**
 * Created by iuliana.cosmina on 02/07/2022
 */
object JPADemo {
    private val LOGGER = LoggerFactory.getLogger(JPADemo::class.java)
    @JvmStatic
    fun main(args: Array<String>) {
        AnnotationConfigApplicationContext(JpaConfig::class.java).use { ctx ->
            val singerService =
                ctx.getBean(
                    SingerService::class.java
                )
            LOGGER.info(" ----Retrieving first name using stored procedure:")
            val ben = singerService.findFirstNameByIdUsingProc(2L)
            LOGGER.info("Extracted: {}", ben)
            LOGGER.info(" ---- Listing singers:")
            singerService.findAll()
                .forEach{ s: Singer ->
                    LOGGER.info(
                        s.toString()
                    )
                }
        }
    }
}