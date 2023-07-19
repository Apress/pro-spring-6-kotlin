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
package com.apress.prospring6.eleven.boot

import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.core.env.AbstractEnvironment

/**
 * Created by iuliana.cosmina on 16/12/2021
 */
@SpringBootApplication
open class Chapter11Application {
    companion object {
        private val LOGGER = LoggerFactory.getLogger(Chapter11Application::class.java)

        @JvmStatic
        fun main(args: Array<String>) {
            System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "dev")
            val ctx = SpringApplication.run(
                Chapter11Application::class.java, *args
            )
            val singerBeanValidationService = ctx.getBean(
                SingerValidationService::class.java
            )
            val singer = Singer()
            singer.firstName = "J"
            singer.lastName = "Mayer"
            singer.genre = null
            singer.gender = null
            val violations = singerBeanValidationService.validateSinger(singer)
            if (violations.size != 2) {
                LOGGER.error("Unexpected number of violations: {}", violations.size)
            }
            violations.forEach { violation ->
                LOGGER.info(
                    "Validation error for property: {} with value: {} with error message: {}",
                    violation.getPropertyPath(), violation.invalidValue, violation.message
                )
            }
        }
    }
}