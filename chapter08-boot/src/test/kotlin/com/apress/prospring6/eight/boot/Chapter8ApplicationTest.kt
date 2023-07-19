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
package com.apress.prospring6.eight.boot

import com.apress.prospring6.eight.boot.entities.Album
import com.apress.prospring6.eight.boot.entities.Instrument
import com.apress.prospring6.eight.boot.entities.Singer
import com.apress.prospring6.eight.boot.service.SingerService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.SqlMergeMode
import org.testcontainers.junit.jupiter.Testcontainers
import java.util.function.Consumer

/**
 * Created by iuliana.cosmina on 28/07/2022
 */
@ActiveProfiles("test")
@Testcontainers
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql("classpath:testcontainers/drop-schema.sql", "classpath:testcontainers/create-schema.sql")
@SpringBootTest(classes = [Chapter8Application::class])
class Chapter8ApplicationTest {
    @Autowired
    var singerService: SingerService? = null

    @Test
    @DisplayName("should return all singers")
    fun testFindAllWithJdbcTemplate() {
        val singers: List<Singer> = singerService!!.findAll()
            .peek{ singer: Singer ->
                    LOGGER.info(
                        singer.toString()
                    )
                }.toList()
        Assertions.assertEquals(3, singers.size)
    }

    @Test
    @DisplayName("should return all singers with albums")
    fun testFindAllWithAlbum() {
        val singers: List<Singer> = singerService!!.findAllWithAlbum()
            .peek{ s: Singer ->
                    LOGGER.info(s.toString())
                    s.albums.forEach{ a: Album ->
                        LOGGER.info(
                            "\tAlbum:$a"
                        )
                    }
                    s.instruments.forEach{ i: Instrument ->
                        LOGGER.info(
                            "\tInstrument: " + i.instrumentId
                        )
                    }
                }.toList()
        Assertions.assertEquals(3, singers.size)
    }

    companion object {
        private val LOGGER = LoggerFactory.getLogger(Chapter8ApplicationTest::class.java)
    }
}