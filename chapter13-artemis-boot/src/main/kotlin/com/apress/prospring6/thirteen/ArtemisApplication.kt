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
package com.apress.prospring6.thirteen

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean
import org.springframework.jms.support.converter.MappingJackson2MessageConverter
import org.springframework.jms.support.converter.MessageConverter
import org.springframework.jms.support.converter.MessageType
import java.io.IOException
import java.time.LocalDate
import java.util.*

/**
 * Created by iuliana on 07/12/2022
 */
@SpringBootApplication
open class ArtemisApplication {
    @Bean
    open fun messageConverter(): MessageConverter {
        val mapper = JsonMapper().apply {
            registerModule(JavaTimeModule())
        }
        val converter = MappingJackson2MessageConverter().apply {
            setTargetType(MessageType.TEXT)
            setTypeIdPropertyName("_type")
            setObjectMapper(mapper)
        }
        return converter
    }

    companion object {
        val log = LoggerFactory.getLogger(ArtemisApplication::class.java)
        @JvmStatic
        fun main(args: Array<String>) {
            try {
                SpringApplication.run(ArtemisApplication::class.java, *args).use { ctx ->
                    // Arrays.stream(ctx.getBeanDefinitionNames()).forEach(cn -> log.info(" >>> {}: {}", cn, ctx.getBean(cn).getClass()));
                    val sender =
                        ctx.getBean(
                            Sender::class.java
                        )
                    for (i in 0..9) {
                        val letter =
                            Letter("Letter no. $i", "Test", LocalDate.now(), UUID.randomUUID().toString())
                        sender.send(letter)
                    }
                    System.`in`.read()
                }
            } catch (e: IOException) {
                log.error("Problem reading keystrokes.")
            }
        }
    }
}
