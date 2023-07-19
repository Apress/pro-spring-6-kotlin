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
package com.apress.prospring6.six.rowmapper

import com.apress.prospring6.six.config.EmbeddedJdbcConfig
import com.apress.prospring6.six.plain.records.Singer
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate
import java.util.function.Consumer
import javax.sql.DataSource

/**
 * Created by iuliana.cosmina on 10/05/2022
 */
class JdbcNamedTemplateConfigTest {
    @Test
    fun testFindAll() {
        val ctx = AnnotationConfigApplicationContext(
            TestDbCfg::class.java
        )
        val singerDao = ctx.getBean(
            "singerDao",
            SingerDao::class.java
        )
        val singers = singerDao.findAll()
        Assertions.assertEquals(3, singers!!.size)
        singers.forEach{ singer: Singer? ->
            LOGGER.info(
                singer.toString()
            )
        }
        ctx.close()
    }

    @Test
    fun testFindAllWithAlbums() {
        val ctx = AnnotationConfigApplicationContext(
            TestDbCfg::class.java
        )
        val singerDao = ctx.getBean(
            "singerDao",
            SingerDao::class.java
        )
        val singers = singerDao.findAllWithAlbums()
        Assertions.assertEquals(3, singers!!.size)
        singers.forEach{ singer: Singer? ->
            LOGGER.info(
                singer.toString()
            )
        }
        ctx.close()
    }

    @Test
    fun testInsertAllWithAlbums() {
        val ctx = AnnotationConfigApplicationContext(
            TestDbCfg::class.java
        )
        val singerDao = ctx.getBean(
            "singerDao",
            SingerDao::class.java
        )
        val singers = singerDao.findAllWithAlbums()
        Assertions.assertEquals(3, singers!!.size)
        singers.forEach{ singer: Singer? ->
            LOGGER.info(
                singer.toString()
            )
        }
        ctx.close()
    }

    @Import(EmbeddedJdbcConfig::class)
    @Configuration
    open class TestDbCfg {
        @Autowired
        var dataSource: DataSource? = null

        @Bean
        open fun namedTemplate(): NamedParameterJdbcTemplate {
            return NamedParameterJdbcTemplate(dataSource!!)
        }

        @Bean
        open fun singerDao(): SingerDao {
            return RowMapperDao().apply {
                namedTemplate = namedTemplate()
            }
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(JdbcNamedTemplateConfigTest::class.java)
    }
}