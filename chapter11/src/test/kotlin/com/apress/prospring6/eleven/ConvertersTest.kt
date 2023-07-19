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
package com.apress.prospring6.eleven

import com.apress.prospring6.eleven.converter.bean.ConverterCfg
import com.apress.prospring6.eleven.domain.Blogger
import com.apress.prospring6.eleven.domain.SimpleBlogger
import com.apress.prospring6.eleven.formatter.FormattingServiceCfg
import com.apress.prospring6.eleven.property.editor.CustomEditorCfg
import com.apress.prospring6.eleven.property.editor.CustomRegistrarCfg
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.core.convert.ConversionService

/**
 * Created by iuliana on 20/08/2022
 */
class ConvertersTest {
    @Test // the old way
    fun testCustomPropertyEditorRegistrar() {
        AnnotationConfigApplicationContext(
            AppConfig::class.java,
            CustomRegistrarCfg::class.java
        ).use { ctx ->
            val springBlogger = ctx.getBean("springBlogger", Blogger::class.java)
            LOGGER.info("SpringBlogger info: {}", springBlogger)
            val awsBlogger = ctx.getBean("awsBlogger", Blogger::class.java)
            LOGGER.info("AwsBlogger info: {}", awsBlogger)
        }
    }

    @Test // also the old way
    fun testLocalDateEditor() {
        AnnotationConfigApplicationContext(AppConfig::class.java, CustomEditorCfg::class.java).use { ctx ->
            val springBlogger = ctx.getBean("springBlogger", Blogger::class.java)
            LOGGER.info("SpringBlogger info: {}", springBlogger)
            val awsBlogger = ctx.getBean("awsBlogger", Blogger::class.java)
            LOGGER.info("AwsBlogger info: {}", awsBlogger)
        }
    }

    @Test
    fun testFormattingConverterBean() {
        AnnotationConfigApplicationContext(AppConfig::class.java, ConverterCfg::class.java).use { ctx ->
            val springBlogger = ctx.getBean("springBlogger", Blogger::class.java)
            LOGGER.info("SpringBlogger info: {}", springBlogger)
            val awsBlogger = ctx.getBean("awsBlogger", Blogger::class.java)
            LOGGER.info("AwsBlogger info: {}", awsBlogger)
        }
    }

    @Test
    fun testConvertingToSimpleBlogger() {
        AnnotationConfigApplicationContext(AppConfig::class.java, ConverterCfg::class.java).use { ctx ->
            val springBlogger = ctx.getBean("springBlogger", Blogger::class.java)
            LOGGER.info("SpringBlogger info: {}", springBlogger)
            val conversionService =
                ctx.getBean(
                    ConversionService::class.java
                )
            val simpleBlogger =
                conversionService.convert(springBlogger, SimpleBlogger::class.java)
            LOGGER.info("simpleBlogger info: {}", simpleBlogger)
        }
    }

    @Test
    fun testFormattingConversionService() {
        AnnotationConfigApplicationContext(
            AppConfig::class.java,
            FormattingServiceCfg::class.java
        ).use { ctx ->
            val springBlogger = ctx.getBean("springBlogger", Blogger::class.java)
            LOGGER.info("SpringBlogger info: {}", springBlogger)
            val awsBlogger = ctx.getBean("awsBlogger", Blogger::class.java)
            LOGGER.info("AwsBlogger info: {}", awsBlogger)
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(ConvertersTest::class.java)
    }
}