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
package com.apress.prospring6.ten

import com.apress.prospring6.ten.config.DataJpaCfg
import com.apress.prospring6.ten.entities.Album
import com.apress.prospring6.ten.service.AlbumService
import jakarta.annotation.PostConstruct
import org.hibernate.cfg.Environment
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig
import org.testcontainers.junit.jupiter.Testcontainers
import java.time.LocalDate
import java.util.*
import java.util.function.Consumer

/**
 * Created by iuliana.cosmina on 02/08/2022
 */
@Testcontainers
@Sql("classpath:testcontainers/drop-schema.sql", "classpath:testcontainers/create-schema.sql")
@SpringJUnitConfig(classes = [AlbumServiceTest.TestContainersConfig::class])
class AlbumServiceTest : TestContainersBase() {
    @Autowired
    var albumService: AlbumService? = null

    @Test
    fun testFindWithReleaseDateGreaterThan() {
        val albums: List<Album> = albumService!!
            .findWithReleaseDateGreaterThan(LocalDate.of(2010, 1, 1))
            .peek{ s: Album ->
                LOGGER.info(
                    s.toString()
                )
            }.toList()
        Assertions.assertEquals(2, albums.size)
    }

    @Test
    fun testFindByTitle() {
        val albums: List<Album> = albumService!!
            .findByTitle("The")
            .peek{ s: Album ->
                LOGGER.info(
                    s.toString()
                )
            }.toList()
        Assertions.assertEquals(1, albums.size)
    }

    @Configuration
    @Import(DataJpaCfg::class)
    open class TestContainersConfig {
        @Autowired
        var jpaProperties: Properties? = null
        @PostConstruct
        open fun initialize() {
            jpaProperties!![Environment.FORMAT_SQL] = true
            jpaProperties!![Environment.USE_SQL_COMMENTS] = true
            jpaProperties!![Environment.SHOW_SQL] = true
            jpaProperties!![Environment.STATEMENT_BATCH_SIZE] = 30
        }
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(AlbumServiceTest::class.java)
    }
}