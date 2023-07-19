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

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.context.support.GenericApplicationContext
import org.springframework.core.env.MapPropertySource

/**
 * Created by iuliana.cosmina on 03/04/2022
 */
class EnvironmentTest {
    @Test
    fun testPropertySourceOne() {
        val ctx = GenericApplicationContext()
        val env = ctx.environment
        val propertySources = env.propertySources
        val appMap: MutableMap<String, Any> = HashMap()
        appMap["user.home"] = "CUSTOM_USER_HOME"
        propertySources.addLast(MapPropertySource("prospring6_MAP", appMap)) // notice the addLast
        LOGGER.info("-- Env Variables  from java.lang.System --")
        LOGGER.info("user.home: " + System.getProperty("user.home"))
        LOGGER.info("JAVA_HOME: " + System.getenv("JAVA_HOME"))
        LOGGER.info("-- Env Variables  from ConfigurableEnvironment --")
        LOGGER.info("user.home: " + env.getProperty("user.home"))
        LOGGER.info("JAVA_HOME: " + env.getProperty("JAVA_HOME"))
        ctx.close()
    }

    @Test
    fun testPropertySourceTwo() {
        val ctx = GenericApplicationContext()
        val env = ctx.environment
        val propertySources = env.propertySources
        val appMap: MutableMap<String, Any> = HashMap()
        appMap["user.home"] = "CUSTOM_USER_HOME"
        propertySources.addFirst(MapPropertySource("prospring6_MAP", appMap)) // notice the addFirst
        LOGGER.info("-- Env Variables  from java.lang.System --")
        LOGGER.info("user.home: " + System.getProperty("user.home"))
        LOGGER.info("JAVA_HOME: " + System.getenv("JAVA_HOME"))
        LOGGER.info("-- Env Variables  from ConfigurableEnvironment --")
        LOGGER.info("user.home: " + env.getProperty("user.home"))
        LOGGER.info("JAVA_HOME: " + env.getProperty("JAVA_HOME"))
        ctx.close()
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(EnvironmentTest::class.java)
    }
}